package com.blogspot.javadots.classwiper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Performance_Tests {
   @Test
   public void shouldProvideLinearTimeAccessToNeighbors() {
      long manyVertexTime = 0;
      long singleVertexTime = 0;
      
      for(int k = 0; k < 500; ++k) {
         
         Graph g = new Graph();
         String v = populate(g, 500);
         
         long t = System.nanoTime();
         g.neighbors(v);
         t = System.nanoTime() - t;
         singleVertexTime += t;
         
         g = new Graph();
         v = populate(g, 10000);
         
         t = System.nanoTime();
         g.neighbors(v);
         t = System.nanoTime() - t;
         manyVertexTime += t;
      } 
      
      
      // Convert to 10e-6
      manyVertexTime /= 1000;
      singleVertexTime /= 1000;
      
      String msg = "many=" + manyVertexTime + " single=" + singleVertexTime;
      System.out.println(msg);
      assertTrue(msg, 
         manyVertexTime < 10 * singleVertexTime);
   }

   private String populate(Graph g, int top) {
      for(int i = 0; i <= top; ++i)
         g.edge("v" + i, "v" + (i+1));
      return "v" + top;
   }
}
