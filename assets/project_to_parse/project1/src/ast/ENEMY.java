package ast;

import libs.Node;

import java.util.List;

public class ENEMY extends Node{
    ENEMYVALUE enemyvalue;
    SPAWN spawn;
    ROWS rows;

    public ENEMY(ENEMYVALUE enemyvalue, SPAWN spawn, ROWS rows){
        this.enemyvalue = enemyvalue;
        this.spawn = spawn;
        this.rows = rows;

    }

    public ENEMYVALUE getEnemyvalue() {
        return enemyvalue;
    }

    public SPAWN getSpawn() {
        return spawn;
    }

    public ROWS getRows() {
        return rows;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
