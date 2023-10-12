package com.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author harmony
 */
public class HarmonyApplicationContext {

    private Class configClass;

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池
    private Map<String, Object> singletonObjects = new HashMap<>();

    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public HarmonyApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描
        scan(configClass);

        // 创建单例Bean
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("Singleton")) {
                // 单例
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * 创建Bean实例
     * @param beanName
     * @param beanDefinition
     * @return
     */
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        Object instance = null;
        try {
            // 根据构造方法，得到一个对象
            instance = clazz.getConstructor().newInstance();

            // 进行依赖注入
            // 遍历该对象中的属性
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    // 值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查
                    field.setAccessible(true);
                    field.set(instance, getBean(field.getName()));
                }
            }

            if (instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // 初始化后
            for (BeanPostProcessor bProcessor : beanPostProcessorList) {
                instance = bProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            // 初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // 初始化后
            for (BeanPostProcessor bProcessor : beanPostProcessorList) {
                instance = bProcessor.postProcessAfterInitialization(instance, beanName);
            }

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 获取bean实例
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        }

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition.getScope().equals("Singleton")) {
            // 单例
            Object singletonBean = singletonObjects.get(beanName);

            // 在UserService中依赖注入OrderService，假如此时还没有创建OrderService的Bean，从单例池是获取不到的！
            if (singletonBean == null) {
                singletonBean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, singletonBean);
            }
            return singletonBean;
        } else {
            // 原型
            Object prototypeBean = createBean(beanName, beanDefinition);
            return prototypeBean;
        }
    }

    /**
     * scan: 扫描
     * @param configClass
     *   解析传入的类，得到扫描路径，遍历路径下面的每一个class文件
     *   加载每一个class文件，得到每一个class对象
     *   判断该class对象有没有@Compennet注解，解析Bean的名字、Scope注解，得到一个 BeanDefinition
     *   将 BeanDefinition 存储到 BeanDefinitionMap 中
     */
    private void scan(Class configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".", "/");
            System.out.println(path);

            ClassLoader classLoader = HarmonyApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            // 拿到这目录
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace("\\", ".");
                    // com.harmony.service.*
                    System.out.println(absolutePath);

                    try {
                        // 加载类
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        // 找到 @Component注解 -> 说明是一个Bean
                        if (clazz.isAnnotationPresent(Component.class)) {

                            // clazz类是不是实现了 BeanPostProcessor接口 -> 缓存
                            // A.isAssignableFrom(B) -> 一个类(B)是不是继承来自于另一个父类(A)，一个接口(A)是不是实现了另外一个接口(B)，或者两个类相同
                            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                                BeanPostProcessor instance = (BeanPostProcessor)clazz.getConstructor().newInstance();
                                // 加到List里缓存
                                beanPostProcessorList.add(instance);
                            }

                            // Bean的名字
                            Component componentAnnotation = clazz.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();

                            // 假如没有指定beanName的名字
                            if ("".equals((beanName))) {
                                // jdk
                                // 默认根据类的名字生成
                                beanName = Introspector.decapitalize(clazz.getSimpleName());
                            }

                            // 创建一个Bean的定义
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setType(clazz);  // bean的类型

                            // 判断是否是单例Bean
                            if (clazz.isAnnotationPresent(Scope.class)) {
                                Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                String value = scopeAnnotation.value();
                                beanDefinition.setScope(value);
                            } else {
                                // 没有Scope注解,说明是单例的(Spring中，Scope注解默认是单例)
                                beanDefinition.setScope("Singleton");
                            }
                            /**
                             * 每扫描到一个bean，就生成一个 BeanDefinition，再将这个 BeanDefinition 存到 map 里面
                             */
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

}