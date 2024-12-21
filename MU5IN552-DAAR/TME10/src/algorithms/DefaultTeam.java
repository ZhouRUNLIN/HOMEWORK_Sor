package algorithms;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultTeam {
    private ArrayList<Point> points;
    private int edgeThreshold;
    private List<List<Integer>> adjList;
    private Map<Point, Integer> pointIndex;

    public ArrayList<Point> calculDominatingSet(ArrayList<Point> points, int edgeThreshold) {
        if (points == null || points.isEmpty()) {
            return new ArrayList<>();
        }

        // Initialize fields
        this.points = points;
        this.edgeThreshold = edgeThreshold;

        // Determine dynamic number of clusters
        int numClusters = Math.max(2, points.size() / 50); // Adaptive number of clusters
        ArrayList<Point> finalSolution = points;
        for (int i = 0; i < 5; i++){
            // System.out.println("Clustering points using k-means with " + numClusters + " clusters...");
            List<List<Point>> clusters = kMeansClustering(points, numClusters);

            ArrayList<Point> combinedSolution = new ArrayList<>();

            // System.out.println("Calculating initial solution for each cluster using enhanced greedy algorithm...");
            for (List<Point> cluster : clusters) {
                this.points = new ArrayList<>(cluster);
                buildAdjList();
                combinedSolution.addAll(buildEnhancedGreedySolution());
            }

            // System.out.println("Rebuilding global adjacency list...");
            this.points = points;
            buildAdjList();

            // System.out.println("Optimizing combined solution using Targeted Simulated Annealing...");
            ArrayList<Point> temp = simulatedAnnealing(combinedSolution);

            // System.out.println("Final MDS size: " + finalSolution.size());
            if (temp.size() < finalSolution.size())
                finalSolution = temp;
            System.out.println("Final MDS size: " + i);
        }
        return finalSolution;
    }

    private void buildAdjList() {
        int n = points.size();
        pointIndex = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            pointIndex.put(points.get(i), i);
        }

        adjList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adjList.add(new ArrayList<>());
        for (int i = 0; i < n; i++) {
            Point p = points.get(i);
            for (int j = i + 1; j < n; j++) {
                Point q = points.get(j);
                if (p.distance(q) < edgeThreshold) {
                    adjList.get(i).add(j);
                    adjList.get(j).add(i);
                }
            }
        }
    }

    private ArrayList<Point> buildEnhancedGreedySolution() {
        boolean[] dominated = new boolean[points.size()];
        ArrayList<Point> solution = new ArrayList<>();
        PriorityQueue<Integer> candidatePoints = new PriorityQueue<>(
                (a, b) -> Integer.compare(countCoverage(b, dominated), countCoverage(a, dominated))
        );

        for (int i = 0; i < points.size(); i++) {
            candidatePoints.add(i);
        }

        while (!isFullyDominated(dominated)) {
            int best = candidatePoints.poll();
            if (dominated[best]) continue;
            solution.add(points.get(best));
            markDominated(best, dominated);
        }

        return cleanUselessDominatingPoints(solution);
    }

    private ArrayList<Point> simulatedAnnealing(ArrayList<Point> currentSolution) {
        double temperature = 5000.0;
        double coolingRate = 0.999;
        int maxIterations = 20000;

        ArrayList<Point> bestSolution = new ArrayList<>(currentSolution);
        int bestCost = bestSolution.size();
        Random random = new Random();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            ArrayList<Point> newSolution = generateNeighbor(currentSolution, random);

            if (isFullyDominated(computeDominated(newSolution))) {
                int currentCost = currentSolution.size();
                int newCost = newSolution.size();

                if (newCost < currentCost) {
                    currentSolution = new ArrayList<>(newSolution);
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
            if (temperature < 1e-24) {
                break;
            }
        }

        return bestSolution;
    }

    private List<List<Point>> kMeansClustering(ArrayList<Point> points, int k) {
        Random random = new Random(System.nanoTime());
        List<Point> centroids = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            centroids.add(points.get(random.nextInt(points.size())));
        }

        Map<Point, List<Point>> clusters;
        boolean changed;

        do {
            clusters = new HashMap<>();
            for (Point centroid : centroids) {
                clusters.put(centroid, new ArrayList<>());
            }

            for (Point point : points) {
                Point closestCentroid = centroids.stream()
                        .min(Comparator.comparingDouble(centroid -> point.distance(centroid)))
                        .orElseThrow();
                clusters.get(closestCentroid).add(point);
            }

            changed = false;
            List<Point> newCentroids = new ArrayList<>();
            for (Point centroid : centroids) {
                List<Point> clusterPoints = clusters.get(centroid);
                if (!clusterPoints.isEmpty()) {
                    int avgX = (int) clusterPoints.stream().mapToInt(p -> p.x).average().orElse(0);
                    int avgY = (int) clusterPoints.stream().mapToInt(p -> p.y).average().orElse(0);
                    Point newCentroid = new Point(avgX, avgY);
                    newCentroids.add(newCentroid);
                    if (!newCentroid.equals(centroid)) {
                        changed = true;
                    }
                } else {
                    newCentroids.add(centroid);
                }
            }
            centroids = newCentroids;
        } while (changed);

        return new ArrayList<>(clusters.values());
    }

    private boolean isFullyDominated(boolean[] dominated) {
        for (boolean b : dominated) {
            if (!b) return false;
        }
        return true;
    }

    private int countCoverage(int candidate, boolean[] dominated) {
        int count = 0;
        if (!dominated[candidate]) count++;
        for (int nei : adjList.get(candidate)) {
            if (!dominated[nei]) count++;
        }
        return count;
    }

    private void markDominated(int idx, boolean[] dominated) {
        dominated[idx] = true;
        for (int nei : adjList.get(idx)) {
            dominated[nei] = true;
        }
    }

    private ArrayList<Point> cleanUselessDominatingPoints(ArrayList<Point> dominating) {
        ArrayList<Point> res = new ArrayList<>(dominating);
        for (int i = 0; i < res.size(); i++) {
            Point p = res.get(i);
            ArrayList<Point> test = new ArrayList<>(res);
            test.remove(p);
            if (!isFullyDominated(computeDominated(test))) {
                // Keep p
            } else {
                res = test;
                i--;
            }
        }
        return res;
    }

    private boolean[] computeDominated(ArrayList<Point> solution) {
        boolean[] dominated = new boolean[points.size()];
        for (Point p : solution) {
            int idx = pointIndex.get(p);
            markDominated(idx, dominated);
        }
        return dominated;
    }

    private ArrayList<Point> generateNeighbor(ArrayList<Point> solution, Random random) {
        ArrayList<Point> newSolution = new ArrayList<>(solution);
        int operation = random.nextInt(3);

        Set<Point> solSet = new HashSet<>(solution);
        if (operation == 0 && !solution.isEmpty()) {
            int indexToRemove = random.nextInt(newSolution.size());
            newSolution.remove(indexToRemove);
        } else if (operation == 1) {
            ArrayList<Point> candidates = new ArrayList<>(points);
            candidates.removeAll(solSet);
            if (!candidates.isEmpty()) {
                int indexToAdd = random.nextInt(candidates.size());
                newSolution.add(candidates.get(indexToAdd));
            }
        } else if (operation == 2 && !solution.isEmpty()) {
            int indexToReplace = random.nextInt(newSolution.size());
            Point pointToRemove = newSolution.get(indexToReplace);
            ArrayList<Point> candidates = new ArrayList<>(points);
            candidates.removeAll(solSet);
            if (!candidates.isEmpty()) {
                int indexToAdd = random.nextInt(candidates.size());
                Point pointToAdd = candidates.get(indexToAdd);
                newSolution.set(indexToReplace, pointToAdd);
            } else {
                newSolution.remove(pointToRemove);
            }
        }
        return cleanUselessDominatingPoints(newSolution);
    }
}
