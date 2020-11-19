package ast;

import libs.Node;

public class DIRECTION extends Node {
    // one of LEFT RIGHT UP DOWN
    String dir;

    public DIRECTION(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
