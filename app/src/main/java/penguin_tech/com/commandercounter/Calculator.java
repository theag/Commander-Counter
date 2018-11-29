package penguin_tech.com.commandercounter;

public class Calculator {

    public static final String NUMBER_PATTERN = "[\\d,\\.]+";
    public static final String GOOD_PATTERN = "[\\d,\\.,\\^,\\*,\\+,\\-,\\(,\\)]+";

    private String expression;
    private boolean vhasResult;
    private double result;

    public Calculator(String expression) throws CalculatorException {
        this.expression = expression.replaceAll(" ", "");
        if(!this.expression.matches(GOOD_PATTERN)) {
            throw new CalculatorException(0, "Expression contains illegal characters");
        }
        finalCalculation();
    }

    public Calculator() {
        vhasResult = false;
    }

    public boolean hasResult() {
        return vhasResult;
    }

    public String partialCalculation(String expression) {
        vhasResult = false;
        this.expression = expression;
        if(!expression.matches(GOOD_PATTERN)) {
            return "";
        }
        //brackets
        int open = 0;
        int index = expression.indexOf("(");
        while(index >= 0) {
            open++;
            index = expression.indexOf("(");
        }
        int closed = 0;
        index = expression.indexOf("(");
        while(index >= 0) {
            closed++;
            index = expression.indexOf("(");
        }
        if(closed > open) {
            return "";
        }
        try {
            result = compute(expression);
            vhasResult = true;
            return printResult();
        } catch(CalculatorException eRef) {
            return "";
        }
    }

    public double getResult() {
        return result;
    }

    public String printResult() {
        if(!vhasResult) {
            return "";
        } else if(Math.floor(result) == result) {
            return ""+((int)result);
        } else {
            return ""+result;
        }
    }

    public final void finalCalculation() throws CalculatorException {
        if(expression.isEmpty()) {
            result = 0;
        } else {
            result = compute(expression);
        }
    }

    private double compute(String expression) throws CalculatorException {
        try {
            return Double.parseDouble(expression);
        } catch(NumberFormatException eRef) {

        }
        //brackets
        int index1 = expression.indexOf("(");
        int index2;
        while(index1 >= 0) {
            int open = 1;
            index2 = index1;
            while(open > 0) {
                index2 = indexMin(expression.indexOf("(", index2+1), expression.indexOf(")", index2+1));
                if(index2 < 0) {
                    index2 = expression.length() - 1;
                    break;
                }
                if(expression.charAt(index2) == ')') {
                    open--;
                } else {
                    open++;
                }
            }
            if(open < 0) {
                throw new CalculatorException(1, "There is a close bracket without a matching open.");
            }
            expression = expression.substring(0, index1) + compute(expression.substring(index1+1, index2)) + expression.substring(index2+1);
            index1 = expression.indexOf("(");
        }
        //exponents
        int index0 = expression.indexOf("^");
        double a, b;
        while(index0 > 0 && index0 < expression.length() - 1) {
            index1 = index0-1;
            while(index1 > 0 && expression.substring(index1-1, index0).matches(NUMBER_PATTERN)) {
                index1--;
            }
            index2 = index0 + 2;
            while(index2 < expression.length() && expression.substring(index0+1, index2+1).matches(NUMBER_PATTERN)) {
                index2++;
            }
            a = Double.parseDouble(expression.substring(index1, index0));
            b = Double.parseDouble(expression.substring(index0+1, index2));
            expression = expression.substring(0, index1) + Math.pow(a, b) + expression.substring(index2);
            index0 = expression.indexOf("^");
        }
        if(index0 == expression.length() - 1) {
            expression = expression.substring(0, index0);
        }
        //division multiplication
        index0 = indexMin(expression.indexOf("/"), expression.indexOf("*"));
        while(index0 > 0 && index0 < expression.length() - 1) {
            index1 = index0-1;
            while(index1 > 0 && expression.substring(index1-1, index0).matches(NUMBER_PATTERN)) {
                index1--;
            }
            index2 = index0 + 2;
            while(index2 < expression.length() && expression.substring(index0+1, index2+1).matches(NUMBER_PATTERN)) {
                index2++;
            }
            a = Double.parseDouble(expression.substring(index1, index0));
            b = Double.parseDouble(expression.substring(index0+1, index2));
            if(expression.charAt(index0) == '/') {
                if(b == 0) {
                    throw new CalculatorException(2, "");
                }
                a = a/b;
            } else {
                a = a*b;
            }
            expression = expression.substring(0, index1) + a + expression.substring(index2);
            index0 = indexMin(expression.indexOf("/"), expression.indexOf("*"));
        }
        if(index0 == expression.length() - 1) {
            expression = expression.substring(0, index0);
        }
        //addition subtraction
        index0 = indexMin(expression.indexOf("+"), expression.indexOf("-"));
        while(index0 > 0 && index0 < expression.length() - 1) {
            index1 = index0-1;
            while(index1 > 0 && expression.substring(index1-1, index0).matches(NUMBER_PATTERN)) {
                index1--;
            }
            index2 = index0 + 2;
            while(index2 < expression.length() && expression.substring(index0+1, index2+1).matches(NUMBER_PATTERN)) {
                index2++;
            }
            try {
                a = Double.parseDouble(expression.substring(index1, index0));
                b = Double.parseDouble(expression.substring(index0+1, index2));
            }catch(NumberFormatException eRef) {
                System.out.println(expression+": " +index1 +" " +index0 +" " +index2);
                System.out.println("a = " +expression.substring(index1, index0));
                System.out.println("b = " +expression.substring(index0+1, index2));
                throw eRef;
            }
            if(expression.charAt(index0) == '+') {
                a = a+b;
            } else {
                a = a-b;
            }
            expression = expression.substring(0, index1) + a + expression.substring(index2);
            index0 = indexMin(expression.indexOf("+"), expression.indexOf("-"));
        }
        if(index0 == expression.length() - 1) {
            expression = expression.substring(0, index0);
        }
        vhasResult = true;
        return Double.parseDouble(expression);
    }

    private int indexMin(int a, int b) {
        if(a < 0) {
            return b;
        } else if(b < 0) {
            return a;
        } else {
            return Math.min(a, b);
        }
    }

}
