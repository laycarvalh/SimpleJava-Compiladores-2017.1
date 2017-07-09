package ast;

import java.util.ArrayList;

public class AST {
	
	String rotulo;
    int linha;
	ArrayList<AST> filhos;
	
	public AST(String rotulo){
		this.rotulo = rotulo;
		this.linha = 0;
		filhos = new ArrayList<AST>();
	}
	
	public AST criarNo(String rotulo){
		AST novaAST = new AST(rotulo); 
		filhos = new ArrayList<>();
		return novaAST;
	}
        
    public void setLinha(int i){
    	this.linha = i;
    }
    
    public Integer getLinha(){
    	return linha;
    }
    
	
    public void adicionaFilho(AST No){
    	this.filhos.add(No);
    	//this.filhos.add(0,No);//inserir no inicio da lista
    }
        
	public String getRotulo() {
		return rotulo;
	}
	
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}
	
	public ArrayList<AST> getFilhos() {
		return filhos;
	}
}
