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
        keywords.put("define", new Token(Token.Type.DEFINE, new StringBuilder("define")));
        keywords.put("let", new Token(Token.Type.LET, new StringBuilder("let")));
        keywords.put("cond", new Token(Token.Type.COND, new StringBuilder("cond")));
        keywords.put("if", new Token(Token.Type.IF, new StringBuilder("if")));
        keywords.put("begin", new Token(Token.Type.BEGIN, new StringBuilder("begin")));
        // ...

        digitHashSetWithOutZero = new HashSet<>();
        for (char c = '1'; c <= '9'; c++) {
            digitHashSetWithOutZero.add(c);
        }
        digitHashSetWithZero = new HashSet<>();
        for (char c = '0'; c <= '9'; c++) {
            digitHashSetWithOutZero.add(c);
        }
        plusMinusHashSet = new HashSet<>();
        plusMinusHashSet.add("+");
        plusMinusHashSet.add("-");

        eEHashSet = new HashSet<>();
        eEHashSet.add('e');
        eEHashSet.add('E');

        binaryDigitHashSet = new HashSet<>();
        binaryDigitHashSet.add(0);
        binaryDigitHashSet.add(1);

        hexadecimalHashSet = new HashSet<>();
        hexadecimalHashSet.add('0');
        hexadecimalHashSet.add('1');
        hexadecimalHashSet.add('2');
        hexadecimalHashSet.add('3');
        hexadecimalHashSet.add('4');
        hexadecimalHashSet.add('5');
        hexadecimalHashSet.add('6');
        hexadecimalHashSet.add('7');
        hexadecimalHashSet.add('8');
        hexadecimalHashSet.add('9');
        hexadecimalHashSet.add('A');
        hexadecimalHashSet.add('B');
        hexadecimalHashSet.add('C');
        hexadecimalHashSet.add('D');
        hexadecimalHashSet.add('E');
        hexadecimalHashSet.add('F');

        customHashSet = new HashSet<>();
        customHashSet.add('!');
        customHashSet.add('*');
        customHashSet.add('/');
        customHashSet.add(':');
        customHashSet.add('<');
        customHashSet.add('=');
        customHashSet.add('>');
        customHashSet.add('?');
        for (char c = 'a'; c <= 'z'; c++) {
            if (c != 'f' && c != 't' && c != 'd' && c != 'l' && c != 'c' && c != 'i' && c != 'b') {
                customHashSet.add(c);
            }
        }

        customHashSet2 = new HashSet<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            customHashSet2.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            customHashSet2.add(c);
        }
        for (char c = '0'; c <= '9'; c++) {
            customHashSet2.add(c);
        }
        customHashSet2.add('.');
        customHashSet2.add('+');
        customHashSet2.add('-');

    }

    public Token getNextToken() {

        int state = 0;
        StringBuilder lexeme = new StringBuilder("");
        char currentChar = ' ';

        while (pos < input.length()) {
            currentChar = input.charAt(pos);

            switch (state) {
                case 0: // initial state
                    if (currentChar == 'd') {
                        pos++;
                        state = 52;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'l') {
                        pos++;
                        state = 48;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'c') {
                        pos++;
                        state = 59;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'i') {
                        pos++;
                        state = 64;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'b') {
                        pos++;
                        state = 67;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'f') {
                        pos++;
                        state = 47;
                        lexeme.append(currentChar);
                    } else if (currentChar == '0') {
                        pos++;
                        state = 10;
                        lexeme.append(currentChar);
                    } else if (digitHashSetWithOutZero.contains(currentChar)) {
                        pos++;
                        state = 9;
                        lexeme.append(currentChar);
                    } else if ((currentChar == '+') || (currentChar == '-')) {
                        pos++;
                        state = 8;
                        lexeme.append(currentChar);
                    } else if (currentChar == '"') {
                        pos++;
                        state = 41;
                        lexeme.append(currentChar);
                    } else if (currentChar == '\'') {
                        pos++;
                        state = 39;
                        lexeme.append(currentChar);
                    } else if (currentChar == 't') {
                        pos++;
                        state = 26;
                        lexeme.append(currentChar);
                    } else if (currentChar == '.') {
                        pos++;
                        state = 7;
                        lexeme.append(currentChar);
                    } else if (customHashSet.contains(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }

//                    } else if (currentChar == '~') {
//                        state = 1; // switch to comment state
//                    } else if (currentChar == '(') {
//                        pos++;
//                        return new Token(Token.Type.LEFTPAR, "(");
//                    } else if (currentChar == ')') {
//                        pos++;
//                        return new Token(Token.Type.RIGHTPAR, ")");
//                    } else if (currentChar == '[') {
//                        pos++;
//                        return new Token(Token.Type.LEFTSQUAREB, "[");
//                    } else if (currentChar == ']') {
//                        pos++;
//                        return new Token(Token.Type.RIGHTSQUAREB, "]");
//                    } else if (currentChar == '{') {
//                        pos++;
//                        return new Token(Token.Type.LEFTCURLYB, "{");
//                    } else if (currentChar == '}') {
//                        pos++;
//                        return new Token(Token.Type.RIGHTCURLYB, "}");
//                    } else if (Character.isDigit(currentChar)) {
//                        lexeme += currentChar;
//                        state = 2; // switch to number state
//                    } else if (currentChar == '\'') {
//                        state = 3; // switch to character literal state
//                    } else if (currentChar == '\"') {
//                        state = 4; // switch to string literal state
//                    } else if (Character.isLetter(currentChar)) {
//                        lexeme += currentChar;
//                        state = 5; // switch to identifier or keyword state
//                    } else {
//                        // invalid character
//                        pos++;
//                        return new Token(Token.Type.ERROR, Character.toString(currentChar));
//                    }
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
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 3;
                        lexeme.append(currentChar);
                    }
                    break;

                case 3: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 3;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        // pos++ yok
                        state = 5;
                        lexeme.append(currentChar);
                    }
                    break;
                case 4: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 4;
                        lexeme.append(currentChar);
                    } else if (eEHashSet.contains(currentChar)) {
                        pos++;
                        state = 1;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        //pos++ yok
                        state = 5;
                        lexeme.append(currentChar);
                    }
                    break;

                case 5: // finish floating point
                    pos++;
                    currentToken = new Token(Token.Type.IDENTIFIER, lexeme);
                    state = 0;
                    break;

                case 6:
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 4;
                        lexeme.append(currentChar);
                    }
                    break;

                case 7: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 4;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 21;
                        lexeme.append(currentChar);
                    }
                    break;

                case 8: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 11;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 21;
                        lexeme.append(currentChar);
                    } else if (currentChar == '.') {
                        pos++;
                        state = 6;
                        lexeme.append(currentChar);
                    }
                    break;

                case 9: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 9;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 12;
                        lexeme.append(currentChar);
                    } else if (currentChar == '.') {
                        pos++;
                        state = 6;
                        lexeme.append(currentChar);
                    } else if (eEHashSet.contains(currentChar)) {
                        pos++;
                        state = 22;
                        lexeme.append(currentChar);
                    }
                    break;

                case 10:
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 13;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 12;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'x') {
                        pos++;
                        state = 14;
                        lexeme.append(currentChar);
                    } else if (currentChar == 'b') {
                        pos++;
                        state = 17;
                        lexeme.append(currentChar);
                    } else if (eEHashSet.contains(currentChar)) {
                        pos++;
                        state = 22;
                        lexeme.append(currentChar);
                    }
                    break;

                case 11: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 11;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 12;
                        lexeme.append(currentChar);
                    } else if (currentChar == '.') {
                        pos++;
                        state = 6;
                        lexeme.append(currentChar);
                    } else if (eEHashSet.contains(currentChar)) {
                        pos++;
                        state = 22;
                        lexeme.append(currentChar);
                    }
                    break;
                //todo eksik
                case 12: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state==12;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        // state = 12;
                        lexeme.append(currentChar);
                    }
                    break;

                case 13: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 13;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        pos++;
                        state = 12;
                        lexeme.append(currentChar);
                    } else if (currentChar == '.') {
                        pos++;
                        state = 6;
                        lexeme.append(currentChar);
                    } else if (eEHashSet.contains(currentChar)) {
                        pos++;
                        state = 22;
                        lexeme.append(currentChar);
                    }
                    break;

                case 14: // character literal state
                    if (hexadecimalHashSet.contains(currentChar)) {
                        pos++;
                        state = 15;
                        lexeme.append(currentChar);
                    }
                    break;

                case 15: // character literal state
                    if (hexadecimalHashSet.contains(currentChar)) {
                        pos++;
                        //state = 15;
                        lexeme.append(currentChar);
                    } else if (currentChar == '0') {
                        //pos++;
                        state = 16;
                        lexeme.append(currentChar);
                    }
                    break;

                case 16: // finish hex state
                    pos++;
                    currentToken = new Token(Token.Type.IDENTIFIER, lexeme);
                    state = 0;
                    break;

                case 17: // character literal state
                    if (binaryDigitHashSet.contains(currentChar)) {
                        pos++;
                        state = 18;
                        lexeme.append(currentChar);
                    }
                    break;

                case 18: // character literal state
                    if (binaryDigitHashSet.contains(currentChar)) {
                        pos++;
                        //state = 18;
                        lexeme.append(currentChar);
                    } else if (currentChar == ' ') {
                        // pos++;
                        state = 19;
                        lexeme.append(currentChar);
                    }
                    break;

                case 19: // finish binary state
                    pos++;
                    currentToken = new Token(Token.Type.IDENTIFIER, lexeme);
                    state = 0;
                    break;


                case 52:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 53;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme);
                        }
                    } else if (Character.isWhitespace(currentChar)) {

                        state = 21;
                        lexeme.append(currentChar);
                    }
                    break;
                case 53:
                    if (Character.isWhitespace(currentChar)) {

                        state = 21;
                    }
                    break;
                case 21:
                    pos++;
                    currentToken = new Token(Token.Type.IDENTIFIER, lexeme);
                    break;
            }
        }

        return currentToken;
    }

    public boolean hasNextToken() {
        return pos < input.length();
    }
}