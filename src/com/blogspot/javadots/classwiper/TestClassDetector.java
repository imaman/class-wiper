package com.blogspot.javadots.classwiper;

import java.util.List;

import org.junit.Test;


public class TestClassDetector {

   private static final String ORG_JUNIT_TEST = Test.class.getName();
   
   private Graph graph;
   private List<String> testClasses;

   public TestClassDetector(Graph graph) {
      this.graph = graph;
   }

   public boolean isTestClass(String className) {
      return testClasses.contains(className);
   }

   public void run() {
      testClasses = graph.traverseFrom(ORG_JUNIT_TEST);
   }
}
