package gk646.jnet;

import gk646.jnet.util.datastructures.LimitedQueue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueueTest {
    @Test
    void shiftTest() {
        int maxCapacity = 5;
        LimitedQueue<Integer> queue = new LimitedQueue<>(maxCapacity);
        queue.add(5);
        queue.add(4);
        for (int i = 0; i < 3; i++) {
            queue.add(1);
        }
        queue.add(10);

        assertEquals(5, queue.get(maxCapacity));


        for (int i = 0; i < 4; i++) {
            queue.add(4);
        }
        assertEquals(10, queue.get(5));


        queue.add(12);
        assertEquals(12, queue.get(0));
    }

    @Test
    void forEachTest() {
        int maxCapacity = 500;

        LimitedQueue<Integer> queue = new LimitedQueue<>(maxCapacity);
        for (int i = 0; i < maxCapacity; i++) {
            queue.add(i);
        }
        int counter= maxCapacity-1;
        for (int num : queue) {
            assertEquals(counter--, num);
        }

        for (int i = 0; i < 10000; i++) {
            queue.add(i);
        }
        assertEquals(9999, queue.get(0));
    }

    @Test
    void testBackwardsAccess() {
        int maxCapacity = 50;

        ArrayList<Integer> list = new ArrayList<>();
        LimitedQueue<Integer> queue = new LimitedQueue<>(maxCapacity);
        for (int i = 0; i < maxCapacity; i++) {
            list.add(i);
        }
        for (int i = maxCapacity-1; i >= 0; i--) {
            queue.add(i);
        }
        for (int i = maxCapacity-1; i >= 0; i--) {
            assertEquals(list.get(i), queue.get(i));
        }
    }


    @Test
    void addTest() {
        int maxCapacity = 5;

        LimitedQueue<Integer> queue = new LimitedQueue<>(maxCapacity);

        queue.add(3);
        queue.add(3);

        assertEquals(queue.directGet(0), 3);
    }
}
