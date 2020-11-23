package ast;

import libs.Node;

import java.util.List;

public class MOVEMENTS extends Node {
    List<MOVEMENT> movement;

    public MOVEMENTS( List<MOVEMENT> movement) {
        this.movement = movement;

    }

    public List<MOVEMENT> getMovement() {
        return movement;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}