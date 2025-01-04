
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.DirectedGraph;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.EdgeType;

import org.apache.commons.collections15.Factory;

//No other imports allowed
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;







/**
 * Implementation of a directed graph using a min-heap structure for edge priorities.
 * 
 * @param <V> the type of vertices in the graph
 */
class ThreeTenGraph<V extends Comparable<V>> implements Graph<V, Destination<V>>, DirectedGraph<V, Destination<V>> {
    
    /**
     * The adjacency list of the graph, where each vertex maps to a min-heap of edges.
     */
    private LinkedHashMap<V, MinHeap<Destination<V>>> adjHeap = new LinkedHashMap<>();
	
    /**
     * Returns a view of all vertices in this graph.
     * 
     * @return a Collection view of all vertices in this graph
     */
    @Override
    public Collection<V> getVertices() {
        return new LinkedList<>(adjHeap.keySet());
    }

    /**
     * Returns a view of all edges in this graph.
     * 
     * @return a Collection view of all edges in this graph
     */
    @Override
    public Collection<Destination<V>> getEdges() {
        LinkedList<Destination<V>> edges = new LinkedList<>();
        for (MinHeap<Destination<V>> heap : adjHeap.values()) {
            edges.addAll(heap);
        }
        return edges;
    }

    /**
     * Returns the number of vertices in this graph.
     * 
     * @return the number of vertices in this graph
     */
    @Override
    public int getVertexCount() {
        return adjHeap.size();
    }

    /**
     * Returns the number of edges in this graph.
     * 
     * @return the number of edges in this graph
     */
    @Override
    public int getEdgeCount() {
        int edgeCount = 0;
        for (MinHeap<Destination<V>> heap : adjHeap.values()) {
            edgeCount += heap.size();
        }
        return edgeCount;
    }

    /**
     * Returns true if this graph contains the specified vertex.
     * 
     * @param vertex the vertex to check
     * @return true if this graph contains the vertex; false otherwise
     */
    @Override
    public boolean containsVertex(V vertex) {
        return adjHeap.containsKey(vertex);
    }

    /**
     * Returns a Collection view of the successors of the specified vertex.
     * A successor is a vertex connected to the given vertex by an outgoing edge.
     * 
     * @param vertex the vertex whose successors are to be returned
     * @return a Collection view of the successors of the specified vertex
     */
    @Override
    public Collection<V> getSuccessors(V vertex) {
        if (!adjHeap.containsKey(vertex)) {
            return Collections.emptyList();
        }

        MinHeap<Destination<V>> heap = adjHeap.get(vertex);
        LinkedList<V> successors = new LinkedList<>();
        for (Destination<V> edge : heap) {
            successors.add(edge.node);
        }

        Collections.sort(successors); // Ensure sorted order
        return successors;
    }

    /**
     * Returns a Collection view of the predecessors of the specified vertex.
     * A predecessor is a vertex connected to the given vertex by an incoming edge.
     * 
     * @param vertex the vertex whose predecessors are to be returned
     * @return a Collection view of the predecessors of the specified vertex
     */
    @Override
    public Collection<V> getPredecessors(V vertex) {
        LinkedList<V> predecessors = new LinkedList<>();
        for (V key : adjHeap.keySet()) {
            MinHeap<Destination<V>> heap = adjHeap.get(key);
            for (Destination<V> edge : heap) {
                if (edge.node.equals(vertex)) {
                    predecessors.add(key);
                }
            }
        }
        return predecessors;
    }

    /**
     * Finds and returns an edge connecting two specified vertices, if it exists.
     * 
     * @param v1 the source vertex
     * @param v2 the destination vertex
     * @return the edge connecting {@code v1} to {@code v2}, or null if no such edge exists
     */
    @Override
    public Destination<V> findEdge(V v1, V v2) {
        if (!adjHeap.containsKey(v1)) {
            return null;
        }

        MinHeap<Destination<V>> heap = adjHeap.get(v1);
        for (Destination<V> edge : heap) {
            if (edge.node.equals(v2)) {
                return edge;
            }
        }
        return null;
    }

    /**
     * Returns the endpoints of the specified edge as a {@link Pair}.
     * 
     * @param edge the edge whose endpoints are to be returned
     * @return the endpoints of the edge, or null if the edge does not exist
     */
    @Override
    public Pair<V> getEndpoints(Destination<V> edge) {
        for (V key : adjHeap.keySet()) {
            MinHeap<Destination<V>> heap = adjHeap.get(key);
            if (heap.contains(edge)) {
                return new Pair<>(key, edge.node);
            }
        }
        return null;
    }

    /**
     * Adds a vertex to this graph.
     * 
     * @param vertex the vertex to add
     * @return true if the vertex was successfully added; false otherwise
     * @throws IllegalArgumentException if the vertex is null
     */
    @Override
    public boolean addVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        if (adjHeap.containsKey(vertex)) {
            return false;
        }

        adjHeap.put(vertex, new MinHeap<>());
        return true;
    }

    /**
     * Adds an edge to this graph that connects the specified vertices.
     * 
     * @param e  the edge to add
     * @param v1 the source vertex
     * @param v2 the destination vertex
     * @return true if the edge was successfully added; false otherwise
     * @throws IllegalArgumentException if any parameter is null
     */
    @Override
    public boolean addEdge(Destination<V> e, V v1, V v2) {
        if (e == null || v1 == null || v2 == null) {
            throw new IllegalArgumentException("Null values not allowed");
        }
        if (!adjHeap.containsKey(v1) || !adjHeap.containsKey(v2)) {
            return false;
        }

        MinHeap<Destination<V>> heap = adjHeap.get(v1);

        // Check for duplicate edges
        for (Destination<V> existingEdge : heap) {
            if (existingEdge.node.equals(v2)) {
                return false; // No parallel edges allowed
            }
        }

        e.node = v2;
        heap.offer(e);
        return true;
    }

    /**
     * Removes a vertex and all its incident edges from this graph.
     * 
     * @param vertex the vertex to remove
     * @return true if the vertex was successfully removed; false otherwise
     */
    @Override
    public boolean removeVertex(V vertex) {
        if (!adjHeap.containsKey(vertex)) {
            return false;
        }

        adjHeap.remove(vertex);
        for (V key : adjHeap.keySet()) {
            MinHeap<Destination<V>> heap = adjHeap.get(key);
            heap.removeIf(edge -> edge.node.equals(vertex));
        }
        return true;
    }

    /**
     * Removes an edge from this graph.
     * 
     * @param edge the edge to remove
     * @return true if the edge was successfully removed; false otherwise
     */
    @Override
    public boolean removeEdge(Destination<V> edge) {
        for (V key : adjHeap.keySet()) {
            MinHeap<Destination<V>> heap = adjHeap.get(key);
            if (heap.remove(edge)) {
                return true;
            }
        }
        return false;
    }

    //********************************************************************************
    //   testing code goes here... edit this as much as you want!
    //********************************************************************************
    
    /**
     * Returns a string representation of the graph.
     *
     * @return a string representation of the graph
     */
    public String toString() {
        return super.toString();
    }

    /**
     * Main method to test the ThreeTenGraph class.
     *
     * @param args the command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create a set of nodes and edges to test with
        String[] nodes = {"X", "G", "Hat", "A!"};

        // Constructs a graph
        ThreeTenGraph<String> graph = new ThreeTenGraph<>();
        for (String n : nodes) {
            graph.addVertex(n);
        }

        Destination<String> e1 = new Destination<>("G", 1);
        Destination<String> e2 = new Destination<>("A!", 7);
        Destination<String> e3 = new Destination<>("X", 7);

        graph.addEdge(e1, "X", "G");
        graph.addEdge(e2, "X", "A!");
        graph.addEdge(e3, "Hat", "X");

        // Initial Tests
        if (graph.getVertexCount() == 4 && graph.getEdgeCount() == 3) {
            System.out.println("Yay 1");
        }

        if (graph.containsVertex("X") && graph.containsEdge(e2)) {
            System.out.println("Yay 2");
        }

        if (graph.getSuccessors("X").contains("G") && graph.getSuccessors("X").contains("A!")) {
            System.out.println("Yay 3");
        }

        // Test getVertices
        if (graph.getVertices().contains("X") && graph.getVertices().size() == 4) {
            System.out.println("Vertices are correct");
        }

        // Test getEdges
        if (graph.getEdges().contains(e1) && graph.getEdges().contains(e2) && graph.getEdges().size() == 3) {
            System.out.println("Edges are correct");
        }

        // Test findEdge
        if (graph.findEdge("X", "G").equals(e1)) {
            System.out.println("Edge lookup is correct");
        }

        // Test getEndpoints
        Pair<String> endpoints = graph.getEndpoints(e1);
        if (endpoints.getFirst().equals("X") && endpoints.getSecond().equals("G")) {
            System.out.println("Endpoints are correct");
        }

        // Test getPredecessors
        if (graph.getPredecessors("X").contains("Hat") && graph.getPredecessors("X").size() == 1) {
            System.out.println("Predecessors are correct");
        }

        // Test removeVertex
        if (graph.removeVertex("X") && !graph.containsVertex("X") && graph.getVertexCount() == 3) {
            System.out.println("Vertex removal is correct");
        }

        // Re-add the vertex and edges for further tests
        graph.addVertex("X");
        graph.addEdge(e1, "X", "G");
        graph.addEdge(e2, "X", "A!");

        // Test removeEdge
        if (graph.removeEdge(e1) && !graph.containsEdge(e1) && graph.getEdgeCount() == 2) {
            System.out.println("Edge removal is correct");
        }

        // Test graph consistency after edge removal
        if (graph.getSuccessors("X").contains("A!") && !graph.getSuccessors("X").contains("G")) {
            System.out.println("Graph consistency after edge removal is correct");
        }

        // Test isIncident
        if (graph.isIncident("X", e2)) {
            System.out.println("Incident edge test is correct");
        }

        // Test getNeighborCount
        if (graph.getNeighborCount("Hat") == 1 && graph.getNeighborCount("G") == 0) {
            System.out.println("Neighbor count test is correct");
        }

        // Test degree calculations
        if (graph.degree("X") == 2 && graph.inDegree("X") == 1 && graph.outDegree("X") == 1) {
            System.out.println("Degree calculations are correct");
        }

        // Test adding a duplicate vertex
        if (!graph.addVertex("X")) {
            System.out.println("Duplicate vertex test is correct");
        }

        // Test adding a duplicate edge
        if (!graph.addEdge(e2, "X", "A!")) {
            System.out.println("Duplicate edge test is correct");
        }

        System.out.println("All tests completed!");
    }
	
    //********************************************************************************
    //   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
    //********************************************************************************

    /**
     * Constructs an empty graph using a LinkedHashMap for adjacency storage.
     */
    @SuppressWarnings("unchecked")
    public ThreeTenGraph() {
        adjHeap = new LinkedHashMap<>();
    }

    /**
     * Checks if the specified vertex and edge are incident to each other.
     *
     * @param vertex the vertex to check
     * @param edge the edge to check
     * @return true if the vertex and edge are incident, false otherwise
     */
    public boolean isIncident(V vertex, Destination<V> edge) {
        return getIncidentEdges(vertex).contains(edge);
    }

    /**
     * Determines if the first vertex is a predecessor of the second vertex.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return true if v1 is a predecessor of v2, false otherwise
     */
    public boolean isPredecessor(V v1, V v2) {
        return getPredecessors(v1).contains(v2);
    }

    /**
     * Determines if the first vertex is a successor of the second vertex.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return true if v1 is a successor of v2, false otherwise
     */
    public boolean isSuccessor(V v1, V v2) {
        return getSuccessors(v1).contains(v2);
    }

    /**
     * Retrieves all incoming edges incident to the specified vertex.
     *
     * @param vertex the vertex whose incoming edges are to be retrieved
     * @return a collection of incoming edges incident to the vertex
     */
    public Collection<Destination<V>> getInEdges(V vertex) {
        LinkedList<Destination<V>> ret = new LinkedList<>();
        for (V pred : getPredecessors(vertex)) {
            ret.add(findEdge(pred, vertex));
        }
        return ret;
    }

    /**
     * Retrieves all outgoing edges incident to the specified vertex.
     *
     * @param vertex the vertex whose outgoing edges are to be retrieved
     * @return a collection of outgoing edges incident to the vertex
     */
    public Collection<Destination<V>> getOutEdges(V vertex) {
        LinkedList<Destination<V>> ret = new LinkedList<>();
        for (V succ : getSuccessors(vertex)) {
            ret.add(findEdge(vertex, succ));
        }
        return ret;
    }

    /**
     * Checks if two vertices share an incident edge.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return true if v1 and v2 share an incident edge, false otherwise
     */
    public boolean isNeighbor(V v1, V v2) {
        return (findEdge(v1, v2) != null);
    }

    /**
     * Checks if the specified edge is contained in the graph.
     *
     * @param edge the edge to check
     * @return true if the edge is contained in the graph, false otherwise
     */
    public boolean containsEdge(Destination<V> edge) {
        return (getEndpoints(edge) != null);
    }

    /**
     * Calculates the number of incoming edges for the specified vertex.
     *
     * @param vertex the vertex whose indegree is to be calculated
     * @return the indegree of the vertex
     */
    public int inDegree(V vertex) {
        return getInEdges(vertex).size();
    }

    /**
     * Calculates the number of outgoing edges for the specified vertex.
     *
     * @param vertex the vertex whose outdegree is to be calculated
     * @return the outdegree of the vertex
     */
    public int outDegree(V vertex) {
        return getOutEdges(vertex).size();
    }

    /**
     * Retrieves edges in the graph that match the specified edge type.
     *
     * @param edgeType the type of edges to retrieve
     * @return a collection of edges of the specified type, or null if unsupported
     */
    public Collection<Destination<V>> getEdges(EdgeType edgeType) {
        if (edgeType == EdgeType.DIRECTED) {
            return getEdges();
        }
        return null;
    }

    /**
     * Retrieves the source vertex of a directed edge.
     *
     * @param directedEdge the directed edge
     * @return the source vertex of the edge, or null if not applicable
     */
    public V getSource(Destination<V> directedEdge) {
        return getEndpoints(directedEdge).getFirst();
    }

    /**
     * Retrieves the destination vertex of a directed edge.
     *
     * @param directedEdge the directed edge
     * @return the destination vertex of the edge, or null if not applicable
     */
    public V getDest(Destination<V> directedEdge) {
        return getEndpoints(directedEdge).getSecond();
    }

    /**
     * Retrieves the number of edges of the specified type in the graph.
     *
     * @param edgeType the type of edges to count
     * @return the number of edges of the specified type
     */
    public int getEdgeCount(EdgeType edgeType) {
        if (edgeType == EdgeType.DIRECTED) {
            return getEdgeCount();
        }
        return 0;
    }

    /**
     * Calculates the total degree of the specified vertex.
     * 
     * <p>Special cases:
     * <ul>
     * <li>Self-loops are counted once.</li>
     * <li>If each neighbor is connected by one edge, the degree equals the neighbor count.</li>
     * <li>For directed graphs, the degree is the sum of indegree and outdegree, minus self-loops.</li>
     * </ul>
     *
     * @param vertex the vertex whose degree is to be calculated
     * @return the degree of the vertex
     */
    public int degree(V vertex) {
        return inDegree(vertex) + outDegree(vertex);
    }
    /**
     * Returns the number of vertices adjacent to the specified vertex.
     * This is equivalent to the size of the collection returned by {@code getNeighbors(vertex)}.
     *
     * @param vertex the vertex whose neighbor count is to be returned
     * @return the number of neighboring vertices
     */
    public int getNeighborCount(V vertex) {
		return getNeighbors(vertex).size();
	}
    
    /**
     * Returns a collection of edges that are incident to the specified vertex.
     *
     * @param vertex the vertex whose incident edges are to be returned
     * @return a collection of edges connected to the vertex,
     *         or an empty collection if the vertex is not present
     */
    public Collection<Destination<V>> getIncidentEdges(V vertex) {
        LinkedList<Destination<V>> ret = new LinkedList<>();
        ret.addAll(getInEdges(vertex));
        ret.addAll(getOutEdges(vertex));
        return ret;
    }

    /**
     * Returns a collection of vertices connected to the specified vertex via any edge.
     * If the vertex is connected to itself with a self-loop, it will be included.
     *
     * @param vertex the vertex whose neighbors are to be returned
     * @return a collection of vertices connected to the vertex,
     *         or an empty collection if the vertex is not present
     */
    public Collection<V> getNeighbors(V vertex) {
		LinkedList<V> ret = new LinkedList<>();
		
		Collection<V> fetch = getPredecessors(vertex);
		if(fetch != null) ret.addAll(fetch);
		fetch = getSuccessors(vertex);
		if(fetch != null) ret.addAll(fetch);
		
		return ret;
	}
    
    /**
     * Returns a collection of vertices connected to the specified edge.
     * For graphs where edges connect exactly two vertices, this collection
     * will contain those two vertices.
     *
     * @param edge the edge whose incident vertices are to be returned
     * @return a collection of vertices connected to the edge,
     *         or null if the edge is not present
     */
    public Collection<V> getIncidentVertices(Destination<V> edge) {
		Pair<V> p = getEndpoints(edge);
		if(p == null) return null;
		
		LinkedList<V> ret = new LinkedList<>();
		ret.add(p.getFirst());
		ret.add(p.getSecond());
		return ret;
	}

    /**
     * Returns the number of predecessors of the specified vertex in the graph.
     * This is equivalent to the size of the collection returned by {@code getPredecessors(vertex)}.
     *
     * @param vertex the vertex whose predecessor count is to be returned
     * @return the number of predecessors of the vertex
     */
    public int getPredecessorCount(V vertex) {
		return inDegree(vertex);
	}
    
    /**
     * Returns the number of successors of the specified vertex in the graph.
     * This is equivalent to the size of the collection returned by {@code getSuccessors(vertex)}.
     *
     * @param vertex the vertex whose successor count is to be returned
     * @return the number of successors of the vertex
     */
    public int getSuccessorCount(V vertex) {
		return outDegree(vertex);
	}
    
    /**
     * Returns the vertex opposite to the specified vertex on the specified edge.
     * That is, the vertex incident to the edge that is not the specified vertex.
     *
     * @param vertex the vertex whose opposite is to be found
     * @param edge the edge connecting the vertices
     * @return the vertex opposite to the specified vertex on the edge,
     *         or null if the vertex is not part of the edge
     */
    public V getOpposite(V vertex, Destination<V> edge) {
		Pair<V> p = getEndpoints(edge);
		if(p.getFirst().equals(vertex)) {
			return p.getSecond();
		}
		else {
			return p.getFirst();
		}
	}
    
    /**
     * Returns all edges connecting the specified vertices.
     * If the graph contains multiple edges connecting the vertices,
     * all such edges are returned. If the vertices are not connected,
     * returns an empty collection.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return a collection of edges connecting v1 and v2,
     *         or an empty collection if the vertices are not connected
     */
    public Collection<Destination<V>> findEdgeSet(V v1, V v2) {
		Destination<V> edge = findEdge(v1, v2);
		if(edge == null) {
			return null;
		}
		
		LinkedList<Destination<V>> ret = new LinkedList<>();
		ret.add(edge);
		return ret;
		
	}
	
    /**
     * Checks if the specified vertex is the source of the specified edge.
     *
     * @param vertex the vertex to be checked
     * @param edge   the edge to be checked
     * @return true if the vertex is the source of the edge, false otherwise
     */
    public boolean isSource(V vertex, Destination<V> edge) {
		return getSource(edge).equals(vertex);
	}
    
    /**
     * Checks if the specified vertex is the destination of the specified edge.
     *
     * @param vertex the vertex to be checked
     * @param edge   the edge to be checked
     * @return true if the vertex is the destination of the edge, false otherwise
     */
    public boolean isDest(V vertex, Destination<V> edge) {
		return getDest(edge).equals(vertex);
	}
    
    /**
     * Adds an edge to the graph, connecting two vertices.
     * Throws an exception if the edge type is undirected.
     *
     * @param e the edge to be added
     * @param v1 the source vertex
     * @param v2 the destination vertex
     * @param edgeType the type of the edge (must be directed)
     * @return true if the edge was added successfully, false otherwise
     * @throws IllegalArgumentException if the edge type is undirected
     */
    public boolean addEdge(Destination<V> e, V v1, V v2, EdgeType edgeType) {
		//NOTE: Only directed edges allowed
		
		if(edgeType == EdgeType.UNDIRECTED) {
			throw new IllegalArgumentException();
		}
		
		return addEdge(e, v1, v2);
	}
    
 /**
     * Adds an edge to the graph, connecting the specified vertices.
     * If the vertices are already connected by another edge, the method fails.
     *
     * @param edge the edge to be added
     * @param vertices the collection of vertices to be connected
     * @return true if the edge was added successfully, false otherwise
     * @throws IllegalArgumentException if the edge or vertices are null,
     *                                  or if the vertices are invalid for this graph
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(Destination<V> edge, Collection<? extends V> vertices) {
        if (edge == null || vertices == null || vertices.size() != 2) {
            return false;
        }

        V[] vs = (V[]) vertices.toArray();
        return addEdge(edge, vs[0], vs[1]);
    }

 /**
     * Adds an edge to the graph with the specified type.
     * This method enforces constraints on edge types and vertex connections.
     *
     * @param edge the edge to be added
     * @param vertices the vertices to be connected
     * @param edgeType the type of the edge (must be directed)
     * @return true if the edge was added successfully, false otherwise
     * @throws IllegalArgumentException if the edge or vertices are null,
     *                                  or if the edge type is invalid
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(Destination<V> edge, Collection<? extends V> vertices, EdgeType edgeType) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}
		
		V[] vs = (V[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1], edgeType);
	}
	
	//********************************************************************************
	//   DO NOT EDIT ANYTHING BELOW THIS LINE
	//********************************************************************************
	
	/**
     * Returns a {@code Factory} that creates an instance of this graph type.
     *
     * @param <V> the vertex type for the graph factory
     * @param <E> the edge type for the graph factory
     * @return a factory instance for creating directed graphs
     */
	public static <V,E> Factory<DirectedGraph<V,E>> getFactory() { 
		return new Factory<DirectedGraph<V,E>> () {
			@SuppressWarnings("unchecked")
			public DirectedGraph<V,E> create() {
				return (DirectedGraph<V,E>) new ThreeTenGraph();
			}
		};
	}
    
    /**
     * Returns the edge type of the specified edge in this graph.
     *
     * @param edge the edge whose type is to be returned
     * @return the {@code EdgeType} of the edge, or null if the edge has no defined type
     */
    public EdgeType getEdgeType(Destination<V> edge) {
		return EdgeType.DIRECTED;
	}
    
    /**
     * Returns the default edge type for this graph.
     *
     * @return the default {@code EdgeType} for this graph
     */
    public EdgeType getDefaultEdgeType() {
		return EdgeType.DIRECTED;
	}
    
    /**
     * Returns the number of vertices that are incident to the specified edge.
     * For regular edges, this is typically 2; for self-loops, it may be 1.
     *
     * <p>Equivalent to {@code getIncidentVertices(edge).size()}.
     *
     * @param edge the edge whose incident vertex count is to be returned
     * @return the number of vertices incident to the edge
     */
    public int getIncidentCount(Destination<V> edge) {
		return 2;
	}
}