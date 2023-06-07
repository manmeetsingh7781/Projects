package Graphing;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


public class Function {

    private Set<String> variables;
    private String fraction;
    private ScriptEngine scriptEngine;

    public Function(String fraction) {
        Parser parser = new Parser(fraction);
        this.fraction = parser.getFraction();
        this.variables = parser.getVariables();

        // loading the engine
        this.scriptEngine = new ScriptEngineManager().getEngineByName("js");
    }

    private String replaceInputs(Number... inputs) {
        String var_decl = "";
        Iterator<String> iterable = variables.iterator();
        for (int i = 0; i < inputs.length && iterable.hasNext(); i++) {
            var_decl += "var " + iterable.next() + " = " + inputs[i] + ";\n";
        }
        return var_decl + fraction;
    }


    public Number solve(Number... input) throws Exception {


        /*
            check if the input that is passed
            and variables in the fraction are same of size


            for ex:
                    f(x, y) = x * y
                    => f(2, 1) = 2 * 1 = 2

            Variables must be equal to the size of input

         */


        // if the number of input and variables does not match then we can not process further so we throw an error
        if (variables.size() > input.length) {
            throw new ArrayIndexOutOfBoundsException("Size of variables and input does not match");
        }
        return (Number) this.scriptEngine.eval(replaceInputs(input));

    }

    // generates the table of f(x) within given range
    public ArrayList<Number> generateTable(int from, int to, Number... inputValues) throws Exception {
        ArrayList<Number> table = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            table.add(this.solve(inputValues));
            inputValues[i % inputValues.length] = inputValues[i % inputValues.length].doubleValue() + 1;
        }
        return table;
    }


    public String help() {
        return "" +
                " * E\t\treturns Euler's number\n" +
                " * PI\t\treturns PI\n" +
                " * SQRT2\t\treturns the square root of 2\n" +
                " * SQRT1_2\t\treturns the square root of 1/2\n" +
                " * LN2\t\treturns the natural logarithm of 2\n" +
                " * LN10\t\treturns the natural logarithm of 10\n" +
                " * LOG2E\t\treturns base 2 logarithm of E\n" +
                " * LOG10E\t\treturns base 10 logarithm of E\n" +
                " * abs(x)\t\tReturns the absolute value of x\n" +
                " * acos(x)\t\tReturns the arccosine of x, in radians\n" +
                " * acosh(x)\t\tReturns the hyperbolic arccosine of x\n" +
                " * asin(x)\t\tReturns the arcsine of x, in radians\n" +
                " * asinh(x)\t\tReturns the hyperbolic arcsine of x\n" +
                " * atan(x)\t\tReturns the arctangent of x as a numeric value between -PI/2 and PI/2 radians\n" +
                " * atan2(y, x)\t\tReturns the arctangent of the quotient of its arguments\n" +
                " * atanh(x)\t\tReturns the hyperbolic arctangent of x\n" +
                " * cbrt(x)\t\tReturns the cubic root of x\n" +
                " * ceil(x)\t\tReturns x, rounded upwards to the nearest integer\n" +
                " * cos(x)\t\tReturns the cosine of x (x is in radians)\n" +
                " * cosh(x)\t\tReturns the hyperbolic cosine of x\n" +
                " * exp(x)\t\tReturns the value of Ex\n" +
                " * floor(x)\t\tReturns x, rounded downwards to the nearest integer\n" +
                " * log(x)\t\tReturns the natural logarithm (base E) of x\n" +
                " * max(x, y, z, ..., n)\t\tReturns the number with the highest value\n" +
                " * min(x, y, z, ..., n)\t\tReturns the number with the lowest value\n" +
                " * pow(x, y)\t\tReturns the value of x to the power of y\n" +
                " * random()\t\tReturns a random number between 0 and 1\n" +
                " * round(x)\t\tRounds x to the nearest integer\n" +
                " * sin(x)\t\tReturns the sine of x (x is in radians)\n" +
                " * sinh(x)\t\tReturns the hyperbolic sine of x\n" +
                " * sqrt(x)\t\tReturns the square root of x\n" +
                " * tan(x)\t\tReturns the tangent of an angle\n" +
                " * tanh(x)\t\tReturns the hyperbolic tangent of a number\n" +
                " * trunc(x)\t\tReturns the integer part of a number (x)";

    }
}
