package data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaParser {
    public JavaParser() {}

    public void parse() throws IOException {
        //get the files to parse in folder
        File projectDirectory = new File("assets/project_to_parse");
        SourceRoot source = new SourceRoot(projectDirectory.toPath());
        //Tries to parse all .java files under the source root recursively,
        // and returns all files ever parsed with this source root.
        List<ParseResult<CompilationUnit>> parseResults = source.tryToParse();//this has all the files parsed from our project 1;
        List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

        //checking if the parse was successful, then putting all the compilationUnits into a list
        for (ParseResult<CompilationUnit> parsecu : parseResults) {
            if(parsecu.isSuccessful() || checkProblem(parsecu.getProblems())) {
                if(parsecu.isSuccessful() || checkProblem(parsecu.getProblems())) {
                    compilationUnits.add(parsecu.getResult().get());
                }
            }
        }

        List<JavaClass> ListOfJavaClasses = new ArrayList<JavaClass>();
        Integer index = 0;
        for (CompilationUnit cu : compilationUnits) {
            //visit the ast structure and get the info that we want
            JavaClass javaClass = new JavaClass();
            javaClass.setID(index);
            javaClass.setDependencies(new ArrayList<JavaDependency>());
            javaClass.setImports(new ArrayList<String>());
            javaClass.setGlobalVariables(new ArrayList<JavaVariable>());
            javaClass.setMethods(new ArrayList<JavaMethod>());
            javaClass.setEnum(false);
            javaClass.setInformation("Potential Code Smells: \n");
            JavaVisitor visitor = new JavaVisitor();
            cu.accept(visitor, javaClass);
            //add it to the list of java classes
            ListOfJavaClasses.add(javaClass);
            index++;
        }
        //Checking the dependencies
        checkDependencies(ListOfJavaClasses);

        JavaCodeChecker javaCodeChecker = new JavaCodeChecker();
        javaCodeChecker.checkClass(ListOfJavaClasses);

        JSONConvertor convertor = new JSONConvertor();
        JSONObject jsonProject = convertor.createJSON(ListOfJavaClasses);

        try (FileWriter file = new FileWriter("assets/project.json")) {

            file.write(jsonProject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkProblem(List<Problem> problems){
        boolean IsNotproblem = true;
        for(Problem p: problems){
            String message = p.getMessage();
            if(!(message.equals("Switch expressions are not supported."))){
                IsNotproblem = false;
            }

        }
        return IsNotproblem;
    }

    public static void checkDependencies(List<JavaClass> list) {
        for (JavaClass jc : list) {
            List<JavaDependency> dependencies = new ArrayList<JavaDependency>();
            for (int i = 0; i < jc.getDependencies().size(); i++) {
                List<String> ClassNames = new ArrayList<String>();
                for(String s: jc.getDependencies().get(i).getClassNames()){
                    for(JavaClass j: list){
                        if(s.equals(j.getClassName())){
                            ClassNames.add(s);
                        }
                    }
                }
                jc.getDependencies().get(i).setClassNames(ClassNames);

                Boolean classExist = false;
                for (JavaClass j : list) {
                    for(String s: jc.getDependencies().get(i).getClassNames()){
                        if (s.equals(j.getClassName())) {
                            classExist = true;
                        }
                    }
                }
                if (classExist.equals(true)) {
                    dependencies.add(jc.getDependencies().get(i));
                }
            }
            jc.setDependencies(dependencies);
        }
    }
}
