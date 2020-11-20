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
    Boolean IsEnum;
    Integer LineCount;
    List<JavaVariable> GlobalVariables;
    List<JavaMethod> Methods;
    List<String> Links;

    public JavaClass(String cn , boolean i,boolean e, List<JavaVariable> gv, List<JavaMethod> m, List<String> l, Integer count){
        ClassName = cn;
        Interface = i;
        IsEnum = e;
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

    public Boolean getEnum() {
        return IsEnum;
    }

    public void setEnum(Boolean anEnum) {
        IsEnum = anEnum;
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

}
