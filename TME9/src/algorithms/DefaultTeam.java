package algorithms;

import java.awt.Point;
import java.util.*;

/*
Algorithm Steps:
    1. Greedy Algorithm: Generates an initial solution by removing nodes that are most critical in forming cycles.
    2. Simulated Annealing: Starts from the initial solution and explores the solution space to find better solutions
        by probabilistically accepting worse solutions to escape local minima.
    3. Local Search: Fine-tunes the best solution found by the simulated annealing to further reduce the size of the FVS.
 */

public class DefaultTeam {
  public ArrayList<Point> calculFVS(ArrayList<Point> points, int edgeThreshold) {
    ArrayList<Point> bestResult = points;
    int i = 0;
    do {
      // Use the improved greedy algorithm to generate an initial solution.
      ArrayList<Point> initialSolution = grgeedy(points, edgeThreshold);
      // Use simulated annealing to further optimize the solution.
      ArrayList<Point> optimizedSolution = simulatedAnnealing(initialSolution, points, edgeThreshold);
      if (bestResult.size() > optimizedSolution.size())
        bestResult = optimizedSolution;
      i++;
    } while (i < 5);
    return bestResult;
  }

  private ArrayList<Point> grgeedy(ArrayList<Point> points, int edgeThreshold) {
    // Build the adjacency list representation of the graph.
    Map<Point, Set<Point>> adjacencyList = buildGraph(points, edgeThreshold);
    Set<Point> fvs = new HashSet<>();

    // Remove all leaves (nodes with degree 0 or 1) from the graph.
    remove(adjacencyList);

    // Main loop: Continue until the graph has no cycles.
    while (hasCycle(adjacencyList)) {
      List<Point> largestCycle = findLargestCycle(adjacencyList);

      if (largestCycle == null || largestCycle.isEmpty()) {
        break;
      }

      // Count the number of cycles each node participates in
      // Select the node within the largest cycle that participates in the most cycles.
      Map<Point, Integer> cycleCounts = countCyclesForNodes(adjacencyList);
      Point nodeToRemove = selectNodeInMostCycles(largestCycle, cycleCounts);

      // Add the selected node to the FVS and remove it from the graph.
      fvs.add(nodeToRemove);
      removeVertex(adjacencyList, nodeToRemove);
      remove(adjacencyList);
    }
    return new ArrayList<>(fvs);
  }

  /*
   * Builds the adjacency list representation of the graph.
   */
  private Map<Point, Set<Point>> buildGraph(ArrayList<Point> points, int edgeThreshold) {
    // map init
    Map<Point, Set<Point>> graph = new HashMap<>();
    for (Point p : points) {
      graph.put(p, new HashSet<>());
    }

    for (int i = 0; i < points.size(); i++) {
      Point p = points.get(i);
      for (int j = i + 1; j < points.size(); j++) {
        Point q = points.get(j);
        if (p.distance(q) < edgeThreshold) {
          graph.get(p).add(q);
          graph.get(q).add(p);
        }
      }
    }
    return graph;
  }

  private void remove(Map<Point, Set<Point>> graph) {
    Queue<Point> leaves = new LinkedList<>();
    for (Point v : graph.keySet()) {
      if (graph.get(v).size() <= 1) {
        leaves.add(v);
      }
    }

    while (!leaves.isEmpty()) {
      Point leaf = leaves.poll();
      if (!graph.containsKey(leaf)) {
        continue;
      }
      for (Point neighbor : graph.get(leaf)) {
        graph.get(neighbor).remove(leaf);
        if (graph.get(neighbor).size() == 1)    leaves.add(neighbor);
      }
      // Remove the leaf from the graph.
      graph.remove(leaf);
    }
  }

  private boolean hasCycle(Map<Point, Set<Point>> graph) {
    Set<Point> visited = new HashSet<>();
    // Perform DFS from each unvisited node.
    for (Point node : graph.keySet()) {
      if (!visited.contains(node)) {
        if (dfs(graph, node, null, visited))    return true;
      }
    }
    return false;
  }

  private boolean dfs(Map<Point, Set<Point>> graph, Point current, Point parent, Set<Point> visited) {
    visited.add(current);
    for (Point neighbor : graph.get(current)) {
      if (!neighbor.equals(parent)) {
        if (visited.contains(neighbor))     return true;
        else {
          if (dfs(graph, neighbor, current, visited))     return true;
        }
      }
    }
    return false;
  }

  private List<Point> findLargestCycle(Map<Point, Set<Point>> graph) {
    List<Point> largestCycle = new ArrayList<>();
    Set<Point> visited = new HashSet<>();

    // Perform DFS from each unvisited node to find cycles.
    for (Point node : graph.keySet()) {
      if (!visited.contains(node)) {
        List<Point> cycle = new ArrayList<>();
        if (findCycle(graph, node, null, visited, cycle)) {
          if (cycle.size() > largestCycle.size())     largestCycle = new ArrayList<>(cycle);
        }
      }
    }
    return largestCycle;
  }

  private boolean findCycle(Map<Point, Set<Point>> graph, Point current, Point parent, Set<Point> visited, List<Point> path) {
    visited.add(current);
    path.add(current);
    for (Point neighbor : graph.get(current)) {
      if (!neighbor.equals(parent)) {
        if (visited.contains(neighbor)) {         // Cycle detected, construct the cycle path.
          path.add(neighbor);
          return true;
        } else { if (findCycle(graph, neighbor, current, visited, path))     return true; }
      }
    }
    path.remove(current);
    return false;
  }

  private Map<Point, Integer> countCyclesForNodes(Map<Point, Set<Point>> graph) {
    Map<Point, Integer> cycleCounts = new HashMap<>();
    // Initialize cycle counts to zero.
    for (Point node : graph.keySet()) {
      cycleCounts.put(node, 0);
    }

    Set<Point> visited = new HashSet<>();
    // Perform DFS from each node to count cycles.
    for (Point node : graph.keySet()) {
      Set<Point> stack = new HashSet<>();
      countCyclesDFS(graph, node, null, visited, stack, cycleCounts);
    }

    return cycleCounts;
  }

  private void countCyclesDFS(Map<Point, Set<Point>> graph, Point current, Point parent, Set<Point> visited, Set<Point> stack, Map<Point, Integer> cycleCounts) {
    visited.add(current);
    stack.add(current);

    // Explore all adjacent nodes.
    for (Point neighbor : graph.get(current)) {
      if (!neighbor.equals(parent)) {
        if (stack.contains(neighbor)) {
          // Cycle detected, increment cycle counts for nodes in the stack.
          for (Point nodeInCycle : stack) {
            cycleCounts.put(nodeInCycle, cycleCounts.get(nodeInCycle) + 1);
          }
        } else if (!visited.contains(neighbor)) {
          countCyclesDFS(graph, neighbor, current, visited, stack, cycleCounts);
        }
      }
    }

    stack.remove(current);
  }

  private Point selectNodeInMostCycles(List<Point> cycle, Map<Point, Integer> cycleCounts) {
    Point selectedNode = null;
    int maxCycles = -1;
    for (Point node : cycle) {
      int cycles = cycleCounts.getOrDefault(node, 0);
      if (cycles > maxCycles) {
        maxCycles = cycles;
        selectedNode = node;
      }
    }
    return selectedNode;
  }

  private void removeVertex(Map<Point, Set<Point>> graph, Point vertex) {
    if (graph.containsKey(vertex)) {
      Set<Point> neighbors = new HashSet<>(graph.get(vertex));
      for (Point neighbor : neighbors) {
        graph.get(neighbor).remove(vertex);
      }
      graph.remove(vertex);
    }
  }

  private ArrayList<Point> simulatedAnnealing(ArrayList<Point> currentSolution, ArrayList<Point> points, int edgeThreshold) {
    double temperature = 1000.0;      // Initial temperature.
    double coolingRate = 0.999;      // Cooling rate.
    int maxIterations = 110000;       // Maximum number of iterations.

    ArrayList<Point> bestSolution = new ArrayList<>(currentSolution);
    int bestCost = bestSolution.size();

    Random random = new Random();

    // Main loop: Simulated Annealing iterations.
    for (int iteration = 0; iteration < maxIterations; iteration++) {
      ArrayList<Point> newSolution = generateNeighbor(currentSolution, points);

      if (isValidSolution(newSolution, points, edgeThreshold)) {
        int currentCost = currentSolution.size();
        int newCost = newSolution.size();
        if (newCost < currentCost) {
          currentSolution = new ArrayList<>(newSolution);         // Accept the new solution if it's better.
          if (newCost < bestCost) {
            bestSolution = new ArrayList<>(newSolution);
            bestCost = newCost;
          }
        } else {
          double acceptanceProbability = Math.exp((currentCost - newCost) / temperature);
          if (random.nextDouble() < acceptanceProbability) {
            currentSolution = new ArrayList<>(newSolution);
          }
        }
      }

      temperature *= coolingRate;
      if (temperature < 1e-32) {
        break;
      }
    }
    bestSolution = localSearch(bestSolution, points, edgeThreshold);
    return bestSolution;
  }

  private ArrayList<Point> generateNeighbor(ArrayList<Point> solution, ArrayList<Point> points) {
    ArrayList<Point> newSolution = new ArrayList<>(solution);
    Random random = new Random(System.nanoTime());
    int operation = random.nextInt(3);

    if (operation == 0 && !solution.isEmpty()) {                // Delete a random node from the solution.
      int indexToRemove = random.nextInt(solution.size());
      newSolution.remove(indexToRemove);
    } else if (operation == 1) {                                // Add a random node to the solution.
      ArrayList<Point> candidates = new ArrayList<>(points);
      candidates.removeAll(solution);
      if (!candidates.isEmpty()) {
        int indexToAdd = random.nextInt(candidates.size());
        newSolution.add(candidates.get(indexToAdd));
      }
    } else if (operation == 2 && !solution.isEmpty()) {         // Replace a node in the solution with another node.
      int indexToReplace = random.nextInt(solution.size());
      Point pointToRemove = solution.get(indexToReplace);
      ArrayList<Point> candidates = new ArrayList<>(points);
      candidates.removeAll(solution);
      if (!candidates.isEmpty()) {
        int indexToAdd = random.nextInt(candidates.size());
        Point pointToAdd = candidates.get(indexToAdd);
        newSolution.set(indexToReplace, pointToAdd);
      } else {
        newSolution.remove(pointToRemove);
      }
    }

    return newSolution;
  }

  private boolean isValidSolution(ArrayList<Point> candidateSolution, ArrayList<Point> points, int edgeThreshold) {
    // Rebuild the graph.
    Map<Point, Set<Point>> graph = buildGraph(points, edgeThreshold);
    for (Point v : candidateSolution) {
      removeVertex(graph, v);
    }
    return !hasCycle(graph);
  }

  private ArrayList<Point> localSearch(ArrayList<Point> solution, ArrayList<Point> points, int edgeThreshold) {
    ArrayList<Point> improvedSolution = new ArrayList<>(solution);
    boolean improved = true;
    while (improved) {
      improved = false;
      for (int i = 0; i < improvedSolution.size(); i++) {
        Point removedPoint = improvedSolution.remove(i);
        if (isValidSolution(improvedSolution, points, edgeThreshold))     improved = true;
        else    improvedSolution.add(i, removedPoint);
      }
    }
    return improvedSolution;
  }
}

