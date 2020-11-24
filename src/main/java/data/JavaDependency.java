package data;

import java.util.HashMap;
import java.util.List;

public class JavaDependency {
    String DependencyName;
    HashMap<String,List<JavaCollection>> Collection;
    List<String> ClassNames;

    public JavaDependency(String dependencyName, HashMap<String, List<JavaCollection>> collection, List<String> classNames) {
        DependencyName = dependencyName;
        Collection = collection;
        ClassNames = classNames;
    }

    public String getDependencyName() {
        return DependencyName;
    }

    public void setDependencyName(String dependencyName) {
        DependencyName = dependencyName;
    }

    public HashMap<String, List<JavaCollection>> getCollection() {
        return Collection;
    }

    public void setCollection(HashMap<String, List<JavaCollection>> collection) {
        Collection = collection;
    }

    public List<String> getClassNames() {
        return ClassNames;
    }

    public void setClassNames(List<String> classNames) {
        ClassNames = classNames;
    }
}
