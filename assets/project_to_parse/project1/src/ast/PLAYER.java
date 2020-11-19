package ast;

import libs.Node;

public class PLAYER  extends Node {
    HP hp;
    SPEED speed;

    public PLAYER(HP hp, SPEED speed){
        this.hp = hp;
        this.speed = speed;
    }

    public HP getHp() {
        return hp;
    }

    public SPEED getSpeed() {
        return speed;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
