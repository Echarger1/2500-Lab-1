
public class StringStack //this is my stack class
{
	private StringNode top;
	//standard stack operations
	public void push(String data) {
		StringNode newTop = new StringNode(data);
		newTop.setNext(top);
		top = newTop;
	}
	//pops top of stack
	public String pop() {
		String data = top.getData();
		top = top.getNext();
		return data;
	}
	//sees if theres something on the stack
	public String peek() {
		return top.getData();
	}
	//checks if empty
	public boolean isEmpty() {
		return top == null;
	}
}
