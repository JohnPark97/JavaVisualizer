import ast.PROGRAM;
import ast.SpaceInvaderParser;
import ast.SpaceInvaderValidator;
import ast.SpaceInvaderVisitor;
import eval.Settings;
import libs.Node;
import libs.SimpleTokenizer;
import libs.Tokenizer;
import ui.Game;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        // Read user input
        List<String> literals = Arrays.asList("|","VARIABLE: ","=","PLAYER: ","BLOCKADE: ",
                                              "WAVE: ","[","] ","ALIEN","BOSS","BONUS",
                                              "HP","SPEED","SPAWN","ROWS","POINTS","MOVEMENT: ",
                                                "LEFT","RIGHT","UP","DOWN","LOOP");
        String jamesFile = "/Users/James/Documents/school/CPSC410/cpsc410_project1_team6/src/input.txt";
        String juanFile = "/Repo/cpsc410_project1_team6/src/input.txt";
        String johnFile = "/Users/johnpark/IdeaProjects/CPSC410/SpaceInvader/cpsc410_project1_team6/src/input.txt";
        String mandyFile = "/Users/mandy/CPSC410/Project/prog_1_final/src/input.txt";
        Tokenizer tokenizer = SimpleTokenizer.createSimpleTokenizer("src/input.txt",literals);
        System.out.println("Done tokenizing");
        SpaceInvaderParser parser = SpaceInvaderParser.getParser(tokenizer);

        PROGRAM p = parser.parseProgram();
        System.out.println("Done parsing");


        SpaceInvaderValidator validator = new SpaceInvaderValidator();
        String errorMessages = p.accept(new HashSet<>(), validator);
        if(!errorMessages.equals("")){
            throw new RuntimeException(errorMessages);
        }
        Settings settings = p.evaluate();
        System.out.println("Done evaluation");


        // Start the game
        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(600, 400);
        Game game = new Game(frame, settings);
        frame.add(game);
        frame.setBackground(Color.DARK_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        while (!game.isOver()) {
            game.update();
            game.repaint();
            Thread.sleep(10);
        }
    }

}
