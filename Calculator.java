import java.util.Scanner;
import java.util.ArrayList;
/**
 * Half-precision binary floating point to decimal double converter. 
 * 
 * @author Isaac Allen 
 * @version 1.0.2
 */
public class Calculator
{
    private Scanner keyboard = new Scanner(System.in);
    private int[] numArr = new int[16];
    private boolean exit = false;
    private boolean valid = true;
    private int nValidRuns = 0;
    private String stringNumber;
    private double length;
    private boolean parsed = false;
    private int tasks = 0;
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
            double exponent = 0;
            int sign = 1;
            double wholeValue = 0;
            double numerator = 0;
            double denominator = 0;
            double fraction = 0;
            double baseTenValue;

            if (numArr[0] == 0)
            {
                sign = 1;
            } else
            {
                sign = -1;
            }

            int j = 5;

            for (int i = 1; i < 6 ; i++ )
            {
                exponent += (numArr[i] * Math.pow(2, j)) / 2;
                j--;
            }
            exponent -= 15;
            ArrayList<String> out = new ArrayList<>();

            out.add(0, "1");
            out.add(1,".");
            int l = 2;

            for (int i = 6; i < 16; i++)
            {
                out.add(l, String.valueOf(numArr[i]));
                l++;
            }
            if ((exponent + 1) < 0)
            {
                out.remove(1);
                for(int i = 1; i <= ((exponent + 1) * -1); i++)
                {
                    out.add(0, "0");
                }
                out.add(0, ".");
            } else if (exponent > out.size())
                {
                    for (int i = 12; i < exponent + 1; i++)
                    {
                        out.add(i, "0");
                    }        
                    out.remove(1);
                    out.add((int) exponent, ".");           
                }
            int decimalPosition = out.indexOf(".");
            if (exponent != decimalPosition && exponent > 0)
            {
                out.remove(decimalPosition);
                out.add((int) exponent + 1, ".");
            }
            decimalPosition = out.indexOf(".");
            int backwards = decimalPosition - 1;
            int backwards2 = out.size() - 1;
            double nBeforeDecimal = 0;
            int p = out.lastIndexOf("1");
            int q = 0;

            for (int i = 0; i < out.size(); i++)
            {               
                if (i < decimalPosition)
                {
                    wholeValue += (Double.valueOf(out.get(backwards)) * Math.pow(2, i));
                    nBeforeDecimal++;  
                    backwards--;
                }
                if (p >= 0 && p > decimalPosition)
                {
                    if (backwards2 > decimalPosition && !out.get(p).equals("."))
                    {
                        numerator += (Double.valueOf(out.get(p)) * Math.pow(2, q));
                        q++;
                        p--;
                    } 
                    backwards2--;
                }
            }
            if (decimalPosition != out.size() - 1 && numerator != 0)
            {            
                denominator = Math.pow(2, (out.lastIndexOf("1") - nBeforeDecimal));
            }

            wholeValue *= sign;
            if (denominator != 0)
            {
                fraction = numerator / denominator;
                fraction *= sign;
            }
            baseTenValue = wholeValue + fraction;

            StringBuilder sb = new StringBuilder();
            if (numArr[0] == 1)
            {
                sb.append("-");
            }
            for (String s : out)
            {
                sb.append(s);
            }
            if (out.get(out.size() - 1).equals("."))
            {
                sb.append("0");
            }
            String binVal = sb.toString();
            System.out.println("Binary value = " + binVal);
            System.out.println("Decimal value = " + baseTenValue);
            valid = true;
        }
    }

    public static void main(String[] args)
    {
        new Calculator();
    }
}
