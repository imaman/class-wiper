package com.blogspot.javadots.classwiper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class DependecnyExtractor {

   private final class ClassConstantsReader extends ClassReader {
      private ClassConstantsReader(String name) throws IOException {
         super(name);
      }

      private ClassConstantsReader(InputStream is) throws IOException {
         super(is);
      }
      
      @Override
      public String readClass(int index, char[] buf) {
         String result = super.readClass(index, buf);
         dependencies.add(result);
         return result;
      }
   }

   public String actualClassName;

   private final class NameAndSuperClassExtractor implements ClassVisitor {
      private final class MethodAnnotationExtractor implements MethodVisitor {
         @Override
         public void visitVarInsn(int opcode, int var) {
         }

         @Override
         public void visitTypeInsn(int opcode, String type) {
         }

         @Override
         public void visitTryCatchBlock(Label start, Label end, Label handler,
            String type) {
         }

         @Override
         public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
         }

         @Override
         public AnnotationVisitor visitParameterAnnotation(int parameter,
            String desc, boolean visible) {
            return null;
         }

         @Override
         public void visitMultiANewArrayInsn(String desc, int dims) {
         }

         @Override
         public void visitMethodInsn(int opcode, String owner, String name,
            String desc) {
         }

         @Override
         public void visitMaxs(int maxStack, int maxLocals) {
         }

         @Override
         public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
         }

         @Override
         public void visitLocalVariable(String name, String desc, String signature,
            Label start, Label end, int index) {
         }

         @Override
         public void visitLineNumber(int line, Label start) {
         }

         @Override
         public void visitLdcInsn(Object cst) {
         }

         @Override
         public void visitLabel(Label label) {
         }

         @Override
         public void visitJumpInsn(int opcode, Label label) {
         }

         @Override
         public void visitIntInsn(int opcode, int operand) {
         }

         @Override
         public void visitInsn(int opcode) {
         }

         @Override
         public void visitIincInsn(int var, int increment) {
         }

         @Override
         public void visitFrame(int type, int nLocal, Object[] local, int nStack,
            Object[] stack) {
         }

         @Override
         public void visitFieldInsn(int opcode, String owner, String name, String desc) {
         }

         @Override
         public void visitEnd() {
         }

         @Override
         public void visitCode() {
         }

         @Override
         public void visitAttribute(Attribute attr) {
         }

         @Override
         public AnnotationVisitor visitAnnotationDefault() {
            return null;
         }

         @Override
         public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            String s = fromBinaryName(desc.substring(1));
            s = s.substring(0, s.length() - 1);
            dependencies.add(s);
            return null;
         }
      }

      @Override
      public void visitSource(String source, String debug) {
      }

      @Override
      public void visitOuterClass(String owner, String name, String desc) {
      }

      @Override
      public MethodVisitor visitMethod(int access, String name, String desc,
         String signature, String[] exceptions) {
         return new MethodAnnotationExtractor();
      }

      @Override
      public void visitInnerClass(String name, String outerName, String innerName,
         int access) {
      }

      @Override
      public FieldVisitor visitField(int access, String name, String desc,
         String signature, Object value) {
         return null;
      }

      @Override
      public void visitEnd() {
      }

      @Override
      public void visitAttribute(Attribute attr) {
      }

      @Override
      public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
         dependencies.add(desc);
         return null;
      }

      @Override
      public void visit(int version, int access, String name, String signature,
         String superName, String[] interfaces) {
         dependencies.add(superName);
         actualClassName = name;         
      }            
   }

   private String className;
   private Set<String> dependencies =  new HashSet<String>();
   private InputStream inputStream;

   public DependecnyExtractor(String classToInspect) {
      this.className = classToInspect;
   }
   
   public DependecnyExtractor(Class<?> cls) {
      this.className = cls.getName();
   }

   public DependecnyExtractor(InputStream is) {
      this.inputStream = is;
   }

   public void run() throws IOException {
      ClassReader cr = (inputStream == null) ? new ClassConstantsReader(className)
         : new ClassConstantsReader(inputStream);
      
      ClassVisitor cv = new NameAndSuperClassExtractor();
      cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);  
   }

   public boolean contains(String className) {
      return dependencies.contains(toBinaryName(className));
   }
   
   @Override
   public String toString() {
      return className + " depends on " + dependencies.toString();
   }

   public List<String> getDependencies() {
      List<String> result = new ArrayList<String>();
      for(String s : dependencies) 
         result.add(fromBinaryName(s));
      return result;
   }

   private String toBinaryName(String className) {
      return className.replace('.', '/');
   }

   private String fromBinaryName(String s) {
      return s.replace('/', '.');
   }

   public String getClassName() {
      return fromBinaryName(actualClassName);
   }
}
