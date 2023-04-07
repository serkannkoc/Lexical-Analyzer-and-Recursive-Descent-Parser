public class Token {
    public enum Type {LEFTPAR, RIGHTPAR, LEFTSQUAREB, RIGHTSQUAREB, LEFTCURLYB,RIGHTCURLYB,
        NUMBER,BOOLEAN,CHAR,STRING,DEFINE,LET,COND,IF,BEGIN,IDENTIFIER,ERROR};

    private Type type;
    private String lexeme;

    public Token(Type type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public Type getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String toString() {
        return type.toString() + ": " + lexeme;
    }
}
