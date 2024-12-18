// Generated from Assm.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AssmLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "T__24", 
			"T__25", "T__26", "T__27", "T__28", "T__29", "T__30", "T__31", "FLOAT_LITERAL", 
			"Digits", "NEWLINE", "INT", "ID", "WS"
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


	public AssmLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Assm.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\'\u00f6\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25"+
		"\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3"+
		" \3!\3!\3\"\3\"\3\"\3\"\3#\6#\u00d7\n#\r#\16#\u00d8\3$\6$\u00dc\n$\r$"+
		"\16$\u00dd\3%\6%\u00e1\n%\r%\16%\u00e2\3&\6&\u00e6\n&\r&\16&\u00e7\3&"+
		"\7&\u00eb\n&\f&\16&\u00ee\13&\3\'\6\'\u00f1\n\'\r\'\16\'\u00f2\3\'\3\'"+
		"\2\2(\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35"+
		"\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36"+
		";\37= ?!A\"C#E\2G$I%K&M\'\3\2\7\3\2\62;\4\2\f\f\17\17\4\2C\\c|\5\2\62"+
		";C\\c|\4\2\13\13\"\"\2\u00fa\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2"+
		"\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37"+
		"\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3"+
		"\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2"+
		"\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C"+
		"\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5R\3\2"+
		"\2\2\7W\3\2\2\2\t\\\3\2\2\2\13`\3\2\2\2\rd\3\2\2\2\17i\3\2\2\2\21n\3\2"+
		"\2\2\23r\3\2\2\2\25v\3\2\2\2\27{\3\2\2\2\31\u0081\3\2\2\2\33\u0085\3\2"+
		"\2\2\35\u008c\3\2\2\2\37\u0091\3\2\2\2!\u0096\3\2\2\2#\u009b\3\2\2\2%"+
		"\u009d\3\2\2\2\'\u00a0\3\2\2\2)\u00a3\3\2\2\2+\u00a6\3\2\2\2-\u00aa\3"+
		"\2\2\2/\u00af\3\2\2\2\61\u00b4\3\2\2\2\63\u00b9\3\2\2\2\65\u00bf\3\2\2"+
		"\2\67\u00c5\3\2\2\29\u00c7\3\2\2\2;\u00c9\3\2\2\2=\u00cb\3\2\2\2?\u00cd"+
		"\3\2\2\2A\u00cf\3\2\2\2C\u00d1\3\2\2\2E\u00d6\3\2\2\2G\u00db\3\2\2\2I"+
		"\u00e0\3\2\2\2K\u00e5\3\2\2\2M\u00f0\3\2\2\2OP\7^\2\2PQ\7p\2\2Q\4\3\2"+
		"\2\2RS\7N\2\2ST\7Q\2\2TU\7C\2\2UV\7F\2\2V\6\3\2\2\2WX\7O\2\2XY\7W\2\2"+
		"YZ\7N\2\2Z[\7V\2\2[\b\3\2\2\2\\]\7C\2\2]^\7F\2\2^_\7F\2\2_\n\3\2\2\2`"+
		"a\7U\2\2ab\7W\2\2bc\7D\2\2c\f\3\2\2\2de\7F\2\2ef\7K\2\2fg\7X\2\2gh\7H"+
		"\2\2h\16\3\2\2\2ij\7F\2\2jk\7K\2\2kl\7X\2\2lm\7Z\2\2m\20\3\2\2\2no\7C"+
		"\2\2op\7D\2\2pq\7U\2\2q\22\3\2\2\2rs\7E\2\2st\7J\2\2tu\7U\2\2u\24\3\2"+
		"\2\2vw\7U\2\2wx\7K\2\2xy\7I\2\2yz\7P\2\2z\26\3\2\2\2{|\7F\2\2|}\7G\2\2"+
		"}~\7N\2\2~\177\7V\2\2\177\u0080\7C\2\2\u0080\30\3\2\2\2\u0081\u0082\7"+
		"P\2\2\u0082\u0083\7Q\2\2\u0083\u0084\7R\2\2\u0084\32\3\2\2\2\u0085\u0086"+
		"\7T\2\2\u0086\u0087\7G\2\2\u0087\u0088\7V\2\2\u0088\u0089\7W\2\2\u0089"+
		"\u008a\7T\2\2\u008a\u008b\7P\2\2\u008b\34\3\2\2\2\u008c\u008d\7X\2\2\u008d"+
		"\u008e\7C\2\2\u008e\u008f\7T\2\2\u008f\u0090\7Z\2\2\u0090\36\3\2\2\2\u0091"+
		"\u0092\7X\2\2\u0092\u0093\7C\2\2\u0093\u0094\7T\2\2\u0094\u0095\7H\2\2"+
		"\u0095 \3\2\2\2\u0096\u0097\7X\2\2\u0097\u0098\7C\2\2\u0098\u0099\7T\2"+
		"\2\u0099\u009a\7C\2\2\u009a\"\3\2\2\2\u009b\u009c\7D\2\2\u009c$\3\2\2"+
		"\2\u009d\u009e\7D\2\2\u009e\u009f\7O\2\2\u009f&\3\2\2\2\u00a0\u00a1\7"+
		"D\2\2\u00a1\u00a2\7R\2\2\u00a2(\3\2\2\2\u00a3\u00a4\7D\2\2\u00a4\u00a5"+
		"\7\\\2\2\u00a5*\3\2\2\2\u00a6\u00a7\7D\2\2\u00a7\u00a8\7P\2\2\u00a8\u00a9"+
		"\7\\\2\2\u00a9,\3\2\2\2\u00aa\u00ab\7E\2\2\u00ab\u00ac\7C\2\2\u00ac\u00ad"+
		"\7N\2\2\u00ad\u00ae\7N\2\2\u00ae.\3\2\2\2\u00af\u00b0\7N\2\2\u00b0\u00b1"+
		"\7T\2\2\u00b1\u00b2\7G\2\2\u00b2\u00b3\7I\2\2\u00b3\60\3\2\2\2\u00b4\u00b5"+
		"\7U\2\2\u00b5\u00b6\7T\2\2\u00b6\u00b7\7G\2\2\u00b7\u00b8\7I\2\2\u00b8"+
		"\62\3\2\2\2\u00b9\u00ba\7U\2\2\u00ba\u00bb\7V\2\2\u00bb\u00bc\7Q\2\2\u00bc"+
		"\u00bd\7T\2\2\u00bd\u00be\7G\2\2\u00be\64\3\2\2\2\u00bf\u00c0\7N\2\2\u00c0"+
		"\u00c1\7C\2\2\u00c1\u00c2\7D\2\2\u00c2\u00c3\7G\2\2\u00c3\u00c4\7N\2\2"+
		"\u00c4\66\3\2\2\2\u00c5\u00c6\7<\2\2\u00c68\3\2\2\2\u00c7\u00c8\7,\2\2"+
		"\u00c8:\3\2\2\2\u00c9\u00ca\7-\2\2\u00ca<\3\2\2\2\u00cb\u00cc\7/\2\2\u00cc"+
		">\3\2\2\2\u00cd\u00ce\7*\2\2\u00ce@\3\2\2\2\u00cf\u00d0\7+\2\2\u00d0B"+
		"\3\2\2\2\u00d1\u00d2\5E#\2\u00d2\u00d3\7\60\2\2\u00d3\u00d4\5E#\2\u00d4"+
		"D\3\2\2\2\u00d5\u00d7\t\2\2\2\u00d6\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2"+
		"\u00d8\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9F\3\2\2\2\u00da\u00dc\t"+
		"\3\2\2\u00db\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd"+
		"\u00de\3\2\2\2\u00deH\3\2\2\2\u00df\u00e1\t\2\2\2\u00e0\u00df\3\2\2\2"+
		"\u00e1\u00e2\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3J\3"+
		"\2\2\2\u00e4\u00e6\t\4\2\2\u00e5\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7"+
		"\u00e5\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ec\3\2\2\2\u00e9\u00eb\t\5"+
		"\2\2\u00ea\u00e9\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec"+
		"\u00ed\3\2\2\2\u00edL\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f1\t\6\2\2"+
		"\u00f0\u00ef\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3"+
		"\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\u00f5\b\'\2\2\u00f5N\3\2\2\2\t\2\u00d8"+
		"\u00dd\u00e2\u00e7\u00ec\u00f2\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}