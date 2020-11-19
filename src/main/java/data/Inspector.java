package data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.utils.SourceRoot;
import com.sun.jdi.InterfaceType;
import javassist.compiler.ast.Visitor;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inspector {
    public static void main(String[] args) throws IOException {
        //get the files from project 1
        File projectDirectory = new File("assets/project1");
        SourceRoot source = new SourceRoot(projectDirectory.toPath());
        //Tries to parse all .java files under the source root recursively,
        // and returns all files ever parsed with this source root.
        List<ParseResult<CompilationUnit>> parseResults = source.tryToParse();//this has all the files parsed from our project 1;
        List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

        System.out.println(parseResults.size());
//checking if the parse was successful, then putting all the compilationUnits into a list
            for (ParseResult<CompilationUnit> parsecu : parseResults) {
               // if(parsecu.isSuccessful()) {
                    compilationUnits.add(parsecu.getResult().get());
             //   }
            }

        // Prints out the ast structure:
        YamlPrinter printer = new YamlPrinter(true);
 //       System.out.println(printer.output(compilationUnits.get(1)));

            System.out.println(compilationUnits.size());

            List<JavaClass> ListOfJavaClasses = new ArrayList<JavaClass>();

            for(CompilationUnit cu : compilationUnits){
  //              System.out.println(cu);
                //visit the ast structure and get the info that we want
                JavaClass javaClass = new JavaClass(null, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                JavaVisitor visitor = new JavaVisitor();
                cu.accept(visitor,javaClass);
                //add it to the list of java classes
                ListOfJavaClasses.add(javaClass);

            }


            List<ClassOrInterfaceDeclaration> className = compilationUnits.get(1).findAll(ClassOrInterfaceDeclaration.class);
           // System.out.println(className.get(0).getName());

            List<FieldDeclaration> fields = compilationUnits.get(1).findAll(FieldDeclaration.class);
           // System.out.println(fields);

            List<ImportDeclaration> imports = compilationUnits.get(1).findAll(ImportDeclaration.class);
           // System.out.println(imports);

            List<MethodDeclaration> methods = compilationUnits.get(1).findAll(MethodDeclaration.class);
           // System.out.println(methods.get(1).getDeclarationAsString());



 //       iSystem.out.println(compilationUnits.get(1).findAll(MethodDeclaration.class));



    }
}