package libs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTokenizer implements Tokenizer {

    public static Tokenizer createSimpleTokenizer(String filename, List<String> fixedLiteralsList)
    {
        return new SimpleTokenizer(filename, fixedLiteralsList);
    }
    private static final String RESERVEDWORD = "_";

    private String inputProgram;
    private List<String> fixedLiterals;
    private String[] tokens;
    private int currentToken = 0;

    private SimpleTokenizer(String filename, List<String> fixedLiteralsList){
        fixedLiterals = fixedLiteralsList;
        try {
            inputProgram = Files.readString(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Didn't find file");
            System.exit(0);
        }
        tokenize();
    }
     //TODO: Citation: Used SimpleTokenizer that had been show in class "Lectures 2 and 3: Tokenization and Parsing" slides 17-18
     // and added lines to removed any token that are "" or " ";
     // Linked to slides: https://docs.google.com/presentation/d/1uHVLBYavXVpOA3BqYIa_7ttqnI9RT1hDZQrY1F8pZhw/edit#slide=id.g40cccd4300_0_322
    //modifies: this.tokens
    //effects: will result in a list of tokens (sitting at this.tokens) that has no spaces around tokens.
    private void tokenize (){
        //0. Pick some RESERVEDWORD (string which never occurs in your input) : we'll use _
        //1. Read the whole program into a single string; kill the newlines
        String tokenizedProgram = inputProgram.replace("\n", "");
        System.out.println(tokenizedProgram);
        //2. Replace all constant literals with “RESERVEDWORD”<the literal>“RESERVEDWORD”
        for(String s : fixedLiterals) {
            tokenizedProgram = tokenizedProgram.replace(s, RESERVEDWORD + s + RESERVEDWORD);
            System.out.println(tokenizedProgram);
        }
        //3. Replace all “RESERVEDWORDRESERVEDWORD” with just “RESERVEDWORD”
        tokenizedProgram = tokenizedProgram.replace(RESERVEDWORD+RESERVEDWORD,RESERVEDWORD);
        System.out.println(tokenizedProgram);
        //4. Remove leading “_” character, then split on “_”
        if(tokenizedProgram.length() > 0 && tokenizedProgram.startsWith(RESERVEDWORD)) {
            tokenizedProgram = tokenizedProgram.substring(RESERVEDWORD.length()); // without first character
        }
        tokens = tokenizedProgram.split(RESERVEDWORD);


        //5. Trim whitespace around tokens (unless you want it)
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        //Added this to removed tokens that are " " or ""
        List<String> ListofToken = new ArrayList(Arrays.asList(tokens));
        List<String> newListofToken = new ArrayList<>();
        for (int i = 0; i < ListofToken.size(); i++) {
            if(ListofToken.get(i).equals(" ") || ListofToken.get(i).equals("")){

            }else{
                newListofToken.add(ListofToken.get(i));
            }
        }

        tokens = newListofToken.toArray(new String[0]);

        System.out.println(Arrays.asList(tokens));
    }


    private String checkNext(){
        String token="";
        if (currentToken<tokens.length){
            token = tokens[currentToken];
        }
        else
            token="NO_MORE_TOKENS";
        return token;
    }

    @Override
    public String getNext(){
        String token="";
        if (currentToken<tokens.length){
            token = tokens[currentToken];
            currentToken++;
        }
        else
            token="NULLTOKEN";
        return token;
    }


    @Override
    public boolean checkToken(String regexp){
        String s = checkNext();
        System.out.println("comparing: |"+s+"|  to  |"+regexp+"|");
        return (s.matches(regexp));
    }


    @Override
    public String getAndCheckNext(String regexp){
        String s = getNext();
        if (!s.matches(regexp)) {
            throw new RuntimeException("Unexpected next token for Parsing! Expected something matching: " + regexp + " but got: " + s);
        }
        System.out.println("matched: "+s+"  to  "+regexp);
        return s;
    }

    @Override
    public boolean moreTokens(){
        return currentToken<tokens.length;
    }

}
