package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathExpression {

    String expression;

    String predefinedExpressions = "(?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\))|(?=\\))|(?<=sin)|(?=sin)|(?<=\\()|(?=\\()|(?<=cos)|(?=cos)|(?<=\\*)|(?=\\*)";

    String[] splits;

    ArrayList<String> expressionList;

    int variable = 0;

    public MathExpression(String expression) {
        this.expression = expression;
        if (this.expression != null) {
            splitter();
        }
    }

    private void splitter() {
        expression = expression.replaceAll("\\s+","");
        expression = expression.trim();
        splits = expression.split(predefinedExpressions);
        expressionList = new ArrayList<String>(Arrays.asList(splits));
        getValue(1);
    }

    public int getValue(int t) {
        variable = t;
        ArrayList<String> newExpr = new ArrayList<>(expressionList);
        for (String s : newExpr) {
            if (s.equals("t")) {
                newExpr.set(newExpr.indexOf("t"), Double.toString(variable));
            }
        }
        return (int)Math.round(Float.parseFloat(getValue(newExpr).get(0)));
    }

    private int getLastBracketIndex(ArrayList<String> expr, int startingPos) {
        int numberOfBrackets = 1;
        for (int i = startingPos + 1; i < expr.size(); i++) {
            if (expr.get(i).contains("(")) {
                numberOfBrackets++;
            } else if (expr.get(i).contains(")")) {
                numberOfBrackets--;
            }
            if (numberOfBrackets == 0) {
                return i;
            }
        }

        return -1;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private ArrayList<String> getValue(ArrayList<String> expr) {
        if (expr.size() == 1) {
            return expr;
        } else {
            if (expr.contains("(")) {
                int index = expr.indexOf("(");
                int lastIndex = getLastBracketIndex(expr, index);
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index));
                newList.addAll(getValue(new ArrayList<String>(expr.subList(index + 1, lastIndex))));
                newList.addAll(new ArrayList<String>(expr.subList(lastIndex + 1, expr.size())));
                return getValue(newList);
            } else if (expr.contains("sin")) {
                int index = expr.indexOf("sin");
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index));
                newList.add(Double.toString(Math.sin(Double.parseDouble(expr.get(index+1)))));
                newList.addAll(new ArrayList<String>(expr.subList(index + 2, expr.size())));
                return getValue(newList);
            } else if (expr.contains("cos")) {
                int index = expr.indexOf("cos");
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index));
                newList.add(Double.toString(Math.cos(Double.parseDouble(expr.get(index+1)))));
                newList.addAll(new ArrayList<String>(expr.subList(index + 2, expr.size())));
                return getValue(newList);
            } else if (expr.contains("*")) {
                int index = expr.indexOf("*");
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index-1));
                newList.add(Double.toString(Double.parseDouble(expr.get(index-1)) * Double.parseDouble(expr.get(index+1))));
                newList.addAll(new ArrayList<String>(expr.subList(index + 2, expr.size())));
                return getValue(newList);
            } else if (expr.contains("+")) {
                int index = expr.indexOf("+");
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index-1));
                newList.add(Double.toString(Double.parseDouble(expr.get(index-1)) + Double.parseDouble(expr.get(index+1))));
                newList.addAll(new ArrayList<String>(expr.subList(index + 2, expr.size())));
                return getValue(newList);
            } else if (expr.contains("-")) {
                int index = expr.indexOf("-");
                ArrayList<String> newList = new ArrayList<String>(expr.subList(0, index-1));
                newList.add(Double.toString(Double.parseDouble(expr.get(index-1)) - Double.parseDouble(expr.get(index+1))));
                newList.addAll(new ArrayList<String>(expr.subList(index + 2, expr.size())));
                return getValue(newList);
            } else {
                double total = 1;
                for (String s : expr) {
                    total *= Double.parseDouble(s);
                }
                ArrayList<String> newList = new ArrayList<String>(
                        Arrays.asList(Double.toString(total)));
                return newList;
            }
        }
    }

}
