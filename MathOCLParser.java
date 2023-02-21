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
		T__80=81, T__81=82, T__82=83, T__83=84, T__84=85, T__85=86, T__86=87, 
		T__87=88, T__88=89, T__89=90, T__90=91, T__91=92, T__92=93, T__93=94, 
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, T__100=101, 
		T__101=102, T__102=103, T__103=104, FLOAT_LITERAL=105, STRING_LITERAL=106, 
		NULL_LITERAL=107, MULTILINE_COMMENT=108, IN=109, NOTIN=110, INTEGRAL=111, 
		SIGMA=112, PRODUCT=113, INFINITY=114, DIFFERENTIAL=115, PARTIALDIFF=116, 
		FORALL=117, EXISTS=118, EMPTYSET=119, SQUAREROOT=120, NATURAL=121, INTEGER=122, 
		REAL=123, CDOT=124, NEWLINE=125, INT=126, ID=127, WS=128;
	public static final int
		RULE_specification = 0, RULE_part = 1, RULE_formula = 2, RULE_constraint = 3, 
		RULE_reexpression = 4, RULE_simplify = 5, RULE_substituting = 6, RULE_solve = 7, 
		RULE_prove = 8, RULE_expanding = 9, RULE_factorBy = 10, RULE_cancelIn = 11, 
		RULE_idList = 12, RULE_type = 13, RULE_expressionList = 14, RULE_expression = 15, 
		RULE_basicExpression = 16, RULE_conditionalExpression = 17, RULE_lambdaExpression = 18, 
		RULE_letExpression = 19, RULE_logicalExpression = 20, RULE_equalityExpression = 21, 
		RULE_additiveExpression = 22, RULE_factorExpression = 23, RULE_factor2Expression = 24, 
		RULE_setExpression = 25, RULE_identifier = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "part", "formula", "constraint", "reexpression", "simplify", 
			"substituting", "solve", "prove", "expanding", "factorBy", "cancelIn", 
			"idList", "type", "expressionList", "expression", "basicExpression", 
			"conditionalExpression", "lambdaExpression", "letExpression", "logicalExpression", 
			"equalityExpression", "additiveExpression", "factorExpression", "factor2Expression", 
			"setExpression", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'specification'", "'Define'", "'='", "'Constraint'", "'on'", "'|'", 
			"'Express'", "'as'", "'Simplify'", "'Substitute'", "'for'", "'in'", "'Solve'", 
			"'Prove'", "'if'", "'Expanding'", "'to'", "'terms'", "'Factor'", "'by'", 
			"'Cancel'", "','", "'Sequence'", "'('", "')'", "'Set'", "'Bag'", "'OrderedSet'", 
			"'Map'", "'Function'", "'true'", "'false'", "'.'", "'['", "']'", "'g{'", 
			"'}'", "'then'", "'else'", "'endif'", "'lambda'", "':'", "'let'", "'=>'", 
			"'implies'", "'or'", "'xor'", "'&'", "'and'", "'not'", "'<'", "'>'", 
			"'>='", "'<='", "'/='", "'<>'", "'/:'", "'<:'", "'+'", "'-'", "'..'", 
			"'|->'", "'C'", "'_{'", "'^{'", "'*'", "'/'", "'mod'", "'div'", "'!'", 
			"'->size()'", "'->isEmpty()'", "'->notEmpty()'", "'->asSet()'", "'->asBag()'", 
			"'->asOrderedSet()'", "'->asSequence()'", "'->sort()'", "'->any()'", 
			"'->first()'", "'->last()'", "'->front()'", "'->tail()'", "'->reverse()'", 
			"'->max()'", "'->min()'", "'->at'", "'->union'", "'->intersection'", 
			"'->includes'", "'->excludes'", "'->including'", "'->excluding'", "'->includesAll'", 
			"'->excludesAll'", "'->prepend'", "'->append'", "'->count'", "'->apply'", 
			"'OrderedSet{'", "'Bag{'", "'Set{'", "'Sequence{'", "'Map{'", null, null, 
			"'null'", null, "'\u00A9'", "'\u00A2'", "'\u2021'", "'\u20AC'", "'\u00D7'", 
			"'\u2026'", "'\u00B4'", "'\u00D0'", "'\u00A1'", "'\u00A3'", "'\u00D8'", 
			"'\u2020'", "'\u00D1'", "'\u017D'", "'\u00AE'", "'\u2022'"
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
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "FLOAT_LITERAL", 
			"STRING_LITERAL", "NULL_LITERAL", "MULTILINE_COMMENT", "IN", "NOTIN", 
			"INTEGRAL", "SIGMA", "PRODUCT", "INFINITY", "DIFFERENTIAL", "PARTIALDIFF", 
			"FORALL", "EXISTS", "EMPTYSET", "SQUAREROOT", "NATURAL", "INTEGER", "REAL", 
			"CDOT", "NEWLINE", "INT", "ID", "WS"
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
			setState(54);
			match(T__0);
			setState(55);
			match(ID);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__3) | (1L << T__6) | (1L << T__8) | (1L << T__9) | (1L << T__12) | (1L << T__13) | (1L << T__15) | (1L << T__18) | (1L << T__20))) != 0)) {
				{
				{
				setState(56);
				part();
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(62);
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
		public ReexpressionContext reexpression() {
			return getRuleContext(ReexpressionContext.class,0);
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
		public FactorByContext factorBy() {
			return getRuleContext(FactorByContext.class,0);
		}
		public CancelInContext cancelIn() {
			return getRuleContext(CancelInContext.class,0);
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
			setState(74);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				formula();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				constraint();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(66);
				reexpression();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 4);
				{
				setState(67);
				expanding();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(68);
				simplify();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 6);
				{
				setState(69);
				substituting();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 7);
				{
				setState(70);
				solve();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 8);
				{
				setState(71);
				prove();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 9);
				{
				setState(72);
				factorBy();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 10);
				{
				setState(73);
				cancelIn();
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
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
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
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(T__1);
			setState(77);
			match(ID);
			setState(78);
			match(T__2);
			setState(79);
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
		enterRule(_localctx, 6, RULE_constraint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			match(T__3);
			setState(82);
			match(T__4);
			setState(83);
			expression();
			setState(84);
			match(T__5);
			setState(85);
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

	public static class ReexpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ReexpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reexpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).enterReexpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MathOCLListener ) ((MathOCLListener)listener).exitReexpression(this);
		}
	}

	public final ReexpressionContext reexpression() throws RecognitionException {
		ReexpressionContext _localctx = new ReexpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_reexpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(T__6);
			setState(88);
			expression();
			setState(89);
			match(T__7);
			setState(90);
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
		enterRule(_localctx, 10, RULE_simplify);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(T__8);
			setState(93);
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

	public static class SubstitutingContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
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
		enterRule(_localctx, 12, RULE_substituting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(T__9);
			setState(96);
			expression();
			setState(97);
			match(T__10);
			setState(98);
			match(ID);
			setState(99);
			match(T__11);
			setState(100);
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
		enterRule(_localctx, 14, RULE_solve);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(T__12);
			setState(103);
			expressionList();
			setState(104);
			match(T__10);
			setState(105);
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
		enterRule(_localctx, 16, RULE_prove);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(T__13);
			setState(108);
			logicalExpression(0);
			setState(109);
			match(T__14);
			setState(110);
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
		enterRule(_localctx, 18, RULE_expanding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(T__15);
			setState(113);
			expression();
			setState(114);
			match(T__16);
			setState(115);
			match(INT);
			setState(116);
			match(T__17);
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
		enterRule(_localctx, 20, RULE_factorBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(T__18);
			setState(119);
			expression();
			setState(120);
			match(T__19);
			setState(121);
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
		enterRule(_localctx, 22, RULE_cancelIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(T__20);
			setState(124);
			expression();
			setState(125);
			match(T__11);
			setState(126);
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
		enterRule(_localctx, 24, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(128);
					match(ID);
					setState(129);
					match(T__21);
					}
					} 
				}
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(135);
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
		enterRule(_localctx, 26, RULE_type);
		try {
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__22:
				enterOuterAlt(_localctx, 1);
				{
				setState(137);
				match(T__22);
				setState(138);
				match(T__23);
				setState(139);
				type();
				setState(140);
				match(T__24);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				match(T__25);
				setState(143);
				match(T__23);
				setState(144);
				type();
				setState(145);
				match(T__24);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 3);
				{
				setState(147);
				match(T__26);
				setState(148);
				match(T__23);
				setState(149);
				type();
				setState(150);
				match(T__24);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 4);
				{
				setState(152);
				match(T__27);
				setState(153);
				match(T__23);
				setState(154);
				type();
				setState(155);
				match(T__24);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 5);
				{
				setState(157);
				match(T__28);
				setState(158);
				match(T__23);
				setState(159);
				type();
				setState(160);
				match(T__21);
				setState(161);
				type();
				setState(162);
				match(T__24);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 6);
				{
				setState(164);
				match(T__29);
				setState(165);
				match(T__23);
				setState(166);
				type();
				setState(167);
				match(T__21);
				setState(168);
				type();
				setState(169);
				match(T__24);
				}
				break;
			case NATURAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(171);
				match(NATURAL);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 8);
				{
				setState(172);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(173);
				match(REAL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(174);
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
		enterRule(_localctx, 28, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(177);
					expression();
					setState(178);
					match(T__21);
					}
					} 
				}
				setState(184);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(185);
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
		enterRule(_localctx, 30, RULE_expression);
		try {
			setState(191);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
			case T__30:
			case T__31:
			case T__35:
			case T__49:
			case T__58:
			case T__59:
			case T__62:
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
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
				setState(187);
				logicalExpression(0);
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 2);
				{
				setState(188);
				conditionalExpression();
				}
				break;
			case T__40:
				enterOuterAlt(_localctx, 3);
				{
				setState(189);
				lambdaExpression();
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 4);
				{
				setState(190);
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
		int _startState = 32;
		enterRecursionRule(_localctx, 32, RULE_basicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL_LITERAL:
				{
				setState(194);
				match(NULL_LITERAL);
				}
				break;
			case T__30:
				{
				setState(195);
				match(T__30);
				}
				break;
			case T__31:
				{
				setState(196);
				match(T__31);
				}
				break;
			case ID:
				{
				setState(197);
				identifier();
				}
				break;
			case T__35:
				{
				setState(198);
				match(T__35);
				setState(199);
				match(ID);
				setState(200);
				match(T__36);
				}
				break;
			case INT:
				{
				setState(201);
				match(INT);
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(202);
				match(FLOAT_LITERAL);
				}
				break;
			case STRING_LITERAL:
				{
				setState(203);
				match(STRING_LITERAL);
				}
				break;
			case INFINITY:
				{
				setState(204);
				match(INFINITY);
				}
				break;
			case EMPTYSET:
				{
				setState(205);
				match(EMPTYSET);
				}
				break;
			case T__23:
				{
				setState(206);
				match(T__23);
				setState(207);
				expression();
				setState(208);
				match(T__24);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(226);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(212);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(213);
						match(T__32);
						setState(214);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(215);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(216);
						match(T__23);
						setState(218);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
							{
							setState(217);
							expressionList();
							}
						}

						setState(220);
						match(T__24);
						}
						break;
					case 3:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(221);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(222);
						match(T__33);
						setState(223);
						expression();
						setState(224);
						match(T__34);
						}
						break;
					}
					} 
				}
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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
		enterRule(_localctx, 34, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(T__14);
			setState(232);
			expression();
			setState(233);
			match(T__37);
			setState(234);
			expression();
			setState(235);
			match(T__38);
			setState(236);
			expression();
			setState(237);
			match(T__39);
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
		enterRule(_localctx, 36, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			match(T__40);
			setState(240);
			identifier();
			setState(241);
			match(T__41);
			setState(242);
			type();
			setState(243);
			match(T__11);
			setState(244);
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
		enterRule(_localctx, 38, RULE_letExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(T__42);
			setState(247);
			identifier();
			setState(248);
			match(T__2);
			setState(249);
			expression();
			setState(250);
			match(T__11);
			setState(251);
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
		int _startState = 40;
		enterRecursionRule(_localctx, 40, RULE_logicalExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				{
				setState(254);
				match(FORALL);
				setState(255);
				identifier();
				setState(256);
				match(CDOT);
				setState(257);
				logicalExpression(4);
				}
				break;
			case EXISTS:
				{
				setState(259);
				match(EXISTS);
				setState(260);
				identifier();
				setState(261);
				match(CDOT);
				setState(262);
				logicalExpression(3);
				}
				break;
			case T__49:
				{
				setState(264);
				match(T__49);
				setState(265);
				logicalExpression(2);
				}
				break;
			case T__23:
			case T__30:
			case T__31:
			case T__35:
			case T__58:
			case T__59:
			case T__62:
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
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
				setState(266);
				equalityExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(289);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(287);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(269);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(270);
						match(T__43);
						setState(271);
						logicalExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(272);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(273);
						match(T__44);
						setState(274);
						logicalExpression(10);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(275);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(276);
						match(T__45);
						setState(277);
						logicalExpression(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(278);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(279);
						match(T__46);
						setState(280);
						logicalExpression(8);
						}
						break;
					case 5:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(281);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(282);
						match(T__47);
						setState(283);
						logicalExpression(7);
						}
						break;
					case 6:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(284);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(285);
						match(T__48);
						setState(286);
						logicalExpression(6);
						}
						break;
					}
					} 
				}
				setState(291);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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
		enterRule(_localctx, 42, RULE_equalityExpression);
		int _la;
		try {
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(292);
				additiveExpression(0);
				setState(293);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__41) | (1L << T__50) | (1L << T__51) | (1L << T__52) | (1L << T__53) | (1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__57))) != 0) || _la==IN || _la==NOTIN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(294);
				additiveExpression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(296);
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
		int _startState = 44;
		enterRecursionRule(_localctx, 44, RULE_additiveExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(300);
				factorExpression(0);
				setState(301);
				_la = _input.LA(1);
				if ( !(_la==T__60 || _la==T__61) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(302);
				factorExpression(0);
				}
				break;
			case 2:
				{
				setState(304);
				factorExpression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(315);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(313);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(307);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(308);
						match(T__58);
						setState(309);
						additiveExpression(5);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(310);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(311);
						match(T__59);
						setState(312);
						factorExpression(0);
						}
						break;
					}
					} 
				}
				setState(317);
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

	public static class FactorExpressionContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode INTEGRAL() { return getToken(MathOCLParser.INTEGRAL, 0); }
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public TerminalNode SIGMA() { return getToken(MathOCLParser.SIGMA, 0); }
		public TerminalNode PRODUCT() { return getToken(MathOCLParser.PRODUCT, 0); }
		public List<FactorExpressionContext> factorExpression() {
			return getRuleContexts(FactorExpressionContext.class);
		}
		public FactorExpressionContext factorExpression(int i) {
			return getRuleContext(FactorExpressionContext.class,i);
		}
		public TerminalNode SQUAREROOT() { return getToken(MathOCLParser.SQUAREROOT, 0); }
		public TerminalNode PARTIALDIFF() { return getToken(MathOCLParser.PARTIALDIFF, 0); }
		public Factor2ExpressionContext factor2Expression() {
			return getRuleContext(Factor2ExpressionContext.class,0);
		}
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
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_factorExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(371);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(319);
				match(T__62);
				setState(320);
				match(T__63);
				setState(321);
				expression();
				setState(322);
				match(T__36);
				setState(323);
				match(T__64);
				setState(324);
				expression();
				setState(325);
				match(T__36);
				}
				break;
			case 2:
				{
				setState(327);
				match(INTEGRAL);
				setState(328);
				match(T__63);
				setState(329);
				expression();
				setState(330);
				match(T__36);
				setState(331);
				match(T__64);
				setState(332);
				expression();
				setState(333);
				match(T__36);
				setState(334);
				expression();
				setState(335);
				match(ID);
				}
				break;
			case 3:
				{
				setState(337);
				match(INTEGRAL);
				setState(338);
				expression();
				setState(339);
				match(ID);
				}
				break;
			case 4:
				{
				setState(341);
				match(SIGMA);
				setState(342);
				match(T__63);
				setState(343);
				expression();
				setState(344);
				match(T__36);
				setState(345);
				match(T__64);
				setState(346);
				expression();
				setState(347);
				match(T__36);
				setState(348);
				expression();
				}
				break;
			case 5:
				{
				setState(350);
				match(PRODUCT);
				setState(351);
				match(T__63);
				setState(352);
				expression();
				setState(353);
				match(T__36);
				setState(354);
				match(T__64);
				setState(355);
				expression();
				setState(356);
				match(T__36);
				setState(357);
				expression();
				}
				break;
			case 6:
				{
				setState(359);
				match(T__59);
				setState(360);
				factorExpression(7);
				}
				break;
			case 7:
				{
				setState(361);
				match(T__58);
				setState(362);
				factorExpression(6);
				}
				break;
			case 8:
				{
				setState(363);
				match(SQUAREROOT);
				setState(364);
				factorExpression(5);
				}
				break;
			case 9:
				{
				setState(365);
				match(PARTIALDIFF);
				setState(366);
				match(T__63);
				setState(367);
				match(ID);
				setState(368);
				match(T__36);
				setState(369);
				factorExpression(4);
				}
				break;
			case 10:
				{
				setState(370);
				factor2Expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(382);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(380);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(373);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(374);
						_la = _input.LA(1);
						if ( !(((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & ((1L << (T__65 - 66)) | (1L << (T__66 - 66)) | (1L << (T__67 - 66)) | (1L << (T__68 - 66)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(375);
						factorExpression(13);
						}
						break;
					case 2:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(376);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(377);
						match(T__69);
						}
						break;
					case 3:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(378);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(379);
						match(DIFFERENTIAL);
						}
						break;
					}
					} 
				}
				setState(384);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
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
		int _startState = 48;
		enterRecursionRule(_localctx, 48, RULE_factor2Expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(388);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__99:
			case T__100:
			case T__101:
			case T__102:
			case T__103:
				{
				setState(386);
				setExpression();
				}
				break;
			case T__23:
			case T__30:
			case T__31:
			case T__35:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INFINITY:
			case EMPTYSET:
			case INT:
			case ID:
				{
				setState(387);
				basicExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(428);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(426);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(390);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(391);
						match(T__70);
						}
						break;
					case 2:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(392);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(393);
						_la = _input.LA(1);
						if ( !(((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & ((1L << (T__71 - 72)) | (1L << (T__72 - 72)) | (1L << (T__73 - 72)) | (1L << (T__74 - 72)) | (1L << (T__75 - 72)) | (1L << (T__76 - 72)) | (1L << (T__77 - 72)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 3:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(394);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(395);
						match(T__78);
						}
						break;
					case 4:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(396);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(397);
						match(T__79);
						}
						break;
					case 5:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(398);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(399);
						match(T__80);
						}
						break;
					case 6:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(400);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(401);
						match(T__81);
						}
						break;
					case 7:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(402);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(403);
						match(T__82);
						}
						break;
					case 8:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(404);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(405);
						match(T__83);
						}
						break;
					case 9:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(406);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(407);
						match(T__84);
						}
						break;
					case 10:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(408);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(409);
						match(T__85);
						}
						break;
					case 11:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(410);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(411);
						match(T__64);
						setState(412);
						expression();
						setState(413);
						match(T__36);
						}
						break;
					case 12:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(415);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(416);
						match(T__63);
						setState(417);
						expression();
						setState(418);
						match(T__36);
						}
						break;
					case 13:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(420);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(421);
						_la = _input.LA(1);
						if ( !(((((_la - 87)) & ~0x3f) == 0 && ((1L << (_la - 87)) & ((1L << (T__86 - 87)) | (1L << (T__87 - 87)) | (1L << (T__88 - 87)) | (1L << (T__89 - 87)) | (1L << (T__90 - 87)) | (1L << (T__91 - 87)) | (1L << (T__92 - 87)) | (1L << (T__93 - 87)) | (1L << (T__94 - 87)) | (1L << (T__95 - 87)) | (1L << (T__96 - 87)) | (1L << (T__97 - 87)) | (1L << (T__98 - 87)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(422);
						match(T__23);
						setState(423);
						expression();
						setState(424);
						match(T__24);
						}
						break;
					}
					} 
				}
				setState(430);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
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
		enterRule(_localctx, 50, RULE_setExpression);
		int _la;
		try {
			setState(456);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__99:
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				match(T__99);
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
					{
					setState(432);
					expressionList();
					}
				}

				setState(435);
				match(T__36);
				}
				break;
			case T__100:
				enterOuterAlt(_localctx, 2);
				{
				setState(436);
				match(T__100);
				setState(438);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
					{
					setState(437);
					expressionList();
					}
				}

				setState(440);
				match(T__36);
				}
				break;
			case T__101:
				enterOuterAlt(_localctx, 3);
				{
				setState(441);
				match(T__101);
				setState(443);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
					{
					setState(442);
					expressionList();
					}
				}

				setState(445);
				match(T__36);
				}
				break;
			case T__102:
				enterOuterAlt(_localctx, 4);
				{
				setState(446);
				match(T__102);
				setState(448);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
					{
					setState(447);
					expressionList();
					}
				}

				setState(450);
				match(T__36);
				}
				break;
			case T__103:
				enterOuterAlt(_localctx, 5);
				{
				setState(451);
				match(T__103);
				setState(453);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__23) | (1L << T__30) | (1L << T__31) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 100)) & ~0x3f) == 0 && ((1L << (_la - 100)) & ((1L << (T__99 - 100)) | (1L << (T__100 - 100)) | (1L << (T__101 - 100)) | (1L << (T__102 - 100)) | (1L << (T__103 - 100)) | (1L << (FLOAT_LITERAL - 100)) | (1L << (STRING_LITERAL - 100)) | (1L << (NULL_LITERAL - 100)) | (1L << (INTEGRAL - 100)) | (1L << (SIGMA - 100)) | (1L << (PRODUCT - 100)) | (1L << (INFINITY - 100)) | (1L << (PARTIALDIFF - 100)) | (1L << (FORALL - 100)) | (1L << (EXISTS - 100)) | (1L << (EMPTYSET - 100)) | (1L << (SQUAREROOT - 100)) | (1L << (INT - 100)) | (1L << (ID - 100)))) != 0)) {
					{
					setState(452);
					expressionList();
					}
				}

				setState(455);
				match(T__36);
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
		enterRule(_localctx, 52, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458);
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
		case 16:
			return basicExpression_sempred((BasicExpressionContext)_localctx, predIndex);
		case 20:
			return logicalExpression_sempred((LogicalExpressionContext)_localctx, predIndex);
		case 22:
			return additiveExpression_sempred((AdditiveExpressionContext)_localctx, predIndex);
		case 23:
			return factorExpression_sempred((FactorExpressionContext)_localctx, predIndex);
		case 24:
			return factor2Expression_sempred((Factor2ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean basicExpression_sempred(BasicExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 11);
		case 1:
			return precpred(_ctx, 10);
		case 2:
			return precpred(_ctx, 9);
		}
		return true;
	}
	private boolean logicalExpression_sempred(LogicalExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 10);
		case 4:
			return precpred(_ctx, 9);
		case 5:
			return precpred(_ctx, 8);
		case 6:
			return precpred(_ctx, 7);
		case 7:
			return precpred(_ctx, 6);
		case 8:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean additiveExpression_sempred(AdditiveExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 4);
		case 10:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean factorExpression_sempred(FactorExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 11:
			return precpred(_ctx, 12);
		case 12:
			return precpred(_ctx, 3);
		case 13:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean factor2Expression_sempred(Factor2ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 14:
			return precpred(_ctx, 15);
		case 15:
			return precpred(_ctx, 14);
		case 16:
			return precpred(_ctx, 13);
		case 17:
			return precpred(_ctx, 12);
		case 18:
			return precpred(_ctx, 11);
		case 19:
			return precpred(_ctx, 10);
		case 20:
			return precpred(_ctx, 9);
		case 21:
			return precpred(_ctx, 8);
		case 22:
			return precpred(_ctx, 7);
		case 23:
			return precpred(_ctx, 6);
		case 24:
			return precpred(_ctx, 5);
		case 25:
			return precpred(_ctx, 4);
		case 26:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0082\u01cf\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\2\7\2<\n\2\f\2\16\2?\13\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3M\n\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\7"+
		"\16\u0085\n\16\f\16\16\16\u0088\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\5\17\u00b2\n\17\3\20\3\20\3\20\7\20\u00b7"+
		"\n\20\f\20\16\20\u00ba\13\20\3\20\3\20\3\21\3\21\3\21\3\21\5\21\u00c2"+
		"\n\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\5\22\u00d5\n\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22"+
		"\u00dd\n\22\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u00e5\n\22\f\22\16\22\u00e8"+
		"\13\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u010e\n\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\7\26\u0122\n\26\f\26\16\26\u0125\13\26\3\27\3\27\3\27\3\27"+
		"\3\27\5\27\u012c\n\27\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0134\n\30\3"+
		"\30\3\30\3\30\3\30\3\30\3\30\7\30\u013c\n\30\f\30\16\30\u013f\13\30\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0176\n\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\7\31\u017f\n\31\f\31\16\31\u0182\13"+
		"\31\3\32\3\32\3\32\5\32\u0187\n\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\7\32\u01ad\n\32\f\32\16\32\u01b0\13\32\3\33\3\33\5\33\u01b4\n\33"+
		"\3\33\3\33\3\33\5\33\u01b9\n\33\3\33\3\33\3\33\5\33\u01be\n\33\3\33\3"+
		"\33\3\33\5\33\u01c3\n\33\3\33\3\33\3\33\5\33\u01c8\n\33\3\33\5\33\u01cb"+
		"\n\33\3\34\3\34\3\34\2\7\"*.\60\62\35\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\66\2\7\6\2\5\5,,\65<op\3\2?@\3\2DG\3\2JP\3\2"+
		"Ye\2\u0209\28\3\2\2\2\4L\3\2\2\2\6N\3\2\2\2\bS\3\2\2\2\nY\3\2\2\2\f^\3"+
		"\2\2\2\16a\3\2\2\2\20h\3\2\2\2\22m\3\2\2\2\24r\3\2\2\2\26x\3\2\2\2\30"+
		"}\3\2\2\2\32\u0086\3\2\2\2\34\u00b1\3\2\2\2\36\u00b8\3\2\2\2 \u00c1\3"+
		"\2\2\2\"\u00d4\3\2\2\2$\u00e9\3\2\2\2&\u00f1\3\2\2\2(\u00f8\3\2\2\2*\u010d"+
		"\3\2\2\2,\u012b\3\2\2\2.\u0133\3\2\2\2\60\u0175\3\2\2\2\62\u0186\3\2\2"+
		"\2\64\u01ca\3\2\2\2\66\u01cc\3\2\2\289\7\3\2\29=\7\u0081\2\2:<\5\4\3\2"+
		";:\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>@\3\2\2\2?=\3\2\2\2@A\7\2\2\3"+
		"A\3\3\2\2\2BM\5\6\4\2CM\5\b\5\2DM\5\n\6\2EM\5\24\13\2FM\5\f\7\2GM\5\16"+
		"\b\2HM\5\20\t\2IM\5\22\n\2JM\5\26\f\2KM\5\30\r\2LB\3\2\2\2LC\3\2\2\2L"+
		"D\3\2\2\2LE\3\2\2\2LF\3\2\2\2LG\3\2\2\2LH\3\2\2\2LI\3\2\2\2LJ\3\2\2\2"+
		"LK\3\2\2\2M\5\3\2\2\2NO\7\4\2\2OP\7\u0081\2\2PQ\7\5\2\2QR\5 \21\2R\7\3"+
		"\2\2\2ST\7\6\2\2TU\7\7\2\2UV\5 \21\2VW\7\b\2\2WX\5*\26\2X\t\3\2\2\2YZ"+
		"\7\t\2\2Z[\5 \21\2[\\\7\n\2\2\\]\5 \21\2]\13\3\2\2\2^_\7\13\2\2_`\5 \21"+
		"\2`\r\3\2\2\2ab\7\f\2\2bc\5 \21\2cd\7\r\2\2de\7\u0081\2\2ef\7\16\2\2f"+
		"g\5 \21\2g\17\3\2\2\2hi\7\17\2\2ij\5\36\20\2jk\7\r\2\2kl\5\32\16\2l\21"+
		"\3\2\2\2mn\7\20\2\2no\5*\26\2op\7\21\2\2pq\5\36\20\2q\23\3\2\2\2rs\7\22"+
		"\2\2st\5 \21\2tu\7\23\2\2uv\7\u0080\2\2vw\7\24\2\2w\25\3\2\2\2xy\7\25"+
		"\2\2yz\5 \21\2z{\7\26\2\2{|\5 \21\2|\27\3\2\2\2}~\7\27\2\2~\177\5 \21"+
		"\2\177\u0080\7\16\2\2\u0080\u0081\5 \21\2\u0081\31\3\2\2\2\u0082\u0083"+
		"\7\u0081\2\2\u0083\u0085\7\30\2\2\u0084\u0082\3\2\2\2\u0085\u0088\3\2"+
		"\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0089\u008a\7\u0081\2\2\u008a\33\3\2\2\2\u008b\u008c\7"+
		"\31\2\2\u008c\u008d\7\32\2\2\u008d\u008e\5\34\17\2\u008e\u008f\7\33\2"+
		"\2\u008f\u00b2\3\2\2\2\u0090\u0091\7\34\2\2\u0091\u0092\7\32\2\2\u0092"+
		"\u0093\5\34\17\2\u0093\u0094\7\33\2\2\u0094\u00b2\3\2\2\2\u0095\u0096"+
		"\7\35\2\2\u0096\u0097\7\32\2\2\u0097\u0098\5\34\17\2\u0098\u0099\7\33"+
		"\2\2\u0099\u00b2\3\2\2\2\u009a\u009b\7\36\2\2\u009b\u009c\7\32\2\2\u009c"+
		"\u009d\5\34\17\2\u009d\u009e\7\33\2\2\u009e\u00b2\3\2\2\2\u009f\u00a0"+
		"\7\37\2\2\u00a0\u00a1\7\32\2\2\u00a1\u00a2\5\34\17\2\u00a2\u00a3\7\30"+
		"\2\2\u00a3\u00a4\5\34\17\2\u00a4\u00a5\7\33\2\2\u00a5\u00b2\3\2\2\2\u00a6"+
		"\u00a7\7 \2\2\u00a7\u00a8\7\32\2\2\u00a8\u00a9\5\34\17\2\u00a9\u00aa\7"+
		"\30\2\2\u00aa\u00ab\5\34\17\2\u00ab\u00ac\7\33\2\2\u00ac\u00b2\3\2\2\2"+
		"\u00ad\u00b2\7{\2\2\u00ae\u00b2\7|\2\2\u00af\u00b2\7}\2\2\u00b0\u00b2"+
		"\7\u0081\2\2\u00b1\u008b\3\2\2\2\u00b1\u0090\3\2\2\2\u00b1\u0095\3\2\2"+
		"\2\u00b1\u009a\3\2\2\2\u00b1\u009f\3\2\2\2\u00b1\u00a6\3\2\2\2\u00b1\u00ad"+
		"\3\2\2\2\u00b1\u00ae\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b0\3\2\2\2\u00b2"+
		"\35\3\2\2\2\u00b3\u00b4\5 \21\2\u00b4\u00b5\7\30\2\2\u00b5\u00b7\3\2\2"+
		"\2\u00b6\u00b3\3\2\2\2\u00b7\u00ba\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9"+
		"\3\2\2\2\u00b9\u00bb\3\2\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bc\5 \21\2\u00bc"+
		"\37\3\2\2\2\u00bd\u00c2\5*\26\2\u00be\u00c2\5$\23\2\u00bf\u00c2\5&\24"+
		"\2\u00c0\u00c2\5(\25\2\u00c1\u00bd\3\2\2\2\u00c1\u00be\3\2\2\2\u00c1\u00bf"+
		"\3\2\2\2\u00c1\u00c0\3\2\2\2\u00c2!\3\2\2\2\u00c3\u00c4\b\22\1\2\u00c4"+
		"\u00d5\7m\2\2\u00c5\u00d5\7!\2\2\u00c6\u00d5\7\"\2\2\u00c7\u00d5\5\66"+
		"\34\2\u00c8\u00c9\7&\2\2\u00c9\u00ca\7\u0081\2\2\u00ca\u00d5\7\'\2\2\u00cb"+
		"\u00d5\7\u0080\2\2\u00cc\u00d5\7k\2\2\u00cd\u00d5\7l\2\2\u00ce\u00d5\7"+
		"t\2\2\u00cf\u00d5\7y\2\2\u00d0\u00d1\7\32\2\2\u00d1\u00d2\5 \21\2\u00d2"+
		"\u00d3\7\33\2\2\u00d3\u00d5\3\2\2\2\u00d4\u00c3\3\2\2\2\u00d4\u00c5\3"+
		"\2\2\2\u00d4\u00c6\3\2\2\2\u00d4\u00c7\3\2\2\2\u00d4\u00c8\3\2\2\2\u00d4"+
		"\u00cb\3\2\2\2\u00d4\u00cc\3\2\2\2\u00d4\u00cd\3\2\2\2\u00d4\u00ce\3\2"+
		"\2\2\u00d4\u00cf\3\2\2\2\u00d4\u00d0\3\2\2\2\u00d5\u00e6\3\2\2\2\u00d6"+
		"\u00d7\f\r\2\2\u00d7\u00d8\7#\2\2\u00d8\u00e5\7\u0081\2\2\u00d9\u00da"+
		"\f\f\2\2\u00da\u00dc\7\32\2\2\u00db\u00dd\5\36\20\2\u00dc\u00db\3\2\2"+
		"\2\u00dc\u00dd\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00e5\7\33\2\2\u00df"+
		"\u00e0\f\13\2\2\u00e0\u00e1\7$\2\2\u00e1\u00e2\5 \21\2\u00e2\u00e3\7%"+
		"\2\2\u00e3\u00e5\3\2\2\2\u00e4\u00d6\3\2\2\2\u00e4\u00d9\3\2\2\2\u00e4"+
		"\u00df\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e7\3\2"+
		"\2\2\u00e7#\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ea\7\21\2\2\u00ea\u00eb"+
		"\5 \21\2\u00eb\u00ec\7(\2\2\u00ec\u00ed\5 \21\2\u00ed\u00ee\7)\2\2\u00ee"+
		"\u00ef\5 \21\2\u00ef\u00f0\7*\2\2\u00f0%\3\2\2\2\u00f1\u00f2\7+\2\2\u00f2"+
		"\u00f3\5\66\34\2\u00f3\u00f4\7,\2\2\u00f4\u00f5\5\34\17\2\u00f5\u00f6"+
		"\7\16\2\2\u00f6\u00f7\5 \21\2\u00f7\'\3\2\2\2\u00f8\u00f9\7-\2\2\u00f9"+
		"\u00fa\5\66\34\2\u00fa\u00fb\7\5\2\2\u00fb\u00fc\5 \21\2\u00fc\u00fd\7"+
		"\16\2\2\u00fd\u00fe\5 \21\2\u00fe)\3\2\2\2\u00ff\u0100\b\26\1\2\u0100"+
		"\u0101\7w\2\2\u0101\u0102\5\66\34\2\u0102\u0103\7~\2\2\u0103\u0104\5*"+
		"\26\6\u0104\u010e\3\2\2\2\u0105\u0106\7x\2\2\u0106\u0107\5\66\34\2\u0107"+
		"\u0108\7~\2\2\u0108\u0109\5*\26\5\u0109\u010e\3\2\2\2\u010a\u010b\7\64"+
		"\2\2\u010b\u010e\5*\26\4\u010c\u010e\5,\27\2\u010d\u00ff\3\2\2\2\u010d"+
		"\u0105\3\2\2\2\u010d\u010a\3\2\2\2\u010d\u010c\3\2\2\2\u010e\u0123\3\2"+
		"\2\2\u010f\u0110\f\f\2\2\u0110\u0111\7.\2\2\u0111\u0122\5*\26\r\u0112"+
		"\u0113\f\13\2\2\u0113\u0114\7/\2\2\u0114\u0122\5*\26\f\u0115\u0116\f\n"+
		"\2\2\u0116\u0117\7\60\2\2\u0117\u0122\5*\26\13\u0118\u0119\f\t\2\2\u0119"+
		"\u011a\7\61\2\2\u011a\u0122\5*\26\n\u011b\u011c\f\b\2\2\u011c\u011d\7"+
		"\62\2\2\u011d\u0122\5*\26\t\u011e\u011f\f\7\2\2\u011f\u0120\7\63\2\2\u0120"+
		"\u0122\5*\26\b\u0121\u010f\3\2\2\2\u0121\u0112\3\2\2\2\u0121\u0115\3\2"+
		"\2\2\u0121\u0118\3\2\2\2\u0121\u011b\3\2\2\2\u0121\u011e\3\2\2\2\u0122"+
		"\u0125\3\2\2\2\u0123\u0121\3\2\2\2\u0123\u0124\3\2\2\2\u0124+\3\2\2\2"+
		"\u0125\u0123\3\2\2\2\u0126\u0127\5.\30\2\u0127\u0128\t\2\2\2\u0128\u0129"+
		"\5.\30\2\u0129\u012c\3\2\2\2\u012a\u012c\5.\30\2\u012b\u0126\3\2\2\2\u012b"+
		"\u012a\3\2\2\2\u012c-\3\2\2\2\u012d\u012e\b\30\1\2\u012e\u012f\5\60\31"+
		"\2\u012f\u0130\t\3\2\2\u0130\u0131\5\60\31\2\u0131\u0134\3\2\2\2\u0132"+
		"\u0134\5\60\31\2\u0133\u012d\3\2\2\2\u0133\u0132\3\2\2\2\u0134\u013d\3"+
		"\2\2\2\u0135\u0136\f\6\2\2\u0136\u0137\7=\2\2\u0137\u013c\5.\30\7\u0138"+
		"\u0139\f\5\2\2\u0139\u013a\7>\2\2\u013a\u013c\5\60\31\2\u013b\u0135\3"+
		"\2\2\2\u013b\u0138\3\2\2\2\u013c\u013f\3\2\2\2\u013d\u013b\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013e/\3\2\2\2\u013f\u013d\3\2\2\2\u0140\u0141\b\31\1\2"+
		"\u0141\u0142\7A\2\2\u0142\u0143\7B\2\2\u0143\u0144\5 \21\2\u0144\u0145"+
		"\7\'\2\2\u0145\u0146\7C\2\2\u0146\u0147\5 \21\2\u0147\u0148\7\'\2\2\u0148"+
		"\u0176\3\2\2\2\u0149\u014a\7q\2\2\u014a\u014b\7B\2\2\u014b\u014c\5 \21"+
		"\2\u014c\u014d\7\'\2\2\u014d\u014e\7C\2\2\u014e\u014f\5 \21\2\u014f\u0150"+
		"\7\'\2\2\u0150\u0151\5 \21\2\u0151\u0152\7\u0081\2\2\u0152\u0176\3\2\2"+
		"\2\u0153\u0154\7q\2\2\u0154\u0155\5 \21\2\u0155\u0156\7\u0081\2\2\u0156"+
		"\u0176\3\2\2\2\u0157\u0158\7r\2\2\u0158\u0159\7B\2\2\u0159\u015a\5 \21"+
		"\2\u015a\u015b\7\'\2\2\u015b\u015c\7C\2\2\u015c\u015d\5 \21\2\u015d\u015e"+
		"\7\'\2\2\u015e\u015f\5 \21\2\u015f\u0176\3\2\2\2\u0160\u0161\7s\2\2\u0161"+
		"\u0162\7B\2\2\u0162\u0163\5 \21\2\u0163\u0164\7\'\2\2\u0164\u0165\7C\2"+
		"\2\u0165\u0166\5 \21\2\u0166\u0167\7\'\2\2\u0167\u0168\5 \21\2\u0168\u0176"+
		"\3\2\2\2\u0169\u016a\7>\2\2\u016a\u0176\5\60\31\t\u016b\u016c\7=\2\2\u016c"+
		"\u0176\5\60\31\b\u016d\u016e\7z\2\2\u016e\u0176\5\60\31\7\u016f\u0170"+
		"\7v\2\2\u0170\u0171\7B\2\2\u0171\u0172\7\u0081\2\2\u0172\u0173\7\'\2\2"+
		"\u0173\u0176\5\60\31\6\u0174\u0176\5\62\32\2\u0175\u0140\3\2\2\2\u0175"+
		"\u0149\3\2\2\2\u0175\u0153\3\2\2\2\u0175\u0157\3\2\2\2\u0175\u0160\3\2"+
		"\2\2\u0175\u0169\3\2\2\2\u0175\u016b\3\2\2\2\u0175\u016d\3\2\2\2\u0175"+
		"\u016f\3\2\2\2\u0175\u0174\3\2\2\2\u0176\u0180\3\2\2\2\u0177\u0178\f\16"+
		"\2\2\u0178\u0179\t\4\2\2\u0179\u017f\5\60\31\17\u017a\u017b\f\5\2\2\u017b"+
		"\u017f\7H\2\2\u017c\u017d\f\4\2\2\u017d\u017f\7u\2\2\u017e\u0177\3\2\2"+
		"\2\u017e\u017a\3\2\2\2\u017e\u017c\3\2\2\2\u017f\u0182\3\2\2\2\u0180\u017e"+
		"\3\2\2\2\u0180\u0181\3\2\2\2\u0181\61\3\2\2\2\u0182\u0180\3\2\2\2\u0183"+
		"\u0184\b\32\1\2\u0184\u0187\5\64\33\2\u0185\u0187\5\"\22\2\u0186\u0183"+
		"\3\2\2\2\u0186\u0185\3\2\2\2\u0187\u01ae\3\2\2\2\u0188\u0189\f\21\2\2"+
		"\u0189\u01ad\7I\2\2\u018a\u018b\f\20\2\2\u018b\u01ad\t\5\2\2\u018c\u018d"+
		"\f\17\2\2\u018d\u01ad\7Q\2\2\u018e\u018f\f\16\2\2\u018f\u01ad\7R\2\2\u0190"+
		"\u0191\f\r\2\2\u0191\u01ad\7S\2\2\u0192\u0193\f\f\2\2\u0193\u01ad\7T\2"+
		"\2\u0194\u0195\f\13\2\2\u0195\u01ad\7U\2\2\u0196\u0197\f\n\2\2\u0197\u01ad"+
		"\7V\2\2\u0198\u0199\f\t\2\2\u0199\u01ad\7W\2\2\u019a\u019b\f\b\2\2\u019b"+
		"\u01ad\7X\2\2\u019c\u019d\f\7\2\2\u019d\u019e\7C\2\2\u019e\u019f\5 \21"+
		"\2\u019f\u01a0\7\'\2\2\u01a0\u01ad\3\2\2\2\u01a1\u01a2\f\6\2\2\u01a2\u01a3"+
		"\7B\2\2\u01a3\u01a4\5 \21\2\u01a4\u01a5\7\'\2\2\u01a5\u01ad\3\2\2\2\u01a6"+
		"\u01a7\f\5\2\2\u01a7\u01a8\t\6\2\2\u01a8\u01a9\7\32\2\2\u01a9\u01aa\5"+
		" \21\2\u01aa\u01ab\7\33\2\2\u01ab\u01ad\3\2\2\2\u01ac\u0188\3\2\2\2\u01ac"+
		"\u018a\3\2\2\2\u01ac\u018c\3\2\2\2\u01ac\u018e\3\2\2\2\u01ac\u0190\3\2"+
		"\2\2\u01ac\u0192\3\2\2\2\u01ac\u0194\3\2\2\2\u01ac\u0196\3\2\2\2\u01ac"+
		"\u0198\3\2\2\2\u01ac\u019a\3\2\2\2\u01ac\u019c\3\2\2\2\u01ac\u01a1\3\2"+
		"\2\2\u01ac\u01a6\3\2\2\2\u01ad\u01b0\3\2\2\2\u01ae\u01ac\3\2\2\2\u01ae"+
		"\u01af\3\2\2\2\u01af\63\3\2\2\2\u01b0\u01ae\3\2\2\2\u01b1\u01b3\7f\2\2"+
		"\u01b2\u01b4\5\36\20\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5"+
		"\3\2\2\2\u01b5\u01cb\7\'\2\2\u01b6\u01b8\7g\2\2\u01b7\u01b9\5\36\20\2"+
		"\u01b8\u01b7\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01cb"+
		"\7\'\2\2\u01bb\u01bd\7h\2\2\u01bc\u01be\5\36\20\2\u01bd\u01bc\3\2\2\2"+
		"\u01bd\u01be\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01cb\7\'\2\2\u01c0\u01c2"+
		"\7i\2\2\u01c1\u01c3\5\36\20\2\u01c2\u01c1\3\2\2\2\u01c2\u01c3\3\2\2\2"+
		"\u01c3\u01c4\3\2\2\2\u01c4\u01cb\7\'\2\2\u01c5\u01c7\7j\2\2\u01c6\u01c8"+
		"\5\36\20\2\u01c7\u01c6\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01c9\3\2\2\2"+
		"\u01c9\u01cb\7\'\2\2\u01ca\u01b1\3\2\2\2\u01ca\u01b6\3\2\2\2\u01ca\u01bb"+
		"\3\2\2\2\u01ca\u01c0\3\2\2\2\u01ca\u01c5\3\2\2\2\u01cb\65\3\2\2\2\u01cc"+
		"\u01cd\7\u0081\2\2\u01cd\67\3\2\2\2\37=L\u0086\u00b1\u00b8\u00c1\u00d4"+
		"\u00dc\u00e4\u00e6\u010d\u0121\u0123\u012b\u0133\u013b\u013d\u0175\u017e"+
		"\u0180\u0186\u01ac\u01ae\u01b3\u01b8\u01bd\u01c2\u01c7\u01ca";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}