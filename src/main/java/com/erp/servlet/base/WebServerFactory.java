package com.erp.servlet.base;

import com.erp.Launch;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import properties.abs.ApplicationPropertiesBase;
import properties.annotations.PropertiesFilePath;
import properties.annotations.PropertiesName;
import utils.Log4j;
import utils.XmlReadUtils;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;

import static io.undertow.servlet.Servlets.servlet;

/**
 * @Author: leeping
 * @Date: 2019/12/6 14:05
 */
@PropertiesFilePath("/app.properties")
public class WebServerFactory {

    private static String host = localIP();

    private static String localIP(){
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PropertiesName(("local.port"))
    private static int port;

    @PropertiesName(("local.domain"))
    private static String domain;

    static {
        ApplicationPropertiesBase.initStaticFields(WebServerFactory.class);
    }

    //当前web服务对象
    private static Undertow webObject;

    /************************web 服务*******************/
    public static void startWebServer() throws Exception{
        if (webObject!=null) return;

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(Launch.class.getClassLoader())
                .setContextPath("/")
                .setDeploymentName("erp-web-server.war");
        createServlet(servletBuilder);
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        HttpHandler httpHandler = manager.start();
        //路径默认处理程序
        PathHandler pathHandler = Handlers.path(httpHandler);

        webObject =  Undertow.builder()
                .addHttpListener(port, host, pathHandler)
                .build();
        webObject .start();
    }

    private static void createServlet(DeploymentInfo servletBuilder) {

        final String classPrev = "com.erp.servlet.imp.";
        try(InputStream is = ApplicationPropertiesBase.readPathProperties(WebServerFactory.class,"/config.xml")){
            List<XmlReadUtils.XmlType> list = XmlReadUtils.readXml(is,"servlet");
            for (int i = 0;i<list.size();i+=2){
                XmlReadUtils.XmlType type1 = list.get(i);
                XmlReadUtils.XmlType type2 = list.get(i+1);
                String name = type1.getValue();
                String classPath = classPrev+type2.getValue();
                try {
                    Class<? extends WebServletAbs> clazz = (Class<? extends WebServletAbs>) Class.forName(classPath);
                    servletBuilder.addServlet(servlet(name, clazz).addMapping(name));
               } catch (Exception e) {
                    Log4j.error(e);
                }
            }
        }catch (Exception e){
            Log4j.error(e);
        }

    }

    public static void stopWebServer(){
        if (webObject!=null) webObject.stop();
    }

}
