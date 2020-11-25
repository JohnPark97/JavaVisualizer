package data;

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

public class ComplicatedConditionalVisitor extends VoidVisitorAdapter<List<String>> {

    public List<String> operators = Arrays.asList( "AND", "BINARY_OR", "BINARY_AND"
            ,"EQUALS","GREATER","GREATER_EQUALS","LESS",
            "LESS_EQUALS","NOT_EQUALS","OR","XOR");

    //a && b or 155 * 33
    @Override
    public void visit(BinaryExpr be, List<String> n){
        if(operators.contains(be.getOperator().name())){
            n.add(be.getOperator().name());
            Expression left = be.getLeft();
            Expression right = be.getRight();
            ComplicatedConditionalVisitor cv = new ComplicatedConditionalVisitor();
            if(left.isBinaryExpr()){
                left.accept(cv,n);
            }
            if(right.isBinaryExpr()){
                right.accept(cv,n);
            }
        }
    }


}