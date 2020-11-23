package data;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        jsonClass.put("imports",jc.getImplements());
        jsonClass.put("IsInterface", jc.getInterface());

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
        jsonDep.put("Dependencies", jc.getDependencies());
        return jsonDep;
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
