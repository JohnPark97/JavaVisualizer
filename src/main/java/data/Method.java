package data;

import java.util.List;

public class Method {
    String returnType;
    String name;
    List<String> modifiers;
    List<JavaParameter> parameterList;

    public Method(String returnType, String name, List<String> modifiers, List<JavaParameter> parameterList) {
        this.returnType = returnType;
        this.name = name;
        this.modifiers = modifiers;
        this.parameterList = parameterList;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public List<JavaParameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<JavaParameter> parameterList) {
        this.parameterList = parameterList;
    }
}
