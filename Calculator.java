import java.util.Scanner;
import java.util.ArrayList;
/**
 * Half-precision binary floating point to decimal double converter. 
 * 
 * @author Isaac Allen 
 * @version 1.0
 */
public class Calculator
{
    private String stringNumber;
    private int sign;
    private Scanner keyboard = new Scanner(System.in);
    private int i = 0;
    private double length = 0;
    private int[] numArr = new int[16];
    private boolean exit = false;
    private boolean valid = true;
    private int nRuns = 0;
    public Calculator()
    {      
        taskHandler();
    }

    public void taskHandler()
    {
        while(!exit)
        {
            promptUser();
            if (exit)
            {
                return;
            }
            filterInput();
            if (exit)
            {
                return;
            }
            valueAssigner();
        }
    }

    public void promptUser()
    {
        if (valid)
        {
            i++;
            if (valid && nRuns == 0)
            {
                System.out.println("Enter a 16 bit floating point value or type exit.");
                nRuns++;
            } else
            {
                System.out.println("Enter another 16 bit floating point value or type exit.");
            }
        } else 
        {
            System.out.println("Invalid number, please re-enter or type exit.");
            valid = true;
        }
        stringNumber = keyboard.nextLine();
        length = stringNumber.length();
        if (stringNumber.toLowerCase().equals("exit"))
        {
            exit = true;
        }
        if (exit)
        {
            return;
        }
        if (length != 16)
        {
            valid = false;
            promptUser();
        } else
        {
            try{
                for(int i = 0; i < 16; i++)
                {
                    numArr[i] = Integer.parseInt(String.valueOf(stringNumber.charAt(i)));
                }
            } catch(NumberFormatException e)
            {
                valid = false;
                promptUser();   
            }                 
        }
    }

    public void filterInput()
    {

        for(int i = 0; i < 16; i++)
        {
            if (exit)
            {
                break;
            }
            if (numArr[i] != 0 && numArr[i] != 1)
            {
                valid = false;
                promptUser(); 
            }
        }
    }

    public void valueAssigner()
    {
        double exponent = 0;
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
        double expHandler = 0;
        for (int i = 1; i < 6 ; i++ )
        {
            exponent += (numArr[i] * Math.pow(2, j)) / 2;
            j--;
        }
        exponent -= 15;
        ArrayList<String> out = new ArrayList<String>();
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
        } else
        {
            if (exponent > out.size())
            {
                for (int i = 12; i < exponent + 1; i++)
                {
                    out.add(i, "0");
                } 
            }
            out.remove(1);
            out.add((int) exponent + 1, ".");           
        }
        int decimalPosition = out.indexOf(".");
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
            if (p >= 0)
            {
                if (p > decimalPosition)
                {
                    if (backwards2 > decimalPosition && out.get(p) != ".")
                    {
                        numerator += (Double.valueOf(out.get(p)) * Math.pow(2, q));
                        q++;
                        p--;
                    } 
                    backwards2--;
                }
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
        String binVal = "";
        if (sign == -1)
        {
            binVal += "-";
        }
        boolean zeroed = false;
        if (out.get(0) == ".")
        {
            binVal += "0.";
            zeroed = true;
        }
        boolean stop = false;
        int lastOne = out.lastIndexOf("1");
        for (int i = 0; i < out.size(); i++)
        {
            if (!zeroed && !stop)
            {
                binVal += out.get(i);
                if (i == lastOne)
                {
                    stop = true;
                }
                if (lastOne < decimalPosition && stop)
                {
                    binVal += ".0";
                }
            } else if (zeroed && !stop && i + 1 < out.size())
            {
                binVal += out.get(i + 1);
                if (i + 1 == lastOne && lastOne > decimalPosition)
                {
                    stop = true;
                }
            }
        }
        System.out.println("Binary value = " + binVal);

        System.out.println("Decimal value = " + baseTenValue);
    }

    public static void main(String[] args)
    {
        Calculator cal1 = new Calculator();
    }
}
