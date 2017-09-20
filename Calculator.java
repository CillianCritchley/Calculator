/*
Cillian O Criothaile
C00139896
 */
import javax.swing.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


class Calculator
{


    void setStringListener(StringListener listener) {
        this.textListener = listener;
    }

    private static Map<String,Operation> symbols = new HashMap<>();

    static    {
        symbols.put("+", BigDecimal::add);
        symbols.put("-", BigDecimal::subtract);
        symbols.put("*", BigDecimal::multiply);
        symbols.put("/", (term1, term2) -> {
                    if(term2.intValue()==0)
                        throw new DivideByZeroException();
            return term1.divide(term2, 9, BigDecimal.ROUND_HALF_EVEN);
        });
        symbols.put("%", BigDecimal::remainder);
        symbols.put("√", (term1, term2) -> {
                if(term1.intValue() < 0)
                {
                    throw new NumberFormatException();
                }
                double dub = Math.sqrt(term1.doubleValue());
            return BigDecimal.valueOf(dub);
        });
        /*
        the rounding causes the answer to eventually become 0 if the value being squared is between
        (but not including) -1 and 1
         */
        symbols.put("x²", (term1, term2) -> term1.pow(2).setScale(9, BigDecimal.ROUND_HALF_EVEN));
        symbols.put("1/x", (term1, term2) -> {
            if(term1.intValue()==0) {
                throw new DivideByZeroException();
            }
            BigDecimal one = new BigDecimal("1.0");

            return one.divide(term1,9,BigDecimal.ROUND_HALF_EVEN);
        });
        symbols.put("<html> x <sup> y</sup> </html>", (term1, term2) -> {
            /*
            if the exponent is positive (or zero) just do the calculation. if it's negative multiple it by -1,
            calculate the value of term1 to the power of term2 (now positive) and then pass that value
            to the 1/x function since 3^-2 is the same as 1/3^2
             */
            if(term2.intValue() >=0) {
                return term1.pow(term2.intValue());
            }
            else{
                term2 = term2.multiply(new BigDecimal("-1"));
                return symbols.get("1/x").calculate(term1.pow(term2.intValue()),term1);
            }
        });
    }



    private boolean isNumeric(String testString)
    {
        try
        {
            Double tried =  Double.parseDouble(testString);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private String operator;
    private BigDecimal num1 = new BigDecimal("0");
    private BigDecimal num2 = new BigDecimal("0");
    private StringListener textListener;
    private boolean num1Set = false;
    private String outString ="";
    private String memString="";
    Calculator()
    {
    }

    void put(String theString)
    {
           if (isNumeric(theString))
               {
                number(theString);
               } //isNumeric
           /*
           if the = key was pressed and there is a value stored in outString, do stuff.
           if there isn't a value stored in outString then assigning a value to num2 is always going to be
           wrong.

           assigning a value to num1 is deal with near the bottom of the method
            */
             else if(theString.equals("=") && !outString.equals(""))
                    {
                        num2 = new BigDecimal(outString);
                       equals();
                    }

           else switch (theString) {
                   case "MC":
                   case "MR":
                   case "MS":
                   case "M+":
                   case "M-":
                       memFunctions(theString);
                       break;
                   default:
                       switch (theString) {
                           case "+/-":
                           case ".":
                           case "x²":
                           case "1/x":
                           case "√":
                           case "C":
                           case "AC":
                               miscFunc(theString);
                               break;
                           default:
                      /*
                      if one of the operator keys has been pressed and
                      if no value has been assigned to num1, and outString contains a numeric value assign
                      the value in outString to num1. assign the operator stored in theString to 'operator',
                      set num1Set to true (signifying that number 1 has been assigned a value) and clear outString
                      so that when output() is called below the textfield will be blank to receive the next number
                       */

                      /*
                            the next key non numeric key pressed after the operator wil be the '=' key which is
                            dealt with above near the top of put().
                            the value for num2 is assigned up there
                                                */
                               if (!num1Set && isNumeric(outString)) {
                                   num1 = new BigDecimal(outString);
                                   operator = theString;
                                   num1Set = true;
                                   outString = "";
                               }

                               break;
                       }
                       break;
               }
        output();
        }
    //functions I couldn't easily fit into the hashmap
    private void miscFunc(String funcString) {
        switch (funcString) {
            case "+/-":
             /*
               if the first number was entered but the second wasn't, the +/- toggle is meant for the first number
               otherwise it's for the second
                */
                if (!num1Set && isNumeric(outString)) {
                    if (!outString.equals("0")) {
                        num1 = new BigDecimal(outString);
                        num1 = num1.multiply(BigDecimal.valueOf(-1));
                        outString = num1.toPlainString();
                        output();
                    }
                } else if (isNumeric(outString)) {
                    num2 = new BigDecimal(outString);
                    num2 = num2.multiply(BigDecimal.valueOf(-1));
                    outString = num2.toPlainString();
                    output();
                }

                break;
                /*
                the following three are included in the map but because they only require one term, i have to
                assign the operator and call equals method separate from the rest of the functions
                 */
            case "√":
            case "1/x":
            case "x²":
                if(!outString.equals("")) {
                    num1 = new BigDecimal(outString);
                    operator = funcString;
                    equals();
                }
                break;
                /*
                clear the current value in the field
                 */
            case "C":
                outString = "";
                output();
                break;
                /*
                clear everything except memory
                 */
            case "AC":
                num1Set = false;
                outString = "";
                num1 = BigDecimal.valueOf(0);
                num2 = BigDecimal.valueOf(0);
                operator = "";
                output();
                break;
                /*
                if . is pressed when the calc is first started or after AC has been pressed (or C). the field
                will display 0.
                otherwise it will append the period to the number already displayed
                 */
            case ".":
                if (outString.equals("")) {
                    outString = "0" + funcString;
                } else if (outString.contains(".")) {
                    /*
                    prevent the user from adding a second decimal place eg, 1.2.
                     */
                    outString = outString;
                } else {
                    outString = outString + funcString;
                    output();
                }


                break;
        }
    }
    private void number(String theString)
    {
        if (!num1Set ) {
            /*
            straight after a calculation, or on opening the calculator for the first time
            or on pressing AC to clear the calculator. the next numeric input will
            overwrite the value in the field and any subsequent numeric inputs (second or higher
            digit in a number) will be appended
             */
            if (isNumeric(outString) && !outString.equals("0")
                    && !outString.equals(num1.toPlainString())) {
                outString = outString + theString;
            }
            else if(outString.equals("0."))
            {
                outString = outString + theString;
            }
            else if(!outString.equals("") && !outString.equals(num1.toPlainString())) {
                outString = outString + theString;
            }
            else{
                outString = theString;
            }
        }
        else {
            outString = outString + theString;

            }

        output();
    }
    private void memFunctions(String memfunc)
    {
        switch (memfunc) {
            /*
            memory save button.
             */
            case "MS":
                memString = outString;
                break;
                /*
                memory recall button. if num1Set is false, num1 is the number to be recalled to. otherwise it's num2
                added a check to make sure memString isn't empty so don't try to create and assign an empty BigDecimal
                 */
            case "MR":
                    if(!memString.equals("")) {
                        if (!num1Set) {
                            num1 = new BigDecimal(memString);
                        } else {
                            num2 = new BigDecimal(memString);
                        }
                    }
                outString = memString;
                output();
                break;
                /*
                adds the value stored in memory to displayed value
                 */
            case "M+":
                /*
                   dont try to assign values to either BigDecimal if the Strings are blank. causes numberformatexception
                 */
                if (!outString.equals("") && !memString.equals("")) {
                    BigDecimal bd2 = new BigDecimal(outString);
                    BigDecimal bd3 = new BigDecimal(memString);
                    bd2 = bd2.add(bd3);
                    outString = bd2.toString();
                    output();
                }
                break;
                /*
                subtracts the value stored in memory from the displayed value
                 */
            case "M-":
                if (!outString.equals("") && !memString.equals("")) {
                    BigDecimal bd2 = new BigDecimal(outString);
                    BigDecimal bd3 = new BigDecimal(memString);
                    bd2 = bd2.subtract(bd3);
                    outString = bd2.toString();
                    output();
                }
                break;
            default:
                //memString = "";
                break;
        }
    }
    private void equals()
    {

        outString = calculate();
        if(isNumeric(outString)) {
            num1 = new BigDecimal(outString);
        /*
        set num1 to the value of the most recent calculation, just in case the user wants to
        get the square root of the number displayed after hitting the equals sign
         */
            num1Set = false;
        }
        output();        // output the result of the calculation
    }



    private void output()
    {
        /*
        don't allow the string to overrun the textfield. starting character of string will always be displayed
        extra will be truncated but the stored value in the calculator will be correct for further calculations
         */
        if(outString.length() > 20) {
            textListener.textEmitted(outString.substring(0, 16) + "...");
        }
        else {
            textListener.textEmitted(outString);
        } //send the string to the stringlistener on MainFrame
    }
   private String calculate() {
        if(symbols.containsKey(operator)) {
           try {
               return symbols.get(operator).calculate(num1, num2).stripTrailingZeros().toPlainString();
           }
           catch (DivideByZeroException | NumberFormatException dbz)
           {
               JOptionPane.showMessageDialog(null,dbz, "Error Message", JOptionPane.ERROR_MESSAGE);
           }
        }
       return "";
    }


}