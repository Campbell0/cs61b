import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
	private int nextFirst;
	private int nextLast;
	private T[] items;
	private int size;
	private int capacity;
	public ArrayDeque() {
		capacity = 8;
		items = (T[]) new Object[capacity];
		size = 0;
		nextFirst = (capacity - 1) / 2;
		nextLast = capacity / 2;
	}


	private void resizeUp() {
		int newCapacity = capacity * 2;
		T[] newItems = (T[]) new Object[newCapacity];
		int newNextFirst = (newCapacity - 1) / 4;
		int newNextLast = newNextFirst + 1;
		for (int i = 0; i < size; i++) {
			newItems[newNextLast] = get(i);
			newNextLast = (newNextLast + 1) % newCapacity;
		}
		items = newItems;
		nextFirst = newNextFirst;
		nextLast = newNextLast;
		capacity = newCapacity;
	}
	private void resizeDown() {
		int newCapacity = capacity / 2;
		T[] newItems = (T[]) new Object[newCapacity];
		int newNextFirst = (newCapacity - 1) / 4;
		int newNextLast = newNextFirst + 1;
		for (int i = 0; i < size; i++) {
			newItems[newNextLast] = get(i);
			newNextLast = (newNextLast + 1) % newCapacity;
		}
		items = newItems;
		nextFirst = newNextFirst;
		nextLast = newNextLast;
		capacity = newCapacity;
	}
	private boolean checkIfSizeDown() {
		if (capacity <= 16) {
			return false;
		}
		return  ((double) size / capacity) < 0.25;
	}
	@Override
	public void addFirst(T x) {
		if (size >= capacity) {
			resizeUp();
		}
		items[nextFirst] = x;
		nextFirst = (nextFirst - 1 + capacity) % capacity;
		size++;
	}

	@Override
	public void addLast(T x) {
		if (size >= capacity) {
			resizeUp();
		}
		items[nextLast] = x;
		nextLast = (nextLast + 1) % capacity;
		size++;
	}

	@Override
	public List<T> toList() {
		List<T> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(get(i));
		}
		return list;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	public int capacity() {
		return capacity;
	}

	private int getFirstElementIndex() {
		return (nextFirst + 1) % capacity;
	}

	private int getLastElementIndex() {
		return (nextLast - 1 + capacity) % capacity;
	}
	@Override
	public T removeFirst() {
		if (size == 0) {
			return null;
		}
		int index = getFirstElementIndex();
		T temp = items[index];
		items[index] = null;
		nextFirst = index;
		size--;
		if (checkIfSizeDown()) {
			resizeDown();
		}
		return  temp;
	}

	@Override
	public T removeLast() {
		if (size == 0) {
			return null;
		}
		int index = getLastElementIndex();
		T temp = items[index];
		items[index] = null;
		nextLast = index;
		size--;
		if (checkIfSizeDown()) {
			resizeDown();
		}
		return temp;
	}

	@Override
	public T get(int index) {
		if (index > size - 1 || index < 0) {
			return null;
		}
		return items[(getFirstElementIndex() + index) % capacity];
	}

	@Override
	public T getRecursive(int index) {
		if (index > size - 1 || index < 0) {
			return null;
		}
		return getRecursiveHelper(index, (nextFirst + 1) % capacity);
	}

	private T getRecursiveHelper(int targetIndex, int currentIndex) {
		if (targetIndex == 0) {
			return items[currentIndex];
		}
		return getRecursiveHelper((targetIndex - 1) % capacity,
				(currentIndex + 1) % capacity);
	}
}
