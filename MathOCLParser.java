// Generated from MathOCL.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MathOCLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, T__78=79, T__79=80, 
		T__80=81, T__81=82, FLOAT_LITERAL=83, STRING_LITERAL=84, NULL_LITERAL=85, 
		MULTILINE_COMMENT=86, IN=87, NOTIN=88, INTEGRAL=89, SIGMA=90, PRODUCT=91, 
		INFINITY=92, DIFFERENTIAL=93, PARTIALDIFF=94, FORALL=95, EXISTS=96, EMPTYSET=97, 
		SQUAREROOT=98, ARROW=99, NATURAL=100, INTEGER=101, REAL=102, CDOT=103, 
		NEWLINE=104, INT=105, ID=106, WS=107;
	public static final int
		RULE_specification = 0, RULE_part = 1, RULE_formula = 2, RULE_instruction = 3, 
		RULE_constraint = 4, RULE_theorem = 5, RULE_rewrite = 6, RULE_simplify = 7, 
		RULE_substituting = 8, RULE_solve = 9, RULE_prove = 10, RULE_expanding = 11, 
		RULE_substituteIn = 12, RULE_expandTo = 13, RULE_expressAs = 14, RULE_factorBy = 15, 
		RULE_cancelIn = 16, RULE_groupBy = 17, RULE_idList = 18, RULE_type = 19, 
		RULE_expressionList = 20, RULE_expression = 21, RULE_basicExpression = 22, 
		RULE_conditionalExpression = 23, RULE_lambdaExpression = 24, RULE_letExpression = 25, 
		RULE_logicalExpression = 26, RULE_equalityExpression = 27, RULE_additiveExpression = 28, 
		RULE_factorExpression = 29, RULE_factor2Expression = 30, RULE_setExpression = 31, 
		RULE_identifier = 32;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "part", "formula", "instruction", "constraint", "theorem", 
			"rewrite", "simplify", "substituting", "solve", "prove", "expanding", 
			"substituteIn", "expandTo", "expressAs", "factorBy", "cancelIn", "groupBy", 
			"idList", "type", "expressionList", "expression", "basicExpression", 
			"conditionalExpression", "lambdaExpression", "letExpression", "logicalExpression", 
			"equalityExpression", "additiveExpression", "factorExpression", "factor2Expression", 
			"setExpression", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'specification'", "'Define'", "'='", "'~'", "'Constraint'", "'on'", 
			"'|'", "'Theorem'", "'when'", "'Rewrite'", "'to'", "'Simplify'", "'Substitute'", 
			"'for'", "'in'", "'Solve'", "'Prove'", "'if'", "'Expanding'", "'terms'", 
			"'Expand'", "'Express'", "'as'", "'polynomial'", "'Factor'", "'by'", 
			"'Cancel'", "'Group'", "','", "'Sequence'", "'('", "')'", "'Set'", "'Bag'", 
			"'OrderedSet'", "'Map'", "'Function'", "'true'", "'false'", "'?'", "'.'", 
			"'g{'", "'}'", "'then'", "'else'", "'endif'", "'lambda'", "':'", "'let'", 
			"'&'", "'and'", "'or'", "'xor'", "'=>'", "'implies'", "'not'", "'<'", 
			"'>'", "'>='", "'<='", "'/='", "'<>'", "'/:'", "'<:'", "'+'", "'-'", 
			"'..'", "'|->'", "'C_{'", "'^{'", "'E['", "']'", "'lim_{'", "'*'", "'/'", 
			"'mod'", "'div'", "'_{'", "'!'", "'{'", "'Set{'", "'Sequence{'", null, 
			null, "'null'", null, "'\u00A9'", "'\u00A2'", "'\u2021'", "'\u20AC'", 
			"'\u00D7'", "'\u2026'", "'\u00B4'", "'\u00D0'", "'\u00A1'", "'\u00A3'", 
			"'\u00D8'", "'\u2020'", "'\u00BB'", "'\u00D1'", "'\u017D'", "'\u00AE'", 
			"'\u2022'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "FLOAT_LITERAL", 
			"STRING_LITERAL", "NULL_LITERAL", "MULTILINE_COMMENT", "IN", "NOTIN", 
			"INTEGRAL", "SIGMA", "PRODUCT", "INFINITY", "DIFFERENTIAL", "PARTIALDIFF", 
			"FORALL", "EXISTS", "EMPTYSET", "SQUAREROOT", "ARROW", "NATURAL", "INTEGER", 
			"REAL", "CDOT", "NEWLINE", "INT", "ID", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MathOCL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MathOCLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class SpecificationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TerminalNode EOF() { return getToken(MathOCLParser.EOF, 0); }
		public List<PartContext> part() {
			return getRuleContexts(PartContext.class);
		}
		public PartContext part(int i) {
			return getRuleContext(PartContext.class,i);
		}
		public SpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSpecification(this);
		}
	}

	public final SpecificationContext specification() throws RecognitionException {
		SpecificationContext _localctx = new SpecificationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_specification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(T__0);
			setState(67);
			match(ID);
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__4) | (1L << T__7) | (1L << T__9) | (1L << T__11) | (1L << T__12) | (1L << T__15) | (1L << T__16) | (1L << T__18))) != 0)) {
				{
				{
				setState(68);
				part();
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PartContext extends ParserRuleContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public ConstraintContext constraint() {
			return getRuleContext(ConstraintContext.class,0);
		}
		public TheoremContext theorem() {
			return getRuleContext(TheoremContext.class,0);
		}
		public RewriteContext rewrite() {
			return getRuleContext(RewriteContext.class,0);
		}
		public ExpandingContext expanding() {
			return getRuleContext(ExpandingContext.class,0);
		}
		public SimplifyContext simplify() {
			return getRuleContext(SimplifyContext.class,0);
		}
		public SubstitutingContext substituting() {
			return getRuleContext(SubstitutingContext.class,0);
		}
		public SolveContext solve() {
			return getRuleContext(SolveContext.class,0);
		}
		public ProveContext prove() {
			return getRuleContext(ProveContext.class,0);
		}
		public PartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_part; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitPart(this);
		}
	}

	public final PartContext part() throws RecognitionException {
		PartContext _localctx = new PartContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_part);
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				formula();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				constraint();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(78);
				theorem();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(79);
				rewrite();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 5);
				{
				setState(80);
				expanding();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 6);
				{
				setState(81);
				simplify();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 7);
				{
				setState(82);
				substituting();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 8);
				{
				setState(83);
				solve();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 9);
				{
				setState(84);
				prove();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormulaContext extends ParserRuleContext {
		public BasicExpressionContext basicExpression() {
			return getRuleContext(BasicExpressionContext.class,0);
		}
		public InstructionContext instruction() {
			return getRuleContext(InstructionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterFormula(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitFormula(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_formula);
		try {
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(87);
				match(T__1);
				setState(88);
				basicExpression(0);
				setState(89);
				match(T__2);
				setState(92);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__12:
				case T__20:
				case T__21:
				case T__24:
				case T__26:
				case T__27:
					{
					setState(90);
					instruction();
					}
					break;
				case T__17:
				case T__30:
				case T__37:
				case T__38:
				case T__39:
				case T__41:
				case T__46:
				case T__48:
				case T__55:
				case T__64:
				case T__65:
				case T__68:
				case T__70:
				case T__72:
				case T__79:
				case T__80:
				case T__81:
				case FLOAT_LITERAL:
				case STRING_LITERAL:
				case NULL_LITERAL:
				case INTEGRAL:
				case SIGMA:
				case PRODUCT:
				case INFINITY:
				case PARTIALDIFF:
				case FORALL:
				case EXISTS:
				case EMPTYSET:
				case SQUAREROOT:
				case INT:
				case ID:
					{
					setState(91);
					expression();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				match(T__1);
				setState(95);
				basicExpression(0);
				setState(96);
				match(T__3);
				setState(97);
				expression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				match(T__1);
				setState(100);
				basicExpression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InstructionContext extends ParserRuleContext {
		public SubstituteInContext substituteIn() {
			return getRuleContext(SubstituteInContext.class,0);
		}
		public ExpandToContext expandTo() {
			return getRuleContext(ExpandToContext.class,0);
		}
		public ExpressAsContext expressAs() {
			return getRuleContext(ExpressAsContext.class,0);
		}
		public CancelInContext cancelIn() {
			return getRuleContext(CancelInContext.class,0);
		}
		public FactorByContext factorBy() {
			return getRuleContext(FactorByContext.class,0);
		}
		public GroupByContext groupBy() {
			return getRuleContext(GroupByContext.class,0);
		}
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterInstruction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitInstruction(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_instruction);
		try {
			setState(109);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__12:
				enterOuterAlt(_localctx, 1);
				{
				setState(103);
				substituteIn();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(104);
				expandTo();
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 3);
				{
				setState(105);
				expressAs();
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 4);
				{
				setState(106);
				cancelIn();
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 5);
				{
				setState(107);
				factorBy();
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 6);
				{
				setState(108);
				groupBy();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConstraintContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public ConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitConstraint(this);
		}
	}

	public final ConstraintContext constraint() throws RecognitionException {
		ConstraintContext _localctx = new ConstraintContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_constraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(T__4);
			setState(112);
			match(T__5);
			setState(113);
			expression();
			setState(114);
			match(T__6);
			setState(115);
			logicalExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TheoremContext extends ParserRuleContext {
		public List<LogicalExpressionContext> logicalExpression() {
			return getRuleContexts(LogicalExpressionContext.class);
		}
		public LogicalExpressionContext logicalExpression(int i) {
			return getRuleContext(LogicalExpressionContext.class,i);
		}
		public TheoremContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theorem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterTheorem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitTheorem(this);
		}
	}

	public final TheoremContext theorem() throws RecognitionException {
		TheoremContext _localctx = new TheoremContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_theorem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(T__7);
			setState(118);
			logicalExpression(0);
			setState(119);
			match(T__8);
			setState(120);
			logicalExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RewriteContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public RewriteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rewrite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterRewrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitRewrite(this);
		}
	}

	public final RewriteContext rewrite() throws RecognitionException {
		RewriteContext _localctx = new RewriteContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_rewrite);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(T__9);
			setState(123);
			expression();
			setState(124);
			match(T__10);
			setState(125);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimplifyContext extends ParserRuleContext {
		public InstructionContext instruction() {
			return getRuleContext(InstructionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SimplifyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simplify; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSimplify(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSimplify(this);
		}
	}

	public final SimplifyContext simplify() throws RecognitionException {
		SimplifyContext _localctx = new SimplifyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_simplify);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			match(T__11);
			setState(130);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__12:
			case T__20:
			case T__21:
			case T__24:
			case T__26:
			case T__27:
				{
				setState(128);
				instruction();
				}
				break;
			case T__17:
			case T__30:
			case T__37:
			case T__38:
			case T__39:
			case T__41:
			case T__46:
			case T__48:
			case T__55:
			case T__64:
			case T__65:
			case T__68:
			case T__70:
			case T__72:
			case T__79:
			case T__80:
			case T__81:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INTEGRAL:
			case SIGMA:
			case PRODUCT:
			case INFINITY:
			case PARTIALDIFF:
			case FORALL:
			case EXISTS:
			case EMPTYSET:
			case SQUAREROOT:
			case INT:
			case ID:
				{
				setState(129);
				expression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubstitutingContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BasicExpressionContext basicExpression() {
			return getRuleContext(BasicExpressionContext.class,0);
		}
		public SubstitutingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_substituting; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSubstituting(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSubstituting(this);
		}
	}

	public final SubstitutingContext substituting() throws RecognitionException {
		SubstitutingContext _localctx = new SubstitutingContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_substituting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(T__12);
			setState(133);
			expression();
			setState(134);
			match(T__13);
			setState(135);
			basicExpression(0);
			setState(136);
			match(T__14);
			setState(137);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SolveContext extends ParserRuleContext {
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public SolveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_solve; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSolve(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSolve(this);
		}
	}

	public final SolveContext solve() throws RecognitionException {
		SolveContext _localctx = new SolveContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_solve);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(T__15);
			setState(140);
			expressionList();
			setState(141);
			match(T__13);
			setState(142);
			idList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ProveContext extends ParserRuleContext {
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ProveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prove; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterProve(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitProve(this);
		}
	}

	public final ProveContext prove() throws RecognitionException {
		ProveContext _localctx = new ProveContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_prove);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(T__16);
			setState(145);
			logicalExpression(0);
			setState(146);
			match(T__17);
			setState(147);
			expressionList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpandingContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INT() { return getToken(MathOCLParser.INT, 0); }
		public ExpandingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expanding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterExpanding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitExpanding(this);
		}
	}

	public final ExpandingContext expanding() throws RecognitionException {
		ExpandingContext _localctx = new ExpandingContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_expanding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(T__18);
			setState(150);
			expression();
			setState(151);
			match(T__10);
			setState(152);
			match(INT);
			setState(153);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubstituteInContext extends ParserRuleContext {
		public BasicExpressionContext basicExpression() {
			return getRuleContext(BasicExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SubstituteInContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_substituteIn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSubstituteIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSubstituteIn(this);
		}
	}

	public final SubstituteInContext substituteIn() throws RecognitionException {
		SubstituteInContext _localctx = new SubstituteInContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_substituteIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(T__12);
			setState(156);
			basicExpression(0);
			setState(157);
			match(T__14);
			setState(158);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpandToContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode INT() { return getToken(MathOCLParser.INT, 0); }
		public ExpandToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expandTo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterExpandTo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitExpandTo(this);
		}
	}

	public final ExpandToContext expandTo() throws RecognitionException {
		ExpandToContext _localctx = new ExpandToContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_expandTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(T__20);
			setState(161);
			expression();
			setState(162);
			match(T__10);
			setState(163);
			match(INT);
			setState(164);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressAsContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public ExpressAsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressAs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterExpressAs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitExpressAs(this);
		}
	}

	public final ExpressAsContext expressAs() throws RecognitionException {
		ExpressAsContext _localctx = new ExpressAsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_expressAs);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(T__21);
			setState(167);
			expression();
			setState(168);
			match(T__22);
			setState(169);
			match(T__23);
			setState(170);
			match(T__14);
			setState(171);
			identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FactorByContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public FactorByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factorBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterFactorBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitFactorBy(this);
		}
	}

	public final FactorByContext factorBy() throws RecognitionException {
		FactorByContext _localctx = new FactorByContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_factorBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(T__24);
			setState(174);
			expression();
			setState(175);
			match(T__25);
			setState(176);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CancelInContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public CancelInContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cancelIn; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterCancelIn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitCancelIn(this);
		}
	}

	public final CancelInContext cancelIn() throws RecognitionException {
		CancelInContext _localctx = new CancelInContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_cancelIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(T__26);
			setState(179);
			expression();
			setState(180);
			match(T__14);
			setState(181);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GroupByContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public GroupByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterGroupBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitGroupBy(this);
		}
	}

	public final GroupByContext groupBy() throws RecognitionException {
		GroupByContext _localctx = new GroupByContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_groupBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(T__27);
			setState(184);
			expression();
			setState(185);
			match(T__25);
			setState(186);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdListContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(MathOCLParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(MathOCLParser.ID, i);
		}
		public IdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterIdList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitIdList(this);
		}
	}

	public final IdListContext idList() throws RecognitionException {
		IdListContext _localctx = new IdListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(188);
					match(ID);
					setState(189);
					match(T__28);
					}
					} 
				}
				setState(194);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(195);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public TerminalNode NATURAL() { return getToken(MathOCLParser.NATURAL, 0); }
		public TerminalNode INTEGER() { return getToken(MathOCLParser.INTEGER, 0); }
		public TerminalNode REAL() { return getToken(MathOCLParser.REAL, 0); }
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_type);
		try {
			setState(235);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__29:
				enterOuterAlt(_localctx, 1);
				{
				setState(197);
				match(T__29);
				setState(198);
				match(T__30);
				setState(199);
				type();
				setState(200);
				match(T__31);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
				match(T__32);
				setState(203);
				match(T__30);
				setState(204);
				type();
				setState(205);
				match(T__31);
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 3);
				{
				setState(207);
				match(T__33);
				setState(208);
				match(T__30);
				setState(209);
				type();
				setState(210);
				match(T__31);
				}
				break;
			case T__34:
				enterOuterAlt(_localctx, 4);
				{
				setState(212);
				match(T__34);
				setState(213);
				match(T__30);
				setState(214);
				type();
				setState(215);
				match(T__31);
				}
				break;
			case T__35:
				enterOuterAlt(_localctx, 5);
				{
				setState(217);
				match(T__35);
				setState(218);
				match(T__30);
				setState(219);
				type();
				setState(220);
				match(T__28);
				setState(221);
				type();
				setState(222);
				match(T__31);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 6);
				{
				setState(224);
				match(T__36);
				setState(225);
				match(T__30);
				setState(226);
				type();
				setState(227);
				match(T__28);
				setState(228);
				type();
				setState(229);
				match(T__31);
				}
				break;
			case NATURAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(231);
				match(NATURAL);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 8);
				{
				setState(232);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(233);
				match(REAL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(234);
				match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitExpressionList(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(237);
					expression();
					setState(238);
					match(T__28);
					}
					} 
				}
				setState(244);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(245);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public LogicalExpressionContext logicalExpression() {
			return getRuleContext(LogicalExpressionContext.class,0);
		}
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public LetExpressionContext letExpression() {
			return getRuleContext(LetExpressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_expression);
		try {
			setState(251);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__30:
			case T__37:
			case T__38:
			case T__39:
			case T__41:
			case T__55:
			case T__64:
			case T__65:
			case T__68:
			case T__70:
			case T__72:
			case T__79:
			case T__80:
			case T__81:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INTEGRAL:
			case SIGMA:
			case PRODUCT:
			case INFINITY:
			case PARTIALDIFF:
			case FORALL:
			case EXISTS:
			case EMPTYSET:
			case SQUAREROOT:
			case INT:
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				logicalExpression(0);
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				conditionalExpression();
				}
				break;
			case T__46:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				lambdaExpression();
				}
				break;
			case T__48:
				enterOuterAlt(_localctx, 4);
				{
				setState(250);
				letExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BasicExpressionContext extends ParserRuleContext {
		public TerminalNode NULL_LITERAL() { return getToken(MathOCLParser.NULL_LITERAL, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TerminalNode INT() { return getToken(MathOCLParser.INT, 0); }
		public TerminalNode FLOAT_LITERAL() { return getToken(MathOCLParser.FLOAT_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(MathOCLParser.STRING_LITERAL, 0); }
		public TerminalNode INFINITY() { return getToken(MathOCLParser.INFINITY, 0); }
		public TerminalNode EMPTYSET() { return getToken(MathOCLParser.EMPTYSET, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BasicExpressionContext basicExpression() {
			return getRuleContext(BasicExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public BasicExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basicExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterBasicExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitBasicExpression(this);
		}
	}

	public final BasicExpressionContext basicExpression() throws RecognitionException {
		return basicExpression(0);
	}

	private BasicExpressionContext basicExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		BasicExpressionContext _localctx = new BasicExpressionContext(_ctx, _parentState);
		BasicExpressionContext _prevctx = _localctx;
		int _startState = 44;
		enterRecursionRule(_localctx, 44, RULE_basicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL_LITERAL:
				{
				setState(254);
				match(NULL_LITERAL);
				}
				break;
			case T__37:
				{
				setState(255);
				match(T__37);
				}
				break;
			case T__38:
				{
				setState(256);
				match(T__38);
				}
				break;
			case T__39:
				{
				setState(257);
				match(T__39);
				}
				break;
			case ID:
				{
				setState(258);
				identifier();
				}
				break;
			case T__41:
				{
				setState(259);
				match(T__41);
				setState(260);
				match(ID);
				setState(261);
				match(T__42);
				}
				break;
			case INT:
				{
				setState(262);
				match(INT);
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(263);
				match(FLOAT_LITERAL);
				}
				break;
			case STRING_LITERAL:
				{
				setState(264);
				match(STRING_LITERAL);
				}
				break;
			case INFINITY:
				{
				setState(265);
				match(INFINITY);
				}
				break;
			case EMPTYSET:
				{
				setState(266);
				match(EMPTYSET);
				}
				break;
			case T__30:
				{
				setState(267);
				match(T__30);
				setState(268);
				expression();
				setState(269);
				match(T__31);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(284);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(282);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(273);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(274);
						match(T__40);
						setState(275);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(276);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(277);
						match(T__30);
						setState(279);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__30) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__41) | (1L << T__46) | (1L << T__48) | (1L << T__55))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__68 - 65)) | (1L << (T__70 - 65)) | (1L << (T__72 - 65)) | (1L << (T__79 - 65)) | (1L << (T__80 - 65)) | (1L << (T__81 - 65)) | (1L << (FLOAT_LITERAL - 65)) | (1L << (STRING_LITERAL - 65)) | (1L << (NULL_LITERAL - 65)) | (1L << (INTEGRAL - 65)) | (1L << (SIGMA - 65)) | (1L << (PRODUCT - 65)) | (1L << (INFINITY - 65)) | (1L << (PARTIALDIFF - 65)) | (1L << (FORALL - 65)) | (1L << (EXISTS - 65)) | (1L << (EMPTYSET - 65)) | (1L << (SQUAREROOT - 65)) | (1L << (INT - 65)) | (1L << (ID - 65)))) != 0)) {
							{
							setState(278);
							expressionList();
							}
						}

						setState(281);
						match(T__31);
						}
						break;
					}
					} 
				}
				setState(286);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class ConditionalExpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterConditionalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitConditionalExpression(this);
		}
	}

	public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
		ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(287);
			match(T__17);
			setState(288);
			expression();
			setState(289);
			match(T__43);
			setState(290);
			expression();
			setState(291);
			match(T__44);
			setState(292);
			expression();
			setState(293);
			match(T__45);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LambdaExpressionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitLambdaExpression(this);
		}
	}

	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(T__46);
			setState(296);
			identifier();
			setState(297);
			match(T__47);
			setState(298);
			type();
			setState(299);
			match(T__14);
			setState(300);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LetExpressionContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public LetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_letExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterLetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitLetExpression(this);
		}
	}

	public final LetExpressionContext letExpression() throws RecognitionException {
		LetExpressionContext _localctx = new LetExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_letExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
			match(T__48);
			setState(303);
			identifier();
			setState(304);
			match(T__2);
			setState(305);
			expression();
			setState(306);
			match(T__14);
			setState(307);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogicalExpressionContext extends ParserRuleContext {
		public TerminalNode FORALL() { return getToken(MathOCLParser.FORALL, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode CDOT() { return getToken(MathOCLParser.CDOT, 0); }
		public List<LogicalExpressionContext> logicalExpression() {
			return getRuleContexts(LogicalExpressionContext.class);
		}
		public LogicalExpressionContext logicalExpression(int i) {
			return getRuleContext(LogicalExpressionContext.class,i);
		}
		public TerminalNode EXISTS() { return getToken(MathOCLParser.EXISTS, 0); }
		public EqualityExpressionContext equalityExpression() {
			return getRuleContext(EqualityExpressionContext.class,0);
		}
		public LogicalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterLogicalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitLogicalExpression(this);
		}
	}

	public final LogicalExpressionContext logicalExpression() throws RecognitionException {
		return logicalExpression(0);
	}

	private LogicalExpressionContext logicalExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LogicalExpressionContext _localctx = new LogicalExpressionContext(_ctx, _parentState);
		LogicalExpressionContext _prevctx = _localctx;
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_logicalExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				{
				setState(310);
				match(FORALL);
				setState(311);
				identifier();
				setState(312);
				match(T__47);
				setState(313);
				type();
				setState(314);
				match(CDOT);
				setState(315);
				logicalExpression(4);
				}
				break;
			case EXISTS:
				{
				setState(317);
				match(EXISTS);
				setState(318);
				identifier();
				setState(319);
				match(T__47);
				setState(320);
				type();
				setState(321);
				match(CDOT);
				setState(322);
				logicalExpression(3);
				}
				break;
			case T__55:
				{
				setState(324);
				match(T__55);
				setState(325);
				equalityExpression();
				}
				break;
			case T__30:
			case T__37:
			case T__38:
			case T__39:
			case T__41:
			case T__64:
			case T__65:
			case T__68:
			case T__70:
			case T__72:
			case T__79:
			case T__80:
			case T__81:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INTEGRAL:
			case SIGMA:
			case PRODUCT:
			case INFINITY:
			case PARTIALDIFF:
			case EMPTYSET:
			case SQUAREROOT:
			case INT:
			case ID:
				{
				setState(326);
				equalityExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(349);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(347);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(329);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(330);
						match(T__49);
						setState(331);
						logicalExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(332);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(333);
						match(T__50);
						setState(334);
						logicalExpression(10);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(335);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(336);
						match(T__51);
						setState(337);
						logicalExpression(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(338);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(339);
						match(T__52);
						setState(340);
						logicalExpression(8);
						}
						break;
					case 5:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(341);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(342);
						match(T__53);
						setState(343);
						logicalExpression(7);
						}
						break;
					case 6:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(344);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(345);
						match(T__54);
						setState(346);
						logicalExpression(6);
						}
						break;
					}
					} 
				}
				setState(351);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class EqualityExpressionContext extends ParserRuleContext {
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public TerminalNode IN() { return getToken(MathOCLParser.IN, 0); }
		public TerminalNode NOTIN() { return getToken(MathOCLParser.NOTIN, 0); }
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitEqualityExpression(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_equalityExpression);
		int _la;
		try {
			setState(357);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(352);
				additiveExpression(0);
				setState(353);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__47) | (1L << T__56) | (1L << T__57) | (1L << T__58) | (1L << T__59) | (1L << T__60) | (1L << T__61) | (1L << T__62))) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (T__63 - 64)) | (1L << (IN - 64)) | (1L << (NOTIN - 64)))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(354);
				additiveExpression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(356);
				additiveExpression(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AdditiveExpressionContext extends ParserRuleContext {
		public List<FactorExpressionContext> factorExpression() {
			return getRuleContexts(FactorExpressionContext.class);
		}
		public FactorExpressionContext factorExpression(int i) {
			return getRuleContext(FactorExpressionContext.class,i);
		}
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitAdditiveExpression(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		return additiveExpression(0);
	}

	private AdditiveExpressionContext additiveExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, _parentState);
		AdditiveExpressionContext _prevctx = _localctx;
		int _startState = 56;
		enterRecursionRule(_localctx, 56, RULE_additiveExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(360);
				factorExpression(0);
				setState(361);
				_la = _input.LA(1);
				if ( !(_la==T__66 || _la==T__67) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(362);
				factorExpression(0);
				}
				break;
			case 2:
				{
				setState(364);
				factorExpression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(375);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(373);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(367);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(368);
						match(T__64);
						setState(369);
						additiveExpression(5);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(370);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(371);
						match(T__65);
						setState(372);
						factorExpression(0);
						}
						break;
					}
					} 
				}
				setState(377);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FactorExpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<FactorExpressionContext> factorExpression() {
			return getRuleContexts(FactorExpressionContext.class);
		}
		public FactorExpressionContext factorExpression(int i) {
			return getRuleContext(FactorExpressionContext.class,i);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ARROW() { return getToken(MathOCLParser.ARROW, 0); }
		public Factor2ExpressionContext factor2Expression() {
			return getRuleContext(Factor2ExpressionContext.class,0);
		}
		public TerminalNode INTEGRAL() { return getToken(MathOCLParser.INTEGRAL, 0); }
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TerminalNode SIGMA() { return getToken(MathOCLParser.SIGMA, 0); }
		public TerminalNode PRODUCT() { return getToken(MathOCLParser.PRODUCT, 0); }
		public TerminalNode SQUAREROOT() { return getToken(MathOCLParser.SQUAREROOT, 0); }
		public TerminalNode PARTIALDIFF() { return getToken(MathOCLParser.PARTIALDIFF, 0); }
		public TerminalNode DIFFERENTIAL() { return getToken(MathOCLParser.DIFFERENTIAL, 0); }
		public FactorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factorExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterFactorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitFactorExpression(this);
		}
	}

	public final FactorExpressionContext factorExpression() throws RecognitionException {
		return factorExpression(0);
	}

	private FactorExpressionContext factorExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FactorExpressionContext _localctx = new FactorExpressionContext(_ctx, _parentState);
		FactorExpressionContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_factorExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(444);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(379);
				match(T__68);
				setState(380);
				expression();
				setState(381);
				match(T__42);
				setState(382);
				match(T__69);
				setState(383);
				expression();
				setState(384);
				match(T__42);
				}
				break;
			case 2:
				{
				setState(386);
				match(T__70);
				setState(387);
				expression();
				setState(388);
				match(T__71);
				}
				break;
			case 3:
				{
				setState(390);
				match(T__65);
				setState(391);
				factorExpression(13);
				}
				break;
			case 4:
				{
				setState(392);
				match(T__72);
				setState(393);
				identifier();
				setState(394);
				match(ARROW);
				setState(395);
				expression();
				setState(396);
				match(T__42);
				setState(397);
				factor2Expression(0);
				}
				break;
			case 5:
				{
				setState(399);
				match(INTEGRAL);
				setState(400);
				match(T__77);
				setState(401);
				expression();
				setState(402);
				match(T__42);
				setState(403);
				match(T__69);
				setState(404);
				expression();
				setState(405);
				match(T__42);
				setState(406);
				expression();
				setState(407);
				match(ID);
				}
				break;
			case 6:
				{
				setState(409);
				match(INTEGRAL);
				setState(410);
				expression();
				setState(411);
				match(ID);
				}
				break;
			case 7:
				{
				setState(413);
				match(SIGMA);
				setState(414);
				match(T__77);
				setState(415);
				expression();
				setState(416);
				match(T__42);
				setState(417);
				match(T__69);
				setState(418);
				expression();
				setState(419);
				match(T__42);
				setState(420);
				factorExpression(7);
				}
				break;
			case 8:
				{
				setState(422);
				match(PRODUCT);
				setState(423);
				match(T__77);
				setState(424);
				expression();
				setState(425);
				match(T__42);
				setState(426);
				match(T__69);
				setState(427);
				expression();
				setState(428);
				match(T__42);
				setState(429);
				factorExpression(6);
				}
				break;
			case 9:
				{
				setState(431);
				match(T__64);
				setState(432);
				factor2Expression(0);
				}
				break;
			case 10:
				{
				setState(433);
				match(SQUAREROOT);
				setState(434);
				factor2Expression(0);
				}
				break;
			case 11:
				{
				setState(435);
				match(PARTIALDIFF);
				setState(436);
				match(T__77);
				setState(437);
				match(ID);
				setState(438);
				match(T__42);
				setState(439);
				factor2Expression(0);
				}
				break;
			case 12:
				{
				setState(440);
				factor2Expression(0);
				setState(441);
				match(T__78);
				}
				break;
			case 13:
				{
				setState(443);
				factor2Expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(453);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(451);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(446);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(447);
						_la = _input.LA(1);
						if ( !(((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (T__73 - 74)) | (1L << (T__74 - 74)) | (1L << (T__75 - 74)) | (1L << (T__76 - 74)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(448);
						factorExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(449);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(450);
						match(DIFFERENTIAL);
						}
						break;
					}
					} 
				}
				setState(455);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Factor2ExpressionContext extends ParserRuleContext {
		public SetExpressionContext setExpression() {
			return getRuleContext(SetExpressionContext.class,0);
		}
		public BasicExpressionContext basicExpression() {
			return getRuleContext(BasicExpressionContext.class,0);
		}
		public Factor2ExpressionContext factor2Expression() {
			return getRuleContext(Factor2ExpressionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Factor2ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factor2Expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterFactor2Expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitFactor2Expression(this);
		}
	}

	public final Factor2ExpressionContext factor2Expression() throws RecognitionException {
		return factor2Expression(0);
	}

	private Factor2ExpressionContext factor2Expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Factor2ExpressionContext _localctx = new Factor2ExpressionContext(_ctx, _parentState);
		Factor2ExpressionContext _prevctx = _localctx;
		int _startState = 60;
		enterRecursionRule(_localctx, 60, RULE_factor2Expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(459);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__79:
			case T__80:
			case T__81:
				{
				setState(457);
				setExpression();
				}
				break;
			case T__30:
			case T__37:
			case T__38:
			case T__39:
			case T__41:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INFINITY:
			case EMPTYSET:
			case INT:
			case ID:
				{
				setState(458);
				basicExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(468);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
					setState(461);
					if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
					setState(462);
					match(T__69);
					setState(463);
					expression();
					setState(464);
					match(T__42);
					}
					} 
				}
				setState(470);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SetExpressionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode CDOT() { return getToken(MathOCLParser.CDOT, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public SetExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterSetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitSetExpression(this);
		}
	}

	public final SetExpressionContext setExpression() throws RecognitionException {
		SetExpressionContext _localctx = new SetExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_setExpression);
		int _la;
		try {
			setState(499);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(471);
				match(T__79);
				setState(472);
				match(ID);
				setState(473);
				match(T__47);
				setState(474);
				type();
				setState(475);
				match(T__6);
				setState(476);
				expression();
				setState(477);
				match(T__42);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(479);
				match(T__79);
				setState(480);
				match(ID);
				setState(481);
				match(T__47);
				setState(482);
				type();
				setState(483);
				match(T__6);
				setState(484);
				expression();
				setState(485);
				match(CDOT);
				setState(486);
				expression();
				setState(487);
				match(T__42);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(489);
				match(T__80);
				setState(491);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__30) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__41) | (1L << T__46) | (1L << T__48) | (1L << T__55))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__68 - 65)) | (1L << (T__70 - 65)) | (1L << (T__72 - 65)) | (1L << (T__79 - 65)) | (1L << (T__80 - 65)) | (1L << (T__81 - 65)) | (1L << (FLOAT_LITERAL - 65)) | (1L << (STRING_LITERAL - 65)) | (1L << (NULL_LITERAL - 65)) | (1L << (INTEGRAL - 65)) | (1L << (SIGMA - 65)) | (1L << (PRODUCT - 65)) | (1L << (INFINITY - 65)) | (1L << (PARTIALDIFF - 65)) | (1L << (FORALL - 65)) | (1L << (EXISTS - 65)) | (1L << (EMPTYSET - 65)) | (1L << (SQUAREROOT - 65)) | (1L << (INT - 65)) | (1L << (ID - 65)))) != 0)) {
					{
					setState(490);
					expressionList();
					}
				}

				setState(493);
				match(T__42);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(494);
				match(T__81);
				setState(496);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__30) | (1L << T__37) | (1L << T__38) | (1L << T__39) | (1L << T__41) | (1L << T__46) | (1L << T__48) | (1L << T__55))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__65 - 65)) | (1L << (T__68 - 65)) | (1L << (T__70 - 65)) | (1L << (T__72 - 65)) | (1L << (T__79 - 65)) | (1L << (T__80 - 65)) | (1L << (T__81 - 65)) | (1L << (FLOAT_LITERAL - 65)) | (1L << (STRING_LITERAL - 65)) | (1L << (NULL_LITERAL - 65)) | (1L << (INTEGRAL - 65)) | (1L << (SIGMA - 65)) | (1L << (PRODUCT - 65)) | (1L << (INFINITY - 65)) | (1L << (PARTIALDIFF - 65)) | (1L << (FORALL - 65)) | (1L << (EXISTS - 65)) | (1L << (EMPTYSET - 65)) | (1L << (SQUAREROOT - 65)) | (1L << (INT - 65)) | (1L << (ID - 65)))) != 0)) {
					{
					setState(495);
					expressionList();
					}
				}

				setState(498);
				match(T__42);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitIdentifier(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(501);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 22:
			return basicExpression_sempred((BasicExpressionContext)_localctx, predIndex);
		case 26:
			return logicalExpression_sempred((LogicalExpressionContext)_localctx, predIndex);
		case 28:
			return additiveExpression_sempred((AdditiveExpressionContext)_localctx, predIndex);
		case 29:
			return factorExpression_sempred((FactorExpressionContext)_localctx, predIndex);
		case 30:
			return factor2Expression_sempred((Factor2ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean basicExpression_sempred(BasicExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		}
		return true;
	}
	private boolean logicalExpression_sempred(LogicalExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean additiveExpression_sempred(AdditiveExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean factorExpression_sempred(FactorExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 10:
			return precpred(_ctx, 10);
		case 11:
			return precpred(_ctx, 12);
		}
		return true;
	}
	private boolean factor2Expression_sempred(Factor2ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 12:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3m\u01fa\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\2\7\2H\n\2\f\2\16\2K\13\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\5\3X\n\3\3\4\3\4\3\4\3\4\3\4\5\4_\n\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\5\4h\n\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5p\n\5\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\5\t"+
		"\u0085\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3"+
		"\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3"+
		"\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3"+
		"\24\7\24\u00c1\n\24\f\24\16\24\u00c4\13\24\3\24\3\24\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00ee\n\25\3\26\3\26\3\26\7\26"+
		"\u00f3\n\26\f\26\16\26\u00f6\13\26\3\26\3\26\3\27\3\27\3\27\3\27\5\27"+
		"\u00fe\n\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0112\n\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\5\30\u011a\n\30\3\30\7\30\u011d\n\30\f\30\16\30\u0120\13\30\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\5\34\u014a\n\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\7\34\u015e\n\34\f\34\16\34\u0161\13\34\3\35\3\35"+
		"\3\35\3\35\3\35\5\35\u0168\n\35\3\36\3\36\3\36\3\36\3\36\3\36\5\36\u0170"+
		"\n\36\3\36\3\36\3\36\3\36\3\36\3\36\7\36\u0178\n\36\f\36\16\36\u017b\13"+
		"\36\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u01bf\n\37"+
		"\3\37\3\37\3\37\3\37\3\37\7\37\u01c6\n\37\f\37\16\37\u01c9\13\37\3 \3"+
		" \3 \5 \u01ce\n \3 \3 \3 \3 \3 \7 \u01d5\n \f \16 \u01d8\13 \3!\3!\3!"+
		"\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\5!\u01ee\n!\3!\3!"+
		"\3!\5!\u01f3\n!\3!\5!\u01f6\n!\3\"\3\"\3\"\2\7.\66:<>#\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@B\2\5\6\2\5\6\62\62"+
		";BYZ\3\2EF\3\2LO\2\u0228\2D\3\2\2\2\4W\3\2\2\2\6g\3\2\2\2\bo\3\2\2\2\n"+
		"q\3\2\2\2\fw\3\2\2\2\16|\3\2\2\2\20\u0081\3\2\2\2\22\u0086\3\2\2\2\24"+
		"\u008d\3\2\2\2\26\u0092\3\2\2\2\30\u0097\3\2\2\2\32\u009d\3\2\2\2\34\u00a2"+
		"\3\2\2\2\36\u00a8\3\2\2\2 \u00af\3\2\2\2\"\u00b4\3\2\2\2$\u00b9\3\2\2"+
		"\2&\u00c2\3\2\2\2(\u00ed\3\2\2\2*\u00f4\3\2\2\2,\u00fd\3\2\2\2.\u0111"+
		"\3\2\2\2\60\u0121\3\2\2\2\62\u0129\3\2\2\2\64\u0130\3\2\2\2\66\u0149\3"+
		"\2\2\28\u0167\3\2\2\2:\u016f\3\2\2\2<\u01be\3\2\2\2>\u01cd\3\2\2\2@\u01f5"+
		"\3\2\2\2B\u01f7\3\2\2\2DE\7\3\2\2EI\7l\2\2FH\5\4\3\2GF\3\2\2\2HK\3\2\2"+
		"\2IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LM\7\2\2\3M\3\3\2\2\2NX\5\6"+
		"\4\2OX\5\n\6\2PX\5\f\7\2QX\5\16\b\2RX\5\30\r\2SX\5\20\t\2TX\5\22\n\2U"+
		"X\5\24\13\2VX\5\26\f\2WN\3\2\2\2WO\3\2\2\2WP\3\2\2\2WQ\3\2\2\2WR\3\2\2"+
		"\2WS\3\2\2\2WT\3\2\2\2WU\3\2\2\2WV\3\2\2\2X\5\3\2\2\2YZ\7\4\2\2Z[\5.\30"+
		"\2[^\7\5\2\2\\_\5\b\5\2]_\5,\27\2^\\\3\2\2\2^]\3\2\2\2_h\3\2\2\2`a\7\4"+
		"\2\2ab\5.\30\2bc\7\6\2\2cd\5,\27\2dh\3\2\2\2ef\7\4\2\2fh\5.\30\2gY\3\2"+
		"\2\2g`\3\2\2\2ge\3\2\2\2h\7\3\2\2\2ip\5\32\16\2jp\5\34\17\2kp\5\36\20"+
		"\2lp\5\"\22\2mp\5 \21\2np\5$\23\2oi\3\2\2\2oj\3\2\2\2ok\3\2\2\2ol\3\2"+
		"\2\2om\3\2\2\2on\3\2\2\2p\t\3\2\2\2qr\7\7\2\2rs\7\b\2\2st\5,\27\2tu\7"+
		"\t\2\2uv\5\66\34\2v\13\3\2\2\2wx\7\n\2\2xy\5\66\34\2yz\7\13\2\2z{\5\66"+
		"\34\2{\r\3\2\2\2|}\7\f\2\2}~\5,\27\2~\177\7\r\2\2\177\u0080\5,\27\2\u0080"+
		"\17\3\2\2\2\u0081\u0084\7\16\2\2\u0082\u0085\5\b\5\2\u0083\u0085\5,\27"+
		"\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085\21\3\2\2\2\u0086\u0087"+
		"\7\17\2\2\u0087\u0088\5,\27\2\u0088\u0089\7\20\2\2\u0089\u008a\5.\30\2"+
		"\u008a\u008b\7\21\2\2\u008b\u008c\5,\27\2\u008c\23\3\2\2\2\u008d\u008e"+
		"\7\22\2\2\u008e\u008f\5*\26\2\u008f\u0090\7\20\2\2\u0090\u0091\5&\24\2"+
		"\u0091\25\3\2\2\2\u0092\u0093\7\23\2\2\u0093\u0094\5\66\34\2\u0094\u0095"+
		"\7\24\2\2\u0095\u0096\5*\26\2\u0096\27\3\2\2\2\u0097\u0098\7\25\2\2\u0098"+
		"\u0099\5,\27\2\u0099\u009a\7\r\2\2\u009a\u009b\7k\2\2\u009b\u009c\7\26"+
		"\2\2\u009c\31\3\2\2\2\u009d\u009e\7\17\2\2\u009e\u009f\5.\30\2\u009f\u00a0"+
		"\7\21\2\2\u00a0\u00a1\5,\27\2\u00a1\33\3\2\2\2\u00a2\u00a3\7\27\2\2\u00a3"+
		"\u00a4\5,\27\2\u00a4\u00a5\7\r\2\2\u00a5\u00a6\7k\2\2\u00a6\u00a7\7\26"+
		"\2\2\u00a7\35\3\2\2\2\u00a8\u00a9\7\30\2\2\u00a9\u00aa\5,\27\2\u00aa\u00ab"+
		"\7\31\2\2\u00ab\u00ac\7\32\2\2\u00ac\u00ad\7\21\2\2\u00ad\u00ae\5B\"\2"+
		"\u00ae\37\3\2\2\2\u00af\u00b0\7\33\2\2\u00b0\u00b1\5,\27\2\u00b1\u00b2"+
		"\7\34\2\2\u00b2\u00b3\5,\27\2\u00b3!\3\2\2\2\u00b4\u00b5\7\35\2\2\u00b5"+
		"\u00b6\5,\27\2\u00b6\u00b7\7\21\2\2\u00b7\u00b8\5,\27\2\u00b8#\3\2\2\2"+
		"\u00b9\u00ba\7\36\2\2\u00ba\u00bb\5,\27\2\u00bb\u00bc\7\34\2\2\u00bc\u00bd"+
		"\5,\27\2\u00bd%\3\2\2\2\u00be\u00bf\7l\2\2\u00bf\u00c1\7\37\2\2\u00c0"+
		"\u00be\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2"+
		"\2\2\u00c3\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c6\7l\2\2\u00c6"+
		"\'\3\2\2\2\u00c7\u00c8\7 \2\2\u00c8\u00c9\7!\2\2\u00c9\u00ca\5(\25\2\u00ca"+
		"\u00cb\7\"\2\2\u00cb\u00ee\3\2\2\2\u00cc\u00cd\7#\2\2\u00cd\u00ce\7!\2"+
		"\2\u00ce\u00cf\5(\25\2\u00cf\u00d0\7\"\2\2\u00d0\u00ee\3\2\2\2\u00d1\u00d2"+
		"\7$\2\2\u00d2\u00d3\7!\2\2\u00d3\u00d4\5(\25\2\u00d4\u00d5\7\"\2\2\u00d5"+
		"\u00ee\3\2\2\2\u00d6\u00d7\7%\2\2\u00d7\u00d8\7!\2\2\u00d8\u00d9\5(\25"+
		"\2\u00d9\u00da\7\"\2\2\u00da\u00ee\3\2\2\2\u00db\u00dc\7&\2\2\u00dc\u00dd"+
		"\7!\2\2\u00dd\u00de\5(\25\2\u00de\u00df\7\37\2\2\u00df\u00e0\5(\25\2\u00e0"+
		"\u00e1\7\"\2\2\u00e1\u00ee\3\2\2\2\u00e2\u00e3\7\'\2\2\u00e3\u00e4\7!"+
		"\2\2\u00e4\u00e5\5(\25\2\u00e5\u00e6\7\37\2\2\u00e6\u00e7\5(\25\2\u00e7"+
		"\u00e8\7\"\2\2\u00e8\u00ee\3\2\2\2\u00e9\u00ee\7f\2\2\u00ea\u00ee\7g\2"+
		"\2\u00eb\u00ee\7h\2\2\u00ec\u00ee\7l\2\2\u00ed\u00c7\3\2\2\2\u00ed\u00cc"+
		"\3\2\2\2\u00ed\u00d1\3\2\2\2\u00ed\u00d6\3\2\2\2\u00ed\u00db\3\2\2\2\u00ed"+
		"\u00e2\3\2\2\2\u00ed\u00e9\3\2\2\2\u00ed\u00ea\3\2\2\2\u00ed\u00eb\3\2"+
		"\2\2\u00ed\u00ec\3\2\2\2\u00ee)\3\2\2\2\u00ef\u00f0\5,\27\2\u00f0\u00f1"+
		"\7\37\2\2\u00f1\u00f3\3\2\2\2\u00f2\u00ef\3\2\2\2\u00f3\u00f6\3\2\2\2"+
		"\u00f4\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f7\3\2\2\2\u00f6\u00f4"+
		"\3\2\2\2\u00f7\u00f8\5,\27\2\u00f8+\3\2\2\2\u00f9\u00fe\5\66\34\2\u00fa"+
		"\u00fe\5\60\31\2\u00fb\u00fe\5\62\32\2\u00fc\u00fe\5\64\33\2\u00fd\u00f9"+
		"\3\2\2\2\u00fd\u00fa\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe"+
		"-\3\2\2\2\u00ff\u0100\b\30\1\2\u0100\u0112\7W\2\2\u0101\u0112\7(\2\2\u0102"+
		"\u0112\7)\2\2\u0103\u0112\7*\2\2\u0104\u0112\5B\"\2\u0105\u0106\7,\2\2"+
		"\u0106\u0107\7l\2\2\u0107\u0112\7-\2\2\u0108\u0112\7k\2\2\u0109\u0112"+
		"\7U\2\2\u010a\u0112\7V\2\2\u010b\u0112\7^\2\2\u010c\u0112\7c\2\2\u010d"+
		"\u010e\7!\2\2\u010e\u010f\5,\27\2\u010f\u0110\7\"\2\2\u0110\u0112\3\2"+
		"\2\2\u0111\u00ff\3\2\2\2\u0111\u0101\3\2\2\2\u0111\u0102\3\2\2\2\u0111"+
		"\u0103\3\2\2\2\u0111\u0104\3\2\2\2\u0111\u0105\3\2\2\2\u0111\u0108\3\2"+
		"\2\2\u0111\u0109\3\2\2\2\u0111\u010a\3\2\2\2\u0111\u010b\3\2\2\2\u0111"+
		"\u010c\3\2\2\2\u0111\u010d\3\2\2\2\u0112\u011e\3\2\2\2\u0113\u0114\f\f"+
		"\2\2\u0114\u0115\7+\2\2\u0115\u011d\7l\2\2\u0116\u0117\f\13\2\2\u0117"+
		"\u0119\7!\2\2\u0118\u011a\5*\26\2\u0119\u0118\3\2\2\2\u0119\u011a\3\2"+
		"\2\2\u011a\u011b\3\2\2\2\u011b\u011d\7\"\2\2\u011c\u0113\3\2\2\2\u011c"+
		"\u0116\3\2\2\2\u011d\u0120\3\2\2\2\u011e\u011c\3\2\2\2\u011e\u011f\3\2"+
		"\2\2\u011f/\3\2\2\2\u0120\u011e\3\2\2\2\u0121\u0122\7\24\2\2\u0122\u0123"+
		"\5,\27\2\u0123\u0124\7.\2\2\u0124\u0125\5,\27\2\u0125\u0126\7/\2\2\u0126"+
		"\u0127\5,\27\2\u0127\u0128\7\60\2\2\u0128\61\3\2\2\2\u0129\u012a\7\61"+
		"\2\2\u012a\u012b\5B\"\2\u012b\u012c\7\62\2\2\u012c\u012d\5(\25\2\u012d"+
		"\u012e\7\21\2\2\u012e\u012f\5,\27\2\u012f\63\3\2\2\2\u0130\u0131\7\63"+
		"\2\2\u0131\u0132\5B\"\2\u0132\u0133\7\5\2\2\u0133\u0134\5,\27\2\u0134"+
		"\u0135\7\21\2\2\u0135\u0136\5,\27\2\u0136\65\3\2\2\2\u0137\u0138\b\34"+
		"\1\2\u0138\u0139\7a\2\2\u0139\u013a\5B\"\2\u013a\u013b\7\62\2\2\u013b"+
		"\u013c\5(\25\2\u013c\u013d\7i\2\2\u013d\u013e\5\66\34\6\u013e\u014a\3"+
		"\2\2\2\u013f\u0140\7b\2\2\u0140\u0141\5B\"\2\u0141\u0142\7\62\2\2\u0142"+
		"\u0143\5(\25\2\u0143\u0144\7i\2\2\u0144\u0145\5\66\34\5\u0145\u014a\3"+
		"\2\2\2\u0146\u0147\7:\2\2\u0147\u014a\58\35\2\u0148\u014a\58\35\2\u0149"+
		"\u0137\3\2\2\2\u0149\u013f\3\2\2\2\u0149\u0146\3\2\2\2\u0149\u0148\3\2"+
		"\2\2\u014a\u015f\3\2\2\2\u014b\u014c\f\f\2\2\u014c\u014d\7\64\2\2\u014d"+
		"\u015e\5\66\34\r\u014e\u014f\f\13\2\2\u014f\u0150\7\65\2\2\u0150\u015e"+
		"\5\66\34\f\u0151\u0152\f\n\2\2\u0152\u0153\7\66\2\2\u0153\u015e\5\66\34"+
		"\13\u0154\u0155\f\t\2\2\u0155\u0156\7\67\2\2\u0156\u015e\5\66\34\n\u0157"+
		"\u0158\f\b\2\2\u0158\u0159\78\2\2\u0159\u015e\5\66\34\t\u015a\u015b\f"+
		"\7\2\2\u015b\u015c\79\2\2\u015c\u015e\5\66\34\b\u015d\u014b\3\2\2\2\u015d"+
		"\u014e\3\2\2\2\u015d\u0151\3\2\2\2\u015d\u0154\3\2\2\2\u015d\u0157\3\2"+
		"\2\2\u015d\u015a\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u015d\3\2\2\2\u015f"+
		"\u0160\3\2\2\2\u0160\67\3\2\2\2\u0161\u015f\3\2\2\2\u0162\u0163\5:\36"+
		"\2\u0163\u0164\t\2\2\2\u0164\u0165\5:\36\2\u0165\u0168\3\2\2\2\u0166\u0168"+
		"\5:\36\2\u0167\u0162\3\2\2\2\u0167\u0166\3\2\2\2\u01689\3\2\2\2\u0169"+
		"\u016a\b\36\1\2\u016a\u016b\5<\37\2\u016b\u016c\t\3\2\2\u016c\u016d\5"+
		"<\37\2\u016d\u0170\3\2\2\2\u016e\u0170\5<\37\2\u016f\u0169\3\2\2\2\u016f"+
		"\u016e\3\2\2\2\u0170\u0179\3\2\2\2\u0171\u0172\f\6\2\2\u0172\u0173\7C"+
		"\2\2\u0173\u0178\5:\36\7\u0174\u0175\f\5\2\2\u0175\u0176\7D\2\2\u0176"+
		"\u0178\5<\37\2\u0177\u0171\3\2\2\2\u0177\u0174\3\2\2\2\u0178\u017b\3\2"+
		"\2\2\u0179\u0177\3\2\2\2\u0179\u017a\3\2\2\2\u017a;\3\2\2\2\u017b\u0179"+
		"\3\2\2\2\u017c\u017d\b\37\1\2\u017d\u017e\7G\2\2\u017e\u017f\5,\27\2\u017f"+
		"\u0180\7-\2\2\u0180\u0181\7H\2\2\u0181\u0182\5,\27\2\u0182\u0183\7-\2"+
		"\2\u0183\u01bf\3\2\2\2\u0184\u0185\7I\2\2\u0185\u0186\5,\27\2\u0186\u0187"+
		"\7J\2\2\u0187\u01bf\3\2\2\2\u0188\u0189\7D\2\2\u0189\u01bf\5<\37\17\u018a"+
		"\u018b\7K\2\2\u018b\u018c\5B\"\2\u018c\u018d\7e\2\2\u018d\u018e\5,\27"+
		"\2\u018e\u018f\7-\2\2\u018f\u0190\5> \2\u0190\u01bf\3\2\2\2\u0191\u0192"+
		"\7[\2\2\u0192\u0193\7P\2\2\u0193\u0194\5,\27\2\u0194\u0195\7-\2\2\u0195"+
		"\u0196\7H\2\2\u0196\u0197\5,\27\2\u0197\u0198\7-\2\2\u0198\u0199\5,\27"+
		"\2\u0199\u019a\7l\2\2\u019a\u01bf\3\2\2\2\u019b\u019c\7[\2\2\u019c\u019d"+
		"\5,\27\2\u019d\u019e\7l\2\2\u019e\u01bf\3\2\2\2\u019f\u01a0\7\\\2\2\u01a0"+
		"\u01a1\7P\2\2\u01a1\u01a2\5,\27\2\u01a2\u01a3\7-\2\2\u01a3\u01a4\7H\2"+
		"\2\u01a4\u01a5\5,\27\2\u01a5\u01a6\7-\2\2\u01a6\u01a7\5<\37\t\u01a7\u01bf"+
		"\3\2\2\2\u01a8\u01a9\7]\2\2\u01a9\u01aa\7P\2\2\u01aa\u01ab\5,\27\2\u01ab"+
		"\u01ac\7-\2\2\u01ac\u01ad\7H\2\2\u01ad\u01ae\5,\27\2\u01ae\u01af\7-\2"+
		"\2\u01af\u01b0\5<\37\b\u01b0\u01bf\3\2\2\2\u01b1\u01b2\7C\2\2\u01b2\u01bf"+
		"\5> \2\u01b3\u01b4\7d\2\2\u01b4\u01bf\5> \2\u01b5\u01b6\7`\2\2\u01b6\u01b7"+
		"\7P\2\2\u01b7\u01b8\7l\2\2\u01b8\u01b9\7-\2\2\u01b9\u01bf\5> \2\u01ba"+
		"\u01bb\5> \2\u01bb\u01bc\7Q\2\2\u01bc\u01bf\3\2\2\2\u01bd\u01bf\5> \2"+
		"\u01be\u017c\3\2\2\2\u01be\u0184\3\2\2\2\u01be\u0188\3\2\2\2\u01be\u018a"+
		"\3\2\2\2\u01be\u0191\3\2\2\2\u01be\u019b\3\2\2\2\u01be\u019f\3\2\2\2\u01be"+
		"\u01a8\3\2\2\2\u01be\u01b1\3\2\2\2\u01be\u01b3\3\2\2\2\u01be\u01b5\3\2"+
		"\2\2\u01be\u01ba\3\2\2\2\u01be\u01bd\3\2\2\2\u01bf\u01c7\3\2\2\2\u01c0"+
		"\u01c1\f\f\2\2\u01c1\u01c2\t\4\2\2\u01c2\u01c6\5<\37\r\u01c3\u01c4\f\16"+
		"\2\2\u01c4\u01c6\7_\2\2\u01c5\u01c0\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c6"+
		"\u01c9\3\2\2\2\u01c7\u01c5\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8=\3\2\2\2"+
		"\u01c9\u01c7\3\2\2\2\u01ca\u01cb\b \1\2\u01cb\u01ce\5@!\2\u01cc\u01ce"+
		"\5.\30\2\u01cd\u01ca\3\2\2\2\u01cd\u01cc\3\2\2\2\u01ce\u01d6\3\2\2\2\u01cf"+
		"\u01d0\f\5\2\2\u01d0\u01d1\7H\2\2\u01d1\u01d2\5,\27\2\u01d2\u01d3\7-\2"+
		"\2\u01d3\u01d5\3\2\2\2\u01d4\u01cf\3\2\2\2\u01d5\u01d8\3\2\2\2\u01d6\u01d4"+
		"\3\2\2\2\u01d6\u01d7\3\2\2\2\u01d7?\3\2\2\2\u01d8\u01d6\3\2\2\2\u01d9"+
		"\u01da\7R\2\2\u01da\u01db\7l\2\2\u01db\u01dc\7\62\2\2\u01dc\u01dd\5(\25"+
		"\2\u01dd\u01de\7\t\2\2\u01de\u01df\5,\27\2\u01df\u01e0\7-\2\2\u01e0\u01f6"+
		"\3\2\2\2\u01e1\u01e2\7R\2\2\u01e2\u01e3\7l\2\2\u01e3\u01e4\7\62\2\2\u01e4"+
		"\u01e5\5(\25\2\u01e5\u01e6\7\t\2\2\u01e6\u01e7\5,\27\2\u01e7\u01e8\7i"+
		"\2\2\u01e8\u01e9\5,\27\2\u01e9\u01ea\7-\2\2\u01ea\u01f6\3\2\2\2\u01eb"+
		"\u01ed\7S\2\2\u01ec\u01ee\5*\26\2\u01ed\u01ec\3\2\2\2\u01ed\u01ee\3\2"+
		"\2\2\u01ee\u01ef\3\2\2\2\u01ef\u01f6\7-\2\2\u01f0\u01f2\7T\2\2\u01f1\u01f3"+
		"\5*\26\2\u01f2\u01f1\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f4\3\2\2\2\u01f4"+
		"\u01f6\7-\2\2\u01f5\u01d9\3\2\2\2\u01f5\u01e1\3\2\2\2\u01f5\u01eb\3\2"+
		"\2\2\u01f5\u01f0\3\2\2\2\u01f6A\3\2\2\2\u01f7\u01f8\7l\2\2\u01f8C\3\2"+
		"\2\2\37IW^go\u0084\u00c2\u00ed\u00f4\u00fd\u0111\u0119\u011c\u011e\u0149"+
		"\u015d\u015f\u0167\u016f\u0177\u0179\u01be\u01c5\u01c7\u01cd\u01d6\u01ed"+
		"\u01f2\u01f5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}