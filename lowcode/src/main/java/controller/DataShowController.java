package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.types.ObjectId;
import service.DataBaseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@WebServlet("/dataShow.do")
public class DataShowController extends HttpServlet {
    public static String concat(String type, String value, String segment, Boolean f) {
        //函数用于拼接字符串，作为json字符串返回前端

        if (f == Boolean.TRUE) {
            value = "\"" + value + "\"";
        }
        return "{\"field\":\"" + segment + "\", \"schema\":\"" + type + "\", \"value\":" + value + "}";
    }

    protected Document getAggregation(MongoCollection<Document> collection, String type, String field){
        //这个函数的存在意义相当于用重载对targetId做一个默认值处理
        return getAggregation(collection,type,field,"null");
    }

    protected Document getAggregation(MongoCollection<Document> collection, String type, String field,String targetId) {
        // 创建聚合管道demo
        List<Document> ops = new ArrayList<Document>();
        Document firstMatch = null;
        if(field != null) {
            Document query = new Document(field, new Document(new Document("$not", new Document("$type", Arrays.asList("int", "long", "double", "decimal")))));
            firstMatch = collection.find(query).first();
        }
        if((field == null || firstMatch != null) && !type.equals("count")) {
            //简单判断，不允许存在非数值元素的字段进行除count以外的聚合
            System.out.println("error!");
            return null;
        }

        switch (type) {
            case "max": {
                //$max求最大值，后面跟$ + 想要求最大值的字段
                ops.add(new Document("$group", new Document("_id","$" +  targetId).append("max" + field, new Document("$max", "$" + field))));
                break;
            }
            case "min": {
                //$min
                ops.add(new Document("$group", new Document("_id","$" +  targetId).append("max" + field, new Document("$min", "$" + field))));
                break;
            }
            case "avg": {
                //$avg
                ops.add(new Document("$group", new Document("_id","$" +  targetId).append("avg" + field, new Document("$avg", "$" + field))));
                break;
            }
            case "sum": {
                //$sum
                ops.add(new Document("$group", new Document("_id","$" + targetId).append("sum" + field, new Document("$sum", "$" + field))));
                break;
            }
            case "count":{
                ops.add(new Document("$group", new Document("_id","$" + targetId).append("count", new Document("$sum", 1))));
                break;
            }

        }
        ops.add(new Document("$sort", new Document("_id", 1)));
        AggregateIterable<Document> aggregate = collection.aggregate(ops);
        for (Document dbObject : aggregate) {
            System.out.println(dbObject);
        }
        // 创建一个新的文档，用于存放合并后的结果，搞一个合适的格式返回前端
        Document merged = new Document();

        for (Document document : aggregate) {
            merged.append(document.get("_id").toString(), document.get("avgsalary"));
        }

        System.out.println(merged.toJson());
        return merged;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //接受解析json串
//        StringBuilder jsonBuilder = new StringBuilder();
//        String line;
//        BufferedReader reader = request.getReader();
//        while ((line = reader.readLine()) != null) {
//            jsonBuilder.append(line);
//        }
//        String json = jsonBuilder.toString();
//        System.out.println(json);
//        ArrayList<Map<String, Object>> dataList = JSON.parseObject(json, new TypeReference<ArrayList<Map<String, Object>>>(){});

        String pageString = request.getParameter("page");
        String pageSizeString = request.getParameter("pageSize");
        int page = Integer.parseInt(pageString);
        int pageSize = Integer.parseInt(pageSizeString);
        String json = request.getParameter("value");
        JSONArray jsonArray = JSON.parseArray(json);

        //处理输入main_class主类(默认第一个)，classes[]涉及查询的类，segments[[]]每个类对应需要的属性
        ArrayList<String> classes = new ArrayList<String>();
        ArrayList<ArrayList<String>> segments = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String mode = jsonObject.getString("schema");
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

        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase db = dataBaseService.SelectDateBase();

        //query_list[{}]是所有非main_class的查询结果，为HashMap:_id->{所有数据串}
        HashMap<String, Document> dic = new HashMap<String, Document>();
        for (int i = 1; i < classes.size(); i++) {
//            segments.get(i).add(0,"_id");
            FindIterable<Document> documents;
            if (segments.get(i).size() == 0)
                documents = db.getCollection(classes.get(i)).find().skip(page).limit(pageSize);
            else
                documents = db.getCollection(classes.get(i)).find()
                        .projection(Projections.fields(Projections.include(segments.get(i)))).skip(page).limit(pageSize);
            for (Document document : documents) {
                String id = document.getObjectId("_id").toHexString();
                document.remove("_id");
                System.out.println(document.toString());
                dic.put(id, document);
            }
        }

        // 拼接json串
        StringBuilder res = new StringBuilder("[");
        int f = 0;
        FindIterable<Document> documents = db.getCollection(classes.get(0)).find().skip(page).limit(pageSize);
//        System.out.println(documents);
//                .projection(Projections.fields(Projections.include(segments.get(0))));
        System.out.println("test1");
        for (Document document : documents) {
            if (f == 1)
                res.append(",");
            f = 1;
            StringBuilder obj = new StringBuilder("[");
            int ff = 0;
            for (String key : document.keySet()) {
                int ind = classes.indexOf(key);
                if (ind != -1) {
////                    System.out.println("test2");
//                    Document tmp = dic.get(document.get(key).toString());
//                    for(String k:tmp.keySet()){
//                        if(ff == 1)
//                            obj.append(",");
//                        ff = 1;
//                        obj.append(concat(key, tmp.get(k).toString(), k, tmp.get(k) instanceof String));
//                    }
                    ArrayList<String>list = (ArrayList<String>)document.get(key);
//                    ArrayList<ObjectId> list = (ArrayList<ObjectId>) document.get(key);
//                    for (ObjectId o : list) {
//                        String s = o.toString();
                    for(String s :list){
                        if (ff == 1)
                            obj.append(",");
                        ff = 1;
                        obj.append("[");
                        int tf = 0;
                        Document tmp = dic.get(s);
                        for (String k : tmp.keySet()) {
                            if (tf == 1)
                                obj.append(",");
                            tf = 1;
                            obj.append(concat(key, tmp.get(k).toString(), k, tmp.get(k) instanceof String));
                        }
                        obj.append("]");
                    }
                } else if (segments.get(0).contains(key)) {
                    if (ff == 1)
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
