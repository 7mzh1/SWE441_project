/* @(#)DirectedGraphPathBuilderTest.java
 * Copyright (c) 2017 The authors and contributors of JHotDraw. MIT License.
 */

package org.jhotdraw8.graph;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * DirectedGraphUniqueCostPathBuilderTest.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DirectedGraphUniqueCostPathBuilderTest {

    public DirectedGraphUniqueCostPathBuilderTest() {
    }

    private DirectedGraph<Integer, Double> createGraph() {
        DirectedGraphBuilder<Integer, Double> builder = new DirectedGraphBuilder<>();

        // __|  1  |  2  |  3  |  4  |  5  |   6
        // 1 |       7.0   9.0  14.0         14.0
        // 2 | 7.0        10.0  15.0
        // 3 |                  11.0          2.0
        // 4 |                         6.0
        // 5 |                                9.0
        // 6 |14.0                     9.0
        //
        //

        builder.addVertex(1);
        builder.addVertex(2);
        builder.addVertex(3);
        builder.addVertex(4);
        builder.addVertex(5);
        builder.addVertex(6);
        builder.addBidiArrow(1, 2, 7.0);
        builder.addArrow(1, 3, 9.0);
        builder.addArrow(1, 4, 14.0);
        builder.addBidiArrow(1, 6, 14.0);
        builder.addArrow(2, 3, 10.0);
        builder.addArrow(2, 4, 15.0);
        builder.addArrow(3, 4, 11.0);
        builder.addArrow(3, 6, 2.0);
        builder.addArrow(4, 5, 6.0);
        builder.addBidiArrow(5, 6, 9.0);
        return builder;
    }


    @Test
    public void testCreateGraph() {
        final DirectedGraph<Integer, Double> graph = createGraph();

        final String expected
                = "1 -> 2, 3, 4, 6.\n"
                + "2 -> 1, 3, 4.\n"
                + "3 -> 4, 6.\n"
                + "4 -> 5.\n"
                + "5 -> 6.\n"
                + "6 -> 1, 5.";

        final String actual = DumpGraphs.dumpAsAdjacencyList(graph);
        System.out.println(actual);

        assertEquals(expected, actual);
    }


    @TestFactory
    public List<DynamicTest> testFindShortestVertexPath() {
        return Arrays.asList(
                dynamicTest("nonunique", () -> doFindShortestVertexPath(1, 5, null, 0.0)),
                dynamicTest("2", () -> doFindShortestVertexPath(1, 4, VertexPath.of(1, 4), 14.0)),
                dynamicTest("3", () -> doFindShortestVertexPath(2, 6, VertexPath.of(2, 3, 6), 12.0)),
                dynamicTest("nopath", () -> doFindShortestVertexPath(2, 99, null, 0.0))
        );
    }

    /**
     * Test of findAnyPath method, of class DirectedGraphUniqueCostPathBuilder.
     */
    public void doFindShortestVertexPath(Integer start, Integer goal, VertexPath<Integer> expPath, double expCost) throws Exception {
        System.out.println("doFindShortestVertexPath start:" + start + " goal:" + goal + " expResult:" + expPath + " expCost: " + expCost);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg -> arg;
        DirectedGraphUniqueCostPathBuilder<Integer, Double> instance = new DirectedGraphUniqueCostPathBuilder<>(graph::getNextEntries, costf);
        Map.Entry<VertexPath<Integer>, Double> result = instance.findUniqueShortestVertexPath(start, goal::equals, Double.MAX_VALUE);
        if (result == null) {
            assertNull(expPath);
        } else {
            assertEquals(expPath, result.getKey());
            assertEquals(expCost, result.getValue().doubleValue());
        }
    }

    @TestFactory
    public List<DynamicTest> testFindShortestEdgeMultiGoalPath() throws Exception {
        return Arrays.asList(
                dynamicTest("1.nonunique", () -> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(5, 6), null)),
                dynamicTest("2.nonunique", () -> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(4, 5), null)),
                dynamicTest("3", () -> doFindShortestEdgeMultiGoalPath(2, Arrays.asList(3, 6), EdgePath.of(10.0))),
                dynamicTest("4.nonunique", () -> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(6, 5), null)),
                dynamicTest("5.nonunique", () -> doFindShortestEdgeMultiGoalPath(1, Arrays.asList(5, 4), null)),
                dynamicTest("6", () -> doFindShortestEdgeMultiGoalPath(2, Arrays.asList(6, 3), EdgePath.of(10.0))),
                dynamicTest("7.unreachable", () -> doFindShortestEdgeMultiGoalPath(2, Arrays.asList(600, 300), null))
        );
    }

    /**
     * Test of findAnyPath method, of class DirectedGraphUniqueCostPathBuilder.
     */
    public void doFindShortestEdgeMultiGoalPath(Integer start, List<Integer> multiGoal, EdgePath<Double> expResult) throws Exception {
        System.out.println("doFindShortestEdgeMultiGoalPath start:" + start + " goal:" + multiGoal + " expResult:" + expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg -> arg;
        DirectedGraphUniqueCostPathBuilder<Integer, Double> instance = new DirectedGraphUniqueCostPathBuilder<>(graph::getNextEntries, costf);

        // Find a path for each individual goal, and remember the shortest path
        EdgePath<Double> individualShortestPath = null;
        double individualShortestLength = Double.POSITIVE_INFINITY;
        for (Integer goal : multiGoal) {
            Map.Entry<EdgePath<Double>, Double> resultEntry = instance.findUniqueShortestEdgePath(start, goal::equals, Double.POSITIVE_INFINITY);
            if (resultEntry == null) {
                assertNull(expResult);
                return;
            } else {
                EdgePath<Double> result = resultEntry.getKey();
                double resultLength = result.getEdges().stream().mapToDouble(Double::doubleValue).sum();
                if (resultLength < individualShortestLength) {
                    individualShortestLength = resultLength;
                    individualShortestPath = result;
                }
            }
        }

        // Find shortest path to any of the goals
        Map.Entry<EdgePath<Double>, Double> actualShortestPath = instance.findUniqueShortestEdgePath(start, multiGoal::contains, Double.POSITIVE_INFINITY);
        double actualLength = actualShortestPath.getValue();

        System.out.println("  individual shortest path: " + individualShortestPath);
        System.out.println("  actual shortest path: " + actualShortestPath);

        assertEquals(individualShortestLength, actualLength);
        assertEquals(expResult, actualShortestPath.getKey());
    }

    @TestFactory
    public List<DynamicTest> testFindShortestEdgePath() throws Exception {
        return Arrays.asList(
                dynamicTest("1.nonunique", () -> doFindShortestEdgePath(1, 5, null)),
                dynamicTest("2", () -> doFindShortestEdgePath(1, 4, EdgePath.of(14.0))),
                dynamicTest("3", () -> doFindShortestEdgePath(2, 6, EdgePath.of(10.0, 2.0)))
        );
    }

    /**
     * Test of findAnyPath method, of class DirectedGraphUniqueCostPathBuilder.
     */
    private void doFindShortestEdgePath(Integer start, Integer goal, EdgePath<Double> expResult) throws Exception {
        System.out.println("doFindShortestEdgePath start:" + start + " goal:" + goal + " expResult:" + expResult);
        DirectedGraph<Integer, Double> graph = createGraph();
        ToDoubleFunction<Double> costf = arg -> arg;
        DirectedGraphUniqueCostPathBuilder<Integer, Double> instance = new DirectedGraphUniqueCostPathBuilder<>(graph::getNextEntries, costf);
        Map.Entry<EdgePath<Double>, Double> result = instance.findUniqueShortestEdgePath(start, goal::equals, Double.POSITIVE_INFINITY);
        assertEquals(expResult, result == null ? null : result.getKey());
    }

    private DirectedGraph<Integer, Double> createGraph2() {
        DirectedGraphBuilder<Integer, Double> b = new DirectedGraphBuilder<>();
        b.addVertex(1);
        b.addVertex(2);
        b.addVertex(3);
        b.addVertex(4);
        b.addVertex(5);

        b.addArrow(1, 2, 1.0);
        b.addArrow(1, 3, 1.0);
        b.addArrow(2, 3, 1.0);
        b.addArrow(3, 4, 1.0);
        b.addArrow(3, 5, 1.0);
        b.addArrow(4, 5, 1.0);
        return b;
    }


    @TestFactory
    public List<DynamicTest> testFindShortestVertexPathOverWaypoints() throws Exception {
        return Arrays.asList(
                dynamicTest("1", () -> doFindShortestVertexPathOverWaypoints(Arrays.asList(1, 3, 5), VertexPath.of(1, 3, 6, 5), 20.0)),
                dynamicTest("2", () -> doFindShortestVertexPathOverWaypoints(Arrays.asList(1, 4), VertexPath.of(1, 4), 14.0)),
                dynamicTest("3", () -> doFindShortestVertexPathOverWaypoints(Arrays.asList(2, 6), VertexPath.of(2, 3, 6), 12.0)),
                dynamicTest("4", () -> doFindShortestVertexPathOverWaypoints(Arrays.asList(1, 6, 5), VertexPath.of(1, 3, 6, 5), 20.0))
        );
    }

    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilder.
     */
    private void doFindShortestVertexPathOverWaypoints(List<Integer> waypoints, VertexPath<Integer> expResult, double expCost) throws Exception {
        System.out.println("doFindVertexPathOverWaypoints waypoints:" + waypoints + " expResult:" + expResult + " expCost:" + expCost);
        ToDoubleFunction<Double> costf = arg -> arg;
        DirectedGraph<Integer, Double> graph = createGraph();
        DirectedGraphUniqueCostPathBuilder<Integer, Double> instance = new DirectedGraphUniqueCostPathBuilder<>(graph::getNextEntries, costf);
        Map.Entry<VertexPath<Integer>, Double> actual = instance.findUniqueShortestVertexPathOverWaypoints(waypoints, Integer.MAX_VALUE);
        if (actual == null) {
            assertNull(expResult);
        } else {
            assertEquals(expResult, actual.getKey());
            assertEquals(expCost, actual.getValue().doubleValue());
        }
    }

    @TestFactory
    public List<DynamicTest> testFindEdgePathOverWaypoints() throws Exception {
        return Arrays.asList(
                dynamicTest("1.nonunique", () -> doFindEdgePathOverWaypoints(Arrays.asList(1, 5), null, 0.0)),
                dynamicTest("2", () -> doFindEdgePathOverWaypoints(Arrays.asList(1, 4), EdgePath.of(14.0), 14.0)),
                dynamicTest("3", () -> doFindEdgePathOverWaypoints(Arrays.asList(2, 6), EdgePath.of(10.0, 2.0), 12.0)),
                dynamicTest("4", () -> doFindEdgePathOverWaypoints(Arrays.asList(1, 6, 5), EdgePath.of(9.0, 2.0, 9.0), 20.0))
        );
    }

    /**
     * Test of findAnyVertexPath method, of class DirectedGraphPathBuilder.
     */
    private void doFindEdgePathOverWaypoints(List<Integer> waypoints, EdgePath<Double> expResult, double expCost) throws Exception {
        System.out.println("doFindVertexPathOverWaypoints waypoints:" + waypoints + " expResult:" + expResult);
        ToDoubleFunction<Double> costf = arg -> arg;
        DirectedGraph<Integer, Double> graph = createGraph();
        DirectedGraphUniqueCostPathBuilder<Integer, Double> instance = new DirectedGraphUniqueCostPathBuilder<>(graph::getNextEntries, costf);
        Map.Entry<EdgePath<Double>, Double> actual = instance.findUniqueShortestEdgePathOverWaypoints(waypoints, Integer.MAX_VALUE);
        if (actual == null) {
            assertNull(expResult);
        } else {
            assertEquals(expResult, actual.getKey());
            assertEquals(expCost, actual.getValue().doubleValue());
        }
    }
}