package com.erp.irequest.imp;

import com.erp.irequest.abs.ICENodeServerFactory;
import com.erp.irequest.abs.INodeRequest;

/**
 * @Author: leeping
 * @Date: 2019/12/23 12:37
 * 测试通讯
 */
public class TestConnect implements INodeRequest {

    @Override
    public ICENodeServerFactory.ICENoteResult execute(String param) {
        return ICENodeServerFactory.SUCCESS("同步调用,通讯正常",null);
    }

    @Override
    public void executeAsync(String param, String callback) throws Exception {
        Thread.sleep(5000);
        ICENodeServerFactory.ICENoteResult r = ICENodeServerFactory.SUCCESS("异步回调,通讯测试",null);
        ICENodeServerFactory.invoke(callback, r) ;
    }

}
