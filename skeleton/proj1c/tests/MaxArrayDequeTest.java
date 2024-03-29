import deque.MaxArrayDeque;
import java.util.Comparator;

import org.junit.jupiter.api.*;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDequeTest {
	@Test
	public void defaultComparatorTest() {
		MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<>();
		for (int i = 0; i < 10; ++i) {
			lld1.addLast(i);
		}
		assertThat(lld1.max()).isEqualTo(9);
		lld1.removeLast();
		assertThat(lld1.max()).isEqualTo(8);

		MaxArrayDeque<String> lld2 = new MaxArrayDeque<>();
		lld2.addLast("dog");
		lld2.addLast("cat");
		lld2.addLast("pig");
		lld2.addLast("bear");
		lld2.addLast("elegant");
		assertThat(lld2.max()).isEqualTo("pig");
	}

	@Test
	public void specifiedComparatorTest() {
		MaxArrayDeque<Integer> lld1 = new MaxArrayDeque<>();
		for (int i = 0; i < 10; ++i) {
			lld1.addLast(i);
		}
		Comparator<Integer> comparator1 =
				(Integer a, Integer b) -> b.compareTo(a);

		assertThat(lld1.max(comparator1)).isEqualTo(0);
		lld1.removeFirst();
		assertThat(lld1.max(comparator1)).isEqualTo(1);

		Comparator<String> comparator2=
				(String a, String b) -> b.compareTo(a);

		MaxArrayDeque<String> lld2 = new MaxArrayDeque<>();
		lld2.addLast("dog");
		lld2.addLast("cat");
		lld2.addLast("pig");
		lld2.addLast("bear");
		lld2.addLast("elegant");
		assertThat(lld2.max(comparator2)).isEqualTo("bear");
	}
}
