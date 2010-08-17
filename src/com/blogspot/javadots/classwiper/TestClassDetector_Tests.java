package com.blogspot.javadots.classwiper;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestClassDetector_Tests {

   @Test
   public void jreClassesAreNotTests() {
      Graph graph = new Graph();

      String NUMBER = Number.class.getName();
      graph.edge(Object.class.getName(), NUMBER);
      TestClassDetector td = new TestClassDetector(graph);
      td.run();
      assertFalse(td.isTestClass(NUMBER));
   }

   @Test
   public void classDependingOnOrgJunitTestIsATestClass() {
      Graph graph = new Graph();

      String SOME_CLASS = "some.Class";
      graph.edge(Test.class.getName(), SOME_CLASS);
      TestClassDetector td = new TestClassDetector(graph);
      td.run();
      assertTrue(td.isTestClass(SOME_CLASS));
   }

   @Test
   public void classThatIsTransitivelyDependingOnOrgJunitTestIsATestClass() {
      Graph graph = new Graph();

      String SOME_CLASS = "some.Class";
      String SOME_OTHER_CLASS = "some.other.Class";
      graph.edge(Test.class.getName(), SOME_CLASS);
      graph.edge(SOME_CLASS, SOME_OTHER_CLASS);
      TestClassDetector td = new TestClassDetector(graph);
      td.run();
      assertTrue(td.isTestClass(SOME_OTHER_CLASS));
   }
}
