package sistema;

import java.util.HashMap;
import java.util.Map;

import retornos.Retornos;
import ast.AST;
import excecoes.ErroDeTipo;
import excecoes.ErroOperadorNot;
import excecoes.ErroVariavelNaoDelarada;

public class TypeChecker {

	GeradorDeCodigo geradorDeCodigo;
	
	public TypeChecker(GeradorDeCodigo geradorDeCodigo) {
		this.geradorDeCodigo = geradorDeCodigo;
	}
	
	public Retornos checaTipoConstanteGlobal(String tipo, String valor, AST noConstante) throws ErroDeTipo{
		Retornos cons = new Retornos(); //instancia para armazenar o valor e o tipo da constante
		int v1;
		float v2;
		boolean v3;
		//se o tipo for igua a int, entao tenta converter o valor(string) para inteiro, senao gera um
		//erro de tipo
		if(tipo == "int"){
			cons.setTipo(tipo);
			try{
				v1 = Integer.parseInt(valor);
			}catch(Exception e){
				throw new ErroDeTipo("Erro de tipo em : "+ noConstante.getLinha(), noConstante.getLinha());
			}
			cons.setValor(v1);
		}else if(tipo == "real"){ 
			//se o tipo for igual a float, entao tenta converter o valor(string) para float, senao gera um
			//erro de tipo
			cons.setTipo(tipo);
			try{
				v2 = Float.parseFloat(valor);
			}catch(Exception e){
				throw new ErroDeTipo("Erro de tipo em : "+ noConstante.getLinha(), noConstante.getLinha());
			}
			cons.setValor(v2);
		}else if(tipo == "bool"){
			//se o tipo for igua a boolean, entao tenta converter o valor(string) para boolean, senao gera um
			//erro de tipo
			cons.setTipo(tipo);
			if(valor.equals("TRUE")){
				v3 = true;
			}else if(valor.equals("FALSE")){
				v3 = false;
			}else{
				throw new ErroDeTipo("Erro de tipo em : "+ noConstante.getLinha(), noConstante.getLinha());
			}
			cons.setValor(v3);
		}else{
			//se o tipo for igua a string, entao verifica se o valor esta entre aspas duplas, senao gera
			//erro de tipo
			cons.setTipo(tipo);
			if(valor.charAt(0) =='"' &&  valor.charAt(valor.length()-1)== '"'){
				cons.setValor(valor);
			}else{
				throw new ErroDeTipo("Erro de tipo em : "+ noConstante.getLinha(), noConstante.getLinha());
			}

		}
		return cons;
	}
	
	
	public void checaTipoExpressaoAtribuicao(TabelaDeSimbolos tabela, String anotacao, AST no, String tipo_declaracao) throws ErroDeTipo, ErroVariavelNaoDelarada{
		
		//variaveis para armazenar o valor do tipo da variavel local e valor para armazenar
		//o rotulo para verificacao
		String tipo_local;
		String valor = no.getRotulo();
		
		//verifica se existe uma instancia declarada em constantes locais
		if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(valor, anotacao)){
			tipo_local = tabela.retornaConstanteLocal(anotacao, valor).getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(tipo_local != tipo_declaracao){
				//verifica se o valor do lado direito for int e do lado esquerdo, permite conversao
				if(tipo_local.equals("int") && tipo_declaracao.equals("real")) return;
				else throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
			} else return;
		}else if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(valor, anotacao)){
			//verifica se existe uma instancia declarada em variaveis locais
			tipo_local = tabela.retornaVariavelLocal(anotacao, valor).getTipo();
			if(tipo_local != tipo_declaracao){
				if(tipo_local.equals("int") && tipo_declaracao.equals("real")) return;
				else throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
			} else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(valor)){
			//verifica se existe uma instancia declarada em constantes globais
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_const").getTipo();
			if(tipo_local != tipo_declaracao){
				if(tipo_local.equals("int") && tipo_declaracao.equals("real")) return;
				else throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
			} else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(valor)){
			//verifica se existe uma instancia declarada em variaveis globais
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_var").getTipo();
			if(tipo_local != tipo_declaracao){
				if(tipo_local.equals("int") && tipo_declaracao.equals("real")) return;
				else throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
			} else return;
		}else if(valor.charAt(0)!='0' &&
				valor.charAt(0)!='1' &&
				valor.charAt(0)!='2' &&
				valor.charAt(0)!='3' &&
				valor.charAt(0)!='4' &&
				valor.charAt(0)!='5' &&
				valor.charAt(0)!='6' &&
				valor.charAt(0)!='7' &&
				valor.charAt(0)!='8' &&
				valor.charAt(0)!='9'){ //verifica se o primeiro elemento da string é um digito
									   //se for entao o valor é uma variavel nao declarada
			
			if(tipo_declaracao.equals("string")){
                            System.out.println("Entrou no checador de tipo");
				if(valor.charAt(0) !='"' &&  valor.charAt(valor.length()-1)!= '"'){
					throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", no.getLinha());
				}
			}else if(tipo_declaracao.equals("bool")){
				if(valor.equals("TRUE") || valor.equals("FALSE")) return; 
				else throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", no.getLinha());
				
			}else{
				if(!valor.equals("TRUE") && !valor.equals("FALSE"))
				throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", no.getLinha());
				else throw new ErroDeTipo("O Tipo de "
						+valor+ " nao e compativel com "+tipo_declaracao+" em : "+no.getLinha(), no.getLinha());
			}
			
			
		}else{
			//se o valor nao foi declarado em lugar nenhum e nao e um nome e sim digito
			//entao vai verificar o tipo do lado esquerdo da expressao e tenta converter o digito
			//para aquele tipo
			
			if(tipo_declaracao.equals("int")){
				try{
					Integer.parseInt(valor);
					geradorDeCodigo.carregaConstante(valor);
				}catch(Exception e){
					throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
				}
			}else if(tipo_declaracao.equals("real")){
				try{
					Float.parseFloat(valor);
					geradorDeCodigo.carregaConstante(valor);
				}catch(Exception e){
					throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
				}
			}else if(tipo_declaracao.equals("bool") || tipo_declaracao.equals("string")){
				throw new ErroDeTipo("Esperado tipo "+tipo_declaracao+" em: "+no.getLinha(), no.getLinha());
			}
		}
	}

	public void checaTipoExprNot(AST no, String tipo_declarado) throws ErroOperadorNot, ErroDeTipo{
		
		if(tipo_declarado != "bool"){
			
			throw new ErroDeTipo("Eperado tipo "
					+ " "+tipo_declarado+" em: "+no.getLinha(), no.getLinha());
			
		}else{
			
			String rotulo = no.getFilhos().get(0).getRotulo();
			
			if(rotulo.equals("+") || rotulo.equals("-") || rotulo.equals("*") || rotulo.equals("/"))
				
				throw new ErroOperadorNot("Operador ! nao pode ser aplicado ao simbolo "
					+ " "+rotulo+" em: "+no.getLinha(), no.getLinha(), no.getLinha());
			
		}
	
	}
	
	public void checaTipoValorOperacaoRelacionalBoolean(AST noFilhoDeNot, String anotacao_funcao, TabelaDeSimbolos tabela) throws ErroDeTipo{
		String valorEmExpressaoNot = noFilhoDeNot.getRotulo();
		String tipoDeclaradoLocal, tipoDeclaradoGlobal;
		if(!valorEmExpressaoNot.equals("TRUE") && !valorEmExpressaoNot.equals("FALSE")){
			if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(valorEmExpressaoNot, anotacao_funcao)){
				tipoDeclaradoLocal = tabela.retornaVariavelLocal(anotacao_funcao, valorEmExpressaoNot).getTipo();
				if(!tipoDeclaradoLocal.equals("bool")){
					throw new ErroDeTipo("Tipo da variavel "+valorEmExpressaoNot+" é "+tipoDeclaradoLocal+
							" era esperado boolean em :"+noFilhoDeNot.getLinha(), noFilhoDeNot.getLinha());
				}
			}else if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(valorEmExpressaoNot, anotacao_funcao)){
				tipoDeclaradoLocal = tabela.retornaConstanteLocal(anotacao_funcao, valorEmExpressaoNot).getTipo();
				if(!tipoDeclaradoLocal.equals("bool")){
					throw new ErroDeTipo("Tipo da constante "+valorEmExpressaoNot+" é "+tipoDeclaradoLocal+
							" era esperado boolean em :"+noFilhoDeNot.getLinha(), noFilhoDeNot.getLinha());
				}
			}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(valorEmExpressaoNot)){
				tipoDeclaradoGlobal = tabela.getDeclaracoes_globais().get(valorEmExpressaoNot+"_var").getTipo();
				if(!tipoDeclaradoGlobal.equals("bool")){
					throw new ErroDeTipo("Tipo da variavel global "+valorEmExpressaoNot+" é "+tipoDeclaradoGlobal+
							" era esperado boolean em :"+noFilhoDeNot.getLinha(), noFilhoDeNot.getLinha());
				}
			}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(valorEmExpressaoNot)){
				tipoDeclaradoGlobal = tabela.getDeclaracoes_globais().get(valorEmExpressaoNot+"_const").getTipo();
				if(!tipoDeclaradoGlobal.equals("bool")){
					throw new ErroDeTipo("Tipo da constante global "+valorEmExpressaoNot+" é "+tipoDeclaradoGlobal+
							" era esperado boolean em :"+noFilhoDeNot.getLinha(), noFilhoDeNot.getLinha());
				}
			}else throw new ErroDeTipo("O valor "+valorEmExpressaoNot+" em "
				+noFilhoDeNot.getLinha()+" nao e um valor do tipo boolean", noFilhoDeNot.getLinha());
		}
	}
	
	public void checaTipoExpressaoEmRelacional(TabelaDeSimbolos tabela, String anotacao_funcao,AST noExpr, String simboloRelacional, boolean entrouEmExprAritmetica) throws ErroDeTipo, ErroVariavelNaoDelarada{
		//variaveis para armazenar o valor do tipo da variavel local e valor para armazenar
		//o rotulo para verificacao
		String tipo_local;
		String valor = noExpr.getRotulo();

		//verifica se existe uma instancia declarada em constantes locais
		if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(valor, anotacao_funcao)){
			tipo_local = tabela.retornaConstanteLocal(anotacao_funcao, valor).getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(simboloRelacional.equals("==") && tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da constante local"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(valor, anotacao_funcao)){
			tipo_local = tabela.retornaVariavelLocal(anotacao_funcao, valor).getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(simboloRelacional.equals("==") && tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da variavel local"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(valor)){
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_const").getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(simboloRelacional.equals("==") && tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da constante global"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(valor)){
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_var").getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(simboloRelacional.equals("==") && tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da variavel global"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(valor.charAt(0)!='0' &&
				valor.charAt(0)!='1' &&
				valor.charAt(0)!='2' &&
				valor.charAt(0)!='3' &&
				valor.charAt(0)!='4' &&
				valor.charAt(0)!='5' &&
				valor.charAt(0)!='6' &&
				valor.charAt(0)!='7' &&
				valor.charAt(0)!='8' &&
				valor.charAt(0)!='9' &&
				!valor.equals("TRUE") &&
				!valor.equals("FALSE")){ //verifica se o primeiro elemento da string é um digito
			//se for entao o valor é uma variavel nao declarada
			
			throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", noExpr.getLinha());

		}else{
			//se o valor nao foi declarado em lugar nenhum e nao e um nome e sim digito
			//entao vai verificar o tipo do lado esquerdo da expressao e tenta converter o digito
			//para aquele tipo
			if(simboloRelacional.equals("==") && (valor.equals("TRUE") || valor.equals("FALSE")) &&
					entrouEmExprAritmetica == false){
				return;
			}else{
				try{
					Float.parseFloat(valor);
				}catch(Exception e){
					throw new ErroDeTipo("Esperado que a constante "
							+valor+ " seja do tipo int ou float para: "+noExpr.getLinha(), noExpr.getLinha());
				}

			}
		}
	}
	
	public void checaTipoExpressaoEmPrint(TabelaDeSimbolos tabela, String anotacao_funcao,AST noExpr, boolean entrouEmExprAritmetica) throws ErroDeTipo, ErroVariavelNaoDelarada{
		//variaveis para armazenar o valor do tipo da variavel local e valor para armazenar
		//o rotulo para verificacao
		String tipo_local;
		String valor = noExpr.getRotulo();
		//verifica se existe uma instancia declarada em constantes locais
		if(tabela.verificaSeExisteDeclaracaoEmConstantesLocais(valor, anotacao_funcao)){
			tipo_local = tabela.retornaConstanteLocal(anotacao_funcao, valor).getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if( tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da constante local"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeFoiDeclaradoEmVariaveisLocais(valor, anotacao_funcao)){
			tipo_local = tabela.retornaVariavelLocal(anotacao_funcao, valor).getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da variavel local "
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmConstantesGlobais(valor)){
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_const").getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da constante global"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(valor)){
			tipo_local = tabela.getDeclaracoes_globais().get(valor+"_var").getTipo();
			//se o tipo do valor do lado direito da expressao for diferente da do lado esquerdo
			if(!tipo_local.equals("int") && !tipo_local.equals("real")){
				if(tipo_local.equals("bool") && entrouEmExprAritmetica == false) return;
				else throw new ErroDeTipo("O Tipo "+tipo_local+" da variavel global"
						+valor+ " nao e compativel em : "+noExpr.getLinha(), noExpr.getLinha());
			}else return;
		}else if(valor.charAt(0)!='0' &&
				valor.charAt(0)!='1' &&
				valor.charAt(0)!='2' &&
				valor.charAt(0)!='3' &&
				valor.charAt(0)!='4' &&
				valor.charAt(0)!='5' &&
				valor.charAt(0)!='6' &&
				valor.charAt(0)!='7' &&
				valor.charAt(0)!='8' &&
				valor.charAt(0)!='9' &&
				!valor.equals("TRUE") &&
				!valor.equals("FALSE") &&
				valor.charAt(0) != '"' && 
				valor.charAt(valor.length()-1)!='"'){ //verifica se o primeiro elemento da string é um digito
			//se for entao o valor é uma variavel nao declarada
			
			throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", noExpr.getLinha());

		}else{
			//se o valor nao foi declarado em lugar nenhum e nao e um nome e sim digito
			//entao vai verificar o tipo do lado esquerdo da expressao e tenta converter o digito
			//para aquele tipo
			if((valor.equals("TRUE") || valor.equals("FALSE")) &&
					entrouEmExprAritmetica == false){
				return;
			}else if(valor.charAt(0) != '"' && valor.charAt(valor.length()-1)!= '"'){
				try{
					Float.parseFloat(valor);
				}catch(Exception e){
					throw new ErroDeTipo("Esperado que a constante "
							+valor+ " seja do tipo int ou float para: "+noExpr.getLinha(), noExpr.getLinha());
				}

			}else if(valor.charAt(0) == '"' && valor.charAt(valor.length()-1) == '"' &&
					entrouEmExprAritmetica == false){
				return;
			}else throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", noExpr.getLinha());
		}
	}

	public void checaRead(TabelaDeSimbolos tabela,AST filhoNoRead, String anotacao_funcao) throws ErroVariavelNaoDelarada {
		//verifica se existe uma instancia declarada em constantes locais
		String valor = filhoNoRead.getRotulo();
		if(!tabela.verificaSeFoiDeclaradoEmVariaveisLocais(valor, anotacao_funcao)){
			if(!tabela.verificaSeExisteDeclaracaoEmVariaveisGlobais(valor)){
				throw new ErroVariavelNaoDelarada("Variavel "+valor+" nao declarada", filhoNoRead.getLinha());
			} 
		}
		
	}
	
}
