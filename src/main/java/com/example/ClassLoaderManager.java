package com.example;

import com.example.copy.CustomClassLoader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassLoaderManager {
    // create class loader on update of jar location
    Map<String,ClassLoader> jarPathToClassLoaderMap = new HashMap<>();

    public void addOrUpdateTCJarPath(String tcVersion, String newJarPath) {
//        ClassLoader classLoader = jarPathToClassLoaderMap.get(tcVersion);
//        if (classLoader == null) {
//            // If class loader does not exist for TC version, create a new one
//            classLoader = new CustomClassLoader(newJarPath);
//            jarPathToClassLoaderMap.put(tcVersion, classLoader);
//        } else {
//            // If class loader already exists for TC version, update its JAR path
//            classLoader.updateJarPath(newJarPath);
       // }
    }


}
