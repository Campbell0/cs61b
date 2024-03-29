import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.In;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Performs some basic linked list tests.
 */
public class LinkedListDequeTest {

    @Test
    @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
    void noNonTrivialFields() {
        Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
        List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
    }

    @Test
	/* In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }

    @Test
    /* In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
       In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new LinkedListDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
	/* This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    // Below, you'll write your own tests for LinkedListDeque.
    @Test
	/* test the isEmpty() and size() method. */
    public void isEmptyAndSizeTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        assertThat(lld1.isEmpty()).isTrue();
        assertThat(lld1.size()).isEqualTo(0);

        lld1.addFirst(1);   // [1]
        assertThat(lld1.size()).isEqualTo(1);
        assertThat(lld1.isEmpty()).isFalse();

        lld1.removeLast();  //[]
        assertThat(lld1.size()).isEqualTo(0);
        assertThat(lld1.isEmpty()).isTrue();
    }
    @Test
    public void getTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        Integer res = lld1.get(0);
        assertThat(res).isNull();

        res = lld1.get(-1);
        assertThat(res).isNull();

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);
        lld1.addLast(4);
        lld1.addLast(5);

        res = lld1.get(0);
        assertThat(res).isEqualTo(1);

        res = lld1.get(5);
        assertThat(res).isNull();
    }

    @Test
    public void getRecursivelyTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();
        Integer res = lld1.getRecursive(0);
        assertThat(res).isNull();

        res = lld1.getRecursive(-1);
        assertThat(res).isNull();

        for (int i = 0; i < 5; ++i) {
            lld1.addLast(i);
        }

        res = lld1.getRecursive(0);
        assertThat(res).isEqualTo(1);

        res = lld1.getRecursive(2);
        assertThat(res).isEqualTo(3);

        res = lld1.getRecursive(4);
        assertThat(res).isEqualTo(5);

        res = lld1.getRecursive(5);
        assertThat(res).isNull();
    }

    @Test
    public void removeFirstAndRemoveLastTest() {
        Deque<Integer> lld1 = new LinkedListDeque<>();

        // Test when the Deque is empty.
        assertThat(lld1.removeFirst()).isNull();
        assertThat(lld1.removeLast()).isNull();

        // remove from one to empty.
        lld1.addLast(0);   // [0]
        Integer res = lld1.removeFirst();
        assertThat(res).isEqualTo(0);
        assertThat(lld1.size()).isEqualTo(0);

        lld1.addLast(0);   // [0]
        res = lld1.removeLast();
        assertThat(res).isEqualTo(0);
        assertThat(lld1.size()).isEqualTo(0);

        // remove from two to one.
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        res = lld1.removeFirst();
        assertThat(res).isEqualTo(0);
        assertThat(lld1.size()).isEqualTo(1);

        lld1.addLast(1);   // [0, 1]
        res = lld1.removeLast();
        assertThat(res).isEqualTo(1);
        assertThat(lld1.size()).isEqualTo(1);
    }

    @Test
    public void toStringTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        // empty case
        assertThat(lld1.toString()).isEqualTo("{}");

        for (int i = 0; i < 5; ++i) {
            lld1.addLast(i);
        }
        assertThat(lld1.toString()).isEqualTo("{0, 1, 2, 3, 4}");
    }

    @Test
    public void iteratorTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 5; ++i) {
            lld1.addLast(i);
        }
        ArrayList<Integer> list = new ArrayList<>();
        // use "foreach" loop
        for (Integer x : lld1) {
            list.addLast(x);
        }

        assertThat(list).containsExactly(0, 1, 2, 3, 4);
    }

    @Test
    public void equalTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        Deque<Integer> lld2 = new ArrayDeque<>();
        assertThat(lld1.equals(lld2)).isTrue();
        for (int i = 0; i < 5; ++i) {
            lld1.addLast(i);
        }
        for (Integer x : lld1) {
            lld2.addLast(x);
        }
        assertThat(lld1.equals(lld2)).isTrue();

        lld2.removeLast();
        assertThat(lld1.equals(lld2)).isFalse();
    }
}