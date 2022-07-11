package com.wuxianggujun.robotbase.enums;

/**
 * 常量范围
 *
 * @author 无相孤君
 * @date 2022/07/11
 */
public enum ScopeConst {

    /**
     * 单例
     */
    SINGLETON("singleton"),
    /**
     * 多例
     */
    PROTOTYPE("prototype");

    /**
     * 范围
     */
    private final String scope;

    ScopeConst(String scope) {
        this.scope = scope;
    }
}
