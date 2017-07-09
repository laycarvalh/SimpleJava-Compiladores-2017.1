package excecoes;

public class ErroVariavelNaoDelarada extends Exception{
	int linha;
	int coluna;
	
	public ErroVariavelNaoDelarada(String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroVariavelNaoDelarada(String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}
}
