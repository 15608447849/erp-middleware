package com.erp.servlet.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import utils.GsonUtils;
import utils.StringUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/5/31.
 */
public class WebServletAbs extends javax.servlet.http.HttpServlet {


    private final String PARAM_SEPARATOR = ";";

    //跨域
    protected void filter(HttpServletRequest req,HttpServletResponse resp) {
//        http://www.ruanyifeng.com/blog/2016/04/cors.html
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers","x-requested-with"); // 允许x-requested-with请求头
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);
    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        filter(req,resp);
        super.doOptions(req, resp);
    }

    protected ArrayList<String> filterData(String data) {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(data)) { // 不为空
                data = URLDecoder.decode(data, "UTF-8"); // url解码
                if (data.contains(PARAM_SEPARATOR)) {
                    String[] pathArray = data.split(PARAM_SEPARATOR);
                    Collections.addAll(dataList, pathArray);
                } else {
                    dataList.add(data);// 如果只有一个
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    protected ArrayList<String> filterJsonData(String json){
        try {
            if (!StringUtils.isEmpty(json)) { // 不为空
                json = URLDecoder.decode(json, "UTF-8"); // url解码
                return new Gson().fromJson(json,new TypeToken<ArrayList<String>>(){}.getType());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取文本
    protected String getParamText(HttpServletRequest request,String key) {
        if (request == null || key == null || request.getParameter(key)==null) return null;
        try {
            String parameter = request.getParameter(key);
            return URLDecoder.decode(parameter,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    protected void writeString(HttpServletResponse resp, String str) {
        try (PrintWriter out = resp.getWriter()) {
            out.write(str);
            out.flush();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeString(HttpServletResponse resp, Object object) {
        writeString(resp, GsonUtils.javaBeanToJson(object));
    }


}
