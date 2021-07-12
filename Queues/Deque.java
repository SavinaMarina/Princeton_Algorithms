import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue or deque (pronounced “deck”) is a generalization of a stack and a queue that supports
 * adding and removing items from either the front or the back of the data structure.
 */

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> firstNode;
    private Node<Item> lastNode;
    private int size = 0;

    private class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("The argument is Null");
        Node<Item> newNode = new Node<>();
        newNode.item = item;
        if (firstNode != null) {
            newNode.next = firstNode;
            firstNode.previous = newNode;
        }
        firstNode = newNode;
        if (isEmpty()) {
            lastNode = newNode;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("The argument is Null");
        Node<Item> newNode = new Node<>();
        newNode.item = item;
        if (lastNode != null) {
            newNode.previous = lastNode;
            lastNode.next = newNode;
        }
        lastNode = newNode;
        if (isEmpty()) {
            firstNode = newNode;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("The deque is empty!");
        Item item = firstNode.item;
        if (firstNode.next != null) {
            firstNode.next.previous = null;
        }
        firstNode = firstNode.next;
        size--;
        if (isEmpty()) lastNode = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty!");
        Item item = lastNode.item;
        if (lastNode.previous != null) {
            lastNode.previous.next = null;
        }
        lastNode = lastNode.previous;
        size--;
        if (isEmpty()) firstNode = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = firstNode;

        public boolean hasNext() {
            return current != null;
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no more elements!");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        d.addLast("First");
        d.addFirst("Second");
        d.addLast("Third");
        d.addLast("Fourth");
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());

        StdOut.println("---Long Iterator---");

        Iterator<String> i = d.iterator();      
        while (i.hasNext()) {
            String s = i.next();   
            StdOut.println(s);
        }

        StdOut.println("---Short Iterator---");

        for (String s : d)
            StdOut.println(s);

        StdOut.println("---Remove Items---");
        StdOut.println(d.removeFirst());
        StdOut.println(d.removeLast());
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
        StdOut.println(d.removeLast());
        StdOut.println(d.removeLast());
        StdOut.println(d.isEmpty());
        StdOut.println(d.size());
    }

}