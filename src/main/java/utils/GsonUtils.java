package utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Leeping on 2018/6/27.
 * email: 793065165@qq.com
 */

public class GsonUtils {

    private static Gson newGson(){
        return new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING)
//                .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> {
////                    System.out.println("-----------double-------GSON: "+ src+" - "+src.longValue()+" ");
////                    if (src == src.longValue())
////                        return new JsonPrimitive(src.longValue());
////                    return new JsonPrimitive(src);
//                    return new JsonPrimitive(src.longValue()+"");
//                })
//                .registerTypeAdapter(Integer.class,(JsonSerializer<Integer>) (src, typeOfSrc, context) -> {
//                    System.out.println("-------------int-----GSON: "+ src);
//            if (src == src.longValue())
//                return new JsonPrimitive(src.longValue());
//            return new JsonPrimitive(src);
//                    return new JsonPrimitive(src+"");
//        })
                .create();
    }




    /**
     * json to javabean
     *new TypeToken<List<xxx>>(){}.getType()
     * @param json
     */
    public static <T> T jsonToJavaBean(String json,Type type) {
        try {
            if (json==null || json.length()==0) return null;
            return newGson().fromJson(json, type);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * javabean to json
     * @param object
     * @return
     */
    public static String javaBeanToJson(Object object){
        return newGson().toJson(object);
    }
    /**
     * json to javabean
     *
     * @param json
     */
    public static <T> T jsonToJavaBean(String json,Class<T> cls) {
        try {
            if (json==null || json.length()==0) return null;
            return newGson().fromJson(json, cls);//对于javabean直接给出class实例
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T,D> HashMap<T,D> string2Map(String json){
        try {
            if (StringUtils.isEmpty(json)) return null;
            return jsonToJavaBean(json, new TypeToken<HashMap<T,D>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> json2List(String json, Class<T> clazz){
        List<T> list = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(json)) return null;
            Gson gson = newGson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement element : array) {
                list.add(gson.fromJson(element, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private final static Gson builder =  new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) -> new JsonPrimitive(src.longValue()+""))
            .registerTypeAdapter(Integer.class,(JsonSerializer<Integer>) (src, typeOfSrc, context) -> new JsonPrimitive(src+""))
            .create();

    /** long -> string 的json转换, 建议使用 jsonToJavaBean */
    public static String toJson(Object o){
        return builder.toJson(o);
    }

    public static <T> List<T> toList(String json, Class<T> clazz){
        if (StringUtils.isEmpty(json)) return null;
        List<T> list = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement element : array) {
                list.add(builder.fromJson(element, clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
