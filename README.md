# harmony-spring-demo

## 注解
@Retention: 是用来指定注解的**保留策略**的元注解。保留策略指定了注解的生命周期，即注解可以保留多长时间。

* RetentionPolicy.SOURCE: 这种策略表示注解**仅保留在源代码**中，编译器编译后不包含注解信息。这类注解通常**用于生成文档**。
* RetentionPolicy.CLASS: 这种策略表示注解**被保留在编译后的字节码文件**中，但**不被加载到JVM**中。这类注解通常用于**自定义编译**时的处理。
* RetentionPolicy.RUNTIME: 这种策略表示注解在运行时**也可通过反射访问**。这类注解通常用于配置、注入依赖、实现自定义的运行时逻辑。

@Target: 用于指定注解可以应用（生效）的目标元素类型，即你可以将该注解应用在哪些程序元素上，如类、方法、字段等。

@Target 用于指定注解可以应用（生效）的目标元素类型，即你可以将该注解应用在哪些程序元素上，如类、方法、字段等。

* Java 定义了一系列目标元素类型，包括 **ElementType.TYPE（类）、ElementType.METHOD（方法）、ElementType.FIELD（字段）** 等。
* 可以使用**大括号 {}** 来列出多个目标元素类型，表示该注解可以应用于多个不同的元素。


## 类加载器

### 启动类加载器(Bootstrap Class Loader)

这是最高级别的类加载器，它负责加载Java核心库，如java.lang包中的类。它通常是用本地代码实现的，无法通过Java代码获取引用。

管理 jre/lib

### 扩展类加载器(Extension Class Loader)

扩展类加载器负责加载Java扩展库，通常位于jre/lib/ext目录下的JAR文件。它是启动类加载器的子类加载器。

管理 jie/ext/lib


### 应用程序类加载器(Application Class Loader)

也称为系统类加载器，它负责加载应用程序中的类。大部分用户编写的代码都是由这个类加载器加载的。

管理 target/classes

