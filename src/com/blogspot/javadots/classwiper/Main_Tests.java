package com.blogspot.javadots.classwiper;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Main_Tests {

   private final class TestingEntry implements InputEntry {
      private String name;

      private TestingEntry(Class<?> cls) {
         name = cls.getName().replace('.', '/') + ".class";
      }

      @Override
      public InputStream inputStream() {
         return getClass().getClassLoader().getResourceAsStream(name);
      }

      @Override
      public String fileName() {
         return name;
      }
   }

   @Test
   public void shouldDeleteOnlyTestClasses() throws IOException {
      final List<String> filesToDelete = new ArrayList<String>();
      
      WiperAction action = new WiperAction() {
         @Override
         public void processFile(String fileName) {
            filesToDelete.add(fileName);
         }         
      };

      ArrayList<InputEntry> entries = new ArrayList<InputEntry>();
      entries.add(new TestingEntry(Graph_Tests.class));
      entries.add(new TestingEntry(Graph.class));
         
      Main main = new Main(entries.iterator(), action);
      
      main.run();
      
      if(filesToDelete.contains(Graph_Tests.class.getName().replace('.', '/') + ".class"))
         return;
      
      fail(filesToDelete.toString());
   }
}
