package com.example.copy;

import java.lang.reflect.Method;
import java.util.Arrays;


public class CustomLoaderDriver {
    public static void main(String[] args) throws Exception {
        String progClass = "com.example.InterfaceExampleImpl";

        CustomClassLoader customLoader = new CustomClassLoader(CustomLoaderDriver.class.getClassLoader());
        Class<?> clas = customLoader.loadClass(progClass);
        Object instanceFromV1 = Arrays.stream(clas.getDeclaredConstructors()).findFirst().get().newInstance();
        Method methodObject1 = Arrays.stream(instanceFromV1.getClass().getMethods())
                .filter(clazz -> clazz.getName().equalsIgnoreCase("doSomething"))
                .findFirst()
                .get();

        // Invoke the method on the instance (instanceFromV1)
        methodObject1.invoke(instanceFromV1);
    }
}