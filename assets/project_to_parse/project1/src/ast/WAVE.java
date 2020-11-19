package ast;

import libs.Node;

import java.util.List;

public class WAVE  extends Node {
    List<ENEMY> enemies;
    MOVEMENTS movements;


    public WAVE(List<ENEMY> enemies, MOVEMENTS movements){
        this.enemies = enemies;
        this.movements = movements;
    }

    public List<ENEMY> getEnemies() {
        return enemies;
    }

    public MOVEMENTS getMovements() {
        return movements;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
