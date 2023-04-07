import java.util.HashMap;

public class Lexer {
    private String input;
    private int pos;
    private Token currentToken;
    private HashMap<String, Token> keywords;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.currentToken = null;

        // Initialize keyword table
        keywords = new HashMap<String, Token>();
        keywords.put("define", new Token(Token.Type.DEFINE, "define"));
        keywords.put("let", new Token(Token.Type.LET, "let"));
        keywords.put("cond", new Token(Token.Type.COND, "cond"));
        keywords.put("if", new Token(Token.Type.IF, "if"));
        keywords.put("begin", new Token(Token.Type.BEGIN, "begin"));
        // ...
    }

    public Token getNextToken() {
        while(pos<input.length()){
            char currentChar = input.charAt(pos);

            // Check for whitespace chars
            if(Character.isWhitespace(currentChar)){
                pos++;
                continue;
            }

            // Check for comments
            if(currentChar == '~'){
                int endOfLine = input.indexOf('\n',pos);
                if(endOfLine == -1){
                    pos = input.length();
                }else {
                    pos = endOfLine + 1;
                }
                continue;
            }

            // Check for brackets
            if (currentChar == '(') {
                pos++;
                return new Token(Token.Type.LEFTPAR, "(");
            } else if (currentChar == ')') {
                pos++;
                return new Token(Token.Type.RIGHTPAR, ")");
            } else if (currentChar == '[') {
                pos++;
                return new Token(Token.Type.LEFTSQUAREB, "[");
            } else if (currentChar == ']') {
                pos++;
                return new Token(Token.Type.RIGHTSQUAREB, "]");
            } else if (currentChar == '{') {
                pos++;
                return new Token(Token.Type.LEFTCURLYB, "{");
            } else if (currentChar == '}') {
                pos++;
                return new Token(Token.Type.RIGHTCURLYB, "}");
            }

            // Check for number literals
            if (Character.isDigit(currentChar) || currentChar == '-' || currentChar == '+') {
                StringBuilder numberBuilder = new StringBuilder();
                boolean hasDecimalPoint = false;
                boolean hasExponent = false;

                // Parse the integer part
                while (pos < input.length()) {
                    char digit = input.charAt(pos);
                    if (!Character.isDigit(digit)) {
                        if (digit == '.') {
                            if (hasDecimalPoint || hasExponent) {
                                // Invalid floating-point number
                                return new Token(Token.Type.ERROR, "Invalid number literal");
                            }
                            hasDecimalPoint = true;
                        } else if (digit == 'e' || digit == 'E') {
                            if (hasExponent) {
                                // Invalid floating-point number
                                return new Token(Token.Type.ERROR, "Invalid number literal");
                            }
                            hasExponent = true;
                            numberBuilder.append(digit);
                            pos++;
                            if (pos < input.length() && (input.charAt(pos) == '-' || input.charAt(pos) == '+')) {
                                numberBuilder.append(input.charAt(pos));
                                pos++;
                            }
                            continue;
                        } else {
                            break;
                        }
                    }
                    numberBuilder.append(digit);
                    pos++;
                }

                return new Token(Token.Type.NUMBER, numberBuilder.toString());
            }

        }

        char ch = input.charAt(pos);
/*
        if (Character.isLetter(ch)) {
            // Identifier
            String lexeme = "";
            while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
                lexeme += input.charAt(pos);
                pos++;
            }
            if (keywords.containsKey(lexeme)) {
                currentToken = keywords.get(lexeme);
            } else {
                currentToken = new Token(Token.Type.IDENTIFIER, lexeme);
            }
        } else if (ch == ' ') {
            // Ignore whitespace
            pos++;
            return getNextToken();
        } else {
            // Unknown character
            currentToken = new Token(Token.Type.UNKNOWN, Character.toString(ch));
            pos++;
        }
*/
        return currentToken;
    }

    public boolean hasNextToken() {
        return pos < input.length();
    }
}