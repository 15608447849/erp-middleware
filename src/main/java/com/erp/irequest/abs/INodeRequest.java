package com.erp.irequest.abs;

/**
 * @Author: leeping
 * @Date: 2019/12/23 12:31
 */
public interface INodeRequest {
    /**
     * 同步执行
     * return new ICENodeServerFactory.ICENoteResult();
     * */
    ICENodeServerFactory.ICENoteResult execute(String param);

    /** 异步执行
     * ICENodeServerFactory.NodeResult result = ICENodeServerFactory.invoke(callback,"中间件测试回调");
     * */
    void executeAsync(String param, String callback) throws Exception;
}
