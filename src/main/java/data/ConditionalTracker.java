package data;

import com.github.javaparser.Position;

import java.util.List;

public class ConditionalTracker {
    Position StartLine;
    Position EndLine;
    Integer NumberofConditional;
    List<String> Conditionals;

    public ConditionalTracker(Position startLine, Position endLine, Integer numberofConditional, List<String> conditionals) {
        StartLine = startLine;
        EndLine = endLine;
        NumberofConditional = numberofConditional;
        Conditionals = conditionals;
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

    public Integer getNumberofConditional() {
        return NumberofConditional;
    }

    public void setNumberofConditional(Integer numberofConditional) {
        NumberofConditional = numberofConditional;
    }

    public List<String> getConditionals() {
        return Conditionals;
    }

    public void setConditionals(List<String> conditionals) {
        Conditionals = conditionals;
    }
}
