import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
	private final Node sentinel;
	private int size;
	private static class Node<T> {
		Node next;
		Node prev;
		T val;
		public Node(T val, Node prev, Node next) {
			this.val = val;
			this.prev = prev;
			this.next = next;
		}
	}
	public LinkedListDeque() {
		sentinel = new Node(63, null, null);
		sentinel.next = sentinel;
		sentinel.prev = sentinel;
		size = 0;
	}

	public LinkedListDeque(int val) {
		sentinel = new Node(63, null, null);
		sentinel.next = new Node(val, sentinel, sentinel);
		sentinel.prev = sentinel.next;
		size = 1;
	}

	@Override
	public void addFirst(T x) {
		Node temp = sentinel.next;
		sentinel.next = new Node(x, sentinel, temp);
		temp.prev = sentinel.next;
		size++;
	}

	@Override
	public void addLast(T x) {
		Node temp = sentinel.prev;
		sentinel.prev = new Node(x, temp, sentinel);
		temp.next = sentinel.prev;
		size++;
	}

	@Override
	public List<T> toList() {
		List<T> returnList = new ArrayList<>();
		Node head = sentinel.next;
		while (head != sentinel) {
			returnList.add((T) head.val);
			head = head.next;
		}
		return returnList;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T removeFirst() {
		if (size == 0) {
			return null;
		}
		T temp = (T) sentinel.next.val;
		sentinel.next = sentinel.next.next;
		sentinel.next.prev = sentinel;
		size--;
		return temp;
	}

	@Override
	public T removeLast() {
		if (size == 0) {
			return null;
		}
		T temp = (T) sentinel.prev.val;
		sentinel.prev = sentinel.prev.prev;
		sentinel.prev.next = sentinel;
		size--;
		return temp;
	}

	@Override
	public T get(int index) {
		if (index > size - 1 || index < 0) {
			return null;
		}

		Node head = sentinel;
		for (int i = 0; i <= index; ++i) {
			head = head.next;
		}
		return (T) head.val;
	}

	@Override
	public T getRecursive(int index) {
		if (index > size - 1 || index < 0) {
			return null;
		}
		return getRecursiveHelper(index, sentinel);
	}

	public T getRecursiveHelper(int index, Node head) {
		if (index < 0) {
			return (T) head.val;
		}
		return (T) getRecursiveHelper(index - 1, head.next);
	}
}
