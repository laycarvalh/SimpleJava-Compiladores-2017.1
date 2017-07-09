package excecoes;

public class ErroConstanteDuplicada extends Exception{
	int linha;
	int coluna;
	
	public ErroConstanteDuplicada (String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroConstanteDuplicada (String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}
}
