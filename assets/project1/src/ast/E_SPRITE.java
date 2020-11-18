package ast;

import libs.Node;
public class E_SPRITE extends Node{
    // one of ALIEN, ALIENBOSS, BONUS
    String sprite;

    public E_SPRITE(String sprite){
        this.sprite = sprite;
    }

    public String getSprite() {
        return sprite;
    }

    @Override
    public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }
}
