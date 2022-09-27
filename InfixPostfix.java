package edu.iastate.cs228.hw4;

/**
 *  
 * @author Patrick Westerlund
 *
 */

/**
 * 
 * This class evaluates input infix and postfix expressions. 
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

public class InfixPostfix 
{

	/**
	 * Repeatedly evaluates input infix and postfix expressions.  See the project description
	 * for the input description. It constructs a HashMap object for each expression and passes it 
	 * to the created InfixExpression or PostfixExpression object. 
	 *  
	 * @param args
	 * @throws ExpressionFormatException 
	 * @throws UnassignedVariableException 
	 * @throws FileNotFoundException 
	 **/
	public static void main(String[] args) throws ExpressionFormatException, UnassignedVariableException, FileNotFoundException 
	{
		System.out.println("Evaluation of Infix and Postfix Expressions");
		System.out.println("keys: 1 (standard input)  2 (file input)  3 (exit) ");
		System.out.println("(Enter “I” before an infix expression, “P” before a postfix expression”)");
		int select = 0;
		int trial = 1;
		Scanner s = new Scanner(System.in);
		while(select<3)
		{
			System.out.print("Trial " + trial +": ");
			select = s.nextInt();
			if(select<1)
			{
				s.close();
				break;
			}
			if(select>2)
			{
				s.close();
				break;
			}
			trial++;
			if(select==1)
			{
				System.out.print("Expression: ");
				s.nextLine();
				String exp = s.nextLine();
				Scanner str = new Scanner(exp);
				String inf = "";
				String postf = "";
				if(str.next().equals("I"))
				{
					while(str.hasNext())
					{
						String s3 = str.next();
						inf+=s3+" ";
					}
					str.close();
					InfixExpression infi = new InfixExpression(inf);
					System.out.println("Infix Form: " + infi.toString());
					inf = infi.toString();
					infi.postfix();
					System.out.println("Postfix Form: " + infi.postfixExpression);
					if(infi.hasVariable())
					{
						HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
						char[] var = infi.variables();
						System.out.println("where:");
						for(int i = 0;i<var.length;i++)
						{
							if(!hashMap.containsKey(var[i]))
							{
								System.out.print(var[i]+" = ");
								int varNum = s.nextInt();
								hashMap.put(var[i], varNum);
							}
						}
						infi.setVarTable(hashMap);
					}
					System.out.println("Expression Value: " + infi.evaluate());
					System.out.println("");
				}
				else
				{
					while(str.hasNext())
					{
						String s3 = str.next();
						postf+=s3+" ";
					}
					str.close();
					PostfixExpression postfix = new PostfixExpression(postf);
					System.out.println("Postfix form: " + postfix.toString());
					if(postfix.hasVariable())
					{
						HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
						char[] var = postfix.variables();
						System.out.println("where:");
						for(int i = 0;i<var.length;i++)
						{
							if(!hashMap.containsKey(var[i]))
							{
								System.out.print(var[i]+" = ");
								int varNum = s.nextInt();
								hashMap.put(var[i], varNum);
							}
						}
						postfix.setVarTable(hashMap);
					}
					System.out.println("Expression Value: " + postfix.evaluate());
					System.out.println("");
				}
				
			}
			else
			{
				System.out.println("Input from a file");
				System.out.print("Enter file name: ");
				s.nextLine();
				String fileN = s.nextLine();
				System.out.println("");
				File f = new File(fileN);
				Scanner scFile = new Scanner(f);
				while(scFile.hasNextLine())
				{
					String line = scFile.nextLine();
					while(line.length()==0)
					{
						line+=scFile.nextLine();
					}
					Scanner str = new Scanner(line);
					String inf = "";
					String postf = "";
					if(str.next().equals("I"))
					{
						while(str.hasNext())
						{
							String s3 = str.next();
							inf+=s3+" ";
						}
						str.close();
						InfixExpression infi = new InfixExpression(inf);
						System.out.println("Infix Form: " + infi.toString());
						inf = infi.toString();
						infi.postfix();
						System.out.println("Postfix Form: " + infi.postfixExpression);
						if(infi.hasVariable())
						{
							HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
							Set<Character> var = infi.variablesSet();
							System.out.println("where:");
							for(int i = 0;i<var.size();i++)
							{
								String variables = scFile.nextLine();
								Scanner s3 = new Scanner(variables);
								String vari = s3.next();
								char v = vari.charAt(0);
								s3.next();
								int num = s3.nextInt();
								hashMap.put(v, num);
								System.out.println(v + " = "+num);
							}
							infi.setVarTable(hashMap);
						}
						System.out.println("Expression Value: " + infi.evaluate());	
						System.out.println("");
					}
					else
					{
						while(str.hasNext())
						{
							String s3 = str.next();
							postf+=s3+" ";
						}
						str.close();
						PostfixExpression postfix = new PostfixExpression(postf);
						System.out.println("Postfix form: " + postfix.toString());
						if(postfix.hasVariable())
						{
							HashMap<Character, Integer> hashMap = new HashMap<Character, Integer>();
							Set<Character> var = postfix.variablesSet();
							System.out.println("where:");
							for(int i = 0;i<var.size();i++)
							{
								String variables = scFile.nextLine();
								Scanner s3 = new Scanner(variables);
								String vari = s3.next();
								char v = vari.charAt(0);
								s3.next();
								int num = s3.nextInt();
								hashMap.put(v, num);
								System.out.println(v + " = "+num);
							}
							postfix.setVarTable(hashMap);
						}
						System.out.println("Expression Value: " + postfix.evaluate());	
						System.out.println("");
					}
				}
			}
		}
		s.close();
	}
	
	// helper methods if needed
}
