/******************************
* Parser for mathematical notation for financial validation.
*
* 
* Copyright (c) 2003--2023 Howard Haughton, Kevin Lano
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
* *****************************/

grammar MathOCL;	
	
specification
  : 'specification' ID part* EOF
  ;

part
    : formula
    | constraint
    | theorem
    | rewrite
    | expanding
    | simplify
    | substituting
    | solve
    | prove
    ;

formula
    :	'Define' basicExpression '=' (instruction | expression) 
    | 'Define' basicExpression '~' expression
    | 'Define' basicExpression
    ; 

instruction
    : substituteIn 
    | expandTo 
    | expressAs 
    | cancelIn 
    | factorBy 
    | groupBy
    ; 

constraint
    :	'Constraint' 'on' expression '|' logicalExpression
    ; 

theorem
    :	'Theorem' logicalExpression 'when' logicalExpression
    ; 

rewrite
    : 'Rewrite' expression 'to' expression
    ; 

simplify 
    : 'Simplify' (instruction | expression)
    ; 

substituting
    : 'Substitute' expression 'for' basicExpression 'in' expression
    ; 

solve 
    : 'Solve' expressionList 'for' idList
    ; 

prove 
    : 'Prove' logicalExpression 'if' expressionList
    ; 

expanding
    : 'Expanding' expression 'to' INT 'terms' 
    ; 

substituteIn
    : 'Substitute' basicExpression 'in' expression
    ; 

expandTo
    : 'Expand' expression 'to' INT 'terms' 
    ; 

expressAs
    : 'Express' expression 'as' 'polynomial' 'in' identifier 
    ; 

factorBy
    : 'Factor' expression 'by' expression
    ; 

cancelIn
    : 'Cancel' expression 'in' expression
    ; 

groupBy
    : 'Group' expression 'by' expression
    ; 

idList
     : (ID ',')* ID
     ; 


type
    : 'Sequence' '(' type ')'  
    | 'Set' '(' type ')'  
    | 'Bag' '(' type ')' 
    | 'OrderedSet' '(' type ')' 
    | 'Map' '(' type ',' type ')' 
    | 'Function' '(' type ',' type ')' 
    | NATURAL
    | INTEGER
    | REAL
    | ID
    ; 


expressionList
    : (expression ',')* expression
    ; 

expression
    : logicalExpression  
    | conditionalExpression  
    | lambdaExpression  
    | letExpression
    ;


// Basic expressions can appear on the LHS of . or ->

basicExpression
    : 'null' 
    | 'true'
    | 'false'
    | '?'
    | basicExpression '.' ID 
    | basicExpression '(' expressionList? ')'  
    | identifier
    | 'g{' ID '}'  
    | INT  
    | FLOAT_LITERAL
    | STRING_LITERAL
    | INFINITY
    | EMPTYSET   
    |	'(' expression ')'
    ; 

conditionalExpression
    : 'if' expression 'then' expression 'else' expression 'endif'
    ; 

lambdaExpression 
    : 'lambda' identifier ':' type 'in' expression
    ; 

// A let is just an application of a lambda:

letExpression
    : 'let' identifier '=' expression 'in' expression
    ; 

logicalExpression
    : logicalExpression '&' logicalExpression 
    | logicalExpression 'and' logicalExpression
    | logicalExpression 'or' logicalExpression  
    | logicalExpression 'xor' logicalExpression  
    | logicalExpression '=>' logicalExpression  
    | logicalExpression 'implies' logicalExpression  
    | FORALL identifier ':' type CDOT logicalExpression
    | EXISTS identifier ':' type CDOT logicalExpression  
    | 'not' equalityExpression  
    | equalityExpression
    ; 

equalityExpression 
    : additiveExpression 
        ('=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' |
         '~' | ':'| '/:' | '<:' | IN | NOTIN) additiveExpression 
    | additiveExpression
    ; 

additiveExpression
    : additiveExpression '+' additiveExpression 
    | additiveExpression '-' factorExpression
    | factorExpression ('..' | '|->') factorExpression                         
    | factorExpression
    ; 

factorExpression 
    : 'C_{' expression '}' '^{' expression '}'
    | 'E[' expression ']' 
    | '-' factorExpression 
    | factorExpression DIFFERENTIAL 
    | 'lim_{' identifier ARROW expression '}' factor2Expression
    | factorExpression ('*' | '/' | 'mod' | 'div') 
                                   factorExpression
    | INTEGRAL '_{' expression '}' '^{' expression '}' expression ID 
    | INTEGRAL expression ID 
    | SIGMA '_{' expression '}' '^{' expression '}' factorExpression  
    | PRODUCT '_{' expression '}' '^{' expression '}' factorExpression  
    | '+' factor2Expression 
    | SQUAREROOT factor2Expression
    | PARTIALDIFF '_{' ID '}' factor2Expression
    | factor2Expression '!'
    | factor2Expression
    ; 

factor2Expression
  : factor2Expression '^{' expression '}' 
  | setExpression 
  | basicExpression
  ; 

setExpression 
    : '{' ID ':' type '|' expression '}'
    | '{' ID ':' type '|' expression CDOT expression '}'  
    | 'Set{' expressionList? '}'
    | 'Sequence{' expressionList? '}'
    ; 


identifier: ID ;

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

IN : '©'; 
NOTIN : '¢'; 
INTEGRAL : '‡'; 
SIGMA : '€';
PRODUCT : '×'; 
INFINITY : '…';
DIFFERENTIAL : '´';
PARTIALDIFF : 'Ð'; 
FORALL : '¡';
EXISTS : '£'; 
EMPTYSET : 'Ø';
SQUAREROOT : '†';
ARROW : '»';

NATURAL : 'Ñ'; 
INTEGER : 'Ž';
REAL : '®'; 

CDOT : '•'; 

NEWLINE : [\r\n]+ -> skip ;
INT     : [0-9]+ ;
ID  :   [_a-zA-Z]+[a-zA-Z0-9$]* ;      // match identifiers
WS  :   [ \t\n\r]+ -> skip ;

