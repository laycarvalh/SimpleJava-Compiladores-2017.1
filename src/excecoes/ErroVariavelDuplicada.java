package excecoes;

public class ErroVariavelDuplicada extends Exception{
	int linha;
	int coluna;
	
	public ErroVariavelDuplicada (String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroVariavelDuplicada (String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}

}
