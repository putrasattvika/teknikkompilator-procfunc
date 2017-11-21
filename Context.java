/** 
 * @class: Context
 * This class constructs Context object that has attributes : 
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */

package TeknikKompilator;

import java.util.Stack;

class Context
{
    public Context()
    {
        lexicalLevel = -1;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        symbolStack = new Stack();
        typeStack = new Stack();
        printSymbols = false;
        errorCount = 0;
    }

    /**
     * This method chooses which action to be taken
     * @input : ruleNo(type:int)
     * @output: -(type:void)
     */
    public void C(int ruleNo)
    {
        boolean error = false;

        //System.out.println("C" + ruleNo);
        switch(ruleNo)
        {
            case 0:
                lexicalLevel++;
                orderNumber = 0;
                break;

            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;

            case 2:
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                break;

            case 3:
                if (symbolHash.isExist(currentStr, lexicalLevel))
                {
                    System.out.println("Variable declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolHash.insert(new Bucket(currentStr));
                }
                symbolStack.push(currentStr);
                break;

            case 4:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;

            case 5:
                symbolHash.find(currentStr).setIdType(((Integer)typeStack.peek()).intValue());
                break;

            case 6:
                if (!symbolHash.isExist(currentStr))
                {
                    System.out.println("Variable undeclared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolStack.push(currentStr);
                }
                break;

            case 7:
                symbolStack.pop();
                break;

            case 8:
                typeStack.push(new Integer(symbolHash.find(currentStr).getIdType()));
                break;

            case 9:
                typeStack.push(new Integer(Bucket.INTEGER));
                break;

            case 10:
                typeStack.push(new Integer(Bucket.BOOLEAN));
                break;

            case 11:
                typeStack.pop();
                break;

            case 12:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.BOOLEAN:
                        System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;

            case 13:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.INTEGER:
                        System.out.println("Type of boolean expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;

            case 14:
                int temp = ((Integer)typeStack.pop()).intValue();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;

            case 15:
                temp = ((Integer)typeStack.pop()).intValue();
                if ((temp != Bucket.INTEGER) && ((Integer)typeStack.peek()).intValue() != Bucket.INTEGER)
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;

            case 16:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;

            case 17:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != Bucket.INTEGER)
                {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;

            case 18:
                symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
                orderNumber++;
                break;

            case 19:
                symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
                orderNumber += 3;
                break;

            case 20:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.ARRAY:
                        System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;

            case 21:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.SCALAR:
                        System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;

            case 22:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                orderNumber++;

                break;
            
            case 23:
                // implemented in C5
                break;
            
            case 24:
                // insert procedure to symbol table, set type and kind
                if (symbolHash.isExist(currentStr, lexicalLevel)) {
                    System.out.println("Procedure declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }

                // symbolHash.insert(new Bucket(currentStr));
                symbolHash.find(currentStr).setIdType(Bucket.UNDEFINED);
                symbolHash.find(currentStr).setIdKind(Bucket.PROCEDURE);
                symbolHash.find(currentStr).setArgc(0);
                
                symbolStack.push(currentStr);
                break;
            
            case 26:
                // insert function to symbol table
                if (symbolHash.isExist(currentStr, lexicalLevel)) {
                    System.out.println("Function declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }

                symbolHash.find(currentStr).setIdType(Bucket.UNDEFINED);
                symbolHash.find(currentStr).setIdKind(Bucket.FUNCTION);
                symbolHash.find(currentStr).setArgc(0);

                symbolStack.push(currentStr);
                break;

            case 28:
                // check whether identifier is a procedure
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.PROCEDURE) {
                    System.out.println("Procedure expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;

            case 29:
                // check whether function or procedure doesn't have any args
                // context: function or procedure w/o args
                temp = symbolHash.find((String)symbolStack.peek()).getIdKind();
                if (temp == Bucket.PROCEDURE || temp == Bucket.FUNCTION) {
                    if (symbolHash.find((String)symbolStack.peek()).getArgc() != 0) {
                        System.out.println("Procedure without arguments " + currentLine + ": " + currentStr);
                        errorCount++;
                    }
                }
                break;

            case 33:
                // checks whether entry in symbol table is a function or not
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.FUNCTION) {
                    System.out.println("Function expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }   
                break;

            case 36:
                // checks whether return type matches expression in the function
                if (symbolHash.find((String)symbolStack.peek()).getIdType() != ((Integer)typeStack.peek()).intValue()) {
                    System.out.println("Return type does not match at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(((Integer)typeStack.peek()).intValue())); // adds to type stack
                break;
            
            case 37:
                // checks whether identifier is a function or not
                // if so, go to C33
                // otherwise, go to C20, treat it as a scalar
                if(symbolHash.find((String)symbolStack.peek()).getIdKind()==Bucket.FUNCTION) C(33);
                else C(20);
                break;
            
            case 40:
                // already handled in C9 (int), C10 (bool)
                break;

            case 100:
                // additional rule to set function address
                symbolHash.find(currentStr).setFuncAddr(Generate.cell);
                break;

        }
    }

    /**
     * This method sets the current token and line
     * @input : str(type:int), line(type:int)
     * @output: -(type:void)
     */
    public void setCurrent(String str, int line)
    {
        currentStr = str;
        currentLine = line;
    }

    /**
     * This method sets symbol printing option
     * @input : bool(type:boolean)
     * @output: -(type:void)
     */
    public void setPrint(boolean bool)
    {
        printSymbols = bool;
    }

    private final int HASH_SIZE = 211;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    public static Stack symbolStack;
    public static Stack typeStack;
    public static String currentStr;
    public static int currentLine;
    private boolean printSymbols;
    public int errorCount;
}