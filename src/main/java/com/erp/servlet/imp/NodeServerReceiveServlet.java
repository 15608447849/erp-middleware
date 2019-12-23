package com.erp.servlet.imp;

import com.erp.servlet.base.WebServletAbs;
import com.erp.irequest.abs.ICENodeServerFactory;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: leeping
 * @Date: 2019/12/6 15:23
 */
public class NodeServerReceiveServlet extends WebServletAbs {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        String type = getParamText(req,"type");
        String param = getParamText(req,"param");
        String callback = getParamText(req,"callback");
        ICENodeServerFactory.ICENoteResult result = ICENodeServerFactory.request(type,param,callback);
        writeString(resp,result);
    }
}
