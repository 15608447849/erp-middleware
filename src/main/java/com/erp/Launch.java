package com.erp;

import com.erp.servlet.base.WebServerFactory;

/**
 * @Author: leeping
 * @Date: 2019/12/19 22:23
 */

public class Launch {

    public static void main(String[] args) {
        try {
            WebServerFactory.startWebServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
