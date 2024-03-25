import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;


import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

     @Test
     @DisplayName("ArrayDeque has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

     @Test
     public void addFirstTest() {
         Deque<Integer> lld1 = new ArrayDeque<>();

         lld1.addFirst(7);  // [7]
         lld1.addFirst(6);  // [6, 7]
         lld1.addFirst(5);  // [5, 6 ,7]
         assertThat(lld1.toList()).containsExactly(5, 6, 7).inOrder();

         lld1.addFirst(4);  // [4, 5, 6 ,7]
         lld1.addFirst(3);  // [3, 4, 5, 6 ,7]
         lld1.toList();
         lld1.addFirst(2);  // [2, 3, 4, 5, 6 ,7]
         lld1.addFirst(1);  // [1, 2, 3, 4, 5, 6 ,7]
         lld1.addFirst(0);  // [0, 1, 2, 3, 4, 5, 6 ,7]
         assertThat(lld1.toList()).containsExactly(0, 1, 2, 3, 4, 5, 6, 7).inOrder();
     }

    @Test
    public void addLastTest() {
         Deque<Integer> lld1 = new ArrayDeque<>();

        lld1.addLast(7);
        lld1.addLast(6);
        lld1.addLast(5);    // [7, 6, 5]
        assertThat(lld1.toList()).containsExactly(7, 6, 5).inOrder();

        lld1.addLast(4);
        lld1.addLast(3);
        lld1.addLast(2);
        lld1.addLast(1);
        lld1.addLast(0);    // [7, 6, 5, 4, 3, 2, 1]
        assertThat(lld1.toList()).containsExactly(7, 6, 5, 4, 3, 2, 1, 0).inOrder();
    }

    @Test
    public void addFirstAndAddLastTest() {
         Deque<String> lld1 = new ArrayDeque<>();

         lld1.addFirst("big");
         lld1.addLast("small");
         lld1.addLast("middle");
         lld1.addFirst("huge");
         lld1.addLast("giant"); // ["huge", "big", "small", "middle", "giant"]
         assertThat(lld1.toList()).containsExactly("huge", "big", "small", "middle", "giant");
    }

    @Test
    public void getTest() {
         Deque<Integer> lld1 = new ArrayDeque<>();
         assertThat(lld1.get(0)).isNull();

         lld1.addLast(1);
         lld1.addLast(2);
         lld1.addLast(3);

         assertThat(lld1.get(0)).isEqualTo(1);
         assertThat(lld1.get(2)).isEqualTo(3);
         assertThat(lld1.get(99)).isNull();
    }
    @Test
    public void getRecursiveTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.getRecursive(0)).isNull();

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);

        assertThat(lld1.getRecursive(0)).isEqualTo(1);
        assertThat(lld1.getRecursive(2)).isEqualTo(3);
        assertThat(lld1.getRecursive(99)).isNull();
    }

    @Test
    public void removeFirstAndRemoveLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

        // test removeFirst.
        lld1.addFirst(4);
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(0);   // [0, 1, 2, 3 ,4]
        // remove the element in the array's right end.
        Integer res = lld1.removeFirst();
        assertThat(res).isEqualTo(0);
        assertThat(lld1.toList()).containsExactly(1, 2, 3, 4);

        lld1.removeFirst();
        lld1.removeFirst();
        res = lld1.removeFirst();
        // remove the second last element
        assertThat(res).isEqualTo(3);
        res = lld1.removeFirst();
        // remove the last element
        assertThat(res).isEqualTo(4);
        assertThat(lld1.size()).isEqualTo(0);

        // test removeLast.
        lld1.addFirst(4);
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(0);   // [0, 1, 2, 3 ,4]
        // remove the element in the array's right end.
        res = lld1.removeLast();
        assertThat(res).isEqualTo(4);
        assertThat(lld1.toList()).containsExactly(0, 1, 2, 3);

        lld1.removeLast();
        lld1.removeLast();
        res = lld1.removeLast();
        // remove the second last element
        assertThat(res).isEqualTo(1);
        res = lld1.removeLast();
        // remove the last element
        assertThat(res).isEqualTo(0);
        assertThat(lld1.size()).isEqualTo(0);
    }
    @Test
    public void resizeUpAndResizeDownTest() {
         Deque<Integer> lld1 = new ArrayDeque<>();
         for (int i = 0; i < 32; ++i) {
            lld1.addLast(i);
         }
        assertThat(lld1.capacity()).isEqualTo(32);

         for (int i = 0; i < 25; ++i) {
            lld1.removeLast();
         }
         // 8 / 32 = 0.25
         assertThat(lld1.capacity()).isEqualTo(16);

         for (int i = 0; i < 5; ++i) {
             lld1.removeLast();
         }
        assertThat(lld1.capacity()).isEqualTo(16);
    }

}
