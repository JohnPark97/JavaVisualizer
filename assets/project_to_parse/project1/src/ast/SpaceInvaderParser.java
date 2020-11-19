package ast;

import libs.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvaderParser {
    private static final String FXN = "[A-Za-z0-9]*";
    private static final String NAME = "[A-Za-z][A-Za-z0-9]*";
    private Tokenizer tokenizer;

    public static SpaceInvaderParser getParser(Tokenizer tokenizer) {
        return new SpaceInvaderParser(tokenizer);
    }

    private SpaceInvaderParser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    //TODO write parsing methods

    public PROGRAM parseProgram(){
        List<WAVE> waveList = new ArrayList<>();
        List<VARS> varsList = new ArrayList<>();
        PLAYER player = null;
        BLOCKADE blockade = null;
        if(tokenizer.checkToken("VARIABLE:")) {
            while (tokenizer.checkToken("VARIABLE:")) {
                varsList.add(parseVar());
            }
        }
        if (tokenizer.checkToken("PLAYER:")) {
            player = parsePlayer(varsList);
        }
        if (tokenizer.checkToken("BLOCKADE:")|| tokenizer.checkToken(NAME)) {
            blockade = parseBlockade(varsList);
        }
        if(tokenizer.checkToken("WAVE:")) {
            while (tokenizer.checkToken("WAVE:")) {
                waveList.add(parseWave(varsList));
            }
        }
        return new PROGRAM(varsList,player,blockade,waveList);
    }

    public BLOCKADE parseBlockade(List<VARS> variables){
        if(tokenizer.checkToken("BLOCKADE:")) {
            tokenizer.getAndCheckNext("BLOCKADE:");
        }else{
            String var_name = tokenizer.getNext();
            for(int i = 0; i < variables.size();i++){
                if(var_name.equals(variables.get(i).var_name.name)){
                    if(variables.get(i).var_value.bValue != null) {
                        return variables.get(i).var_value.bValue;
                    }else{
                        throw new RuntimeException("Try to use variable but not BLOCKADE :"
                                + variables.get(i).var_name.name);
                    }
                }
            }
            throw new RuntimeException("Can't find variable: " + var_name);

        }
        HP hp = parseHp();
        SPAWN spawn = parseSpawn();
        return new BLOCKADE(hp,spawn);
    }

    public DIRECTION parseDirection(){
        if(tokenizer.checkToken("(LEFT|RIGHT|UP|DOWN)")){
            return new DIRECTION(tokenizer.getNext());
        }else{
            throw new RuntimeException("Not a Direction: " + tokenizer.getNext());
        }
    }

    public E_SPRITE parseE_sprite(){
        if(tokenizer.checkToken("(ALIEN|BOSS|BONUS)")){
            return new E_SPRITE(tokenizer.getNext());
        }else{
            throw new RuntimeException("Not a Sprite: " + tokenizer.getNext());
        }
    }

    public FXN parseFxn() {
       // if (tokenizer.checkToken(FXN)) {
            return new FXN(tokenizer.getNext());
       /// }else{
        ///    throw new RuntimeException("Not a function:" + tokenizer.getNext());
       /// }

    }

    public HP parseHp(){
        tokenizer.getAndCheckNext("HP");
        tokenizer.getAndCheckNext("=");
        int number = -1;
        if(tokenizer.checkToken("[0-9]+")) {
            number = Integer.parseInt(tokenizer.getNext());
        }else{
            throw new RuntimeException("NOT a HP number: " + tokenizer.getNext());
        }
        return new HP(number);
    }

    public LOOP parseLoop(){
        tokenizer.getAndCheckNext("LOOP");
        tokenizer.getAndCheckNext("=");
        int number = -1;
        if(tokenizer.checkToken("[0-9]+")) {
            number = Integer.parseInt(tokenizer.getNext());
        }else{
            throw new RuntimeException("Not a Loop number: " + tokenizer.getNext());
        }
        return new LOOP(number);
    }

    public MOVEMENTS parseMovements(List<VARS> variables){
        List<MOVEMENT> movements = new ArrayList<>();
        tokenizer.getAndCheckNext("MOVEMENT:");
        if(tokenizer.checkToken("(LEFT|RIGHT|UP|DOWN)")) {
            MOVEMENT movement = parseMovement();
            movements.add(movement);
        }else{
            String currToken = tokenizer.getNext();
            Boolean variableFound = false;
            for (int i = 0; i < variables.size(); i++) {
                if (currToken.equals(variables.get(i).var_name.name)){
                    if(variables.get(i).var_value.mValue != null){
                        variableFound = true;
                        movements.add(variables.get(i).var_value.mValue);
                    }else{
                        throw new RuntimeException("Variable is not a Movement: " + variables.get(i).var_name.name);
                    }
                }
            }
            if(variableFound.equals(false)) {
                throw new RuntimeException("Can't find variable: " + currToken);
            }
        }

        while(tokenizer.checkToken("\\|")) {
            tokenizer.getAndCheckNext("\\|");
            if (tokenizer.checkToken("(LEFT|RIGHT|UP|DOWN)")) {
                MOVEMENT movement = parseMovement();
                movements.add(movement);
            } else {
                String currToken = tokenizer.getNext();
                Boolean variableFound = false;
                for (int i = 0; i < variables.size(); i++) {
                    if (currToken.equals(variables.get(i).var_name.name)) {
                        if (variables.get(i).var_value.mValue != null) {
                            variableFound = true;
                            movements.add(variables.get(i).var_value.mValue);
                        } else {
                            throw new RuntimeException("Variable is not a Movement: " + variables.get(i).var_name.name);
                        }
                    }
                }
                if (variableFound.equals(false)) {
                    throw new RuntimeException("Can't find variable: " + currToken);
                }
            }
        }
        if(movements.size() <= 0){
            throw new RuntimeException("No movements");
        }
        return new MOVEMENTS(movements);

    }

    public MOVEMENT parseMovement(){
        List<MOVEMENTVALUE> movementValues = new ArrayList<>();
        while(tokenizer.checkToken("(LEFT|RIGHT|UP|DOWN)")) {
            movementValues.add(parseMovementValue());
        }
        if(movementValues.size() < 0){
            throw new RuntimeException("No movements");
        }
        LOOP loop = null;
        if(tokenizer.checkToken("LOOP")){
            loop = parseLoop();
        }
        return new MOVEMENT(movementValues, loop);
    }

    public MOVEMENTVALUE parseMovementValue(){
        DIRECTION direction = null;
        int number = -1;
            direction = parseDirection();
            tokenizer.getAndCheckNext("=");
            if (tokenizer.checkToken("[0-9]+")) {
                number = Integer.parseInt(tokenizer.getNext());
            } else {
                throw new RuntimeException("Incorrect movement number: " + tokenizer.getNext());
            }
        return new MOVEMENTVALUE(direction,number);
    }

    public PLAYER parsePlayer(List<VARS> varsList){
        tokenizer.getAndCheckNext("PLAYER:");
        tokenizer.getAndCheckNext("\\[");
        HP hp = null;
        SPEED speed = null;
        if(tokenizer.checkToken("HP")){
            hp = parseHp();
        }else{
            throw new RuntimeException("Unknown expression: " + tokenizer.getNext());
        }
        tokenizer.getAndCheckNext("\\|");
        if(tokenizer.checkToken("SPEED") || tokenizer.checkToken(NAME)){
            speed = parseSpeed(varsList);
        }else{
            throw new RuntimeException("Unknown expression: " + tokenizer.getNext());
        }
        tokenizer.getAndCheckNext("\\]");
        return new PLAYER(hp,speed);
    }

    public POINTS parsePoints(){
        tokenizer.getAndCheckNext("POINTS");
        tokenizer.getAndCheckNext("=");
        tokenizer.checkToken("[0-9]+");
        int number = Integer.parseInt(tokenizer.getNext());
        return new POINTS(number);
    }

    public ROWS parseRows(){
        tokenizer.getAndCheckNext("ROWS");
        tokenizer.getAndCheckNext("=");
        tokenizer.checkToken("[0-9]+");
        int number = Integer.parseInt(tokenizer.getNext());
        return new ROWS(number);
    }

    public SPAWN parseSpawn(){
        tokenizer.getAndCheckNext("SPAWN");
        tokenizer.getAndCheckNext("=");
        tokenizer.checkToken("[0-9]+");
        int number = Integer.parseInt(tokenizer.getNext());
        return new SPAWN(number);
    }

    public SPEED parseSpeed(List<VARS> variables){
        if(tokenizer.checkToken("SPEED")) {
            tokenizer.getAndCheckNext("SPEED");
            tokenizer.getAndCheckNext("=");
            if (tokenizer.checkToken("[0-9]+")) {
                int number = Integer.parseInt(tokenizer.getNext());
                return new SPEED(number, null);
            } else {
                FXN function = parseFxn();
                return new SPEED(-1, function);
            }
        }else{
            String currToken = tokenizer.getNext();
            for (int i = 0; i < variables.size(); i++) {
                if (currToken.equals(variables.get(i).var_name.name)){
                    if(variables.get(i).var_value.sValue != null){
                        return variables.get(i).var_value.sValue;
                    }else{
                        throw new RuntimeException("Variable is not a SPEED: " + variables.get(i).var_name.name);
                    }
                }
            }
            throw new RuntimeException("Can't find variable: " + currToken);
        }
    }

    public VARS parseVar(){
        tokenizer.getAndCheckNext("VARIABLE:");
        VAR_NAME name = null;
        if(tokenizer.checkToken(NAME)){
            name = new VAR_NAME(tokenizer.getNext());
        }else{
            throw new RuntimeException("Unknown expression: " + tokenizer.getNext());
        }
        tokenizer.getAndCheckNext("=");
        if(tokenizer.checkToken("SPEED")){
            SPEED speed = parseSpeed(new ArrayList<>());
            VAR_VALUE val = new VAR_VALUE(speed);
            return new VARS(name,val);
        }else if (tokenizer.checkToken("BLOCKADE:")){
            BLOCKADE block = parseBlockade(new ArrayList<>());
            VAR_VALUE val = new VAR_VALUE(block);
            return new VARS(name,val);
        }else if (tokenizer.checkToken("(LEFT|RIGHT|UP|DOWN)")){
            MOVEMENT movement = parseMovement();
            VAR_VALUE val = new VAR_VALUE(movement);
            return new VARS(name,val);
        }else{
            throw new RuntimeException("Unknown expression: " + tokenizer.getNext());
        }
    }
    public ENEMYVALUE parseEnemyValue(List<VARS> variables){
        tokenizer.getAndCheckNext("\\[");
        E_SPRITE sprite = parseE_sprite();
        tokenizer.getAndCheckNext(":");
        HP hp =parseHp();
        tokenizer.getAndCheckNext("\\|");
        SPEED speed = parseSpeed(variables);
        POINTS points = parsePoints();
        tokenizer.getAndCheckNext("\\]");
        return new ENEMYVALUE(sprite,hp,speed,points);
    }

    public ENEMY parseEnemy(List<VARS> variables){
        ENEMYVALUE enemyvalue = parseEnemyValue(variables);
        SPAWN spawn = parseSpawn();
        ROWS rows = parseRows();
        return new ENEMY(enemyvalue,spawn,rows);
    }



    public WAVE parseWave(List<VARS> variables){
        tokenizer.getAndCheckNext("WAVE:");
        List<ENEMY> enemies = new ArrayList<>();
        MOVEMENTS movements = null;
        while(tokenizer.checkToken("\\[")){
            enemies.add(parseEnemy(variables));
        }
        if(enemies.size() <= 0){
            throw new RuntimeException("No enemies");
        }
        if(tokenizer.checkToken("MOVEMENT:")){
            movements = parseMovements(variables);
        }
        return new WAVE(enemies, movements);
    }


}
