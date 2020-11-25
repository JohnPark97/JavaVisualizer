package data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.YamlPrinter;
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
        //get the files from project 1
        File projectDirectory = new File("assets/project_to_parse");
        SourceRoot source = new SourceRoot(projectDirectory.toPath());
        //Tries to parse all .java files under the source root recursively,
        // and returns all files ever parsed with this source root.
        List<ParseResult<CompilationUnit>> parseResults = source.tryToParse();//this has all the files parsed from our project 1;
        List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

        System.out.println(parseResults.size());
//checking if the parse was successful, then putting all the compilationUnits into a list
        for (ParseResult<CompilationUnit> parsecu : parseResults) {
            // if(parsecu.isSuccessful()) {
            compilationUnits.add(parsecu.getResult().get());
            //   }
        }

        // Prints out the ast structure:
        YamlPrinter printer = new YamlPrinter(true);
        //       System.out.println(printer.output(compilationUnits.get(1)));

        System.out.println(compilationUnits.size());

        List<JavaClass> ListOfJavaClasses = new ArrayList<JavaClass>();

        for (CompilationUnit cu : compilationUnits) {
            //              System.out.println(cu);
            //visit the ast structure and get the info that we want
            JavaClass javaClass = new JavaClass();
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
        }
        //Checking the dependencies
        checkDependencies(ListOfJavaClasses);
        JSONConvertor convertor = new JSONConvertor();
        JSONObject jsonProject = JSONConvertor.createJSON(ListOfJavaClasses);

        JavaCodeChecker javaCodeChecker = new JavaCodeChecker();
        javaCodeChecker.checkClass(ListOfJavaClasses);

        try (FileWriter file = new FileWriter("assets/project.json")) {

            file.write(jsonProject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkDependencies(List<JavaClass> list) {
        for (JavaClass jc : list) {
            List<JavaDependency> dependencies = new ArrayList<JavaDependency>();
            for (int i = 0; i < jc.getDependencies().size(); i++) {
                Boolean classExist = false;
                for (JavaClass j : list) {
                    for(String s: jc.getDependencies().get(i).getClassNames()){
                        if (s.contains(j.getClassName())) {
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
