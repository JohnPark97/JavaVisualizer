package data;

import com.github.javaparser.ast.stmt.Statement;
import org.checkerframework.checker.units.qual.A;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

public class JavaCodeChecker {

    public int MaxClassLength = 200;//200
    public int MaxMethodLength = 50;//50
    public int MaxParameterLength = 3;//3
    public int MaxConditionalStmt = 2;//2
    public int MaxDereferences = 2;//2
    public int MaxDataClump = 2;


    public JavaCodeChecker() { }


    public void checkClass(List<JavaClass> javaClasses){
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        for(JavaClass jc: javaClasses){
            //put in methods for checking
            checkLongClass(jc);
            checkDataClump(jc);
            for(JavaMethod jm: jc.getMethods()) {
                checkLongMethod(jc,jm);
                checkTooManyParameters(jc,jm);
                checkComplicatedConditional(jc,jm);
                checkFeatureEnvy(jc,jm);
            }
        }
    }

    //check if method line count exceeds MaxMethodLength
    public void checkLongMethod(JavaClass jc, JavaMethod jm){
        Integer lineCount = jm.getLineCount();
        if(lineCount > MaxMethodLength){
            String codeSmell = jc.getInformation() + "Long Method: "+ jm.getName() +" Method is exceeding " + MaxMethodLength+" lines\n";
            jc.setInformation(codeSmell);

        }



    }
    //check if class line count exceeds MaxClassLength
    public void checkLongClass(JavaClass jc){
        Integer lineCount = jc.getLineCount();
        if(lineCount > MaxClassLength){
            String codeSmell = jc.getInformation() + "Long Class: "+ jc.getClassName() +" Class is exceeding " + MaxClassLength+" lines\n";
            jc.setInformation(codeSmell);
        }

    }

    //check if method has too many parameter (could be due to data clumping)
    public void checkTooManyParameters(JavaClass jc, JavaMethod jm){
        Integer numberOfParameters = jm.getParameterList().size();
        if(numberOfParameters > MaxParameterLength){
            String codeSmell = jc.getInformation() + "Too Many Parameters/Data Clump: "+ jm.getName() +
                    " Method has too many parameters. Exceeding the max parameter: "+MaxParameterLength +".\n";
            jc.setInformation(codeSmell);
        }
    }

    public void checkDataClump(JavaClass jc){
        String CodeSmells = "";
        List<List<JavaParameter>> ListofparameterList = new ArrayList<List<JavaParameter>>();
        List<String> MethodNames = new ArrayList<String>();
        List<JavaParameter> parameters = new ArrayList<JavaParameter>();

        for(JavaMethod jm: jc.getMethods()){
            ListofparameterList.add(jm.getParameterList());
            MethodNames.add(jm.getName());
        }
        for(List<JavaParameter> jplist: ListofparameterList){
            for(JavaParameter jp: jplist){
                if(!inParameterlist(parameters,jp)) {
                    parameters.add(jp);
                }
            }
        }
        HashMap<List<JavaParameter>,Integer> DataClumpTable = new HashMap<>();
        List<JavaParameter> jplist = new ArrayList<>();
        checkingClump(ListofparameterList,parameters,DataClumpTable,jplist);

        HashMap<List<JavaParameter>, Integer> DataClumpfinals = new HashMap<>();
        for(int i = 0; i < DataClumpTable.size(); i++ ) {
            Map<List<JavaParameter>, Integer> result = DataClumpTable.entrySet().stream()
                    .filter(map -> MaxDataClump <= map.getValue() && 2 <= map.getKey().size())
                    .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
            DataClumpfinals.putAll(result);

        }

        Set<List<JavaParameter>> jpkeys = DataClumpfinals.keySet();
        for(List<JavaParameter> key: jpkeys ){
            Integer count = DataClumpfinals.get(key);
            List<String> DataClumpMethods = new ArrayList<String>();
            List<String> DataClumpParameterNames = new ArrayList<String>();
            for(JavaParameter p: key){
                DataClumpParameterNames.add(p.getType()+" "+p.getName());
            }
            Integer i = 0;
            for(List<JavaParameter> jp: ListofparameterList){
                if(ListinParameterlist(key,jp)){
                   DataClumpMethods.add(MethodNames.get(i));
                }
                i++;
            }
            CodeSmells = CodeSmells + "Data Clump between Methods: " +DataClumpMethods + " with parameters: "
                    + DataClumpParameterNames + ".\n";
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }

    public void checkingClump(List<List<JavaParameter>> ListofparameterList,
                              List<JavaParameter> parameters,
                              HashMap<List<JavaParameter>,Integer> IDataClumpTable,
                              List<JavaParameter> list){
        List<JavaParameter> newParameters = new ArrayList<JavaParameter>();
        for(JavaParameter x: parameters){
            newParameters.add(x);
        }
        JavaParameter[] parameterArray = new JavaParameter[newParameters.size()];
        for(int i = 0; i < newParameters.size(); i++){
            parameterArray[i] = newParameters.get(i);
        }
        List<List<JavaParameter>> combo = getallCombos(parameterArray);
        if(combo.size() <= 50) {
            for (List<JavaParameter> jp : combo) {
                Integer count = 0;
                for (List<JavaParameter> jplist : ListofparameterList) {
                    if (ListinParameterlist(jp, jplist)) {
                        count++;
                    }
                }
                IDataClumpTable.put(jp, count);
            }
        }
    }
    public List<List<JavaParameter>>  getallCombos(JavaParameter[] args){
        List<List<JavaParameter>> powerSet = new LinkedList<List<JavaParameter>>();

        for (int i = 1; i <= args.length; i++)
            powerSet.addAll(combinationparameters(Arrays.asList(args),i));
        return powerSet;
    }

    public static List<List<JavaParameter>> combinationparameters(List<JavaParameter> values, int size) {

        if (0 == size) {
            return Collections.singletonList(Collections.<data.JavaParameter> emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<JavaParameter>> combination = new LinkedList<List<JavaParameter>>();

        JavaParameter actual = values.iterator().next();

        List<JavaParameter> subSet = new LinkedList<JavaParameter>(values);
        subSet.remove(actual);

        List<List<JavaParameter>> subSetCombination = combinationparameters(subSet, size - 1);

        for (List<JavaParameter> set : subSetCombination) {
            List<JavaParameter> newSet = new LinkedList<JavaParameter>(set);
            newSet.add(0, actual);
            combination.add(newSet);
        }

        combination.addAll(combinationparameters(subSet, size));

        return combination;
    }

    public boolean ListinParameterlist(List<JavaParameter> jpIn, List<JavaParameter>  jpOut){
        boolean isInList = true;
        for(JavaParameter p1 : jpIn){
            boolean exist = false;
            for(JavaParameter p2 : jpOut) {
                if (checksameParameter(p1, p2)) {
                    exist = true;
                }
            }
            if(!exist){
                isInList = false;
            }
        }
        return isInList;
    }

    public boolean inParameterlist(List<JavaParameter> jps, JavaParameter jp){
        boolean isInList = false;
        for(JavaParameter p : jps){
            if(checksameParameter(p,jp)){
                isInList = true;
            }
        }
        return isInList;
    }
    public boolean checksameParameter(JavaParameter jp1, JavaParameter jp2){
        String name1 = jp1.getName();
        String type1 = jp1.getType();
        String name2 = jp2.getName();
        String type2 = jp2.getType();
        return name1.equals(name2) && type1.equals(type2);
    }

   //check if method has Complicated conditional
    public void checkComplicatedConditional(JavaClass jc, JavaMethod jm) {
        String CodeSmells = "";
        for (Statement s : jm.getStatements()) {
            System.out.println(s);
            ComplicatedConditionalVisitor javaCodeCheckerVisitor = new ComplicatedConditionalVisitor();
            List<ConditionalTracker> numberofConditional = new ArrayList<ConditionalTracker>();
            s.accept(javaCodeCheckerVisitor, numberofConditional);
            for(ConditionalTracker c: numberofConditional) {
                if (c.getConditionals().size() >= MaxConditionalStmt) {
                    CodeSmells = CodeSmells + "Complicated Conditional: " + "Conditional is too complicated on lines: "
                            + c.getStartLine() + " - " + c.getEndLine() + ".\n";
                }
            }
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }

    //check if method has FeatureEnvy
    public void checkFeatureEnvy(JavaClass jc, JavaMethod jm) {
        String CodeSmells = "";
        for (Statement s : jm.getStatements()) {
            System.out.println(s);
            FeatureEnvyVisitor featureEnvyVisitor = new FeatureEnvyVisitor();
            List<FeatureEnvyTracker> numberofFeatureEnvy = new ArrayList<FeatureEnvyTracker>();
            s.accept(featureEnvyVisitor, numberofFeatureEnvy);
            for(FeatureEnvyTracker f: numberofFeatureEnvy) {
                if (f.getNumberofDereferences() >= MaxDereferences) {
                    CodeSmells = CodeSmells + "Feature Envy: " + "In "+jm.getName()+ " Method: Too many dereferences on lines: "
                            + f.getStartLine() + " - " + f.getEndLine() + ".\n";
                }
            }
        }
        String finalcodeSmell = jc.getInformation() + CodeSmells;
        jc.setInformation(finalcodeSmell);
    }


}
