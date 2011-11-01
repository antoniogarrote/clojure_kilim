package clojure.kilim;

import clojure.asm.*;
import clojure.asm.commons.Method;
import clojure.lang.*;
import clojure.lang.Compiler;
import kilim.analysis.ClassInfo;
import kilim.analysis.ClassWeaver;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;




/**
 * User: antonio
 * Date: 07/10/2011
 * Time: 14:38
 */
public class KilimTransformerAgent implements ClassFileTransformer {
    public static byte[] lastClassLoadedBytes;
    public static String lastClassloadedName;
    public static String nextFunctionToWeaveHint;
    public static String kindOfTransformation;
    public static boolean shouldDebug = false;

    public static void setNextFunctionToWeave(String fnNameHint) {
        if(shouldDebug)
            System.out.println("*** next function to weave: "+fnNameHint);

        nextFunctionToWeaveHint = fnNameHint;
    }


    public static byte[] getLastClassLoadedBytes() {
        return lastClassLoadedBytes;
    }

    public static String getLastClassLoadedName() {
        return lastClassloadedName;
    }

    public static void setKindOfTransformation(String kind) {
        if(shouldDebug)
            System.out.println("*** kind of transformation to apply: "+kind);

        kindOfTransformation = kind;
    }

    public static void setDebugging(boolean debugging) {
        shouldDebug = debugging;
    }

    public KilimTransformerAgent() {
        super();
    }

    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        try {
            if(KilimTransformerAgent.nextFunctionToWeaveHint != null &&
               isFn(bytes) &&
               s.indexOf(KilimTransformerAgent.nextFunctionToWeaveHint)!=-1) {

                if(KilimTransformerAgent.shouldDebug)
                    System.out.println("*** found matching function to transform: "+s);

                KilimTransformerAgent.lastClassLoadedBytes = bytes;
                KilimTransformerAgent.lastClassloadedName = s;

                return transformLastFunction();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    protected boolean isFn(byte[] bytes) {
        IFnDetector detector = new IFnDetector();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(detector,0);

        return detector.isFn;
    }


    public byte[] transformLastFunction() throws Exception {
        try {
           Var.pushThreadBindings(RT.map(Compiler.LOADER, RT.makeClassLoader()));
            if(KilimTransformerAgent.kindOfTransformation.equals("task")) {
                return ClojureFnAdapter.makeTask(getLastClassLoadedName(), getLastClassLoadedBytes());
            } else {
                return ClojureFnAdapter.makeGenerator(getLastClassLoadedName(), getLastClassLoadedBytes());
            }

        } finally {
            Var.popThreadBindings();
        }
    }

    /**
     * User: antonio
     * Date: 06/10/2011
     * Time: 14:06
     */
    public static class ClojureFnAdapter extends ClassAdapter {

        // Function that must be adapted
        private String fnClassName;
        private Type fnType;
        private String kind;

        /**
         * Constructs a new {@link clojure.asm.ClassAdapter} object.
         *
         */
        public ClojureFnAdapter(final ClassVisitor cv, String fnClassName, String kind) {
            super(cv);
            this.fnClassName = fnClassName;
            //this.fnType = Type.getType(fnClassName);
            this.kind = kind;
        }


        /**
         * Things to change
         * - super class must be kilim.Task
         * - must implement IFn
         */
        public void visit(
                final int version,
                final int access,
                final String name,
                final String signature,
                final String superName,
                final String[] interfaces){

            if(kind.equals("task")) {
                cv.visit(version, access, name, signature, Type.getType(ATaskFunction.class).getInternalName(), interfaces);
            } else {
                cv.visit(version, access, name, signature, Type.getType(AGeneratorFunction.class).getInternalName(), interfaces);
            }
        }

        /**
         * Things to change
         * - if it is an invoke method, add the Pausable exception
         */
        public MethodVisitor visitMethod(
                final int access,
                final String name,
                final String desc,
                final String signature,
                final String[] exceptions){
            if(name.equals("invoke")) {
                String[] pausableException = { "kilim/Pausable" };
                MethodVisitor mv = null;
                if(kind.equals("task")) {
                    mv =  cv.visitMethod(access, "invokeTask", desc, signature, pausableException);
                } else {
                    mv =  cv.visitMethod(access, "generate", desc, signature, pausableException);
                }
                // adapts cod eof the method:
                //  - Reflection
                //  - invoke/invokeTask
                return new TaskMethodAdapter(mv, fnClassName.split("Pusable")[0],fnClassName);
            } else if(name.equals("<init>")) {
                MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
                return new ChangeSuperCallConstructor(mv,kind);
            } else {
                return cv.visitMethod(access, name, desc, signature, exceptions);
            }
        }


        /**
         * Things to change
         * - Add execute method
         */
        public void visitEnd(){
            String[] pausableException = { "kilim/Pausable" };
            MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC,
                                              "execute",
                                              "()V",
                                              null,
                                              pausableException);


            mv.visitCode();

            // load this in the operand stack
            mv.visitVarInsn(Opcodes.ALOAD, 0);

            // invoke 'invoke' from the IFn interface
            if(kind.equals("task")) {
                Type[] args = new Type[0];
                Type owner = Type.getType(ITaskFn.class);
                Method method = new Method("invokeTask",
                                           Type.getType(Object.class),
                                           args);
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                                   owner.getInternalName(),
                                   method.getName(),
                                   method.getDescriptor());

                mv.visitInsn(Opcodes.POP);
            } else {
                Type[] args = new Type[0];
                Type owner = Type.getType(AGeneratorFn.class);
                Method method = new Method("runGenerator",
                                           Type.VOID_TYPE,
                                           args);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                                   owner.getInternalName(),
                                   method.getName(),
                                   method.getDescriptor());
            }

            // return void
            mv.visitInsn(Opcodes.RETURN);

            // end method visitor
            mv.visitMaxs(0,0);
            mv.visitEnd();

            cv.visitEnd();
        }

        public static byte[] makeTask(String className, byte[] bytecode) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
            DynamicClassLoader loader = (DynamicClassLoader) clojure.lang.Compiler.LOADER.deref();

            ClassReader reader = new ClassReader(bytecode);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClojureFnAdapter adapter = new ClojureFnAdapter(writer, className, "task");
            reader.accept(adapter,0);

            byte[] bytes = writer.toByteArray();

            try {
                ClassWeaver weaver = new ClassWeaver(bytes);
                for(ClassInfo info : weaver.getClassInfos()) {
                    String pname = info.className.replace("/",".");
                    if(info.className.equals(className)) {
                        if(KilimTransformerAgent.shouldDebug)
                            System.out.println("*** returning weaved class "+className);
                        bytes = info.bytes;
                    } else {
                        if(KilimTransformerAgent.shouldDebug)
                            System.out.print("*** loading auxiliary weaved class "+pname);
                        loader.defineClass(pname,info.bytes,null);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return bytes;
        }

        public static byte[] makeGenerator(String className, byte[] bytecode) {
            DynamicClassLoader loader = (DynamicClassLoader) clojure.lang.Compiler.LOADER.deref();

            ClassReader reader = new ClassReader(bytecode);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClojureFnAdapter adapter = new ClojureFnAdapter(writer, className, "generator");
            reader.accept(adapter,0);

            byte[] bytes = writer.toByteArray();

            try {
                ClassWeaver weaver = new ClassWeaver(bytes);
                for(ClassInfo info : weaver.getClassInfos()) {
                    String pname = info.className.replace("/",".");
                    if(info.className.equals(className)) {
                        if(KilimTransformerAgent.shouldDebug)
                            System.out.println("*** returning weaved class "+className);
                        bytes = info.bytes;
                    } else {
                        if(KilimTransformerAgent.shouldDebug)
                            System.out.print("*** loading auxiliary weaved class "+pname);
                        loader.defineClass(pname,info.bytes,null);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            return bytes;
        }
    }

    static class IFnDetector implements ClassVisitor{

        boolean isFn = false;

        public void visit(
                final int version,
                final int access,
                final String name,
                final String signature,
                final String superName,
                final String[] interfaces){

            if(superName.equals("clojure/lang/AFunction") &&
               name.indexOf("clojure/") == -1) {
                if(name.indexOf("$eval")!=-1 && name.indexOf("fn__")==-1) {
                    isFn = false;
                } else {
                    isFn = true;
                }
            } else {
                isFn = false;
            }
        }

        public void visitSource(String source, String debug) { }

        public void visitOuterClass(String owner, String name, String desc) { }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return null;
        }

        public void visitAttribute(Attribute attr) { }

        public void visitInnerClass(String name, String outerName, String innerName, int access) { }

        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            return null;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            return null;
        }

        public void visitEnd() { }
    }

    static class ChangeSuperCallConstructor extends MethodAdapter {

        private String kind;

        public ChangeSuperCallConstructor(MethodVisitor methodVisitor, String kind) {
            super(methodVisitor);
            this.kind = kind;
        }

         public void visitMethodInsn(int opcode, String owner, String name, String desc) {
             if(name.equals("<init>")) {
                 if(kind.equals("task")) {
                     this.mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                                             Type.getType(ATaskFunction.class).getInternalName(),
                                             name,
                                             desc);
                 } else {
                     this.mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                                             Type.getType(AGeneratorFunction.class).getInternalName(),
                                             name,
                                             desc);
                 }
             } else {
                 this.mv.visitMethodInsn(opcode,owner,name,desc);
             }
         }
    }

    static class MethodPrinter implements MethodVisitor {

        public AnnotationVisitor visitAnnotationDefault() {
            System.out.println("- visit annotation default");
            return null;
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            System.out.println("- ANNOTATION "+desc+" visible? "+visible);
            return null;
        }

        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            System.out.println("- ANNOTATION PARAMETER "+parameter+" DESC: "+desc+" VISIBLE? "+visible);
            return null;
        }

        public void visitAttribute(Attribute attr) {
            System.out.println("- ATTRIBUTE "+attr);
        }

        public void visitCode() {
            //ignore
        }

        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            System.out.println("- FRAME: "+type+" nLocal:"+nLocal+" local:"+local+" nStack:"+nStack);
        }

        public void visitInsn(int opcode) {
            System.out.println("- INSTRUCTION "+opcode);
        }

        public void visitIntInsn(int opcode, int operand) {
            System.out.println("- INT INSTRUCTION "+opcode+" OPERAND:"+operand);
        }

        public void visitVarInsn(int opcode, int var) {
            System.out.println("- VAR INSTRUCTION "+opcode+" OPERAND:"+var);
        }

        public void visitTypeInsn(int opcode, String desc) {
            System.out.println("- TYPE INSTRUCTION "+opcode+" OPERAND:"+desc);
        }

        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            System.out.println("- FIELD INSTRUCTION "+opcode+" OWNER:"+owner+" name:"+name+" desc:"+desc);
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            System.out.println("- METHOD INSTRUCTION "+opcode+" OWNER:"+owner+" name:"+name+" desc:"+desc);
        }

        public void visitJumpInsn(int opcode, Label label) {
            System.out.println("- JUMP INSTRUCTION "+opcode+" LABEL:"+label);
        }

        public void visitLabel(Label label) {
            System.out.println("- VISIT LABEL :"+label);
        }

        public void visitLdcInsn(Object cst) {
            System.out.println("- LCD INSTRUCTION:"+cst);
        }

        public void visitIincInsn(int var, int increment) {
            System.out.println("- INC INSTRUCTION VAR:"+var+" INCREMENT:"+increment);
        }

        public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
            System.out.println("- TABLE SWITCH INSTRUCTION LABEL:"+dflt);
        }

        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            System.out.println("- LOOPKUP SWITCH INSTRUCTION LABEL:"+dflt);
        }

        public void visitMultiANewArrayInsn(String desc, int dims) {
            System.out.println("- MULTI NEW ARRAY:"+desc+" DIMS:"+dims);
        }

        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            System.out.println("- TRY CATCH BLOCK START:"+start+" END:"+end+" HANDLER:"+handler+" TYPE:"+type);
        }

        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            System.out.println("- LOCAL VAR: "+name+" DESC:"+desc+" SIGNATURE:"+signature);
        }

        public void visitLineNumber(int line, Label start) {
            System.out.println("- LINE NUMBER LINE:"+line+" LABEL:"+start);
        }

        public void visitMaxs(int maxStack, int maxLocals) {
            System.out.println("- VISIT MAX MAXSTACK:"+maxStack+" MAXLOCALS:"+maxLocals);
        }

        public void visitEnd() {
            System.out.println("- VISIT END");
        }
    }

    static class TaskMethodAdapter extends MethodAdapter {

        String oldFnName;
        String newFnName;

        /**
         * Constructs a new {@link clojure.asm.MethodAdapter} object.
         *
         * @param mv the code visitor to which this adapter must delegate calls.
         */
        public TaskMethodAdapter(final MethodVisitor mv, String oldFnName, String newFnName) {
            super(mv);
            this.oldFnName = oldFnName;
            this.newFnName = newFnName;
        }

        public void visitMethodInsn(
            final int opcode,
            final String owner,
            final String name,
            final String desc){

            //System.out.println("-VISIT METHOD OPCODE:"+opcode+" OWNER:"+owner+" NAME:"+name+" DESC:"+desc);
            if(owner.equals("clojure/lang/Reflector")) {
                mv.visitMethodInsn(opcode, "clojure/lang/ReflectorKilim", name, desc);
            } else {
                mv.visitMethodInsn(opcode, owner, name, desc);
            }
        }
    }
}
