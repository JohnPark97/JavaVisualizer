package data;

import java.util.List;

public class DataClumpTracker {
    List<List<String>> MethodCombos;
    List<List<JavaParameter>> ParameterCombos;

    public DataClumpTracker(List<List<String>> methodCombos, List<List<JavaParameter>> parameterCombos) {
        this.MethodCombos = methodCombos;
        this.ParameterCombos = parameterCombos;
    }

    public List<List<String>> getMethodCombos() {
        return MethodCombos;
    }

    public void setMethodCombos(List<List<String>> methodCombos) {
        MethodCombos = methodCombos;
    }

    public List<List<JavaParameter>> getParameterCombos() {
        return ParameterCombos;
    }

    public void setParameterCombos(List<List<JavaParameter>> parameterCombos) {
        ParameterCombos = parameterCombos;
    }
}
