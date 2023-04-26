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
		T__101=102, FLOAT_LITERAL=103, STRING_LITERAL=104, NULL_LITERAL=105, MULTILINE_COMMENT=106, 
		IN=107, NOTIN=108, INTEGRAL=109, SIGMA=110, PRODUCT=111, INFINITY=112, 
		DIFFERENTIAL=113, PARTIALDIFF=114, FORALL=115, EXISTS=116, EMPTYSET=117, 
		SQUAREROOT=118, NATURAL=119, INTEGER=120, REAL=121, CDOT=122, NEWLINE=123, 
		INT=124, ID=125, WS=126;
	public static final int
		RULE_specification = 0, RULE_part = 1, RULE_formula = 2, RULE_constraint = 3, 
		RULE_reexpression = 4, RULE_simplify = 5, RULE_substituting = 6, RULE_solve = 7, 
		RULE_prove = 8, RULE_expanding = 9, RULE_substituteIn = 10, RULE_expandTo = 11, 
		RULE_factorBy = 12, RULE_cancelIn = 13, RULE_idList = 14, RULE_type = 15, 
		RULE_expressionList = 16, RULE_expression = 17, RULE_basicExpression = 18, 
		RULE_conditionalExpression = 19, RULE_lambdaExpression = 20, RULE_letExpression = 21, 
		RULE_logicalExpression = 22, RULE_equalityExpression = 23, RULE_additiveExpression = 24, 
		RULE_factorExpression = 25, RULE_factor2Expression = 26, RULE_setExpression = 27, 
		RULE_identifier = 28;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "part", "formula", "constraint", "reexpression", "simplify", 
			"substituting", "solve", "prove", "expanding", "substituteIn", "expandTo", 
			"factorBy", "cancelIn", "idList", "type", "expressionList", "expression", 
			"basicExpression", "conditionalExpression", "lambdaExpression", "letExpression", 
			"logicalExpression", "equalityExpression", "additiveExpression", "factorExpression", 
			"factor2Expression", "setExpression", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'specification'", "'Define'", "'='", "'~'", "'Constraint'", "'on'", 
			"'|'", "'Express'", "'as'", "'Simplify'", "'Substitute'", "'for'", "'in'", 
			"'Solve'", "'Prove'", "'if'", "'Expanding'", "'to'", "'terms'", "'Expand'", 
			"'Factor'", "'by'", "'Cancel'", "','", "'Sequence'", "'('", "')'", "'Set'", 
			"'Bag'", "'OrderedSet'", "'Map'", "'Function'", "'true'", "'false'", 
			"'.'", "'g{'", "'}'", "'then'", "'else'", "'endif'", "'lambda'", "':'", 
			"'let'", "'=>'", "'implies'", "'or'", "'xor'", "'&'", "'and'", "'not'", 
			"'<'", "'>'", "'>='", "'<='", "'/='", "'<>'", "'/:'", "'<:'", "'+'", 
			"'-'", "'..'", "'|->'", "'C_{'", "'^{'", "'E['", "']'", "'*'", "'/'", 
			"'mod'", "'div'", "'_{'", "'!'", "'->size()'", "'->isEmpty()'", "'->notEmpty()'", 
			"'->asSet()'", "'->asBag()'", "'->asOrderedSet()'", "'->asSequence()'", 
			"'->sort()'", "'->any()'", "'->first()'", "'->last()'", "'->front()'", 
			"'->tail()'", "'->reverse()'", "'->max()'", "'->min()'", "'->at'", "'->union'", 
			"'->intersection'", "'->includes'", "'->excludes'", "'->including'", 
			"'->excluding'", "'->includesAll'", "'->excludesAll'", "'->prepend'", 
			"'->append'", "'->count'", "'->apply'", "'{'", null, null, "'null'", 
			null, "'\u00A9'", "'\u00A2'", "'\u2021'", "'\u20AC'", "'\u00D7'", "'\u2026'", 
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
			null, null, null, null, null, null, null, "FLOAT_LITERAL", "STRING_LITERAL", 
			"NULL_LITERAL", "MULTILINE_COMMENT", "IN", "NOTIN", "INTEGRAL", "SIGMA", 
			"PRODUCT", "INFINITY", "DIFFERENTIAL", "PARTIALDIFF", "FORALL", "EXISTS", 
			"EMPTYSET", "SQUAREROOT", "NATURAL", "INTEGER", "REAL", "CDOT", "NEWLINE", 
			"INT", "ID", "WS"
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
			setState(58);
			match(T__0);
			setState(59);
			match(ID);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__4) | (1L << T__7) | (1L << T__9) | (1L << T__10) | (1L << T__13) | (1L << T__14) | (1L << T__16))) != 0)) {
				{
				{
				setState(60);
				part();
				}
				}
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(66);
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
			setState(76);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(68);
				formula();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(69);
				constraint();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(70);
				reexpression();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 4);
				{
				setState(71);
				expanding();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(72);
				simplify();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(73);
				substituting();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 7);
				{
				setState(74);
				solve();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 8);
				{
				setState(75);
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
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public SubstituteInContext substituteIn() {
			return getRuleContext(SubstituteInContext.class,0);
		}
		public ExpandToContext expandTo() {
			return getRuleContext(ExpandToContext.class,0);
		}
		public CancelInContext cancelIn() {
			return getRuleContext(CancelInContext.class,0);
		}
		public FactorByContext factorBy() {
			return getRuleContext(FactorByContext.class,0);
		}
		public SimplifyContext simplify() {
			return getRuleContext(SimplifyContext.class,0);
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
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(78);
				match(T__1);
				setState(79);
				match(ID);
				setState(80);
				match(T__2);
				setState(87);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__15:
				case T__25:
				case T__32:
				case T__33:
				case T__35:
				case T__40:
				case T__42:
				case T__49:
				case T__58:
				case T__59:
				case T__62:
				case T__64:
				case T__101:
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
					setState(81);
					expression();
					}
					break;
				case T__10:
					{
					setState(82);
					substituteIn();
					}
					break;
				case T__19:
					{
					setState(83);
					expandTo();
					}
					break;
				case T__22:
					{
					setState(84);
					cancelIn();
					}
					break;
				case T__20:
					{
					setState(85);
					factorBy();
					}
					break;
				case T__9:
					{
					setState(86);
					simplify();
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
				setState(89);
				match(T__1);
				setState(90);
				match(ID);
				setState(91);
				match(T__3);
				setState(92);
				expression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(93);
				match(T__1);
				setState(94);
				match(ID);
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
			setState(97);
			match(T__4);
			setState(98);
			match(T__5);
			setState(99);
			expression();
			setState(100);
			match(T__6);
			setState(101);
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
			setState(103);
			match(T__7);
			setState(104);
			expression();
			setState(105);
			match(T__8);
			setState(106);
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
			setState(108);
			match(T__9);
			setState(109);
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
			setState(111);
			match(T__10);
			setState(112);
			expression();
			setState(113);
			match(T__11);
			setState(114);
			match(ID);
			setState(115);
			match(T__12);
			setState(116);
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
			setState(118);
			match(T__13);
			setState(119);
			expressionList();
			setState(120);
			match(T__11);
			setState(121);
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
			setState(123);
			match(T__14);
			setState(124);
			logicalExpression(0);
			setState(125);
			match(T__15);
			setState(126);
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
			setState(128);
			match(T__16);
			setState(129);
			expression();
			setState(130);
			match(T__17);
			setState(131);
			match(INT);
			setState(132);
			match(T__18);
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
		public TerminalNode ID() { return getToken(MathOCLParser.ID, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpandToContext expandTo() {
			return getRuleContext(ExpandToContext.class,0);
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
		enterRule(_localctx, 20, RULE_substituteIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(T__10);
			setState(135);
			match(ID);
			setState(136);
			match(T__12);
			setState(139);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
			case T__25:
			case T__32:
			case T__33:
			case T__35:
			case T__40:
			case T__42:
			case T__49:
			case T__58:
			case T__59:
			case T__62:
			case T__64:
			case T__101:
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
				setState(137);
				expression();
				}
				break;
			case T__19:
				{
				setState(138);
				expandTo();
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
		enterRule(_localctx, 22, RULE_expandTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__19);
			setState(142);
			expression();
			setState(143);
			match(T__17);
			setState(144);
			match(INT);
			setState(145);
			match(T__18);
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
		enterRule(_localctx, 24, RULE_factorBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(T__20);
			setState(148);
			expression();
			setState(149);
			match(T__21);
			setState(150);
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
		enterRule(_localctx, 26, RULE_cancelIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__22);
			setState(153);
			expression();
			setState(154);
			match(T__12);
			setState(155);
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
		enterRule(_localctx, 28, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(157);
					match(ID);
					setState(158);
					match(T__23);
					}
					} 
				}
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(164);
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
		enterRule(_localctx, 30, RULE_type);
		try {
			setState(204);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				match(T__24);
				setState(167);
				match(T__25);
				setState(168);
				type();
				setState(169);
				match(T__26);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 2);
				{
				setState(171);
				match(T__27);
				setState(172);
				match(T__25);
				setState(173);
				type();
				setState(174);
				match(T__26);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 3);
				{
				setState(176);
				match(T__28);
				setState(177);
				match(T__25);
				setState(178);
				type();
				setState(179);
				match(T__26);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 4);
				{
				setState(181);
				match(T__29);
				setState(182);
				match(T__25);
				setState(183);
				type();
				setState(184);
				match(T__26);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 5);
				{
				setState(186);
				match(T__30);
				setState(187);
				match(T__25);
				setState(188);
				type();
				setState(189);
				match(T__23);
				setState(190);
				type();
				setState(191);
				match(T__26);
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 6);
				{
				setState(193);
				match(T__31);
				setState(194);
				match(T__25);
				setState(195);
				type();
				setState(196);
				match(T__23);
				setState(197);
				type();
				setState(198);
				match(T__26);
				}
				break;
			case NATURAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(200);
				match(NATURAL);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 8);
				{
				setState(201);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(202);
				match(REAL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(203);
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
		enterRule(_localctx, 32, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(211);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(206);
					expression();
					setState(207);
					match(T__23);
					}
					} 
				}
				setState(213);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			setState(214);
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
		enterRule(_localctx, 34, RULE_expression);
		try {
			setState(220);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__25:
			case T__32:
			case T__33:
			case T__35:
			case T__49:
			case T__58:
			case T__59:
			case T__62:
			case T__64:
			case T__101:
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
				setState(216);
				logicalExpression(0);
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(217);
				conditionalExpression();
				}
				break;
			case T__40:
				enterOuterAlt(_localctx, 3);
				{
				setState(218);
				lambdaExpression();
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 4);
				{
				setState(219);
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
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_basicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL_LITERAL:
				{
				setState(223);
				match(NULL_LITERAL);
				}
				break;
			case T__32:
				{
				setState(224);
				match(T__32);
				}
				break;
			case T__33:
				{
				setState(225);
				match(T__33);
				}
				break;
			case ID:
				{
				setState(226);
				identifier();
				}
				break;
			case T__35:
				{
				setState(227);
				match(T__35);
				setState(228);
				match(ID);
				setState(229);
				match(T__36);
				}
				break;
			case INT:
				{
				setState(230);
				match(INT);
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(231);
				match(FLOAT_LITERAL);
				}
				break;
			case STRING_LITERAL:
				{
				setState(232);
				match(STRING_LITERAL);
				}
				break;
			case INFINITY:
				{
				setState(233);
				match(INFINITY);
				}
				break;
			case EMPTYSET:
				{
				setState(234);
				match(EMPTYSET);
				}
				break;
			case T__25:
				{
				setState(235);
				match(T__25);
				setState(236);
				expression();
				setState(237);
				match(T__26);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(252);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(250);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(241);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(242);
						match(T__34);
						setState(243);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(244);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(245);
						match(T__25);
						setState(247);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__25) | (1L << T__32) | (1L << T__33) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__101 - 65)) | (1L << (FLOAT_LITERAL - 65)) | (1L << (STRING_LITERAL - 65)) | (1L << (NULL_LITERAL - 65)) | (1L << (INTEGRAL - 65)) | (1L << (SIGMA - 65)) | (1L << (PRODUCT - 65)) | (1L << (INFINITY - 65)) | (1L << (PARTIALDIFF - 65)) | (1L << (FORALL - 65)) | (1L << (EXISTS - 65)) | (1L << (EMPTYSET - 65)) | (1L << (SQUAREROOT - 65)) | (1L << (INT - 65)) | (1L << (ID - 65)))) != 0)) {
							{
							setState(246);
							expressionList();
							}
						}

						setState(249);
						match(T__26);
						}
						break;
					}
					} 
				}
				setState(254);
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
		enterRule(_localctx, 38, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(T__15);
			setState(256);
			expression();
			setState(257);
			match(T__37);
			setState(258);
			expression();
			setState(259);
			match(T__38);
			setState(260);
			expression();
			setState(261);
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
		enterRule(_localctx, 40, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(T__40);
			setState(264);
			identifier();
			setState(265);
			match(T__41);
			setState(266);
			type();
			setState(267);
			match(T__12);
			setState(268);
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
		enterRule(_localctx, 42, RULE_letExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			match(T__42);
			setState(271);
			identifier();
			setState(272);
			match(T__2);
			setState(273);
			expression();
			setState(274);
			match(T__12);
			setState(275);
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
		int _startState = 44;
		enterRecursionRule(_localctx, 44, RULE_logicalExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				{
				setState(278);
				match(FORALL);
				setState(279);
				identifier();
				setState(280);
				match(T__41);
				setState(281);
				type();
				setState(282);
				match(CDOT);
				setState(283);
				logicalExpression(4);
				}
				break;
			case EXISTS:
				{
				setState(285);
				match(EXISTS);
				setState(286);
				identifier();
				setState(287);
				match(T__41);
				setState(288);
				type();
				setState(289);
				match(CDOT);
				setState(290);
				logicalExpression(3);
				}
				break;
			case T__49:
				{
				setState(292);
				match(T__49);
				setState(293);
				logicalExpression(2);
				}
				break;
			case T__25:
			case T__32:
			case T__33:
			case T__35:
			case T__58:
			case T__59:
			case T__62:
			case T__64:
			case T__101:
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
				setState(294);
				equalityExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(317);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(315);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
					case 1:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(297);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(298);
						match(T__43);
						setState(299);
						logicalExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(300);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(301);
						match(T__44);
						setState(302);
						logicalExpression(10);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(303);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(304);
						match(T__45);
						setState(305);
						logicalExpression(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(306);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(307);
						match(T__46);
						setState(308);
						logicalExpression(8);
						}
						break;
					case 5:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(309);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(310);
						match(T__47);
						setState(311);
						logicalExpression(7);
						}
						break;
					case 6:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(312);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(313);
						match(T__48);
						setState(314);
						logicalExpression(6);
						}
						break;
					}
					} 
				}
				setState(319);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
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
		enterRule(_localctx, 46, RULE_equalityExpression);
		int _la;
		try {
			setState(325);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(320);
				additiveExpression(0);
				setState(321);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__41) | (1L << T__50) | (1L << T__51) | (1L << T__52) | (1L << T__53) | (1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__57))) != 0) || _la==IN || _la==NOTIN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(322);
				additiveExpression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(324);
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
		int _startState = 48;
		enterRecursionRule(_localctx, 48, RULE_additiveExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(328);
				factorExpression(0);
				setState(329);
				_la = _input.LA(1);
				if ( !(_la==T__60 || _la==T__61) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(330);
				factorExpression(0);
				}
				break;
			case 2:
				{
				setState(332);
				factorExpression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(343);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(341);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(335);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(336);
						match(T__58);
						setState(337);
						additiveExpression(5);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(338);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(339);
						match(T__59);
						setState(340);
						factorExpression(0);
						}
						break;
					}
					} 
				}
				setState(345);
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
		public List<FactorExpressionContext> factorExpression() {
			return getRuleContexts(FactorExpressionContext.class);
		}
		public FactorExpressionContext factorExpression(int i) {
			return getRuleContext(FactorExpressionContext.class,i);
		}
		public TerminalNode PRODUCT() { return getToken(MathOCLParser.PRODUCT, 0); }
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
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_factorExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(347);
				match(T__62);
				setState(348);
				expression();
				setState(349);
				match(T__36);
				setState(350);
				match(T__63);
				setState(351);
				expression();
				setState(352);
				match(T__36);
				}
				break;
			case 2:
				{
				setState(354);
				match(T__64);
				setState(355);
				expression();
				setState(356);
				match(T__65);
				}
				break;
			case 3:
				{
				setState(358);
				match(INTEGRAL);
				setState(359);
				match(T__70);
				setState(360);
				expression();
				setState(361);
				match(T__36);
				setState(362);
				match(T__63);
				setState(363);
				expression();
				setState(364);
				match(T__36);
				setState(365);
				expression();
				setState(366);
				match(ID);
				}
				break;
			case 4:
				{
				setState(368);
				match(INTEGRAL);
				setState(369);
				expression();
				setState(370);
				match(ID);
				}
				break;
			case 5:
				{
				setState(372);
				match(SIGMA);
				setState(373);
				match(T__70);
				setState(374);
				expression();
				setState(375);
				match(T__36);
				setState(376);
				match(T__63);
				setState(377);
				expression();
				setState(378);
				match(T__36);
				setState(379);
				factorExpression(9);
				}
				break;
			case 6:
				{
				setState(381);
				match(PRODUCT);
				setState(382);
				match(T__70);
				setState(383);
				expression();
				setState(384);
				match(T__36);
				setState(385);
				match(T__63);
				setState(386);
				expression();
				setState(387);
				match(T__36);
				setState(388);
				factorExpression(8);
				}
				break;
			case 7:
				{
				setState(390);
				match(T__59);
				setState(391);
				factorExpression(7);
				}
				break;
			case 8:
				{
				setState(392);
				match(T__58);
				setState(393);
				factorExpression(6);
				}
				break;
			case 9:
				{
				setState(394);
				match(SQUAREROOT);
				setState(395);
				factorExpression(5);
				}
				break;
			case 10:
				{
				setState(396);
				match(PARTIALDIFF);
				setState(397);
				match(T__70);
				setState(398);
				match(ID);
				setState(399);
				match(T__36);
				setState(400);
				factorExpression(4);
				}
				break;
			case 11:
				{
				setState(401);
				factor2Expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(413);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(411);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(404);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(405);
						_la = _input.LA(1);
						if ( !(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (T__66 - 67)) | (1L << (T__67 - 67)) | (1L << (T__68 - 67)) | (1L << (T__69 - 67)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(406);
						factorExpression(13);
						}
						break;
					case 2:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(407);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(408);
						match(T__71);
						}
						break;
					case 3:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(409);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(410);
						match(DIFFERENTIAL);
						}
						break;
					}
					} 
				}
				setState(415);
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
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_factor2Expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__101:
				{
				setState(417);
				setExpression();
				}
				break;
			case T__25:
			case T__32:
			case T__33:
			case T__35:
			case FLOAT_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case INFINITY:
			case EMPTYSET:
			case INT:
			case ID:
				{
				setState(418);
				basicExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(459);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(457);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
					case 1:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(421);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(422);
						match(T__72);
						}
						break;
					case 2:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(423);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(424);
						_la = _input.LA(1);
						if ( !(((((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & ((1L << (T__73 - 74)) | (1L << (T__74 - 74)) | (1L << (T__75 - 74)) | (1L << (T__76 - 74)) | (1L << (T__77 - 74)) | (1L << (T__78 - 74)) | (1L << (T__79 - 74)))) != 0)) ) {
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
						setState(425);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(426);
						match(T__80);
						}
						break;
					case 4:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(427);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(428);
						match(T__81);
						}
						break;
					case 5:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(429);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(430);
						match(T__82);
						}
						break;
					case 6:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(431);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(432);
						match(T__83);
						}
						break;
					case 7:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(433);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(434);
						match(T__84);
						}
						break;
					case 8:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(435);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(436);
						match(T__85);
						}
						break;
					case 9:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(437);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(438);
						match(T__86);
						}
						break;
					case 10:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(439);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(440);
						match(T__87);
						}
						break;
					case 11:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(441);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(442);
						match(T__63);
						setState(443);
						expression();
						setState(444);
						match(T__36);
						}
						break;
					case 12:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(446);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(447);
						match(T__70);
						setState(448);
						expression();
						setState(449);
						match(T__36);
						}
						break;
					case 13:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(451);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(452);
						_la = _input.LA(1);
						if ( !(((((_la - 89)) & ~0x3f) == 0 && ((1L << (_la - 89)) & ((1L << (T__88 - 89)) | (1L << (T__89 - 89)) | (1L << (T__90 - 89)) | (1L << (T__91 - 89)) | (1L << (T__92 - 89)) | (1L << (T__93 - 89)) | (1L << (T__94 - 89)) | (1L << (T__95 - 89)) | (1L << (T__96 - 89)) | (1L << (T__97 - 89)) | (1L << (T__98 - 89)) | (1L << (T__99 - 89)) | (1L << (T__100 - 89)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(453);
						match(T__25);
						setState(454);
						expression();
						setState(455);
						match(T__26);
						}
						break;
					}
					} 
				}
				setState(461);
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
		enterRule(_localctx, 54, RULE_setExpression);
		try {
			setState(480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(462);
				match(T__101);
				setState(463);
				match(ID);
				setState(464);
				match(T__41);
				setState(465);
				type();
				setState(466);
				match(T__6);
				setState(467);
				expression();
				setState(468);
				match(T__36);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(470);
				match(T__101);
				setState(471);
				match(ID);
				setState(472);
				match(T__41);
				setState(473);
				type();
				setState(474);
				match(T__6);
				setState(475);
				expression();
				setState(476);
				match(CDOT);
				setState(477);
				expression();
				setState(478);
				match(T__36);
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
		enterRule(_localctx, 56, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(482);
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
		case 18:
			return basicExpression_sempred((BasicExpressionContext)_localctx, predIndex);
		case 22:
			return logicalExpression_sempred((LogicalExpressionContext)_localctx, predIndex);
		case 24:
			return additiveExpression_sempred((AdditiveExpressionContext)_localctx, predIndex);
		case 25:
			return factorExpression_sempred((FactorExpressionContext)_localctx, predIndex);
		case 26:
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
			return precpred(_ctx, 12);
		case 11:
			return precpred(_ctx, 3);
		case 12:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean factor2Expression_sempred(Factor2ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 13:
			return precpred(_ctx, 15);
		case 14:
			return precpred(_ctx, 14);
		case 15:
			return precpred(_ctx, 13);
		case 16:
			return precpred(_ctx, 12);
		case 17:
			return precpred(_ctx, 11);
		case 18:
			return precpred(_ctx, 10);
		case 19:
			return precpred(_ctx, 9);
		case 20:
			return precpred(_ctx, 8);
		case 21:
			return precpred(_ctx, 7);
		case 22:
			return precpred(_ctx, 6);
		case 23:
			return precpred(_ctx, 5);
		case 24:
			return precpred(_ctx, 4);
		case 25:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0080\u01e7\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\7\2@\n"+
		"\2\f\2\16\2C\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3O\n\3\3\4"+
		"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4Z\n\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4"+
		"b\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\5\f\u008e\n\f\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\7"+
		"\20\u00a2\n\20\f\20\16\20\u00a5\13\20\3\20\3\20\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\5\21\u00cf\n\21\3\22\3\22\3\22\7\22\u00d4"+
		"\n\22\f\22\16\22\u00d7\13\22\3\22\3\22\3\23\3\23\3\23\3\23\5\23\u00df"+
		"\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\5\24\u00f2\n\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24"+
		"\u00fa\n\24\3\24\7\24\u00fd\n\24\f\24\16\24\u0100\13\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u012a\n\30\3\30\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\7\30\u013e\n\30\f\30\16\30\u0141\13\30\3\31\3\31\3\31\3\31"+
		"\3\31\5\31\u0148\n\31\3\32\3\32\3\32\3\32\3\32\3\32\5\32\u0150\n\32\3"+
		"\32\3\32\3\32\3\32\3\32\3\32\7\32\u0158\n\32\f\32\16\32\u015b\13\32\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5"+
		"\33\u0195\n\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u019e\n\33\f\33"+
		"\16\33\u01a1\13\33\3\34\3\34\3\34\5\34\u01a6\n\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\7\34\u01cc\n\34\f\34\16\34\u01cf\13\34\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\5\35\u01e3\n\35\3\36\3\36\3\36\2\7&.\62\64\66\37\2\4\6\b\n"+
		"\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:\2\7\6\2\5\6,,\65"+
		"<mn\3\2?@\3\2EH\3\2LR\3\2[g\2\u021d\2<\3\2\2\2\4N\3\2\2\2\6a\3\2\2\2\b"+
		"c\3\2\2\2\ni\3\2\2\2\fn\3\2\2\2\16q\3\2\2\2\20x\3\2\2\2\22}\3\2\2\2\24"+
		"\u0082\3\2\2\2\26\u0088\3\2\2\2\30\u008f\3\2\2\2\32\u0095\3\2\2\2\34\u009a"+
		"\3\2\2\2\36\u00a3\3\2\2\2 \u00ce\3\2\2\2\"\u00d5\3\2\2\2$\u00de\3\2\2"+
		"\2&\u00f1\3\2\2\2(\u0101\3\2\2\2*\u0109\3\2\2\2,\u0110\3\2\2\2.\u0129"+
		"\3\2\2\2\60\u0147\3\2\2\2\62\u014f\3\2\2\2\64\u0194\3\2\2\2\66\u01a5\3"+
		"\2\2\28\u01e2\3\2\2\2:\u01e4\3\2\2\2<=\7\3\2\2=A\7\177\2\2>@\5\4\3\2?"+
		">\3\2\2\2@C\3\2\2\2A?\3\2\2\2AB\3\2\2\2BD\3\2\2\2CA\3\2\2\2DE\7\2\2\3"+
		"E\3\3\2\2\2FO\5\6\4\2GO\5\b\5\2HO\5\n\6\2IO\5\24\13\2JO\5\f\7\2KO\5\16"+
		"\b\2LO\5\20\t\2MO\5\22\n\2NF\3\2\2\2NG\3\2\2\2NH\3\2\2\2NI\3\2\2\2NJ\3"+
		"\2\2\2NK\3\2\2\2NL\3\2\2\2NM\3\2\2\2O\5\3\2\2\2PQ\7\4\2\2QR\7\177\2\2"+
		"RY\7\5\2\2SZ\5$\23\2TZ\5\26\f\2UZ\5\30\r\2VZ\5\34\17\2WZ\5\32\16\2XZ\5"+
		"\f\7\2YS\3\2\2\2YT\3\2\2\2YU\3\2\2\2YV\3\2\2\2YW\3\2\2\2YX\3\2\2\2Zb\3"+
		"\2\2\2[\\\7\4\2\2\\]\7\177\2\2]^\7\6\2\2^b\5$\23\2_`\7\4\2\2`b\7\177\2"+
		"\2aP\3\2\2\2a[\3\2\2\2a_\3\2\2\2b\7\3\2\2\2cd\7\7\2\2de\7\b\2\2ef\5$\23"+
		"\2fg\7\t\2\2gh\5.\30\2h\t\3\2\2\2ij\7\n\2\2jk\5$\23\2kl\7\13\2\2lm\5$"+
		"\23\2m\13\3\2\2\2no\7\f\2\2op\5$\23\2p\r\3\2\2\2qr\7\r\2\2rs\5$\23\2s"+
		"t\7\16\2\2tu\7\177\2\2uv\7\17\2\2vw\5$\23\2w\17\3\2\2\2xy\7\20\2\2yz\5"+
		"\"\22\2z{\7\16\2\2{|\5\36\20\2|\21\3\2\2\2}~\7\21\2\2~\177\5.\30\2\177"+
		"\u0080\7\22\2\2\u0080\u0081\5\"\22\2\u0081\23\3\2\2\2\u0082\u0083\7\23"+
		"\2\2\u0083\u0084\5$\23\2\u0084\u0085\7\24\2\2\u0085\u0086\7~\2\2\u0086"+
		"\u0087\7\25\2\2\u0087\25\3\2\2\2\u0088\u0089\7\r\2\2\u0089\u008a\7\177"+
		"\2\2\u008a\u008d\7\17\2\2\u008b\u008e\5$\23\2\u008c\u008e\5\30\r\2\u008d"+
		"\u008b\3\2\2\2\u008d\u008c\3\2\2\2\u008e\27\3\2\2\2\u008f\u0090\7\26\2"+
		"\2\u0090\u0091\5$\23\2\u0091\u0092\7\24\2\2\u0092\u0093\7~\2\2\u0093\u0094"+
		"\7\25\2\2\u0094\31\3\2\2\2\u0095\u0096\7\27\2\2\u0096\u0097\5$\23\2\u0097"+
		"\u0098\7\30\2\2\u0098\u0099\5$\23\2\u0099\33\3\2\2\2\u009a\u009b\7\31"+
		"\2\2\u009b\u009c\5$\23\2\u009c\u009d\7\17\2\2\u009d\u009e\5$\23\2\u009e"+
		"\35\3\2\2\2\u009f\u00a0\7\177\2\2\u00a0\u00a2\7\32\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\u00a6\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6\u00a7\7\177\2\2\u00a7\37\3\2"+
		"\2\2\u00a8\u00a9\7\33\2\2\u00a9\u00aa\7\34\2\2\u00aa\u00ab\5 \21\2\u00ab"+
		"\u00ac\7\35\2\2\u00ac\u00cf\3\2\2\2\u00ad\u00ae\7\36\2\2\u00ae\u00af\7"+
		"\34\2\2\u00af\u00b0\5 \21\2\u00b0\u00b1\7\35\2\2\u00b1\u00cf\3\2\2\2\u00b2"+
		"\u00b3\7\37\2\2\u00b3\u00b4\7\34\2\2\u00b4\u00b5\5 \21\2\u00b5\u00b6\7"+
		"\35\2\2\u00b6\u00cf\3\2\2\2\u00b7\u00b8\7 \2\2\u00b8\u00b9\7\34\2\2\u00b9"+
		"\u00ba\5 \21\2\u00ba\u00bb\7\35\2\2\u00bb\u00cf\3\2\2\2\u00bc\u00bd\7"+
		"!\2\2\u00bd\u00be\7\34\2\2\u00be\u00bf\5 \21\2\u00bf\u00c0\7\32\2\2\u00c0"+
		"\u00c1\5 \21\2\u00c1\u00c2\7\35\2\2\u00c2\u00cf\3\2\2\2\u00c3\u00c4\7"+
		"\"\2\2\u00c4\u00c5\7\34\2\2\u00c5\u00c6\5 \21\2\u00c6\u00c7\7\32\2\2\u00c7"+
		"\u00c8\5 \21\2\u00c8\u00c9\7\35\2\2\u00c9\u00cf\3\2\2\2\u00ca\u00cf\7"+
		"y\2\2\u00cb\u00cf\7z\2\2\u00cc\u00cf\7{\2\2\u00cd\u00cf\7\177\2\2\u00ce"+
		"\u00a8\3\2\2\2\u00ce\u00ad\3\2\2\2\u00ce\u00b2\3\2\2\2\u00ce\u00b7\3\2"+
		"\2\2\u00ce\u00bc\3\2\2\2\u00ce\u00c3\3\2\2\2\u00ce\u00ca\3\2\2\2\u00ce"+
		"\u00cb\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cd\3\2\2\2\u00cf!\3\2\2\2"+
		"\u00d0\u00d1\5$\23\2\u00d1\u00d2\7\32\2\2\u00d2\u00d4\3\2\2\2\u00d3\u00d0"+
		"\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6"+
		"\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8\u00d9\5$\23\2\u00d9#\3\2\2\2"+
		"\u00da\u00df\5.\30\2\u00db\u00df\5(\25\2\u00dc\u00df\5*\26\2\u00dd\u00df"+
		"\5,\27\2\u00de\u00da\3\2\2\2\u00de\u00db\3\2\2\2\u00de\u00dc\3\2\2\2\u00de"+
		"\u00dd\3\2\2\2\u00df%\3\2\2\2\u00e0\u00e1\b\24\1\2\u00e1\u00f2\7k\2\2"+
		"\u00e2\u00f2\7#\2\2\u00e3\u00f2\7$\2\2\u00e4\u00f2\5:\36\2\u00e5\u00e6"+
		"\7&\2\2\u00e6\u00e7\7\177\2\2\u00e7\u00f2\7\'\2\2\u00e8\u00f2\7~\2\2\u00e9"+
		"\u00f2\7i\2\2\u00ea\u00f2\7j\2\2\u00eb\u00f2\7r\2\2\u00ec\u00f2\7w\2\2"+
		"\u00ed\u00ee\7\34\2\2\u00ee\u00ef\5$\23\2\u00ef\u00f0\7\35\2\2\u00f0\u00f2"+
		"\3\2\2\2\u00f1\u00e0\3\2\2\2\u00f1\u00e2\3\2\2\2\u00f1\u00e3\3\2\2\2\u00f1"+
		"\u00e4\3\2\2\2\u00f1\u00e5\3\2\2\2\u00f1\u00e8\3\2\2\2\u00f1\u00e9\3\2"+
		"\2\2\u00f1\u00ea\3\2\2\2\u00f1\u00eb\3\2\2\2\u00f1\u00ec\3\2\2\2\u00f1"+
		"\u00ed\3\2\2\2\u00f2\u00fe\3\2\2\2\u00f3\u00f4\f\f\2\2\u00f4\u00f5\7%"+
		"\2\2\u00f5\u00fd\7\177\2\2\u00f6\u00f7\f\13\2\2\u00f7\u00f9\7\34\2\2\u00f8"+
		"\u00fa\5\"\22\2\u00f9\u00f8\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb\3"+
		"\2\2\2\u00fb\u00fd\7\35\2\2\u00fc\u00f3\3\2\2\2\u00fc\u00f6\3\2\2\2\u00fd"+
		"\u0100\3\2\2\2\u00fe\u00fc\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\'\3\2\2\2"+
		"\u0100\u00fe\3\2\2\2\u0101\u0102\7\22\2\2\u0102\u0103\5$\23\2\u0103\u0104"+
		"\7(\2\2\u0104\u0105\5$\23\2\u0105\u0106\7)\2\2\u0106\u0107\5$\23\2\u0107"+
		"\u0108\7*\2\2\u0108)\3\2\2\2\u0109\u010a\7+\2\2\u010a\u010b\5:\36\2\u010b"+
		"\u010c\7,\2\2\u010c\u010d\5 \21\2\u010d\u010e\7\17\2\2\u010e\u010f\5$"+
		"\23\2\u010f+\3\2\2\2\u0110\u0111\7-\2\2\u0111\u0112\5:\36\2\u0112\u0113"+
		"\7\5\2\2\u0113\u0114\5$\23\2\u0114\u0115\7\17\2\2\u0115\u0116\5$\23\2"+
		"\u0116-\3\2\2\2\u0117\u0118\b\30\1\2\u0118\u0119\7u\2\2\u0119\u011a\5"+
		":\36\2\u011a\u011b\7,\2\2\u011b\u011c\5 \21\2\u011c\u011d\7|\2\2\u011d"+
		"\u011e\5.\30\6\u011e\u012a\3\2\2\2\u011f\u0120\7v\2\2\u0120\u0121\5:\36"+
		"\2\u0121\u0122\7,\2\2\u0122\u0123\5 \21\2\u0123\u0124\7|\2\2\u0124\u0125"+
		"\5.\30\5\u0125\u012a\3\2\2\2\u0126\u0127\7\64\2\2\u0127\u012a\5.\30\4"+
		"\u0128\u012a\5\60\31\2\u0129\u0117\3\2\2\2\u0129\u011f\3\2\2\2\u0129\u0126"+
		"\3\2\2\2\u0129\u0128\3\2\2\2\u012a\u013f\3\2\2\2\u012b\u012c\f\f\2\2\u012c"+
		"\u012d\7.\2\2\u012d\u013e\5.\30\r\u012e\u012f\f\13\2\2\u012f\u0130\7/"+
		"\2\2\u0130\u013e\5.\30\f\u0131\u0132\f\n\2\2\u0132\u0133\7\60\2\2\u0133"+
		"\u013e\5.\30\13\u0134\u0135\f\t\2\2\u0135\u0136\7\61\2\2\u0136\u013e\5"+
		".\30\n\u0137\u0138\f\b\2\2\u0138\u0139\7\62\2\2\u0139\u013e\5.\30\t\u013a"+
		"\u013b\f\7\2\2\u013b\u013c\7\63\2\2\u013c\u013e\5.\30\b\u013d\u012b\3"+
		"\2\2\2\u013d\u012e\3\2\2\2\u013d\u0131\3\2\2\2\u013d\u0134\3\2\2\2\u013d"+
		"\u0137\3\2\2\2\u013d\u013a\3\2\2\2\u013e\u0141\3\2\2\2\u013f\u013d\3\2"+
		"\2\2\u013f\u0140\3\2\2\2\u0140/\3\2\2\2\u0141\u013f\3\2\2\2\u0142\u0143"+
		"\5\62\32\2\u0143\u0144\t\2\2\2\u0144\u0145\5\62\32\2\u0145\u0148\3\2\2"+
		"\2\u0146\u0148\5\62\32\2\u0147\u0142\3\2\2\2\u0147\u0146\3\2\2\2\u0148"+
		"\61\3\2\2\2\u0149\u014a\b\32\1\2\u014a\u014b\5\64\33\2\u014b\u014c\t\3"+
		"\2\2\u014c\u014d\5\64\33\2\u014d\u0150\3\2\2\2\u014e\u0150\5\64\33\2\u014f"+
		"\u0149\3\2\2\2\u014f\u014e\3\2\2\2\u0150\u0159\3\2\2\2\u0151\u0152\f\6"+
		"\2\2\u0152\u0153\7=\2\2\u0153\u0158\5\62\32\7\u0154\u0155\f\5\2\2\u0155"+
		"\u0156\7>\2\2\u0156\u0158\5\64\33\2\u0157\u0151\3\2\2\2\u0157\u0154\3"+
		"\2\2\2\u0158\u015b\3\2\2\2\u0159\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a"+
		"\63\3\2\2\2\u015b\u0159\3\2\2\2\u015c\u015d\b\33\1\2\u015d\u015e\7A\2"+
		"\2\u015e\u015f\5$\23\2\u015f\u0160\7\'\2\2\u0160\u0161\7B\2\2\u0161\u0162"+
		"\5$\23\2\u0162\u0163\7\'\2\2\u0163\u0195\3\2\2\2\u0164\u0165\7C\2\2\u0165"+
		"\u0166\5$\23\2\u0166\u0167\7D\2\2\u0167\u0195\3\2\2\2\u0168\u0169\7o\2"+
		"\2\u0169\u016a\7I\2\2\u016a\u016b\5$\23\2\u016b\u016c\7\'\2\2\u016c\u016d"+
		"\7B\2\2\u016d\u016e\5$\23\2\u016e\u016f\7\'\2\2\u016f\u0170\5$\23\2\u0170"+
		"\u0171\7\177\2\2\u0171\u0195\3\2\2\2\u0172\u0173\7o\2\2\u0173\u0174\5"+
		"$\23\2\u0174\u0175\7\177\2\2\u0175\u0195\3\2\2\2\u0176\u0177\7p\2\2\u0177"+
		"\u0178\7I\2\2\u0178\u0179\5$\23\2\u0179\u017a\7\'\2\2\u017a\u017b\7B\2"+
		"\2\u017b\u017c\5$\23\2\u017c\u017d\7\'\2\2\u017d\u017e\5\64\33\13\u017e"+
		"\u0195\3\2\2\2\u017f\u0180\7q\2\2\u0180\u0181\7I\2\2\u0181\u0182\5$\23"+
		"\2\u0182\u0183\7\'\2\2\u0183\u0184\7B\2\2\u0184\u0185\5$\23\2\u0185\u0186"+
		"\7\'\2\2\u0186\u0187\5\64\33\n\u0187\u0195\3\2\2\2\u0188\u0189\7>\2\2"+
		"\u0189\u0195\5\64\33\t\u018a\u018b\7=\2\2\u018b\u0195\5\64\33\b\u018c"+
		"\u018d\7x\2\2\u018d\u0195\5\64\33\7\u018e\u018f\7t\2\2\u018f\u0190\7I"+
		"\2\2\u0190\u0191\7\177\2\2\u0191\u0192\7\'\2\2\u0192\u0195\5\64\33\6\u0193"+
		"\u0195\5\66\34\2\u0194\u015c\3\2\2\2\u0194\u0164\3\2\2\2\u0194\u0168\3"+
		"\2\2\2\u0194\u0172\3\2\2\2\u0194\u0176\3\2\2\2\u0194\u017f\3\2\2\2\u0194"+
		"\u0188\3\2\2\2\u0194\u018a\3\2\2\2\u0194\u018c\3\2\2\2\u0194\u018e\3\2"+
		"\2\2\u0194\u0193\3\2\2\2\u0195\u019f\3\2\2\2\u0196\u0197\f\16\2\2\u0197"+
		"\u0198\t\4\2\2\u0198\u019e\5\64\33\17\u0199\u019a\f\5\2\2\u019a\u019e"+
		"\7J\2\2\u019b\u019c\f\4\2\2\u019c\u019e\7s\2\2\u019d\u0196\3\2\2\2\u019d"+
		"\u0199\3\2\2\2\u019d\u019b\3\2\2\2\u019e\u01a1\3\2\2\2\u019f\u019d\3\2"+
		"\2\2\u019f\u01a0\3\2\2\2\u01a0\65\3\2\2\2\u01a1\u019f\3\2\2\2\u01a2\u01a3"+
		"\b\34\1\2\u01a3\u01a6\58\35\2\u01a4\u01a6\5&\24\2\u01a5\u01a2\3\2\2\2"+
		"\u01a5\u01a4\3\2\2\2\u01a6\u01cd\3\2\2\2\u01a7\u01a8\f\21\2\2\u01a8\u01cc"+
		"\7K\2\2\u01a9\u01aa\f\20\2\2\u01aa\u01cc\t\5\2\2\u01ab\u01ac\f\17\2\2"+
		"\u01ac\u01cc\7S\2\2\u01ad\u01ae\f\16\2\2\u01ae\u01cc\7T\2\2\u01af\u01b0"+
		"\f\r\2\2\u01b0\u01cc\7U\2\2\u01b1\u01b2\f\f\2\2\u01b2\u01cc\7V\2\2\u01b3"+
		"\u01b4\f\13\2\2\u01b4\u01cc\7W\2\2\u01b5\u01b6\f\n\2\2\u01b6\u01cc\7X"+
		"\2\2\u01b7\u01b8\f\t\2\2\u01b8\u01cc\7Y\2\2\u01b9\u01ba\f\b\2\2\u01ba"+
		"\u01cc\7Z\2\2\u01bb\u01bc\f\7\2\2\u01bc\u01bd\7B\2\2\u01bd\u01be\5$\23"+
		"\2\u01be\u01bf\7\'\2\2\u01bf\u01cc\3\2\2\2\u01c0\u01c1\f\6\2\2\u01c1\u01c2"+
		"\7I\2\2\u01c2\u01c3\5$\23\2\u01c3\u01c4\7\'\2\2\u01c4\u01cc\3\2\2\2\u01c5"+
		"\u01c6\f\5\2\2\u01c6\u01c7\t\6\2\2\u01c7\u01c8\7\34\2\2\u01c8\u01c9\5"+
		"$\23\2\u01c9\u01ca\7\35\2\2\u01ca\u01cc\3\2\2\2\u01cb\u01a7\3\2\2\2\u01cb"+
		"\u01a9\3\2\2\2\u01cb\u01ab\3\2\2\2\u01cb\u01ad\3\2\2\2\u01cb\u01af\3\2"+
		"\2\2\u01cb\u01b1\3\2\2\2\u01cb\u01b3\3\2\2\2\u01cb\u01b5\3\2\2\2\u01cb"+
		"\u01b7\3\2\2\2\u01cb\u01b9\3\2\2\2\u01cb\u01bb\3\2\2\2\u01cb\u01c0\3\2"+
		"\2\2\u01cb\u01c5\3\2\2\2\u01cc\u01cf\3\2\2\2\u01cd\u01cb\3\2\2\2\u01cd"+
		"\u01ce\3\2\2\2\u01ce\67\3\2\2\2\u01cf\u01cd\3\2\2\2\u01d0\u01d1\7h\2\2"+
		"\u01d1\u01d2\7\177\2\2\u01d2\u01d3\7,\2\2\u01d3\u01d4\5 \21\2\u01d4\u01d5"+
		"\7\t\2\2\u01d5\u01d6\5$\23\2\u01d6\u01d7\7\'\2\2\u01d7\u01e3\3\2\2\2\u01d8"+
		"\u01d9\7h\2\2\u01d9\u01da\7\177\2\2\u01da\u01db\7,\2\2\u01db\u01dc\5 "+
		"\21\2\u01dc\u01dd\7\t\2\2\u01dd\u01de\5$\23\2\u01de\u01df\7|\2\2\u01df"+
		"\u01e0\5$\23\2\u01e0\u01e1\7\'\2\2\u01e1\u01e3\3\2\2\2\u01e2\u01d0\3\2"+
		"\2\2\u01e2\u01d8\3\2\2\2\u01e39\3\2\2\2\u01e4\u01e5\7\177\2\2\u01e5;\3"+
		"\2\2\2\35ANYa\u008d\u00a3\u00ce\u00d5\u00de\u00f1\u00f9\u00fc\u00fe\u0129"+
		"\u013d\u013f\u0147\u014f\u0157\u0159\u0194\u019d\u019f\u01a5\u01cb\u01cd"+
		"\u01e2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}