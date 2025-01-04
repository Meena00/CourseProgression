import java.io.*;
import java.util.Scanner;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashSet;

/**
 * Provides methods for constructing a graph from a file
 * and performing a topological sort on a directed acyclic graph (DAG).
 */
class TopologicalSort {


    /**
     * Constructs a {@link ThreeTenGraph} from the specified file.
     *
     * <p>The file format:
     * <ul>
     *   <li>Number of nodes</li>
     *   <li>Names of the nodes</li>
     *   <li>Number of edges</li>
     *   <li>Edges in the format "start,end,priority"</li>
     * </ul>
     *
     * @param filename the name of the input file
     * @return a {@link ThreeTenGraph} constructed from the file contents
     * @throws IOException if an error occurs while reading the file
     */
    public static ThreeTenGraph<String> getGraph(String filename) throws IOException {
        ThreeTenGraph<String> graph = new ThreeTenGraph<>();

        try (Scanner r = new Scanner(new File(filename))) {
            int numNodes = Integer.parseInt(r.nextLine());
            for (int i = 0; i < numNodes; i++) {
                String nodeName = r.nextLine().trim();
                graph.addVertex(nodeName);
            }

            int numEdges = Integer.parseInt(r.nextLine());
            for (int i = 0; i < numEdges; i++) {
                String[] fromToPriority = r.nextLine().trim().split(",");
                graph.addEdge(
                    new Destination<>(fromToPriority[1], Integer.parseInt(fromToPriority[2])),
                    fromToPriority[0],
                    fromToPriority[1]
                );
            }
        }

        return graph;
    }

    /**
     * Performs a topological sort on the given graph, starting from the specified node.
     *
     * <p>The sort respects edge priorities by visiting lower-priority edges first.
     * If the graph contains a cycle, this method will throw an exception.
     *
     * @param <T>       the type of the vertices in the graph
     * @param graph     the {@link ThreeTenGraph} to sort
     * @param startNode the node to begin the sorting
     * @return a {@link LinkedList} of nodes in topological order
     * @throws IllegalArgumentException if the graph is null, the startNode is null,
     *                                  the startNode is not in the graph, or the graph contains a cycle
     */
    public static <T extends Comparable<T>> LinkedList<T> topologicalSort(ThreeTenGraph<T> graph, T startNode) {
        if (graph == null || startNode == null) {
            throw new IllegalArgumentException("Graph or starting node cannot be null");
        }

        // Check if startNode exists in the graph
        if (!graph.containsVertex(startNode)) {
            throw new IllegalArgumentException("Graph does not contain starting node");
        }

        // Sets to track started and finished nodes
        HashSet<T> started = new HashSet<>();
        HashSet<T> finished = new HashSet<>();

        // Stack for the sorted order
        LinkedList<T> sortedOrder = new LinkedList<>();

        boolean dfsDone = false;

        while (!dfsDone) {
            // Visit the start node
            if (!visit(graph, startNode, started, finished, sortedOrder)) {
                throw new IllegalArgumentException("Graph contains a cycle.");
            }

            // Check if all nodes have been visited
            dfsDone = true;
            for (T v : graph.getVertices()) {
                if (!finished.contains(v)) {
                    startNode = v;
                    dfsDone = false;
                    break;
                }
            }
        }

        return sortedOrder;
    }

    /**
     * Visits a node in the graph recursively using depth-first traversal.
     *
     * <p>This method helps identify cycles and maintain the sorted order of nodes
     * in a topological sort.
     *
     * @param graph        the {@link ThreeTenGraph} to traverse
     * @param currentNode         the node being visited
     * @param started      the set of nodes that have been started but not finished
     * @param finished     the set of nodes that have been fully processed
     * @param sortedOrder  the resulting topological order
     * @param <T>          the type of the vertices in the graph
     * @return true if the node is processed successfully, false if a cycle is detected
     */
    private static <T extends Comparable<T>> boolean visit(
            ThreeTenGraph<T> graph, T currentNode, HashSet<T> started,
            HashSet<T> finished, LinkedList<T> sortedOrder) {
        started.add(currentNode);

        // Get neighbors in sorted reverse order
        LinkedList<T> revNeighbors = new LinkedList<>(graph.getSuccessors(currentNode));
        Collections.reverse(revNeighbors);

        // Visit each neighbor
        for (T neighbor : revNeighbors) {
            // Detect cycle
            if (started.contains(neighbor) && !finished.contains(neighbor)) {
                return false;
            }

            // Visit unvisited neighbors
            if (!started.contains(neighbor)) {
                if (!visit(graph, neighbor, started, finished, sortedOrder)) {
                    return false;
                }
            }
        }

        // Mark current node as finished and add to sorted order
        finished.add(currentNode);
        sortedOrder.addFirst(currentNode);

        return true;
    }
}