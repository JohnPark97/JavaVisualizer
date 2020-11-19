package data;

import java.util.List;

public class JavaClass {
    String ClassName;
    Boolean Interface;
    List<String> GlobalVariables;
    List<String> Methods;
    List<String> Dependencies;

    public JavaClass(){

    }

    public String getClassName() {
        return ClassName;
    }

    public Boolean getInterface() {
        return Interface;
    }

    public List<String> getGlobalVariables() {
        return GlobalVariables;
    }

    public List<String> getMethods() {
        return Methods;
    }

    public List<String> getDependencies() {
        return Dependencies;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public void setInterface(Boolean anInterface) {
        Interface = anInterface;
    }

    public void setGlobalVariables(List<String> globalVariables) {
        GlobalVariables = globalVariables;
    }

    public void setMethods(List<String> methods) {
        Methods = methods;
    }

    public void setDependencies(List<String> dependencies) {
        Dependencies = dependencies;
    }
}
