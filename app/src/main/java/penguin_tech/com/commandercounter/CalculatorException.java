package penguin_tech.com.commandercounter;

public class CalculatorException extends Exception {

    private int code;
    /*
    0 - illegal character
    1 - mismatched bracket
    2 - division by zero
    */


    public CalculatorException(int code, String message) {
        super(message);
        this.code = code;
    }

}
