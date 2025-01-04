// no additional imports!
import java.util.Collection;
import java.util.Queue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A MinHeap is a priority queue represented as a balanced binary heap. The two
 * children of queue[n] are queue[2*n+1] and queue[2*(n+1)]. The priority queue
 * is ordered by the elements' natural ordering.
 *
 * @param <E> the type of elements held in this MinHeap, which must be Comparable.
 */
public class MinHeap<E extends Comparable<E>> implements Queue<E> {
    /**
     * The default initial capacity of the heap when empty.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    /**
     * The array representation of the binary heap.
     */
    private Comparable<E>[] queue;

    /**
     * The number of elements currently in the heap.
     */
    private int size = 0;

    /**
     * Constructs an empty MinHeap with the default initial capacity.
     */
    @SuppressWarnings("unchecked")
    public MinHeap() {
        this.queue = (Comparable<E>[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
    }

    /**
     * Constructs a MinHeap as a copy of another MinHeap.
     *
     * @param other the MinHeap to copy.
     */
    @SuppressWarnings("unchecked")
    public MinHeap(MinHeap<E> other) {
        this.size = other.size;
        this.queue = (Comparable<E>[]) new Comparable[other.queue.length];
        System.arraycopy(other.queue, 0, this.queue, 0, other.size);
    }

    /**
     * Inserts the specified element into the heap, maintaining the min-heap property.
     *
     * @param element the element to be added to the heap.
     * @return true if the element was successfully added.
     * @throws IllegalArgumentException if the specified element is null.
     */
    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Null elements are not allowed in MinHeap.");
        }
        if (size == queue.length) {
            grow(); // Double the capacity if needed.
        }
        queue[size] = element;
        siftUp(size); // Restore the min-heap property.
        size++;
        return true;
    }

    /**
     * Retrieves and removes the head of this heap, or returns null if the heap is empty.
     *
     * @return the smallest element in the heap, or null if the heap is empty.
     */
    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) queue[0]; // Suppress unchecked cast warning.
        queue[0] = queue[size - 1];
        queue[size - 1] = null;
        size--;
        siftDown(0); // Restore the min-heap property.
        return result;
    }

    /**
     * Retrieves, but does not remove, the head of this heap, or returns null if the heap is empty.
     *
     * @return the smallest element in the heap, or null if the heap is empty.
     */
    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked") // Suppress unchecked cast warning.
        E result = (E) queue[0];
        return result;
    }

    /**
     * Returns the number of elements in the heap.
     *
     * @return the size of the heap.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the heap is empty.
     *
     * @return true if the heap is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Doubles the capacity of the underlying array.
     */
    private void grow() {
        int newCapacity = queue.length * 2;
        queue = Arrays.copyOf(queue, newCapacity);
    }

    /**
     * Restores the min-heap property by moving the element at the specified index upwards.
     *
     * @param index the index of the element to sift up.
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (((E) queue[index]).compareTo((E) queue[parent]) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    /**
     * Restores the min-heap property by moving the element at the specified index downwards.
     *
     * @param index the index of the element to sift down.
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int leftChild = index * 2 + 1;
            int rightChild = index * 2 + 2;
            int smallest = leftChild;
            if (rightChild < size && ((E) queue[rightChild]).compareTo((E) queue[leftChild]) < 0) {
                smallest = rightChild;
            }
            if (((E) queue[index]).compareTo((E) queue[smallest]) <= 0) {
                break;
            }
            swap(index, smallest);
            index = smallest;
        }
    }

    /**
     * Swaps the elements at the specified indices in the heap.
     *
     * @param i the index of the first element.
     * @param j the index of the second element.
     */
    private void swap(int i, int j) {
        Comparable<E> temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    /**
     * Returns an iterator over the elements in the heap.
     * The iterator provides sequential access to the elements stored in the heap.
     * The iteration does not guarantee any specific order beyond the array's underlying order.
     *
     * @return an iterator over the elements in the heap.
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            /**
             * Checks if there are more elements to iterate over.
             *
             * @return true if there are remaining elements, false otherwise.
             */
            @Override
            public boolean hasNext() {
                return currentIndex < size; // Check if there are remaining elements.
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the heap.
             * @throws NoSuchElementException if there are no more elements to iterate over.
             */
            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the heap.");
                }
                @SuppressWarnings("unchecked")
            E result = (E) queue[currentIndex++];
                return result;
            }
        
            /**
             * Removes the last element returned by this iterator from the heap.
             * The heap structure is adjusted to maintain the min-heap property.
             *
             * @throws IllegalStateException if the remove method is called
             *         before next or after a previous remove call.
             */
            @Override
            public void remove() {
                if (currentIndex <= 0 || currentIndex > size) {
                    throw new IllegalStateException("Cannot remove element at this position.");
                }
                int indexToRemove = --currentIndex;
                MinHeap.this.remove(queue[indexToRemove]);
            }
        };
    }

    /**
     * Checks if the heap contains the specified element.
     *
     * @param o the object to search for in the heap.
     * @return true if the heap contains the specified object; false otherwise.
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (queue[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the heap to an array containing all elements.
     *
     * @return an array containing all elements in the heap.
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(queue, size); // Copies only the used portion of the array.
    }

    /**
     * Converts the heap to an array containing all elements, with the runtime type
     * of the specified array. If the specified array is too small, a new array
     * of the same runtime type is created.
     *
     * @param <T> the type of the array elements.
     * @param a the array into which the elements of the heap are to be stored.
     * @return an array containing the elements of the heap.
     * @throws ArrayStoreException if the runtime type of the specified array is not
     *                              compatible with the heap's elements.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            // Create a new array of the same runtime type as a, but large enough to hold the heap.
            return (T[]) Arrays.copyOf(queue, size, a.getClass());
        }
        // Copy elements into a and pad the remaining space with null (if any).
        System.arraycopy(queue, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null; // Null-terminate if the array has extra capacity.
        }
        return a;
    }

    /**
     * Removes a single instance of the specified element from the heap, if it is present.
     *
     * @param o the object to remove from the heap.
     * @return true if the heap contained the specified object; false otherwise.
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (queue[i].equals(o)) {
                queue[i] = queue[size - 1]; // Replace with the last element.
                queue[size - 1] = null;
                size--;
                siftDown(i); // Restore heap property.
                siftUp(i);   // Adjust upwards as well.
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the heap contains all elements in the specified collection.
     * This operation is not supported.
     *
     * @param c the collection to check for containment.
     * @return always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds all elements of the specified collection to the heap.
     * This operation is not supported.
     *
     * @param c the collection containing elements to be added to the heap.
     * @return always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all elements in the specified collection from the heap.
     * This operation is not supported.
     *
     * @param c the collection containing elements to be removed from the heap.
     * @return always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retains only the elements in the heap that are contained in the specified
     * collection. This operation is not supported.
     *
     * @param c the collection containing elements to be retained in the heap.
     * @return always throws UnsupportedOperationException.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all elements from the heap.
     * This operation is not supported.
     *
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds the specified element to the heap.
     *
     * @param e the element to add.
     * @return true if the element was added successfully.
     * @throws IllegalArgumentException if the element is null.
     */
    @Override
    public boolean add(E e) {
        return offer(e);
    }

    /**
     * Removes and returns the head of the heap.
     *
     * @return the head of the heap.
     * @throws NoSuchElementException if the heap is empty.
     */
    @Override
    public E remove() {
        E result = poll();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    /**
     * Retrieves, but does not remove, the head of the heap.
     *
     * @return the head of the heap.
     * @throws NoSuchElementException if the heap is empty.
     */
    @Override
    public E element() {
        E result = peek();
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }
}