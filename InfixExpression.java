package edu.iastate.cs228.hw4;

/**
 *  
 * @author Patrick Westerlund
 *
 */

import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix conversion using 
 * one stack, and evaluates the converted postfix expression.    
 *
 */

public class InfixExpression extends Expression 
{
	private String infixExpression;   	// the infix expression to convert		
	private boolean postfixReady = false;   // postfix already generated if true
	private int rankTotal = 0;		// Keeps track of the cumulative rank of the infix expression.
	private PureStack<Operator> operatorStack; 	  // stack of operators 
	
	
	/**
	 * Constructor stores the input infix string, and initializes the operand stack and 
	 * the hash map.
	 * 
	 * @param st  input infix string. 
	 * @param varTbl  hash map storing all variables in the infix expression and their values. 
	 */
	public InfixExpression (String st, HashMap<Character, Integer> varTbl)
	{
		infixExpression = Expression.removeExtraSpaces(st);
		varTable = varTbl;
		operatorStack = new ArrayBasedStack<Operator>();
		super.postfixExpression="";
	}
	

	/**
	 * Constructor supplies a default hash map. 
	 * 
	 * @param s
	 */
	public InfixExpression (String s)
	{
		infixExpression = Expression.removeExtraSpaces(s);
		operatorStack = new ArrayBasedStack<Operator>();
		varTable = new HashMap<Character, Integer>();
		super.postfixExpression="";
	}
	

	/**
	 * Outputs the infix expression according to the format in the project description.
	 */
	@Override
	public String toString()
	{
		String s = infixExpression;
		s=Expression.removeExtraSpaces(s);
		s=s.replace("( ", "(");
	    s=s.replace(" )", ")");
		return s;
	}
	
	
	/** 
	 * @return equivalent postfix expression, or  
	 * 
	 *         a null string if a call to postfix() inside the body (when postfixReady 
	 * 		   == false) throws an exception.
	 */
	public String postfixString() 
	{
		if(postfixReady==false)
		{
			return null;
		}
		else
		{
			return postfixExpression;
		}
	}


	/**
	 * Resets the infix expression. 
	 * 
	 * @param st
	 */
	public void resetInfix (String st)
	{
		st = Expression.removeExtraSpaces(st);
		infixExpression = st; 
	}


	/**
	 * Converts infix expression to an equivalent postfix string stored at postfixExpression.
	 * If postfixReady == false, the method scans the infixExpression, and does the following
	 * (for algorithm details refer to the relevant PowerPoint slides): 
	 * 
	 *     1. Skips a whitespace character.
	 *     2. Writes a scanned operand to postfixExpression. 
	 *     3. When an operator is scanned, generates an operator object.  In case the operator is 
	 *        determined to be a unary minus, store the char '~' in the generated operator object.
	 *     4. If the scanned operator has a higher input precedence than the stack precedence of 
	 *        the top operator on the operatorStack, push it onto the stack.   
	 *     5. Otherwise, first calls outputHigherOrEqual() before pushing the scanned operator 
	 *        onto the stack. No push if the scanned operator is ). 
     *     6. Keeps track of the cumulative rank of the infix expression. 
     *     
     *  During the conversion, catches errors in the infixExpression by throwing 
     *  ExpressionFormatException with one of the following messages:
     *  
     *      -- "Operator expected" if the cumulative rank goes above 1;
     *      -- "Operand expected" if the rank goes below 0; 
     *      -- "Missing '('" if scanning a ‘)’ results in popping the stack empty with no '(';
     *      -- "Missing ')'" if a '(' is left unmatched on the stack at the end of the scan; 
     *      -- "Invalid character" if a scanned char is neither a digit nor an operator; 
     *   
     *  If an error is not one of the above types, throw the exception with a message you define.
     *      
     *  Sets postfixReady to true.  
	 */
	public void postfix() throws ExpressionFormatException
	{
		 infixExpression = Expression.removeExtraSpaces(infixExpression);
		 Scanner s = new Scanner(infixExpression);
		 String previous = null;
		 String p = null;
		 if(postfixReady == false)
		 {
			 while(s.hasNext())
			 {
				 previous = p;
				 p = s.next();
				 char c =  p.charAt(0);
				 if(Expression.isInt(p))
				 {
					 if(previous!=null)
					 {
						 super.postfixExpression+=" "+p;
						 rankTotal++;
					 }
					 else
					 {
						 super.postfixExpression+=p;
						 rankTotal++;
					 }
				 }
				 else if(Expression.isVariable(c)&&p.length()==1)
				 {
					 if(previous!=null)
					 {
						 super.postfixExpression+=" "+c;
						 rankTotal++;
					 }
					 else
					 {
						 super.postfixExpression+=c;
						 rankTotal++;
					 } 
				 }
				 else if(Expression.isOperator(c)&&p.length()==1)
				 {
					 Operator op;
					 if(c=='-')
					 {
						 if(previous==null||Expression.isOperatorNotRPara(previous.charAt(0)))
						 {
							 op = new Operator('~');
						 }
						 else
						 {
							 op = new Operator('-');
							 rankTotal--;
						 }
					 }
					 else
					 {
						op = new Operator(c);
						if(op.getOp()!='('&&op.getOp()!=')')
						{
							rankTotal--;
						}
					 }
					 if(operatorStack.isEmpty())
					 {
						 operatorStack.push(op);
					 }
					 else if(operatorStack.peek().getOp()!='('&&operatorStack.size()==1&&op.getOp()==')')
					 {
						 s.close();
						 throw new ExpressionFormatException("Missing '(");
					 }
					 else if(operatorStack.peek().compareTo(op)==-1)
					 {
						 operatorStack.push(op);
					 }
					 else
					 {
						this.outputHigherOrEqual(op);
						if(op.getOp()!=')')
						{
							operatorStack.push(op);
						}
					 }
				 }
	
				 else
				 {
					 s.close();
					 throw new ExpressionFormatException("Invalid character");
				 }
				 if(rankTotal>1)
				 {
					 s.close();
					 throw new ExpressionFormatException("Operator expected"); 
				 }
				 if(rankTotal<0)
				 {
					 s.close();
					 throw new ExpressionFormatException("Operand expected");
				 }
			 }
	 }
	 super.postfixExpression+=" ";
	 while(!operatorStack.isEmpty())
	 {
		Operator op1 = operatorStack.pop();
		if(op1.getOp()!='('&&op1.getOp()!=')')
		{
			super.postfixExpression+=op1.getOp()+" ";
		}
	 }
	 if(rankTotal==0)
	 {
		 s.close();
		 throw new ExpressionFormatException("Operand expected");
	 }
	 s.close();
	 super.postfixExpression = Expression.removeExtraSpaces(super.postfixExpression);
	 postfixReady=true;
}
	
	
	
	/**
	 * This function first calls postfix() to convert infixExpression into postfixExpression. Then 
	 * it creates a PostfixExpression object and calls its evaluate() method (which may throw  
	 * an exception).  It also passes any exception thrown by the evaluate() method of the 
	 * PostfixExpression object upward the chain. 
	 * 
	 * @return value of the infix expression 
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException
    {
    	this.postfix();
    	PostfixExpression s = new PostfixExpression(postfixExpression,varTable);
    	return s.evaluate();
    }


	/**
	 * Pops the operator stack and output as long as the operator on the top of the stack has a 
	 * stack precedence greater than or equal to the input precedence of the current operator op.  
	 * Writes the popped operators to the string postfixExpression.  
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the stack but does 
	 * not write it to postfixExpression. 
	 * 
	 * @param op  current operator
	 * @throws ExpressionFormatException
	 *    with the following message
	 *    -- "Missing '('" if op is a ')' and matching '(' is not found on stack. 
	 */
	private void outputHigherOrEqual(Operator op) throws ExpressionFormatException
	{
		while(!operatorStack.isEmpty()&&operatorStack.peek().compareTo(op)>=0)
		{
			Operator op1 = operatorStack.pop();
			super.postfixExpression+=" "+op1.getOp();
		}
		if(!operatorStack.isEmpty()&&op.getOp()==')'&&operatorStack.peek().getOp()!='(')
		{
			throw new ExpressionFormatException("Missing '('");
		}
		if(!operatorStack.isEmpty()&&op.getOp()==')'&&operatorStack.peek().getOp()=='(')
		{
			operatorStack.pop();
		}
	}
	
	/**
	 * Returns true if infixExpression has a variable, return false if not
	 * @return Returns true if infixExpression has a variable, return false if not
	 */
	public boolean hasVariable()
	{
		Scanner s = new Scanner(infixExpression);
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
		Scanner s = new Scanner(infixExpression);
		Scanner s1 = new Scanner(infixExpression);
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
	  * @return - a set of chars array with all the variables in the expression
	  */
	public Set<Character> variablesSet()
	{
		Scanner s1 = new Scanner(infixExpression);
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
