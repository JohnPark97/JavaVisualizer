package data;

import com.github.javaparser.Position;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class FeatureEnvyTracker {
    Position StartLine;
    Position EndLine;
    Integer numberofDereferences;

    public FeatureEnvyTracker(Position startLine, Position endLine, Integer numberofDereferences) {
        StartLine = startLine;
        EndLine = endLine;
        this.numberofDereferences = numberofDereferences;
    }

    public Position getStartLine() {
        return StartLine;
    }

    public void setStartLine(Position startLine) {
        StartLine = startLine;
    }

    public Position getEndLine() {
        return EndLine;
    }

    public void setEndLine(Position endLine) {
        EndLine = endLine;
    }

    public Integer getNumberofDereferences() {
        return numberofDereferences;
    }

    public void setNumberofDereferences(Integer numberofDereferences) {
        this.numberofDereferences = numberofDereferences;
    }
}

