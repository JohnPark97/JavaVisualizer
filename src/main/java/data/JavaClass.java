package data;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.errorprone.annotations.Var;
import org.json.simple.JSONObject;

import java.util.List;

public class JavaClass {
    String ClassName;
    Boolean Interface;
    List<JavaVariable> GlobalVariables;
    List<JavaMethod> Methods;
    List<String> Links;

    public JavaClass(String cn , boolean i, List<JavaVariable> gv, List<JavaMethod> m, List<String> l){
        ClassName = cn;
        Interface = i;
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
    public JSONObject createJSON(){
        return new JSONObject();
    }

}
