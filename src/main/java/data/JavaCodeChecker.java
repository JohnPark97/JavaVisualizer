package data;

import java.util.List;

public class JavaCodeChecker {

    public int MaxClassLength = 1;
    public int MaxMethodLength = 1;


    public JavaCodeChecker() { }


    public void checkClass(List<JavaClass> javaClasses){
        for(JavaClass jc: javaClasses){
            //put in methods for checking
            checkLongClass(jc);
            for(JavaMethod jm: jc.getMethods()) {
                checkLongMethod(jc,jm);
            }
        }
    }

    //check if method line count exceeds MaxMethodLength
    public void checkLongMethod(JavaClass jc, JavaMethod jm){
        Integer lineCount = jm.getLineCount();
        if(lineCount > MaxMethodLength){
            String codeSmell = jc.getInformation() + "Long Method: "+ jm.getName() +" is exceeding " + MaxMethodLength+" lines\n";
            jc.setInformation(codeSmell);

        }



    }
    //check if class line count exceeds MaxClassLength
    public void checkLongClass(JavaClass jc){
        Integer lineCount = jc.getLineCount();
        if(lineCount > MaxClassLength){
            String codeSmell = jc.getInformation() + "Long Class: "+ jc.getClassName() +" is exceeding " + MaxClassLength+" lines\n";
            jc.setInformation(codeSmell);
        }

    }
}
