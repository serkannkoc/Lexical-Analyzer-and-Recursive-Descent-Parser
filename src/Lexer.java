import java.util.HashMap;
import java.util.HashSet;


public class Lexer {
    private String input;
    private int pos;
    private Token currentToken;
    private HashMap<String, Token> keywords;
    private HashSet<Character> digitHashSetWithOutZero;
    private HashSet<Character> digitHashSetWithZero;
    private HashSet<String> plusMinusHashSet;
    private HashSet<Character> eEHashSet;
    private HashSet<Integer> binaryDigitHashSet;
    private HashSet<Character> hexadecimalHashSet;
    private HashSet<Character> customHashSet;
    private HashSet<Character> customHashSet2;


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
                    if ((currentChar == '+') || (currentChar == '-')) {
                        pos++;
                        state = 2;
                        lexeme.append(currentChar);
                    } else if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 3;
                        lexeme.append(currentChar);
                    }
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

        return currentToken;
    }

    public boolean hasNextToken() {
        return pos < input.length();
    }
}