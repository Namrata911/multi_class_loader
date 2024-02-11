package com.example.copy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomClassLoaderMain {
    private static Map<String, ClassLoader> classLoaderCache = new HashMap<>();
    public static void main(String[] args) throws Exception {
//        String className = "com.example.InterfaceExampleImpl";
//        String path1 = "/Users/namratamaradkar/AppData/IdeaProjects/multiClassLoader/external_jars/c1/";
//        loadClassFromPath(path1,className);
//        String path2 = "/Users/namratamaradkar/AppData/IdeaProjects/multiClassLoader/external_jars/c2/";
//        loadClassFromPath(path2,className);
//        loadClassFromPath(path2,"com.example.InterfaceExample1Impl");
// 'com.example.InterfaceExampleImpl' ,  /Users/namratamaradkar/AppData/IdeaProjects/multiClassLoader/external_jars/c1/
//  'com.example.InterfaceExampleImpl' ,  /Users/namratamaradkar/AppData/IdeaProjects/multiClassLoader/external_jars/c2/
     //   'com.example.InterfaceExample1Impl' ,  /Users/namratamaradkar/AppData/IdeaProjects/multiClassLoader/external_jars/c2/
        boolean continueInput = true;
        Scanner scanner = new Scanner(System.in);
        while (continueInput) {


            // Prompt the user to enter the JAR location
            System.out.print("Enter the JAR location: ");
            String jarLocation = scanner.nextLine();
            System.out.print("Enter the JAR location: ");
            String className = scanner.nextLine();

            // Print the entered JAR location
            System.out.println("Entered JAR location: " + jarLocation);
            loadClassFromPath(jarLocation,className);


            // Ask if the user wants to continue
            System.out.print("Do you want to enter another JAR location? true/false: ");
            boolean input = scanner.nextBoolean();
            continueInput = Boolean.TRUE.equals(input);
            scanner.nextLine();


        }
        // Close the scanner
        scanner.close();

    }


    private static void loadClassFromPath(String path, String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        // Check if the class loader for this path is already in the cache
        ClassLoader loader = getClassLoader(path);

        Class<?> clazz =
                loader.loadClass(className);
        Object instance = clazz.newInstance();
        clazz.getMethod("doSomething").invoke(instance);
    }

    private static ClassLoader getClassLoader(String path) {
        ClassLoader loader = classLoaderCache.get(path);
        if (loader == null) {
            // If class loader doesn't exist, create a new one and cache it
            loader = new CustomClassLoader(
                    CustomClassLoaderMain.class.getClassLoader(), path
            );
            classLoaderCache.put(path, loader);
        }
        return loader;
    }

}
