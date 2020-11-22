package data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.utils.SourceRoot;
import com.sun.jdi.InterfaceType;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.Visitor;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
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
            javaClass.setDependencies(new ArrayList<String>());
            javaClass.setImports(new ArrayList<String>());
            javaClass.setGlobalVariables(new ArrayList<JavaVariable>());
            javaClass.setMethods(new ArrayList<JavaMethod>());
            javaClass.setEnum(false);
            JavaVisitor visitor = new JavaVisitor();
            cu.accept(visitor, javaClass);
            //add it to the list of java classes
            ListOfJavaClasses.add(javaClass);
        }
        //Checking the dependencies
        checkDependencies(ListOfJavaClasses);
        JSONObject jsonProject = createJSON(ListOfJavaClasses);


        try (FileWriter file = new FileWriter("assets/project.json")) {

            file.write(jsonProject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkDependencies(List<JavaClass> list) {
        for (JavaClass jc : list) {
            List<String> dependencies = new ArrayList<String>();
            for (int i = 0; i < jc.getDependencies().size(); i++) {
                Boolean classExist = false;
                for (JavaClass j : list) {
                    if (jc.getDependencies().get(i).contains(j.getClassName()) ||
                            jc.getDependencies().get(i).equals(j.getClassName())) {
                        classExist = true;
                    }
                }
                if (classExist.equals(true)) {
                    dependencies.add(jc.getDependencies().get(i));
                }
            }
            jc.setDependencies(dependencies);
        }
    }

    public static JSONObject createJSON(List<JavaClass> list) {
        JSONObject jsonProject = new JSONObject();
        List<JSONObject> classes = new ArrayList<JSONObject>();
        List<JSONObject> links = new ArrayList<JSONObject>();
        for (JavaClass jc : list) {
            classes.add(JSONClass(jc));
            links.add(JSONDependency(jc));
        }
        jsonProject.put("classes", classes);
        jsonProject.put("links", links);

        return jsonProject;
    }

    public static JSONObject JSONClass(JavaClass jc) {
        JSONObject jsonClass = new JSONObject();
        jsonClass.put("name", jc.getClassName());
        jsonClass.put("line_count", jc.getLineCount());
        jsonClass.put("IsEnumeration",jc.getEnum());
        jsonClass.put("imports",jc.getImplements());
        jsonClass.put("IsInterface", jc.getInterface());

        List<JSONObject> methods = new ArrayList<JSONObject>();
        for (JavaMethod m : jc.Methods) {
            methods.add(JSONMethod(m));
        }
        jsonClass.put("methods", methods);

        List<JSONObject> variables = new ArrayList<JSONObject>();
        for (JavaVariable v : jc.GlobalVariables) {
            variables.add(JSONVariable(v));
        }
        jsonClass.put("variables", variables);

        return jsonClass;
    }

    public static JSONObject JSONVariable(JavaVariable jv) {
        JSONObject jsonVariable = new JSONObject();
        jsonVariable.put("name", jv.getName());
        jsonVariable.put("type",jv.getType());
        jsonVariable.put("modifiers", jv.getModifiers());
        return jsonVariable;
    }

    public static JSONObject JSONDependency(JavaClass jc) {
        JSONObject jsonDep = new JSONObject();
        jsonDep.put("Class",jc.getClassName());
        jsonDep.put("Implementations", jc.getImplements());
        jsonDep.put("Extensions", jc.getExtensions());
        jsonDep.put("Dependencies", jc.getDependencies());
        return jsonDep;
    }

    public static JSONObject JSONMethod(JavaMethod jm) {
        JSONObject jsonMethod = new JSONObject();
        jsonMethod.put("name", jm.getName());
        jsonMethod.put("returnType", jm.getReturnType());
        jsonMethod.put("IsConstructor", jm.getConstructor());
        jsonMethod.put("modifiers", jm.getModifiers());
        List<JSONObject> parameters = new ArrayList<JSONObject>();
        for (JavaParameter p : jm.getParameterList()) {
            parameters.add(JSONParameter(p));
        }
        jsonMethod.put("parameters", parameters);
        return jsonMethod;
    }

    public static JSONObject JSONParameter(JavaParameter jp) {
        JSONObject jsonParameter = new JSONObject();
        jsonParameter.put("name", jp.getName());
        jsonParameter.put("type", jp.getType());
        return jsonParameter;
    }
}


    /*
      public static JSONObject createJSON(List<JavaClass> list){
        JSONObject jsonProject = new JSONObject();
        List<JSONObject> classes = new ArrayList<JSONObject>();
        List<JSONObject> links = new ArrayList<JSONObject>();
        for(JavaClass jc: list){
            classes.add(JSONClass(jc));
            links.add(JSONDependency(jc));
        }
        jsonProject.put("classes", classes);
        jsonProject.put("links", links);

        return jsonProject;
    }

    public static JSONObject JSONClass(JavaClass jc){
        JSONObject jsonClass = new JSONObject();
        jsonClass.put("name",jc.ClassName);
        jsonClass.put("line_count", jc.LineCount);

        List<JSONObject> methods = new ArrayList<JSONObject>();
        for(JavaMethod m: jc.Methods){
            JSONObject jsonMethod = new JSONObject();
            jsonMethod.put("name",m.getName());
            methods.add(jsonMethod);
        }
        jsonClass.put("methods",methods);

        List<JSONObject> variables = new ArrayList<JSONObject>();
        for(JavaVariable v: jc.GlobalVariables){
            JSONObject jsonVariable = new JSONObject();
            jsonVariable.put("name",v.getName());
            variables.add(jsonVariable);
        }
        jsonClass.put("variables",variables);

       return jsonClass;
    }
    public static JSONObject JSONDependency(JavaClass jc){
        JSONObject jsonDep = new JSONObject();
        //jsonDep.put(jc.ClassName,jc.Links);
        return jsonDep;
    }
     */
