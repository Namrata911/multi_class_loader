package com.example.copy;



import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Our custom implementation of the ClassLoader.
 * For any of classes from "javablogging" package
 * it will use its {@link CustomClassLoader#getClass()}
 * method to load it from the specific .class file. For any
 * other class it will use the super.loadClass() method
 * from ClassLoader, which will eventually pass the
 * request to the parent.
 */
public class CustomClassLoader extends ClassLoader {

    /**
     * Parent ClassLoader passed to this constructor
     * will be used if this ClassLoader can not resolve a
     * particular class.
     *
     * @param parent Parent ClassLoader
     *               (may be from getClass().getClassLoader())
     */
    String relativeJarPath;

    public CustomClassLoader(ClassLoader parent, String relativeJarPath) {
        super(parent);
        this.relativeJarPath = relativeJarPath;

    }

    /**
     * Loads a given class from .class file just like
     * the default ClassLoader. This method could be
     * changed to load the class over network from some
     * other server or from the database.
     *
     * @param name Full class name
     */
    private Class<?> getClass(String name)
            throws ClassNotFoundException {
        // We are getting a name that looks like
        // javablogging.package.ClassToLoad
        // and we have to convert it into the .class file name
        // like javablogging/package/ClassToLoad.class
        String file = this.relativeJarPath
                + name.replace('.', File.separatorChar)
                + ".class";
        byte[] b = null;
        try {
            // This loads the byte code data from the file
            b = loadClassData(file);
            // defineClass is inherited from the ClassLoader class
            // and converts the byte array into a Class
            Class<?> c = defineClass(name, b, 0, b.length);
            resolveClass(c);
            return c;
        } catch (Exception e) {
//            e.printStackTrace();
            return super.loadClass(name);
        }
    }

    /**
     * Every request for a class passes through this method.
     * If the requested class is in "javablogging" package,
     * it will load it using the
     * {@link CustomClassLoader#getClass()} method.
     * If not, it will use the super.loadClass() method
     * which in turn will pass the request to the parent.
     *
     * @param name Full class name
     */
    @Override
    public Class<?> loadClass(String name)
            throws ClassNotFoundException {
        System.out.println("loading class '" + name + "'");
        Class<?> aClass;
        try {
            if (name.startsWith("com.example.InterfaceExample")) {
                aClass = getClass(name);
                return aClass;
            }
        } catch (Exception ex) {
            System.out.println("NOT FOUDN IN EXTERNAL JARS");
        }
        return super.loadClass(name);
    }

    /**
     * Loads a given file (presumably .class) into a byte array.
     * The file should be accessible as a resource, for example
     * it could be located on the classpath.
     *
     * @param name File name to load
     * @return Byte array read from the file
     * @throws IOException Is thrown when there
     *                     was some problem reading the file
     */
    private byte[] loadClassData(String name) throws IOException {
        // Opening the file
        InputStream stream = Files.newInputStream(Path.of(name));

        int size = stream.available();
        byte[] buff = new byte[size];
        DataInputStream in = new DataInputStream(stream);
        // Reading the binary data
        in.readFully(buff);
        in.close();
        return buff;
    }

    public String getRelativeJarPath() {
        return relativeJarPath;
    }

//    public InputStream getResourceAsStreamMy(String name) {
//        Objects.requireNonNull(name);
//        URL url = getResourceMy(name);
//        try {
//            return url != null ? url.openStream() : null;
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public URL getResourceMy(String name) {
//        Objects.requireNonNull(name);
//        URL url;
//        url = BootLoader.findResource(name);
//        if (url == null) {
//            url = findResource(name);
//        }
//        return url;
//    }


}