/*
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

lexer grammar CGTLLexer;

options { }

// Keywords and CGTL symbols

NOT                : 'not';
ALL                : 'all';
ANY                : 'any';
MATCHES            : 'matches';

WHEN               : '«when»';
ACTION             : '«action»';
ENDRULE            : '«end»';
MAPSTO             : '«mapsto»';

COMMA              : ',';
COLONCOLON         : '::';
BQ                 : '`'; 
REPLACE            : '/';


STRING_LITERAL:     '"' (~["\\\r\n])* '"';

MULTILINE_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

SYMBOL : [!#-/:-@[-^{-~]+;

fragment Digits
    : [0-9]+
    ;

NEWLINE : [\r\n]+ -> skip ;
INT     : [0-9]+ ;
FLOAT_LITERAL:  Digits '.' Digits ;

ID  :   [a-zA-Z][a-zA-Z0-9_$]* ;      // match identifiers
VAR :   '_' [0-9]+
    | '_*'
    | '_+'
    ; 

WS  :   [ \t\n\r]+ -> skip ;

