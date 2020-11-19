package ast;

import libs.Node;

import java.util.List;

public class MOVEMENTVALUE extends Node {
    DIRECTION direction;
    int speed;


    public MOVEMENTVALUE (DIRECTION direction, int speed) {
        this.direction = direction;
        this.speed = speed;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
