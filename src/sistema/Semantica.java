package sistema;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retornos.Retornos;
import ast.AST;
import excecoes.ErroAnotacaoDeComando;
import excecoes.ErroComandoReadSemParametros;
import excecoes.ErroDeTipo;
import excecoes.ErroFuncaoComMesmaAnotacao;
import excecoes.ErroFuncaoNaoDeclarada;
import excecoes.ErroOperadorNot;
import excecoes.ErroTipoFuncaoIncompativel;
import excecoes.ErroVariavelDuplicada;
import excecoes.ErroVariavelNaoDelarada;

public class Semantica extends Exception{
	
	private static final long serialVersionUID = 1L;
	AST ast;
	TabelaDeSimbolos tabela;
	TypeChecker tp;
	String anotacao_funcao="";
	GeradorDeCodigo geradorDeCodigo;
	int enderecoLocal;
	
	
	public Semantica(AST ast) throws Exception{
		this.ast = ast;
		this.tabela = new TabelaDeSimbolos(ast);//porque?
		this.geradorDeCodigo = new GeradorDeCodigo(ast.getRotulo());
		this.tp = new TypeChecker(geradorDeCodigo);
		
		
		preencheTabelaSimbolos();
		tabela.imprimeDeclaracoesGlobais();
		tabela.imprimeFuncoes();
	}
	
	
	
	public void preencheTabelaSimbolos() throws Exception{
		
		for(AST filhoDoNoInicial : ast.getFilhos()){
			String rotuloAtual = filhoDoNoInicial.getRotulo();
			if(rotuloAtual == "var") 
				adicionarVariaveisATabela(filhoDoNoInicial, "", "global");
			else if(rotuloAtual == "const") 
				adicionarConstantesATabela(filhoDoNoInicial,"","global");
			else if(rotuloAtual == "function") 
				preencheTabelaFuncoes(filhoDoNoInicial);
			else if(rotuloAtual == "main") 
				preencheTabelaMain(filhoDoNoInicial);
		}
	}
	
	public void preencheTabelaMain(AST noMain) throws Exception{
		String nome = noMain.getRotulo();
		HashMap<String,Retornos> variaveis_locais = new HashMap<>();
		tabela.getFuncoes().put(nome, variaveis_locais);
		tabela.getTipo_funcaoMap().put(nome, null);
		adicionarDeclaracoesLocaisATabela(noMain, nome);
	}
	
	
	
	private void preencheTabelaFuncoes(AST noFuncao) throws Exception {
		
		String nome = noFuncao.getFilhos().get(0).getRotulo();
		String retorno = noFuncao.getFilhos().get(2).getRotulo();	
		anotacao_funcao = nome;//acumula o nome da fun��o retorno e seus parametros
		AST noParametros = new AST("sem parametros");
		if(!noFuncao.getFilhos().get(1).getFilhos().get(0).getRotulo().equals("")){
			noParametros =	noFuncao.getFilhos().get(1).getFilhos().get(0);		
		}
		
		
		//Cria uma entrada para a fun��o na tabela de s�mbolos
		enderecoLocal = 0;
		incluirParametrosEmAnotacaoFuncao(noParametros,nome,noFuncao);
		
		//incluir o tipo da funcao em outra hash chamada tipo funcao
		tabela.getTipo_funcaoMap().put(anotacao_funcao, retorno);
		
		//Adiciona as vari�veis e constantes declaradas dentro da fun��o na sua entrada na tabela de s�mbolos
		adicionarDeclaracoesLocaisATabela(noFuncao, anotacao_funcao);
		
		geradorDeCodigo.criarCabecalhoNovaFuncao(tabela.retornaDeclaracoesLocais(anotacao_funcao), nome, retorno);
		
		if(noFuncao.getFilhos().get(0)!= null){
			for(AST noComandosDentrDaFuncao : noFuncao.getFilhos()){
				
				String rotuloAtual = noComandosDentrDaFuncao.getRotulo();
				if(rotuloAtual.equals("=")) 
					testaAtribuicao(noComandosDentrDaFuncao, anotacao_funcao);
				else if(rotuloAtual.equals("print")){
					AST filhoNoPrint = noComandosDentrDaFuncao.getFilhos().get(0);
					testaPrint(filhoNoPrint, anotacao_funcao);
				}else if(rotuloAtual.equals("read")){
					AST filhoNoRead = noComandosDentrDaFuncao.getFilhos().get(0);
					if(filhoNoRead != null)
						testaScan(filhoNoRead, anotacao_funcao);
					else throw new ErroComandoReadSemParametros("O comando read sem par�metros em :"+noComandosDentrDaFuncao.getLinha(), 
							noComandosDentrDaFuncao.getLinha());
				}else if(rotuloAtual.equals("if")){
					testaIf(noComandosDentrDaFuncao, anotacao_funcao);
				}
				else if(rotuloAtual.equals("for")) testaComandos(noComandosDentrDaFuncao, anotacao_funcao);
				else if(rotuloAtual.equals("while")) testaComandos(noComandosDentrDaFuncao, anotacao_funcao);
				else if(rotuloAtual.equals("chamada_funcao")) testaChamadaFuncao(noComandosDentrDaFuncao, anotacao_funcao);
				
			}
		}else throw new ErroAnotacaoDeComando("O comando "+noFuncao.getRotulo()+" com problema na anotacao em: "+noFuncao.getLinha(),
				noFuncao.getLinha());
		
		geradorDeCodigo.finalizarFuncao(nome);
		
	}
	
	


	
	private void incluirParametrosEmAnotacaoFuncao(AST noParametros, String nome, AST noFuncao) throws Exception{
		
		HashMap<String, Retornos> variaveis_locais = new HashMap<>();
		
		//Se s� existe um par�metro
		if(noParametros.getRotulo() != "," && !noParametros.getRotulo().equals("sem parametros")){
			
			String tipo = noParametros.getRotulo();
			String ID = noParametros.getFilhos().get(0).getRotulo();
			String parametro =  "_"+tipo;
			anotacao_funcao+=parametro;
			
			Retornos retornosParametros = new Retornos(); //para criar uma inst�ncia de uma vari�vel
			//mandada como par�metro
			
			//Seta o parametro da funcao como uma variavel local
			retornosParametros.setTipo(tipo);
			retornosParametros.setValor(enderecoLocal);
			retornosParametros.setParametro(true);
			enderecoLocal = enderecoLocal+1;

			variaveis_locais.put(ID+"_var", retornosParametros);
			//return;//porque?
			
		}else{//Existe mais de um par�metro
			//Ao final desta fun��o a vari�vel anotacao_funcao estar� completa
			//passa para variaveis_locais as variaveis_locais(par�metros) inseridas dentro do m�todo
			//gerarAnotacaoFuncao
			variaveis_locais = gerarAnotacaoFuncao(noParametros,variaveis_locais);
		}
		
		
		if(!tabela.getFuncoes().containsKey(anotacao_funcao)){//N�o existe fun��o semelhante
			
			tabela.getFuncoes().put(anotacao_funcao, variaveis_locais);//inclui na tabela de s�mbolos
		
		}else throw new ErroFuncaoComMesmaAnotacao("Fun��o "+
												  /*anotacao_funcao.substring(0, nome.length())+*/nome+
												  " j� declarada "+
												  "com a mesma anota��o, linha:"+
												  noFuncao.getLinha(),
												  noFuncao.getLinha());
	}
	
	
	public HashMap<String, Retornos> gerarAnotacaoFuncao(AST param, HashMap<String, Retornos> variaveis_locais) throws ErroVariavelDuplicada, ErroFuncaoComMesmaAnotacao {
		
		//Se ja encontrou um par�metro
		if(param.getRotulo() != "," && !param.getRotulo().equals("sem parametros")){
			

			String tipo = param.getRotulo();
			String ID = param.getFilhos().get(0).getRotulo();
			
			//verifica se j� n�o existe uma declara��o de par�metro com o mesmo nome na tabela
			//de s�mbolos da
			if(!variaveis_locais.containsKey(ID)){
				
				String parametro = "_"+tipo;
				
				anotacao_funcao+=parametro;
				
				Retornos retornosParametros = new Retornos(); //para criar uma inst�ncia da vari�vel
				//mandada como par�metro
				
				//Seta o parametro da funcao como uma variavel local
				retornosParametros.setTipo(tipo);
				retornosParametros.setValor(enderecoLocal);
				retornosParametros.setParametro(true);
				
				enderecoLocal = enderecoLocal+1;
				
				variaveis_locais.put(ID+"_var", retornosParametros);
			//caso contr�rio gerar� exce��o de vari�vel j� declarada
			}else throw new ErroVariavelDuplicada("J� existe um par�metro com o nome "+ID, param.getLinha());
			
		//Se ainda n�o encontrou o par�metro	
		}else if(!param.getRotulo().equals("sem parametros")){
			//Percorre a subarvore da esquerda em busca de um par�metro
			gerarAnotacaoFuncao(param.getFilhos().get(0), variaveis_locais);
			//Percorre a subarvore da direita em busca de uma v�rgula ou outro par�metro
			gerarAnotacaoFuncao(param.getFilhos().get(1), variaveis_locais);
			
		}
			return variaveis_locais;
	}
		
	
	public void adicionarDeclaracoesLocaisATabela(AST no, String anotacao) throws Exception{
		
		for(AST f : no.getFilhos()){
			
			if(f.getRotulo() == "var") adicionarVariaveisATabela(f, anotacao, "local");
			else if(f.getRotulo() == "const") adicionarConstantesATabela(f, anotacao, "local");
			
		}
		
	}
	
	
	public void adicionarVariaveisATabela(AST noDeclaracaoDeVariavel, String chave, String tipo_declaracao) throws Exception{
		
		String rotulo;
		String tipo = "";
		Map<String, Retornos> variaveisNaTabelaDeSimbolos = null;
		Retornos var = new Retornos();
		
		//Verifica em qual dos dois HashMaps ser� inserida vari�vel
		if(tipo_declaracao.equals("global")){
			variaveisNaTabelaDeSimbolos = tabela.getDeclaracoes_globais();
		}else{
			variaveisNaTabelaDeSimbolos = tabela.getFuncoes().get(chave);
		}
		
		//Pode ser que haja uma declara��o aninhada de m�ltiplas vari�veis
		for(AST noElementoDaDeclaracaoDeVariavel : noDeclaracaoDeVariavel.getFilhos()){
			
			rotulo = noElementoDaDeclaracaoDeVariavel.getRotulo();
			
			if(rotulo.equals("int") ||
			   rotulo.equals("real") ||
			   rotulo.equals("string") ||
			   rotulo.equals("bool")){
				
				tipo = rotulo;
				
			}else{
				//N�o � uma declara��o de v�rias vari�veis
				if(rotulo.equals(",") == false){
					//N�o existe nenhuma chave com o nome desta vari�vel
					if(!variaveisNaTabelaDeSimbolos.containsKey(rotulo+"_var") &&
					   !variaveisNaTabelaDeSimbolos.containsKey(rotulo+"_const")){
						
						var.setTipo(tipo);
						var.setValor(enderecoLocal);
						var.setParametro(false);
						enderecoLocal = enderecoLocal+1;
						
						if(tipo_declaracao.equals("global"))
							
							tabela.getDeclaracoes_globais().put(rotulo+"_var", var);
						
						else 
							
							tabela.getFuncoes().get(chave).put(rotulo+"_var", var);
						
					}else{
						
						throw new ErroVariavelDuplicada("Vari�vel ou constante \'"+
														rotulo+
														"\' duplicada, linha: "+
						                                noElementoDaDeclaracaoDeVariavel.getLinha(),
						                                noElementoDaDeclaracaoDeVariavel.getLinha());
						
					}
						
				}else{
					//mais de uma vari�vel declarada na mesma linha
					adicionaDelaracoesAninhadasATabela(noElementoDaDeclaracaoDeVariavel,tipo, chave, tipo_declaracao);
				}
			}		
		}
	}
	
	
	public void adicionaDelaracoesAninhadasATabela(AST noElementoDaDeclaracaoDeVariavel, String tipo, String chave, String tipo_declaracao) throws Exception{
		//variavel var para armazenar o tipo e o valor da variavel percorrida no n�
		Retornos var = new Retornos();
		Map<String, Retornos> variaveis = null;
		
		if(tipo_declaracao.equals("global")){
			//se o m�todo foi chamado em preencheTabela, onde as declara��es s�o globais
			//armazena as declara��es j� existentes na tabela de s�mbolos em variaveis
			variaveis = tabela.getDeclaracoes_globais();
			
		}else{
			//se o m�todo foi chamado em adicionarDeclaracoesLocaisATabela, onde as declara��es s�o globais
			//armazena as declara��es j� existentes na tabela de s�mbolos em variaveis
			variaveis = tabela.getFuncoes().get(chave);
			
		}
		
		for(AST p : noElementoDaDeclaracaoDeVariavel.getFilhos()){
			if(p.getRotulo() != ","){
				//verifica se j� cont�m a declara��o da vari�vel em declara��es_globais, faz tanto para var
				//quanto para constante
				if(!variaveis.containsKey(p.getRotulo()+"_var") && !variaveis.containsKey(p.getRotulo()+"_const")){
					var.setTipo(tipo);
					if(tipo_declaracao.equals("global"))
						tabela.getDeclaracoes_globais().put(p.getRotulo()+"_var", var);
					else tabela.getFuncoes().get(chave).put(p.getRotulo()+"_var", var);
				}else{
					//erro de vari�vel ou constante duplicada
					throw new ErroVariavelDuplicada("Nome de vari�vel ou constante duplicada na linha : "+ p.getLinha(), p.getLinha());
				}
			}else{
				adicionaDelaracoesAninhadasATabela(p, tipo, chave, tipo_declaracao);
			}
		}
	}
	
		public void adicionarConstantesATabela(AST noConstante, String anotacao_funcao, String tipo_declaracao) throws Exception{
		
		//cria inst�ncia cons do tipo dos valores das declaracoes globais da tabela
		//de s�mbolos
		Retornos cons = new Retornos();
		Map<String, Retornos> constantes = null;
		
		//verifica se a declaracao de constante � global ou local
		if(tipo_declaracao.equals("global")){
			constantes = tabela.getDeclaracoes_globais();
		}else{
			constantes = tabela.getFuncoes().get(anotacao_funcao);
		}
		//captura o nome, o tipo e o valor na ast
		String nome = noConstante.getFilhos().get(0).getRotulo();
		String tipo = noConstante.getFilhos().get(1).getRotulo();
		String valor = noConstante.getFilhos().get(2).getRotulo();
		
		//verifica se j� existe esta inst�ncia em declarada como constante ou vari�vel
		if(!constantes.containsKey(nome+"_const") && !constantes.containsKey(nome+"_var")){
			cons = tp.checaTipoConstanteGlobal(tipo, valor, noConstante);
			if(tipo_declaracao.equals("global"))
				tabela.getDeclaracoes_globais().put(nome+"_const", cons);
			else tabela.getFuncoes().get(anotacao_funcao).put(nome+"_const", cons);
			
			cons.setParametro(false);
		}else{
			throw new ErroVariavelDuplicada("Nome de vari�vel ou constante duplicada na linha : "+ noConstante.getLinha(), noConstante.getLinha());
		}
	}
	
	
	public void testaAtribuicao(AST noAtribuicao, String anotacao_funcao) throws ErroVariavelNaoDelarada, 
				ErroOperadorNot, ErroDeTipo, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, IOException{
		
		//Problema: Somente o ID do lado esquerdo da atribui��o est� sendo verificado como vari�vel existente
		//Consertado erro de vari�vel n�o declarada lado direito da atribui��o
		
		String tipoDeclarado;//armazena o tipo declarado em escopo global ou local
		String ID = noAtribuicao.getFilhos().get(0).getRotulo();
		
		if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(ID, anotacao_funcao)){
			
			Retornos variavelLocal = tabela.retornaVariavelLocal(anotacao_funcao, ID);
			tipoDeclarado = variavelLocal.getTipo();
			AST subArvoreDireitaDaDeclaracao = noAtribuicao.getFilhos().get(1);
			verificaExpressaoEmAtribuicao(subArvoreDireitaDaDeclaracao, anotacao_funcao, tipoDeclarado);
			
		}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(ID)){
			
			Retornos variavelGlobal = tabela.getDeclaracoes_globais().get(ID+"_var");
			tipoDeclarado = variavelGlobal.getTipo();
			AST subArvoreDireitaDaDeclaracao = noAtribuicao.getFilhos().get(1);
			verificaExpressaoEmAtribuicao(subArvoreDireitaDaDeclaracao,anotacao_funcao,tipoDeclarado);
			
		}else {
			
			throw new ErroVariavelNaoDelarada("Variavel "+
											  ID+
											  ""+//porque?
											  " n�o declarada em : "+
											  noAtribuicao.getLinha(),
											  noAtribuicao.getLinha());
			
		}
	}
	
	
	public void verificaExpressao(AST noExpr, String anotacao_funcao, 
			String simboloRelacional, boolean entrouEmExprAritmetica, String tipoRetorno) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		
		String rotuloAtual = noExpr.getRotulo();
		
		if(rotuloAtual.equals("+") ||
		   rotuloAtual.equals("-") ||
		   rotuloAtual.equals("*") ||
		   rotuloAtual.equals("/") ){
			
			AST subarvoreEsquerda = noExpr.getFilhos().get(0);
			AST subarvoreDireita = noExpr.getFilhos().get(1);
			
			verificaExpressao(subarvoreEsquerda, anotacao_funcao, simboloRelacional, true,tipoRetorno);
			verificaExpressao(subarvoreDireita, anotacao_funcao, simboloRelacional, true,tipoRetorno);
			
		}else if(rotuloAtual.equals("!")) {
			verificaOperadorRelacional(noExpr, anotacao_funcao,"bool");
			
		}else if(rotuloAtual.equals("chamada_funcao") && !tipoRetorno.equals("")){
			//tp.checarChamadaFuncao()
			String anotacaoChamaFuncao = montarAnotacaoChamadaFuncao(noExpr, anotacao_funcao);
			if(tabela.getTipo_funcaoMap().containsKey(anotacaoChamaFuncao)){
				String tipoFuncaoChamada = tabela.getTipo_funcaoMap().get(anotacaoChamaFuncao);
				if(tipoRetorno == tipoFuncaoChamada) return;
				else if(tipoRetorno.equals("real") && tipoFuncaoChamada.equals("int")) return;
				else throw new ErroTipoFuncaoIncompativel("A funcao com a anotacao "+anotacaoChamaFuncao+
						" � do tipo "+tipoFuncaoChamada+" em :"+noExpr.getLinha(), noExpr.getLinha());
			}else throw new ErroFuncaoNaoDeclarada("A fun��o com anota��o "+anotacaoChamaFuncao
					+" n�o existe ou parametros de tipos incompat�veis em : "+noExpr.getLinha(),noExpr.getLinha());
		}else if(rotuloAtual.equals("chamada_funcao") && tipoRetorno.equals("")){
			//tp.checarChamadaFuncao()
			String anotacaoChamaFuncao = montarAnotacaoChamadaFuncao(noExpr, anotacao_funcao);
			if(tabela.getTipo_funcaoMap().containsKey(anotacaoChamaFuncao)){
				return;
			}else throw new ErroFuncaoNaoDeclarada("A fun��o com anota��o "+anotacaoChamaFuncao
					+" n�o existe ou parametros de tipos incompat�veis em : "+noExpr.getLinha(),noExpr.getLinha());
		}else if(!simboloRelacional.equals("")){
			tp.checaTipoExpressaoEmRelacional(tabela, anotacao_funcao, noExpr, simboloRelacional, entrouEmExprAritmetica);
		}else{	
			
			tp.checaTipoExpressaoEmPrint(tabela, anotacao_funcao, noExpr, entrouEmExprAritmetica);
				
			return;
			
		}
	}
	
	
	public void verificaOperadorRelacional(AST noOperador, String anotacao_funcao, String tipoRetoro) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		if(noOperador != null){
			String rotuloAtual = noOperador.getRotulo();
			AST subArvoreEsquerda, subArvoreDireita, noFilhoOperadorNot;
			if(rotuloAtual.equals("==") ||
			   rotuloAtual.equals("!=") ||
			   rotuloAtual.equals("<>") ||
			   rotuloAtual.equals(">")  ||
			   rotuloAtual.equals(">=") ||
			   rotuloAtual.equals("<")  ||
			   rotuloAtual.equals("<=")){
				subArvoreEsquerda = noOperador.getFilhos().get(0);
				subArvoreDireita = noOperador.getFilhos().get(1);
				verificaExpressao(subArvoreEsquerda, anotacao_funcao, rotuloAtual, false,tipoRetoro);
				verificaExpressao(subArvoreDireita, anotacao_funcao,  rotuloAtual, false,tipoRetoro);
				//"" significa que a express�o n�o est�
				// vinculada a atribui��o, sendo assim a 
				// a passagem do tipo do lado esquerdo de uma atribui��o n�o � poss�vel
				
			}else if(rotuloAtual.equals("!")){
				//se o r�tulo for o operador not, ent�o verifica se o filho dele � um
				//operador de compara��o, como '==', '!=', '!', ou uma vari�vel
				noFilhoOperadorNot = noOperador.getFilhos().get(0);
				verificaOperadorRelacional(noFilhoOperadorNot, anotacao_funcao,"bool");
			}else if(rotuloAtual.equals("chamada_funcao")){
				String anotacao = montarAnotacaoChamadaFuncao(noOperador, anotacao_funcao);
				if(tabela.getFuncoes().containsKey(anotacao)){
					String tipo = tabela.getTipo_funcaoMap().get(anotacao);
					String nomeChamadaFuncao = noOperador.getFilhos().get(0).getRotulo();
					if(tipo == "bool") return;
					else throw new ErroTipoFuncaoIncompativel("A funcao "+nomeChamadaFuncao+" possui tipo "+tipo+" em : "
							+noOperador.getLinha(), noOperador.getLinha());
				}
			}else{
				//se acontecer de a opera��o relacional tiver apenas uma vari�vel ou 'TRUE', 'FALSE', ou
				//um valor qualquer, ent�o checa se � boolean
				tp.checaTipoValorOperacaoRelacionalBoolean(noOperador, anotacao_funcao, tabela);
			}
		}
	}
	
	public void verificaExpressaoEmAtribuicao(AST no, String anotacao_funcao, String tipo) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, IOException{
		
		String rotuloAtual = no.getRotulo();
		
		if(rotuloAtual.equals("+") ||
		   rotuloAtual.equals("-") ||
		   rotuloAtual.equals("*") ||
		   rotuloAtual.equals("/") ){
			
			AST subarvoreEsquerda = no.getFilhos().get(0);
			AST subarvoreDireita = no.getFilhos().get(1);
			
			// se esse passo n�o gerar exce��o ent�o o operando da esquerda � do tipo certo
			verificaExpressaoEmAtribuicao(subarvoreEsquerda, anotacao_funcao, tipo);
			// se esse passo n�o gerar exce��o ent�o o operando da direita � do tipo certo
			verificaExpressaoEmAtribuicao(subarvoreDireita, anotacao_funcao, tipo);
			
			geradorDeCodigo.criarOperacaoMatematica(rotuloAtual, tipo);
			
		}else if(rotuloAtual.equals("!")) {
			
			tp.checaTipoExprNot(no, tipo);
			
			verificaOperadorRelacional(no, anotacao_funcao, "bool");
			
			return;
			
		}else if(rotuloAtual.equals("chamada_funcao")){
			//tp.checarChamadaFuncao()
			String anotacaoChamaFuncao = montarAnotacaoChamadaFuncao(no, anotacao_funcao);
			if(tabela.getTipo_funcaoMap().containsKey(anotacaoChamaFuncao)){
				String tipoFuncaoChamada = tabela.getTipo_funcaoMap().get(anotacaoChamaFuncao);
				if(tipo == tipoFuncaoChamada) return;
				else if(tipo.equals("real") && tipoFuncaoChamada.equals("int")) return;
				else throw new ErroTipoFuncaoIncompativel("A funcao com a anotacao "+anotacaoChamaFuncao+
						" � do tipo "+tipoFuncaoChamada+" em :"+no.getLinha(), no.getLinha());
			}else throw new ErroFuncaoNaoDeclarada("A fun��o com anota��o "+anotacaoChamaFuncao
					+" n�o existe ou parametros de tipos incompat�veis em : "+no.getLinha(),no.getLinha());
		}else{
			tp.checaTipoExpressaoAtribuicao(tabela, anotacao_funcao, no, tipo);
			return;
			
		}
	}
	
	public String montarAnotacaoChamadaFuncao(AST noChamadaFuncao, String anotacao) throws ErroVariavelNaoDelarada, ErroOperadorNot, ErroDeTipo, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		String nomeFuncao = noChamadaFuncao.getFilhos().get(0).getRotulo();
		String anotacaoMontada = "";
		AST filhoLadoDireito = noChamadaFuncao.getFilhos().get(1);
		String tipo = montarParametrosAnotacaoFuncao(filhoLadoDireito, anotacao);
		anotacaoMontada = nomeFuncao+tipo;
		return anotacaoMontada;
	}
	
	public String montarParametrosAnotacaoFuncao(AST parametroChamadaFuncao, String anotacao) throws ErroDeTipo, ErroVariavelNaoDelarada, ErroOperadorNot, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		AST filhoEsquerdo, filhoDireito;
		String rotulo = parametroChamadaFuncao.getRotulo(), tipo = "", anotacaoMontada = "";
		
		if(rotulo != "," && !rotulo.equals("")){
			if( !rotulo.equals("+") &&
				!rotulo.equals("-") &&
				!rotulo.equals("/") && 
				!rotulo.equals("*") &&
				!rotulo.equals("!")){
				if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(rotulo, 
						anotacao)){
					tipo = tabela.retornaConstanteLocal(anotacao, rotulo).getTipo();
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(rotulo, 
						anotacao)){
					tipo = tabela.retornaVariavelLocal(anotacao, rotulo).getTipo();
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(rotulo)){
					tipo = tabela.getDeclaracoes_globais().get(rotulo+"_const").getTipo();
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(rotulo)){
					tipo = tabela.getDeclaracoes_globais().get(rotulo+"_var").getTipo();
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(rotulo.charAt(0)=='0' ||
						 rotulo.charAt(0)=='1' ||
						 rotulo.charAt(0)=='2' ||
						 rotulo.charAt(0)=='3' ||
						 rotulo.charAt(0)=='4' ||
						 rotulo.charAt(0)=='5' ||
						 rotulo.charAt(0)=='6' ||
						 rotulo.charAt(0)=='7' ||
						 rotulo.charAt(0)=='8' ||
						 rotulo.charAt(0)=='9'){
					if(rotulo.contains(".")){
						tipo = "real";
						anotacaoMontada = "_"+tipo;
						return anotacaoMontada;
					}else{
						tipo = "int";
						anotacaoMontada = "_"+tipo;
						return anotacaoMontada;
					}
				}else if(rotulo.charAt(0)=='"' && 
						rotulo.charAt(rotulo.length()-1)=='"'){
					tipo ="string";
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(rotulo.equals("TRUE") || rotulo.equals("FALSE")){
					tipo ="bool";
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else if(rotulo.equals("chamada_funcao")){
					String anotacaoFuncaoAninhada = montarAnotacaoChamadaFuncao(parametroChamadaFuncao, anotacao);
					tipo = tabela.getTipo_funcaoMap().get(anotacaoFuncaoAninhada);
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else throw new ErroVariavelNaoDelarada("A variavel "+rotulo+" "
						+ " n�o foi declarada em : "+parametroChamadaFuncao.getLinha(), parametroChamadaFuncao.getLinha());
				
			}else if( rotulo.equals("+") ||
					  rotulo.equals("-") ||
					  rotulo.equals("/") || 
					  rotulo.equals("*") ||
					  rotulo.equals("!")){
				verificaExpressao(parametroChamadaFuncao, anotacao, "", false,"real");
				if(rotulo.equals("!")){
					tipo = "bool";
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}else{
					tipo = retornaTipoComumExpressaoChamadaFuncao(parametroChamadaFuncao, "", anotacao);
					anotacaoMontada = "_"+tipo;
					return anotacaoMontada;
				}
			}
		}else if(rotulo.equals(",")){
			filhoEsquerdo = parametroChamadaFuncao.getFilhos().get(0);
			filhoDireito = parametroChamadaFuncao.getFilhos().get(1);
			anotacaoMontada+= montarParametrosAnotacaoFuncao(filhoEsquerdo, anotacao);
			anotacaoMontada+= montarParametrosAnotacaoFuncao(filhoDireito, anotacao);
		}
		return anotacaoMontada;
	}
	
	public String retornaTipoComumExpressaoChamadaFuncao(AST expr, String tipo, String anotacao) throws ErroDeTipo, ErroVariavelNaoDelarada{
		String rotulo = expr.getRotulo();
		if(rotulo.equals("+") ||
			rotulo.equals("-")||
			rotulo.equals("/")||
			rotulo.equals("*")){
			AST filhoEsquerdo, filhoDireito;
			filhoEsquerdo = expr.getFilhos().get(0);
			filhoDireito = expr.getFilhos().get(1);
			if(!tipo.equals("real"))
				tipo = retornaTipoComumExpressaoChamadaFuncao(filhoEsquerdo, tipo, anotacao);
			if(!tipo.equals("real"))
				tipo = retornaTipoComumExpressaoChamadaFuncao(filhoDireito, tipo, anotacao);
		}else if((rotulo.charAt(0)=='0' ||
				 rotulo.charAt(0)=='1' ||
				 rotulo.charAt(0)=='2' ||
				 rotulo.charAt(0)=='3' ||
				 rotulo.charAt(0)=='4' ||
				 rotulo.charAt(0)=='5' ||
				 rotulo.charAt(0)=='6' ||
				 rotulo.charAt(0)=='7' ||
				 rotulo.charAt(0)=='8' ||
				 rotulo.charAt(0)=='9')){
			if(rotulo.contains(".")){
				tipo = "real";
				return tipo;
			}else{
				tipo ="int";
				return tipo;
			}
		}else if(!rotulo.equals("TRUE") &&
				 !rotulo.equals("FALSE")&&
				 (rotulo.charAt(0)!= '"' && rotulo.charAt(rotulo.length()-1)!='"')){
			if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(rotulo, 
					anotacao)){
				tipo = tabela.retornaConstanteLocal(anotacao, rotulo).getTipo();
				return tipo;
			}else if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(rotulo, 
					anotacao)){
				tipo = tabela.retornaVariavelLocal(anotacao, rotulo).getTipo();
				return tipo;
			}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(rotulo)){
				tipo = tabela.getDeclaracoes_globais().get(rotulo+"_const").getTipo();
				return tipo;
			}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(rotulo)){
				tipo = tabela.getDeclaracoes_globais().get(rotulo+"_var").getTipo();
				return tipo;
			}else throw new ErroVariavelNaoDelarada("A vari�vel "+rotulo+" n�o declarada em : " 
					+expr.getLinha(), expr.getLinha());
		}else throw new ErroDeTipo("O valor "+rotulo+" com tipo incompat�vel para express�o em : "
				+expr.getLinha(), expr.getLinha());
		return tipo;
	}
	
	
	public void testaPrint(AST noPrint, String anotacao_funcao) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		String RotulofilhoDeNoPrint = noPrint.getRotulo();
		if(!RotulofilhoDeNoPrint .equals(",") && !RotulofilhoDeNoPrint.equals("")){
			verificaExpressao(noPrint, anotacao_funcao, "", false,"");
			return;
		}else if(RotulofilhoDeNoPrint.equals(",")){
			AST noExprDeNoPrintEsquerda = noPrint.getFilhos().get(0);
			AST noExprDeNoPrintDireita = noPrint.getFilhos().get(1);
			testaPrint(noExprDeNoPrintEsquerda, anotacao_funcao);
			testaPrint(noExprDeNoPrintDireita, anotacao_funcao);
		}else return;
	}
	
	public void testaReturn(AST noReturn, String anotacao) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, IOException{
		AST expr = noReturn.getFilhos().get(0);
		String tipoFuncao = tabela.getTipo_funcaoMap().get(anotacao);
		verificaExpressaoEmAtribuicao(expr, anotacao, tipoFuncao);
	}
	
	public void testaScan(AST noRead, String anotacao_funcao) throws ErroVariavelNaoDelarada {
		String rotulo = noRead.getRotulo();
		if(!rotulo .equals(",") && !rotulo.equals("")){
			tp.checaRead(tabela, noRead, anotacao_funcao);
			return;
		}else if(rotulo.equals(",")){
			AST noExprDeNoPrintEsquerda = noRead.getFilhos().get(0);
			AST noExprDeNoPrintDireita = noRead.getFilhos().get(1);
			testaScan(noExprDeNoPrintEsquerda, anotacao_funcao);
			testaScan(noExprDeNoPrintDireita, anotacao_funcao);
		}else return;
		
	}
	
	public void testaElse(AST noElse, String anotacao_funcao) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, ErroComandoReadSemParametros, ErroAnotacaoDeComando, IOException{
		String rotuloAtual;
		if(noElse.getFilhos().get(0) != null){
			for(AST filhosComandoElse : noElse.getFilhos()){
				rotuloAtual = filhosComandoElse.getRotulo();
				if(rotuloAtual.equals("print")) testaPrint(filhosComandoElse.getFilhos().get(0), anotacao_funcao);
				else if(rotuloAtual.equals("read")){
					AST filhoNoRead = filhosComandoElse.getFilhos().get(0);
					if(filhoNoRead != null)
						testaScan(filhoNoRead, anotacao_funcao);
					else throw new ErroComandoReadSemParametros("O comando read sem par�metros em :"+filhosComandoElse.getLinha(), 
							filhosComandoElse.getLinha());
				}
				else if(rotuloAtual.equals("for")) testaComandos(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("while")) testaComandos(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("if")) testaComandos(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("=")) testaAtribuicao(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("return")) testaReturn(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("exit")) continue;
				//else if(rotuloAtual.equals("else")) testaElse(filhosComandoElse, anotacao_funcao);
				else if(rotuloAtual.equals("chamada_funcao"))testaChamadaFuncao(filhosComandoElse, anotacao_funcao);
			}
		}else throw new ErroAnotacaoDeComando("O comando "+noElse.getRotulo()+" com problema na anotacao em: "+noElse.getLinha(),
				noElse.getLinha());
	}
	
	public void testaChamadaFuncao(AST noChamadaFuncao, String anotacao_funcao) throws ErroVariavelNaoDelarada, ErroOperadorNot, ErroDeTipo, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel{
		String anotacaoChamaFuncao = montarAnotacaoChamadaFuncao(noChamadaFuncao, anotacao_funcao);
		if(tabela.getTipo_funcaoMap().containsKey(anotacaoChamaFuncao)){
			return;
		}else throw new ErroFuncaoNaoDeclarada("A fun��o com anota��o "+anotacaoChamaFuncao
				+" n�o existe ou parametros de tipos incompat�veis em : "+noChamadaFuncao.getLinha(),noChamadaFuncao.getLinha());
		
	}
	
	public void testaIf(AST noIf, String anotacao_funcao) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, ErroComandoReadSemParametros, ErroAnotacaoDeComando, IOException {
		AST noOperadorRelacional = noIf.getFilhos().get(0);
		verificaOperadorRelacional(noOperadorRelacional, anotacao_funcao, "bool");
		String rotuloAtual;
		if(noIf.getFilhos().get(0) != null){
			for(AST filhosComandoIf : noIf.getFilhos()){
				rotuloAtual = filhosComandoIf.getRotulo();
				if(rotuloAtual.equals("print")) testaPrint(filhosComandoIf.getFilhos().get(0), anotacao_funcao);
				else if(rotuloAtual.equals("read")){
					AST filhoNoRead = filhosComandoIf.getFilhos().get(0);
					if(filhoNoRead != null)
						testaScan(filhoNoRead, anotacao_funcao);
					else throw new ErroComandoReadSemParametros("O comando read sem par�metros em :"+filhosComandoIf.getLinha(), 
							filhosComandoIf.getLinha());
				}
				else if(rotuloAtual.equals("for")) testaComandos(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("while")) testaComandos(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("if")) testaIf(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("=")) testaAtribuicao(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("return")) testaReturn(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("exit")) continue;
				else if(rotuloAtual.equals("else")) testaElse(filhosComandoIf, anotacao_funcao);
				else if(rotuloAtual.equals("chamada_funcao")) testaChamadaFuncao(filhosComandoIf, anotacao_funcao);
			}
		}else throw new ErroAnotacaoDeComando("O comando "+noIf.getRotulo()+" com problema na anotacao em: "+noIf.getLinha(),
				noIf.getLinha());
	}
	
	public void testaComandos(AST no, String anotacao) throws ErroOperadorNot, ErroDeTipo, ErroVariavelNaoDelarada, ErroFuncaoNaoDeclarada, ErroTipoFuncaoIncompativel, ErroComandoReadSemParametros, ErroAnotacaoDeComando, IOException{
		String variavel="", valorInicial="", tipoValorInicial="", valorFinal="", passo="";
		String rotulo = no.getRotulo();
		if(rotulo.equals("while")){
			AST noOperadorRelacional = no.getFilhos().get(0);
			verificaOperadorRelacional(noOperadorRelacional, anotacao_funcao, "bool");
		}else if(rotulo.equals("for")){
			variavel = no.getFilhos().get(0).getFilhos().get(0).getRotulo();
			valorInicial = no.getFilhos().get(0).getFilhos().get(1).getRotulo();
			valorFinal = no.getFilhos().get(1).getFilhos().get(0).getRotulo();
			if(no.getFilhos().size()>2){
				if(no.getFilhos().get(2).getRotulo().equals("step"))
					passo = no.getFilhos().get(2).getFilhos().get(0).getRotulo();
			}
			if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(variavel, anotacao)){
				tipoValorInicial = tabela.retornaVariavelLocal(anotacao, variavel).getTipo();
			}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(variavel)){
				tipoValorInicial = tabela.getDeclaracoes_globais().get(variavel+"_var").getTipo();
			}else throw new ErroVariavelNaoDelarada("A vari�vel "+variavel+" n�o foi declarada em : "
					+no.getLinha(), no.getLinha());
			if(tipoValorInicial.equals("int")){
				try{
					Integer.parseInt(valorInicial);
					//Integer.parseInt(valorFinal);
				}catch(Exception e){
					throw new ErroDeTipo("O valor "+valorInicial+" tem que ser do tipo "+tipoValorInicial
							+" em :"+no.getLinha(),no.getLinha());
				}
				
				try{
					//Integer.parseInt(valorInicial);
					Integer.parseInt(valorFinal);
				}catch(Exception e){
					throw new ErroDeTipo("O valor "+valorFinal+" tem que ser do tipo "+tipoValorInicial
							+" em :"+no.getLinha(),no.getLinha());
				}
				if(!passo.equals("")){
					try{
						Integer.parseInt(passo);
						//Integer.parseInt(valorFinal);
					}catch(Exception e){
						throw new ErroDeTipo("O valor do passo  tem que ser do tipo "+tipoValorInicial
								+" em :"+no.getLinha(),no.getLinha());
					}
				}
			}else if(tipoValorInicial.equals("real")){
				try{
					Float.parseFloat(valorInicial);
					//Integer.parseInt(valorFinal);
				}catch(Exception e){
					throw new ErroDeTipo("O valor "+valorInicial+" tem que ser do tipo "+tipoValorInicial
							+" em :"+no.getLinha(),no.getLinha());
				}
				
				try{
					//Integer.parseInt(valorInicial);
					Float.parseFloat(valorFinal);
				}catch(Exception e){
					throw new ErroDeTipo("O valor "+valorFinal+" tem que ser do tipo "+tipoValorInicial
							+" em :"+no.getLinha(),no.getLinha());
				}
				
				if(!passo.equals("")){
					try{
						Float.parseFloat(passo);
						//Integer.parseInt(valorFinal);
					}catch(Exception e){
						throw new ErroDeTipo("O valor do passo  tem que ser do tipo "+tipoValorInicial
								+" em :"+no.getLinha(),no.getLinha());
					}
				}
			}else throw new ErroDeTipo("Tipo "+tipoValorInicial+" n�o compat�vel em for em :"+no.getLinha(),
					no.getLinha());
		}
		
		String rotuloAtual;
		if(no.getFilhos().get(0) != null){
			for(AST comandos : no.getFilhos()){
				rotuloAtual = comandos.getRotulo();
				if(rotuloAtual.equals("print")) testaPrint(comandos.getFilhos().get(0), anotacao_funcao);
				else if(rotuloAtual.equals("read")){
					AST filhoNoRead = comandos.getFilhos().get(0);
					if(filhoNoRead != null)
						testaScan(filhoNoRead, anotacao_funcao);
					else throw new ErroComandoReadSemParametros("O comando read sem par�metros em :"+comandos.getLinha(), 
								comandos.getLinha());
				}
				else if(rotuloAtual.equals("for")) testaComandos(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("while")) testaComandos(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("if")) testaIf(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("=")) testaAtribuicao(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("return")) testaReturn(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("exit")) continue;
				//else if(rotuloAtual.equals("else")) testaElse(comandos, anotacao_funcao);
				else if(rotuloAtual.equals("chamada_funcao")) testaChamadaFuncao(comandos, anotacao_funcao);
			}
			
		}else throw new ErroAnotacaoDeComando("O comando "+rotulo+" com problema na anotacao em: "+no.getLinha(),
				no.getLinha());
		}
		
}
