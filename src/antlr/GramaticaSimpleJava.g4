grammar GramaticaSimpleJava;

prog : 'Prog' ID '{' declaracao* funcao* principal '}'  #Body
;
principal : (declaracao|comandos)* #Main
          ;

declaracao_var : tipo lista_ids ';' #Variaveis
               ; 

declaracao_const : 'Static' tipo ID '=' (INT|REAL|STRING|BOOL) ';' #Contantes; 

lista_ids : ID ',' lista_ids #listaIds
          | ID #listaIdsID
          ;

declaracao : declaracao_var #declaracaoVariaveis
           | declaracao_const #declaracaoContantes
           ; 
funcao : tipo ID '(' lista_de_parametros ')' '{' declaracao* comandos* '}' #Function
       ;
retorno : 'return' '(' expr ')' ';' #Return
        ;

lista_de_parametros : parametro ',' lista_de_parametros #listaParam
                    | parametro #Param
                    |/*empyt*/#emptyParam
                    ;

parametro : tipo ID #Parameter;

tipo : 'Int' #tipoInt
     | 'Real' #tipoReal 
     | 'String' #tipoString
     | 'Bool' #tipoBool 
     ;


lista_de_expr : expr ',' lista_de_expr #listExpression
                    | expr #Expression
                    |/*empyt*/ #emptyExpression;

comandos : 'print' '(' lista_de_expr ')' ';' #comandoPrint
         | 'scan' '(' lista_ids ')' ';' #comadoScan 
         | atribuicao #comandoAtribuicao
         | comandos_controle #comandoControle
         | 'break' ';' #comandoBreak 
         | chamada_de_funcao #comandoChamadaFuncao
         | retorno #comandoRetorno
         ;

chamada_de_funcao : ID '(' lista_de_expr ')' ';' #chamadaFuncao
;
chamada_de_funcao_aninhada : ID '(' lista_de_expr ')' #chamadaFuncaoAninhada
;

comando_else : 'else' '{' comandos* '}'  #controleElse;

step_for: 'step' valor #StepFor;

comandos_controle : 'if' '(' oprelacional ')' '{' comandos* '}' (comando_else)? #comandoControleIf
                | 'while' '(' oprelacional ')' '{' comandos* '}' #comandoControleWhile
                | 'for' '(' identificador '=' valor 'to' valor ( step_for )? ')' '{' comandos* '}' #comandoControleFor
                  ;
atribuicao : ID '=' expr ';'  #Atribution
           ;

expr : parentese #exprParentese
    | opnot #exprNot
    | '-' expr #exprMinus
    | v1 = expr '^' v2 = expr #exprPot
    | v1 = expr s=('*'|'/') v2 = expr #exprMultdiv
    | v1 = expr s=('+'|'-') v2 = expr #exprSomasub
    ;
opnot : '!' '(' oprelacional ')' #opnotRelacional
      | '!' fator #opnotFator
      ;
oprelacional : v1 = expr s=('=='|'!='| '&&'| '||'|'>'|'>='|'<'|'<=') v2 = expr #operadorRelacional
    | opnot #operadorRelacionalExpr
                 
 /*                 opnot #operadorRelacionalExpr
           | v1 = expr s=('=='|'!=') v2 = expr #operadorRelacionalIgualDif
            | v1 = expr s=('>'|'>='|'<'|'<=') v2 = expr #operadorRelacionalMaiorMenor
            | v1 = expr s=('&&' | '||') v2 = expr #operadorRelacionalAndOr
*/    ;
parentese : '(' expr ')' #Parens
    | fator #Fat
    ;
fator : numero #fatorNumero
    | BOOL #fatorBool // ---------------fatorBoolean
    | chamada_de_funcao_aninhada #fatorChamadaFuncaoAninhada
    | STRING #fatorString
    | identificador #fatorIdentificador
    ;

valor: INT #valorINT
    | ID #valorID
    ;
numero : NUM #Number;

identificador : ID #Id;

// some lexer rules
STRING : '"'.*?'"';
BOOL : 'TRUE'|'FALSE';
ID : [a-zA-Z][a-zA-Z0-9_]*;
NUM : [0-9]+('.'[0-9]+)?;
REAL : [0-9]+('.'[0-9]+)?;
INT : [0-9]+;
WS : [ \n\r\t]+ -> skip;
LINE_COMMENT : '//' .*? '\r'? '\n' -> skip ; // Matc h"//" stuff '\n'
COMMENT : '/*' .*? '*/' -> skip ; // Match "/*" stuff "*/"
