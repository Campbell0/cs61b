package deque;

import java.util.Comparator;

public class MaxArrayDeque<T extends Comparable<T>> extends ArrayDeque<T> {
	GenericComparator<T> comparator;
	public static class GenericComparator<T extends Comparable<T>> implements Comparator<T> {

		@Override
		public int compare(T o1, T o2) {
			return o1.compareTo(o2);
		}

	}
	public MaxArrayDeque() {
		super();
		comparator = new GenericComparator<>();
	}
	public MaxArrayDeque(GenericComparator<T> c) {
		super();
		comparator = c;
	}

	public T max() {
		if (size() == 0) {
			return null;
		}
		T maximum = get(0);
		for (int i = 1; i < size(); ++i) {
			if (maximum.compareTo(get(i)) < 0) {
				maximum = get(i);
			}
		}
		return maximum;
	}

	public T max(Comparator<T> c) {
		if (size() == 0) {
			return null;
		}
		T maximum = get(0);
		for (int i = 1; i < size(); ++i) {
			if (c.compare(maximum, get(i)) > 0) {
				maximum = maximum;
			}
			else {
				maximum = get(i);
			}
		}
		return maximum;
	}
}
