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
		RULE_specification = 0, RULE_part = 1, RULE_formula = 2, RULE_instruction = 3, 
		RULE_constraint = 4, RULE_reexpression = 5, RULE_simplify = 6, RULE_substituting = 7, 
		RULE_solve = 8, RULE_prove = 9, RULE_expanding = 10, RULE_substituteIn = 11, 
		RULE_expandTo = 12, RULE_factorBy = 13, RULE_cancelIn = 14, RULE_idList = 15, 
		RULE_type = 16, RULE_expressionList = 17, RULE_expression = 18, RULE_basicExpression = 19, 
		RULE_conditionalExpression = 20, RULE_lambdaExpression = 21, RULE_letExpression = 22, 
		RULE_logicalExpression = 23, RULE_equalityExpression = 24, RULE_additiveExpression = 25, 
		RULE_factorExpression = 26, RULE_factor2Expression = 27, RULE_setExpression = 28, 
		RULE_identifier = 29;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "part", "formula", "instruction", "constraint", "reexpression", 
			"simplify", "substituting", "solve", "prove", "expanding", "substituteIn", 
			"expandTo", "factorBy", "cancelIn", "idList", "type", "expressionList", 
			"expression", "basicExpression", "conditionalExpression", "lambdaExpression", 
			"letExpression", "logicalExpression", "equalityExpression", "additiveExpression", 
			"factorExpression", "factor2Expression", "setExpression", "identifier"
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
			setState(60);
			match(T__0);
			setState(61);
			match(ID);
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__4) | (1L << T__7) | (1L << T__9) | (1L << T__10) | (1L << T__13) | (1L << T__14) | (1L << T__16))) != 0)) {
				{
				{
				setState(62);
				part();
				}
				}
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(68);
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
			setState(78);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				formula();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				constraint();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				reexpression();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				expanding();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				simplify();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(75);
				substituting();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 7);
				{
				setState(76);
				solve();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 8);
				{
				setState(77);
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
			setState(93);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(80);
				match(T__1);
				setState(81);
				match(ID);
				setState(82);
				match(T__2);
				setState(85);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
				case T__10:
				case T__19:
				case T__20:
				case T__22:
					{
					setState(83);
					instruction();
					}
					break;
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
					setState(84);
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
				setState(87);
				match(T__1);
				setState(88);
				match(ID);
				setState(89);
				match(T__3);
				setState(90);
				expression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(91);
				match(T__1);
				setState(92);
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

	public static class InstructionContext extends ParserRuleContext {
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
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__10:
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				substituteIn();
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				expandTo();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 3);
				{
				setState(97);
				cancelIn();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 4);
				{
				setState(98);
				factorBy();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 5);
				{
				setState(99);
				simplify();
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
			setState(102);
			match(T__4);
			setState(103);
			match(T__5);
			setState(104);
			expression();
			setState(105);
			match(T__6);
			setState(106);
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
		enterRule(_localctx, 10, RULE_reexpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			match(T__7);
			setState(109);
			expression();
			setState(110);
			match(T__8);
			setState(111);
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
		enterRule(_localctx, 12, RULE_simplify);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(T__9);
			setState(114);
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
		enterRule(_localctx, 14, RULE_substituting);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(T__10);
			setState(117);
			expression();
			setState(118);
			match(T__11);
			setState(119);
			match(ID);
			setState(120);
			match(T__12);
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
		enterRule(_localctx, 16, RULE_solve);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(T__13);
			setState(124);
			expressionList();
			setState(125);
			match(T__11);
			setState(126);
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
		enterRule(_localctx, 18, RULE_prove);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(T__14);
			setState(129);
			logicalExpression(0);
			setState(130);
			match(T__15);
			setState(131);
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
		enterRule(_localctx, 20, RULE_expanding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__16);
			setState(134);
			expression();
			setState(135);
			match(T__17);
			setState(136);
			match(INT);
			setState(137);
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
		enterRule(_localctx, 22, RULE_substituteIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(T__10);
			setState(140);
			match(ID);
			setState(141);
			match(T__12);
			setState(144);
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
				setState(142);
				expression();
				}
				break;
			case T__19:
				{
				setState(143);
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
		enterRule(_localctx, 24, RULE_expandTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			match(T__19);
			setState(147);
			expression();
			setState(148);
			match(T__17);
			setState(149);
			match(INT);
			setState(150);
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
		enterRule(_localctx, 26, RULE_factorBy);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__20);
			setState(153);
			expression();
			setState(154);
			match(T__21);
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
		enterRule(_localctx, 28, RULE_cancelIn);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(T__22);
			setState(158);
			expression();
			setState(159);
			match(T__12);
			setState(160);
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
		enterRule(_localctx, 30, RULE_idList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(162);
					match(ID);
					setState(163);
					match(T__23);
					}
					} 
				}
				setState(168);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			setState(169);
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
		enterRule(_localctx, 32, RULE_type);
		try {
			setState(209);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__24:
				enterOuterAlt(_localctx, 1);
				{
				setState(171);
				match(T__24);
				setState(172);
				match(T__25);
				setState(173);
				type();
				setState(174);
				match(T__26);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				match(T__27);
				setState(177);
				match(T__25);
				setState(178);
				type();
				setState(179);
				match(T__26);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 3);
				{
				setState(181);
				match(T__28);
				setState(182);
				match(T__25);
				setState(183);
				type();
				setState(184);
				match(T__26);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 4);
				{
				setState(186);
				match(T__29);
				setState(187);
				match(T__25);
				setState(188);
				type();
				setState(189);
				match(T__26);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 5);
				{
				setState(191);
				match(T__30);
				setState(192);
				match(T__25);
				setState(193);
				type();
				setState(194);
				match(T__23);
				setState(195);
				type();
				setState(196);
				match(T__26);
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 6);
				{
				setState(198);
				match(T__31);
				setState(199);
				match(T__25);
				setState(200);
				type();
				setState(201);
				match(T__23);
				setState(202);
				type();
				setState(203);
				match(T__26);
				}
				break;
			case NATURAL:
				enterOuterAlt(_localctx, 7);
				{
				setState(205);
				match(NATURAL);
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 8);
				{
				setState(206);
				match(INTEGER);
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 9);
				{
				setState(207);
				match(REAL);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 10);
				{
				setState(208);
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
		enterRule(_localctx, 34, RULE_expressionList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(211);
					expression();
					setState(212);
					match(T__23);
					}
					} 
				}
				setState(218);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(219);
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
		enterRule(_localctx, 36, RULE_expression);
		try {
			setState(225);
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
				setState(221);
				logicalExpression(0);
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(222);
				conditionalExpression();
				}
				break;
			case T__40:
				enterOuterAlt(_localctx, 3);
				{
				setState(223);
				lambdaExpression();
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 4);
				{
				setState(224);
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
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_basicExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NULL_LITERAL:
				{
				setState(228);
				match(NULL_LITERAL);
				}
				break;
			case T__32:
				{
				setState(229);
				match(T__32);
				}
				break;
			case T__33:
				{
				setState(230);
				match(T__33);
				}
				break;
			case ID:
				{
				setState(231);
				identifier();
				}
				break;
			case T__35:
				{
				setState(232);
				match(T__35);
				setState(233);
				match(ID);
				setState(234);
				match(T__36);
				}
				break;
			case INT:
				{
				setState(235);
				match(INT);
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(236);
				match(FLOAT_LITERAL);
				}
				break;
			case STRING_LITERAL:
				{
				setState(237);
				match(STRING_LITERAL);
				}
				break;
			case INFINITY:
				{
				setState(238);
				match(INFINITY);
				}
				break;
			case EMPTYSET:
				{
				setState(239);
				match(EMPTYSET);
				}
				break;
			case T__25:
				{
				setState(240);
				match(T__25);
				setState(241);
				expression();
				setState(242);
				match(T__26);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(257);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(255);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(246);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(247);
						match(T__34);
						setState(248);
						match(ID);
						}
						break;
					case 2:
						{
						_localctx = new BasicExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_basicExpression);
						setState(249);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(250);
						match(T__25);
						setState(252);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__15) | (1L << T__25) | (1L << T__32) | (1L << T__33) | (1L << T__35) | (1L << T__40) | (1L << T__42) | (1L << T__49) | (1L << T__58) | (1L << T__59) | (1L << T__62))) != 0) || ((((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & ((1L << (T__64 - 65)) | (1L << (T__101 - 65)) | (1L << (FLOAT_LITERAL - 65)) | (1L << (STRING_LITERAL - 65)) | (1L << (NULL_LITERAL - 65)) | (1L << (INTEGRAL - 65)) | (1L << (SIGMA - 65)) | (1L << (PRODUCT - 65)) | (1L << (INFINITY - 65)) | (1L << (PARTIALDIFF - 65)) | (1L << (FORALL - 65)) | (1L << (EXISTS - 65)) | (1L << (EMPTYSET - 65)) | (1L << (SQUAREROOT - 65)) | (1L << (INT - 65)) | (1L << (ID - 65)))) != 0)) {
							{
							setState(251);
							expressionList();
							}
						}

						setState(254);
						match(T__26);
						}
						break;
					}
					} 
				}
				setState(259);
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
		enterRule(_localctx, 40, RULE_conditionalExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(T__15);
			setState(261);
			expression();
			setState(262);
			match(T__37);
			setState(263);
			expression();
			setState(264);
			match(T__38);
			setState(265);
			expression();
			setState(266);
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
		enterRule(_localctx, 42, RULE_lambdaExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(268);
			match(T__40);
			setState(269);
			identifier();
			setState(270);
			match(T__41);
			setState(271);
			type();
			setState(272);
			match(T__12);
			setState(273);
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
		enterRule(_localctx, 44, RULE_letExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			match(T__42);
			setState(276);
			identifier();
			setState(277);
			match(T__2);
			setState(278);
			expression();
			setState(279);
			match(T__12);
			setState(280);
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
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_logicalExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case FORALL:
				{
				setState(283);
				match(FORALL);
				setState(284);
				identifier();
				setState(285);
				match(T__41);
				setState(286);
				type();
				setState(287);
				match(CDOT);
				setState(288);
				logicalExpression(4);
				}
				break;
			case EXISTS:
				{
				setState(290);
				match(EXISTS);
				setState(291);
				identifier();
				setState(292);
				match(T__41);
				setState(293);
				type();
				setState(294);
				match(CDOT);
				setState(295);
				logicalExpression(3);
				}
				break;
			case T__49:
				{
				setState(297);
				match(T__49);
				setState(298);
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
				setState(299);
				equalityExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(322);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(320);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(302);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(303);
						match(T__43);
						setState(304);
						logicalExpression(11);
						}
						break;
					case 2:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(305);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(306);
						match(T__44);
						setState(307);
						logicalExpression(10);
						}
						break;
					case 3:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(308);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(309);
						match(T__45);
						setState(310);
						logicalExpression(9);
						}
						break;
					case 4:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(311);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(312);
						match(T__46);
						setState(313);
						logicalExpression(8);
						}
						break;
					case 5:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(314);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(315);
						match(T__47);
						setState(316);
						logicalExpression(7);
						}
						break;
					case 6:
						{
						_localctx = new LogicalExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logicalExpression);
						setState(317);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(318);
						match(T__48);
						setState(319);
						logicalExpression(6);
						}
						break;
					}
					} 
				}
				setState(324);
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
		enterRule(_localctx, 48, RULE_equalityExpression);
		int _la;
		try {
			setState(330);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(325);
				additiveExpression(0);
				setState(326);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__2) | (1L << T__3) | (1L << T__41) | (1L << T__50) | (1L << T__51) | (1L << T__52) | (1L << T__53) | (1L << T__54) | (1L << T__55) | (1L << T__56) | (1L << T__57))) != 0) || _la==IN || _la==NOTIN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(327);
				additiveExpression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(329);
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
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_additiveExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(333);
				factorExpression(0);
				setState(334);
				_la = _input.LA(1);
				if ( !(_la==T__60 || _la==T__61) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(335);
				factorExpression(0);
				}
				break;
			case 2:
				{
				setState(337);
				factorExpression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(348);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(346);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
					case 1:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(340);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(341);
						match(T__58);
						setState(342);
						additiveExpression(5);
						}
						break;
					case 2:
						{
						_localctx = new AdditiveExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_additiveExpression);
						setState(343);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(344);
						match(T__59);
						setState(345);
						factorExpression(0);
						}
						break;
					}
					} 
				}
				setState(350);
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
		int _startState = 52;
		enterRecursionRule(_localctx, 52, RULE_factorExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(352);
				match(T__62);
				setState(353);
				expression();
				setState(354);
				match(T__36);
				setState(355);
				match(T__63);
				setState(356);
				expression();
				setState(357);
				match(T__36);
				}
				break;
			case 2:
				{
				setState(359);
				match(T__64);
				setState(360);
				expression();
				setState(361);
				match(T__65);
				}
				break;
			case 3:
				{
				setState(363);
				match(INTEGRAL);
				setState(364);
				match(T__70);
				setState(365);
				expression();
				setState(366);
				match(T__36);
				setState(367);
				match(T__63);
				setState(368);
				expression();
				setState(369);
				match(T__36);
				setState(370);
				expression();
				setState(371);
				match(ID);
				}
				break;
			case 4:
				{
				setState(373);
				match(INTEGRAL);
				setState(374);
				expression();
				setState(375);
				match(ID);
				}
				break;
			case 5:
				{
				setState(377);
				match(SIGMA);
				setState(378);
				match(T__70);
				setState(379);
				expression();
				setState(380);
				match(T__36);
				setState(381);
				match(T__63);
				setState(382);
				expression();
				setState(383);
				match(T__36);
				setState(384);
				factorExpression(9);
				}
				break;
			case 6:
				{
				setState(386);
				match(PRODUCT);
				setState(387);
				match(T__70);
				setState(388);
				expression();
				setState(389);
				match(T__36);
				setState(390);
				match(T__63);
				setState(391);
				expression();
				setState(392);
				match(T__36);
				setState(393);
				factorExpression(8);
				}
				break;
			case 7:
				{
				setState(395);
				match(T__59);
				setState(396);
				factorExpression(7);
				}
				break;
			case 8:
				{
				setState(397);
				match(T__58);
				setState(398);
				factorExpression(6);
				}
				break;
			case 9:
				{
				setState(399);
				match(SQUAREROOT);
				setState(400);
				factorExpression(5);
				}
				break;
			case 10:
				{
				setState(401);
				match(PARTIALDIFF);
				setState(402);
				match(T__70);
				setState(403);
				match(ID);
				setState(404);
				match(T__36);
				setState(405);
				factorExpression(4);
				}
				break;
			case 11:
				{
				setState(406);
				factor2Expression(0);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(418);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(416);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
					case 1:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(409);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(410);
						_la = _input.LA(1);
						if ( !(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & ((1L << (T__66 - 67)) | (1L << (T__67 - 67)) | (1L << (T__68 - 67)) | (1L << (T__69 - 67)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(411);
						factorExpression(13);
						}
						break;
					case 2:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(412);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(413);
						match(T__71);
						}
						break;
					case 3:
						{
						_localctx = new FactorExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factorExpression);
						setState(414);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(415);
						match(DIFFERENTIAL);
						}
						break;
					}
					} 
				}
				setState(420);
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
		int _startState = 54;
		enterRecursionRule(_localctx, 54, RULE_factor2Expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__101:
				{
				setState(422);
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
				setState(423);
				basicExpression(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(464);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(462);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
					case 1:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(426);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(427);
						match(T__72);
						}
						break;
					case 2:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(428);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(429);
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
						setState(430);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(431);
						match(T__80);
						}
						break;
					case 4:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(432);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(433);
						match(T__81);
						}
						break;
					case 5:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(434);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(435);
						match(T__82);
						}
						break;
					case 6:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(436);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(437);
						match(T__83);
						}
						break;
					case 7:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(438);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(439);
						match(T__84);
						}
						break;
					case 8:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(440);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(441);
						match(T__85);
						}
						break;
					case 9:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(442);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(443);
						match(T__86);
						}
						break;
					case 10:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(444);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(445);
						match(T__87);
						}
						break;
					case 11:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(446);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(447);
						match(T__63);
						setState(448);
						expression();
						setState(449);
						match(T__36);
						}
						break;
					case 12:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(451);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(452);
						match(T__70);
						setState(453);
						expression();
						setState(454);
						match(T__36);
						}
						break;
					case 13:
						{
						_localctx = new Factor2ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_factor2Expression);
						setState(456);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(457);
						_la = _input.LA(1);
						if ( !(((((_la - 89)) & ~0x3f) == 0 && ((1L << (_la - 89)) & ((1L << (T__88 - 89)) | (1L << (T__89 - 89)) | (1L << (T__90 - 89)) | (1L << (T__91 - 89)) | (1L << (T__92 - 89)) | (1L << (T__93 - 89)) | (1L << (T__94 - 89)) | (1L << (T__95 - 89)) | (1L << (T__96 - 89)) | (1L << (T__97 - 89)) | (1L << (T__98 - 89)) | (1L << (T__99 - 89)) | (1L << (T__100 - 89)))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(458);
						match(T__25);
						setState(459);
						expression();
						setState(460);
						match(T__26);
						}
						break;
					}
					} 
				}
				setState(466);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
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
		enterRule(_localctx, 56, RULE_setExpression);
		try {
			setState(485);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(467);
				match(T__101);
				setState(468);
				match(ID);
				setState(469);
				match(T__41);
				setState(470);
				type();
				setState(471);
				match(T__6);
				setState(472);
				expression();
				setState(473);
				match(T__36);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(475);
				match(T__101);
				setState(476);
				match(ID);
				setState(477);
				match(T__41);
				setState(478);
				type();
				setState(479);
				match(T__6);
				setState(480);
				expression();
				setState(481);
				match(CDOT);
				setState(482);
				expression();
				setState(483);
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
		enterRule(_localctx, 58, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
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
		case 19:
			return basicExpression_sempred((BasicExpressionContext)_localctx, predIndex);
		case 23:
			return logicalExpression_sempred((LogicalExpressionContext)_localctx, predIndex);
		case 25:
			return additiveExpression_sempred((AdditiveExpressionContext)_localctx, predIndex);
		case 26:
			return factorExpression_sempred((FactorExpressionContext)_localctx, predIndex);
		case 27:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0080\u01ec\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2\3\2\3"+
		"\2\7\2B\n\2\f\2\16\2E\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"Q\n\3\3\4\3\4\3\4\3\4\3\4\5\4X\n\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4`\n\4\3"+
		"\5\3\5\3\5\3\5\3\5\5\5g\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3"+
		"\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\5\r\u0093"+
		"\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20"+
		"\3\20\3\20\3\20\3\21\3\21\7\21\u00a7\n\21\f\21\16\21\u00aa\13\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00d4\n\22"+
		"\3\23\3\23\3\23\7\23\u00d9\n\23\f\23\16\23\u00dc\13\23\3\23\3\23\3\24"+
		"\3\24\3\24\3\24\5\24\u00e4\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00f7\n\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\5\25\u00ff\n\25\3\25\7\25\u0102\n\25\f\25\16\25\u0105"+
		"\13\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31"+
		"\u012f\n\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\7\31\u0143\n\31\f\31\16\31\u0146\13\31"+
		"\3\32\3\32\3\32\3\32\3\32\5\32\u014d\n\32\3\33\3\33\3\33\3\33\3\33\3\33"+
		"\5\33\u0155\n\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u015d\n\33\f\33\16"+
		"\33\u0160\13\33\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\5\34\u019a\n\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\7\34"+
		"\u01a3\n\34\f\34\16\34\u01a6\13\34\3\35\3\35\3\35\5\35\u01ab\n\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\7\35\u01d1\n\35\f\35\16\35\u01d4\13"+
		"\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\5\36\u01e8\n\36\3\37\3\37\3\37\2\7(\60\64\668"+
		" \2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<\2\7\6"+
		"\2\5\6,,\65<mn\3\2?@\3\2EH\3\2LR\3\2[g\2\u0221\2>\3\2\2\2\4P\3\2\2\2\6"+
		"_\3\2\2\2\bf\3\2\2\2\nh\3\2\2\2\fn\3\2\2\2\16s\3\2\2\2\20v\3\2\2\2\22"+
		"}\3\2\2\2\24\u0082\3\2\2\2\26\u0087\3\2\2\2\30\u008d\3\2\2\2\32\u0094"+
		"\3\2\2\2\34\u009a\3\2\2\2\36\u009f\3\2\2\2 \u00a8\3\2\2\2\"\u00d3\3\2"+
		"\2\2$\u00da\3\2\2\2&\u00e3\3\2\2\2(\u00f6\3\2\2\2*\u0106\3\2\2\2,\u010e"+
		"\3\2\2\2.\u0115\3\2\2\2\60\u012e\3\2\2\2\62\u014c\3\2\2\2\64\u0154\3\2"+
		"\2\2\66\u0199\3\2\2\28\u01aa\3\2\2\2:\u01e7\3\2\2\2<\u01e9\3\2\2\2>?\7"+
		"\3\2\2?C\7\177\2\2@B\5\4\3\2A@\3\2\2\2BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D"+
		"F\3\2\2\2EC\3\2\2\2FG\7\2\2\3G\3\3\2\2\2HQ\5\6\4\2IQ\5\n\6\2JQ\5\f\7\2"+
		"KQ\5\26\f\2LQ\5\16\b\2MQ\5\20\t\2NQ\5\22\n\2OQ\5\24\13\2PH\3\2\2\2PI\3"+
		"\2\2\2PJ\3\2\2\2PK\3\2\2\2PL\3\2\2\2PM\3\2\2\2PN\3\2\2\2PO\3\2\2\2Q\5"+
		"\3\2\2\2RS\7\4\2\2ST\7\177\2\2TW\7\5\2\2UX\5\b\5\2VX\5&\24\2WU\3\2\2\2"+
		"WV\3\2\2\2X`\3\2\2\2YZ\7\4\2\2Z[\7\177\2\2[\\\7\6\2\2\\`\5&\24\2]^\7\4"+
		"\2\2^`\7\177\2\2_R\3\2\2\2_Y\3\2\2\2_]\3\2\2\2`\7\3\2\2\2ag\5\30\r\2b"+
		"g\5\32\16\2cg\5\36\20\2dg\5\34\17\2eg\5\16\b\2fa\3\2\2\2fb\3\2\2\2fc\3"+
		"\2\2\2fd\3\2\2\2fe\3\2\2\2g\t\3\2\2\2hi\7\7\2\2ij\7\b\2\2jk\5&\24\2kl"+
		"\7\t\2\2lm\5\60\31\2m\13\3\2\2\2no\7\n\2\2op\5&\24\2pq\7\13\2\2qr\5&\24"+
		"\2r\r\3\2\2\2st\7\f\2\2tu\5&\24\2u\17\3\2\2\2vw\7\r\2\2wx\5&\24\2xy\7"+
		"\16\2\2yz\7\177\2\2z{\7\17\2\2{|\5&\24\2|\21\3\2\2\2}~\7\20\2\2~\177\5"+
		"$\23\2\177\u0080\7\16\2\2\u0080\u0081\5 \21\2\u0081\23\3\2\2\2\u0082\u0083"+
		"\7\21\2\2\u0083\u0084\5\60\31\2\u0084\u0085\7\22\2\2\u0085\u0086\5$\23"+
		"\2\u0086\25\3\2\2\2\u0087\u0088\7\23\2\2\u0088\u0089\5&\24\2\u0089\u008a"+
		"\7\24\2\2\u008a\u008b\7~\2\2\u008b\u008c\7\25\2\2\u008c\27\3\2\2\2\u008d"+
		"\u008e\7\r\2\2\u008e\u008f\7\177\2\2\u008f\u0092\7\17\2\2\u0090\u0093"+
		"\5&\24\2\u0091\u0093\5\32\16\2\u0092\u0090\3\2\2\2\u0092\u0091\3\2\2\2"+
		"\u0093\31\3\2\2\2\u0094\u0095\7\26\2\2\u0095\u0096\5&\24\2\u0096\u0097"+
		"\7\24\2\2\u0097\u0098\7~\2\2\u0098\u0099\7\25\2\2\u0099\33\3\2\2\2\u009a"+
		"\u009b\7\27\2\2\u009b\u009c\5&\24\2\u009c\u009d\7\30\2\2\u009d\u009e\5"+
		"&\24\2\u009e\35\3\2\2\2\u009f\u00a0\7\31\2\2\u00a0\u00a1\5&\24\2\u00a1"+
		"\u00a2\7\17\2\2\u00a2\u00a3\5&\24\2\u00a3\37\3\2\2\2\u00a4\u00a5\7\177"+
		"\2\2\u00a5\u00a7\7\32\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8"+
		"\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ab\3\2\2\2\u00aa\u00a8\3\2"+
		"\2\2\u00ab\u00ac\7\177\2\2\u00ac!\3\2\2\2\u00ad\u00ae\7\33\2\2\u00ae\u00af"+
		"\7\34\2\2\u00af\u00b0\5\"\22\2\u00b0\u00b1\7\35\2\2\u00b1\u00d4\3\2\2"+
		"\2\u00b2\u00b3\7\36\2\2\u00b3\u00b4\7\34\2\2\u00b4\u00b5\5\"\22\2\u00b5"+
		"\u00b6\7\35\2\2\u00b6\u00d4\3\2\2\2\u00b7\u00b8\7\37\2\2\u00b8\u00b9\7"+
		"\34\2\2\u00b9\u00ba\5\"\22\2\u00ba\u00bb\7\35\2\2\u00bb\u00d4\3\2\2\2"+
		"\u00bc\u00bd\7 \2\2\u00bd\u00be\7\34\2\2\u00be\u00bf\5\"\22\2\u00bf\u00c0"+
		"\7\35\2\2\u00c0\u00d4\3\2\2\2\u00c1\u00c2\7!\2\2\u00c2\u00c3\7\34\2\2"+
		"\u00c3\u00c4\5\"\22\2\u00c4\u00c5\7\32\2\2\u00c5\u00c6\5\"\22\2\u00c6"+
		"\u00c7\7\35\2\2\u00c7\u00d4\3\2\2\2\u00c8\u00c9\7\"\2\2\u00c9\u00ca\7"+
		"\34\2\2\u00ca\u00cb\5\"\22\2\u00cb\u00cc\7\32\2\2\u00cc\u00cd\5\"\22\2"+
		"\u00cd\u00ce\7\35\2\2\u00ce\u00d4\3\2\2\2\u00cf\u00d4\7y\2\2\u00d0\u00d4"+
		"\7z\2\2\u00d1\u00d4\7{\2\2\u00d2\u00d4\7\177\2\2\u00d3\u00ad\3\2\2\2\u00d3"+
		"\u00b2\3\2\2\2\u00d3\u00b7\3\2\2\2\u00d3\u00bc\3\2\2\2\u00d3\u00c1\3\2"+
		"\2\2\u00d3\u00c8\3\2\2\2\u00d3\u00cf\3\2\2\2\u00d3\u00d0\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d2\3\2\2\2\u00d4#\3\2\2\2\u00d5\u00d6\5&\24\2"+
		"\u00d6\u00d7\7\32\2\2\u00d7\u00d9\3\2\2\2\u00d8\u00d5\3\2\2\2\u00d9\u00dc"+
		"\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dd\3\2\2\2\u00dc"+
		"\u00da\3\2\2\2\u00dd\u00de\5&\24\2\u00de%\3\2\2\2\u00df\u00e4\5\60\31"+
		"\2\u00e0\u00e4\5*\26\2\u00e1\u00e4\5,\27\2\u00e2\u00e4\5.\30\2\u00e3\u00df"+
		"\3\2\2\2\u00e3\u00e0\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4"+
		"\'\3\2\2\2\u00e5\u00e6\b\25\1\2\u00e6\u00f7\7k\2\2\u00e7\u00f7\7#\2\2"+
		"\u00e8\u00f7\7$\2\2\u00e9\u00f7\5<\37\2\u00ea\u00eb\7&\2\2\u00eb\u00ec"+
		"\7\177\2\2\u00ec\u00f7\7\'\2\2\u00ed\u00f7\7~\2\2\u00ee\u00f7\7i\2\2\u00ef"+
		"\u00f7\7j\2\2\u00f0\u00f7\7r\2\2\u00f1\u00f7\7w\2\2\u00f2\u00f3\7\34\2"+
		"\2\u00f3\u00f4\5&\24\2\u00f4\u00f5\7\35\2\2\u00f5\u00f7\3\2\2\2\u00f6"+
		"\u00e5\3\2\2\2\u00f6\u00e7\3\2\2\2\u00f6\u00e8\3\2\2\2\u00f6\u00e9\3\2"+
		"\2\2\u00f6\u00ea\3\2\2\2\u00f6\u00ed\3\2\2\2\u00f6\u00ee\3\2\2\2\u00f6"+
		"\u00ef\3\2\2\2\u00f6\u00f0\3\2\2\2\u00f6\u00f1\3\2\2\2\u00f6\u00f2\3\2"+
		"\2\2\u00f7\u0103\3\2\2\2\u00f8\u00f9\f\f\2\2\u00f9\u00fa\7%\2\2\u00fa"+
		"\u0102\7\177\2\2\u00fb\u00fc\f\13\2\2\u00fc\u00fe\7\34\2\2\u00fd\u00ff"+
		"\5$\23\2\u00fe\u00fd\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0100\3\2\2\2\u0100"+
		"\u0102\7\35\2\2\u0101\u00f8\3\2\2\2\u0101\u00fb\3\2\2\2\u0102\u0105\3"+
		"\2\2\2\u0103\u0101\3\2\2\2\u0103\u0104\3\2\2\2\u0104)\3\2\2\2\u0105\u0103"+
		"\3\2\2\2\u0106\u0107\7\22\2\2\u0107\u0108\5&\24\2\u0108\u0109\7(\2\2\u0109"+
		"\u010a\5&\24\2\u010a\u010b\7)\2\2\u010b\u010c\5&\24\2\u010c\u010d\7*\2"+
		"\2\u010d+\3\2\2\2\u010e\u010f\7+\2\2\u010f\u0110\5<\37\2\u0110\u0111\7"+
		",\2\2\u0111\u0112\5\"\22\2\u0112\u0113\7\17\2\2\u0113\u0114\5&\24\2\u0114"+
		"-\3\2\2\2\u0115\u0116\7-\2\2\u0116\u0117\5<\37\2\u0117\u0118\7\5\2\2\u0118"+
		"\u0119\5&\24\2\u0119\u011a\7\17\2\2\u011a\u011b\5&\24\2\u011b/\3\2\2\2"+
		"\u011c\u011d\b\31\1\2\u011d\u011e\7u\2\2\u011e\u011f\5<\37\2\u011f\u0120"+
		"\7,\2\2\u0120\u0121\5\"\22\2\u0121\u0122\7|\2\2\u0122\u0123\5\60\31\6"+
		"\u0123\u012f\3\2\2\2\u0124\u0125\7v\2\2\u0125\u0126\5<\37\2\u0126\u0127"+
		"\7,\2\2\u0127\u0128\5\"\22\2\u0128\u0129\7|\2\2\u0129\u012a\5\60\31\5"+
		"\u012a\u012f\3\2\2\2\u012b\u012c\7\64\2\2\u012c\u012f\5\60\31\4\u012d"+
		"\u012f\5\62\32\2\u012e\u011c\3\2\2\2\u012e\u0124\3\2\2\2\u012e\u012b\3"+
		"\2\2\2\u012e\u012d\3\2\2\2\u012f\u0144\3\2\2\2\u0130\u0131\f\f\2\2\u0131"+
		"\u0132\7.\2\2\u0132\u0143\5\60\31\r\u0133\u0134\f\13\2\2\u0134\u0135\7"+
		"/\2\2\u0135\u0143\5\60\31\f\u0136\u0137\f\n\2\2\u0137\u0138\7\60\2\2\u0138"+
		"\u0143\5\60\31\13\u0139\u013a\f\t\2\2\u013a\u013b\7\61\2\2\u013b\u0143"+
		"\5\60\31\n\u013c\u013d\f\b\2\2\u013d\u013e\7\62\2\2\u013e\u0143\5\60\31"+
		"\t\u013f\u0140\f\7\2\2\u0140\u0141\7\63\2\2\u0141\u0143\5\60\31\b\u0142"+
		"\u0130\3\2\2\2\u0142\u0133\3\2\2\2\u0142\u0136\3\2\2\2\u0142\u0139\3\2"+
		"\2\2\u0142\u013c\3\2\2\2\u0142\u013f\3\2\2\2\u0143\u0146\3\2\2\2\u0144"+
		"\u0142\3\2\2\2\u0144\u0145\3\2\2\2\u0145\61\3\2\2\2\u0146\u0144\3\2\2"+
		"\2\u0147\u0148\5\64\33\2\u0148\u0149\t\2\2\2\u0149\u014a\5\64\33\2\u014a"+
		"\u014d\3\2\2\2\u014b\u014d\5\64\33\2\u014c\u0147\3\2\2\2\u014c\u014b\3"+
		"\2\2\2\u014d\63\3\2\2\2\u014e\u014f\b\33\1\2\u014f\u0150\5\66\34\2\u0150"+
		"\u0151\t\3\2\2\u0151\u0152\5\66\34\2\u0152\u0155\3\2\2\2\u0153\u0155\5"+
		"\66\34\2\u0154\u014e\3\2\2\2\u0154\u0153\3\2\2\2\u0155\u015e\3\2\2\2\u0156"+
		"\u0157\f\6\2\2\u0157\u0158\7=\2\2\u0158\u015d\5\64\33\7\u0159\u015a\f"+
		"\5\2\2\u015a\u015b\7>\2\2\u015b\u015d\5\66\34\2\u015c\u0156\3\2\2\2\u015c"+
		"\u0159\3\2\2\2\u015d\u0160\3\2\2\2\u015e\u015c\3\2\2\2\u015e\u015f\3\2"+
		"\2\2\u015f\65\3\2\2\2\u0160\u015e\3\2\2\2\u0161\u0162\b\34\1\2\u0162\u0163"+
		"\7A\2\2\u0163\u0164\5&\24\2\u0164\u0165\7\'\2\2\u0165\u0166\7B\2\2\u0166"+
		"\u0167\5&\24\2\u0167\u0168\7\'\2\2\u0168\u019a\3\2\2\2\u0169\u016a\7C"+
		"\2\2\u016a\u016b\5&\24\2\u016b\u016c\7D\2\2\u016c\u019a\3\2\2\2\u016d"+
		"\u016e\7o\2\2\u016e\u016f\7I\2\2\u016f\u0170\5&\24\2\u0170\u0171\7\'\2"+
		"\2\u0171\u0172\7B\2\2\u0172\u0173\5&\24\2\u0173\u0174\7\'\2\2\u0174\u0175"+
		"\5&\24\2\u0175\u0176\7\177\2\2\u0176\u019a\3\2\2\2\u0177\u0178\7o\2\2"+
		"\u0178\u0179\5&\24\2\u0179\u017a\7\177\2\2\u017a\u019a\3\2\2\2\u017b\u017c"+
		"\7p\2\2\u017c\u017d\7I\2\2\u017d\u017e\5&\24\2\u017e\u017f\7\'\2\2\u017f"+
		"\u0180\7B\2\2\u0180\u0181\5&\24\2\u0181\u0182\7\'\2\2\u0182\u0183\5\66"+
		"\34\13\u0183\u019a\3\2\2\2\u0184\u0185\7q\2\2\u0185\u0186\7I\2\2\u0186"+
		"\u0187\5&\24\2\u0187\u0188\7\'\2\2\u0188\u0189\7B\2\2\u0189\u018a\5&\24"+
		"\2\u018a\u018b\7\'\2\2\u018b\u018c\5\66\34\n\u018c\u019a\3\2\2\2\u018d"+
		"\u018e\7>\2\2\u018e\u019a\5\66\34\t\u018f\u0190\7=\2\2\u0190\u019a\5\66"+
		"\34\b\u0191\u0192\7x\2\2\u0192\u019a\5\66\34\7\u0193\u0194\7t\2\2\u0194"+
		"\u0195\7I\2\2\u0195\u0196\7\177\2\2\u0196\u0197\7\'\2\2\u0197\u019a\5"+
		"\66\34\6\u0198\u019a\58\35\2\u0199\u0161\3\2\2\2\u0199\u0169\3\2\2\2\u0199"+
		"\u016d\3\2\2\2\u0199\u0177\3\2\2\2\u0199\u017b\3\2\2\2\u0199\u0184\3\2"+
		"\2\2\u0199\u018d\3\2\2\2\u0199\u018f\3\2\2\2\u0199\u0191\3\2\2\2\u0199"+
		"\u0193\3\2\2\2\u0199\u0198\3\2\2\2\u019a\u01a4\3\2\2\2\u019b\u019c\f\16"+
		"\2\2\u019c\u019d\t\4\2\2\u019d\u01a3\5\66\34\17\u019e\u019f\f\5\2\2\u019f"+
		"\u01a3\7J\2\2\u01a0\u01a1\f\4\2\2\u01a1\u01a3\7s\2\2\u01a2\u019b\3\2\2"+
		"\2\u01a2\u019e\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a3\u01a6\3\2\2\2\u01a4\u01a2"+
		"\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\67\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a7"+
		"\u01a8\b\35\1\2\u01a8\u01ab\5:\36\2\u01a9\u01ab\5(\25\2\u01aa\u01a7\3"+
		"\2\2\2\u01aa\u01a9\3\2\2\2\u01ab\u01d2\3\2\2\2\u01ac\u01ad\f\21\2\2\u01ad"+
		"\u01d1\7K\2\2\u01ae\u01af\f\20\2\2\u01af\u01d1\t\5\2\2\u01b0\u01b1\f\17"+
		"\2\2\u01b1\u01d1\7S\2\2\u01b2\u01b3\f\16\2\2\u01b3\u01d1\7T\2\2\u01b4"+
		"\u01b5\f\r\2\2\u01b5\u01d1\7U\2\2\u01b6\u01b7\f\f\2\2\u01b7\u01d1\7V\2"+
		"\2\u01b8\u01b9\f\13\2\2\u01b9\u01d1\7W\2\2\u01ba\u01bb\f\n\2\2\u01bb\u01d1"+
		"\7X\2\2\u01bc\u01bd\f\t\2\2\u01bd\u01d1\7Y\2\2\u01be\u01bf\f\b\2\2\u01bf"+
		"\u01d1\7Z\2\2\u01c0\u01c1\f\7\2\2\u01c1\u01c2\7B\2\2\u01c2\u01c3\5&\24"+
		"\2\u01c3\u01c4\7\'\2\2\u01c4\u01d1\3\2\2\2\u01c5\u01c6\f\6\2\2\u01c6\u01c7"+
		"\7I\2\2\u01c7\u01c8\5&\24\2\u01c8\u01c9\7\'\2\2\u01c9\u01d1\3\2\2\2\u01ca"+
		"\u01cb\f\5\2\2\u01cb\u01cc\t\6\2\2\u01cc\u01cd\7\34\2\2\u01cd\u01ce\5"+
		"&\24\2\u01ce\u01cf\7\35\2\2\u01cf\u01d1\3\2\2\2\u01d0\u01ac\3\2\2\2\u01d0"+
		"\u01ae\3\2\2\2\u01d0\u01b0\3\2\2\2\u01d0\u01b2\3\2\2\2\u01d0\u01b4\3\2"+
		"\2\2\u01d0\u01b6\3\2\2\2\u01d0\u01b8\3\2\2\2\u01d0\u01ba\3\2\2\2\u01d0"+
		"\u01bc\3\2\2\2\u01d0\u01be\3\2\2\2\u01d0\u01c0\3\2\2\2\u01d0\u01c5\3\2"+
		"\2\2\u01d0\u01ca\3\2\2\2\u01d1\u01d4\3\2\2\2\u01d2\u01d0\3\2\2\2\u01d2"+
		"\u01d3\3\2\2\2\u01d39\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d5\u01d6\7h\2\2\u01d6"+
		"\u01d7\7\177\2\2\u01d7\u01d8\7,\2\2\u01d8\u01d9\5\"\22\2\u01d9\u01da\7"+
		"\t\2\2\u01da\u01db\5&\24\2\u01db\u01dc\7\'\2\2\u01dc\u01e8\3\2\2\2\u01dd"+
		"\u01de\7h\2\2\u01de\u01df\7\177\2\2\u01df\u01e0\7,\2\2\u01e0\u01e1\5\""+
		"\22\2\u01e1\u01e2\7\t\2\2\u01e2\u01e3\5&\24\2\u01e3\u01e4\7|\2\2\u01e4"+
		"\u01e5\5&\24\2\u01e5\u01e6\7\'\2\2\u01e6\u01e8\3\2\2\2\u01e7\u01d5\3\2"+
		"\2\2\u01e7\u01dd\3\2\2\2\u01e8;\3\2\2\2\u01e9\u01ea\7\177\2\2\u01ea=\3"+
		"\2\2\2\36CPW_f\u0092\u00a8\u00d3\u00da\u00e3\u00f6\u00fe\u0101\u0103\u012e"+
		"\u0142\u0144\u014c\u0154\u015c\u015e\u0199\u01a2\u01a4\u01aa\u01d0\u01d2"+
		"\u01e7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}