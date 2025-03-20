package org.rhw.bmr.project.common.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * PageRank algo
 *
 */
public class CustomPageRankAlgorithm {

    public static Map<Long, Double> computePageRank(Map<Long, List<Long>> graph,
                                                    double dampingFactor,
                                                    int maxIterations,
                                                    double epsilon) {
        if (graph == null || graph.isEmpty()) {
            return new HashMap<Long, Double>();
        }

        Map<Long, List<Long>> reverseGraph = buildReverseGraph(graph);
        Map<Long, Integer> outDegreeMap = buildOutDegreeMap(graph);

        int nodeCount = graph.size();
        Map<Long, Double> pageRank = new HashMap<Long, Double>();
        for (Long node : graph.keySet()) {
            pageRank.put(node, 1.0 / nodeCount);
        }

        for (int iter = 0; iter < maxIterations; iter++) {
            double danglingSum = 0.0;
            for (Long node : graph.keySet()) {
                int outDeg = outDegreeMap.get(node);
                if (outDeg == 0) {
                    danglingSum += pageRank.get(node);
                }
            }

            Map<Long, Double> newPageRank = new HashMap<Long, Double>();
            for (Long node : graph.keySet()) {
                double rank = (1.0 - dampingFactor) / nodeCount
                        + dampingFactor * (danglingSum / nodeCount);

                List<Long> inNeighbors = reverseGraph.get(node);
                if (inNeighbors != null && !inNeighbors.isEmpty()) {
                    for (Long inNode : inNeighbors) {
                        int outDeg = outDegreeMap.get(inNode);
                        if (outDeg > 0) {
                            rank += dampingFactor * (pageRank.get(inNode) / outDeg);
                        }
                    }
                }

                newPageRank.put(node, rank);
            }

            double diff = calculateDifference(pageRank, newPageRank);
            pageRank = newPageRank; // 更新

            if (diff < epsilon) {
                break;
            }
        }

        return pageRank;
    }


    /**
     * Construction d'une table de voisinage inverse à partir d'une table de voisinage directe
     */
    private static Map<Long, List<Long>> buildReverseGraph(Map<Long, List<Long>> graph) {
        Map<Long, List<Long>> reverseGraph = new HashMap<Long, List<Long>>();
        for (Long node : graph.keySet()) {
            reverseGraph.put(node, new ArrayList<Long>());
        }
        for (Long node : graph.keySet()) {
            List<Long> outNodes = graph.get(node);
            if (outNodes != null) {
                for (Long outNode : outNodes) {
                    reverseGraph.get(outNode).add(node);
                }
            }
        }
        return reverseGraph;
    }

    /**
     * Construct the out-degree table of each node
     */
    private static Map<Long, Integer> buildOutDegreeMap(Map<Long, List<Long>> graph) {
        Map<Long, Integer> outDegreeMap = new HashMap<Long, Integer>();
        for (Long node : graph.keySet()) {
            List<Long> outList = graph.get(node);
            int outDegree = (outList == null) ? 0 : outList.size();
            outDegreeMap.put(node, outDegree);
        }
        return outDegreeMap;
    }

    /**
     * Calculate the sum of the differences in PageRank values between iterations.
     */
    private static double calculateDifference(Map<Long, Double> oldPR, Map<Long, Double> newPR) {
        double diff = 0.0;
        for (Long node : oldPR.keySet()) {
            diff += Math.abs(oldPR.get(node) - newPR.get(node));
        }
        return diff;
    }
}
