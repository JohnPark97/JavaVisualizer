package data;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.util.ArrayList;
import java.util.List;

public class JavaCodeChecker {

    public int MaxClassLength = 200;
    public int MaxMethodLength = 50;
    public int MaxParameterLength = 3;
    public int MaxConditionalStmt = 2;


    public JavaCodeChecker() { }


    public void checkClass(List<JavaClass> javaClasses){
        for(JavaClass jc: javaClasses){
            //put in methods for checking
            checkLongClass(jc);
            for(JavaMethod jm: jc.getMethods()) {
                checkLongMethod(jc,jm);
                checkTooManyParameters(jc,jm);
                checkComplicatedConditional(jc,jm);
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
            List<String> numberofConditional = new ArrayList<String>();
            s.accept(javaCodeCheckerVisitor, numberofConditional);
            if(numberofConditional.size() >= MaxConditionalStmt){
                CodeSmells = CodeSmells + "Complicated Conditional: " + "Conditional is too complicated on lines: "
                        +s.getRange().get().begin +" - "+s.getRange().get().end +".\n";
            }
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }
}
