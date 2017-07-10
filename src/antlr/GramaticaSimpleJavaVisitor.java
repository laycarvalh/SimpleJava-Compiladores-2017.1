// Generated from C:\Users\layca\Documents\NetBeansProjects\SimpleJava\src\antlr\GramaticaSimpleJava.g4 by ANTLR 4.2.2
package antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GramaticaSimpleJavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GramaticaSimpleJavaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprSomasub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprSomasub(@NotNull GramaticaSimpleJavaParser.ExprSomasubContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#controleElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControleElse(@NotNull GramaticaSimpleJavaParser.ControleElseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#chamadaFuncao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChamadaFuncao(@NotNull GramaticaSimpleJavaParser.ChamadaFuncaoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoBreak}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoBreak(@NotNull GramaticaSimpleJavaParser.ComandoBreakContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#chamadaFuncaoAninhada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChamadaFuncaoAninhada(@NotNull GramaticaSimpleJavaParser.ChamadaFuncaoAninhadaContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprParentese}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprParentese(@NotNull GramaticaSimpleJavaParser.ExprParenteseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#declaracaoVariaveis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracaoVariaveis(@NotNull GramaticaSimpleJavaParser.DeclaracaoVariaveisContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#listaIds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaIds(@NotNull GramaticaSimpleJavaParser.ListaIdsContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#tipoString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoString(@NotNull GramaticaSimpleJavaParser.TipoStringContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoControle}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoControle(@NotNull GramaticaSimpleJavaParser.ComandoControleContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Atribution}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtribution(@NotNull GramaticaSimpleJavaParser.AtributionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#valorID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValorID(@NotNull GramaticaSimpleJavaParser.ValorIDContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoControleWhile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoControleWhile(@NotNull GramaticaSimpleJavaParser.ComandoControleWhileContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoChamadaFuncao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoChamadaFuncao(@NotNull GramaticaSimpleJavaParser.ComandoChamadaFuncaoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#fatorNumero}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFatorNumero(@NotNull GramaticaSimpleJavaParser.FatorNumeroContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#valorINT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValorINT(@NotNull GramaticaSimpleJavaParser.ValorINTContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#tipoReal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoReal(@NotNull GramaticaSimpleJavaParser.TipoRealContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoControleIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoControleIf(@NotNull GramaticaSimpleJavaParser.ComandoControleIfContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#fatorString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFatorString(@NotNull GramaticaSimpleJavaParser.FatorStringContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Contantes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContantes(@NotNull GramaticaSimpleJavaParser.ContantesContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoRetorno}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoRetorno(@NotNull GramaticaSimpleJavaParser.ComandoRetornoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(@NotNull GramaticaSimpleJavaParser.IdContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comadoScan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComadoScan(@NotNull GramaticaSimpleJavaParser.ComadoScanContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#fatorIdentificador}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFatorIdentificador(@NotNull GramaticaSimpleJavaParser.FatorIdentificadorContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprMultdiv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprMultdiv(@NotNull GramaticaSimpleJavaParser.ExprMultdivContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#emptyParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyParam(@NotNull GramaticaSimpleJavaParser.EmptyParamContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#tipoInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoInt(@NotNull GramaticaSimpleJavaParser.TipoIntContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprMinus}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprMinus(@NotNull GramaticaSimpleJavaParser.ExprMinusContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#opnotRelacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpnotRelacional(@NotNull GramaticaSimpleJavaParser.OpnotRelacionalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Parens}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParens(@NotNull GramaticaSimpleJavaParser.ParensContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#operadorRelacionalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperadorRelacionalExpr(@NotNull GramaticaSimpleJavaParser.OperadorRelacionalExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoControleFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoControleFor(@NotNull GramaticaSimpleJavaParser.ComandoControleForContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(@NotNull GramaticaSimpleJavaParser.FunctionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#listaIdsID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaIdsID(@NotNull GramaticaSimpleJavaParser.ListaIdsIDContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#opnotFator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpnotFator(@NotNull GramaticaSimpleJavaParser.OpnotFatorContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(@NotNull GramaticaSimpleJavaParser.NumberContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprPot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprPot(@NotNull GramaticaSimpleJavaParser.ExprPotContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#operadorRelacional}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperadorRelacional(@NotNull GramaticaSimpleJavaParser.OperadorRelacionalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#listExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListExpression(@NotNull GramaticaSimpleJavaParser.ListExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBody(@NotNull GramaticaSimpleJavaParser.BodyContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#fatorChamadaFuncaoAninhada}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFatorChamadaFuncaoAninhada(@NotNull GramaticaSimpleJavaParser.FatorChamadaFuncaoAninhadaContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Return}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(@NotNull GramaticaSimpleJavaParser.ReturnContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#exprNot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNot(@NotNull GramaticaSimpleJavaParser.ExprNotContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoAtribuicao}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoAtribuicao(@NotNull GramaticaSimpleJavaParser.ComandoAtribuicaoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#StepFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepFor(@NotNull GramaticaSimpleJavaParser.StepForContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#listaParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaParam(@NotNull GramaticaSimpleJavaParser.ListaParamContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Variaveis}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariaveis(@NotNull GramaticaSimpleJavaParser.VariaveisContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#tipoBool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoBool(@NotNull GramaticaSimpleJavaParser.TipoBoolContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#emptyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyExpression(@NotNull GramaticaSimpleJavaParser.EmptyExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#comandoPrint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComandoPrint(@NotNull GramaticaSimpleJavaParser.ComandoPrintContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#declaracaoContantes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracaoContantes(@NotNull GramaticaSimpleJavaParser.DeclaracaoContantesContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull GramaticaSimpleJavaParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(@NotNull GramaticaSimpleJavaParser.ParamContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Fat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFat(@NotNull GramaticaSimpleJavaParser.FatContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(@NotNull GramaticaSimpleJavaParser.ParameterContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#Main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(@NotNull GramaticaSimpleJavaParser.MainContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSimpleJavaParser#fatorBool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFatorBool(@NotNull GramaticaSimpleJavaParser.FatorBoolContext ctx);
}