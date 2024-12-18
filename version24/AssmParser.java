// Generated from Assm.g4 by ANTLR 4.8
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AssmParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, FLOAT_LITERAL=33, NEWLINE=34, INT=35, ID=36, WS=37;
	public static final int
		RULE_prog = 0, RULE_stat = 1, RULE_expr = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stat", "expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\\n'", "'LOAD'", "'MULT'", "'ADD'", "'SUB'", "'DIVF'", "'DIVX'", 
			"'ABS'", "'CHS'", "'SIGN'", "'DELTA'", "'NOP'", "'RETURN'", "'VARX'", 
			"'VARF'", "'VARA'", "'B'", "'BM'", "'BP'", "'BZ'", "'BNZ'", "'CALL'", 
			"'LREG'", "'SREG'", "'STORE'", "'LABEL'", "':'", "'*'", "'+'", "'-'", 
			"'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "FLOAT_LITERAL", 
			"NEWLINE", "INT", "ID", "WS"
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
	public String getGrammarFileName() { return "Assm.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AssmParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(AssmParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(AssmParser.NEWLINE, i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(11);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25))) != 0)) {
				{
				{
				setState(6);
				stat();
				setState(7);
				_la = _input.LA(1);
				if ( !(_la==T__0 || _la==NEWLINE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(13);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class StatContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).enterStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).exitStat(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stat);
		try {
			setState(60);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(14);
				match(T__1);
				setState(15);
				expr(0);
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(16);
				match(T__2);
				setState(17);
				expr(0);
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 3);
				{
				setState(18);
				match(T__3);
				setState(19);
				expr(0);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 4);
				{
				setState(20);
				match(T__4);
				setState(21);
				expr(0);
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 5);
				{
				setState(22);
				match(T__5);
				setState(23);
				expr(0);
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 6);
				{
				setState(24);
				match(T__6);
				setState(25);
				expr(0);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 7);
				{
				setState(26);
				match(T__7);
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 8);
				{
				setState(27);
				match(T__8);
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 9);
				{
				setState(28);
				match(T__9);
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 10);
				{
				setState(29);
				match(T__10);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 11);
				{
				setState(30);
				match(T__11);
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 12);
				{
				setState(31);
				match(T__12);
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 13);
				{
				setState(32);
				match(T__13);
				setState(33);
				expr(0);
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 14);
				{
				setState(34);
				match(T__14);
				setState(35);
				expr(0);
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 15);
				{
				setState(36);
				match(T__15);
				setState(37);
				expr(0);
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 16);
				{
				setState(38);
				match(T__16);
				setState(39);
				expr(0);
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 17);
				{
				setState(40);
				match(T__17);
				setState(41);
				expr(0);
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 18);
				{
				setState(42);
				match(T__18);
				setState(43);
				expr(0);
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 19);
				{
				setState(44);
				match(T__19);
				setState(45);
				expr(0);
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 20);
				{
				setState(46);
				match(T__20);
				setState(47);
				expr(0);
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 21);
				{
				setState(48);
				match(T__21);
				setState(49);
				expr(0);
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 22);
				{
				setState(50);
				match(T__22);
				setState(51);
				expr(0);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 23);
				{
				setState(52);
				match(T__23);
				setState(53);
				expr(0);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 24);
				{
				setState(54);
				match(T__24);
				setState(55);
				expr(0);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 25);
				{
				setState(56);
				match(T__25);
				setState(57);
				expr(0);
				setState(58);
				match(T__26);
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

	public static class ExprContext extends ParserRuleContext {
		public Token INT;
		public Token ID;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode INT() { return getToken(AssmParser.INT, 0); }
		public TerminalNode FLOAT_LITERAL() { return getToken(AssmParser.FLOAT_LITERAL, 0); }
		public TerminalNode ID() { return getToken(AssmParser.ID, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AssmListener ) ((AssmListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__27:
				{
				setState(63);
				match(T__27);
				setState(64);
				expr(6);
				}
				break;
			case INT:
				{
				setState(65);
				((ExprContext)_localctx).INT = match(INT);
				 System.out.println("parsed integer: " + (((ExprContext)_localctx).INT!=null?Integer.valueOf(((ExprContext)_localctx).INT.getText()):0)); 
				}
				break;
			case FLOAT_LITERAL:
				{
				setState(67);
				match(FLOAT_LITERAL);
				}
				break;
			case ID:
				{
				setState(68);
				((ExprContext)_localctx).ID = match(ID);
				 System.out.println("parsed identifier: " + (((ExprContext)_localctx).ID!=null?((ExprContext)_localctx).ID.getText():null)); 
				}
				break;
			case T__30:
				{
				setState(70);
				match(T__30);
				setState(71);
				expr(0);
				setState(72);
				match(T__31);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(81);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExprContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expr);
					setState(76);
					if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
					setState(77);
					_la = _input.LA(1);
					if ( !(_la==T__28 || _la==T__29) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(78);
					expr(6);
					}
					} 
				}
				setState(83);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\'W\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\7\2\f\n\2\f\2\16\2\17\13\2\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3?\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\5\4M\n\4\3\4\3\4\3\4\7\4R\n\4\f\4\16\4U\13\4\3\4\2\3\6\5"+
		"\2\4\6\2\4\4\2\3\3$$\3\2\37 \2q\2\r\3\2\2\2\4>\3\2\2\2\6L\3\2\2\2\b\t"+
		"\5\4\3\2\t\n\t\2\2\2\n\f\3\2\2\2\13\b\3\2\2\2\f\17\3\2\2\2\r\13\3\2\2"+
		"\2\r\16\3\2\2\2\16\3\3\2\2\2\17\r\3\2\2\2\20\21\7\4\2\2\21?\5\6\4\2\22"+
		"\23\7\5\2\2\23?\5\6\4\2\24\25\7\6\2\2\25?\5\6\4\2\26\27\7\7\2\2\27?\5"+
		"\6\4\2\30\31\7\b\2\2\31?\5\6\4\2\32\33\7\t\2\2\33?\5\6\4\2\34?\7\n\2\2"+
		"\35?\7\13\2\2\36?\7\f\2\2\37?\7\r\2\2 ?\7\16\2\2!?\7\17\2\2\"#\7\20\2"+
		"\2#?\5\6\4\2$%\7\21\2\2%?\5\6\4\2&\'\7\22\2\2\'?\5\6\4\2()\7\23\2\2)?"+
		"\5\6\4\2*+\7\24\2\2+?\5\6\4\2,-\7\25\2\2-?\5\6\4\2./\7\26\2\2/?\5\6\4"+
		"\2\60\61\7\27\2\2\61?\5\6\4\2\62\63\7\30\2\2\63?\5\6\4\2\64\65\7\31\2"+
		"\2\65?\5\6\4\2\66\67\7\32\2\2\67?\5\6\4\289\7\33\2\29?\5\6\4\2:;\7\34"+
		"\2\2;<\5\6\4\2<=\7\35\2\2=?\3\2\2\2>\20\3\2\2\2>\22\3\2\2\2>\24\3\2\2"+
		"\2>\26\3\2\2\2>\30\3\2\2\2>\32\3\2\2\2>\34\3\2\2\2>\35\3\2\2\2>\36\3\2"+
		"\2\2>\37\3\2\2\2> \3\2\2\2>!\3\2\2\2>\"\3\2\2\2>$\3\2\2\2>&\3\2\2\2>("+
		"\3\2\2\2>*\3\2\2\2>,\3\2\2\2>.\3\2\2\2>\60\3\2\2\2>\62\3\2\2\2>\64\3\2"+
		"\2\2>\66\3\2\2\2>8\3\2\2\2>:\3\2\2\2?\5\3\2\2\2@A\b\4\1\2AB\7\36\2\2B"+
		"M\5\6\4\bCD\7%\2\2DM\b\4\1\2EM\7#\2\2FG\7&\2\2GM\b\4\1\2HI\7!\2\2IJ\5"+
		"\6\4\2JK\7\"\2\2KM\3\2\2\2L@\3\2\2\2LC\3\2\2\2LE\3\2\2\2LF\3\2\2\2LH\3"+
		"\2\2\2MS\3\2\2\2NO\f\7\2\2OP\t\3\2\2PR\5\6\4\bQN\3\2\2\2RU\3\2\2\2SQ\3"+
		"\2\2\2ST\3\2\2\2T\7\3\2\2\2US\3\2\2\2\6\r>LS";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}