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
    Boolean IsAbstract;
    Integer LineCount;
    List<JavaVariable> GlobalVariables;
    List<JavaMethod> Methods;
    List<String> Extensions;
    List<String> Implements;
    List<JavaDependency> Dependencies;
    List<String> Imports;

    public JavaClass(){}

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

    public List<String> getExtensions() {
        return Extensions;
    }

    public void setExtensions(List<String> extensions) {
        Extensions = extensions;
    }

    public List<String> getImplements() {
        return Implements;
    }

    public void setImplements(List<String> anImplements) {
        Implements = anImplements;
    }

    public List<String> getImports() {
        return Imports;
    }

    public Boolean getAbstract() {
        return IsAbstract;
    }

    public void setAbstract(Boolean anAbstract) {
        IsAbstract = anAbstract;
    }

    public void setImports(List<String> imports) {
        Imports = imports;
    }

    public List<JavaDependency> getDependencies() {
        return Dependencies;
    }

    public void setDependencies(List<JavaDependency> dependencies) {
        Dependencies = dependencies;
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


}
