package interfaceGrafica;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class Editor extends KeyAdapter {

	private JTextArea textEditor;
	private boolean debug = true;
	private int nivelDeIdentacao;
	private String linhaAtual;

	public Editor(JTextArea textEditor) {
		this.textEditor = textEditor;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER
				&& textEditor.getText().charAt(textEditor.getCaretPosition()-2) == '{') {
			identacaoAutomatica();
		}
	}
	
	private void identacaoAutomatica(){
		int ultimaTabulacao = 0;
		int contador = 0;
		int qtTabulacoes = 0;
			
		while(ultimaTabulacao != -1){
			ultimaTabulacao = linhaAtual.indexOf("\t");
			if(ultimaTabulacao != -1){
				qtTabulacoes++;
				linhaAtual = linhaAtual.substring(ultimaTabulacao+1,linhaAtual.length());
			}else{
				break;
			}
		}
		while(contador<qtTabulacoes){
			textEditor.insert("\t",textEditor.getCaretPosition());
			contador++;
		}
		textEditor.insert("\t\n",textEditor.getCaretPosition());
		contador = 0;
		while(contador<qtTabulacoes){
			textEditor.insert("\t",textEditor.getCaretPosition());
			contador++;
		}
		System.out.println(textEditor.getText()+"\nposicao do cursor = "+textEditor.getCaretPosition());
		textEditor.insert("}",textEditor.getCaretPosition());
		
		textEditor.setCaretPosition(textEditor.getCaretPosition() - (2+qtTabulacoes));
	}
	
	private void capturaLinha(){
		try {
			int offset = textEditor.getLineOfOffset(textEditor.getCaretPosition());
			int comecoDaLinha = textEditor.getLineStartOffset(offset);
			int fimDaLinha = textEditor.getLineEndOffset(offset);
			linhaAtual = textEditor.getText(comecoDaLinha, (fimDaLinha-comecoDaLinha));
			System.out.println("linha atual = "+linhaAtual.replace("\t", "0"));
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	private void formatarTexto() {
		
		String textoSemFormatacao = textEditor.getText();
		int posicaoAbreChaveAbsoluta = 0;
		int posicaoAbreChaveRelativa;
		int posicaoFechaChave = 0;
		
		while (posicaoAbreChaveAbsoluta != -1) {
			
			posicaoAbreChaveAbsoluta = textoSemFormatacao.indexOf('{');
			posicaoFechaChave = textoSemFormatacao.indexOf('}');
			
			if (posicaoFechaChave != -1) {
				//Definir substring limitada pelos par�nteses encontrados
				String substringQueSeraSubstituida = textoSemFormatacao
						.substring(posicaoAbreChaveAbsoluta, posicaoFechaChave + 1);
				
				//Verificar se ainda existem par�nteses internos nessa substring
				posicaoAbreChaveRelativa = buscarPosicaoDoAbreChaveMaisInterno(posicaoAbreChaveAbsoluta, substringQueSeraSubstituida);
				
				//Calcular a posi��o do par�ntese mais interno na String de entrada
				posicaoAbreChaveAbsoluta = posicaoAbreChaveAbsoluta+posicaoAbreChaveRelativa;
				
				//Definir a nova substring que ser� utilizada
				//Garantia de que se trata do primeiro abre parentese mais interno
				substringQueSeraSubstituida = textoSemFormatacao
						.substring(posicaoAbreChaveAbsoluta, posicaoFechaChave + 1);
				
				if (debug == true) {
					System.out.println("substringQueSeraSubstituida:");
					System.out.println(substringQueSeraSubstituida);
				}
				
				//
				String textoComAsPartesJaFormatadasRetiradas = substringQueSeraSubstituida
						.replace("{\n", "��");
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace(";\n\t", "~~");
				if (debug == true) {
					System.out
							.println("textoComAsPartesJaFormatadasRetiradas:");
					System.out.println(textoComAsPartesJaFormatadasRetiradas);
				}
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace(";\n}", "^^");
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace(";\n", ";\n\t");
				if (debug == true) {
					System.out.println("formatando:");
					System.out.println(textoComAsPartesJaFormatadasRetiradas
							.replace(";\n", ";\n\t"));
				}
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace("~~", ";\n\t");
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace("��", "{\n\t");
				textoComAsPartesJaFormatadasRetiradas = textoComAsPartesJaFormatadasRetiradas
						.replace("^^", ";\n}");
				if (debug == true) {
					System.out.println("formatado:");
					System.out.println(textoComAsPartesJaFormatadasRetiradas);
				}
				textoSemFormatacao = textoSemFormatacao.replaceAll(
						Pattern.quote(substringQueSeraSubstituida),
						textoComAsPartesJaFormatadasRetiradas);
				posicaoAbreChaveAbsoluta = textoSemFormatacao.indexOf('{');
				posicaoFechaChave = textoSemFormatacao.indexOf('}');
				textoSemFormatacao = textoSemFormatacao.substring(0,
						posicaoAbreChaveAbsoluta)
						+ "``"
						+ textoSemFormatacao.substring(posicaoAbreChaveAbsoluta + 1);
				textoSemFormatacao = textoSemFormatacao.substring(0,
						posicaoFechaChave)
						+ "##"
						+ textoSemFormatacao.substring(posicaoFechaChave + 2);
				if (debug == true) {
					System.out.println("Removendo { e }:");
					System.out.println(textoSemFormatacao);
				}
			}
		}
		textoSemFormatacao = textoSemFormatacao.replaceAll("``", "{");
		textoSemFormatacao = textoSemFormatacao.replaceAll("##", "\n}");
		textEditor.setText(textoSemFormatacao);
	}

	private int buscarPosicaoDoAbreChaveMaisInterno(int posicaoAbreChave, String substring){
		boolean existeAbreChaveInterno = true;
		while (existeAbreChaveInterno) {
			posicaoAbreChave = substring.indexOf('{');
			if(posicaoAbreChave != -1){
				//tira o primeira chave
				substring = substring.substring(0,
						posicaoAbreChave)
						+ "`"
						+ substring
								.substring(posicaoAbreChave + 1);
				if (debug == true) {
					System.out.println("Parentese encontrado na posicao = "+posicaoAbreChave);
					System.out.println("Tirando abre parentese:");
					System.out.println(substring);
				}
				if (substring.substring(posicaoAbreChave).indexOf("{") != -1) {//busca por abre chave interno
					if(debug == true){
						System.out.println("Parentese interno encontrado");
					}
					continue;
				} else {
					substring = substring.replaceAll("`",
							"{");
					if (debug == true) {
						System.out.println("Todsos abre parentese tirados:");
						System.out.println(substring);
					}
					existeAbreChaveInterno = false;
				}
			}
		}
		return posicaoAbreChave;
	}
}