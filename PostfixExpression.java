package edu.iastate.cs228.hw4;

/**
 *  
 * @author Patrick Westerlund
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set; 

public class PostfixExpression extends Expression 
{
	private int leftOperand;            // left operand for the current evaluation step             
	private int rightOperand;           // right operand (or the only operand in the case of 
	                                    // a unary minus) for the current evaluation step	

	private PureStack<Integer> operandStack;  // stack of operands
	

	/**
	 * Constructor stores the input postfix string and initializes the operand stack.
	 * 
	 * @param st      input postfix string. 
	 * @param varTbl  hash map that stores variables from the postfix string and their values.
	 */
	public PostfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		super(st,varTbl);
		postfixExpression = Expression.removeExtraSpaces(postfixExpression);
		operandStack = new ArrayBasedStack<Integer>();
	}
	
	
	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public PostfixExpression (String s)
	{
		super(s);
		postfixExpression = Expression.removeExtraSpaces(postfixExpression);
		operandStack = new ArrayBasedStack<Integer>();
	}

	
	/**
	 * Outputs the postfix expression according to the format in the project description.
	 */
	@Override 
	public String toString()
	{
		return postfixExpression;
	}
	

	/**
	 * Resets the postfix expression. 
	 * @param st
	 */
	public void resetPostfix (String st)
	{
		st = Expression.removeExtraSpaces(st);
		postfixExpression = st; 
	}


	/**
     * Scan the postfixExpression and carry out the following:  
     * 
     *    1. Whenever an integer is encountered, push it onto operandStack.
     *    2. Whenever a binary (unary) operator is encountered, invoke it on the two (one) elements popped from  
     *       operandStack,  and push the result back onto the stack.  
     *    3. On encountering a character that is not a digit, an operator, or a blank space, stop 
     *       the evaluation. 
     *       
     * @return value of the postfix expression 
     * @throws ExpressionFormatException with one of the messages below: 
     *  
     *           -- "Invalid character" if encountering a character that is not a digit, an operator
     *              or a whitespace (blank, tab); 
     *           --	"Too many operands" if operandStack is non-empty at the end of evaluation; 
     *           -- "Too many operators" if getOperands() throws NoSuchElementException; 
     *           -- "Divide by zero" if division or modulo is the current operation and rightOperand == 0;
     *           -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0;
     *           -- self-defined message if the error is not one of the above.
     *           
     *         UnassignedVariableException if the operand as a variable does not have a value stored
     *            in the hash map.  In this case, the exception is thrown with the message
     *            
     *           -- "Variable <name> was not assigned a value", where <name> is the name of the variable.  
     *           
     */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException
    {
    	Scanner s = new Scanner(super.postfixExpression);
    	while(s.hasNext())
    	{
    		String s1 = s.next();
    		char c = s1.charAt(0);
    		if(Expression.isVariable(c)&&s1.length()==1)
    		{
    			if(!varTable.containsKey(c))
    			{
    				s.close();
    				throw new UnassignedVariableException("Variable " + c + " was not assigned a value");
    			}
    			else if(s1.length()==1)
    			{
    				int x = (int) varTable.get(c);
    				operandStack.push(x);
    			}
    		}
    		else if(Expression.isInt(s1))
    		{
    			operandStack.push(Integer.parseInt(s1));
    		}
    		else if(Expression.isOperator(c)&&s1.length()==1)
    		{
    			getOperands(c);
    			int com = compute(c);
    			operandStack.push(com);
    			rightOperand=0;
    			leftOperand=0;
    		}
    		else
    		{
    			s.close();
    			throw new ExpressionFormatException("Invalid character");
    		}
    	}
    	if(operandStack.size()>1)
    	{
    		s.close();
    		throw new ExpressionFormatException("Too many operands");
    	}
    	s.close();
    	return operandStack.pop();
    }
	

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it to rightOperand. The stack must have at least
	 * one entry. Otherwise, throws NoSuchElementException.
	 * For binary operator, pops the right and left operands from operandStack, and assign them to rightOperand and leftOperand, respectively. The stack must have at least
	 * two entries. Otherwise, throws NoSuchElementException.
	 * @param op
	 * 			char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException 
	{
		Operator op1 = new Operator(op); 
		if(operandStack.isEmpty())
		{
			throw new NoSuchElementException("No operands in stack");
		}
		if(op1.getOp()=='~')
		{
			rightOperand=operandStack.pop();
		}
		else
		{
			if(operandStack.size()<2)
			{
				throw new NoSuchElementException("Too many operators");
			}
			rightOperand=operandStack.pop();
			leftOperand=operandStack.pop();
		}
	}


	/**
	 * Computes "leftOperand op rightOperand" or "op rightOperand" if a unary operator. 
	 * 
	 * @param op operator that acts on leftOperand and rightOperand. 
	 * @return
	 *     returns the value obtained by computation.
	 * @throws ExpressionFormatException
	 *             with one of the messages below: <br>
	 *             -- "Divide by zero" if division is the current operation and rightOperand == 0; <br>
	 *             -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0.
	 */
	private int compute(char op) throws ExpressionFormatException
	{
		Operator op1 = new Operator(op);
		if(op1.getOp()=='/'&&rightOperand==0)
		{
			throw new ExpressionFormatException("Divide by zero");	
		}
		if(op1.getOp()=='^'&&leftOperand == 0 && rightOperand == 0)
		{
			throw new ExpressionFormatException("0^0");
		}
		if(op1.getOp()=='~')
		{
			return -rightOperand;
		}
		else if(op1.getOp()=='+')
		{
			return leftOperand + rightOperand;
		}
		else if(op1.getOp()=='-')
		{
			return leftOperand - rightOperand;
		}
		else if(op1.getOp()=='*')
		{
			return leftOperand * rightOperand;
		}
		else if(op1.getOp()=='/')
		{
			return leftOperand / rightOperand;
		}
		else if(op1.getOp()=='^')
		{
			return (int) Math.pow(leftOperand, rightOperand);
		}
		else
		{
			return leftOperand % rightOperand;
		}
	}
	/**
	 * Returns true if infixExpression has a variable, return false if not
	 * @return Returns true if infixExpression has a variable, return false if not
	 */
	public boolean hasVariable()
	{
		Scanner s = new Scanner(postfixExpression);
		while(s.hasNext())
		{
			String s1 = s.next();
			char c = s1.charAt(0);
			if(Expression.isVariable(c))
			{
				s.close();
				return true;
			}
		}
		s.close();
		return false;
	}
	
	/**
	  * Returns a char array with all the variables in the expression
	  * @return - a char array with all the variables in the expression
	  */
	public char[] variables()
	{
		Scanner s = new Scanner(postfixExpression);
		Scanner s1 = new Scanner(postfixExpression);
		int count = 0;
		while(s.hasNext())
		{
			String stri = s.next();
			char c = stri.charAt(0);
			if(Expression.isVariable(c))
			{
				count++;
			}
		}
		s.close();
		char[] ch = new char[count];
		for(int i = 0;i<ch.length;i++)
		{
			while(s1.hasNext())
			{
				String strig = s1.next();
				char cha = strig.charAt(0);
				if(Expression.isVariable(cha))
				{
					ch[i]=cha;
					i++;
				}	
			}
		}
		s1.close();
		return ch;
	}
	/**
	  * Returns a set of chars with all the variables in the expression
	 * @return 
	  * @return - a set of chars array with all the variables in the expression
	  */
	public Set<Character> variablesSet()
	{
		Scanner s1 = new Scanner(postfixExpression);
		Set<Character> var = new HashSet<Character>();
		while(s1.hasNext())
		{
			String s2 = s1.next();
			char c = s2.charAt(0);
			if(Expression.isVariable(c))
			{
				var.add(c);
			}
		}
		s1.close();
		return var;
	}
}
