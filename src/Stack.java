/* Class called Stack
 * NAME: Yuxin Zhou
 * ID:260806428
 */

public class Stack
{

	public ListNode top;
	
	//Constructor
	public Stack () 
	{
		super();
		top=null;
	}
	
	//push
	public void Push(String newdata) 
	{
		ListNode pushdata= new ListNode(newdata);
		
		//If stack is not empty
		if(top != null)
		{
			top.next= pushdata; //the next top is pushdata
			pushdata.previous= top; //previous to pushdata is the top
		}
		top= pushdata; //the new top is pushdata
	}
	
	//pop
	public String Pop() 
	{
		
		//verify if stack is empty
		if(top == null) 
		{
			System.out.println("Error!Empty Stack");
			return null;
		}
		
		String result = top.key_data;
		if (top.previous == null) 
		{ //only one element in stack
			top = null;
		}//if there's more than one element in the stack
		else
		{
			ListNode pretop =top.previous; //pretop = variable for previous to the top
			pretop.next=null; //next to pretop is now null, doesn't point to the top anymore
			top=pretop; //new top becomes pretop
		}
		return result;
	}

}



