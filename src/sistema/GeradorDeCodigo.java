package sistema;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

import retornos.Retornos;

public class GeradorDeCodigo {
	
	private File arquivoSaida = new File("saidaJasmin.j");
	private String underlines = "_____________________________";
	
	public GeradorDeCodigo(String nomeDaClasse) {
		try {
			//Limpa dados anteriores
			PrintWriter pw = new PrintWriter(arquivoSaida);
			pw.print("");
			
			//Abre arquivo em modo append
			pw.println(".class public"+nomeDaClasse);
			pw.println(".super java/lang/Object");
			pw.close();
			
			criarFuncaoReadInteiro();
			criarFuncaoReadFloat();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void criarFuncaoReadInteiro() throws IOException{
		
		File arquivoReadF = new File("arquivosEntrada\\readI.txt");
 
		FileInputStream fis = new FileInputStream(arquivoReadF);
		BufferedReader entrada = new BufferedReader(new InputStreamReader(fis));
 
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
 
		String linhaCorrente = null;
		saida.write("; "+underlines+"inicio funcao read de inteiros"+underlines);
		saida.newLine();
		while ((linhaCorrente = entrada.readLine()) != null) {
			saida.write(linhaCorrente);
			saida.newLine();
		}
		saida.write("; "+underlines+"fim funcao read de inteiros"+underlines);
		saida.newLine();
		entrada.close();
		saida.close();
	}
	
	public void criarFuncaoReadFloat() throws IOException{
		File arquivoReadF = new File("arquivosEntrada\\readF.txt");
		FileInputStream fis = new FileInputStream(arquivoReadF);
		BufferedReader entrada = new BufferedReader(new InputStreamReader(fis));
 
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
 
		String linhaCorrente = null;
		saida.write("; "+underlines+"inicio funcao read de float"+underlines);
		saida.newLine();
		while ((linhaCorrente = entrada.readLine()) != null) {
			saida.write(linhaCorrente);
			saida.newLine();
		}
		saida.write("; "+underlines+"fim funcao read de float"+underlines);
		saida.newLine();
		entrada.close();
		saida.close();
	}
	
	public void criarCabecalhoNovaFuncao(HashMap<String,Retornos> dadosFuncao,String nomeDaFuncao,String retorno) throws IOException{
		
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
		char tipoDoParametroMaiusculo = ' ';
		char tipoDoRetornoMaiusculo = ' ';
		
		saida.write("; "+underlines+"inicio funcao "+nomeDaFuncao+underlines);
		saida.newLine();
		for(String chaveAtual : dadosFuncao.keySet()){
			saida.write("; "+chaveAtual+" = "+dadosFuncao.get(chaveAtual).getValor());
			saida.newLine();
		}
		saida.newLine();
		saida.write(".method public "+nomeDaFuncao+"(");
		
		
		for(Retornos retornoAtual : dadosFuncao.values()){
			if(retornoAtual.isParametro()){
				tipoDoParametroMaiusculo = Character.toUpperCase(retornoAtual.getTipo().charAt(0));
				saida.write(tipoDoParametroMaiusculo);
			}
		}
		tipoDoRetornoMaiusculo = Character.toUpperCase(retorno.charAt(0));
		if(tipoDoRetornoMaiusculo != 'B')
			saida.write(")"+tipoDoRetornoMaiusculo);
		else
			saida.write(")Z");
		saida.newLine();
		saida.write(".limit stack 10");
		saida.newLine();
		saida.write(".limit locals "+dadosFuncao.values().size());
		saida.newLine();
		saida.close();
	}
	
	public void criarCabecalhoFuncaoMain(HashMap<String,Retornos> dadosFuncao,String nomeDaFuncao,String retorno) throws IOException{
		
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
		
		saida.write("; "+underlines+"inicio funcao "+nomeDaFuncao+underlines);
		saida.newLine();
		for(String chaveAtual : dadosFuncao.keySet()){
			saida.write("; "+chaveAtual+" = "+dadosFuncao.get(chaveAtual).getValor());
			saida.newLine();
		}
		saida.newLine();
		saida.write(".method public static main([Ljava/lang/String;)V");
		saida.newLine();
		saida.write(".limit stack 10");
		saida.newLine();
		saida.write(".limit locals "+dadosFuncao.values().size());
		saida.newLine();
		saida.close();
	}
	
	public void finalizarFuncao(String nomeDaFuncao) throws IOException{
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
		saida.write("return");
		saida.newLine();
		saida.write(".end method");
		saida.newLine();
		saida.write("; "+underlines+"fim funcao "+nomeDaFuncao+underlines);
		saida.newLine();
		saida.close();
	}
	
	public void criarOperacaoMatematica(String operador, String tipo) throws IOException{
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
		switch(operador){
			case "+":
				if(tipo.equals("float"))
					saida.write("fadd");
				else if(tipo.equals("int"))
					saida.write("iadd");
			break;
			case "-":
				if(tipo.equals("float"))
					saida.write("fsub");
				else if(tipo.equals("int"))
					saida.write("isub");
			break;
			case "*":
				if(tipo.equals("float"))
					saida.write("fmul");
				else if(tipo.equals("int"))
					saida.write("imul");
			break;
			case "/":
				if(tipo.equals("float"))
					saida.write("fdiv");
				else if(tipo.equals("int"))
					saida.write("idiv");
			break;
		}
		saida.newLine();
		saida.close();
		
		
		
	}
	public void carregaConstante(String valor) throws IOException{
		FileWriter fstream = new FileWriter(arquivoSaida, true);
		BufferedWriter saida = new BufferedWriter(fstream);
		saida.write("ldc "+valor);
		saida.newLine();
		saida.close();
	}
}
