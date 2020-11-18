package data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.SourceRoot;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Inspector {
    public static void main(String[] args) throws IOException {
        //get the files from project 1
        File projectDirectory = new File("assets/project1");
        SourceRoot source = new SourceRoot(projectDirectory.toPath());
        //Tries to parse all .java files under the source root recursively,
        // and returns all files ever parsed with this source root.
        List<ParseResult<CompilationUnit>> parseResults = source.tryToParse();//this has all the files parsed from our project 1;
        List<CompilationUnit> compilationUnits = new ArrayList<CompilationUnit>();

//checking if the parse was successful, then putting all the compilationUnits into a list
            for (ParseResult<CompilationUnit> parsecu : parseResults) {
                if(parsecu.isSuccessful()) {
                    compilationUnits.add(parsecu.getResult().get());
                }
            }

            for(CompilationUnit cu : compilationUnits){

            }

        System.out.println(compilationUnits.get(1).getImports());
    }
}