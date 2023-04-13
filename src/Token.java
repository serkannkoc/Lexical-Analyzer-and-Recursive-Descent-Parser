public class Token {
    public enum Type {LEFTPAR, RIGHTPAR, LEFTSQUAREB, RIGHTSQUAREB, LEFTCURLYB,RIGHTCURLYB,
        NUMBER,BOOLEAN,CHAR,STRING,DEFINE,LET,COND,IF,BEGIN,IDENTIFIER,ERROR};

    private Type type;
    private StringBuilder lexeme;

    public Token(Type type, StringBuilder lexeme) {
        this.type = type;
        this.lexeme = new StringBuilder(lexeme);
    }

    public Type getType() {
        return type;
    }

    public StringBuilder getLexeme() {
        return lexeme;
    }

    public String toString() {
        return type.toString() + ": " + lexeme;
    }
}
