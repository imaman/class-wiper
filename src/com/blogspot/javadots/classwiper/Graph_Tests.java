package com.blogspot.javadots.classwiper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.blogspot.javadots.classwiper.Graph.Seq;

public class Graph_Tests {

   @Test
   public void shouldHaveNoVerticesInitially() {
      Graph g = new Graph();
      assertEquals(0, g.vertexCount());
   }
   
   @Test
   public void sizeShouldReflectVertexCount() {
      Graph g = new Graph();
      g.vertex("v1");
      assertEquals(1, g.vertexCount());
   }   

   @Test
   public void anEdgeShouldDefineItsVertices() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      assertEquals(2, g.vertexCount());
   }

   @Test
   public void shouldNotCountAVertexTwice() {
      Graph g = new Graph();
      g.vertex("v1");
      g.edge("v1", "v2");
      assertEquals(2, g.vertexCount());
   }
   
   @Test
   public void shouldReportNoNeighborsForIsolatedVertex() {
      Graph g = new Graph();
      g.vertex("v1");
      assertFalse(g.neighbors("v1").notEmpty());
   }
   
   @Test
   public void shouldReportNeighbors() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      assertEquals(Arrays.asList("v2"), toList(g.neighbors("v1")));
   }
      
   private List<String> toList(Seq s) {
      List<String> result = new ArrayList<String>();
      for(Seq curr = s; curr.notEmpty(); curr = curr.tail)
         result.add(curr.head);
      return result;
   }

   @Test
   public void shouldReportOnlyNeighbors() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v2", "v3");
      g.edge("v2", "v4");
      assertEquals(Arrays.asList("v2"), toList(g.neighbors("v1")));
   }

   @Test
   public void shouldPresentFixedOrder() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v2", "v3");
      g.edge("v2", "v4");
      g.edge("v1", "v6");
      g.edge("v1", "v7");
      g.edge("v1", "v8");
      g.edge("v1", "v9");
      assertEquals(Arrays.asList("v9", "v8", "v7", "v6", "v2"), toList(g.neighbors("v1")));
   }
   
   
   @Test
   public void shouldTraverse() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v1", "v3");
      g.edge("v2", "v4");
      assertEquals(Arrays.asList("v4"), g.traverseFrom("v2"));
   }

   @Test
   public void shouldTraverseTransitively() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v1", "v3");
      g.edge("v2", "v4");
      assertEquals(Arrays.asList("v3", "v2", "v4"), g.traverseFrom("v1"));
   }

   @Test
   public void shouldVisitAVertexOnlyOnceInATraversal() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v1", "v3");
      g.edge("v1", "v4");
      g.edge("v2", "v4");
      assertEquals(Arrays.asList("v4", "v3", "v2"), g.traverseFrom("v1"));
   }
   
   @Test
   public void shouldReportVertices() {
      Graph g = new Graph();
      g.edge("v1", "v2");
      g.edge("v1", "v3");
      g.edge("v1", "v4");
      g.edge("v2", "v4");
      assertEquals(Arrays.asList("v1", "v2", "v3", "v4"), g.vertices());
   }
}
