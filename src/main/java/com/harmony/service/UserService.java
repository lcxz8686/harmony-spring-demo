package com.harmony.service;

import com.spring.*;

/**
 * InitializingBean、BeanNameAware: 模拟初始化
 */
@Component("UserService") // 给自己当前的bean取名字
@Scope("Singleton") // 指定单例模式
public class UserService implements InitializingBean, BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;

    /**
     * UserService 实现了 BeanNameAware接口，在创建Bean实例的时候会调用该方法
     * @param beanName
     */
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void test() {
        System.out.println(orderService);
    }

    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean#afterPropertiesSet");
    }
}
