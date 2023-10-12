package com.harmony.service;

import com.spring.Component;
import com.spring.Scope;

@Component
@Scope("Prototype")
public class OrderService {
    public void test() {
        System.out.println("test");
    }
}
