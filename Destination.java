/**
 * Represents a destination with a priority, used in graphs and heaps.
 * This class is comparable based on its priority.
 *
 * @param <V> the type of the node
 */
class Destination<V> implements Comparable<Destination<V>> {
    /**
     * The node associated with this destination.
     */
    V node;

    /**
     * The priority associated with this destination.
     */
    Integer priority;

    /**
     * Constructs a new {@code Destination} with the specified node and priority.
     *
     * @param n the node
     * @param e the priority
     */
    Destination(V n, Integer e) {
        this.node = n;
        this.priority = e;
    }

    /**
     * Compares this destination to another based on their priorities.
     *
     * @param other the other destination to compare to
     * @return a negative integer, zero, or a positive integer as this destination
     *         has a priority less than, equal to, or greater than the other
     */
    @Override
    public int compareTo(Destination<V> other) {
        return this.priority.compareTo(other.priority);
    }

    /**
     * Returns the node associated with this destination.
     *
     * @return the node
     */
    public V getNode() {
        return node;
    }

    /**
     * Returns the priority associated with this destination.
     *
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Returns a string representation of this destination.
     * The string contains the node and its priority.
     *
     * @return a string representation of the destination
     */
    @Override
    public String toString() {
        return String.format("Destination[node=%s, priority=%d]", node, priority);
    }
}