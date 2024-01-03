package com.example.copy;

public class CustomClassLoaderMain {

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader = new CustomClassLoader(
                CustomClassLoaderMain.class.getClassLoader());
        Class<?> clazz =
                loader.loadClass("com.example.InterfaceExampleImpl");
        Object instance = clazz.newInstance();
        clazz.getMethod("doSomething").invoke(instance);
    }
}
