package com.erp.irequest.abs;

import com.onek.client.IceClient;
import properties.abs.ApplicationPropertiesBase;
import properties.annotations.PropertiesFilePath;
import properties.annotations.PropertiesName;
import threadpool.IOThreadPool;
import utils.GsonUtils;
import utils.Log4j;
import utils.StringUtils;
import utils.XmlReadUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: leeping
 * @Date: 2019/12/21 21:02
 *  客户端工具
 */
@PropertiesFilePath("/app.properties")
public class ICENodeServerFactory {
    @PropertiesName("server.instance")
    private static String iceGridTag ;
    @PropertiesName("server.address")
    private static String iceRegAddress;
    @PropertiesName("server.args")
    private static String iceArgs;
    @PropertiesName("server.token")
    private static String token;

    private static final IOThreadPool pool = new IOThreadPool();
    private static final HashMap<String,Class<? extends INodeRequest>> executeMap = new HashMap<>();

    static {
        ApplicationPropertiesBase.initStaticFields(ICENodeServerFactory.class);
        createRequestExecute();
    }

    private static void createRequestExecute(){
        final String classPrev = "com.erp.irequest.imp.";
        try(InputStream is = ApplicationPropertiesBase.readPathProperties(ICENodeServerFactory.class,"/config.xml")){
            List<XmlReadUtils.XmlType> list = XmlReadUtils.readXml(is,"execute");
            for (int i = 0;i<list.size();i+=2){
                XmlReadUtils.XmlType type1 = list.get(i);
                XmlReadUtils.XmlType type2 = list.get(i+1);
                String name = type1.getValue();
                String classPath = classPrev+type2.getValue();
                try {
                    Class<? extends INodeRequest> clazz = (Class<? extends INodeRequest>) Class.forName(classPath);
                    executeMap.put(name, clazz);
                } catch (Exception e) {
                    Log4j.error(e);
                }
            }
        }catch (Exception e){
            Log4j.error(e);
        }
    }

    public static class ICENoteResult{
        private final int code;
        private final String message;
        private final Object data;

        private ICENoteResult(int code, String message, Object data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    public static ICENoteResult ERROR(Throwable e){
        Log4j.error(e);
        return new ICENoteResult(0, StringUtils.printExceptInfo(e),null);
    }

    public static ICENoteResult SUCCESS(String message,Object data){ return new ICENoteResult(200, message,data); }

    public static ICENoteResult FAIL(String message){
        return new ICENoteResult(-1, message,null);
    }

    /* 创建客户端 */
    private static IceClient createClient(String serverName_module_method) {
        IceClient client = new IceClient(iceGridTag,iceRegAddress,iceArgs)
                .startCommunication();
        String[] arr = serverName_module_method.split("@");
        client.setServerAndRequest(token,arr[0],arr[1],arr[2]);
        return client;
    }

    /**
     服务名@模块名@方法名
     数组参数
     返回调用结果
     */
    public static ICENoteResult invoke(String serverName_module_method,String... param){
        IceClient client = null;
        try {
            client = createClient(serverName_module_method);
            String json = client.setArrayParams(param).execute();
            Log4j.info("服务\t"+serverName_module_method+"\n\t参数\t"+ Arrays.toString(param)+"\t\n返回信息\t"+json);
            return GsonUtils.jsonToJavaBean(json,ICENoteResult.class);
        } catch (Exception e) {
            return ERROR(e);
        }finally {
            if (client!=null) client.stopCommunication();
        }
    }

    /**
     服务名@模块名@方法名
     json对象
     返回调用结果
     */
    public static ICENoteResult invoke(String serverName_module_method,Object jsonObject){
        IceClient client = null;
        try {
            client = createClient(serverName_module_method);
            String jsonStr = GsonUtils.javaBeanToJson(jsonObject);
            String json = client.settingParam(jsonStr).execute();
            Log4j.info("\n请求节点服务\t"+serverName_module_method+"\n\t参数\t"+jsonStr+"\t\n返回信息\t"+json);
            return GsonUtils.jsonToJavaBean(json,ICENoteResult.class);
        } catch (Exception e) {
            return ERROR(e);
        }finally {
            if (client!=null) client.stopCommunication();
        }
    }


    /* 收到节点服务的请求,分发 */
    public static ICENodeServerFactory.ICENoteResult request(String type, String param, String callback){
        Log4j.info("收到请求: " + type+" , "+param+" "+callback);
        Class<? extends INodeRequest> clazz = executeMap.get(type);
        if (clazz==null) return ICENodeServerFactory.FAIL("没有可执行的类型:"+type);
        try {
            INodeRequest e = clazz.newInstance();

            if (callback!=null){
                //异步执行
                pool.post(()->{
                    try {
                        e.executeAsync(param,callback);
                    } catch (Exception ex) {
                        Log4j.error(ex);
                        ICENodeServerFactory.invoke(callback,"执行异常", StringUtils.printExceptInfo(ex));
                    }
                });
                return ICENodeServerFactory.SUCCESS("异步调用成功",null);
            }else{
                //同步执行
                return e.execute(param);
            }
        } catch (Exception e) {
            return ICENodeServerFactory.ERROR(e);
        }

    }
}
