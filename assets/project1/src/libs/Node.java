package libs;


import ast.SpaceInvaderVisitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class Node {

    //abstract public void evaluate();
    abstract public <C, T> T accept(C context, SpaceInvaderVisitor<C, T> v);


}
