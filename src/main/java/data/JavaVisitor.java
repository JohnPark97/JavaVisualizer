package data;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaVisitor extends VoidVisitorAdapter<JavaClass> {
    //https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/index.html
    //Declared types
    //https://www.javadoc.io/doc/com.github.javaparser/javaparser-core/latest/com/github/javaparser/ast/visitor/VoidVisitorAdapter.html
    //Types of visitors what we can use

    @Override
    public void visit(MethodDeclaration md, JavaClass arg) {
        super.visit(md, arg);
        System.out.println("Method Name Printed: " + md.getName());
        }

    @Override
    public void visit(FieldDeclaration fd, JavaClass arg) {
        super.visit(fd, arg);

    }

    @Override
    public void visit(ImportDeclaration id, JavaClass  arg) {
        super.visit(id, arg);
        System.out.println("ImportDeclaration Printed: " + id.getName());
    }



    @Override
    public void visit(ClassOrInterfaceDeclaration cid, JavaClass  arg) {
        super.visit(cid, arg);
        System.out.println("ImportDeclaration Printed: " + cid.getName());
    }


}
