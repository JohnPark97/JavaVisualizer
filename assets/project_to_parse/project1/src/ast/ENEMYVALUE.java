package ast;

import libs.Node;

public class ENEMYVALUE  extends Node{
    E_SPRITE e_sprite;
    HP hp;
    SPEED speed;
    POINTS points;

    public ENEMYVALUE(E_SPRITE e_sprite, HP hp, SPEED speed, POINTS points){
        this.e_sprite = e_sprite;
        this.hp = hp;
        this.speed = speed;
        this.points = points;
    }

    public E_SPRITE getE_sprite() {
        return e_sprite;
    }

    public HP getHp() {
        return hp;
    }

    public SPEED getSpeed() {
        return speed;
    }

    public POINTS getPoints() {
        return points;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
