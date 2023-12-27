import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class MultiVersionClassLoaderDemo {

    public static void main(String[] args) throws Exception {
        // Specify the paths to the JAR files containing the different versions of classes
        String jarPathV1 = "/Users/namratamaradkar/AppData/IdeaProjects/CustomJar1/target/CustomJar1-1.0-SNAPSHOT.jar";
        String jarPathV2 =  "/Users/namratamaradkar/AppData/IdeaProjects/CustomJar2/target/CustomJar2-1.0-SNAPSHOT.jar";

        // Create custom class loaders for each version
        ClassLoader classLoaderV1 = new URLClassLoader(new URL[]{new File(jarPathV1).toURI().toURL()});
        ClassLoader classLoaderV2 = new URLClassLoader(new URL[]{new File(jarPathV2).toURI().toURL()});

        // Load classes from different versions
        Class<?> classFromV1 = Class.forName("com.example.InterfaceExampleImpl", true, classLoaderV1);
        Class<?> classFromV2 = Class.forName("com.example.InterfaceExampleImpl", true, classLoaderV2);

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


        // Use the instances as needed
        // ...

        System.out.println("Successfully loaded and instantiated classes from different versions.");
    }
}
