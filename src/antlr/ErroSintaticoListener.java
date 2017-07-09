package antlr;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

import excecoes.ErroSintatico;
import excecoes.InlineRecognitionException;
import excecoes.SyntaxError;


public class ErroSintaticoListener extends BaseErrorListener {
	Container contentFather;
	JTextArea textEditor;
	 


	
	


	private final List<SyntaxError> errors = new ArrayList<SyntaxError>();
	
	public List<SyntaxError> getErrors() {
        return errors;
    }
	
	
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line,int charPositionInLine,
                                String msg,
                                RecognitionException e)  {
		if (e == null) {
            // e is null when the parser was able to recover in line without exiting the surrounding rule.
            e = new InlineRecognitionException(msg, recognizer, ((Parser)recognizer).getInputStream(), ((Parser)recognizer).getContext(), (Token) offendingSymbol);
        }
        
    
        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        StringBuilder buf = new StringBuilder();
		JDialog dialog = new JDialog();
		Container contentPane = dialog.getContentPane();
		//underlineError(recognizer,(Token)offendingSymbol,
		//		line, charPositionInLine, contentPane, dialog, buf);
        String erro = "";
        if(msg.contains("no viable alternative at input")){
        	erro = msg.replace("no viable alternative at input", "Nenhum alternativa para a entrada");
        	this.errors.add(new SyntaxError(erro, e, line, charPositionInLine));
        	//try{
        	//throw e;
        		//throw new ErroSintatico("linha "+line+":"+charPositionInLine+": "+erro, line, charPositionInLine);
        	/*}catch (ErroSintatico e1){
				// TODO Auto-generated catch block
				buf.append("rule stack: "+stack+" ");
				buf.append("linha "+line+":"+charPositionInLine+" em "+
				offendingSymbol+": "+msg);
				contentPane.add(contentFather,new JLabel(buf.toString()));
				contentPane.setBackground(Color.red);
				dialog.setTitle("Erro Sintático");
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				e1.printStackTrace();
			}*/
			
        }else if(msg.contains("extraneous input")){
        	erro = msg.replace("extraneous input","Entrada estranha");
        	erro = erro.replace("expecting", "esperava-se ");
        	this.errors.add(new SyntaxError(erro, e, line, charPositionInLine));
        	try {
				throw new ErroSintatico("linha "+line+":"+charPositionInLine+": "+erro, line, charPositionInLine);
			} catch (ErroSintatico e1) {
				// TODO Auto-generated catch block
				/*e1.printStackTrace();
				buf.append("rule stack: "+stack+" ");
				buf.append("linha "+line+":"+charPositionInLine+" em "+
				offendingSymbol+": "+msg);
				contentPane.add(new JLabel(buf.toString()));
				contentPane.setBackground(Color.red);
				dialog.setTitle("Erro Sintático");
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				e1.printStackTrace();*/
			}
        }else if(msg.contains("missing")){
        	erro = msg.replace("missing", "Esperando");
        	erro = erro.replace("at", "em");
        	this.errors.add(new SyntaxError(erro, e, line, charPositionInLine));
        	try {
				throw new ErroSintatico("linha "+line+":"+charPositionInLine+": "+erro, line, charPositionInLine);
			} catch (ErroSintatico e1) {
				// TODO Auto-generated catch block
				/*e1.printStackTrace();
				buf.append("rule stack: "+stack+" ");
				buf.append("linha "+line+":"+charPositionInLine+" em "+
				offendingSymbol+": "+msg);
				contentPane.add(new JLabel(buf.toString()));
				contentPane.setBackground(Color.red);
				dialog.setTitle("Erro Sintático");
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				e1.printStackTrace();*/
			}
    	}else if(msg.contains("mismatched input")){
    		erro = msg.replace("mismatched input", "entrada incompat�vel ");
    		erro = erro.replace("expecting", "esperava-se");
    		this.errors.add(new SyntaxError(erro, e, line, charPositionInLine));
    		try {
				throw new ErroSintatico("linha "+line+":"+charPositionInLine+": "+erro, line, charPositionInLine);
			} catch (ErroSintatico e1) {
				// TODO Auto-generated catch block
				/*e1.printStackTrace();
				buf.append("rule stack: "+stack+" ");
				buf.append("linha "+line+":"+charPositionInLine+" em "+
				offendingSymbol+": "+msg);
				contentPane.add(new JLabel(buf.toString()));
				contentPane.setBackground(Color.red);
				dialog.setTitle("Erro Sintático");
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				e1.printStackTrace();*/
			}
        }else{
        	this.errors.add(new SyntaxError(msg, e, line, charPositionInLine));
        	try {
				throw new ErroSintatico("linha "+line+":"+charPositionInLine+": "+msg, line, charPositionInLine);
			} catch (ErroSintatico e1) {
				// TODO Auto-generated catch block
				/*e1.printStackTrace();
				buf.append("rule stack: "+stack+" ");
				buf.append("linha "+line+":"+charPositionInLine+" em "+
				offendingSymbol+": "+msg);
				contentPane.add(new JLabel(buf.toString()));
				contentPane.setBackground(Color.red);
				dialog.setTitle("Erro Sintático");
				dialog.pack();
				dialog.setLocationRelativeTo(null);
				dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);
				e1.printStackTrace();*/
			}
        }
    }
	
	
	protected void underlineError(Recognizer recognizer,
			Token offendingToken, int line,
			int charPositionInLine, Container contentPane, JDialog dialog, StringBuilder buf) {
			CommonTokenStream tokens =
			(CommonTokenStream)recognizer.getInputStream();
			String input = tokens.getTokenSource().getInputStream().toString();
			String[] lines = input.split("\n");
			String errorLine = lines[line - 1];
			contentPane.add(new JLabel(errorLine));
			buf.append(errorLine+"\n");
			
			for (int i=0; i<charPositionInLine; i++) buf.append(" ");
			int start = offendingToken.getStartIndex();
			int stop = offendingToken.getStopIndex();
			if ( start>=0 && stop>=0 ) {
			for (int i=start; i<=stop; i++) buf.append("^");
			}
			buf.append("\n");
			
			/*contentPane.add(new JLabel(buf.toString()));
			contentPane.setBackground(Color.red);
			dialog.setTitle("Erro Sintático");
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			System.err.println(buf.toString());*/
			
			}
	
}
