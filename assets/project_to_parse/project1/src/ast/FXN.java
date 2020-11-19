package ast;

import libs.Node;

public class FXN  extends Node {
    // fot stands for function of time
    String fot;

    public FXN(String fot){
        this.fot = fot;
    }

    public String getFot() {
        return fot;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
