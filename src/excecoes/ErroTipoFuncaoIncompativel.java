package excecoes;

public class ErroTipoFuncaoIncompativel extends Exception{
	int linha;
	int coluna;
	
	public ErroTipoFuncaoIncompativel(String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroTipoFuncaoIncompativel(String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}

}
