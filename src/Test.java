import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
//        String input = "(define (fibonacci n)";
        String filePath = "input.txt"; // Replace with the path to your text file
        ArrayList<Token> tokenList = new ArrayList<Token>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount= 1;
            while ((line = reader.readLine()) != null) {
                // Process the line, e.g. store it in a variable
                Lexer lexer = new Lexer(line);

                while (lexer.hasNextToken()) {
                    Token token = lexer.getNextToken();
                    tokenList.add(token);
                    if(token.getType() == Token.Type.ERROR){
                        System.out.println("LEXICAL ERROR"+"["+lineCount+":"+(token.getPosition()+1)+"]: Invalid token = "+ token.getLexeme());
                        System.exit(0);
                    }
                    System.out.println(token.toString()+" "+lineCount+":"+(token.getPosition()+1));
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parser parser = new Parser(tokenList);
        parser.parse();


    }
}
