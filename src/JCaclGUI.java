import java.util.StringTokenizer;
import acm.gui.TableLayout;
import acm.program.Program;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* Name: Yuxin Zhou
 * ID: 260806428
 * Calculator
 */
@SuppressWarnings("serial")
public class JCaclGUI extends Program implements ChangeListener, ActionListener
{
	private JTextField expression= new JTextField();//Display for the user input
	private JTextField answer= new JTextField();//Display for the answer
	private JTextField decimals= new JTextField(); //display for the JSlider value
	private double result=0.0; //Initializing global variable that contains the result
	private JSlider precision= new JSlider(0,10,6);;
	
	
	public void init() 
	{
		//setting size of the Applet
		setSize(500,600); 
		
		//setting the layout of the Applet by inserting a new TableLayout 
		setLayout(new TableLayout(10,4)); 
	
		//making the JTextFields non editable by the user
		expression.setEditable(false);
		answer.setEditable(false);
		decimals.setEditable(false);
		
		//row 1
		add(expression, "gridwidth = 4 height= 70");
		
		//row 2
		add(answer,"gridwidth = 4 height= 70");
		
		//row 3 to row 7: Calculator buttons
		String BUTTON_SIZE = "70";
		
		/*Arraylist: order assigned knowing that the layout manager assigns everything from left to right, top to bottom*/
		String BUTTON_LABEL[]= {"(",")","DEL","C","^","/","\u03C0","%","7","8","9","*","4","5","6","+",
								"1","2","3","-","0",".","(-)","="}; 
		String constraint = "width=" + BUTTON_SIZE + " height=" + BUTTON_SIZE;
		
		for (int i = 0; i < BUTTON_LABEL.length; i++) //for loop to facilitate the addition of buttons the calc.
		{
					
			JButton currentB = new JButton(BUTTON_LABEL[i]);
			currentB.addActionListener(this); //called when buttons are clicked
					
			add(currentB, constraint);
		}
				
		/*row 8: Precision adjustments 
		[JSlider implementation]*/
		add(new JLabel("precision"));
		add(precision,"gridwidth = 2");
		precision.addChangeListener(this); //called when user grabs slider's knob
		add(decimals);
		decimals.setText("6"); //default precision
		

		
	}

	//overriding the stateChanged method in the ChangeListener class
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		// TODO Auto-generated method stub
		//System.out.println("Slider changes");
		int value = precision.getValue();
		decimals.setText(String.valueOf(value));
		
		if(result == 0.0)
		{
			answer.setText(""); /*if the result is equal to the initialization, 
								don't display anything because it means that no computation of expression was done.
								Thus no number to set precision*/
		}
		else
			answer.setText(String.format("%1$."+value+"f", result));	/*Format the double to the precision wanted by 
																		converting the Jslider's value into a floating point
																		and adding it to the first par of the result (after the point)
																		Return a formatted String*/
	}

	//overriding the actionPerformed method in the ActionListener class
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String cmd= e.getActionCommand();
			
		try 
		{
			switch(cmd)
			{
				default: expression.setText(expression.getText() + cmd); //default is anything that is not part of the following cases
							break;
				case "C": removeAll(); //calling removeAll method
								break;
							
				case "=" : result = Compute_postfix(expression.getText()); //calling Compute_postfix method
						   answer.setText(String.format("%1$."+6+"f", result)); //formatting the default answer to 6 decimal place
						   break;
				case "(-)": expression.setText("-"); //unary minus
							break;
				case "DEL": int n = expression.getText().length()-1; //last position
							String new_string = expression.getText().substring(0, n); /*return a substring that goes from index 0
																						to the last position(n)*/
							expression.setText(new_string);
			}
		}
		catch(NullPointerException | NumberFormatException | StringIndexOutOfBoundsException g ) 
		{
			answer.setText("ERROR!");
		}	
	}
	
	//method to clear expression,answer and reset the precision to 6
	public void removeAll()
	{
		result=0.0;
		expression.setText("");
		answer.setText(null);
		decimals.setText("6");
		precision.setValue(6);
	}
	
	
	
	//method to convert string into int to facilitate precedence check
		public int num(String expr) 
		{
			if(expr.compareTo("+") == 0 || expr.compareTo("-") == 0)
				return 1;
			else if (expr.compareTo("/") == 0 || expr.compareTo("*") == 0 )
				return 2;
			else if (expr.compareTo("^") == 0 )
				return 3;
			else if (expr.compareTo("u") == 0 || expr.compareTo("%") == 0 || expr.compareTo("p") == 0)
				return 4;
			else 
				return 0;
		}
		
		//method to convert unary minus to a "u" 
		public String u_minus(String input) 
		{
		
			StringBuilder new_string= new StringBuilder(input);
			
			for (int n = 0; n< input.length(); n++) 
			{
				if(n == 0 && input.charAt(n) == '-')
				{
					new_string.setCharAt(n, 'u');
					input=new_string.toString();
				}
				else if(input.charAt(n) == '-' ||input.charAt(n) == '+' 
										|| input.charAt(n) == '*' || input.charAt(n) == '-' 
										||input.charAt(n) == '^'||input.charAt(n) == '(')
				{
					if(input.charAt(n+1) == '-')
					{
						new_string.setCharAt(n+1, 'u');
						input=new_string.toString();
					}
				}
			}
			return input;
			
		}
		
		//method to convert pi to "p" 
		public String pi(String input)
		{
			StringBuilder a= new StringBuilder(input);
			
			for(int n=0; n<input.length(); n++)
			{
				if(input.charAt(n) == '\u03C0')
				{
					a.setCharAt(n, 'p');
					input=a.toString();
				}
			}
			return input;
		}

		/*Method to compute infix to postfix*/
		public Queue Shunting_Yard(String str)
		{
			Queue Q= new Queue();
			Stack S= new Stack();
				
			//calling method to replace unary minus with "u"
			String u_stg= u_minus(str);
			
			//calling method to replace pi with p
			String stg= pi(u_stg);
				
			//Separate input in tokens
			StringTokenizer st = new StringTokenizer(stg, "+-*^/()up%", true);
				

			//ShuntingYard algorithm: operands in queue, operators in stack
			while(st.hasMoreTokens()) //check token
			{
				//defining variable for next token from st
				String tkn=st.nextToken();
			
				if(Character.isDigit(tkn.charAt(0))) //if digit,enqueues it 
				{
					Q.Enqueue(tkn);
				}
				else //if != digit, then must be operator, or the u we set for the unary minus or the p for pi
				{
					if(S.top == null) 	//if stack is empty, push token in 
					{
						S.Push(tkn);
					}
						
					//Implementing Brackets:
					
					//Comparing top of stack with left_bracket
					else if(S.top.key_data.compareTo("(")  == 0) //if top of stack is "(" (==0), push token in stack
					{
						S.Push(tkn);
					}
					//comparing next token to right_bracket
					else if(tkn.compareTo(")") == 0) //if next token is a ")" (==0), 
					{
						while(S.top.key_data.compareTo("(") != 0) //as long as the top of the stack is not "("
						{
							Q.Enqueue(S.Pop()); //Pop the stack and enqueue it
						}
						S.Pop();//and pop the left bracket
					}
					//comparing next token to left bracket
					else if(tkn.compareTo("(") == 0) //if next token is "("
					{
						S.Push(tkn); //push it onto the stack
					}
						
					else if(num(tkn) > num(S.top.key_data)) //if tkn bigger than the top element in the stack
					{
						S.Push(tkn);//push it on the stack
					}
					else 
					{
						while( S.top != null && num(tkn) <= num(S.top.key_data)) //as long as the stack is not empty
																					//AND that the int associated to the next token
																					//is smaller or equal than the int associated with the top 
						{
							Q.Enqueue(S.Pop()); //Pop stack and enqueue it.
						}
							
						S.Push(tkn); 		
					}
				}
					
			}
				
			//as long as the the stack is not empty
			while(S.top != null) 
			{
				if(S.top.key_data.compareTo("(") == 0) //and that the top of the stack is "("
				{
					S.Pop(); //pop everything
					continue; //skips the rest and goes back to while
				}
				Q.Enqueue(S.Pop());//else, pop stack and enqueue it
					
			}
				
				return Q; //returning the queue so it can be used by Compute_postfix method
		}
		
			
		//Method to compute operators 
		public void compute_Operators(String op, Stack new_s)
		{
			if(new_s.top == null) //if the user only enters pi, then the stack is empty
			{
				if(op.equals("p"))
				{
					new_s.Push(String.valueOf(Math.PI)); //push the result of pi into stack
				}

			}
			else
			{
			
				double digit1 = Double.parseDouble(new_s.Pop());
	
				if(op.equals("u"))
				{
					new_s.Push(String.valueOf(-digit1));
				}
				else if(op.equals("p"))
				{	
					new_s.Push(String.valueOf(digit1*Math.PI));
				}
				else if(op.equals("%"))
				{
					new_s.Push(String.valueOf(digit1/100));
				}
				else 
				{
					double digit2 = Double.parseDouble(new_s.Pop()) ;
					double result = 0.0;
							
					if (op.equals("+"))
					{
						result= digit2 + digit1;
					}
					else if (op.equals("-"))
					{
						result= digit2 - digit1;
					}
					else if (op.equals("*"))
					{
						result= digit2 * digit1;
					}
					else if (op.equals("/"))
					{
						result= digit2 / digit1;
					}
					else if (op.equals("^"))
					{
						result = Math.pow(digit2, digit1);
					}
					else
					{
						print("Error");
					}
					new_s.Push(String.valueOf(result)); //after computing, push it back in the stack
				}
			}
		}
			
		//Method to compute postfix to result	
		public double Compute_postfix(String user_input)
		{
			Stack p_stack = new Stack();
			
			//calling Shunting_Yard method
			Queue postfix= Shunting_Yard(user_input);
					
			while(postfix.front != null)
			{
				String tkn= postfix.Dequeue();
					
				if(Character.isDigit(tkn.charAt(0))) //if digit,stack it 
				{
					p_stack.Push(tkn);	
				}
				else ////if it's an operator or a "u" or a "p"
				{
					compute_Operators(tkn, p_stack); //calling compute_Operators method
				}	
			}
			return(Double.parseDouble(p_stack.Pop()));	
		}
		
}

