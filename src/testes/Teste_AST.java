package testes;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import antlr.ErroSintaticoListener;
import antlr.GramaticaSimpleJavaLexer;
import antlr.GramaticaSimpleJavaParser;
import sistema.ASTJung;
import sistema.Semantica;
import sistema.SimpleJavaVisitor;

public class Teste_AST {

	@Test
	public void test_sizeAST() {
		String textEditor = null;
		try {
			textEditor = ("");
			BufferedReader bufferEntrada = new BufferedReader(new FileReader("arquivosTeste/entrada1.txt"));
			String linhaLida;
	        while((linhaLida = bufferEntrada.readLine()) != null)
	        {
	            textEditor = textEditor + (linhaLida+"\n");
	        }
	        bufferEntrada.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//fail("Not yet implemented");
		String comandos = null;
		ANTLRInputStream antlrInput = new ANTLRInputStream(textEditor);

		GramaticaSimpleJavaLexer lexer = new GramaticaSimpleJavaLexer(antlrInput);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GramaticaSimpleJavaParser parser = new GramaticaSimpleJavaParser(tokens);
		parser.removeErrorListeners();
		
		ParseTree tree = parser.prog();
		SimpleJavaVisitor loader = new SimpleJavaVisitor();
		loader.visit(tree);
		try {
			new Semantica(loader.getAST());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ASTJung astJung = new ASTJung(loader.getAST());
		//astJung.mostrarASTJung();
		assertEquals(loader.getAST().getFilhos().size(), 3);
		//Filhos da AST = nome/function/main
	}
	
	@Test
	public void test_Filhos_Func() {
		String textEditor = null;
		try {
			textEditor = ("");
			BufferedReader bufferEntrada = new BufferedReader(new FileReader("arquivosTeste/entrada1.txt"));
			String linhaLida;
	        while((linhaLida = bufferEntrada.readLine()) != null)
	        {
	            textEditor = textEditor + (linhaLida+"\n");
	        }
	        bufferEntrada.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//fail("Not yet implemented");
		String comandos = null;
		ANTLRInputStream antlrInput = new ANTLRInputStream(textEditor);

		GramaticaSimpleJavaLexer lexer = new GramaticaSimpleJavaLexer(antlrInput);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GramaticaSimpleJavaParser parser = new GramaticaSimpleJavaParser(tokens);
		parser.removeErrorListeners();
		//parser.addErrorListener(new ErroSintaticoListener());
		ParseTree tree = parser.prog();
		SimpleJavaVisitor loader = new SimpleJavaVisitor();
		loader.visit(tree);
		try {
			new Semantica(loader.getAST());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ASTJung astJung = new ASTJung(loader.getAST());
		//astJung.mostrarASTJung();
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(0).getRotulo(), "soma");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(1).getRotulo(), "param");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(2).getRotulo(), "float");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(3).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(4).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(5).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(6).getRotulo(), "if");
		//Filhos da AST = nome/function/main
	}

	
	@Test
	public void test_Filhos() {
		String textEditor = null;
		try {
			textEditor = ("");
			BufferedReader bufferEntrada = new BufferedReader(new FileReader("arquivosTeste/entrada1.txt"));
			String linhaLida;
	        while((linhaLida = bufferEntrada.readLine()) != null)
	        {
	            textEditor = textEditor + (linhaLida+"\n");
	        }
	        bufferEntrada.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//fail("Not yet implemented");
		String comandos = null;
		ANTLRInputStream antlrInput = new ANTLRInputStream(textEditor);

		GramaticaSimpleJavaLexer lexer = new GramaticaSimpleJavaLexer(antlrInput);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		GramaticaSimpleJavaParser parser = new GramaticaSimpleJavaParser(tokens);
		parser.removeErrorListeners();
		//parser.addErrorListener(new ErroSintaticoListener());
		ParseTree tree = parser.prog();
		SimpleJavaVisitor loader = new SimpleJavaVisitor();
		loader.visit(tree);
		try {
			new Semantica(loader.getAST());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ASTJung astJung = new ASTJung(loader.getAST());
		//astJung.mostrarASTJung();
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(0).getRotulo(), "soma");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(1).getRotulo(), "param");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(2).getRotulo(), "float");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(3).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(4).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(5).getRotulo(), "var");
		assertEquals(loader.getAST().getFilhos().get(1).getFilhos().get(6).getRotulo(), "if");
		//Filhos da AST = nome/function/main
	}
	
}
	

