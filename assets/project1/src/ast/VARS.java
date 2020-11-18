package ast;

import libs.Node;

public class VARS extends Node {
    VAR_NAME var_name;
    VAR_VALUE var_value;

    public VARS(VAR_NAME var_name, VAR_VALUE var_value) {
        this.var_name = var_name;
        this.var_value = var_value;
    }

    public VAR_NAME getVar_name() {
        return var_name;
    }

    public VAR_VALUE getVar_value() {
        return var_value;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
