package sistema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retornos.Retornos;
import ast.AST;

public class TabelaDeSimbolos {

	private AST ast;
	
	private Map<String, Retornos> declaracoes_globais;
	
	private Map<String, HashMap<String,Retornos>> funcoes;
	
	private Map<String,String> tipo_funcaoMap;
	
	
	public TabelaDeSimbolos(AST ast){
		this.ast = ast;
		funcoes = new HashMap<>();
		declaracoes_globais = new HashMap<>();
		tipo_funcaoMap = new HashMap<>();
	}

	public AST getAst() {
		return ast;
	}

	public void setAst(AST ast) {
		this.ast = ast;
	}
	
	
	public Map<String, Retornos> getDeclaracoes_globais() {
		return declaracoes_globais;
	}

	public void setDeclaracoes_globais(Map<String, Retornos> declaracoes_globais) {
		this.declaracoes_globais = declaracoes_globais;
	}

	public Map<String, HashMap<String, Retornos>> getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(Map<String, HashMap<String, Retornos>> funcoes) {
		this.funcoes = funcoes;
	}
	
	public void imprimeDeclaracoesGlobais(){
		for(String chave : this.declaracoes_globais.keySet()){
			System.out.println("chave_global:"+chave);
			System.out.println("Retornos: ");
			this.declaracoes_globais.get(chave).imprimeRetornos();
		}
	}
	
	
	
	public Map<String, String> getTipo_funcaoMap() {
		return tipo_funcaoMap;
	}

	public void setTipo_funcaoMap(Map<String, String> tipo_funcaoMap) {
		this.tipo_funcaoMap = tipo_funcaoMap;
	}

	public void imprimeFuncoes(){
		for(String chave : this.funcoes.keySet()){
			System.out.println("chave :"+chave);
			
			for(String valor : this.getFuncoes().get(chave).keySet()){
				System.out.println("chave_local:"+valor);
				System.out.println("Retornos: ");
				this.getFuncoes().get(chave).get(valor).imprimeRetornos();;
			}
		}
	}
	
	public boolean verificaSeExisteDeclaracaoEmVariaveisGlobais(String nome){
		if(this.getDeclaracoes_globais().containsKey(nome+"_var")) return true;
		else return false;
	}
	
	public boolean verificaSeFoiDeclaradoEmVariaveisLocais(String variavel, String anotacao){
		if(this.getFuncoes().get(anotacao).containsKey(variavel+"_var")) return true;
		else return false;
	}
	
	public boolean verificaSeExisteDeclaracaoEmConstantesGlobais(String nome){
		if(this.getDeclaracoes_globais().containsKey(nome+"_const")) return true;
		else return false;
	}
	
	public boolean verificaSeExisteDeclaracaoEmConstantesLocais(String constante, String anotacao){
		if(this.getFuncoes().get(anotacao).containsKey(constante+"_const")) return true;
		else return false;
	}
	
	public HashMap<String,Retornos> retornaDeclaracoesLocais(String anotacao){
		return this.getFuncoes().get(anotacao);
	}
	
	public Retornos retornaVariavelLocal(String anotacao, String variavel){
		return this.retornaDeclaracoesLocais(anotacao).get(variavel+"_var");
	}
	
	public Retornos retornaConstanteLocal(String anotacao, String cons){
		return this.retornaDeclaracoesLocais(anotacao).get(cons+"_const");
	}
	
	public boolean verificaSeExisteFuncaoComMesmaAnotacao(String anotacao){
		if(this.getFuncoes().get(anotacao) != null) return true;
		else return false;
	}
	
}
