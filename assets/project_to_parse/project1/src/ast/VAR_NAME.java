package ast;

import libs.Node;

public class VAR_NAME extends Node {
    String name;

    public VAR_NAME(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
