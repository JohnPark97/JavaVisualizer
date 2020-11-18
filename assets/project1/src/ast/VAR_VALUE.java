package ast;

import libs.Node;

public class VAR_VALUE extends Node {
    BLOCKADE bValue;
    SPEED sValue;
    MOVEMENT mValue;


    public VAR_VALUE(BLOCKADE value) {
        this.bValue = value;
    }

    public VAR_VALUE(SPEED value) { this.sValue = value; }

    public VAR_VALUE(MOVEMENT value) {this.mValue = value;}

    public BLOCKADE getbValue() {
        return bValue;
    }

    public SPEED getsValue() {
        return sValue;
    }

    public MOVEMENT getmValue() {
        return mValue;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}