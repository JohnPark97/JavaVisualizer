package ast;

import libs.Node;

import java.util.List;

public class MOVEMENT extends Node {
    List<MOVEMENTVALUE> movementValueList;
    LOOP loop;

    public MOVEMENT (List<MOVEMENTVALUE> movementValueList, LOOP loop) {
        this.movementValueList = movementValueList;
        this.loop = loop;
    }

    public List<MOVEMENTVALUE> getMovementValueList() {
        return movementValueList;
    }

    public LOOP getLoop() {
        return loop;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
