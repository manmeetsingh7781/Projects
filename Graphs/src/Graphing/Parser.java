package Graphing;


import java.util.*;


public class Parser {

    // 5x^2 => Split with the ^ in half and then write it as Math.pow(5*x, 2)
    // 5x^2 + 9x => Math.pow(5*x, 2) + 9*x

    private String fraction;
    private final String original_notation;
    public ArrayList<String> seprated;
    // String function_name, isFunction = true or Constant = flase
    private final HashMap<String, Boolean> mathMethods;
    private final Set<String> variables;


    Parser(String fraction) {

        mathMethods = new HashMap<>();
        mathMethods.put("sqrt", true);
        mathMethods.put("exp", false);
        mathMethods.put("PI", false);
        mathMethods.put("E", false);
        mathMethods.put("abs", true);
        mathMethods.put("sin", true);
        mathMethods.put("tan", true);
        mathMethods.put("acos", true);
        mathMethods.put("LN2", true);
        mathMethods.put("LN10", true);
        mathMethods.put("LOG2E", true);
        mathMethods.put("LOG10E", true);
        mathMethods.put("acosh", true);
        mathMethods.put("asin", true);
        mathMethods.put("atan", true);
        mathMethods.put("log", true);
        mathMethods.put("atanh", true);
        mathMethods.put("cbrt", true);
        mathMethods.put("cos", true);


        seprated = new ArrayList<>();
        this.fraction = fraction;
        this.original_notation = fraction;
        variables = new HashSet<>();
        initFraction();
        loadVariables();
        this.fraction = parse();


    }

    private void initFraction() {
        Collections.addAll(seprated, fraction.split(" "));
    }

    private String updateNotation(String fraction) {

        String temp;

        fraction = fraction.replaceAll(" ","");
        for (Map.Entry<String, Boolean> mathFun : mathMethods.entrySet()) {

            String key = mathFun.getKey();
            if (fraction.contains(key)) {

                String beforeFunction = fraction.substring(0, fraction.indexOf(key));
                String afterFunction = fraction.substring(fraction.indexOf(key));

//                if (fraction.startsWith("-")) {
                    temp = beforeFunction + "1 * Math." + afterFunction;
//                } else {
//                    temp = beforeFunction + "Math." + afterFunction;
//                }

                if (!fraction.contains("(") && mathFun.getValue()) {
                    temp += "(";
                }

                fraction = temp;
            } else {

                if (fraction.contains("^")) {
                    fraction = pow(fraction);

                }
            }
        }

        return fraction;
    }

    private String parse() {

        // E => Math.E
        // x^2 => Math.pow(x, 2)
        // abs(x^2) => x^2 => Math.pow(x, 2) => Math.abs(Math.pow(x,2))

        boolean containsFunc;

        for (int i = 0; i < seprated.size(); i++) {

            // if the fraction contains ( this means it maybe a composite function that we need to solve before we solve outer
            containsFunc = seprated.get(i).contains("(");

            if (containsFunc) {
                String[] sliced = seprated.get(i).split("\\(");

                for (int s = 0; s < sliced.length; s++) {
                    if ((sliced[s].endsWith(")") && !sliced[s].startsWith("("))) {
                        sliced[s] = sliced[s].replace(")", "");
                    }
                    if (sliced[s].length() > 0) sliced[s] = updateNotation(sliced[s]);
                }
                String formatted = String.join("", sliced) + ")";
                seprated.set(i, formatted);
            } else {
                seprated.set(i, updateNotation(seprated.get(i)));
            }

        }

        return String.join(" ", seprated);
    }


    private String addSpaces(){

        String result = fraction;
        System.out.println(fraction);
        return  result;


    }

    private String parseImporved(){
        String result = this.fraction;
        String[] s = result.split(" ");
        for(int i = 0;  i < s.length; i++){

            if(s[i].contains("^")) s[i] = pow(s[i]);

            if(mathMethods.containsKey(s[i].split("\\(")[0])){
                s[i] = "Math." + s[i];
            }
        }
        return String.join(" ",s);
    }





    public String pow(String fraction) {


        String functionPreFix = "Math.pow";


        // there are 3 cases
        // 1 => -(x^2)   => -1 *
        // 2 => (-x^2)   => -1 *
        // 3 => (-x)^2   => - - +

        // it means we have negative expression
        boolean isNegative = fraction.startsWith("-") || fraction.startsWith("-", 1);
        boolean haveParentheses = fraction.startsWith("(") || (fraction.startsWith("(", 1) && fraction.endsWith(")"));
        boolean isOverallNeg = fraction.indexOf('^') < fraction.indexOf(')') || fraction.startsWith("-");


        fraction = fraction.replaceAll("\\^", ",");

        if (isNegative) {

            if (haveParentheses) {
                // remove the negative and place it up front of the expression
                if (fraction.startsWith("-")) {
                    fraction = fraction.replaceFirst("-", "");
                    return "-1 * " + functionPreFix + String.join(",", fraction);
                } else {
                    return functionPreFix + String.join(",", fraction);
                }
            } else {

                if (!isOverallNeg) {
                    return functionPreFix + "(" + String.join(",", fraction) + ")";
                } else {
                    functionPreFix = functionPreFix + "(" + String.join(",", fraction.replaceFirst("-", "")) + ")";
                    return "-1 * " + functionPreFix;
                }
            }

            // if the fraction is positive
        } else {

            if (haveParentheses) {
                if (!(fraction.contains("(") && fraction.endsWith(")")))
                    return functionPreFix + "(" + String.join(",", fraction) + ")";
                return functionPreFix + fraction;
            } else {
                fraction = fraction.replace("(", "").replace(")", "");
                return functionPreFix + "(" + String.join(",", fraction) + ")";
            }
        }
    }

    public String getFraction() {
        return this.fraction;
    }

    private void loadVariables() {
        char letter;
        String s = "";

        for (int i = 0; i < original_notation.length(); i++) {

            letter = original_notation.charAt(i);

            // check if the variables in the fraction exists
            if (letter >= 'a' && letter <= 'z' || letter >= 'A' && letter <= 'Z')
                s += letter;

            else {
                if (s.length() > 0) {
                    variables.add(s);
                    s = "";
                }
            }

        }

        // Add the dangling item in the end of Fraction
        if (s.length() > 0) {
            variables.add(s);
        }

        // Remove the Math functions that are already defined
        // and we are left of with variables that user has defined
        for (Map.Entry<String, Boolean> m : mathMethods.entrySet()) {
            variables.remove(m.getKey());
        }

    }


    public Set<String> getVariables() {
        return this.variables;
    }


}
