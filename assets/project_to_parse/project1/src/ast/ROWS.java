package ast;

import libs.Node;

public class ROWS extends Node {
    int row;

    public ROWS(int row) {
        this.row = row;

    }

    public int getRow() {
        return row;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
