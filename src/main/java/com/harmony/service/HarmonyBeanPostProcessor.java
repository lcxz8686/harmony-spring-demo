package com.harmony.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

/**
 * 假如一个Bean实现了BeanPostProcessor接口，并定义了一个后置处理器-postProcessAfterInitialization
 * 那么，所有的Bean创建的过程中，都会来调用这个方法（可以在指定某些Bean执行，其它的不执行！）
 */
@Component
public class HarmonyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (beanName.equals("UserService")) {
            System.out.println("postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        // UserService 才执行 初始化后 的逻辑 postProcessAfterInitialization()
        if (beanName.equals("UserService")) {
            System.out.println("postProcessAfterInitialization");
        }
        return bean;
    }
}
