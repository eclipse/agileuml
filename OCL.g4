grammar OCL;	
	
model:
  'package' ID '{' classifier* '}' EOF;

classifier: classDefinition
    | interfaceDefinition
    | usecaseDefinition
    | enumeration;

interfaceDefinition:	 'interface' ID '{' classBody '}' 
    | 'interface' ID 'extends' ID '{' classBody '}'
    | 'interface' ID '{' '}'
    | 'interface' ID 'extends' ID '{' '}'; 

classDefinition:	 'class' ID '{' classBody '}' 
    | 'class' ID 'extends' ID '{' classBody '}'
    | 'class' ID '{' '}'
    | 'class' ID 'extends' ID '{' '}'; 

classBody: classBodyElement+; 

classBodyElement: attributeDefinition 
    | operationDefinition
    | invariant 
    | stereotype; 

attributeDefinition: 
      'attribute' ID ':' type ';' |
      'attribute' ID 'identity' ':' type ';' |
      'attribute' ID 'derived' ':' type ';' | 
      'static' 'attribute' ID ':' type ';'
    ; 

operationDefinition: 
      'operation' ID '(' ')' ':' type 
      'pre:' expression 'post:' expression ';' | 
      'static' 'operation' ID '(' ')' ':' type 
      'pre:' expression 'post:' expression ';' | 
      'operation' ID '(' ')' ':' type 
      'pre:' expression 'post:' expression 
      'activity:' statement ';' | 
      'static' 'operation' ID '(' ')' ':' type 
      'pre:' expression 'post:' expression 
      'activity:' statement ';' | 
      'operation' ID '(' parameterDeclarations ')' ':' type 
      'pre:' expression 'post:' expression ';'  |
      'static' 'operation' ID '(' parameterDeclarations ')' ':' type 
      'pre:' expression 'post:' expression ';'  |
      'operation' ID '(' parameterDeclarations ')' ':' type 
      'pre:' expression 'post:' expression 
      'activity:' statement ';' |
      'static' 'operation' ID '(' parameterDeclarations ')' ':' type 
      'pre:' expression 'post:' expression 
      'activity:' statement ';'  
      ;

parameterDeclarations: 
      (parameterDeclaration ',')* parameterDeclaration;

parameterDeclaration: 
      ID ':' type;

usecaseDefinition: 
      'usecase' ID '{' '}' | 
      'usecase' ID '{' usecaseBody '}' |
      'usecase' ID ':' type '{' '}' | 
      'usecase' ID ':' type '{' usecaseBody '}' | 
      'usecase' ID '(' parameterDeclarations ')' '{' '}' | 
      'usecase' ID '(' parameterDeclarations ')' ':' type '{' '}' | 
      'usecase' ID '(' parameterDeclarations ')' '{' usecaseBody '}' |
      'usecase' ID '(' parameterDeclarations ')' ':' type '{' usecaseBody '}';

usecaseBody: 
      usecaseBodyElement+; 

usecaseBodyElement: 
      'parameter' ID ':' type ';' | 
      'precondition' expression ';' | 
      'extends' ID ';' | 
      'extendedBy' ID ';' | 
      'activity:' statement ';' | 
      '::' expression |
      stereotype;

invariant: 
      'invariant' expression ';' ; 

stereotype: 
      'stereotype' ID ';' | 
      'stereotype' ID '=' STRING_LITERAL ';' | 
      'stereotype' ID '=' ID ';' ;

enumeration: 
      'enumeration' ID '{' enumerationLiteral+ '}';  

enumerationLiteral:
      'literal' ID;

type: 
    'Sequence' '(' type ')' | 
    'Set' '(' type ')' | 
    'Map' '(' type ',' type ')' | 
    'Function' '(' type ',' type ')' | 
    ID; 


expressionList:
    (expression ',')* expression; 

expression: 
    logicalExpression | 
    conditionalExpression | 
    lambdaExpression | 
    letExpression;


// Basic expressions can appear on the LHS of . or ->

basicExpression:
    ID | 
    basicExpression '.' ID | 
    basicExpression '(' ')' | 
    basicExpression '(' expressionList ')' | 
    basicExpression '[' expression ']' | 
    ID '@pre' { System.out.println("parsed prestate identifier: " + $ID.text); } 
    |	INT   { System.out.println("parsed integer: " + $INT.int); } 
    | FLOAT_LITERAL
    | STRING_LITERAL
    | ID    { System.out.println("parsed identifier: " + $ID.text); } 
    |	'(' expression ')'; 

conditionalExpression: 
    'if' expression 
    'then' expression 'else' expression 'endif'; 

lambdaExpression: 
    'lambda' ID ':' type 'in' expression; 

letExpression: 
    'let' ID ':' type '=' expression 'in' expression; 

logicalExpression: 
    logicalExpression '=>' logicalExpression | 
    logicalExpression 'implies' logicalExpression | 
    logicalExpression 'or' logicalExpression | 
    logicalExpression 'xor' logicalExpression | 
    logicalExpression '&' logicalExpression | 
    logicalExpression 'and' logicalExpression | 
    'not' logicalExpression | 
    equalityExpression; 

equalityExpression: 
    additiveExpression 
    ('=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' |
     ':'| '/:' | '<:') additiveExpression | 
    additiveExpression; 

additiveExpression:
     factorExpression ('+' | '-' | '..') 
                          additiveExpression |
     factorExpression; 

factorExpression: 
     factorExpression ('*' | '/' | 'mod' | 'div') 
                      factorExpression |
     '-' factorExpression |  
     '+' factorExpression | 
     factor2Expression; 


// factor2Expressions can appear on LHS of ->

factor2Expression:
    factor2Expression '->size()' |
    factor2Expression '->copy()' | 
    factor2Expression '->isEmpty()' | 
    factor2Expression '->notEmpty()' |
    factor2Expression '->any()' |  
    factor2Expression '->log()' | 
    factor2Expression '->exp()' |
    factor2Expression '->sin()' | 
    factor2Expression '->cos()' |
    factor2Expression '->tan()' | 
    factor2Expression '->asin()' | 
    factor2Expression '->acos()' |
    factor2Expression '->atan()' | 
    factor2Expression '->log10()' |
    factor2Expression '->first()' | 
    factor2Expression '->last()' |
    factor2Expression '->front()' | 
    factor2Expression '->tail()' | 
    factor2Expression '->reverse()' | 
    factor2Expression '->tanh()' | 
    factor2Expression '->sinh()' |
    factor2Expression '->cosh()' |
    factor2Expression '->floor()' | 
    factor2Expression '->ceil()' |
    factor2Expression '->round()' |
    factor2Expression '->abs()' | 
    factor2Expression '->oclType()' |
    factor2Expression '->allInstances()' |
    factor2Expression '->oclIsUndefined()' |
    factor2Expression '->oclIsInvalid()' |
    factor2Expression '->sum()' | 
    factor2Expression '->prd()' | 
    factor2Expression '->max()' | 
    factor2Expression '->min()' | 
    factor2Expression '->sqrt()' | 
    factor2Expression '->cbrt()' | 
    factor2Expression '->sqr()' | 
    factor2Expression ('->unionAll()' | '->intersectAll()' |
                       '->concatenateAll()') | 
    factor2Expression ('->pow' | '->gcd') 
                 '(' expression ')' |
    factor2Expression ('->at' | '->union' | '->intersection' 
            | '->includes' | '->excludes' | '->including' 
            | '->excluding' | '->includesAll' 
            | '->excludesAll' | '->apply') 
                      '(' expression ')' |
    factor2Expression ('->oclAsType' | '->oclIsTypeOf' | 
              '->oclIsKindOf') '(' expression ')' |
    factor2Expression '->collect' '(' identifier '|' expression ')' |
    factor2Expression '->select' '(' identifier '|' expression ')' |
    factor2Expression '->reject' '(' identifier '|' expression ')' |
    factor2Expression '->forAll' '(' identifier '|' expression ')' |
    factor2Expression '->exists' '(' identifier '|' expression ')' |
    factor2Expression '->exists1' '(' identifier '|' expression ')' |
    factor2Expression '->one' '(' identifier '|' expression ')' |
    factor2Expression '->any' '(' identifier '|' expression ')' |
    factor2Expression '->sortedBy' '(' identifier '|' expression ')' |

    factor2Expression '->subrange' '(' expression ',' expression ')' | 
    factor2Expression '->replace' '(' expression ',' expression ')' | 
    factor2Expression '->replaceAll' '(' expression ',' expression ')' | 
    factor2Expression '->replaceAllMatches' '(' expression ',' expression ')' | 
    factor2Expression '->replaceFirstMatch' '(' expression ',' expression ')' | 
    factor2Expression '->insertAt' '(' expression ',' expression ')' | 
    factor2Expression '->insertInto' '(' expression ',' expression ')' | 
    factor2Expression '->setAt' '(' expression ',' expression ')' | 
    factor2Expression '->iterate' '(' identifier ';' identifier '=' expression '|' expression ')' | 
    setExpression |
    basicExpression; 

setExpression: 
    'Set{' '}' | 
    'Sequence{' '}' | 
    'Set{' expressionList '}' | 
    'Sequence{' expressionList '}';

statement: 
   'skip' | 
   'return' | 
   'continue' | 
   'break' | 
   'var' ID ':' type | 
   'if' expression 'then' statement 'else' statement | 
   'while' expression 'do' statement | 
   'for' ID ':' expression 'do' statement |
   'return' expression | 
   basicExpression ':=' expression | 
   statement ';' statement | 
   'execute' expression |
   'call' basicExpression |
   '(' statement ')';  

identifier: 
   ID;

FLOAT_LITERAL:  Digits '.' Digits ;

STRING_LITERAL:     '"' (~["\\\r\n] | EscapeSequence)* '"';

NULL_LITERAL:       'null';

MULTILINE_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);


fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigits
    : HexDigit ((HexDigit | '_')* HexDigit)?
    ;

fragment HexDigit
    : [0-9a-fA-F]
    ;


fragment Digits
    : [0-9]+
    ;

NEWLINE : [\r\n]+ -> skip ;
INT     : [0-9]+ ;
ID  :   [a-zA-Z]+[a-zA-Z0-9_$]* ;      // match identifiers
WS  :   [ \t\n\r]+ -> skip ;
