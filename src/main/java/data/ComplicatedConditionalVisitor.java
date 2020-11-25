package data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplicatedConditionalVisitor extends VoidVisitorAdapter<List<ConditionalTracker>> {

    public List<String> operators = Arrays.asList( "AND", "BINARY_OR", "BINARY_AND"
            ,"EQUALS","GREATER","GREATER_EQUALS","LESS",
            "LESS_EQUALS","NOT_EQUALS","OR","XOR");

    //a && b or 155 * 33
    @Override
    public void visit(BinaryExpr be, List<ConditionalTracker> n){
        Position start = be.getRange().get().begin;
        Position end = be.getRange().get().end;
        Integer numberOfConditional = 0;
        List<String> conditonalList = new ArrayList<String>();
        ConditionalTracker ct = new ConditionalTracker(start,end,numberOfConditional,conditonalList);
        checkBranch(be,ct);
        n.add(ct);
    }

    public void checkBranch(BinaryExpr be, ConditionalTracker c){
        c.setNumberofConditional(c.getNumberofConditional() + 1);
        c.getConditionals().add(be.getOperator().name());
        Expression left = be.getLeft();
        Expression right = be.getRight();
        if(left.isBinaryExpr()){
            checkBranch(left.asBinaryExpr(),c);
        }
        if(right.isBinaryExpr()){
            checkBranch(right.asBinaryExpr(),c);
        }

    }


}