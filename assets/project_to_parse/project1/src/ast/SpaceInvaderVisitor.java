package ast;

public interface SpaceInvaderVisitor<C,T> {
    T visit(C context, PROGRAM p);
    T visit(C context, BLOCKADE b);
    T visit(C context, DIRECTION d);
    T visit(C context, E_SPRITE e);
    T visit(C context, ENEMY e);
    T visit(C context, ENEMYVALUE e);
    T visit(C context, FXN f);
    T visit(C context, HP h);
    T visit(C context, LOOP l);
    T visit(C context, MOVEMENT m);
    T visit(C context, MOVEMENTS m);
    T visit(C context, MOVEMENTVALUE m);
    T visit(C context, PLAYER p);
    T visit(C context, POINTS p);
    T visit(C context, SPAWN s);
    T visit(C context, ROWS r);
    T visit(C context, SPEED s);
    T visit(C context, VAR_NAME v);
    T visit(C context, VAR_VALUE v);
    T visit(C context, VARS v);
    T visit(C context, WAVE w);



}
