import java.util.Scanner;
import java.util.ArrayList;
/**
 * Half-precision binary floating point to base-10 double converter. 
 * 
 * @author Isaac Allen 
 * @version 1.0.2
 */
public class Calculator
{
    private boolean exit = false;
    private boolean parsed = false;
    private boolean valid = true;
    private double length;
    private int nValidRuns = 0;
    private int[] numArr = new int[16];
    private Scanner keyboard = new Scanner(System.in);
    private String stringNumber;
   
    public Calculator()
    {
        taskHandler();
    }

    public void taskHandler()
    {
        do
        {
            if (!exit)
            {
                promptUser();
                filterInput();
            }    
            if (parsed)
            {
                valueAssigner();
            }
        } while (!exit);
             
    }

    public void promptUser()
    {
        if (valid)
        {
            if (nValidRuns == 0)
            {
                System.out.println("Enter a 16 bit floating point value or type exit.");
                nValidRuns++;
            } else
            {
                System.out.println("Enter another 16 bit floating point value or type exit.");
            }
        } else 
        {
            System.out.println("Invalid number, please re-enter or type exit.");
        }
        stringNumber = keyboard.nextLine();
        length = stringNumber.length();      
    }

    public void filterInput()
    {
        if (stringNumber.equalsIgnoreCase("exit"))
        {
            exit = true;
            taskHandler();
        } else if (length != 16)
        {
            valid = false;
            taskHandler();
        } else
        {
            do 
            {
                try{
                    for(int i = 0; i < 16; i++)
                    {
                        numArr[i] = Integer.parseInt(String.valueOf(stringNumber.charAt(i)));
                        parsed = true;
                    }
                } catch(NumberFormatException e)
                {
                    valid = false;
                    taskHandler();
                    break;
                }
            } while (!parsed);                           
        }
        for(int i = 0; i < 16; i++)
        {
            if (numArr[i] != 0 && numArr[i] != 1)
            {
                valid = false;
                taskHandler(); 
            }
        }
    }

    public void valueAssigner()
    {
        if (!exit)
        {
            ArrayList<String> binValList = new ArrayList<>();
            double baseTenValue;
            double denominator = 0;  
            double exponent = 0;
            double fraction = 0;
            double nBeforeDecimal = 0;
            double numerator = 0;            
            double wholeValue = 0; 
            int decimalPosition = 0;
            int fractionalValueIndexDecrementer = 0;
            int indexCoef = 0;
            int indexCoefDecrementer = 5;
            int indexDecrementer = 0;
            int mantissaIndex = 2;
            int sign = 1; 
            int wholeValueIndexDecrementer = 0;
            String binValString = "";
            StringBuilder sb = new StringBuilder();

            if (numArr[0] == 0)
            {
                sign = 1;
            } else
            {
                sign = -1;
            }
            for (int i = 1; i < 6 ; i++ )
            {
                exponent += (numArr[i] * Math.pow(2, indexCoefDecrementer)) / 2;
                indexCoefDecrementer--;
            }

            exponent -= 15;
            binValList.add(0, "1");
            binValList.add(1,".");
            
            for (int i = 6; i < 16; i++)
            {
                binValList.add(mantissaIndex, String.valueOf(numArr[i]));
                mantissaIndex++;
            }
            if ((exponent + 1) < 0)
            {
                binValList.remove(1);
                for(int i = 1; i <= ((exponent + 1) * -1); i++)
                {
                    binValList.add(0, "0");
                }
                binValList.add(0, ".");
            } else if (exponent > binValList.size())
                {
                    for (int i = 12; i < exponent + 1; i++)
                    {
                        binValList.add(i, "0");
                    }        
                    binValList.remove(1);
                    binValList.add((int) exponent, ".");           
                }

            decimalPosition = binValList.indexOf(".");

            if (exponent != decimalPosition && exponent > 0)
            {
                binValList.remove(decimalPosition);
                binValList.add((int) exponent + 1, ".");
            }

            decimalPosition = binValList.indexOf(".");
            wholeValueIndexDecrementer = decimalPosition - 1;
            fractionalValueIndexDecrementer = binValList.size() - 1;           
            indexDecrementer = binValList.lastIndexOf("1"); 

            for (int i = 0; i < binValList.size(); i++)
            {               
                if (i < decimalPosition)
                {
                    wholeValue += (Double.valueOf(binValList.get(wholeValueIndexDecrementer)) * Math.pow(2, i));
                    nBeforeDecimal++;  
                    wholeValueIndexDecrementer--;
                }
                if (indexDecrementer >= 0 && indexDecrementer > decimalPosition)
                {
                    if (fractionalValueIndexDecrementer > decimalPosition && !binValList.get(indexDecrementer).equals("."))
                    {
                        numerator += (Double.valueOf(binValList.get(indexDecrementer)) * Math.pow(2, indexCoef));
                        indexCoef++;
                        indexDecrementer--;
                    } 
                    fractionalValueIndexDecrementer--;
                }
            }
            if (decimalPosition != binValList.size() - 1 && numerator != 0)
            {            
                denominator = Math.pow(2, (binValList.lastIndexOf("1") - nBeforeDecimal));
            }

            wholeValue *= sign;

            if (denominator != 0)
            {
                fraction = numerator / denominator;
                fraction *= sign;
            }

            baseTenValue = wholeValue + fraction;

            if (numArr[0] == 1)
            {
                sb.append("-");
            }
            for (String s : binValList)
            {
                sb.append(s);
            }
            if (binValList.get(binValList.size() - 1).equals("."))
            {
                sb.append("0");
            }

            binValString = sb.toString();

            System.out.println("Binary value = " + binValString);
            System.out.println("Decimal value = " + baseTenValue);

            valid = true;

        }
    }

    public static void main(String[] args)
    {
        new Calculator();
    }
}
