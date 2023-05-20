import java.util.ArrayList;


public class Parser {
    private ArrayList<Token> tokens;
    private int currentTokenIndex;
    private Token currentToken;



    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.currentToken = tokens.get(0);
    }

    private void printGrammar(String grammarName,int tabCounter) {
        StringBuilder grammar = new StringBuilder();
        for (int i = 0; i < tabCounter; i++) {
            grammar.append("\t");
        }
        grammar.append(grammarName);
        System.out.println(grammar.toString());
    }

    private void printLexeme(int tabCounter) {
        tabCounter++;
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

    private void printSpace(int tabCounter){
        tabCounter++;
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < tabCounter; i++) {
            space.append("\t");
        }
        space.append("_____");
        System.out.println(space.toString());
    }

    public void parse() {
        program();
    }

    private void program() {
        int programTabCount = 0;

        if (currentTokenIndex < tokens.size()) {
            printGrammar("<Program>",programTabCount);
            topLevelForm(programTabCount);
            program();
        }else {
            printGrammar("<Program>",programTabCount);
            printSpace(programTabCount);
        }

    }


    private void topLevelForm(int prevTabCount) {
        int topLevelFormTabCount = prevTabCount+1;
        printGrammar("<TopLevelForm>",topLevelFormTabCount);
        match(Token.Type.LEFTPAR,topLevelFormTabCount);
        secondLevelForm(topLevelFormTabCount);
        match(Token.Type.RIGHTPAR,topLevelFormTabCount);
    }

    private void secondLevelForm(int prevTabCount) {
        int secondLevelFormTabCount = prevTabCount+1;
        printGrammar("<SecondLevelForm>",secondLevelFormTabCount);
        definition(secondLevelFormTabCount);
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,secondLevelFormTabCount);
            funCall(secondLevelFormTabCount);
            match(Token.Type.RIGHTPAR,secondLevelFormTabCount);
        }
    }

    private void definition(int prevTabCount) {
        int definitionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.DEFINE) {
            printGrammar("<Definition>",definitionTabCount);
            match(Token.Type.DEFINE,definitionTabCount);
            definitionRight(definitionTabCount);
        }
    }

    private void definitionRight(int prevTabCount) {
        int definitionRightTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            printGrammar("<DefinitionRight>",definitionRightTabCount);
            match(Token.Type.IDENTIFIER,definitionRightTabCount);
            expression(definitionRightTabCount);
        } else if (currentToken.getType() == Token.Type.LEFTPAR) {
            printGrammar("<DefinitionRight>",definitionRightTabCount);
            match(Token.Type.LEFTPAR,definitionRightTabCount);
            match(Token.Type.IDENTIFIER,definitionRightTabCount);
            argList(definitionRightTabCount);
            match(Token.Type.RIGHTPAR,definitionRightTabCount);
            statements(definitionRightTabCount);
        }
    }

    private void argList(int prevTabCount) {
        int argListTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            printGrammar("<ArgList>",argListTabCount);
            match(Token.Type.IDENTIFIER,argListTabCount);
            argList(argListTabCount);
        }else {
            printGrammar("<ArgList>",argListTabCount);
            printSpace(argListTabCount);
        }
    }

    private void statements(int prevTabCount) {
        int statementsTabCount = prevTabCount+1;
        if (currentTokenIndex < tokens.size()) {
            if (currentToken.getType() == Token.Type.RIGHTPAR) {
                return; // epsilon case
            }
            printGrammar("<Statements>",statementsTabCount);
            expression(statementsTabCount);
            definition(statementsTabCount);
            statements(statementsTabCount);
        }
    }


    private void expressions(int prevTabCount) {
        int expressionsTabCount = prevTabCount+1;
        if (currentTokenIndex < tokens.size()) {
            if (currentToken.getType() == Token.Type.RIGHTPAR) {
                printGrammar("<Expressions>",expressionsTabCount);
                printSpace(expressionsTabCount);
                return; // epsilon case
            }
            printGrammar("<Expressions>",expressionsTabCount);
            expression(expressionsTabCount);
            expressions(expressionsTabCount);
        }
    }


    private void expression(int prevTabCount) {
        int expressionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.IDENTIFIER ||
                currentToken.getType() == Token.Type.NUMBER ||
                currentToken.getType() == Token.Type.CHAR ||
                currentToken.getType() == Token.Type.BOOLEAN ||
                currentToken.getType() == Token.Type.STRING) {
            printGrammar("<Expression>",expressionTabCount);
            match(currentToken.getType(),expressionTabCount);
        } else if (currentToken.getType() == Token.Type.LEFTPAR) {
            printGrammar("<Expression>",expressionTabCount);
            match(Token.Type.LEFTPAR,expressionTabCount);
            expr(expressionTabCount);
            match(Token.Type.RIGHTPAR,expressionTabCount);
        }
    }

    private void expr(int prevTabCount) {
        int exprTabCount = prevTabCount+1;
        printGrammar("<Expr>",exprTabCount);
        letExpression(exprTabCount);
        condExpression(exprTabCount);
        ifExpression(exprTabCount);
        beginExpression(exprTabCount);
        funCall(exprTabCount);
    }

    private void funCall(int prevTabCount) {
        int funCallTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.IDENTIFIER) {
            printGrammar("<FunCall>",funCallTabCount);
            match(Token.Type.IDENTIFIER,funCallTabCount);
            expressions(funCallTabCount);
        }
    }

    private void letExpression(int prevTabCount) {
        int letExpressionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.LET) {
            printGrammar("<LetExpression>",letExpressionTabCount);
            match(Token.Type.LET,letExpressionTabCount);
            letExpr(letExpressionTabCount);
        }
    }

    private void letExpr(int prevTabCount) {
        int letExprTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            printGrammar("<LetExpr>",letExprTabCount);
            match(Token.Type.LEFTPAR,letExprTabCount);
            varDefs(letExprTabCount);
            match(Token.Type.RIGHTPAR,letExprTabCount);
            statements(letExprTabCount);
        } else if (currentToken.getType() == Token.Type.IDENTIFIER) {
            printGrammar("<LetExpr>",letExprTabCount);
            match(Token.Type.IDENTIFIER,letExprTabCount);
            match(Token.Type.LEFTPAR,letExprTabCount);
            varDefs(letExprTabCount);
            match(Token.Type.RIGHTPAR,letExprTabCount);
            statements(letExprTabCount);
        }
    }

    private void varDefs(int prevTabCount) {
        int varDefsTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            printGrammar("<VarDefs>",varDefsTabCount);
            match(Token.Type.LEFTPAR,varDefsTabCount);
            match(Token.Type.IDENTIFIER,varDefsTabCount);
            expression(varDefsTabCount);
            match(Token.Type.RIGHTPAR,varDefsTabCount);
            varDef(varDefsTabCount);
        }else {
            printSpace(varDefsTabCount-1);
        }
    }

    private void varDef(int prevTabCount) {
        int varDefTabCount = prevTabCount+1;
        printGrammar("<VarDef>",varDefTabCount);
        varDefs(varDefTabCount);

    }

    private void condExpression(int prevTabCount) {
        int condExpressionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.COND) {
            printGrammar("<CondExpression>",condExpressionTabCount);
            match(Token.Type.COND,condExpressionTabCount);
            condBranches(condExpressionTabCount);
        }
    }

    private void condBranches(int prevTabCount) {
        int condBranchesTabCount = prevTabCount+1;
        printGrammar("<CondBranches>",condBranchesTabCount);
        if (currentToken.getType() == Token.Type.LEFTPAR) {
            match(Token.Type.LEFTPAR,condBranchesTabCount);
            expression(condBranchesTabCount);
            statements(condBranchesTabCount);
            match(Token.Type.RIGHTPAR,condBranchesTabCount);
            condBranches(condBranchesTabCount);
        }
    }

    private void condBranch(int prevTabCount) {
        int condBranchTabCount = prevTabCount+1;
        printGrammar("<CondBranch>",condBranchTabCount);
        if (currentTokenIndex < tokens.size()) {
            if (currentToken.getType() == Token.Type.RIGHTPAR) {
                return; // epsilon case
            }
            match(Token.Type.LEFTPAR, condBranchTabCount);
            expression(condBranchTabCount);
            statements(condBranchTabCount);
            match(Token.Type.RIGHTPAR, condBranchTabCount);
            condBranches(condBranchTabCount);
        }
    }


    private void ifExpression(int prevTabCount) {
        int ifExpressionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.IF) {
            printGrammar("<IfExpression>",ifExpressionTabCount);
            match(Token.Type.IF,ifExpressionTabCount);
            expression(ifExpressionTabCount);
            expression(ifExpressionTabCount);
            endExpression(ifExpressionTabCount);
        }
    }

    private void endExpression(int prevTabCount) {
        int endExpressionTabCount = prevTabCount+1;
        printGrammar("<EndExpression>",endExpressionTabCount);
        if (currentTokenIndex < tokens.size()) {
            expression(endExpressionTabCount);
        }
    }


    private void beginExpression(int prevTabCount) {
        int beginExpressionTabCount = prevTabCount+1;
        if (currentToken.getType() == Token.Type.BEGIN) {
            printGrammar("<BeginExpression>",beginExpressionTabCount);
            match(Token.Type.BEGIN,beginExpressionTabCount);
            statements(beginExpressionTabCount);
        }
    }

    private void match(Token.Type expectedTokenType,int tabCount ) {
        if (currentToken.getType() == expectedTokenType) {
            advance();
        } else {
            throw new RuntimeException("Syntax error: Expected token " + expectedTokenType);
        }
        printLexeme(tabCount);
    }

    private void advance() {
        currentTokenIndex++;
        if (currentTokenIndex < tokens.size()) {
            currentToken = tokens.get(currentTokenIndex);
        } else if (currentTokenIndex == tokens.size()){
            return;
        }else{
            throw new ArrayIndexOutOfBoundsException("Hayırdır oglım!");
        }
    }

}

