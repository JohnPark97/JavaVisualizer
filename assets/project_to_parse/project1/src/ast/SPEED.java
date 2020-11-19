package ast;

import libs.Node;

public class SPEED  extends Node {
    int number;
    FXN fxn;

    public SPEED(int number, FXN fxn){
        this.number = number;
        this.fxn = fxn;
    }

    public int getNumber() {
        return number;
    }

    public FXN getFxn() {
        return fxn;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}


