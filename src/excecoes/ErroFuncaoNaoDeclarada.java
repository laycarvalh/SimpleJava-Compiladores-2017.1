package excecoes;

public class ErroFuncaoNaoDeclarada extends Exception{
	int linha;
	int coluna;
	
	public ErroFuncaoNaoDeclarada(String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroFuncaoNaoDeclarada(String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}

}
