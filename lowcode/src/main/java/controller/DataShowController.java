package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/dataShow.do")
public class DataShowController extends HttpServlet {
    public static String  concat(String type,String value,String segment,Boolean f){
        if(f == Boolean.TRUE){
            value = "\"" + value + "\"";
        }
        return "{\"segment\":\"" + segment + "\", \"type\":\"" + type + "\", \"value\":" + value + "}";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受解析json串
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
        System.out.println(json);
//        ArrayList<Map<String, Object>> dataList = JSON.parseObject(json, new TypeReference<ArrayList<Map<String, Object>>>(){});

        JSONArray jsonArray = JSON.parseArray(json);

        //处理输入main_class主类(默认第一个)，classes[]涉及查询的类，segments[[]]每个类对应需要的属性
        ArrayList<String> classes = new ArrayList<>();
        ArrayList<ArrayList<String>> segments = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String mode = jsonObject.getString("mode");
            String fields = jsonObject.getString("fields");
            ArrayList<String> list = (ArrayList<String>) JSON.parseArray(fields, String.class);
            classes.add(mode);
            segments.add(list);
        }
        System.out.println(classes);
        System.out.println(segments);
//        String segment;
//        String type;
//        int index = -1;
//        //遍历dataList填充segments和classes
//        for (Map<String, Object> map : dataList) {
//            segment = map.get("segment").toString();
//            type = map.get("type").toString();
//            index = classes.indexOf(type);
//            if (index != -1) {
//                segments.get(index).add(segment);
//            } else {
//                ArrayList<String> tmp = new ArrayList<String>();
//                tmp.add(segment);
//                segments.add(tmp);
//                classes.add(type);
//            }
//        }


        for (int i = 1; i < classes.size(); i++) {
            System.out.println(classes.get(i));
            for (int j = 0; j < segments.get(i).size(); j++) {
                System.out.println(segments.get(i).get(j));
            }
            System.out.println('\n');
        }

//        MongoClient client = new MongoClient("127.0.0.1");
        MongoClient client = new MongoClient("mongodb", 27017);
        MongoDatabase db = client.getDatabase("test");

        //query_list[{}]是所有非main_class的查询结果，为HashMap:_id->{所有数据串}
        HashMap<String, Document> dic = new HashMap<>();
        for(int i = 1;i < classes.size();i++){
//            segments.get(i).add(0,"_id");
            FindIterable<Document> documents = db.getCollection(classes.get(i)).find()
                    .projection(Projections.fields(Projections.include(segments.get(i))));
            for(Document document:documents){
                String id = document.getObjectId("_id").toHexString();
                System.out.println(id);
                document.remove("_id");
                System.out.println(document);
                dic.put(id,document);
            }
        }

        // 拼接json串
        StringBuilder res = new StringBuilder("[");
        int f = 0;
        FindIterable<Document> documents = db.getCollection(classes.get(0)).find();
//        System.out.println(documents);
//                .projection(Projections.fields(Projections.include(segments.get(0))));
        System.out.println("test1");
        for(Document document:documents){
            if(f == 1)
                res.append(",");
            f = 1;
            StringBuilder obj = new StringBuilder("[");
            int ff = 0;
            for(String key :document.keySet()){
                int ind = classes.indexOf(key);
                if(ind != -1){
                    ArrayList<ObjectId> list = (ArrayList<ObjectId>)document.get(key);
                    for(ObjectId o:list){
                        String s = o.toString();
                        if(ff == 1)
                            obj.append(",");
                        ff = 1;
                        obj.append("[");
                        int tf = 0;
                        Document tmp = dic.get(s);
                        for(String k:tmp.keySet()){
                            if(tf == 1)
                                obj.append(",");
                            tf = 1;
                            obj.append(concat(key, tmp.get(k).toString(), k, tmp.get(k) instanceof String));
                        }
                        obj.append("]");
                    }
                }
                else if(segments.get(0).contains(key)){
                    if(ff == 1)
                        obj.append(",");
                    ff = 1;
                    obj.append(concat(classes.get(0), document.get(key).toString(), key, document.get(key) instanceof String));
                }
            }
            res.append(obj).append("]");
        }
        res.append("]");
        System.out.println(res);
        // 输出
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(res);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
