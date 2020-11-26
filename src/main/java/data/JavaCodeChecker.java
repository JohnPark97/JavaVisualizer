package data;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.List;

public class JavaCodeChecker {

    public int MaxClassLength = 100;//200
    public int MaxMethodLength = 30;//50
    public int MaxParameterLength = 1;//3
    public int MaxConditionalStmt = 1;//2
    public int MaxDereferences = 1;//2


    public JavaCodeChecker() { }


    public void checkClass(List<JavaClass> javaClasses){
        for(JavaClass jc: javaClasses){
            //put in methods for checking
            checkLongClass(jc);
            for(JavaMethod jm: jc.getMethods()) {
                checkLongMethod(jc,jm);
                checkTooManyParameters(jc,jm);
                checkComplicatedConditional(jc,jm);
                checkFeatureEnvy(jc,jm);
            }
        }
    }

    //check if method line count exceeds MaxMethodLength
    public void checkLongMethod(JavaClass jc, JavaMethod jm){
        Integer lineCount = jm.getLineCount();
        if(lineCount > MaxMethodLength){
            String codeSmell = jc.getInformation() + "Long Method: "+ jm.getName() +" Method is exceeding " + MaxMethodLength+" lines\n";
            jc.setInformation(codeSmell);

        }



    }
    //check if class line count exceeds MaxClassLength
    public void checkLongClass(JavaClass jc){
        Integer lineCount = jc.getLineCount();
        if(lineCount > MaxClassLength){
            String codeSmell = jc.getInformation() + "Long Class: "+ jc.getClassName() +" Class is exceeding " + MaxClassLength+" lines\n";
            jc.setInformation(codeSmell);
        }

    }

    //check if method has too many parameter (could be due to data clumping)
    public void checkTooManyParameters(JavaClass jc, JavaMethod jm){
        Integer numberOfParameters = jm.getParameterList().size();
        if(numberOfParameters > MaxParameterLength){
            String codeSmell = jc.getInformation() + "Too Many Parameters/Data Clump: "+ jm.getName() +
                    " Method has too many parameters. Exceeding the max parameter: "+MaxParameterLength +".\n";
            jc.setInformation(codeSmell);
        }
    }
    
   //check if method has Complicated conditional
    public void checkComplicatedConditional(JavaClass jc, JavaMethod jm) {
        String CodeSmells = "";
        for (Statement s : jm.getStatements()) {
            System.out.println(s);
            ComplicatedConditionalVisitor javaCodeCheckerVisitor = new ComplicatedConditionalVisitor();
            List<ConditionalTracker> numberofConditional = new ArrayList<ConditionalTracker>();
            s.accept(javaCodeCheckerVisitor, numberofConditional);
            for(ConditionalTracker c: numberofConditional) {
                if (c.getConditionals().size() >= MaxConditionalStmt) {
                    CodeSmells = CodeSmells + "Complicated Conditional: " + "Conditional is too complicated on lines: "
                            + c.getStartLine() + " - " + c.getEndLine() + ".\n";
                }
            }
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }

    //check if method has FeatureEnvy
    public void checkFeatureEnvy(JavaClass jc, JavaMethod jm) {
        String CodeSmells = "";
        for (Statement s : jm.getStatements()) {
            System.out.println(s);
            FeatureEnvyVisitor featureEnvyVisitor = new FeatureEnvyVisitor();
            List<FeatureEnvyTracker> numberofFeatureEnvy = new ArrayList<FeatureEnvyTracker>();
            s.accept(featureEnvyVisitor, numberofFeatureEnvy);
            for(FeatureEnvyTracker f: numberofFeatureEnvy) {
                if (f.getNumberofDereferences() >= MaxDereferences) {
                    CodeSmells = CodeSmells + "Feature Envy: " + "In "+jm.getName()+ " Method: Too many dereferences on lines: "
                            + f.getStartLine() + " - " + f.getEndLine() + ".\n";
                }
            }
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }


}
