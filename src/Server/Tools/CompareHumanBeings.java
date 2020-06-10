/**
 * This simple class was created because the compare method implements instrumentation and allows us to compare
 * 2 classes by their weight in memory
 */

package Server.Tools;

import Server.MyOwnClasses.HumanBeing;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class CompareHumanBeings {
    public static boolean compare(HumanBeing humanBeing1, HumanBeing humanBeing2) {
        Instrumentation instrumentation = new Instrumentation() {
            @Override
            public void addTransformer(ClassFileTransformer transformer, boolean canRetransform) {

            }

            @Override
            public void addTransformer(ClassFileTransformer transformer) {

            }

            @Override
            public boolean removeTransformer(ClassFileTransformer transformer) {
                return false;
            }

            @Override
            public boolean isRetransformClassesSupported() {
                return false;
            }

            @Override
            public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {

            }

            @Override
            public boolean isRedefineClassesSupported() {
                return false;
            }

            @Override
            public void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException {

            }

            @Override
            public boolean isModifiableClass(Class<?> theClass) {
                return false;
            }

            @Override
            public Class[] getAllLoadedClasses() {
                return new Class[0];
            }

            @Override
            public Class[] getInitiatedClasses(ClassLoader loader) {
                return new Class[0];
            }

            @Override
            public long getObjectSize(Object objectToSize) {
                return 0;
            }

            @Override
            public void appendToBootstrapClassLoaderSearch(JarFile jarfile) {

            }

            @Override
            public void appendToSystemClassLoaderSearch(JarFile jarfile) {

            }

            @Override
            public boolean isNativeMethodPrefixSupported() {
                return false;
            }

            @Override
            public void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix) {

            }
        };
        return instrumentation.getObjectSize(humanBeing1)>instrumentation.getObjectSize(humanBeing2);
    }
}
