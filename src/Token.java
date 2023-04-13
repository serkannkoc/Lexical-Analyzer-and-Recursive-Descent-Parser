public class Token {
    public enum Type {LEFTPAR, RIGHTPAR, LEFTSQUAREB, RIGHTSQUAREB, LEFTCURLYB,RIGHTCURLYB,
        NUMBER,BOOLEAN,CHAR,STRING,DEFINE,LET,COND,IF,BEGIN,IDENTIFIER,ERROR};

    private Type type;
    private StringBuilder lexeme;
    private int position;

    public Token(Type type, StringBuilder lexeme,int position) {
        this.type = type;
        this.lexeme = new StringBuilder(lexeme);
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public StringBuilder getLexeme() {
        return lexeme;
    }
    public int getPosition() {
        return position;
    }

    public String toString() {
        return type.toString();
    }
}
