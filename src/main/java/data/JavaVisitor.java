package data;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.List;

public class JavaVisitor extends VoidVisitorAdapter<JavaClass> {
    //https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/index.html
    //Declared types
    //https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/com/github/javaparser/ast/visitor/VoidVisitorAdapter.html
    //Types of visitors what we can use

    @Override
    public void visit(MethodDeclaration md, JavaClass arg) {
        super.visit(md, arg);
        String returnType = md.getTypeAsString();
        String name = md.getNameAsString();
        List<String> listofModifiers = new ArrayList<String>();

        NodeList<Modifier> modifiers = md.getModifiers();
        for(Modifier m: modifiers){
            listofModifiers.add(m.getKeyword().asString());
        }
        List<JavaParameter> listofParameters = new ArrayList<JavaParameter>();
        NodeList<Parameter> parameters = md.getParameters();
        for(com.github.javaparser.ast.body.Parameter p: parameters){
            String pname = p.getNameAsString();
            String type = p.getTypeAsString();
            JavaParameter parameter = new JavaParameter(type,pname);
            listofModifiers.add(pname);
        }
        List<Method> methodList = arg.getMethods();
        Method method = new Method(returnType,name,listofModifiers,listofParameters);
        methodList.add(method);
        arg.setMethods(methodList);
        System.out.println("Method Name Printed: " + md.getName());
        }

    @Override
    public void visit(FieldDeclaration fd, JavaClass arg) {
        super.visit(fd, arg);
        String type = fd.getVariable(0).getTypeAsString();
        String name = fd.getVariable(0).getNameAsString();
        List<String> listofModifiers = new ArrayList<String>();

        NodeList<Modifier> modifiers = fd.getModifiers();
        for(Modifier m: modifiers){
            listofModifiers.add(m.getKeyword().asString());
        }

        Variable var = new Variable(type,name,listofModifiers);
        List<Variable> variables = arg.getGlobalVariables();
        variables.add(var);
        arg.setGlobalVariables(variables);
        System.out.println("Added variable to list: " + name);
    }

    @Override
    public void visit(ImportDeclaration id, JavaClass  arg) {
        super.visit(id, arg);
        System.out.println("ImportDeclaration Printed: " + id.getName());
    }



    @Override
    public void visit(ClassOrInterfaceDeclaration cid, JavaClass  arg) {
        super.visit(cid, arg);
        String className = cid.getNameAsString();
        Boolean IsInterface = cid.isInterface();
//get the links of the class
        List<String> links = new ArrayList<String>();
        NodeList<ClassOrInterfaceType> extendedTypes = cid.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implementedTypes = cid.getImplementedTypes();

        for(ClassOrInterfaceType it: implementedTypes){
            links.add(it.getName().getIdentifier());
        }

        for(ClassOrInterfaceType et: extendedTypes){
            links.add(et.getName().getIdentifier());
        }
        arg.setClassName(className);
        arg.setInterface(IsInterface);
        arg.setLinks(links);

        System.out.println("ImportDeclaration Printed: " + className);
    }


}
