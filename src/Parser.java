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

    private void printGrammar(int count, String grammarName) {
        StringBuilder grammar = new StringBuilder();
        for (int i = 0; i < count; i++) {
            grammar.append("\t");
        }
        grammar.append(grammarName);
        System.out.println(grammar.toString());
        tabCounter++;
    }

    private void printLexeme(int count) {
        StringBuilder lexeme = new StringBuilder();
        for (int i = 0; i < count; i++) {
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

    private void program() {
        if (currentTokenIndex >= tokens.size()) {
            return; // epsilon case (end of token list)
        }
        printGrammar(tabCounter, "<Program>");
        topLevelForm();
        program();
    }

    private void topLevelForm() {
        printGrammar(tabCounter, "<TopLevelForm>");
        match(Token.Type.LEFTPAR);
        tabCounter++;
        printLexeme(tabCounter);
        secondLevelForm();
        match(Token.Type.RIGHTPAR);
        tabCounter--;
        printLexeme(tabCounter);
    }

    private void secondLevelForm() {
        printGrammar(tabCounter, "<SecondLevelForm>");
        definition();
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR);
            tabCounter++;
            printLexeme(tabCounter);
            funCall();
            match(Token.Type.RIGHTPAR);
            tabCounter--;
            printLexeme(tabCounter);
        }
    }

    private void definition() {
        if (currentToken.getType() == Token.Type.DEFINE) {
            printGrammar(tabCounter, "<Definition>");
            match(Token.Type.DEFINE);
            tabCounter++;
            printLexeme(tabCounter);
            definitionRight();
        }
    }

    private void definitionRight() {
        printGrammar(tabCounter, "<DefinitionRight>");
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR);
            tabCounter++;
            printLexeme(tabCounter);
            match(Token.Type.IDENTIFIER);
            printLexeme(tabCounter);
            argList();
            match(Token.Type.RIGHTPAR);
            tabCounter--;
            printLexeme(tabCounter);
            statements();
        } else if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER);
            tabCounter++;
            printLexeme(tabCounter);
            expression();
        }
    }

    private void argList() {
        printGrammar(tabCounter, "<ArgList>");
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER);
            tabCounter++;
            printLexeme(tabCounter);
            argList();
        }

    }

    private void statements() {
        expression();
        /*if (currentToken.getType() == TokenType.LEFTPAR ||
                currentToken.getType() == TokenType.IDENTIFIER ||
                currentToken.getType() == TokenType.NUMBER ||
                currentToken.getType() == TokenType.CHAR ||
                currentToken.getType() == TokenType.BOOLEAN ||
                currentToken.getType() == TokenType.STRING) {
            expression();
        } else*/
        if (currentToken.getType() == TokenType.DEFINE) {
            definition();
            statements();
        } else {
            // Handle syntax error
        }
    }

    private void expression() {
        if (currentToken.getType() == Token.Type.IDENTIFIER ||
                currentToken.getType() == Token.Type.NUMBER ||
                currentToken.getType() == Token.Type.CHAR ||
                currentToken.getType() == Token.Type.BOOLEAN ||
                currentToken.getType() == Token.Type.STRING) {
            System.out.println("<Expression>");

            match(currentToken.getType());
            System.out.println(tokens.get(currentTokenIndex - 1).getType() + "(" + tokens.get(currentTokenIndex - 1).getLexeme() + ")");
        } else if (currentToken.getType() == Token.Type.LEFTPAR) {
            System.out.println("<Expression>");

            match(Token.Type.LEFTPAR);
            System.out.println("LEFTPAR" + "(" + tokens.get(currentTokenIndex - 1).getLexeme() + ")");
            expr();
            match(Token.Type.RIGHTPAR);
        } else {
            // Handle syntax error
        }
    }

    private void expr() {
        letExpression();
        condExpression();
        ifExpression();
        beginExpression();
        funCall();
    }

    private void expressions() {
        expression();
        expressions();
    }

    private void letExpression() {
        if (currentToken.getType() == Token.Type.LET) {
            match(Token.Type.LET);
            letExpr();
        }
    }

    private void beginExpression() {
        if (currentToken.getType() == Token.Type.BEGIN) {
            match(Token.Type.BEGIN);
            statements();
        }
    }

    private void funCall() {
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            match(Token.Type.IDENTIFIER);
            expressions();
        }
    }

    private void condExpression() {
        if (currentToken.getType() == Token.Type.COND) {
            match(Token.Type.COND);
            condBranches();
        }
    }

    private void ifExpression() {
        if (currentToken.getType() == Token.Type.IF) {
            match(Token.Type.IF);
            expression();
            expression();
            endExpression();
        }
    }

    private void letExpr() {
        if (currentToken.getType() == TokenType.LEFTPAR) {
            match(TokenType.LEFTPAR);
            varDefs();
            match(TokenType.RIGHTPAR);
            statements();
        } else if (currentToken.getType() == TokenType.IDENTIFIER) {
            match(TokenType.IDENTIFIER);
            match(TokenType.LEFTPAR);
            varDefs();
            match(TokenType.RIGHTPAR);
            statements();
        } else {
            // Handle syntax error
        }
    }

    private void varDefs() {
        if (currentToken.getType() == TokenType.LEFTPAR) {
            match(TokenType.LEFTPAR);
            match(TokenType.IDENTIFIER);
            expression();
            match(TokenType.RIGHTPAR);
            varDef();
        }
    }

    private void varDef() {
        if (currentToken.getType() == TokenType.LEFTPAR) {
            varDefs();
        }
    }


    private void condBranches() {
        if (currentToken.getType() == TokenType.LEFTPAR) {
            match(TokenType.LEFTPAR);
            expression();
            statements();
            match(TokenType.RIGHTPAR);
            condBranches();
        }
    }


    private void endExpression() {
        if (currentToken.getType() == TokenType.IDENTIFIER ||
                currentToken.getType() == TokenType.NUMBER ||
                currentToken.getType() == TokenType.CHAR ||
                currentToken.getType() == TokenType.BOOLEAN ||
                currentToken.getType() == TokenType.STRING ||
                currentToken.getType() == TokenType.LEFTPAR) {
            expression();
        }
    }


    private void match(Token.Type expectedTokenType) {
        if (currentToken.getType() == expectedTokenType) {
            advance();
        } else {
            throw new RuntimeException("Syntax error: Expected token " + expectedTokenType);
        }
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

