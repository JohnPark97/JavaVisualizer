package data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        Integer lineCount = md.getEnd().get().line - md.getBegin().get().line;

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
            listofParameters.add(parameter);
        }
        JavaMethod method = new JavaMethod(returnType,name,false,listofModifiers,listofParameters, lineCount);
        arg.getMethods().add(method);
        System.out.println("Method Name Printed: " + md.getName());
        }

    @Override
    public void visit(FieldDeclaration fd, JavaClass arg) {
        super.visit(fd, arg);
        String type = fd.getVariable(0).getTypeAsString();
        Type varType = fd.getVariable(0).getType();

        setDependencies(varType,arg);

        List<String> listofModifiers = new ArrayList<String>();
        NodeList<Modifier> modifiers = fd.getModifiers();
        for(Modifier m: modifiers){
            listofModifiers.add(m.getKeyword().asString());
        }

        NodeList<VariableDeclarator> names = fd.getVariables();
        for(VariableDeclarator n: names){
            String name = n.getNameAsString();
            JavaVariable var = new JavaVariable(type,name,listofModifiers);
            arg.getGlobalVariables().add(var);
            System.out.println("Added variable to list: " + name);
        }
    }

    public void setDependencies(Type t, JavaClass arg){
        System.out.println("Checking dependencies");
        String DependencyName = t.asString();
        JavaDependency jd = new JavaDependency(DependencyName,new HashMap<String, List<JavaCollection>>(),new ArrayList<String>());
        if(!t.isPrimitiveType()) {
            getCollection(t, jd);
            arg.getDependencies().add(jd);
            System.out.println("Finishes checking dependencies");
        }
    }

    public void getCollection(Type t,JavaDependency jd){
        Type type = t;
        Type elementType = t.getElementType();
        while(!(elementType.asString()).equals(type.asString())){
            List<JavaCollection> innerBrackets = new ArrayList<JavaCollection>();
            innerBrackets.add(new JavaCollection(type.asString(), elementType.asString()));
            jd.getCollection().put(type.asString(),innerBrackets);
            type = elementType;
            elementType = type.getElementType();
        }
            Optional<NodeList<Type>> arguments = type.asClassOrInterfaceType().getTypeArguments();
        if(!arguments.isEmpty()){
            List<JavaCollection> innerTypes = new ArrayList<JavaCollection>();
            for(int i = 0; i < arguments.get().size();i++) {
                Type innertype = arguments.get().get(i);
                innerTypes.add(new JavaCollection(type.asClassOrInterfaceType().getNameAsString(),innertype.asString()));
                getCollection(innertype,jd);
            }
            jd.getCollection().put(t.asString(),innerTypes);
        }else {
            jd.getClassNames().add(t.asString());
        }
    }

    @Override
    public void visit(ConstructorDeclaration cd, JavaClass arg){
        super.visit(cd, arg);
        String name = cd.getNameAsString();
        String returnType = "";
        List<String> listofModifiers = new ArrayList<String>();
        Integer lineCount = cd.getEnd().get().line - cd.getBegin().get().line;

        NodeList<Modifier> modifiers = cd.getModifiers();
        for(Modifier m: modifiers){
            listofModifiers.add(m.getKeyword().asString());
        }
        List<JavaParameter> listofParameters = new ArrayList<JavaParameter>();
        NodeList<Parameter> parameters = cd.getParameters();
        for(com.github.javaparser.ast.body.Parameter p: parameters){
            String pname = p.getNameAsString();
            String type = p.getTypeAsString();
            JavaParameter parameter = new JavaParameter(type,pname);
            listofParameters.add(parameter);
        }
        JavaMethod method = new JavaMethod(returnType,name,true,listofModifiers,listofParameters,lineCount);
        arg.getMethods().add(method);

        System.out.println("Constructor Name Printed: " + cd.getName());
    }

    @Override
    public void visit(EnumDeclaration ed, JavaClass  arg) {
        super.visit(ed, arg);
        String EnumName = ed.getNameAsString();
        Integer count =  ed.getEnd().get().line - ed.getBegin().get().line ;
        List<String> links = new ArrayList<String>();
        NodeList<ClassOrInterfaceType> implementedTypes = ed.getImplementedTypes();

        for(ClassOrInterfaceType it: implementedTypes){
            links.add(it.getName().getIdentifier());
        }

        arg.setClassName(EnumName);
        arg.setLineCount(count);
        arg.setEnum(ed.isEnumDeclaration());
        arg.setInterface(false);
        arg.setImplements(links);
        arg.setExtensions(new ArrayList<String>());
        System.out.println("EnumDeclaration Printed: " + ed.getName());
    }

    @Override
    public void visit(ImportDeclaration id, JavaClass  arg) {
        super.visit(id, arg);
        String name = id.getNameAsString();
        arg.getImports().add(name);
        System.out.println("ImportDeclaration Printed: " + id.getNameAsString());
    }



    @Override
    public void visit(ClassOrInterfaceDeclaration cid, JavaClass  arg) {
        super.visit(cid, arg);
        String className = cid.getNameAsString();
        Boolean IsInterface = cid.isInterface();

        Integer count =  cid.getEnd().get().line - cid.getBegin().get().line ;

//get the links of the class
        List<String> extension= new ArrayList<String>();
        List<String> implement= new ArrayList<String>();
        NodeList<ClassOrInterfaceType> extendedTypes = cid.getExtendedTypes();
        NodeList<ClassOrInterfaceType> implementedTypes = cid.getImplementedTypes();

        for(ClassOrInterfaceType it: implementedTypes){
            extension.add(it.getName().getIdentifier());
        }

        for(ClassOrInterfaceType et: extendedTypes){
            implement.add(et.getName().getIdentifier());
        }
        arg.setClassName(className);
        arg.setInterface(IsInterface);
        arg.setLineCount(count);
        arg.setExtensions(extension);
        arg.setImplements(implement);

        System.out.println("ClassDeclaration Printed: " + className);
    }


}
