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

        int state = 0;
        String lexeme = "";
        char currentChar = ' ';

        while (pos < input.length()) {
            currentChar = input.charAt(pos);

            switch (state) {
                case 0: // initial state
                    if (Character.isWhitespace(currentChar)) {
                        pos++;
                    } else if (currentChar == '~') {
                        state = 1; // switch to comment state
                    } else if (currentChar == '(') {
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
                    } else if (Character.isDigit(currentChar)) {
                        lexeme += currentChar;
                        state = 2; // switch to number state
                    } else if (currentChar == '\'') {
                        state = 3; // switch to character literal state
                    } else if (currentChar == '\"') {
                        state = 4; // switch to string literal state
                    } else if (Character.isLetter(currentChar)) {
                        lexeme += currentChar;
                        state = 5; // switch to identifier or keyword state
                    } else {
                        // invalid character
                        pos++;
                        return new Token(Token.Type.ERROR, Character.toString(currentChar));
                    }
                    break;

                case 1: // comment state
                    if (currentChar == '\n') {
                        state = 0; // switch back to initial state
                    }
                    pos++;
                    break;

                case 2: // number state
                    if (Character.isDigit(currentChar) || currentChar == '.' || currentChar == 'e' || currentChar == 'E'
                            || currentChar == '+' || currentChar == '-') {
                        lexeme += currentChar;
                        pos++;
                    } else {
                        // number lexeme is complete
                        return new Token(Token.Type.NUMBER, lexeme);
                    }
                    break;

                case 3: // character literal state
                    if (currentChar == '\\') {
                        // escape character
                        if (pos + 1 < input.length() && input.charAt(pos + 1) == '\'') {
                            lexeme += "\'";
                            pos += 2;
                        } else {
                            pos++;
                            return new Token(Token.Type.ERROR, lexeme + currentChar);
                        }
                    } else if (currentChar == '\'') {
                        // character literal lexeme is complete
                        if (lexeme.length() != 1) {
                            pos++;
                            return new Token(Token.Type.ERROR, lexeme);
                        } else {
                            pos++;
                            return new Token(Token.Type.CHAR, lexeme);
                        }
                    } else {
                        lexeme += currentChar;
                        pos++;
                    }
                    break;
            }
        }

        /*****************************************

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
        /*****************************************
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