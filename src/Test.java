public class Test {
    public static void main(String[] args) {
//        String input = "(define (fibonacci n)";
        String input = "d ";

        Lexer lexer = new Lexer(input);

        while (lexer.hasNextToken()) {
            Token token = lexer.getNextToken();
            System.out.println(token.toString());
        }
    }
}
