package data;

import java.util.List;

public class JavaVariable {
    String type;
    String Name;
    List<String> Modifiers;

    public JavaVariable(String t, String n, List<String> modifiers){
        type = t;
        Name = n;
        Modifiers = modifiers;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return Name;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<String> getModifiers() {
        return Modifiers;
    }

    public void setModifiers(List<String> modifier) {
        Modifiers = modifier;
    }
}
