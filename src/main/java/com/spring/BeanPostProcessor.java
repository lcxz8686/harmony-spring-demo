package com.spring;

/**
 *  BeanPostProcessor: 后置处理器
 *  连接Spring IOC和AOP的桥梁
 */
public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("postProcessBeforeInitialization");
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("postProcessAfterInitialization");
        return bean;
    }
}
