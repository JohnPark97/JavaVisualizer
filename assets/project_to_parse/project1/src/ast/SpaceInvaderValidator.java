package ast;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SpaceInvaderValidator implements SpaceInvaderVisitor<Set<String>,String> {
    @Override
    public String visit(Set<String> context, PROGRAM p) {
        String error = "";

        List<VARS> variables = p.getVariables();
        PLAYER player = p.getPlayer();
        BLOCKADE blockade = p.getBlockade();
        List<WAVE> waves = p.getWaveList();

        for(int i = 0; i < variables.size();i++){
            error = error + variables.get(i).accept(context,this);
        }

        if(player != null){
            error = error + player.accept(context, this);
        }

        if(blockade != null){
            error = error + blockade.accept(context, this);
        }

        for(int i = 0; i < waves.size();i++){
            error = error + waves.get(i).accept(context,this);
        }
        return error;
    }

    @Override
    public String visit(Set<String> context, BLOCKADE b) {
        String error = "";
        HP hp = b.getHp();
        SPAWN spawn = b.getSpawn();
        error = error + hp.accept(context,this) + spawn.accept(context,this);
        return error;
    }

    @Override
    public String visit(Set<String> context, DIRECTION d) {
        return "";
    }

    @Override
    public String visit(Set<String> context, E_SPRITE e) {
        return "";
    }

    @Override
    public String visit(Set<String> context, ENEMY e) {
        String error = "";

        ENEMYVALUE ev = e.getEnemyvalue();
        ROWS rows = e.getRows();
        SPAWN spawn = e.getSpawn();

        error = error + ev.accept(context,this) + rows.accept(context,this) + spawn.accept(context,this);

        return error;
    }

    @Override
    public String visit(Set<String> context, ENEMYVALUE e) {
        String error = "";
        E_SPRITE e_sprite = e.getE_sprite();
        HP hp = e.getHp();
        POINTS p = e.getPoints();
        SPEED s = e.getSpeed();
        error = error + e_sprite.accept(context,this)
                + hp.accept(context,this)+p.accept(context,this)
                +s.accept(context,this);
        return error;
    }

    @Override
    public String visit(Set<String> context, FXN f) {
        return "";
    }

    @Override
    public String visit(Set<String> context, HP h) {
        return "";
    }

    @Override
    public String visit(Set<String> context, LOOP l) {
        return "";
    }

    @Override
    public String visit(Set<String> context, MOVEMENT m) {
        String error ="";
        List<MOVEMENTVALUE> mv = m.getMovementValueList();
        LOOP l = m.getLoop();
        for(int i = 0; i < mv.size();i++){
            error = error + mv.get(i).accept(context,this);
        }
        if(l != null) {
            error = error + l.accept(context, this);
        }
        return error;
    }

    @Override
    public String visit(Set<String> context, MOVEMENTS m) {
        String error = "";
        List<MOVEMENT> movements = m.getMovement();
        for(int i = 0; i <movements.size();i++) {
            if(movements.get(i).getLoop() == null && i < movements.size() - 1){
                error = error + "There should be a loop for movements that aren't the last one\n";
        }
            error = error + movements.get(i).accept(context, this);
            //check if the last value doesn't have a loop
            if(i == movements.size()-1){
                if(movements.get(i).getLoop() != null){
                    error = error + "There shouldn't be a loop for the last movement\n";
                }
            }
        }
        return error;
    }

    @Override
    public String visit(Set<String> context, MOVEMENTVALUE m) {
        String error = "";
        DIRECTION d = m.getDirection();
        error = error + d.accept(context,this);
        return "";
    }

    @Override
    public String visit(Set<String> context, PLAYER p) {
        String error = "";
        HP hp = p.getHp();
        SPEED s = p.getSpeed();
        error = error + hp.accept(context,this) + s.accept(context,this);
        return error;
    }

    @Override
    public String visit(Set<String> context, POINTS p) {
        return "";
    }

    @Override
    public String visit(Set<String> context, SPAWN s) {
        return "";
    }

    @Override
    public String visit(Set<String> context, ROWS r) {
        return "";
    }

    @Override
    public String visit(Set<String> context, SPEED s) {
        String error = "";
        FXN f = s.getFxn();
        if(f != null){
            error = error + f.accept(context,this);
        }

        return error;
    }

    @Override
    public String visit(Set<String> context, VAR_NAME v) {
        return "";
    }

    @Override
    public String visit(Set<String> context, VAR_VALUE v) {
        String error = "";
        BLOCKADE b = v.getbValue();
        SPEED s = v.getsValue();
        MOVEMENT m = v.getmValue();
        if(b != null){
            error = error + b.accept(context,this);
        }else if(s != null){
            error = error + s.accept(context,this);
        }else{
            error = error + m.accept(context,this);
        }
        return error;
    }

    @Override
    public String visit(Set<String> context, VARS v) {
        if(!context.contains(v.getVar_name().getName())){
            context.add(v.getVar_name().getName());
        }else {
            return "Variables have same name, shouldn't have variables with same name";
        }
        return "";
    }

    @Override
    public String visit(Set<String> context, WAVE w) {
        String error = "";
        List<ENEMY> enemies = w.getEnemies();
        MOVEMENTS movements = w.getMovements();
        for(int i = 0; i < enemies.size();i++){
            error = error + enemies.get(i).accept(context,this);
        }
        error = error + movements.accept(context,this);
        return error;
    }
}


