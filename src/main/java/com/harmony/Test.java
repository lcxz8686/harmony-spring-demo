package com.harmony;

import com.harmony.service.OrderService;
import com.harmony.service.UserService;
import com.spring.HarmonyApplicationContext;

public class Test {
    public static void main(String[] args) {
        HarmonyApplicationContext ctx = new HarmonyApplicationContext(AppConfig.class);

        UserService userService = (UserService)ctx.getBean("UserService");
        OrderService orderService = (OrderService)ctx.getBean("orderService");

        userService.test();
//        orderService.test();
//
//        System.out.println((UserService)ctx.getBean("UserService"));
//        System.out.println((UserService)ctx.getBean("UserService"));
//        System.out.println("============");
//        System.out.println((OrderService)ctx.getBean("orderService"));
    }
}
