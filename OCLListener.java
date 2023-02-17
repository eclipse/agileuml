// Generated from OCL.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OCLParser}.
 */
public interface OCLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OCLParser#specification}.
	 * @param ctx the parse tree
	 */
	void enterSpecification(OCLParser.SpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#specification}.
	 * @param ctx the parse tree
	 */
	void exitSpecification(OCLParser.SpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#classifier}.
	 * @param ctx the parse tree
	 */
	void enterClassifier(OCLParser.ClassifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#classifier}.
	 * @param ctx the parse tree
	 */
	void exitClassifier(OCLParser.ClassifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDefinition(OCLParser.InterfaceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#interfaceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDefinition(OCLParser.InterfaceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void enterClassDefinition(OCLParser.ClassDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#classDefinition}.
	 * @param ctx the parse tree
	 */
	void exitClassDefinition(OCLParser.ClassDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(OCLParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(OCLParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#classBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyElement(OCLParser.ClassBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#classBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyElement(OCLParser.ClassBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#attributeDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAttributeDefinition(OCLParser.AttributeDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#attributeDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAttributeDefinition(OCLParser.AttributeDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#operationDefinition}.
	 * @param ctx the parse tree
	 */
	void enterOperationDefinition(OCLParser.OperationDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#operationDefinition}.
	 * @param ctx the parse tree
	 */
	void exitOperationDefinition(OCLParser.OperationDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#parameterDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclarations(OCLParser.ParameterDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#parameterDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclarations(OCLParser.ParameterDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(OCLParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(OCLParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#idList}.
	 * @param ctx the parse tree
	 */
	void enterIdList(OCLParser.IdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#idList}.
	 * @param ctx the parse tree
	 */
	void exitIdList(OCLParser.IdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#usecaseDefinition}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseDefinition(OCLParser.UsecaseDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#usecaseDefinition}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseDefinition(OCLParser.UsecaseDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#usecaseBody}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseBody(OCLParser.UsecaseBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#usecaseBody}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseBody(OCLParser.UsecaseBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#usecaseBodyElement}.
	 * @param ctx the parse tree
	 */
	void enterUsecaseBodyElement(OCLParser.UsecaseBodyElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#usecaseBodyElement}.
	 * @param ctx the parse tree
	 */
	void exitUsecaseBodyElement(OCLParser.UsecaseBodyElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#invariant}.
	 * @param ctx the parse tree
	 */
	void enterInvariant(OCLParser.InvariantContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#invariant}.
	 * @param ctx the parse tree
	 */
	void exitInvariant(OCLParser.InvariantContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void enterStereotype(OCLParser.StereotypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#stereotype}.
	 * @param ctx the parse tree
	 */
	void exitStereotype(OCLParser.StereotypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#enumeration}.
	 * @param ctx the parse tree
	 */
	void enterEnumeration(OCLParser.EnumerationContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#enumeration}.
	 * @param ctx the parse tree
	 */
	void exitEnumeration(OCLParser.EnumerationContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#enumerationLiteral}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationLiteral(OCLParser.EnumerationLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#enumerationLiteral}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationLiteral(OCLParser.EnumerationLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(OCLParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(OCLParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(OCLParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(OCLParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(OCLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(OCLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void enterBasicExpression(OCLParser.BasicExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#basicExpression}.
	 * @param ctx the parse tree
	 */
	void exitBasicExpression(OCLParser.BasicExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(OCLParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(OCLParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(OCLParser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(OCLParser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void enterLetExpression(OCLParser.LetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#letExpression}.
	 * @param ctx the parse tree
	 */
	void exitLetExpression(OCLParser.LetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalExpression(OCLParser.LogicalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#logicalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalExpression(OCLParser.LogicalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(OCLParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(OCLParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(OCLParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(OCLParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#factorExpression}.
	 * @param ctx the parse tree
	 */
	void enterFactorExpression(OCLParser.FactorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#factorExpression}.
	 * @param ctx the parse tree
	 */
	void exitFactorExpression(OCLParser.FactorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#factor2Expression}.
	 * @param ctx the parse tree
	 */
	void enterFactor2Expression(OCLParser.Factor2ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#factor2Expression}.
	 * @param ctx the parse tree
	 */
	void exitFactor2Expression(OCLParser.Factor2ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void enterSetExpression(OCLParser.SetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#setExpression}.
	 * @param ctx the parse tree
	 */
	void exitSetExpression(OCLParser.SetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(OCLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(OCLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#nlpscript}.
	 * @param ctx the parse tree
	 */
	void enterNlpscript(OCLParser.NlpscriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#nlpscript}.
	 * @param ctx the parse tree
	 */
	void exitNlpscript(OCLParser.NlpscriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#nlpstatement}.
	 * @param ctx the parse tree
	 */
	void enterNlpstatement(OCLParser.NlpstatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#nlpstatement}.
	 * @param ctx the parse tree
	 */
	void exitNlpstatement(OCLParser.NlpstatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#loadStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoadStatement(OCLParser.LoadStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#loadStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoadStatement(OCLParser.LoadStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(OCLParser.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(OCLParser.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#storeStatement}.
	 * @param ctx the parse tree
	 */
	void enterStoreStatement(OCLParser.StoreStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#storeStatement}.
	 * @param ctx the parse tree
	 */
	void exitStoreStatement(OCLParser.StoreStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#analyseStatement}.
	 * @param ctx the parse tree
	 */
	void enterAnalyseStatement(OCLParser.AnalyseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#analyseStatement}.
	 * @param ctx the parse tree
	 */
	void exitAnalyseStatement(OCLParser.AnalyseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#displayStatement}.
	 * @param ctx the parse tree
	 */
	void enterDisplayStatement(OCLParser.DisplayStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#displayStatement}.
	 * @param ctx the parse tree
	 */
	void exitDisplayStatement(OCLParser.DisplayStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link OCLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(OCLParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OCLParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(OCLParser.IdentifierContext ctx);
}