package data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class FeatureEnvyVisitor extends VoidVisitorAdapter<List<FeatureEnvyTracker>> {

    @Override
    public void visit(MethodCallExpr mce, List<FeatureEnvyTracker> n){
        if(!mce.getScope().isEmpty()) {
            Position start = mce.getRange().get().begin;
            Position end = mce.getRange().get().end;
            Integer numberOfdereferences = 0;
            FeatureEnvyTracker fe = new FeatureEnvyTracker(start, end, numberOfdereferences);
            checkDeReferences(mce, fe);
            n.add(fe);
        }
    }

    public void checkDeReferences(MethodCallExpr mce, FeatureEnvyTracker f){
        f.setNumberofDereferences(f.getNumberofDereferences() + 1);
        if(mce.getScope().get().isMethodCallExpr()){
            MethodCallExpr m = (MethodCallExpr) mce.getScope().get();
            checkDeReferences(m,f);
        }

    }
}
