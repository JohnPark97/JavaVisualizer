package data;

public class JavaCollection {
    String Collection;
    String Object; //what type is in Collection<Object>

    public JavaCollection(String collection, String object) {
        Collection = collection;
        Object = object;
    }

    public String getCollection() {
        return Collection;
    }

    public void setCollection(String collection) {
        Collection = collection;
    }

    public String getObject() {
        return Object;
    }

    public void setObject(String object) {
        Object = object;
    }
}
