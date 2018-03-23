/*Class called Queue
 * NAME: Yuxin Zhou
 * ID: 260806428
 */

//Queue need enqueue and dequeue
public class Queue 
{
	public ListNode front;
	public ListNode rear;
	
	//constructor
	public Queue()
	{
		super();
		front=null;
		rear=null;
	}
	
	//Enqueue
	public void Enqueue(String newdata)
	{
		ListNode data = new ListNode(newdata);
		
		//check if node is empty
		if(rear == null)
		{
			rear=data;
			front=data;
		}
		else 
		{
			data.next = rear;
			rear.previous = data;
			rear = data;
		}	
	}
	
	//Dequeue
	public String Dequeue () 
	{
		//check if there is front
		if(front == null)
		{
			System.out.println("Error: the queue is empty");
			return null;
		}
		
		String result = front.key_data;
		if(front.previous == null)
		{
			front= null;
			rear= null;
		}
		else //more than one element 
		{
			ListNode prefront = front.previous;
			prefront.next= null;
			front=prefront;
		}
		return result;
	}
}