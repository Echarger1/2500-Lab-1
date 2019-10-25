
public class StringQueue//creates nodes from stringnode
{
	private StringNode head;
	private StringNode tail;
	//adds item to stack
	public void add(String data) {
		StringNode next = new StringNode(data);
		if(head == null) {
			head = next;
			tail = next;
		}
		else {
			tail.setNext(next);
			tail = next;
		}
	}
	//removes item from stack
	public String remove() {
		String data = head.getData();
		head = head.getNext();
		return data;
	}
	//checks if empty
	public boolean isEmpty() {
		return head == null;
	}
}
