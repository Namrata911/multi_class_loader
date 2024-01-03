import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class MultiVersionClassLoaderDemo {

    public static void main(String[] args) throws Exception {
        // Specify the paths to the JAR files containing the different versions of classes
        String jarPathV1 = "D:\\Learning\\Clean Design\\mentor\\multi_class_loader\\external_jars\\CustomJar1-1.0-SNAPSHOT.jar";
        String jarPathV2 = "D:\\Learning\\Clean Design\\mentor\\multi_class_loader\\external_jars\\CustomJar2-1.0-SNAPSHOT.jar";

        // Create custom class loaders for each version
        ClassLoader classLoaderV1 = new URLClassLoader(new URL[]{new File(jarPathV1).toURI().toURL()}, null);
        ClassLoader classLoaderV2 = new URLClassLoader(new URL[]{new File(jarPathV2).toURI().toURL()}, null);

        // Load classes from different versions
        Class<?> classFromV1 = Class.forName("com.example.InterfaceExample1Impl", true, classLoaderV1);
        Class<?> classFromV2 = Class.forName("com.example.InterfaceExample1Impl", true, classLoaderV2);

        // Instantiate objects from different versions
        Object instanceFromV1 = classFromV1.getDeclaredConstructor().newInstance();
        Object instanceFromV2 = classFromV2.getDeclaredConstructor().newInstance();
        Method methodObject1 = Arrays.stream(instanceFromV1.getClass().getMethods())
                .filter(clazz -> clazz.getName().equalsIgnoreCase("doSomething"))
                .findFirst()
                .get();

        // Invoke the method on the instance (instanceFromV1)
        methodObject1.invoke(instanceFromV1);
        Method methodObject2 = Arrays.stream(instanceFromV2.getClass().getMethods())
                .filter(clazz -> clazz.getName().equalsIgnoreCase("doSomething"))
                .findFirst()
                .get();

        methodObject2.invoke(instanceFromV2);
//        // Replace "com.example.CustomClass" with the fully qualified name of your class
//        CustomURLClassLoader customClassLoader = new CustomURLClassLoader(new URL[]{new File(jarPathV1).toURI().toURL()});
//        Class<?> customClass = customClassLoader.loadClass("com.example.InterfaceExampleImpl");
//        // Instantiate the custom class
//        Object instance = customClass.getDeclaredConstructor().newInstance();


        // Use the instances as needed
        // ...

        System.out.println("Successfully loaded and instantiated classes from different versions.");
    }
}
