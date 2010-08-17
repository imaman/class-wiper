package com.blogspot.javadots.classwiper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

   public static class Seq {
      public final String head;
      public final Seq tail;
      private Seq(String head, Seq tail) {
         this.head = head;
         this.tail = tail;
      }
      
      public static final Seq EMPTY = new Seq(null, null);
      
      public Seq prepend(String s) {
         return new Seq(s, this);         
      }

      public boolean notEmpty() {
         return this != EMPTY;
      }
   }
   
   private Set<String> vertices = new HashSet<String>();
   private Map<String,Seq> map = new HashMap<String,Seq>();
   
   public int vertexCount() {
      return map.size();
   }

   public void vertex(String vertex) {
      if(!map.containsKey(vertex))
         map.put(vertex, Seq.EMPTY);
      vertices.add(vertex);
   }

   public void edge(String from, String to) {
      vertex(from);
      vertex(to);
            
      Seq newSeq = fetchNeighbors(from).prepend(to);
      map.put(from, newSeq);
   }

   public Seq neighbors(String vertex) {
      return fetchNeighbors(vertex); 
   }

   private Seq fetchNeighbors(String vertex) {
      Seq result =  map.get(vertex);
      if(result == null)
         result = Seq.EMPTY;
      return result;
   }

   public List<String> traverseFrom(String vertex) {
      List<String> result = new ArrayList<String>();
      HashSet<String> visited = new HashSet<String>(Arrays.asList(vertex));
      
      for(Seq s = neighbors(vertex); s.notEmpty(); s = s.tail) 
         traverse(s.head, result, visited);

      return result;
   }

   private void traverse(String vertex, List<String> result, Set<String> visited) {
      if(visited.contains(vertex))
         return;
      
      visited.add(vertex);
      result.add(vertex);
      
      for(Seq s = neighbors(vertex); s.notEmpty(); s = s.tail) 
         traverse(s.head, result, visited);
   }

   public List<String> vertices() {
      List<String> result = new ArrayList<String>(vertices);
      Collections.sort(result);
      
      return result;
   }
}
