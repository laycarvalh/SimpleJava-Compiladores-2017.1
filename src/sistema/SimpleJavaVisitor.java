package sistema;


import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;

import antlr.GramaticaSimpleJavaBaseVisitor;
import antlr.GramaticaSimpleJavaParser;
import ast.AST;

public class SimpleJavaVisitor extends GramaticaSimpleJavaBaseVisitor<AST>{
    AST noProg, noDeclaracaoBody, noFuncaoBody, noPrincipalBody;
    
    @Override
    public AST visitBody(GramaticaSimpleJavaParser.BodyContext ctx){
        int i = 0;
        String prog = "prog";
        noProg = new AST(prog);
        
        AST nomeClasse = new AST(ctx.ID().getText());
        nomeClasse.setLinha(ctx.ID().getSymbol().getLine());
        
        
        noProg.adicionaFilho(nomeClasse);
        
        noProg.setLinha(ctx.getStart().getLine());
        
        try{
            noDeclaracaoBody = visit(ctx.declaracao(i));
        }catch(NullPointerException e){
            noDeclaracaoBody = null;
        }
        while(noDeclaracaoBody != null){
            noProg.adicionaFilho(noDeclaracaoBody);
            i++;
            try{
                noDeclaracaoBody = visit(ctx.declaracao(i));
            }catch(NullPointerException e){
                noDeclaracaoBody = null;
            }      
        }
        
        i = 0;
        
        try{
            noFuncaoBody = visit(ctx.funcao(i));
        }catch(NullPointerException e){
            noFuncaoBody = null;
        }
        
        while(noFuncaoBody != null){
            noProg.adicionaFilho(noFuncaoBody);
            i++;
            try{
                noFuncaoBody = visit(ctx.funcao(i));
            }catch(NullPointerException e){
                noFuncaoBody = null;
            }
        }
        
        noPrincipalBody = visit(ctx.principal());
        noProg.adicionaFilho(noPrincipalBody);
        return noProg;
    }
    
    @Override
    public AST visitMain(GramaticaSimpleJavaParser.MainContext ctx){
        int i = 0;
        AST noMain = new AST("main");
        
        
        noMain.setLinha(ctx.getStart().getLine());
        
        AST declaracao, comandos;
        try{
            declaracao = visit(ctx.declaracao(i));
        }catch(NullPointerException e){
            declaracao = null;
        }
        while (declaracao != null){
            noMain.adicionaFilho(declaracao);
            i++;
            try{
                declaracao = visit(ctx.declaracao(i));
            }catch(NullPointerException e){
                declaracao = null;
            }
        }
        
        i = 0;
        
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        while (comandos != null){
            noMain.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        
        return noMain;
    }
    
    @Override
    public AST visitDeclaracaoVariaveis(GramaticaSimpleJavaParser.DeclaracaoVariaveisContext ctx){
    	
    	return visit(ctx.declaracao_var());
    }
    
    @Override
    public AST visitVariaveis(GramaticaSimpleJavaParser.VariaveisContext ctx){
        AST noVariavel = new AST("var");
        
        noVariavel.setLinha(ctx.getStart().getLine());
        
        AST noTipo = visit(ctx.tipo());
        noVariavel.adicionaFilho(noTipo);
        AST noListaIds = visit(ctx.lista_ids());
        noVariavel.adicionaFilho(noListaIds);
        
        return noVariavel;
    }
    
    @Override
    public AST visitTipoInt(GramaticaSimpleJavaParser.TipoIntContext ctx){
        AST noTipoInt = new AST("int");
        
        noTipoInt.setLinha(ctx.getStart().getLine());
        
        return noTipoInt;
    }
    
    @Override
    public AST visitTipoReal(GramaticaSimpleJavaParser.TipoRealContext ctx){
        AST noTipoReal = new AST("real");
        
        noTipoReal.setLinha(ctx.getStart().getLine());
        
        return noTipoReal;
    }
    
    @Override
    public AST visitTipoString(GramaticaSimpleJavaParser.TipoStringContext ctx){
        AST noTipoString = new AST("string");
        
        noTipoString.setLinha(ctx.getStart().getLine());
        
        return noTipoString;
    }
    
    @Override
    public AST visitTipoBool(GramaticaSimpleJavaParser.TipoBoolContext ctx){
        AST noTipoBoolean = new AST("bool");
        
        noTipoBoolean.setLinha(ctx.getStart().getLine());
        
        return noTipoBoolean;
    }
    
    @Override
    public AST visitListaIds(GramaticaSimpleJavaParser.ListaIdsContext ctx){
        AST noListaIds = new AST(",");
        
        noListaIds.setLinha(ctx.getStart().getLine());
        
        AST id = new AST(ctx.ID().getText());
        
        id.setLinha(ctx.ID().getSymbol().getLine());
        
        noListaIds.adicionaFilho(id);
        AST listaIds = visit(ctx.lista_ids());
        
        noListaIds.adicionaFilho(listaIds);
        return noListaIds;
    }
    
    @Override
    public AST visitListaIdsID(GramaticaSimpleJavaParser.ListaIdsIDContext ctx){
        AST noID = new AST(ctx.ID().getText());
        noID.setLinha(ctx.ID().getSymbol().getLine());
        return noID;
    }
    
    @Override
    public AST visitDeclaracaoContantes(GramaticaSimpleJavaParser.DeclaracaoContantesContext ctx){
        return visit(ctx.declaracao_const());
    }
    
    @Override
    public AST visitContantes(GramaticaSimpleJavaParser.ContantesContext ctx){
        AST noConstante = new AST("const");
        
        noConstante.setLinha(ctx.getStart().getLine());
        
        AST nomeConstante = new AST(ctx.ID().getText());
        
        nomeConstante.setLinha(ctx.ID().getSymbol().getLine());
        
        noConstante.adicionaFilho(nomeConstante);
        AST tipo = visit(ctx.tipo());
        AST valor = null;
        if(ctx.BOOL() != null){
            valor = new AST(ctx.BOOL().getText());
            valor.setLinha(ctx.BOOL().getSymbol().getLine());
        }else if(ctx.INT() != null){
            valor = new AST(ctx.INT().getText());
            valor.setLinha(ctx.INT().getSymbol().getLine());
        }else if(ctx.REAL() != null){
            valor = new AST(ctx.REAL().getText());
            valor.setLinha(ctx.REAL().getSymbol().getLine());
        }else{
            valor = new AST(ctx.STRING().getText());
            valor.setLinha(ctx.STRING().getSymbol().getLine());
        }
        noConstante.adicionaFilho(tipo);
        noConstante.adicionaFilho(valor);
        return noConstante;
    }
    
    @Override
    public AST visitFunction(GramaticaSimpleJavaParser.FunctionContext ctx){
        int i = 0;
        AST declaracao;
        AST comandos;
                
        AST noFuncao = new AST("function");
        
        noFuncao.setLinha(ctx.getStart().getLine());
        
        AST nomeFuncao = new AST(ctx.ID().getText());
        nomeFuncao.setLinha(ctx.ID().getSymbol().getLine());
        
        noFuncao.adicionaFilho(nomeFuncao);
        
        AST listaParam = visit(ctx.lista_de_parametros());
        AST param = new AST("param");
        param.adicionaFilho(listaParam);
        noFuncao.adicionaFilho(param);
        
        AST tipo = visit(ctx.tipo());
        noFuncao.adicionaFilho(tipo);
        
        try{
            declaracao = visit(ctx.declaracao(i));
        }catch(NullPointerException e){
            declaracao = null;
        }
        while (declaracao != null){
            noFuncao.adicionaFilho(declaracao);
            i++;
            try{
                declaracao = visit(ctx.declaracao(i));
            }catch(NullPointerException e){
                declaracao = null;
            }
        }
        i = 0;
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        while (comandos != null){
            noFuncao.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        return noFuncao;
    }
    
    @Override
    public AST visitListaParam(GramaticaSimpleJavaParser.ListaParamContext ctx){
        AST noListaParam = new AST(",");
        
        noListaParam.setLinha(ctx.getStart().getLine());
        
        AST parametro = visit(ctx.parametro());
        AST listaParametro = visit(ctx.lista_de_parametros());
        noListaParam.adicionaFilho(parametro);
        noListaParam.adicionaFilho(listaParametro);
        return noListaParam;
    }
    
    @Override
    public AST visitParam(GramaticaSimpleJavaParser.ParamContext ctx){
        return visit(ctx.parametro());
    }
    
    @Override
    public AST visitEmptyParam(GramaticaSimpleJavaParser.EmptyParamContext ctx){
        AST noVazio = new AST("");
        
        noVazio.setLinha(ctx.getStart().getLine());
        
        return noVazio;
    }
    
    @Override
    public AST visitParameter(GramaticaSimpleJavaParser.ParameterContext ctx){
        AST noTipo = visit(ctx.tipo());
        AST noID = new AST(ctx.ID().getText());
        noID.setLinha(ctx.ID().getSymbol().getLine());
        noTipo.adicionaFilho(noID);
        return noTipo;
    }
    
    @Override
    public AST visitComandoPrint(GramaticaSimpleJavaParser.ComandoPrintContext ctx){
        AST print = new AST("print");
        
        print.setLinha(ctx.getStart().getLine());
        
        AST listaExpr = visit(ctx.lista_de_expr());
        print.adicionaFilho(listaExpr);
        return print;
    }
    
    @Override
    public AST visitListExpression(GramaticaSimpleJavaParser.ListExpressionContext ctx){
        AST noListExpr = new AST(",");
        
        noListExpr.setLinha(ctx.getStart().getLine());
        
        AST expr = visit(ctx.expr());
        noListExpr.adicionaFilho(expr);
        AST listExpr = visit(ctx.lista_de_expr());
        noListExpr.adicionaFilho(listExpr);
        return noListExpr;
    }
    
    @Override
    public AST visitEmptyExpression(GramaticaSimpleJavaParser.EmptyExpressionContext ctx){
        AST noVazio = new AST("");

        noVazio.setLinha(ctx.getStart().getLine());
        
        return noVazio;
    }
    
    @Override
    public AST visitExpression(GramaticaSimpleJavaParser.ExpressionContext ctx){
        return visit(ctx.expr());
    }
    
    @Override
    public AST visitExprParentese(GramaticaSimpleJavaParser.ExprParenteseContext ctx){
        return visit(ctx.parentese());
    }
    
    @Override
    public AST visitParens(GramaticaSimpleJavaParser.ParensContext ctx){
        return visit(ctx.expr());
    }
    
    @Override
    public AST visitFat(GramaticaSimpleJavaParser.FatContext ctx){
        return visit(ctx.fator());
    }
    
    @Override
    public AST visitFatorNumero(GramaticaSimpleJavaParser.FatorNumeroContext ctx){
        return visit(ctx.numero());
    }
    
    @Override
    public AST visitNumber(GramaticaSimpleJavaParser.NumberContext ctx){
        AST noNumero = new AST(ctx.NUM().getText());
        noNumero.setLinha(ctx.NUM().getSymbol().getLine());
        return noNumero;
    }
    
    @Override
    public AST visitFatorBool(GramaticaSimpleJavaParser.FatorBoolContext ctx){
        AST noBool = new AST(ctx.BOOL().getText());
        noBool.setLinha(ctx.BOOL().getSymbol().getLine());
        return noBool;
    } 
    
    @Override
    public AST visitFatorString(GramaticaSimpleJavaParser.FatorStringContext ctx){
        AST noString = new AST(ctx.STRING().getText());
        noString.setLinha(ctx.STRING().getSymbol().getLine());
        return noString;
    }
    
    @Override
    public AST visitFatorIdentificador(GramaticaSimpleJavaParser.FatorIdentificadorContext ctx){
        return visit(ctx.identificador());
    }
    
    @Override
    public AST visitId(GramaticaSimpleJavaParser.IdContext ctx){
        AST noId = new AST(ctx.ID().getText());
        noId.setLinha(ctx.ID().getSymbol().getLine());
        return noId;
    }
    
    @Override
    public AST visitFatorChamadaFuncaoAninhada(GramaticaSimpleJavaParser.FatorChamadaFuncaoAninhadaContext ctx){
        return visit(ctx.chamada_de_funcao_aninhada());
    }
    
    @Override
    public AST visitChamadaFuncaoAninhada(GramaticaSimpleJavaParser.ChamadaFuncaoAninhadaContext ctx){
        AST noChamadaFuncao = new AST("chamada_funcao");
        AST nomeFuncao= new AST(ctx.ID().getText());
        noChamadaFuncao.adicionaFilho(nomeFuncao);
        noChamadaFuncao.setLinha(ctx.ID().getSymbol().getLine());
        AST listaExpr = visit(ctx.lista_de_expr());
        noChamadaFuncao.adicionaFilho(listaExpr);
        return noChamadaFuncao;
    }
    
    @Override
    public AST visitExprNot(GramaticaSimpleJavaParser.ExprNotContext ctx){
        AST not = new AST("!");
        not.setLinha(ctx.getStart().getLine());
        AST opnot = visit(ctx.opnot());
        not.adicionaFilho(opnot);
        return not;
    }
    
    @Override
    public AST visitOpnotRelacional(GramaticaSimpleJavaParser.OpnotRelacionalContext ctx){
    	return visit(ctx.oprelacional());
    }
    
    @Override
    public AST visitOpnotFator(GramaticaSimpleJavaParser.OpnotFatorContext ctx){
    	return visit(ctx.fator());
    }
    
    @Override
    public AST visitExprMinus(GramaticaSimpleJavaParser.ExprMinusContext ctx){
        AST minus = new AST("-");
        minus.setLinha(ctx.getStart().getLine());
        AST expr = visit(ctx.expr());
        minus.adicionaFilho(expr);
        return minus;
    }
    
    @Override
    public AST visitExprMultdiv(GramaticaSimpleJavaParser.ExprMultdivContext ctx){
        AST mult_div = new AST(ctx.s.getText());
        mult_div.setLinha(ctx.s.getLine());
        AST expr1 = visit(ctx.expr(0));
        AST expr2 = visit(ctx.expr(1));
        mult_div.adicionaFilho(expr1);
        mult_div.adicionaFilho(expr2);
        return mult_div;
    }
    
    @Override
    public AST visitExprSomasub(GramaticaSimpleJavaParser.ExprSomasubContext ctx){
        AST soma_sub = new AST(ctx.s.getText());
        soma_sub.setLinha(ctx.s.getLine());
        AST expr1 = visit(ctx.expr(0));
        AST expr2 = visit(ctx.expr(1));
        soma_sub.adicionaFilho(expr1);
        soma_sub.adicionaFilho(expr2);
        return soma_sub;
    }
    
    @Override
    public AST visitComandoAtribuicao(GramaticaSimpleJavaParser.ComandoAtribuicaoContext ctx){
        return visit(ctx.atribuicao());
    }
    
    @Override
    public AST visitAtribution(GramaticaSimpleJavaParser.AtributionContext ctx){
        AST noAtribuicao = new AST("=");
        noAtribuicao.setLinha(ctx.getStart().getLine());
        AST ID = new AST(ctx.ID().getText());
        ID.setLinha(ctx.ID().getSymbol().getLine());
        noAtribuicao.adicionaFilho(ID);
        AST expr = visit(ctx.expr());
        noAtribuicao.adicionaFilho(expr);
        return noAtribuicao;
    }
    
    @Override
    public AST visitComandoBreak(GramaticaSimpleJavaParser.ComandoBreakContext ctx){
        AST noBreak = new AST("break");
        noBreak.setLinha(ctx.getStart().getLine());
        return noBreak;
    }
    
    @Override
    public AST visitComadoScan(GramaticaSimpleJavaParser.ComadoScanContext ctx){
        AST noScan = new AST("scan");
        noScan.setLinha(ctx.getStart().getLine());
        AST listaIds = visit(ctx.lista_ids());
        noScan.adicionaFilho(listaIds);
        return noScan;
    }
    
    @Override
    public AST visitComandoChamadaFuncao(GramaticaSimpleJavaParser.ComandoChamadaFuncaoContext ctx){
        return visit(ctx.chamada_de_funcao());
    }
    
    @Override
    public AST visitChamadaFuncao(GramaticaSimpleJavaParser.ChamadaFuncaoContext ctx){
    	AST noChamadaFuncao = new AST("chamada_funcao");
        AST nomeFuncao= new AST(ctx.ID().getText());
        noChamadaFuncao.adicionaFilho(nomeFuncao);
        noChamadaFuncao.setLinha(ctx.ID().getSymbol().getLine());
        AST listaExpr = visit(ctx.lista_de_expr());
        noChamadaFuncao.adicionaFilho(listaExpr);
        return noChamadaFuncao;
    }
    
    @Override
    public AST visitComandoRetorno(GramaticaSimpleJavaParser.ComandoRetornoContext ctx){
        return visit(ctx.retorno());
    }
    
    @Override
    public AST visitReturn(GramaticaSimpleJavaParser.ReturnContext ctx){
        AST noRetorno = new AST("return");
        noRetorno.setLinha(ctx.getStart().getLine());
        AST expr = visit(ctx.expr());
        noRetorno.adicionaFilho(expr);
        return noRetorno;
    }
    
    @Override
    public AST visitComandoControle(GramaticaSimpleJavaParser.ComandoControleContext ctx){
        return visit(ctx.comandos_controle());
    }
    
    @Override
    public AST visitComandoControleIf(GramaticaSimpleJavaParser.ComandoControleIfContext ctx){
        int i = 0;
        AST comandos;
        AST comando_else;
        AST noIf = new AST("if");
        noIf.setLinha(ctx.getStart().getLine());
        AST oprelacional = visit(ctx.oprelacional());
        noIf.adicionaFilho(oprelacional);
        
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        
        while (comandos != null){
            noIf.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        try{
        	comando_else = visit(ctx.comando_else());
        }catch(Exception e){
        	comando_else = null;
        }
        
        if(comando_else != null)
            noIf.adicionaFilho(comando_else);

        return noIf;
    }
    
    @Override
    public AST visitControleElse(GramaticaSimpleJavaParser.ControleElseContext ctx){
        int i = 0;
        AST noElse = new AST("else");
        noElse.setLinha(ctx.getStart().getLine());
        AST comandos;
        
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        
        while(comandos != null){
            noElse.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        
        return noElse;
    }
    
    @Override
    public AST visitOperadorRelacional(GramaticaSimpleJavaParser.OperadorRelacionalContext ctx){
        AST noRelacional = new AST(ctx.s.getText());
        noRelacional.setLinha(ctx.s.getLine());
        AST expr1 = visit(ctx.v1);
        AST expr2 = visit(ctx.v2);
        noRelacional.adicionaFilho(expr1);
        noRelacional.adicionaFilho(expr2);
        return noRelacional;
    }
    
    @Override
    public AST visitOperadorRelacionalExpr(GramaticaSimpleJavaParser.OperadorRelacionalExprContext ctx){
        return visit(ctx.opnot());
    }
    
    @Override
    public AST visitComandoControleWhile(GramaticaSimpleJavaParser.ComandoControleWhileContext ctx){
        int i = 0;
        AST Comandowhile = new AST("while");
        Comandowhile.setLinha(ctx.getStart().getLine());
        AST oprelacional = visit(ctx.oprelacional());
        Comandowhile.adicionaFilho(oprelacional);
        
        AST comandos;
        
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        
        while(comandos != null){
            Comandowhile.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        return Comandowhile;
    }
    
    @Override
    public AST visitComandoControleFor(GramaticaSimpleJavaParser.ComandoControleForContext ctx){
        int i = 0;
        AST noFor = new AST("for");
        noFor.setLinha(ctx.getStart().getLine());
        AST valorInicial = new AST("valor_inicial");
        valorInicial.setLinha(ctx.getStart().getLine());
        
        AST valorFinal = new AST("valor_final");
        valorFinal.setLinha(ctx.getStart().getLine());
        
        AST identificador = visit(ctx.identificador());
        AST valor1 = visit(ctx.valor(0));
        AST valor2 = visit(ctx.valor(1));
        AST noStepFor;
        try{
        	noStepFor = visit(ctx.step_for());
        }catch(Exception e){
        	noStepFor = null;
        }
        
        
        valorInicial.adicionaFilho(identificador);
        valorInicial.adicionaFilho(valor1);
        
        valorFinal.adicionaFilho(valor2);
        
        noFor.adicionaFilho(valorInicial);
        noFor.adicionaFilho(valorFinal);
        
        
        if(noStepFor != null) noFor.adicionaFilho(noStepFor);
        
        AST comandos;
        try{
            comandos = visit(ctx.comandos(i));
        }catch(NullPointerException e){
            comandos = null;
        }
        
        while(comandos != null){
            noFor.adicionaFilho(comandos);
            i++;
            try{
                comandos = visit(ctx.comandos(i));
            }catch(NullPointerException e){
                comandos = null;
            }
        }
        
        return noFor;
    }
    
    @Override
    public AST visitStepFor(GramaticaSimpleJavaParser.StepForContext ctx){
        AST step = new AST("step");
        step.setLinha(ctx.getStart().getLine());
        AST valor = visit(ctx.valor());
        step.adicionaFilho(valor);
        return step;
    }
    
    public AST getAST(){
        return noProg;
    }
}
