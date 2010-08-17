package com.blogspot.javadots.classwiper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

   private Iterator<InputEntry> inputs;
   private WiperAction action;
   private Graph graph = new Graph();
   private TestClassDetector tcd = new TestClassDetector(graph);
   private Map<String,String> fileNameFromClassName = new HashMap<String,String>();

   public Main(Iterator<InputEntry> inputs, WiperAction action) {
      this.inputs = inputs;
      this.action = action;
   }

   public void run() throws IOException {
      buildGraph();     
      tcd.run();      
      applyAction();
   }

   private void applyAction() {
      for(String className : graph.vertices()) {
         if(!tcd.isTestClass(className))
            continue;
         
         String fileName = fileNameOf(className);
         action.processFile(fileName);
      }
   }

   private void buildGraph() throws IOException {
      while(inputs.hasNext()) {
         
         InputEntry e = inputs.next();
         
         InputStream is = e.inputStream();         
         DependecnyExtractor de = new DependecnyExtractor(is);
         de.run();
         is.close();
         
         String className = de.getClassName();
         registerFileName(className,  e.fileName());
         for(String dep : de.getDependencies()) {
            graph.edge(dep, className);
         }         
      }
   }

   private String fileNameOf(String className) {
      return fileNameFromClassName.get(className);
   }

   private void registerFileName(String className, String fileName) {
      fileNameFromClassName.put(className, fileName);
   }
   
   public static void main(String[] args) throws IOException {
      Options options = parseCommandLine(Arrays.asList(args));
      List<InputEntry> entries = new ArrayList<InputEntry>();
      for(String curr : options.files)
         scan(new File(curr).getAbsoluteFile(), entries);
      
      Deleter deleter = new Deleter();
      Main inst =  new Main(entries.iterator(), deleter);
      
      inst.run();
      
      if(options.quiet)
         return;
      
      System.out.println("Deleted " + plural(deleter.count, "file") 
         + " out of " + plural(entries.size(), ".class file"));
   }

   private static String plural(int n, String noun) {
      return n + " " + noun + (n != 1 ? "s" : "");
   }
   
   private static final class Deleter implements WiperAction {
      public int count;

      @Override
      public void processFile(String fileName) {
         boolean b = new File(fileName).delete();
         if(!b)
            throw new RuntimeException("I could not not delete " + fileName);
         else
            ++count;
      }
   }

   public static class Options {
      public final List<String> files = new ArrayList<String>();
      public boolean quiet = false;
   }

   private static Options parseCommandLine(List<String> args) {
      Options result = new Options();
      for(Iterator<String> iter = args.iterator(); iter.hasNext(); ) {
         String curr = iter.next();
         if(curr.equals("-h")) 
            help();
         else if(curr.equals("-q"))
            result.quiet = true;
         else
            result.files.add(curr);
      }
      
      if(result.files.size() == 0)
         help();
      
      
      return result;
   }

   private static void help() {
      System.err.println("ClassWiper: Delete JUnit-related .class files");
      System.err.println("");
      System.err.println("Usage: java -jar class-wiper.jar <dir1> <dir2> ... ");
      System.err.println();
      System.err.println("Look for classes (under the specified directories) that either mention ");
      System.err.println("JUnit's @Test annotation, or that rely on such classes. corresponding ");
      System.err.println(".class files will be discarded.");      
      System.err.println();
      System.err.println("Options");
      System.err.println("  -h Show this help message");
      System.err.println("  -q Quiet mode. Do not print summary");
      System.exit(-1);
   }

   private static void scan(final File f, List<InputEntry> entries) {
      if(f.isDirectory()) {
         for(File child : f.listFiles())
            scan(child, entries);
         return;
      }
      
      if(!isClassFile(f))
         return;
      
      entries.add(new InputEntry() {
         
         @Override
         public InputStream inputStream() {
            try {
               return new FileInputStream(f);
            } catch (FileNotFoundException e) {
               throw new RuntimeException(e);
            }
         }
         
         @Override
         public String fileName() {
            return f.getAbsolutePath();
         }
      });
   }

   private static boolean isClassFile(final File f) {
      return f.getName().endsWith(".class");
   }

}
