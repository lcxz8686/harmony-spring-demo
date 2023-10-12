package com.harmony.service;

import com.spring.*;

@Component("UserService") // 给自己当前的bean取名字
@Scope("Singleton") // 指定单例模式
public class UserService implements InitializingBean, BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean#afterPropertiesSet");
    }
}
