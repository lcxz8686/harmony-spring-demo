package com.spring;

/**
 * BeanDefinition:
 *      用于保存Bean的相关信息
 */
public class BeanDefinition {
    private Class type;     // 类型
    private String scope;   // 作用域
    private boolean isLazy; // 是否是lazy加载

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }
}