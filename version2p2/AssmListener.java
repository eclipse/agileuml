// Generated from Assm.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AssmParser}.
 */
public interface AssmListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AssmParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(AssmParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link AssmParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(AssmParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link AssmParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterStat(AssmParser.StatContext ctx);
	/**
	 * Exit a parse tree produced by {@link AssmParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitStat(AssmParser.StatContext ctx);
	/**
	 * Enter a parse tree produced by {@link AssmParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(AssmParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AssmParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(AssmParser.ExprContext ctx);
}