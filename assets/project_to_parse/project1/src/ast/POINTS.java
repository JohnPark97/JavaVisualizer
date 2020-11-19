package ast;

import libs.Node;

public class POINTS  extends Node {
    int point;

    public POINTS(int point){
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
