package retornos;

public class Retornos{

	Object valor;
	String tipo;
	boolean parametro;

	public String getTipo() {
		return tipo;
	}

	public Object getValor(){
		return this.valor;
	}
	
	public void setValor(Object v){
		this.valor = v;
	}
	
	public void setTipo(String v){
		this.tipo = v;
	}
	
	public boolean equals(Retornos val){
		if((this.valor == val.valor) && (this.tipo == val.tipo)) return true;
		else return false;
	}
	
	public void imprimeRetornos(){
		String result = "valor: "+this.valor+", tipo: "+this.tipo;
		System.out.println(result);
		
	}

	public boolean isParametro() {
		return parametro;
	}

	public void setParametro(boolean parametro) {
		this.parametro = parametro;
	}
	
}
