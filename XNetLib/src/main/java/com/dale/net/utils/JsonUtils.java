package com.dale.net.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class JsonUtils {

    static class Json{
        static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(String.class, new StringAdapter())
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .create();
    }

    public static String toJson(Object src) {
        if (src == null) return "";
        return Json.GSON.toJson(src);
    }

    public static Gson getGson(){
        return Json.GSON;
    }

    public static JsonObject toJsonTree(Object data){
        return Json.GSON.toJsonTree(data).getAsJsonObject();
    }


    /**
     * 如果只需要把Object中的某一个类型转成json字符串，可以借助typeOfSrc来完成
     * @param src 实体类
     * @param typeOfSrc For example,
     * to get the type for {@code Collection<Foo>}, you should use:
     * <pre>
     * Type typeOfSrc = new TypeToken<Collection<Foo>>(){}.getType();
     * @return json字符串
     */
    public String toJson(Object src, Type typeOfSrc) {
        return Json.GSON.toJson(src,typeOfSrc);
    }

    public String toJson(JsonElement jsonElement) {
        return Json.GSON.toJson(jsonElement);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        if (json == null || !json.startsWith("{") || classOfT == null) return null;
        try {
            return Json.GSON.fromJson(json, classOfT);
        }catch (Exception e){
            return null;
        }
    }

    public static <T> T fromJson(JsonElement json, Class<T> classOfT) {
        try {
            if (json == null) return null;
            return Json.GSON.fromJson(json, classOfT);
        }catch (Exception e){
            return null;
        }

    }

    /**
     * desc: 返回List类型
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> classOfT) {
        if (json == null || !json.startsWith("[")) return null;
        try {
            return Json.GSON.fromJson(json, new TypeToken<List<T>>(){}.getType());
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 当Json数据不只包含数组还存在其他参数时，如果只需要json中的某一段数据，需要借助TypeToken将期望解析成的数据类型传入到fromJson()方法中.
     * 例如：
     * List<Person> people = gson.fromJson(jsonData, new TypeToken<List<Person>>(){}.getType());
     * @param json json字符串
     * @param type 要解析的json对象的类型
     * @param <T> 返回的对象
     * @return
     */
    public static <T> T fromJson(String json, Type type) {
        try {
            return Json.GSON.fromJson(json, type);
        }catch (Exception e){
            return null;
        }

    }

    public static JsonObject fromJson(String jsonStr) {
        try {
            return new JsonParser().parse(jsonStr).getAsJsonObject();
        }catch (Exception e){
            return null;
        }
    }



    private static class StringAdapter implements JsonSerializer<String>, JsonDeserializer<String> {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json instanceof JsonPrimitive) {
                return json.getAsString();
            } else {
                return json.toString();
            }
        }

        @Override
        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class DoubleDefault0Adapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为double类型,如果后台返回""或者null,则返回0.00
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private static class LongDefault0Adapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为long类型,如果后台返回""或者null,则返回0
                    return 0L;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsLong();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }
}
