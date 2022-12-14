package edu.iastate.cs228.hw4;

import java.util.HashMap;

/**
 * 
 * @author Patrick Westerlund
 *
 */
public abstract class Expression 
{
	protected String postfixExpression; 		
	protected HashMap<Character, Integer> varTable; // hash map to store variables in the 

	
	protected Expression()
	{
		// no implementation needed 
		// removable when you are done
	}
	
	
	/**
	 * Initialization with a provided hash map. 
	 * 
	 * @param varTbl
	 */
	protected Expression(String st, HashMap<Character, Integer> varTbl)
	{
		postfixExpression = st;
		varTable = varTbl;
	}
	
	
	/**
	 * Initialization with a default hash map.
	 * 
	 * @param st
	 */
	protected Expression(String st) 
	{
		postfixExpression = st;
		varTable = new HashMap<Character, Integer>();
	}

	
	/**
	 * Setter for instance variable varTable.
	 * @param varTbl
	 */
	protected void setVarTable(HashMap<Character, Integer> varTbl) 
	{
		varTable = varTbl;
	}
	
	
	/**
	 * Evaluates the infix or postfix expression. 
	 * 
	 * @return value of the expression 
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	protected abstract int evaluate() throws ExpressionFormatException, UnassignedVariableException;  

	
	
	// --------------------------------------------------------
	// Helper methods for InfixExpression and PostfixExpression 
	// --------------------------------------------------------

	/** 
	 * Checks if a string represents an integer.  You may call the static method 
	 * Integer.parseInt(). 
	 * 
	 * @param s
	 * @return
	 */
	protected static boolean isInt(String s) 
	{
		try
		{
			Integer.parseInt(s);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	
	/**
	 * Checks if a char represents an operator, i.e., one of '~', '+', '-', '*', '/', '%', '^', '(', ')'. 
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isOperator(char c) 
	{
		if(c=='~'||c=='+'||c=='-'||c=='*'||c=='/'||c=='%'||c=='^'||c=='('||c==')')
		{
			return true;
		}
		return false;
	}

	protected static boolean isOperatorNotRPara(char c) 
	{
		if(c=='~'||c=='+'||c=='-'||c=='*'||c=='/'||c=='%'||c=='^'||c=='(')
		{
			return true;
		}
		return false;
	}
	
	/** 
	 * Checks if a char is a variable, i.e., a lower case English letter. 
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isVariable(char c) 
	{
		return Character.isLowerCase(c);
	}
	
	
	/**
	 * Removes extra blank spaces in a string. 
	 * @param s
	 * @return
	 */
	protected static String removeExtraSpaces(String s) 
	{
		String s2 = s.trim().replaceAll(" +", " ");
		s = s2;
		return s;
	}
	
	

}
