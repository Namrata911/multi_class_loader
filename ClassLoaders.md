# Class loaders in JVM

- Loading is the process of finding the binary representation of a class or interface type with a particular name and creating a class or interface from that binary representation.

- Java class loading is basically reading the *.class files from a source directory(directory location in the file system or in a network) and loading them in to the JVM/memory. From an Operating System’s perspective, compiled class file stored in the file system is binary. If JVM is to execute the logic written in byte code, it should first read these files in to the memory.
ClassLoaders load Java classes dynamically during the runtime to Java Virtual Machine.

- In Java, each ClassLoader has a predefined location from which it loads the class file. There are three kinds of ClassLoaders in Java:

## Bootstrap ClassLoader
Java classes load by an instance of java.lang.ClassLoader. ClassLoaders are also classes themselves. So how does java.lang.ClassLoader load for the first time?

That's where the Bootstrap ClassLoader comes into the picture.

Bootstrap ClassLoader is the machine code that kickstarts the operation when JVM begins to run.
Its task is to load the first Java ClassLoader. It is not a Java class. Bootstrap ClassLoader loads the rt.jar file and other core libraries in the $JAVA_HOME/jre/lib directory.
Bootstrap ClassLoader is the parent of all ClassLoaders. It doesn’t have any parents.
It's also known as the Primordial ClassLoader.

## Extension ClassLoader
Extension ClassLoader is the child of Bootstrap ClassLoader.

It handles the loading of core Java class extensions to ensure they are available to all programs running on the platform. Extension ClassLoader loads classes from the $JAVA_HOME/lib/ext directory or other directory listed in the java.ext.dirs system property. Its implementation is done by sun.misc.Launcher$ExtClassLoader in JVM.

##System ClassLoader
This ClassLoader is the child of the Extension ClassLoader.

It loads application-level classes into the JVM. System ClassLoader loads class files from the classpath environment variable, -classpath, or -cp command-line options.
System ClassLoader is also known as the Application ClassLoader.
The implementation is done by sun.misc.Launcher$AppClassLoader class.

##How ClassLoader works in Java
ClassLoader is a part of the JRE. When the JVM calls for a class, the ClassLoader attempts to locate the class and load the class's definition into runtime by using the fully qualified class name.

By using java.lang.ClassLoader.loadClass() method the class definition is loaded into the runtime. It loads the class using the fully qualified class name.

This loadClass() method then invokes the findLoadedClass() method. This method determines whether the class is already loaded or not. It is essential to ensure that the class is not loaded repeatedly.

If the class already exists, it delegates the request to the parent ClassLoader in order to start loading the class. If the ClassLoader cannot locate its class in the database, it uses java.net.URLClassLoader.findClass() method to find the classes within the system file.

If the last child ClassLoader cannot find or load the class, it throws java.lang.ClassNotFoundException or java.lang.NoClassDefFoundError.

Imagine that we have an application-specific class HelloWorld.class. The request to load these class files goes to the Application ClassLoader. The class is then delegated to the parent Extension ClassLoader. Additionally, it delegates to the Bootstrap ClassLoader.

Bootstrap will search for that class within rt.jar and, since the class is not present, it fails to load it. Now it requests transfer to Extension ClassLoader which searches for the directory jre/lib/ext, and attempts to find the class.

If the class is found there, the Extension ClassLoader loads the class. Application ClassLoader does not load the class. If the Extension ClassLoader is unable to load the class then the Application ClaasLoader loads it via CLASSPATH within Java.

Remember that Classpath is used for load class files, and PATH is used to find executables such as javac or java commands.

## Principles of Java ClassLoader
Java ClassLoader works upon three main principles: Delegation, transparency, and uniqueness.

- Delegation principle :
It forwards class loading requests to the parent ClassLoader and loads the class only when the parent cannot locate or load the class.

- Visibility principle :
It allows the child class loader to view all classes loaded by the parent ClassLoader. However, the parent loader cannot see the classes loaded by the child class loader.

- Uniqueness principle :
It ensures no duplicate classes are loaded in the ClassLoader. The child ClassLoader must not reload the class already in the parent ClassLoader.

## Custom ClassLoader
The built-in loader for classes is enough for most cases where files are already in the file system. However, in situations where we have to load classes from either the local drive or network, we might need to use custom ClassLoaders for classes.

Use cases of custom ClassLoaders
- Offers modular architecture: It allows to define multiple ClassLoaders, which allows for modular architecture.
- Avoidance of conflicts: clearly defines the boundaries of the class within the ClassLoader.
- Versioning support: Allows different class versions for different modules within the same VM.
- Memory management: Modules that are not used can be eliminated, which unloads the classes utilized by that module, and cleans memory.
- Classloading from any location: Classes can be loaded from any location, such as databases or Networks.
- Runtime reloading modified classes: Allows reloading a class runtime by creating the child ClassLoader of the actual ClassLoader that includes modifications of the classes.

## Multi version class loader -
## URLClassLoader :
This class loader is used to load classes and resources from a search path of URLs referring to both JAR files and directories. Any jar: scheme URL (see JarURLConnection) is assumed to refer to a JAR file. Any file: scheme URL that ends with a '/' is assumed to refer to a directory. Otherwise, the URL is assumed to refer to a JAR file which will be opened as needed.
This class loader supports the loading of classes and resources from the contents of a multi-release JAR file that is referred to by a given URL.

The AccessControlContext of the thread that created the instance of URLClassLoader will be used when subsequently loading classes and resources.

The classes that are loaded are by default granted permission only to access the URLs specified when the URLClassLoader was created.


Loading multiple versions of the same class with `URLClassLoader` involves creating separate instances of the `URLClassLoader` for each version, each configured with a different set of URLs pointing to the locations of the corresponding class versions. This allows each class loader to load classes independently and in isolation from the others.

Here's an elaboration of the process:

### 1. Define Class Versions:

Assume you have two versions of the same class, let's call it `MyClass`, with different implementations:

- Version 1: `com.example.MyClass` (from `version1.jar`)
- Version 2: `com.example.MyClass` (from `version2.jar`)

### 2. Create URLs for Class Versions:

```java
import java.net.URL;

// URL for version 1
URL urlVersion1 = new URL("file:/path/to/version1.jar");

// URL for version 2
URL urlVersion2 = new URL("file:/path/to/version2.jar");
```

### 3. Create `URLClassLoader` Instances:

Create separate instances of `URLClassLoader` for each version:

```java
import java.net.URL;
import java.net.URLClassLoader;

// URLs for different versions of the same class
URL[] urlsVersion1 = new URL[]{urlVersion1};
URL[] urlsVersion2 = new URL[]{urlVersion2};

// Create URLClassLoader instances for each version
URLClassLoader classLoaderVersion1 = new URLClassLoader(urlsVersion1);
URLClassLoader classLoaderVersion2 = new URLClassLoader(urlsVersion2);
```

### 4. Load Classes Using Respective Class Loaders:

Load the classes using their respective class loaders:

```java
// Load version 1 of the class
Class<?> myClassVersion1 = classLoaderVersion1.loadClass("com.example.MyClass");

// Load version 2 of the class
Class<?> myClassVersion2 = classLoaderVersion2.loadClass("com.example.MyClass");
```

### 5. Instantiate and Use Classes:

Instantiate and use the classes separately:

```java
// Instantiate version 1 of the class
Object instanceVersion1 = myClassVersion1.getDeclaredConstructor().newInstance();

// Instantiate version 2 of the class
Object instanceVersion2 = myClassVersion2.getDeclaredConstructor().newInstance();

// Perform operations using the instances of the loaded classes
// ...
```

### 6. Closing the Class Loaders:

It's good practice to close the `URLClassLoader` instances when they are no longer needed, releasing resources associated with them:

```java
// Close the class loaders when done
classLoaderVersion1.close();
classLoaderVersion2.close();
```

By creating separate `URLClassLoader` instances for each version, you achieve class isolation, ensuring that classes from one version do not interfere with classes from another version. This approach is particularly useful when you need to handle multiple versions of the same class dynamically at runtime.