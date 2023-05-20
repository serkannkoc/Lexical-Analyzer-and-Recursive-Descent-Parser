import java.util.ArrayList;


public class Parser {
    private ArrayList<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;
    private int tabCounter;


    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.tabCounter = 0;
        this.currentToken = tokens.get(0);
    }

    private void printGrammar(String grammarName) {
        StringBuilder grammar = new StringBuilder();
        for (int i = 0; i < tabCounter; i++) {
            grammar.append("\t");
        }
        grammar.append(grammarName);
        System.out.println(grammar.toString());
        tabCounter++;
    }

    private void printLexeme(boolean changeTabCounter) {
        if(changeTabCounter){
            if (tokens.get(currentTokenIndex-1).getType() == Token.Type.RIGHTPAR){
                tabCounter--;
            }else {
                tabCounter++;
            }
        }
        StringBuilder lexeme = new StringBuilder();
        for (int i = 0; i < tabCounter; i++) {
            lexeme.append("\t");
        }
        lexeme.append(tokens.get(currentTokenIndex - 1).getType());
        lexeme.append(" (");
        lexeme.append(tokens.get(currentTokenIndex - 1).getLexeme());
        lexeme.append(")");
        System.out.println(lexeme.toString());
    }

    public void parse() {
        program();
    }

    private void program() { // TODO: Epsilon eklenecek
        printGrammar("<Program>");
//        if (currentTokenIndex >= tokens.size()) {
//            return; // epsilon case (end of token list)
//        }
        topLevelForm();
        program();
    }

    private void topLevelForm() {
        printGrammar("<TopLevelForm>");
        match(Token.Type.LEFTPAR,true);
        secondLevelForm();
        match(Token.Type.RIGHTPAR,true);
    }

    private void secondLevelForm() {
        printGrammar("<SecondLevelForm>");
        definition();
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            funCall();
            match(Token.Type.RIGHTPAR,true);
        }
    }

    private void definition() {
        printGrammar("<Definition>");
        if (currentToken.getType() == Token.Type.DEFINE) {
            match(Token.Type.DEFINE,true);
            definitionRight();
        }
    }

    private void definitionRight() {
        printGrammar("<DefinitionRight>");
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER,true);
            expression();
        } else if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            match(Token.Type.IDENTIFIER,false);
            argList();
            match(Token.Type.RIGHTPAR,true);
            statements();
        }
    }

    private void argList() { // TODO: Epsilon
        printGrammar("<ArgList>");
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER,true);
            argList();
        }

    }

    private void statements() { // TODO: Sıkıntılı
        printGrammar("<Statements>");
        expression();
        definition();
        statements();

    }

    private void expressions() { // TODO: Epsilon
        printGrammar("<Expressions>");
        expression();
        expressions();
    }

    private void expression() {
        printGrammar("<Expression>");

        if (currentToken.getType() == Token.Type.IDENTIFIER ||
                currentToken.getType() == Token.Type.NUMBER ||
                currentToken.getType() == Token.Type.CHAR ||
                currentToken.getType() == Token.Type.BOOLEAN ||
                currentToken.getType() == Token.Type.STRING) {
            match(currentToken.getType(),true);
        } else if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            expr();
            match(Token.Type.RIGHTPAR,true);
        }
    }

    private void expr() {
        printGrammar("<Expr>");
        letExpression();
        condExpression();
        ifExpression();
        beginExpression();
        funCall();
    }

    private void funCall() {
        printGrammar("<FunCall>");
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER,true);
            expressions();
        }
    }

    private void letExpression() {
        printGrammar("<LetExpression>");
        if (currentToken.getType() == Token.Type.LET) {
            match(Token.Type.LET,true);
            letExpr();
        }
    }

    private void letExpr() {
        printGrammar("<LetExpression>");
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            varDefs();
            match(Token.Type.RIGHTPAR,true);
            statements();
        } else if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER,true);
            match(Token.Type.LEFTPAR,false);
            varDefs();
            match(Token.Type.RIGHTPAR,true);
            statements();
        }
    }

    private void varDefs() {
        printGrammar("<VarDefs>");
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            match(Token.Type.IDENTIFIER,false);
            expression();
            match(Token.Type.RIGHTPAR,true);
            varDef();
        }
    }

    private void varDef() { // TODO: Epsilon eklenecek
        printGrammar("<VarDef>");
        varDefs();

    }

    private void condExpression() {
        printGrammar("<CondExpression>");
        if (currentToken.getType() == Token.Type.COND) {
            match(Token.Type.COND,true);
            condBranches();
        }
    }

    private void condBranches() {
        printGrammar("<CondBranches>");
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            expression();
            statements();
            match(Token.Type.RIGHTPAR,true);
            condBranches();
        }
    }

    private void condBranch() { // TODO: Epsilon eklenecek
        printGrammar("<CondBranch>");
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,true);
            expression();
            statements();
            match(Token.Type.RIGHTPAR,true);
        }
    }

    private void ifExpression() {
        printGrammar("<IfExpression>");
        if (currentToken.getType() == Token.Type.IF) {
            match(Token.Type.IF,true);
            expression();
            expression();
            endExpression();
        }
    }

    private void endExpression() { // TODO: Epsilon eklenecek
        printGrammar("<EndExpression>");
        expression();
    }

    private void beginExpression() {
        printGrammar("<BeginExpression>");
        if (currentToken.getType() == Token.Type.BEGIN) {
            match(Token.Type.BEGIN,true);
            statements();
        }
    }

    private void match(Token.Type expectedTokenType,boolean changeTabCounter ) {
        if (currentToken.getType() == expectedTokenType) {
            advance();
        } else {
            throw new RuntimeException("Syntax error: Expected token " + expectedTokenType);
        }
        printLexeme(changeTabCounter);
    }

    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else {
            throw new ArrayIndexOutOfBoundsException("Hayırdır oglım!");
        }
    }

}

