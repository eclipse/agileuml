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
    | reexpression
    | expanding
    | simplify
    | substituting
    | solve
    | prove
    | factorBy
    ;

formula
    :	expression
    ; 

constraint
    :	'Constraint' 'on' expression '|' logicalExpression
    ; 

reexpression
    : 'Express' expression 'as' expression
    ; 

simplify 
    : 'Simplify' expression
    ; 

substituting
    : 'Substitute' expression 'for' ID 'in' expression
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

factorBy
    : 'Factor' expression 'by' expression
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
    | basicExpression '.' ID 
    | basicExpression '(' expressionList? ')'  
    | basicExpression '[' expression ']' 
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
    : 'let' identifier ':' type '=' expression 'in' expression
    ; 

logicalExpression
    : logicalExpression '=>' logicalExpression  
    | logicalExpression 'implies' logicalExpression  
    | logicalExpression 'or' logicalExpression  
    | logicalExpression 'xor' logicalExpression  
    | logicalExpression '&' logicalExpression 
    | logicalExpression 'and' logicalExpression
    | FORALL identifier CDOT logicalExpression
    | EXISTS identifier CDOT logicalExpression  
    | 'not' logicalExpression  
    | equalityExpression
    ; 

equalityExpression 
    : additiveExpression 
        ('=' | '<' | '>' | '>=' | '<=' | '/=' | '<>' |
         ':'| '/:' | '<:' | IN | NOTIN) additiveExpression 
    | additiveExpression
    ; 

additiveExpression
    : additiveExpression '+' additiveExpression 
    | additiveExpression '-' factorExpression
    | factorExpression ('..' | '|->') factorExpression                         
    | factorExpression
    ; 

factorExpression 
    : 'C' '_{' expression '}' '^{' expression '}'
    | factorExpression ('*' | '/' | 'mod' | 'div') 
                                   factorExpression
    | INTEGRAL '_{' expression '}' '^{' expression '}' expression ID 
    | INTEGRAL expression ID 
    | SIGMA '_{' expression '}' '^{' expression '}' expression  
    | PRODUCT '_{' expression '}' '^{' expression '}' expression  
    | '-' factorExpression 
    | '+' factorExpression 
    | SQUAREROOT factorExpression
    | PARTIALDIFF '_{' ID '}' factorExpression
    | factorExpression '!'
    | factorExpression DIFFERENTIAL 
    | factor2Expression
    ; 


// factor2Expressions can appear on LHS of ->
// ->subrange is used for ->substring and ->subSequence

factor2Expression
  : factor2Expression '->size()' 
  | factor2Expression ('->isEmpty()' | 
                       '->notEmpty()' | 
                       '->asSet()' | '->asBag()' | 
                       '->asOrderedSet()' | 
                       '->asSequence()' | 
                       '->sort()' ) 
   | factor2Expression '->any()'   
   | factor2Expression '->first()'  
   | factor2Expression '->last()' 
   | factor2Expression '->front()'  
   | factor2Expression '->tail()' 
   | factor2Expression '->reverse()'  
   | factor2Expression '->max()'  
   | factor2Expression '->min()'  
   | factor2Expression '^{' expression '}' 
   | factor2Expression '_{' expression '}' 
   | factor2Expression ('->at' | '->union' | '->intersection' 
            | '->includes' | '->excludes' | '->including' 
            | '->excluding' | '->includesAll'  
            | '->excludesAll' | '->prepend' | '->append'  
            | '->count' | '->apply') 
                                   '(' expression ')' 
   | setExpression 
   | basicExpression
   ; 

setExpression 
    : 'OrderedSet{' expressionList? '}'  
    | 'Bag{' expressionList? '}'  
    | 'Set{' expressionList? '}' 
    | 'Sequence{' expressionList? '}' 
    | 'Map{' expressionList? '}'
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

NATURAL : 'Ñ'; 
INTEGER : 'Ž';
REAL : '®'; 

CDOT : '•'; 

NEWLINE : [\r\n]+ -> skip ;
INT     : [0-9]+ ;
ID  :   [a-zA-Z]+[a-zA-Z0-9$]* ;      // match identifiers
WS  :   [ \t\n\r]+ -> skip ;
