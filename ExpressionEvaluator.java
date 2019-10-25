//this project is made by William Beck for EECS 2500

import java.util.Stack;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpressionEvaluator
{	//globals
	StringQueue tokens;
	StringQueue postfix;
	StringStack things;
	//main
		public static void main(String [] args) {
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Input infix expressions or hit enter to quit");
			String exp = sc.nextLine();
			while(exp.length() > 0) {
				try {
					ExpressionEvaluator ev = new ExpressionEvaluator(exp);
					ev.convertExpression();
					String postfix = ev.getPostfix();
					System.out.println(postfix);
				}
				catch(Exception ex)
				{
					System.out.println("Badly formed expression");
				}
				exp = sc.nextLine();
			}
			System.out.println("DONE");
		}
	
	public ExpressionEvaluator(String expression) 
	{
		tokens = makeTokens(expression); //makes expression token
	}
	
	public String getPostfix() // this parses the expression given to it
	{
		Long value = 0l;
		StringStack operands = new StringStack();
		String result = "";
		while(postfix.isEmpty() == false) {
			String next = postfix.remove();
			if(Character.isDigit(next.charAt(0))) {
				operands.push(next);
			}
			else //cases for operators
			{
				if(next.equals("+")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left+right));
				}
				if(next.equals("-")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left-right));
				}
				if(next.equals("/")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left/right));
				}
				if(next.equals("*")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left*right));
				}
				if(next.equals("Q")) {
					Long only = Long.parseLong(operands.pop());
					operands.push(""+(Long)(long)Math.sqrt(only));
				}
				if(next.equals("C")) {
					Long only = Long.parseLong(operands.pop());
					operands.push(""+(Long)(long)Math.cbrt(only));
				}
				if(next.equals("<")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left<<right));
				}
				if(next.equals(">")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left>>right));
				}
				if(next.equals("^")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(Long)(long)Math.pow(left, right));
				}
				if(next.equals("%")) {
					Long right = Long.parseLong(operands.pop());
					Long left = Long.parseLong(operands.pop());
					operands.push(""+(left%right));
				}
				
				
			}
			result += next + " ";
		}//detects underflows/overflows
		Long finalAnswer = Long.parseLong(operands.pop());
		result += "\n= " + finalAnswer;
		if(finalAnswer < Integer.MIN_VALUE) {
			result += "\nUNDERFLOW";
		}
		if(finalAnswer > Integer.MAX_VALUE) {
			result += "\nOVERFLOW";
		}
		return result;
	}
	
	private StringQueue makeTokens(String expression) {
		StringQueue toks = new StringQueue();
		
		// checks if there is a valid token
		String currentToken = "";
		Long tokenType = -1l; // no token
		for(int i=0;i<expression.length();i++) {
			char next = expression.charAt(i);
			if(Character.isDigit(next)) {
				currentToken += next;
			}
			else {
				if(currentToken.length() > 0) {
					toks.add(currentToken);
					currentToken = "";
				}
				if(next != ' ') {
					toks.add("" + next);
				}
			}
		}
		
		if(currentToken.length() > 0) {
			toks.add(currentToken);
			currentToken = "";
		}

		return toks;
	}
	
	public void convertExpression()
	{
		things = new StringStack();
		postfix = new StringQueue();
		
		while(tokens.isEmpty() == false) {
			String nextTok = tokens.remove();
			// note: its easier to check for parens & digits first
			if(nextTok.equals("(")) {
				things.push(nextTok);
			}
			else if(nextTok.equals(")")) {
				processRightParen();
			}
			else if(Character.isDigit(nextTok.charAt(0)) == false) {
				processOperator(nextTok);
			}
			else {
				postfix.add(nextTok);
				
			}
		}
		// need to clear remaining stack
		while(things.isEmpty() == false) {
			postfix.add(things.pop());
		}

	}
	
	public void processRightParen()
	{
		String topTok = things.peek();
		while(topTok.equals("(") == false) {
			postfix.add(topTok);
			things.pop();
			topTok = things.peek();
		}
		things.pop();
	}
	
	public void processOperator(String token)
	{
		// if there are no things, there's not a top token
		if(things.isEmpty() == false) {
			
			String topTok = things.peek();
			while(things.isEmpty() == false
					&& topTok.equals("(") == false
					&& precedence(token) <= precedence(topTok)) {
				things.pop();
				postfix.add(topTok);

				// need to filter empty token situation
				if(things.isEmpty() == false) {
					topTok = things.peek();
				}
			}
		}
		things.push(token);
	}
	
	private int precedence(String t) {
		if(t.equals("+") || t.equals("-")) {
			return 1;
		}
		if(t.equals("*") || t.equals("/")) {
			return 2;
		}
		return 0;
		
	}
}
