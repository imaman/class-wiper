package com.blogspot.javadots.classwiper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.junit.Test;

public class DependecnyExtractor_Tests {

   @Test
   public void arrayListDependsOnList() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor("java.util.ArrayList");
      de.run();
      assertTrue(de.toString(), de.contains("java.util.List"));
   }

   @Test
   public void numberDoesNotDependOnList() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor("java.lang.Number");
      de.run();
      assertFalse(de.contains("java.util.List"));
   }

   @Test
   public void shouldRecognizeSuperClassAsDependency() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor("java.util.ArrayList");
      de.run();
      assertTrue(de.contains("java.util.AbstractList"));
   }

   @Test
   public void shouldRecognizeSuperInterfacesAsDependency() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Number.class);
      de.run();
      assertTrue(de.contains("java.io.Serializable"));
   }

   @Test
   public void shouldInspectApplicationClasses() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Sample.class);
      de.run();
   }

   @Test
   public void shouldRecognizeAllSuperInterfaces() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Sample.class);
      de.run();
      assertTrue(de.contains(I1.class.getName()));
      assertTrue(de.contains(I2.class.getName()));
      assertTrue(de.contains(ActionListener.class.getName()));
   }

   
   public interface I1 { }
   public interface I2 { }
   
   public abstract static class Sample implements I1, I2, ActionListener {  }
   
   @Test
   public void shouldReportDependenciesOfSample() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Sample.class);
      de.run();
      List<String> lst = de.getDependencies();
      assertTrue(lst.toString(), lst.contains(I1.class.getName()));
      assertTrue(lst.toString(), lst.contains(I2.class.getName()));
      assertTrue(lst.toString(), lst.contains(ActionListener.class.getName()));
   }
   
   @Test
   public void shouldReportDependenciesOfNumber() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Number.class);
      de.run();
      List<String> lst = de.getDependencies();
      assertTrue(lst.toString(), lst.contains(Serializable.class.getName()));
   }
   
   @Test
   public void shouldReportClassName() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(Number.class);
      de.run();
      assertEquals(Number.class.getName(), de.getClassName());
   }

   @Test
   public void shouldDetectAnnotations() throws IOException {
      DependecnyExtractor de = new DependecnyExtractor(DependecnyExtractor_Tests.class);
      de.run();
      List<String> lst = de.getDependencies();
      assertTrue("lst=" + lst.toString(), lst.contains(Test.class.getName()));
   }
}
