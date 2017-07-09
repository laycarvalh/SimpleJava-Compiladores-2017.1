package excecoes;

import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ErroSintatico extends RuntimeException{

	int linha;
	int coluna;
	
	public ErroSintatico(String mensagem, int linha) {
		super(mensagem);
		this.linha = linha;
		this.coluna = 0;
	}
		
	public ErroSintatico(String mensagem, int linha, int coluna) {
		super(mensagem);
		this.linha = linha;
		this.coluna = coluna;
	}
}
