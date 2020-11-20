package data;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.errorprone.annotations.Var;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JavaClass {
    String ClassName;
    Boolean Interface;
    Integer LineCount;
    List<JavaVariable> GlobalVariables;
    List<JavaMethod> Methods;
    List<String> Links;

    public JavaClass(String cn , boolean i, List<JavaVariable> gv, List<JavaMethod> m, List<String> l, Integer count){
        ClassName = cn;
        Interface = i;
        LineCount = count;
        GlobalVariables = gv;
        Methods  = m;
        Links = l;

    }

    public String getClassName() {
        return ClassName;
    }

    public Boolean getInterface() {
        return Interface;
    }

    public List<JavaVariable> getGlobalVariables() {
        return GlobalVariables;
    }

    public List<JavaMethod> getMethods() {
        return Methods;
    }

    public List<String> getLinks() {
        return Links;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public void setInterface(Boolean anInterface) {
        Interface = anInterface;
    }

    public Integer getLineCount() {
        return LineCount;
    }

    public void setLineCount(Integer lineCount) {
        LineCount = lineCount;
    }

    public void setGlobalVariables(List<JavaVariable> globalVariables) {
        GlobalVariables = globalVariables;
    }

    public void setMethods(List<JavaMethod> methods) {
        Methods = methods;
    }

    public void setLinks(List<String> links) {
        Links = links;
    }
//TODO: need to write the JSONObject creator
    public JSONObject createJSON(JavaClass jc){

        try (FileWriter file = new FileWriter("project.json")) {
            JSONObject jo = new JSONObject();

            file.write(jo.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return new JSONObject();
    }

}
