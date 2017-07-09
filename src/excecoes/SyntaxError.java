package excecoes;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;

public class SyntaxError extends RecognitionException {
	
	
	private int line;
	private int charPositionInLine;
    public SyntaxError(String message, RecognitionException e, int line, int charPositionInLine) {
        super(message, e.getRecognizer(), e.getInputStream(), (ParserRuleContext) e.getCtx());
        this.setOffendingToken(e.getOffendingToken());
        this.initCause(e);
        this.setLine(line);
        this.setCharPositionInLine(charPositionInLine);
        
    }
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getCharPositionInLine() {
		return charPositionInLine;
	}
	public void setCharPositionInLine(int charPositionInLine) {
		this.charPositionInLine = charPositionInLine;
	}
}
