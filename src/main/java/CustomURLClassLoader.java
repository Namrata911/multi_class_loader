import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class CustomURLClassLoader extends URLClassLoader {

    public CustomURLClassLoader(URL[] urls, ClassLoader parent) {
        super(urls,parent);
    }

    //    @Override
//    protected Class<?> findClass(String name) throws ClassNotFoundException {
//        try {
//            // Attempt to find and load the class from the provided URLs
//            return super.findClass(name);
//        } catch (ClassNotFoundException e) {
//            // If the class is not found in the URLs, throw ClassNotFoundException
//            throw new ClassNotFoundException(name);
//        }
//    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {

            if (shouldProhibitDelegation(name)) {
                return getClass(name);
            }

//            System.out.println("Prohibit class from delegating: " + name);
//            Class<?> aClass = findClass(name);
//            if (aClass == null) {
                return super.loadClass(name);
//            }
//            return aClass;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // Attempt to find and load the class from the provided URLs
            byte[] classBytes = loadClassBytes(name);
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    private byte[] loadClassBytes(String className) throws Exception {
        // Replace this logic with your actual class loading logic
        // You may read class bytes from the JAR file or any other source
        // For simplicity, I'm using ClassLoader's getResourceAsStream

        String classFileName = className.replace('.', '/') + ".class";
        try (var inputStream = getResourceAsStream(classFileName)) {
            if (inputStream == null) {
                throw new ClassNotFoundException(className);
            }
            return inputStream.readAllBytes();
        }
    }

    private boolean shouldProhibitDelegation(String className) {
        // Replace this condition with the criteria for classes to be prohibited from delegation
        return className.startsWith("com.example.InterfaceExample");
    }

    private Class getClass(String name) throws ClassNotFoundException {
        String file = name.replace('.', File.separatorChar) + ".class";
        byte[] b = null;
        try {
            // This loads the byte code data from the file
            b = loadClassFileData(file);
            // defineClass is inherited from the ClassLoader class, it converts byte array into a Class.
            Class c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] loadClassFileData(String name) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                name);
        int size = stream.available();
        byte buff[] = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        in.readFully(buff);
        in.close();
        return buff;
    }

    public static void main(String[] args) {
        try {
            // Replace "path/to/your/custom.jar" with the actual path to your JAR file
            String jarFilePath = "file:D:\\Learning\\Clean Design\\mentor\\multi_class_loader\\external_jars\\CustomJar1-1.0-SNAPSHOT.jar";
            URL[] jarUrl = new URL[]{new File(jarFilePath).toURI().toURL()};
            CustomURLClassLoader customClassLoader = new CustomURLClassLoader(jarUrl, CustomURLClassLoader.getPlatformClassLoader());

            // Replace "com.example.CustomClass" with the fully qualified name of your class
            Class<?> customClass = customClassLoader.loadClass("com.example.InterfaceExampleImpl");

            // Instantiate the custom class
            Object instance = customClass.getDeclaredConstructor().newInstance();

            // Perform operations with the instantiated object
            Method methodObject1 = Arrays.stream(instance.getClass().getMethods())
                    .filter(clazz -> clazz.getName().equalsIgnoreCase("doSomething"))
                    .findFirst()
                    .get();

            // Invoke the method on the instance (instanceFromV1)
            methodObject1.invoke(instance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
