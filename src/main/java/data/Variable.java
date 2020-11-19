package data;

public class Variable {
    String type;
    String Name;
    String Modifier;

    public Variable(String t, String n, String modifier){
        type = t;
        Name = n;
        Modifier = modifier;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return Name;
    }

    public String getModifier() {
        return Modifier;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setModifier(String modifier) {
        Modifier = modifier;
    }
}
