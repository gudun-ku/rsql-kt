grammar RSQLParser;

@header {
package com.beloushkin.rsql.parser;
import com.beloushkin.rsql.parser.ast.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
}

@members {
    public NodesFactory factory;
    public String unescapeQuoted(String s) {
        String content = s.substring(1, s.length() - 1);
        if (!content.contains("\\")) {
            return content;
        }
        StringBuilder sb = new StringBuilder(content.length());
        int i = 0;
        while (i < content.length()) {
            if (content.charAt(i) == '\\') {
                i++;
            }
            if (i < content.length()) {
                sb.append(content.charAt(i));
            }
            i++;
        }
        return sb.toString();
    }
}

// Parser rules
input returns [Node node]
    : or EOF { $node = $or.node; }
    ;

or returns [Node node]
    @init { List<Node> nodes = new ArrayList<>(); }
    : a=and { nodes.add($a.node); }
      ( (OR | ALT_OR) a=and { nodes.add($a.node); } )*
      { $node = nodes.size() > 1 ? factory.createLogicalNode(LogicalOperator.OR, nodes) : nodes.get(0); }
    ;

and returns [Node node]
    @init { List<Node> nodes = new ArrayList<>(); }
    : c=constraint { nodes.add($c.node); }
      ( (AND | ALT_AND) c=constraint { nodes.add($c.node); } )*
      { $node = nodes.size() > 1 ? factory.createLogicalNode(LogicalOperator.AND, nodes) : nodes.get(0); }
    ;

constraint returns [Node node]
    : g=group { $node = $g.node; }
    | n=notExpr { $node = $n.node; }
    | c=comparison { $node = $c.node; }
    ;

group returns [Node node]
    : LPAREN o=or RPAREN { $node = $o.node; }
    ;

notExpr returns [Node node]
    @init { List<Node> nodes = new ArrayList<>(1); }
    : NOT c=constraint
      { 
        nodes.add($c.node);
        $node = factory.createLogicalNode(LogicalOperator.NOT, nodes);
      }
    ;

comparison returns [ComparisonNode node]
    : sel=selector op=operator args=arguments
      { $node = factory.createComparisonNode($op.text, $sel.text, $args.list); }
    ;

selector returns [String text]
    : s=UNRESERVED_STR { $text = $s.text; }
    ;

operator returns [String text]
    : op=(COMP_FIQL | COMP_ALT) { $text = $op.text; }
    ;

arguments returns [List<String> list]
    : LPAREN csa=commaSepArguments RPAREN { $list = $csa.list; }
    | a=argument { $list = Arrays.asList($a.text); }
    ;

commaSepArguments returns [List<String> list]
    @init { $list = new ArrayList<>(); }
    : a=argument { $list.add($a.text); }
      ( OR a=argument { $list.add($a.text); } )*
    ;

argument returns [String text]
    : s=UNRESERVED_STR { $text = $s.text; }
    | s=(DOUBLE_QUOTED_STR | SINGLE_QUOTED_STR) 
      { $text = unescapeQuoted($s.text); }
    ;

// Lexer rules
AND         : ';' ;
ALT_AND     : ' and ' ;
OR          : ',' ;
ALT_OR      : ' or ' ;
NOT         : '!' ;
LPAREN      : '(' ;
RPAREN      : ')' ;
COMP_FIQL   : ('=' [a-zA-Z]* | '!') '=' ;
COMP_ALT    : ('>' | '<') '='? ;

UNRESERVED_STR    : ~['"();,=<>!~ ]+ ;
SINGLE_QUOTED_STR : '\'' (ESC | ~['\\])* '\'' ;
DOUBLE_QUOTED_STR : '"' (ESC | ~["\\])* '"' ;
fragment ESC      : '\\' . ;

WS               : [ \t\r\n]+ -> skip ;
