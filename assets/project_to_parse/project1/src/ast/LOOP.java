package ast;

import libs.Node;

public class LOOP extends Node {
    int number;

    public LOOP(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
