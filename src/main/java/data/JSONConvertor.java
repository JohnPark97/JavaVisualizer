package data;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JSONConvertor {

    public JSONConvertor() {
    }

    public static JSONObject createJSON(List<JavaClass> list) {
        JSONObject jsonProject = new JSONObject();
        List<JSONObject> classes = new ArrayList<JSONObject>();
        List<JSONObject> links = new ArrayList<JSONObject>();
        for (JavaClass jc : list) {
            classes.add(JSONClass(jc));
            links.add(JSONDependency(jc));
        }
        jsonProject.put("classes", classes);
        jsonProject.put("links", links);

        return jsonProject;
    }

    public static JSONObject JSONClass(JavaClass jc) {
        JSONObject jsonClass = new JSONObject();
        jsonClass.put("name", jc.getClassName());
        jsonClass.put("line_count", jc.getLineCount());
        jsonClass.put("IsEnumeration",jc.getEnum());
        jsonClass.put("imports",jc.getImports());
        jsonClass.put("IsInterface", jc.getInterface());
        jsonClass.put("IsAbstract", jc.getAbstract());

        List<JSONObject> methods = new ArrayList<JSONObject>();
        for (JavaMethod m : jc.Methods) {
            methods.add(JSONMethod(m));
        }
        jsonClass.put("methods", methods);

        List<JSONObject> variables = new ArrayList<JSONObject>();
        for (JavaVariable v : jc.GlobalVariables) {
            variables.add(JSONVariable(v));
        }
        jsonClass.put("variables", variables);

        return jsonClass;
    }

    public static JSONObject JSONVariable(JavaVariable jv) {
        JSONObject jsonVariable = new JSONObject();
        jsonVariable.put("name", jv.getName());
        jsonVariable.put("type",jv.getType());
        jsonVariable.put("modifiers", jv.getModifiers());
        return jsonVariable;
    }

    public static JSONObject JSONDependency(JavaClass jc) {
        JSONObject jsonDep = new JSONObject();
        jsonDep.put("Class",jc.getClassName());
        jsonDep.put("Implementations", jc.getImplements());
        jsonDep.put("Extensions", jc.getExtensions());
        List<JSONObject> jsonDeps = new ArrayList<JSONObject>();
        for(JavaDependency jd: jc.getDependencies()){
            jsonDeps.add(JSONVariableDependency(jd));
        }
        jsonDep.put("Dependencies", jsonDeps);
        return jsonDep;
    }

    public static JSONObject JSONVariableDependency(JavaDependency jd){
        JSONObject jsonVariableDep = new JSONObject();
        jsonVariableDep.put("ClassNames",jd.getClassNames());
        jsonVariableDep.put("DependencyName", jd.getDependencyName());
        List<JSONObject> CollectionMap = new ArrayList<JSONObject>();
        HashMap<String,List<JavaCollection>> Collection = jd.getCollection();
        Set<String> keys = Collection.keySet();
         for(String k: keys){
             List<JSONObject> CollectionList = new ArrayList<JSONObject>();
             for(JavaCollection jc :Collection.get(k)){
                 CollectionList.add(JSONCollection(jc));
             }
             JSONObject CollectionObject = new JSONObject();
             CollectionObject.put(k,CollectionList);
             CollectionMap.add(CollectionObject);
        }
        jsonVariableDep.put("Collection",CollectionMap);

        return jsonVariableDep;
    }
    public static JSONObject JSONCollection(JavaCollection jc){
        JSONObject jsonCollection = new JSONObject();
        jsonCollection.put("Collection", jc.getCollection());
        jsonCollection.put("Object", jc.getObject());
        return jsonCollection;
    }

    public static JSONObject JSONMethod(JavaMethod jm) {
        JSONObject jsonMethod = new JSONObject();
        jsonMethod.put("name", jm.getName());
        jsonMethod.put("returnType", jm.getReturnType());
        jsonMethod.put("IsConstructor", jm.getConstructor());
        jsonMethod.put("modifiers", jm.getModifiers());
        List<JSONObject> parameters = new ArrayList<JSONObject>();
        for (JavaParameter p : jm.getParameterList()) {
            parameters.add(JSONParameter(p));
        }
        jsonMethod.put("lineCount", jm.getLineCount());
        jsonMethod.put("parameters", parameters);
        return jsonMethod;
    }

    public static JSONObject JSONParameter(JavaParameter jp) {
        JSONObject jsonParameter = new JSONObject();
        jsonParameter.put("name", jp.getName());
        jsonParameter.put("type", jp.getType());
        return jsonParameter;
    }

}
