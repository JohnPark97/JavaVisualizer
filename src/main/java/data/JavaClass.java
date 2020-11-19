package data;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.errorprone.annotations.Var;

import java.util.List;

public class JavaClass {
    String ClassName;
    Boolean Interface;
    List<String> links;
    List<Variable> GlobalVariables;
    List<String> Methods;
    List<String> Links;

    public JavaClass(String cn , boolean i, List<Variable> gv, List<String> m, List<String> l){
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

    public List<Variable> getGlobalVariables() {
        return GlobalVariables;
    }

    public List<String> getMethods() {
        return Methods;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public void setInterface(Boolean anInterface) {
        Interface = anInterface;
    }

    public void setGlobalVariables(List<Variable> globalVariables) {
        GlobalVariables = globalVariables;
    }

    public void setMethods(List<String> methods) {
        Methods = methods;
    }

    public void setLinks(List<String> links) {
        Links = links;
    }

}
