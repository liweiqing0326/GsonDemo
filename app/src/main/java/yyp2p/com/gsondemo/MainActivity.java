package yyp2p.com.gsondemo;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.btn5)
    Button btn5;
    @BindView(R.id.btn6)
    Button btn6;
    @BindView(R.id.btn7)
    Button btn7;
    @BindView(R.id.btn8)
    Button btn8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                gsonTest1();
                break;
            case R.id.btn2:
                gsonTest2();
                break;
            case R.id.btn3:
                gsonTest3();
                break;
            case R.id.btn4:
                gsonTest4();
                break;
            case R.id.btn5:
                gsonTest5();
                break;
            case R.id.btn6:
                gsonTest6();
                break;
            case R.id.btn7:
                gsonTest7();
                break;
            case R.id.btn8:
                gsonTest8();
                break;
        }
    }

    /**
     * Gson的基本使用，简单的Bean和Json的互相转换，带泛型的List和Json互相转换。
     */
    private void gsonTest1() {
        Student student1 = new Student();
        student1.setId(1);
        student1.setBirthDay(new Date(System.currentTimeMillis()));
        student1.setName("张三");

        Gson gson = new Gson();
        //将简单的Bean转换成Json字符串
        String json = gson.toJson(student1);//返回结果：{"birthDay":"May 30, 2016 11:40:17","id":1,"name":"张三"}
        Log.i("liweiqing", "简单Bean转换成Json：" + json);
        //将Json转换成简单Bean
        Student student = gson.fromJson(json, Student.class);
        Log.i("liweiqing", "Json转换成简单Bean：" + student);

        List<Student> list = new ArrayList<>();
        Student student2 = new Student();
        student2.setId(2);
        student2.setName("李四");
        student2.setBirthDay(new Date());

        Student student3 = new Student();
        student3.setId(3);
        student3.setName("王五");
        student3.setBirthDay(new Date());

        list.add(student1);
        list.add(student2);
        list.add(student3);

        //带泛型的List转换成Json
        String listJson = gson.toJson(list);
        Log.i("liweiqing", "将带泛型的List转换成Json:" + listJson);
        //Json转换成带泛型的List
        List<Student> listFromJson = gson.fromJson(listJson, new TypeToken<List<Student>>() {
        }.getType());
        for (Student stu : listFromJson) {
            Log.i("liweiqing", "将Json转换成List：" + stu);
        }
    }

    /**
     * Gson的一些常用设置
     */
    private void gsonTest2() {
        //注意这里的Gson的构建方式为GsonBuilder,区别于test1中的Gson gson = new Gson();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()//不导出实体中没有用@Expose注解的属性
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .serializeNulls()//对空值也进行序列话输出
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转换成特定格式
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效
                .setPrettyPrinting()//对json结果格式化
                .setVersion(1.0)//有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化。
                //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化。
                //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用。
                .create();

        Student student = new Student();
        student.setId(1);
        student.setName(null);
        student.setBirthDay(new Date());
        //只转换用@Expose注解的属性，并且null值可以序列号转换。
        String json = gson.toJson(student);
        Log.i("liweiqing", "只转换用@Expose注解的属性，并且null值可以序列号转换：" + json);
        //将Json转换成Bean
        Student stu = gson.fromJson(json, Student.class);
        Log.i("liweiqing", "Json转换成Bean：" + stu);
    }


    /**
     * Json串和Map的相互转换
     */
    private void gsonTest3() {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .create();

        Map<Point, String> map = new LinkedHashMap<>();// 使用LinkedHashMap将结果按先进先出顺序排列

        map.put(new Point(1, 1), "a");
        map.put(new Point(3, 4), "b");

        //Map转换成Json串
        String json = gson.toJson(map);//返回结果：[[{"x":1,"y":1},"a"],[{"x":3,"y":4},"b"]]
        Log.i("liweiqing", "map转换成Json：" + json);
        //Json转换成Map
        Map<Point, String> mapFromJson = gson.fromJson(json, new TypeToken<Map<Point, String>>() {
        }.getType());
        for (Point p : mapFromJson.keySet()) {
            Log.i("liweiqing", "Json转换成Map，Key:" + p + ",Value:" + map.get(p));
        }


        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setName("刘老师");
        teacher1.setClassName("初三4班");

        Teacher teacher2 = new Teacher();
        teacher2.setName("张老师");
        teacher2.setClassName("高三6班");
        teacherList.add(teacher1);
        teacherList.add(teacher2);

        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setName("孙悟空");
        student1.setBirthDay(new Date());

        Student student2 = new Student();
        student2.setName("猪八戒");
        student2.setBirthDay(new Date());
        studentList.add(student1);
        studentList.add(student2);

        Map<String, Object> listInMap = new LinkedHashMap<>();
        listInMap.put("老师", teacherList);
        listInMap.put("学生", studentList);

        //将复杂类型的Map转换成Json
        String json1 = gson.toJson(listInMap);
        Log.i("liweiqing", json1);
        //将Json转换成复杂类型的Map
        Map<String, Object> mapFromJson1 = gson.fromJson(json1, new TypeToken<Map<String, Object>>() {
        }.getType());
        for (String key : mapFromJson1.keySet()) {
            if (key.equals("老师")) {
                List<Teacher> list = (List<Teacher>) mapFromJson1.get(key);
                Log.i("liweiqing", "老师的集合转换：" + list);
            } else if (key.equals("学生")) {
                List<Student> list = (List<Student>) mapFromJson1.get(key);
                Log.i("liweiqing", "学生的集合转换：" + list);
            }
        }
    }


    /**
     * JsonPrimitive类：可以看到转义的字符串
     */
    private void gsonTest4() {
        String studentJsonStr = "{\"birthDay\":\"May 30, 2016 11:40:17\",\"id\":1,\"name\":\"张三\"}";
        Log.i("liweiqing", "原生的studentJsonStr串：" + studentJsonStr);//{"birthDay":"May 30, 2016 11:40:17","id":1,"name":"张三"}
        JsonPrimitive jsonPrimitive = new JsonPrimitive(studentJsonStr);
        Log.i("liweiqing", "JsonPrimitive转换后的串：" + jsonPrimitive);//"{\"birthDay\":\"May 30, 2016 11:40:17\",\"id\":1,\"name\":\"张三\"}"
        Log.i("liweiqing", "JsonPrimitive转换后的串toString：" + jsonPrimitive.toString());//"{\"birthDay\":\"May 30, 2016 11:40:17\",\"id\":1,\"name\":\"张三\"}"
        Log.i("liweiqing", "JsonPrimitive转换后的串getAsString：" + jsonPrimitive.getAsString());//{"birthDay":"May 30, 2016 11:40:17","id":1,"name":"张三"}
    }

    /**
     * JsonObject和JsonArray测试
     */
    private void gsonTest5() {
        Gson gson = new GsonBuilder()
                .enableComplexMapKeySerialization()//支持Map的key为复杂对象的形式
                .create();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "张三");
        jsonObject.addProperty("年龄", 15);
        //JsonObject转换成Json串
        String jsonObjectToJson = gson.toJson(jsonObject);//{"name":"张三","年龄":15}
        Log.i("liweiqing", "JsonObjectToJson:" + jsonObjectToJson);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add("张三");
        jsonArray.add(1);
        jsonArray.add(true);
        //JsonArray转换成Json
        String jsonArrayToJson = gson.toJson(jsonArray);//["张三",1,true]
        Log.i("liweiqing", "JsonArrayToJson:" + jsonArrayToJson);

    }

    /**
     * Gson的容错方式（不推荐）
     */
    private void gsonTest6() {
        String jsonStr = "{\"id\":\"\",\"name\":\"张三\"}";
        //方式一：创建Gson的方式
        Gson gson = new GsonBuilder()
                .setLenient()//json宽松
                .create();
        //方式二：使用JsonReader
//        JsonReader jsonReader = gson.newJsonReader(value.charStream());

        //方式三：自定义TypeAdapter
        gson = new Gson();
        try {
            Student student = gson.fromJson(jsonStr, Student.class);
            Log.i("liweiqing", "普通解析jsonStr：" + student);
        } catch (Exception e) {
            Log.i("liweiqing", "普通解析jsonStr异常打印：" + e.toString());
        }

        gson = new GsonBuilder().registerTypeAdapter(Student.class, new StudentTypeAdapter()).create();
        try {
            Student student = gson.fromJson(jsonStr, Student.class);
            Log.i("liweiqing", "自定义TypeAdapter方式解析jsonStr：" + student);
        } catch (Exception e) {
            Log.i("liweiqing", "自定义TypeAdapter方式解析jsonStr异常打印：" + e.toString());
        }

    }

    /**
     * gsonTest6()中：方式三的Adapter。
     */
    public class StudentTypeAdapter extends TypeAdapter<Student> {

        @Override
        public void write(JsonWriter out, Student value) throws IOException {
            out.beginObject();
            out.name("name").value(value.getName());
            out.name("id").value(value.getId());
            out.endObject();
        }

        @Override
        public Student read(JsonReader in) throws IOException {
            Student student = new Student();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "name":
                        student.setName(in.nextString());
                        break;
                    case "id":
                        int i = 0;
                        try {
                            String s = in.nextString();
                            i = Integer.parseInt(s);
                        } catch (Exception e) {
                        }
                        student.setId(i);
                        break;
                }
            }
            in.endObject();
            return student;
        }
    }

    /**
     * Gson容错机制（推荐）
     * 这种方式保险是很保险，但是需要维护的地方多，代码量大
     */
    private void gsonTest7() {
        //方式四：基于注解的方式,方式三倾向于整体,注解的方式倾向于字段
        Gson gson = new Gson();
        String jsonStr = "{\"name\":\"张三\",\"id\":\"\"}";

        try {
            Teacher student = gson.fromJson(jsonStr, Teacher.class);
            Log.i("liweiqing", "普通解析jsonStr：" + student);
        } catch (Exception e) {
            Log.i("liweiqing", "普通解析jsonStr异常打印：" + e.toString());
        }

        //注意：看Teacher的id字段，是用了下面的IntegerTypeAdpater来进行容错判断的
        Teacher teacher = gson.fromJson(jsonStr, Teacher.class);
        Log.i("liweiqing", "基于注解的方式来进行容错：" + teacher);
    }


    /**
     * 这个类是用于字段的注解方式实现的容错机制，gsonTest7方法中的方式四。
     */
    public class IntegerTypeAdapter extends TypeAdapter<Integer> {

        @Override
        public void write(JsonWriter out, Integer value) throws IOException {
            out.value(value);
        }

        @Override
        public Integer read(JsonReader in) throws IOException {
            int i = 0;
            try {
                String str = in.nextString();
                i = Integer.parseInt(str);
            } catch (Exception e) {

            }
            return i;
        }
    }

    /**
     * Gson容错机制，基于字段的容错（推荐）
     */
    private void gsonTest8() {
        //int字段解析器
        JsonDeserializer intJsonDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    int asInt = json.getAsInt();
                    return asInt;
                } catch (Exception e) {
                    return 111;
                }
            }
        };
        //String字段解析器不好使，不知道什么原因。
        JsonDeserializer stringJsonDeserializer = new JsonDeserializer() {
            @Override
            public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    String asString = json.getAsString();
                    if (TextUtils.isEmpty(asString)) {
                        asString = "";
                    }
                    Log.i("liweiqing", "String解析器：" + asString);
                    return asString;
                } catch (Exception e) {
                    Log.i("liweiqing", "String解析器异常：" + e.toString());
                    return "异常了";
                }
            }
        };

        Gson gson;
        //方式五：基于某个字段类型的解析
        gson = new Gson();
        String jsonStr = "{\"name\":\"张三\",\"age\":\"\",\"className\":null}";

        try {
            Teacher teacher = gson.fromJson(jsonStr, Teacher.class);
            Log.i("liweiqing", "普通解析jsonStr：" + teacher);
        } catch (Exception e) {
            Log.i("liweiqing", "普通解析jsonStr异常打印：" + e.toString());
        }

        //注意:Teacher的age字段为int型，但是json串里面的age字段对应的却是""，所以解析就会出现问题，但是这种方式匹配了一个专门针对int型的解析器。
        gson = new GsonBuilder()
                .registerTypeAdapter(int.class, intJsonDeserializer)
                .registerTypeAdapter(String.class, stringJsonDeserializer)
                .create();
        Teacher teacher = gson.fromJson(jsonStr, Teacher.class);
        Log.i("liweiqing", "基于某个字段类型的解析：" + teacher);

    }
}
