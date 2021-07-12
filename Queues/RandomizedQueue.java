import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] arr;
    private int n = 0;

    // construct an empty deque
    public RandomizedQueue() {
        arr = (Item[]) new Object[1];
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // resize array - double or shrink
    private void resize(int newSize) {
       Item[] newArr = (Item[]) new Object[newSize];
       for (int i = 0; i < (newSize > arr.length ? arr.length : newSize); i++) {
           newArr[i] = arr[i];
       }
       arr = newArr;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("The argument is Null");
        arr[n] = item;
        n++;
        if (n == arr.length) resize(n *2);
    }

    private int getRandomIndx() {
      return (int) (Math.random()*n);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty!");
        int ind = getRandomIndx();
        Item item = arr[ind];
        if (ind == arr.length) {
            arr[ind] = null;
        }
        else {
            arr[ind] = arr[n-1];
            arr[n-1] = null;
        }
        n--;
        if ((n > 0) && (n == arr.length/4))
            resize(arr.length/2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty!");
        return arr[getRandomIndx()];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final int[] randIndxArr = new int[n];
        private int m = 0;

        public RandomizedQueueIterator() {
            // create array with randomly distributed indexes from 0 to n-1
            for (int i = 0; i < n; i++) {
                randIndxArr[i] = i;
                m++;
            }
            StdRandom.shuffle(randIndxArr);
        }

        public boolean hasNext() {
            return m != 0;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no more elements!");
            Item item = arr[randIndxArr[m-1]];
            m--;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> d = new RandomizedQueue<>();
        System.out.println(d.isEmpty());
        System.out.println(d.size());
        d.enqueue("First");
        d.enqueue("Second");
        d.enqueue("Third");
        d.enqueue("Fourth");
        d.enqueue("Fifth");
        d.enqueue("Sixth");
        d.enqueue("Seventh");
        d.enqueue("Eight");
        d.enqueue("Nine");
        d.enqueue("Tenth");
        System.out.println(d.isEmpty());
        System.out.println(d.size());

        StdOut.println("---Long Iterator---");

        Iterator<String> i = d.iterator();
        while (i.hasNext()) {
            String s = i.next();
            StdOut.println(s);
        }

        StdOut.println("---Short Iterator---");

        for (String s : d)
            StdOut.println(s);

        StdOut.println("---Sample---");

        System.out.println(d.sample());
        System.out.println(d.sample());
        System.out.println(d.sample());
        System.out.println(d.sample());

        StdOut.println("---Dequeue---");

        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.dequeue());
        System.out.println(d.isEmpty());
    }

}