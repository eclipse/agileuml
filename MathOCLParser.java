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
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, FLOAT_LITERAL=101, 
		STRING_LITERAL=102, NULL_LITERAL=103, MULTILINE_COMMENT=104, IN=105, NOTIN=106, 
		INTEGRAL=107, SIGMA=108, PRODUCT=109, INFINITY=110, DIFFERENTIAL=111, 
		PARTIALDIFF=112, FORALL=113, EXISTS=114, EMPTYSET=115, SQUAREROOT=116, 
		NATURAL=117, INTEGER=118, REAL=119, CDOT=120, NEWLINE=121, INT=122, ID=123, 
		WS=124;
	public static final int
		RULE_specification = 0, RULE_part = 1, RULE_formula = 2, RULE_constraint = 3, 
		RULE_reexpression = 4, RULE_simplify = 5, RULE_substituting = 6, RULE_solve = 7, 
		RULE_prove = 8, RULE_expanding = 9, RULE_idList = 10, RULE_type = 11, 
		RULE_expressionList = 12, RULE_expression = 13, RULE_basicExpression = 14, 
		RULE_conditionalExpression = 15, RULE_lambdaExpression = 16, RULE_letExpression = 17, 
		RULE_logicalExpression = 18, RULE_equalityExpression = 19, RULE_additiveExpression = 20, 
		RULE_factorExpression = 21, RULE_factor2Expression = 22, RULE_setExpression = 23, 
		RULE_identifier = 24;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "part", "formula", "constraint", "reexpression", "simplify", 
			"substituting", "solve", "prove", "expanding", "idList", "type", "expressionList", 
			"expression", "basicExpression", "conditionalExpression", "lambdaExpression", 
			"letExpression", "logicalExpression", "equalityExpression", "additiveExpression", 
			"factorExpression", "factor2Expression", "setExpression", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'specification'", "'{'", "'}'", "'Constraint'", "'on'", "'|'", 
			"'Express'", "'as'", "'Simplify'", "'Substitute'", "'for'", "'in'", "'Solve'", 
			"'Prove'", "'if'", "'Expanding'", "'to'", "','", "'Sequence'", "'('", 
			"')'", "'Set'", "'Bag'", "'OrderedSet'", "'Map'", "'Function'", "'true'", 
			"'false'", "'.'", "'['", "']'", "'g{'", "'then'", "'else'", "'endif'", 
			"'lambda'", "':'", "'let'", "'='", "'=>'", "'implies'", "'or'", "'xor'", 
			"'&'", "'and'", "'not'", "'<'", "'>'", "'>='", "'<='", "'/='", "'<>'", 
			"'/:'", "'<:'", "'+'", "'-'", "'..'", "'|->'", "'C'", "'_{'", "'^{'", 
			"'*'", "'/'", "'mod'", "'div'", "'!'", "'->size()'", "'->isEmpty()'", 
			"'->notEmpty()'", "'->asSet()'", "'->asBag()'", "'->asOrderedSet()'", 
			"'->asSequence()'", "'->sort()'", "'->any()'", "'->first()'", "'->last()'", 
			"'->front()'", "'->tail()'", "'->reverse()'", "'->max()'", "'->min()'", 
			"'->at'", "'->union'", "'->intersection'", "'->includes'", "'->excludes'", 
			"'->including'", "'->excluding'", "'->includesAll'", "'->excludesAll'", 
			"'->prepend'", "'->append'", "'->count'", "'->apply'", "'OrderedSet{'", 
			"'Bag{'", "'Set{'", "'Sequence{'", "'Map{'", null, null, "'null'", null, 
			"'\u00A9'", "'\u00A2'", "'\u2021'", "'\u20AC'", "'\u00D7'", "'\u2026'", 
			"'\u00B4'", "'\u00D0'", "'\u00A1'", "'\u00A3'", "'\u00D8'", "'\u2020'", 
			"'\u00D1'", "'\u017D'", "'\u00AE'", "'\u2022'"
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
			null, null, null, null, null, "FLOAT_LITERAL", "STRING_LITERAL", "NULL_LITERAL", 
			"MULTILINE_COMMENT", "IN", "NOTIN", "INTEGRAL", "SIGMA", "PRODUCT", "INFINITY", 
			"DIFFERENTIAL", "PARTIALDIFF", "FORALL", "EXISTS", "EMPTYSET", "SQUAREROOT", 
			"NATURAL", "INTEGER", "REAL", "CDOT", "NEWLINE", "INT", "ID", "WS"
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
			setState(50);
			match(T__0);
			setState(51);
			match(ID);
			setState(52);
			match(T__1);
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__6) | (1L << T__8) | (1L << T__9) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
				{
				{
				setState(53);
				part();
				}
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(59);
			match(T__2);
			setState(60);
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
			setState(70);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
			case T__19:
			case T__26:
			case T__27:
			case T__31:
			case T__35:
			case T__37:
			case T__45:
			case T__54:
			case T__55:
			case T__58:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
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
				setState(62);
				formula();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				constraint();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 3);
				{
				setState(64);
				reexpression();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 4);
				{
				setState(65);
				expanding();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(66);
				simplify();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 6);
				{
				setState(67);
				substituting();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 7);
				{
				setState(68);
				solve();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 8);
				{
				setState(69);
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
			setState(72);
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
			setState(74);
			match(T__3);
			setState(75);
			match(T__4);
			setState(76);
			expression();
			setState(77);
			match(T__5);
			setState(78);
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
			setState(80);
			match(T__6);
			setState(81);
			expression();
			setState(82);
			match(T__7);
			setState(83);
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
			setState(85);
			match(T__8);
			setState(86);
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
			setState(88);
			match(T__9);
			setState(89);
			expression();
			setState(90);
			match(T__10);
			setState(91);
			match(ID);
			setState(92);
			match(T__11);
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
			setState(95);
			match(T__12);
			setState(96);
			expressionList();
			setState(97);
			match(T__10);
			setState(98);
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
			setState(100);
			match(T__13);
			setState(101);
			logicalExpression(0);
			setState(102);
			match(T__14);
			setState(103);
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
			setState(105);
			match(T__15);
			setState(106);
			expression();
			setState(107);
			match(T__16);
			setState(108);
			match(INT);
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
		enterRule(_localctx, 20, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(110);
					match(ID);
					setState(111);
					match(T__17);
					}
					} 
				}
				setState(116);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(117);
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
		enterRule(_localctx, 22, RULE_type);
		try {
			setState(157);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__18:
				enterOuterAlt(_localctx, 1);
				{
				setState(119);
				match(T__18);
				setState(120);
				match(T__19);
				setState(121);
				type();
				setState(122);
				match(T__20);
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 2);
				{
				setState(124);
				match(T__21);
				setState(125);
				match(T__19);
				setState(126);
				type();
				setState(127);
				match(T__20);
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 3);
				{
				setState(129);
				match(T__22);
				setState(130);
				match(T__19);
				setState(131);
				type();
				setState(132);
				match(T__20);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 4);
				{
				setState(134);
				match(T__23);
				setState(135);
				match(T__19);
				setState(136);
				type();
				setState(137);
				match(T__20);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 5);
				{
				setState(139);
				match(T__24);
				setState(140);
				match(T__19);
				setState(141);
				type();
				setState(142);
				match(T__17);
				setState(143);
				type();
				setState(144);
				match(T__20);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 6);
				{
				setState(146);
				match(T__25);
				setState(147);
				match(T__19);
				setState(148);
				type();
				setState(149);
				match(T__17);
				setState(150);
				type();
				setState(151);
				match(T__20);
				}
				break;
			case NATURAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(153);
				match(NATURAL);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 8);
				{
				setState(154);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(155);
				match(REAL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(156);
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
		enterRule(_localctx, 24, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(159);
					expression();
					setState(160);
					match(T__17);
					}
					} 
				}
				setState(166);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			setState(167);
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
		enterRule(_localctx, 26, RULE_expression);
		try {
			setState(173);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__19:
			case T__26:
			case T__27:
			case T__31:
			case T__45:
			case T__54:
			case T__55:
			case T__58:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
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
				setState(169);
				logicalExpression(0);
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 2);
				{
				setState(170);
				conditionalExpression();
				}
				break;
			case T__35:
				enterOuterAlt(_localctx, 3);
				{
				setState(171);
				lambdaExpression();
				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 4);
				{
				setState(172);
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
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_basicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL_LITERAL:
				{
				setState(176);
				match(NULL_LITERAL);
				}
				break;
			case T__26:
				{
				setState(177);
				match(T__26);
				}
				break;
			case T__27:
				{
				setState(178);
				match(T__27);
				}
				break;
			case ID:
				{
				setState(179);
				identifier();
				}
				break;
			case T__31:
				{
				setState(180);
				match(T__31);
				setState(181);
				match(ID);
				setState(182);
				match(T__2);
				}
				break;
			case INT:
				{
				setState(183);
				match(INT);
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(184);
				match(FLOAT_LITERAL);
				}
				break;
			case STRING_LITERAL:
				{
				setState(185);
				match(STRING_LITERAL);
				}
				break;
			case INFINITY:
				{
				setState(186);
				match(INFINITY);
				}
				break;
			case EMPTYSET:
				{
				setState(187);
				match(EMPTYSET);
				}
				break;
			case T__19:
				{
				setState(188);
				match(T__19);
				setState(189);
				expression();
				setState(190);
				match(T__20);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(210);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(208);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(194);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(195);
						match(T__28);
						setState(196);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(197);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(198);
						match(T__19);
						setState(200);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
							{
							setState(199);
							expressionList();
							}
						}

						setState(202);
						match(T__20);
						}
						break;
					case 3:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(203);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(204);
						match(T__29);
						setState(205);
						expression();
						setState(206);
						match(T__30);
						}
						break;
					}
					} 
				}
				setState(212);
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
		enterRule(_localctx, 30, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(T__14);
			setState(214);
			expression();
			setState(215);
			match(T__32);
			setState(216);
			expression();
			setState(217);
			match(T__33);
			setState(218);
			expression();
			setState(219);
			match(T__34);
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
		enterRule(_localctx, 32, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(T__35);
			setState(222);
			identifier();
			setState(223);
			match(T__36);
			setState(224);
			type();
			setState(225);
			match(T__11);
			setState(226);
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
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
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
		enterRule(_localctx, 34, RULE_letExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(T__37);
			setState(229);
			identifier();
			setState(230);
			match(T__36);
			setState(231);
			type();
			setState(232);
			match(T__38);
			setState(233);
			expression();
			setState(234);
			match(T__11);
			setState(235);
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
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode EXISTS() { return getToken(MathOCLParser.EXISTS, 0); }
		public List<LogicalExpressionContext> logicalExpression() {
			return getRuleContexts(LogicalExpressionContext.class);
		}
		public LogicalExpressionContext logicalExpression(int i) {
			return getRuleContext(LogicalExpressionContext.class,i);
		}
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
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_logicalExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				{
				setState(238);
				match(FORALL);
				setState(239);
				identifier();
				setState(240);
				match(CDOT);
				setState(241);
				expression();
				}
				break;
			case EXISTS:
				{
				setState(243);
				match(EXISTS);
				setState(244);
				identifier();
				setState(245);
				match(CDOT);
				setState(246);
				expression();
				}
				break;
			case T__45:
				{
				setState(248);
				match(T__45);
				setState(249);
				logicalExpression(2);
				}
				break;
			case T__19:
			case T__26:
			case T__27:
			case T__31:
			case T__54:
			case T__55:
			case T__58:
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
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
				setState(250);
				equalityExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(273);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(271);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(253);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(254);
						match(T__39);
						setState(255);
						logicalExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(256);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(257);
						match(T__40);
						setState(258);
						logicalExpression(10);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(259);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(260);
						match(T__41);
						setState(261);
						logicalExpression(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(262);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(263);
						match(T__42);
						setState(264);
						logicalExpression(8);
						}
						break;
					case 5:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(265);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(266);
						match(T__43);
						setState(267);
						logicalExpression(7);
						}
						break;
					case 6:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(268);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(269);
						match(T__44);
						setState(270);
						logicalExpression(6);
						}
						break;
					}
					} 
				}
				setState(275);
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
		enterRule(_localctx, 38, RULE_equalityExpression);
		int _la;
		try {
			setState(281);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(276);
				additiveExpression();
				setState(277);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__36) | (1L << T__38) | (1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__49) | (1L << T__50) | (1L << T__51) | (1L << T__52) | (1L << T__53))) != 0) || _la==IN || _la==NOTIN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(278);
				additiveExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(280);
				additiveExpression();
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
		public FactorExpressionContext factorExpression() {
			return getRuleContext(FactorExpressionContext.class,0);
		}
		public AdditiveExpressionContext additiveExpression() {
			return getRuleContext(AdditiveExpressionContext.class,0);
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
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_additiveExpression);
		int _la;
		try {
			setState(288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(283);
				factorExpression(0);
				setState(284);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__57))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(285);
				additiveExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				factorExpression(0);
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
		int _startState = 42;
		enterRecursionRule(_localctx, 42, RULE_factorExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(291);
				match(T__58);
				setState(292);
				match(T__59);
				setState(293);
				expression();
				setState(294);
				match(T__2);
				setState(295);
				match(T__60);
				setState(296);
				expression();
				setState(297);
				match(T__2);
				}
				break;
			case 2:
				{
				setState(299);
				match(INTEGRAL);
				setState(300);
				match(T__59);
				setState(301);
				expression();
				setState(302);
				match(T__2);
				setState(303);
				match(T__60);
				setState(304);
				expression();
				setState(305);
				match(T__2);
				setState(306);
				expression();
				setState(307);
				match(ID);
				}
				break;
			case 3:
				{
				setState(309);
				match(INTEGRAL);
				setState(310);
				expression();
				setState(311);
				match(ID);
				}
				break;
			case 4:
				{
				setState(313);
				match(SIGMA);
				setState(314);
				match(T__59);
				setState(315);
				expression();
				setState(316);
				match(T__2);
				setState(317);
				match(T__60);
				setState(318);
				expression();
				setState(319);
				match(T__2);
				setState(320);
				expression();
				}
				break;
			case 5:
				{
				setState(322);
				match(PRODUCT);
				setState(323);
				match(T__59);
				setState(324);
				expression();
				setState(325);
				match(T__2);
				setState(326);
				match(T__60);
				setState(327);
				expression();
				setState(328);
				match(T__2);
				setState(329);
				expression();
				}
				break;
			case 6:
				{
				setState(331);
				match(T__55);
				setState(332);
				factorExpression(7);
				}
				break;
			case 7:
				{
				setState(333);
				match(T__54);
				setState(334);
				factorExpression(6);
				}
				break;
			case 8:
				{
				setState(335);
				match(SQUAREROOT);
				setState(336);
				factorExpression(5);
				}
				break;
			case 9:
				{
				setState(337);
				match(PARTIALDIFF);
				setState(338);
				factorExpression(4);
				}
				break;
			case 10:
				{
				setState(339);
				factor2Expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(351);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(349);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(342);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(343);
						_la = _input.LA(1);
						if ( !(((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & ((1L << (T__61 - 62)) | (1L << (T__62 - 62)) | (1L << (T__63 - 62)) | (1L << (T__64 - 62)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(344);
						factorExpression(13);
						}
						break;
					case 2:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(345);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(346);
						match(T__65);
						}
						break;
					case 3:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(347);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(348);
						match(DIFFERENTIAL);
						}
						break;
					}
					} 
				}
				setState(353);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
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
		int _startState = 44;
		enterRecursionRule(_localctx, 44, RULE_factor2Expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__95:
			case T__96:
			case T__97:
			case T__98:
			case T__99:
				{
				setState(355);
				setExpression();
				}
				break;
			case T__19:
			case T__26:
			case T__27:
			case T__31:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INFINITY:
			case EMPTYSET:
			case INT:
			case ID:
				{
				setState(356);
				basicExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(397);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(395);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(359);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(360);
						match(T__66);
						}
						break;
					case 2:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(361);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(362);
						_la = _input.LA(1);
						if ( !(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & ((1L << (T__67 - 68)) | (1L << (T__68 - 68)) | (1L << (T__69 - 68)) | (1L << (T__70 - 68)) | (1L << (T__71 - 68)) | (1L << (T__72 - 68)) | (1L << (T__73 - 68)))) != 0)) ) {
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
						setState(363);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(364);
						match(T__74);
						}
						break;
					case 4:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(365);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(366);
						match(T__75);
						}
						break;
					case 5:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(367);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(368);
						match(T__76);
						}
						break;
					case 6:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(369);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(370);
						match(T__77);
						}
						break;
					case 7:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(371);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(372);
						match(T__78);
						}
						break;
					case 8:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(373);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(374);
						match(T__79);
						}
						break;
					case 9:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(375);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(376);
						match(T__80);
						}
						break;
					case 10:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(377);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(378);
						match(T__81);
						}
						break;
					case 11:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(379);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(380);
						match(T__60);
						setState(381);
						expression();
						setState(382);
						match(T__2);
						}
						break;
					case 12:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(384);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(385);
						match(T__59);
						setState(386);
						expression();
						setState(387);
						match(T__2);
						}
						break;
					case 13:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(389);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(390);
						_la = _input.LA(1);
						if ( !(((((_la - 83)) & ~0x3f) == 0 && ((1L << (_la - 83)) & ((1L << (T__82 - 83)) | (1L << (T__83 - 83)) | (1L << (T__84 - 83)) | (1L << (T__85 - 83)) | (1L << (T__86 - 83)) | (1L << (T__87 - 83)) | (1L << (T__88 - 83)) | (1L << (T__89 - 83)) | (1L << (T__90 - 83)) | (1L << (T__91 - 83)) | (1L << (T__92 - 83)) | (1L << (T__93 - 83)) | (1L << (T__94 - 83)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(391);
						match(T__19);
						setState(392);
						expression();
						setState(393);
						match(T__20);
						}
						break;
					}
					} 
				}
				setState(399);
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
		enterRule(_localctx, 46, RULE_setExpression);
		int _la;
		try {
			setState(425);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__95:
				enterOuterAlt(_localctx, 1);
				{
				setState(400);
				match(T__95);
				setState(402);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
					{
					setState(401);
					expressionList();
					}
				}

				setState(404);
				match(T__2);
				}
				break;
			case T__96:
				enterOuterAlt(_localctx, 2);
				{
				setState(405);
				match(T__96);
				setState(407);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
					{
					setState(406);
					expressionList();
					}
				}

				setState(409);
				match(T__2);
				}
				break;
			case T__97:
				enterOuterAlt(_localctx, 3);
				{
				setState(410);
				match(T__97);
				setState(412);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
					{
					setState(411);
					expressionList();
					}
				}

				setState(414);
				match(T__2);
				}
				break;
			case T__98:
				enterOuterAlt(_localctx, 4);
				{
				setState(415);
				match(T__98);
				setState(417);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
					{
					setState(416);
					expressionList();
					}
				}

				setState(419);
				match(T__2);
				}
				break;
			case T__99:
				enterOuterAlt(_localctx, 5);
				{
				setState(420);
				match(T__99);
				setState(422);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__19) | (1L << T__26) | (1L << T__27) | (1L << T__31) | (1L << T__35) | (1L << T__37) | (1L << T__45) | (1L << T__54) | (1L << T__55) | (1L << T__58))) != 0) || ((((_la - 96)) & ~0x3f) == 0 && ((1L << (_la - 96)) & ((1L << (T__95 - 96)) | (1L << (T__96 - 96)) | (1L << (T__97 - 96)) | (1L << (T__98 - 96)) | (1L << (T__99 - 96)) | (1L << (FLOAT_LITERAL - 96)) | (1L << (STRING_LITERAL - 96)) | (1L << (NULL_LITERAL - 96)) | (1L << (INTEGRAL - 96)) | (1L << (SIGMA - 96)) | (1L << (PRODUCT - 96)) | (1L << (INFINITY - 96)) | (1L << (PARTIALDIFF - 96)) | (1L << (FORALL - 96)) | (1L << (EXISTS - 96)) | (1L << (EMPTYSET - 96)) | (1L << (SQUAREROOT - 96)) | (1L << (INT - 96)) | (1L << (ID - 96)))) != 0)) {
					{
					setState(421);
					expressionList();
					}
				}

				setState(424);
				match(T__2);
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
		enterRule(_localctx, 48, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
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
		case 14:
			return basicExpression_sempred((BasicExpressionContext)_localctx, predIndex);
		case 18:
			return logicalExpression_sempred((LogicalExpressionContext)_localctx, predIndex);
		case 21:
			return factorExpression_sempred((FactorExpressionContext)_localctx, predIndex);
		case 22:
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
	private boolean factorExpression_sempred(FactorExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 9:
			return precpred(_ctx, 12);
		case 10:
			return precpred(_ctx, 3);
		case 11:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean factor2Expression_sempred(Factor2ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 12:
			return precpred(_ctx, 15);
		case 13:
			return precpred(_ctx, 14);
		case 14:
			return precpred(_ctx, 13);
		case 15:
			return precpred(_ctx, 12);
		case 16:
			return precpred(_ctx, 11);
		case 17:
			return precpred(_ctx, 10);
		case 18:
			return precpred(_ctx, 9);
		case 19:
			return precpred(_ctx, 8);
		case 20:
			return precpred(_ctx, 7);
		case 21:
			return precpred(_ctx, 6);
		case 22:
			return precpred(_ctx, 5);
		case 23:
			return precpred(_ctx, 4);
		case 24:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3~\u01b0\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\3\2\3\2\3\2\3\2\7\29\n\2\f\2\16\2<\13\2\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3I\n\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\7\fs\n\f\f"+
		"\f\16\fv\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00a0\n\r\3\16\3\16\3\16\7\16\u00a5"+
		"\n\16\f\16\16\16\u00a8\13\16\3\16\3\16\3\17\3\17\3\17\3\17\5\17\u00b0"+
		"\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\5\20\u00c3\n\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20"+
		"\u00cb\n\20\3\20\3\20\3\20\3\20\3\20\3\20\7\20\u00d3\n\20\f\20\16\20\u00d6"+
		"\13\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00fe\n\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\7\24\u0112\n\24\f\24\16\24\u0115\13\24\3\25\3\25"+
		"\3\25\3\25\3\25\5\25\u011c\n\25\3\26\3\26\3\26\3\26\3\26\5\26\u0123\n"+
		"\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0157\n\27\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\7\27\u0160\n\27\f\27\16\27\u0163\13\27\3\30"+
		"\3\30\3\30\5\30\u0168\n\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\7\30"+
		"\u018e\n\30\f\30\16\30\u0191\13\30\3\31\3\31\5\31\u0195\n\31\3\31\3\31"+
		"\3\31\5\31\u019a\n\31\3\31\3\31\3\31\5\31\u019f\n\31\3\31\3\31\3\31\5"+
		"\31\u01a4\n\31\3\31\3\31\3\31\5\31\u01a9\n\31\3\31\5\31\u01ac\n\31\3\32"+
		"\3\32\3\32\2\6\36&,.\33\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*"+
		",.\60\62\2\7\6\2\'\'))\618kl\3\29<\3\2@C\3\2FL\3\2Ua\2\u01e8\2\64\3\2"+
		"\2\2\4H\3\2\2\2\6J\3\2\2\2\bL\3\2\2\2\nR\3\2\2\2\fW\3\2\2\2\16Z\3\2\2"+
		"\2\20a\3\2\2\2\22f\3\2\2\2\24k\3\2\2\2\26t\3\2\2\2\30\u009f\3\2\2\2\32"+
		"\u00a6\3\2\2\2\34\u00af\3\2\2\2\36\u00c2\3\2\2\2 \u00d7\3\2\2\2\"\u00df"+
		"\3\2\2\2$\u00e6\3\2\2\2&\u00fd\3\2\2\2(\u011b\3\2\2\2*\u0122\3\2\2\2,"+
		"\u0156\3\2\2\2.\u0167\3\2\2\2\60\u01ab\3\2\2\2\62\u01ad\3\2\2\2\64\65"+
		"\7\3\2\2\65\66\7}\2\2\66:\7\4\2\2\679\5\4\3\28\67\3\2\2\29<\3\2\2\2:8"+
		"\3\2\2\2:;\3\2\2\2;=\3\2\2\2<:\3\2\2\2=>\7\5\2\2>?\7\2\2\3?\3\3\2\2\2"+
		"@I\5\6\4\2AI\5\b\5\2BI\5\n\6\2CI\5\24\13\2DI\5\f\7\2EI\5\16\b\2FI\5\20"+
		"\t\2GI\5\22\n\2H@\3\2\2\2HA\3\2\2\2HB\3\2\2\2HC\3\2\2\2HD\3\2\2\2HE\3"+
		"\2\2\2HF\3\2\2\2HG\3\2\2\2I\5\3\2\2\2JK\5\34\17\2K\7\3\2\2\2LM\7\6\2\2"+
		"MN\7\7\2\2NO\5\34\17\2OP\7\b\2\2PQ\5&\24\2Q\t\3\2\2\2RS\7\t\2\2ST\5\34"+
		"\17\2TU\7\n\2\2UV\5\34\17\2V\13\3\2\2\2WX\7\13\2\2XY\5\34\17\2Y\r\3\2"+
		"\2\2Z[\7\f\2\2[\\\5\34\17\2\\]\7\r\2\2]^\7}\2\2^_\7\16\2\2_`\5\34\17\2"+
		"`\17\3\2\2\2ab\7\17\2\2bc\5\32\16\2cd\7\r\2\2de\5\26\f\2e\21\3\2\2\2f"+
		"g\7\20\2\2gh\5&\24\2hi\7\21\2\2ij\5\32\16\2j\23\3\2\2\2kl\7\22\2\2lm\5"+
		"\34\17\2mn\7\23\2\2no\7|\2\2o\25\3\2\2\2pq\7}\2\2qs\7\24\2\2rp\3\2\2\2"+
		"sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2uw\3\2\2\2vt\3\2\2\2wx\7}\2\2x\27\3\2\2"+
		"\2yz\7\25\2\2z{\7\26\2\2{|\5\30\r\2|}\7\27\2\2}\u00a0\3\2\2\2~\177\7\30"+
		"\2\2\177\u0080\7\26\2\2\u0080\u0081\5\30\r\2\u0081\u0082\7\27\2\2\u0082"+
		"\u00a0\3\2\2\2\u0083\u0084\7\31\2\2\u0084\u0085\7\26\2\2\u0085\u0086\5"+
		"\30\r\2\u0086\u0087\7\27\2\2\u0087\u00a0\3\2\2\2\u0088\u0089\7\32\2\2"+
		"\u0089\u008a\7\26\2\2\u008a\u008b\5\30\r\2\u008b\u008c\7\27\2\2\u008c"+
		"\u00a0\3\2\2\2\u008d\u008e\7\33\2\2\u008e\u008f\7\26\2\2\u008f\u0090\5"+
		"\30\r\2\u0090\u0091\7\24\2\2\u0091\u0092\5\30\r\2\u0092\u0093\7\27\2\2"+
		"\u0093\u00a0\3\2\2\2\u0094\u0095\7\34\2\2\u0095\u0096\7\26\2\2\u0096\u0097"+
		"\5\30\r\2\u0097\u0098\7\24\2\2\u0098\u0099\5\30\r\2\u0099\u009a\7\27\2"+
		"\2\u009a\u00a0\3\2\2\2\u009b\u00a0\7w\2\2\u009c\u00a0\7x\2\2\u009d\u00a0"+
		"\7y\2\2\u009e\u00a0\7}\2\2\u009fy\3\2\2\2\u009f~\3\2\2\2\u009f\u0083\3"+
		"\2\2\2\u009f\u0088\3\2\2\2\u009f\u008d\3\2\2\2\u009f\u0094\3\2\2\2\u009f"+
		"\u009b\3\2\2\2\u009f\u009c\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u009e\3\2"+
		"\2\2\u00a0\31\3\2\2\2\u00a1\u00a2\5\34\17\2\u00a2\u00a3\7\24\2\2\u00a3"+
		"\u00a5\3\2\2\2\u00a4\u00a1\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4\3\2"+
		"\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9"+
		"\u00aa\5\34\17\2\u00aa\33\3\2\2\2\u00ab\u00b0\5&\24\2\u00ac\u00b0\5 \21"+
		"\2\u00ad\u00b0\5\"\22\2\u00ae\u00b0\5$\23\2\u00af\u00ab\3\2\2\2\u00af"+
		"\u00ac\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00ae\3\2\2\2\u00b0\35\3\2\2"+
		"\2\u00b1\u00b2\b\20\1\2\u00b2\u00c3\7i\2\2\u00b3\u00c3\7\35\2\2\u00b4"+
		"\u00c3\7\36\2\2\u00b5\u00c3\5\62\32\2\u00b6\u00b7\7\"\2\2\u00b7\u00b8"+
		"\7}\2\2\u00b8\u00c3\7\5\2\2\u00b9\u00c3\7|\2\2\u00ba\u00c3\7g\2\2\u00bb"+
		"\u00c3\7h\2\2\u00bc\u00c3\7p\2\2\u00bd\u00c3\7u\2\2\u00be\u00bf\7\26\2"+
		"\2\u00bf\u00c0\5\34\17\2\u00c0\u00c1\7\27\2\2\u00c1\u00c3\3\2\2\2\u00c2"+
		"\u00b1\3\2\2\2\u00c2\u00b3\3\2\2\2\u00c2\u00b4\3\2\2\2\u00c2\u00b5\3\2"+
		"\2\2\u00c2\u00b6\3\2\2\2\u00c2\u00b9\3\2\2\2\u00c2\u00ba\3\2\2\2\u00c2"+
		"\u00bb\3\2\2\2\u00c2\u00bc\3\2\2\2\u00c2\u00bd\3\2\2\2\u00c2\u00be\3\2"+
		"\2\2\u00c3\u00d4\3\2\2\2\u00c4\u00c5\f\r\2\2\u00c5\u00c6\7\37\2\2\u00c6"+
		"\u00d3\7}\2\2\u00c7\u00c8\f\f\2\2\u00c8\u00ca\7\26\2\2\u00c9\u00cb\5\32"+
		"\16\2\u00ca\u00c9\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00d3\7\27\2\2\u00cd\u00ce\f\13\2\2\u00ce\u00cf\7 \2\2\u00cf\u00d0\5"+
		"\34\17\2\u00d0\u00d1\7!\2\2\u00d1\u00d3\3\2\2\2\u00d2\u00c4\3\2\2\2\u00d2"+
		"\u00c7\3\2\2\2\u00d2\u00cd\3\2\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2"+
		"\2\2\u00d4\u00d5\3\2\2\2\u00d5\37\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d8"+
		"\7\21\2\2\u00d8\u00d9\5\34\17\2\u00d9\u00da\7#\2\2\u00da\u00db\5\34\17"+
		"\2\u00db\u00dc\7$\2\2\u00dc\u00dd\5\34\17\2\u00dd\u00de\7%\2\2\u00de!"+
		"\3\2\2\2\u00df\u00e0\7&\2\2\u00e0\u00e1\5\62\32\2\u00e1\u00e2\7\'\2\2"+
		"\u00e2\u00e3\5\30\r\2\u00e3\u00e4\7\16\2\2\u00e4\u00e5\5\34\17\2\u00e5"+
		"#\3\2\2\2\u00e6\u00e7\7(\2\2\u00e7\u00e8\5\62\32\2\u00e8\u00e9\7\'\2\2"+
		"\u00e9\u00ea\5\30\r\2\u00ea\u00eb\7)\2\2\u00eb\u00ec\5\34\17\2\u00ec\u00ed"+
		"\7\16\2\2\u00ed\u00ee\5\34\17\2\u00ee%\3\2\2\2\u00ef\u00f0\b\24\1\2\u00f0"+
		"\u00f1\7s\2\2\u00f1\u00f2\5\62\32\2\u00f2\u00f3\7z\2\2\u00f3\u00f4\5\34"+
		"\17\2\u00f4\u00fe\3\2\2\2\u00f5\u00f6\7t\2\2\u00f6\u00f7\5\62\32\2\u00f7"+
		"\u00f8\7z\2\2\u00f8\u00f9\5\34\17\2\u00f9\u00fe\3\2\2\2\u00fa\u00fb\7"+
		"\60\2\2\u00fb\u00fe\5&\24\4\u00fc\u00fe\5(\25\2\u00fd\u00ef\3\2\2\2\u00fd"+
		"\u00f5\3\2\2\2\u00fd\u00fa\3\2\2\2\u00fd\u00fc\3\2\2\2\u00fe\u0113\3\2"+
		"\2\2\u00ff\u0100\f\f\2\2\u0100\u0101\7*\2\2\u0101\u0112\5&\24\r\u0102"+
		"\u0103\f\13\2\2\u0103\u0104\7+\2\2\u0104\u0112\5&\24\f\u0105\u0106\f\n"+
		"\2\2\u0106\u0107\7,\2\2\u0107\u0112\5&\24\13\u0108\u0109\f\t\2\2\u0109"+
		"\u010a\7-\2\2\u010a\u0112\5&\24\n\u010b\u010c\f\b\2\2\u010c\u010d\7.\2"+
		"\2\u010d\u0112\5&\24\t\u010e\u010f\f\7\2\2\u010f\u0110\7/\2\2\u0110\u0112"+
		"\5&\24\b\u0111\u00ff\3\2\2\2\u0111\u0102\3\2\2\2\u0111\u0105\3\2\2\2\u0111"+
		"\u0108\3\2\2\2\u0111\u010b\3\2\2\2\u0111\u010e\3\2\2\2\u0112\u0115\3\2"+
		"\2\2\u0113\u0111\3\2\2\2\u0113\u0114\3\2\2\2\u0114\'\3\2\2\2\u0115\u0113"+
		"\3\2\2\2\u0116\u0117\5*\26\2\u0117\u0118\t\2\2\2\u0118\u0119\5*\26\2\u0119"+
		"\u011c\3\2\2\2\u011a\u011c\5*\26\2\u011b\u0116\3\2\2\2\u011b\u011a\3\2"+
		"\2\2\u011c)\3\2\2\2\u011d\u011e\5,\27\2\u011e\u011f\t\3\2\2\u011f\u0120"+
		"\5*\26\2\u0120\u0123\3\2\2\2\u0121\u0123\5,\27\2\u0122\u011d\3\2\2\2\u0122"+
		"\u0121\3\2\2\2\u0123+\3\2\2\2\u0124\u0125\b\27\1\2\u0125\u0126\7=\2\2"+
		"\u0126\u0127\7>\2\2\u0127\u0128\5\34\17\2\u0128\u0129\7\5\2\2\u0129\u012a"+
		"\7?\2\2\u012a\u012b\5\34\17\2\u012b\u012c\7\5\2\2\u012c\u0157\3\2\2\2"+
		"\u012d\u012e\7m\2\2\u012e\u012f\7>\2\2\u012f\u0130\5\34\17\2\u0130\u0131"+
		"\7\5\2\2\u0131\u0132\7?\2\2\u0132\u0133\5\34\17\2\u0133\u0134\7\5\2\2"+
		"\u0134\u0135\5\34\17\2\u0135\u0136\7}\2\2\u0136\u0157\3\2\2\2\u0137\u0138"+
		"\7m\2\2\u0138\u0139\5\34\17\2\u0139\u013a\7}\2\2\u013a\u0157\3\2\2\2\u013b"+
		"\u013c\7n\2\2\u013c\u013d\7>\2\2\u013d\u013e\5\34\17\2\u013e\u013f\7\5"+
		"\2\2\u013f\u0140\7?\2\2\u0140\u0141\5\34\17\2\u0141\u0142\7\5\2\2\u0142"+
		"\u0143\5\34\17\2\u0143\u0157\3\2\2\2\u0144\u0145\7o\2\2\u0145\u0146\7"+
		">\2\2\u0146\u0147\5\34\17\2\u0147\u0148\7\5\2\2\u0148\u0149\7?\2\2\u0149"+
		"\u014a\5\34\17\2\u014a\u014b\7\5\2\2\u014b\u014c\5\34\17\2\u014c\u0157"+
		"\3\2\2\2\u014d\u014e\7:\2\2\u014e\u0157\5,\27\t\u014f\u0150\79\2\2\u0150"+
		"\u0157\5,\27\b\u0151\u0152\7v\2\2\u0152\u0157\5,\27\7\u0153\u0154\7r\2"+
		"\2\u0154\u0157\5,\27\6\u0155\u0157\5.\30\2\u0156\u0124\3\2\2\2\u0156\u012d"+
		"\3\2\2\2\u0156\u0137\3\2\2\2\u0156\u013b\3\2\2\2\u0156\u0144\3\2\2\2\u0156"+
		"\u014d\3\2\2\2\u0156\u014f\3\2\2\2\u0156\u0151\3\2\2\2\u0156\u0153\3\2"+
		"\2\2\u0156\u0155\3\2\2\2\u0157\u0161\3\2\2\2\u0158\u0159\f\16\2\2\u0159"+
		"\u015a\t\4\2\2\u015a\u0160\5,\27\17\u015b\u015c\f\5\2\2\u015c\u0160\7"+
		"D\2\2\u015d\u015e\f\4\2\2\u015e\u0160\7q\2\2\u015f\u0158\3\2\2\2\u015f"+
		"\u015b\3\2\2\2\u015f\u015d\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u015f\3\2"+
		"\2\2\u0161\u0162\3\2\2\2\u0162-\3\2\2\2\u0163\u0161\3\2\2\2\u0164\u0165"+
		"\b\30\1\2\u0165\u0168\5\60\31\2\u0166\u0168\5\36\20\2\u0167\u0164\3\2"+
		"\2\2\u0167\u0166\3\2\2\2\u0168\u018f\3\2\2\2\u0169\u016a\f\21\2\2\u016a"+
		"\u018e\7E\2\2\u016b\u016c\f\20\2\2\u016c\u018e\t\5\2\2\u016d\u016e\f\17"+
		"\2\2\u016e\u018e\7M\2\2\u016f\u0170\f\16\2\2\u0170\u018e\7N\2\2\u0171"+
		"\u0172\f\r\2\2\u0172\u018e\7O\2\2\u0173\u0174\f\f\2\2\u0174\u018e\7P\2"+
		"\2\u0175\u0176\f\13\2\2\u0176\u018e\7Q\2\2\u0177\u0178\f\n\2\2\u0178\u018e"+
		"\7R\2\2\u0179\u017a\f\t\2\2\u017a\u018e\7S\2\2\u017b\u017c\f\b\2\2\u017c"+
		"\u018e\7T\2\2\u017d\u017e\f\7\2\2\u017e\u017f\7?\2\2\u017f\u0180\5\34"+
		"\17\2\u0180\u0181\7\5\2\2\u0181\u018e\3\2\2\2\u0182\u0183\f\6\2\2\u0183"+
		"\u0184\7>\2\2\u0184\u0185\5\34\17\2\u0185\u0186\7\5\2\2\u0186\u018e\3"+
		"\2\2\2\u0187\u0188\f\5\2\2\u0188\u0189\t\6\2\2\u0189\u018a\7\26\2\2\u018a"+
		"\u018b\5\34\17\2\u018b\u018c\7\27\2\2\u018c\u018e\3\2\2\2\u018d\u0169"+
		"\3\2\2\2\u018d\u016b\3\2\2\2\u018d\u016d\3\2\2\2\u018d\u016f\3\2\2\2\u018d"+
		"\u0171\3\2\2\2\u018d\u0173\3\2\2\2\u018d\u0175\3\2\2\2\u018d\u0177\3\2"+
		"\2\2\u018d\u0179\3\2\2\2\u018d\u017b\3\2\2\2\u018d\u017d\3\2\2\2\u018d"+
		"\u0182\3\2\2\2\u018d\u0187\3\2\2\2\u018e\u0191\3\2\2\2\u018f\u018d\3\2"+
		"\2\2\u018f\u0190\3\2\2\2\u0190/\3\2\2\2\u0191\u018f\3\2\2\2\u0192\u0194"+
		"\7b\2\2\u0193\u0195\5\32\16\2\u0194\u0193\3\2\2\2\u0194\u0195\3\2\2\2"+
		"\u0195\u0196\3\2\2\2\u0196\u01ac\7\5\2\2\u0197\u0199\7c\2\2\u0198\u019a"+
		"\5\32\16\2\u0199\u0198\3\2\2\2\u0199\u019a\3\2\2\2\u019a\u019b\3\2\2\2"+
		"\u019b\u01ac\7\5\2\2\u019c\u019e\7d\2\2\u019d\u019f\5\32\16\2\u019e\u019d"+
		"\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u01ac\7\5\2\2\u01a1"+
		"\u01a3\7e\2\2\u01a2\u01a4\5\32\16\2\u01a3\u01a2\3\2\2\2\u01a3\u01a4\3"+
		"\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01ac\7\5\2\2\u01a6\u01a8\7f\2\2\u01a7"+
		"\u01a9\5\32\16\2\u01a8\u01a7\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01aa\3"+
		"\2\2\2\u01aa\u01ac\7\5\2\2\u01ab\u0192\3\2\2\2\u01ab\u0197\3\2\2\2\u01ab"+
		"\u019c\3\2\2\2\u01ab\u01a1\3\2\2\2\u01ab\u01a6\3\2\2\2\u01ac\61\3\2\2"+
		"\2\u01ad\u01ae\7}\2\2\u01ae\63\3\2\2\2\35:Ht\u009f\u00a6\u00af\u00c2\u00ca"+
		"\u00d2\u00d4\u00fd\u0111\u0113\u011b\u0122\u0156\u015f\u0161\u0167\u018d"+
		"\u018f\u0194\u0199\u019e\u01a3\u01a8\u01ab";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}