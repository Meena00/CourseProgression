# Graph Algorithms and Data Structures Project

This project demonstrates my skills in software engineering by implementing efficient algorithms and custom data structures for solving graph-related problems. It includes the creation of a directed graph, a priority queue (min-heap), and a topological sort algorithm. These components highlight my abilities to write clean, efficient, and modular code while solving complex problems.

---

## Project Objective

The goal of this project is to represent and manipulate directed graphs with edge priorities using a custom implementation. It solves problems like finding the order of tasks (topological sort), managing priorities efficiently (min-heap), and working with graph relationships.

---

## Files Overview

### 1. `Destination.java`
- **Purpose**: Represents edges in the graph, including the destination node and its priority (e.g., cost or weight).
- **Problem Solved**: Helps in sorting edges by priority, making it useful for algorithms like shortest paths or topological sort.
- **Key Skills**: Object-oriented programming, custom comparators, encapsulation.

---

### 2. `Display.java`
- **Purpose**: Provides an easy-to-read display of graph structures for debugging and testing.
- **Problem Solved**: Simplifies understanding and debugging complex graph relationships.
- **Key Skills**: User interface design for debugging, attention to detail.

---

### 3. `MinHeap.java`
- **Purpose**: Implements a priority queue using a binary heap structure.
- **Problem Solved**: Efficiently manages tasks or edges based on priority, ensuring optimal performance for operations like `poll` and `offer`.
- **Key Methods**: `offer`, `poll`, `peek`, `siftUp`, `siftDown`.
- **Key Skills**: Algorithm design, data structure implementation, performance optimization.

---

### 4. `ThreeTenGraph.java`
- **Purpose**: Implements a directed graph with adjacency lists and edge priorities using the `MinHeap`.
- **Problem Solved**: Manages graph operations such as adding/removing nodes and edges, finding neighbors, and handling directed connections.
- **Key Methods**:
  - `addVertex` and `addEdge`: Build the graph dynamically.
  - `getSuccessors` and `getPredecessors`: Retrieve related nodes.
  - `findEdge`, `getEndpoints`: Analyze edge relationships.
- **Key Skills**: Graph theory, dynamic data structures, problem-solving.

---

### 5. `TopologicalSort.java`
- **Purpose**: Implements a topological sort algorithm for directed acyclic graphs (DAGs) with edge priorities.
- **Problem Solved**: Determines the order of tasks while respecting priorities and ensures the graph has no cycles.
- **Key Methods**:
  - `getGraph`: Reads graph data from a file and constructs a graph.
  - `topologicalSort`: Sorts nodes in a valid order while detecting cycles.
  - `visit`: Uses depth-first search to traverse the graph.
- **Key Skills**: Algorithm design, recursion, file handling, cycle detection.
