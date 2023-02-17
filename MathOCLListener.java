// Generated from MathOCL.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MathOCLParser}.
 */
public interface MathOCLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#specification}.
	 * @param ctx the parse tree
	 */
	void enterSpecification(MathOCLParser.SpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#specification}.
	 * @param ctx the parse tree
	 */
	void exitSpecification(MathOCLParser.SpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#part}.
	 * @param ctx the parse tree
	 */
	void enterPart(MathOCLParser.PartContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#part}.
	 * @param ctx the parse tree
	 */
	void exitPart(MathOCLParser.PartContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#formula}.
	 * @param ctx the parse tree
	 */
	void enterFormula(MathOCLParser.FormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#formula}.
	 * @param ctx the parse tree
	 */
	void exitFormula(MathOCLParser.FormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#constraint}.
	 * @param ctx the parse tree
	 */
	void enterConstraint(MathOCLParser.ConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#constraint}.
	 * @param ctx the parse tree
	 */
	void exitConstraint(MathOCLParser.ConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#reexpression}.
	 * @param ctx the parse tree
	 */
	void enterReexpression(MathOCLParser.ReexpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#reexpression}.
	 * @param ctx the parse tree
	 */
	void exitReexpression(MathOCLParser.ReexpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#simplify}.
	 * @param ctx the parse tree
	 */
	void enterSimplify(MathOCLParser.SimplifyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#simplify}.
	 * @param ctx the parse tree
	 */
	void exitSimplify(MathOCLParser.SimplifyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#substituting}.
	 * @param ctx the parse tree
	 */
	void enterSubstituting(MathOCLParser.SubstitutingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#substituting}.
	 * @param ctx the parse tree
	 */
	void exitSubstituting(MathOCLParser.SubstitutingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#solve}.
	 * @param ctx the parse tree
	 */
	void enterSolve(MathOCLParser.SolveContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#solve}.
	 * @param ctx the parse tree
	 */
	void exitSolve(MathOCLParser.SolveContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#prove}.
	 * @param ctx the parse tree
	 */
	void enterProve(MathOCLParser.ProveContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#prove}.
	 * @param ctx the parse tree
	 */
	void exitProve(MathOCLParser.ProveContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#expanding}.
	 * @param ctx the parse tree
	 */
	void enterExpanding(MathOCLParser.ExpandingContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#expanding}.
	 * @param ctx the parse tree
	 */
	void exitExpanding(MathOCLParser.ExpandingContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#factorBy}.
	 * @param ctx the parse tree
	 */
	void enterFactorBy(MathOCLParser.FactorByContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#factorBy}.
	 * @param ctx the parse tree
	 */
	void exitFactorBy(MathOCLParser.FactorByContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(MathOCLParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(MathOCLParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MathOCLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MathOCLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MathOCLParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MathOCLParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MathOCLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MathOCLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void enterBasicExpression(MathOCLParser.BasicExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void exitBasicExpression(MathOCLParser.BasicExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(MathOCLParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(MathOCLParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(MathOCLParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(MathOCLParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void enterLetExpression(MathOCLParser.LetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void exitLetExpression(MathOCLParser.LetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(MathOCLParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(MathOCLParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(MathOCLParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(MathOCLParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(MathOCLParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(MathOCLParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#factorExpression}.
	 * @param ctx the parse tree
	 */
	void enterFactorExpression(MathOCLParser.FactorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#factorExpression}.
	 * @param ctx the parse tree
	 */
	void exitFactorExpression(MathOCLParser.FactorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#factor2Expression}.
	 * @param ctx the parse tree
	 */
	void enterFactor2Expression(MathOCLParser.Factor2ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#factor2Expression}.
	 * @param ctx the parse tree
	 */
	void exitFactor2Expression(MathOCLParser.Factor2ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void enterSetExpression(MathOCLParser.SetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void exitSetExpression(MathOCLParser.SetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MathOCLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(MathOCLParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link MathOCLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(MathOCLParser.IdentifierContext ctx);
}