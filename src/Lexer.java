import java.util.HashMap;
import java.util.HashSet;


public class Lexer {
    private String input;
    private int pos;
    private Token currentToken;
    private int index;
    private HashSet<Character> digitHashSetWithOutZero;
    private HashSet<Character> digitHashSetWithZero;
    private HashSet<String> plusMinusHashSet;
    private HashSet<Character> eEHashSet;
    private HashSet<Integer> binaryDigitHashSet;
    private HashSet<Character> hexadecimalHashSet;
    private HashSet<Character> customHashSet;
    private HashSet<Character> customHashSet2;
    private HashSet<Character> bracketsSet;

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
        this.currentToken = null;
        this.index = 1;



        digitHashSetWithOutZero = new HashSet<>();
        for (char c = '1'; c <= '9'; c++) {
            digitHashSetWithOutZero.add(c);
        }
        digitHashSetWithZero = new HashSet<>();
        for (char c = '0'; c <= '9'; c++) {
            digitHashSetWithZero.add(c);
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

        bracketsSet = new HashSet<>();
        bracketsSet.add('(');
        bracketsSet.add(')');
        bracketsSet.add('[');
        bracketsSet.add(']');
        bracketsSet.add('{');
        bracketsSet.add('}');
    }

    public Token getNextToken() {

        int state = 0;
        StringBuilder lexeme = new StringBuilder("");
        char currentChar = ' ';
        index = pos;

        while (pos < input.length() ) {
            currentChar = input.charAt(pos);
//            if(currentToken != null)
//                break;

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
                    } else if (currentChar == '(') {
                        pos++;
                        return new Token(Token.Type.LEFTPAR, new StringBuilder("("),index);
                    } else if (currentChar == ')') {
                        pos++;
                        return new Token(Token.Type.RIGHTPAR, new StringBuilder(")"),index);
                    } else if (currentChar == '[') {
                        pos++;
                        return new Token(Token.Type.LEFTSQUAREB, new StringBuilder("["),index);
                    } else if (currentChar == ']') {
                        pos++;
                        return new Token(Token.Type.RIGHTSQUAREB, new StringBuilder("]"),index);
                    } else if (currentChar == '{') {
                        pos++;
                        return new Token(Token.Type.LEFTCURLYB, new StringBuilder("{"),index);
                    } else if (currentChar == '}') {
                        pos++;
                        return new Token(Token.Type.RIGHTCURLYB, new StringBuilder("}"),index);
                    } else if (currentChar == ' '){
                        pos++;
                        index = pos;
                    }
//                    } else if (currentChar == '~') {
//                        state = 1; // switch to comment state
//                    }
//                    }   else if (Character.isDigit(currentChar)) {
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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 5;

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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 5;

                    }
                    break;

                case 5: // finish floating point
                    pos++;
                    return new Token(Token.Type.NUMBER, lexeme,index);

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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 8: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 11;
                        lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 12;

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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 12;

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
                    } else {
                        return new Token(Token.Type.ERROR,lexeme,index);
                    }
                    break;

                case 11: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 11;
                        lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 12;

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
                        char nextChar = input.charAt(pos);
                        if(Character.isDigit(nextChar))
                            lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 73;
                    }
                    break;

                case 13: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 13;
                        lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 12;

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
                    return new Token(Token.Type.NUMBER, lexeme,index);

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
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 19;

                    }
                    break;

                case 19: // finish binary state
                    pos++;
                    return new Token(Token.Type.NUMBER, lexeme,index);

                case 20: // character literal state
                    if (customHashSet2.contains(currentChar) || (currentChar == '|')) {
                        pos++;
                        //state = 20;
                        lexeme.append(currentChar);
                        if(pos == input.length()){
                            return new Token(Token.Type.IDENTIFIER,lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 21: //identifier finish state
                    pos++;
                    return new Token(Token.Type.IDENTIFIER, lexeme,index);

                case 22: // character literal state
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 24;
                        lexeme.append(currentChar);
                    } else if ((currentChar == '+') || (currentChar == '-')) {
                        pos++;
                        state = 23;
                        lexeme.append(currentChar);
                    }
                    break;

                case 23:
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        state = 24;
                        lexeme.append(currentChar);
                    }
                    break;

                case 24:
                    if (digitHashSetWithZero.contains(currentChar)) {
                        pos++;
                        //state = 24;
                        lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 25;

                    }
                    break;

                case 25: // finish state
                    pos++;
                    return new Token(Token.Type.NUMBER, lexeme,index);


                case 26:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'a') {
                            lexeme.append(currentChar);
                            state = 27;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 27:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'l') {
                            lexeme.append(currentChar);
                            state = 29;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 28:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'u') {
                            lexeme.append(currentChar);
                            state = 30;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 29:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 's') {
                            lexeme.append(currentChar);
                            state = 32;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 30:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 31;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 31: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 34;

                } else if (Character.isLetter(currentChar)) {
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 32:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 33;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 33: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 34;

                } else if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 34: // boolean finish state
                    pos++;
                    return new Token(Token.Type.BOOLEAN, lexeme,index);


                case 35: // character literal state
                    if (currentChar == '\'') {
                        //pos++;
                        state = 38;
                        lexeme.append(currentChar);
                    }
                    break;

                case 36: // character literal state
                    if (currentChar == '\'') { // "single quote"
                        pos++;
                        state = 35;
                        lexeme.append(currentChar);
                    } else if (currentChar == '\\') {
                        pos++;
                        state = 37;
                        lexeme.append(currentChar);
                    }
                    break;

                case 37:
                    if (currentChar == '\'') {
                        //pos++;
                        state = 38;
                        lexeme.append(currentChar);
                    }
                    break;

                case 38: // character finish state
                    pos++;
                    return new Token(Token.Type.CHAR, lexeme,index);

                case 39: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 36;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        state = 40;
                        lexeme.append(currentChar);
                    }
                    break;

                case 40: // character literal state
                    if (currentChar == '\'') { //  '\'' -> '
                        //pos++;
                        state = 38;
                        lexeme.append(currentChar);
                    }
                    break;

                case 41: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 43;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        state = 42;
                        lexeme.append(currentChar);
                    }
                    break;

                case 42: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 43;
                        lexeme.append(currentChar);
                    } else if (currentChar == '"') {
                        //pos++;
                        state = 46;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        //state = 42;
                        lexeme.append(currentChar);
                    }
                    break;

                case 43: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 45;
                        lexeme.append(currentChar);
                    } else if (currentChar == '\'') {
                        pos++;
                        state = 44;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        //state = 42;
                        lexeme.append(currentChar);
                    }
                    break;

                case 44: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 43;
                        lexeme.append(currentChar);
                    } else if (currentChar == '"') {
                        //pos++;
                        state = 46;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        state = 42;
                        lexeme.append(currentChar);
                    }
                    break;

                case 45: // character literal state
                    if (currentChar == '\\') {
                        pos++;
                        state = 43;
                        lexeme.append(currentChar);
                    } else if (currentChar == '"') {
                        //pos++;
                        state = 46;
                        lexeme.append(currentChar);
                    } else if (Character.isUnicodeIdentifierPart(currentChar)) {
                        // current character is a Unicode character
                        // do something here
                        pos++;
                        state = 42;
                        lexeme.append(currentChar);
                    }
                    break;

                case 46: // character finish state
                    pos++;
                    return new Token(Token.Type.STRING, lexeme,index);


                case 47:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'r') {
                            lexeme.append(currentChar);
                            state = 28;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 48:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 49;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 49:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 't') {
                            lexeme.append(currentChar);
                            state = 50;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 50: // character literal state
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 51;

                    }
                    break;

                case 51: // finish floating point
                    pos++;
                    return new Token(Token.Type.LET, lexeme,index);

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
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 53:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'f') {
                            lexeme.append(currentChar);
                            state = 54;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 54:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'i') {
                            lexeme.append(currentChar);
                            state = 55;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 55:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'n') {
                            lexeme.append(currentChar);
                            state = 56;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 56:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 57;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 57: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                    if (bracketsSet.contains(currentChar))
                        pos--;
                    state = 58;

                } else if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 58: // finish floating point
                    pos++;
                    return new Token(Token.Type.DEFINE, lexeme,index);

                case 59:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'o') {
                            lexeme.append(currentChar);
                            state = 60;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 60:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'n') {
                            lexeme.append(currentChar);
                            state = 61;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 61:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'd') {
                            lexeme.append(currentChar);
                            state = 62;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 62: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                    if (bracketsSet.contains(currentChar))
                        pos--;
                    state = 63;

                } else if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 63: // cond finish state
                    pos++;
                    return new Token(Token.Type.COND, lexeme,index);


                case 64:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'f') {
                            lexeme.append(currentChar);
                            state = 65;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 65: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                    if (bracketsSet.contains(currentChar))
                        pos--;
                    state = 66;

                } else if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 66: // cond finish state
                    pos++;
                    return new Token(Token.Type.IF, lexeme,index);


                case 67:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'e') {
                            lexeme.append(currentChar);
                            state = 68;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;


                case 68:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'g') {
                            lexeme.append(currentChar);
                            state = 69;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 69:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'i') {
                            lexeme.append(currentChar);
                            state = 70;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 70:
                    if (Character.isLetter(currentChar)) {
                        pos++;
                        if (currentChar == 'n') {
                            lexeme.append(currentChar);
                            state = 71;
                        } else if (Character.isLetter(currentChar)) {
                            lexeme.append(currentChar);
                            state = 20;
                        } else {
                            return new Token(Token.Type.ERROR, lexeme,index);
                        }
                    } else if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                        if (bracketsSet.contains(currentChar))
                            pos--;
                        state = 21;

                    }
                    break;

                case 71: // character literal state
                    if (Character.isWhitespace(currentChar) || bracketsSet.contains(currentChar)) {
                    if (bracketsSet.contains(currentChar))
                        pos--;
                    state = 72;

                } else if (Character.isLetter(currentChar)) {
                        pos++;
                        state = 20;
                        lexeme.append(currentChar);
                    }
                    break;

                case 72: // begin finish state
                    pos++;
                    return new Token(Token.Type.BEGIN, lexeme,index);
                case 73:
                    pos++;
                    return new Token(Token.Type.NUMBER,lexeme,index);
            }
        }

        return currentToken;
    }

    public boolean hasNextToken() {
        return pos < input.length();
    }
}