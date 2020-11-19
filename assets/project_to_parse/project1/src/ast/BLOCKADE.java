package ast;

import libs.Node;

import java.util.ArrayList;
import java.util.List;

public class BLOCKADE extends Node{
    HP hp;
    SPAWN spawn;

    public BLOCKADE(HP hp, SPAWN spawn){
        this.hp = hp;
        this.spawn = spawn;
    }

    public HP getHp() {
        return hp;
    }

    public SPAWN getSpawn() {
        return spawn;
    }


     @Override
     public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v) {
        return v.visit(context,this);
    }


}
