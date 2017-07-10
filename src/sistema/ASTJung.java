/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import antlr.GramaticaSimpleJavaLexer;
import antlr.GramaticaSimpleJavaParser;
import ast.AST;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.LayoutDecorator;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.layout.ObservableCachingLayout;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;




import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

/**
 *
 * @author Karoline
 */
public class ASTJung {
    Graph<AST, Number> g = new DelegateForest<AST, Number>();
    AST ast;
    int aresta;
    public ASTJung(AST ast){
       this.ast = ast;
       aresta = 0;
       this.preencheArvore(ast);
        
    }
    
    public AST preencheArvore(AST ast){
        this.insereVertice(ast);
        
        if(ast.getFilhos() != null){
            for(AST filho : ast.getFilhos()){
                this.insereAresta(ast,filho);
            }
            for(AST filho : ast.getFilhos()){
                this.preencheArvore(filho);
            }
        }else{
            return ast;
        }
        return ast;
    }
    
    public void insereVertice(AST ast){
       g.addVertex(ast);
    }
    
    public void insereAresta(AST v1, AST v2){
        aresta++;
        g.addEdge(aresta,v1, v2);
    }
    
    public void mostrarASTJung(){
    	//ASTJung sgv = new ASTJung(ast); //We create our graph in here
    	Forest<AST, Number> arvore;
        //Layout do grafo
        MinimumSpanningForest2<AST,Number> prim = 
             	new MinimumSpanningForest2<AST,Number>(g,
             		new DelegateForest<AST,Number>(), DelegateTree.<AST,Number>getFactory(),
             		new ConstantTransformer(1.0));
             
             arvore = prim.getForest();
        
        Layout<AST,Number> layout = new TreeLayout<AST,Number>(arvore);
        VisualizationViewer<AST,Number> vv = new VisualizationViewer<AST, Number>(layout);
        
        vv.setPreferredSize(new Dimension(500,500)); //Sets the viewing area size
        
        //Label do vertice
        Transformer<AST,String> vertex = new Transformer<AST,String>() {

            @Override
            public String transform(AST i) {
                return i.getRotulo();
            }

            
        };
        
        //Cor do vertice
        
        Transformer<AST,Paint> vertexColor = new Transformer<AST,Paint>() {
            public Paint transform(AST i) {
                return Color.white;
            }
        };
        
        //dimensao do vertice
         
        Transformer<AST,Shape> vertexSize = new Transformer<AST,Shape>(){
        	int length_rotulo = 0; //verifica tamanho do rotulo da AST
        	public Shape transform(AST i){
            	length_rotulo = i.getRotulo().length();
            	if(length_rotulo < 8){
            		Ellipse2D circle = new Ellipse2D.Double(-20, -20, 40, 40);
            		return circle;
            	}else{
            		Ellipse2D circle = new Ellipse2D.Double(-1*length_rotulo, -1*length_rotulo, 6.5*length_rotulo, 3*length_rotulo);
            		return circle;
            	}
                // in this case, the vertex is twice as large
                //if(i == 2) return AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
                
            }
        };
        
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm);
        
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        
        vv.getRenderContext().setVertexLabelTransformer(vertex);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        
        
        JFrame frame = new JFrame("arvore sintatica");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);
    }
    
   /* public static void main(String[] args) throws IOException {
    	AST ast;
    	
    	String inputFile = "C:\\Users\\Karoline\\Dropbox\\8 Periodo\\Compiladores\\Trabalho Final\\TrabalhoFinalV1\\exemplo1.txt";
        File arquivo = new File(inputFile);
        InputStream is = new FileInputStream(arquivo);
        
        ANTLRInputStream antlrInput = new ANTLRInputStream(is);
        GramaticaSimpleJavaLexer lexer = new GramaticaSimpleJavaLexer(antlrInput);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GramaticaSimpleJavaParser parser = new GramaticaSimpleJavaParser(tokens);
        ParseTree tree = parser.prog();
        T1Visitor loader = new T1Visitor();
        loader.visit(tree);
        ast = loader.getAST();
        
        ASTJung a = new ASTJung(ast);
        a.mostrarASTJung();
	}*/
}