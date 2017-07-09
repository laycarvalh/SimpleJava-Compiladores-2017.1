package excecoes;

public class ErroComandoReadSemParametros extends Exception{
	int linha;
	int coluna;
	
	public ErroComandoReadSemParametros(String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroComandoReadSemParametros(String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}
}
