package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import service.DataBaseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/collect.do")
public class collectController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 获取参数(需修改)
        String collectData = request.getParameter("collectData");
        // 处理
        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();

        // 处理
        String jsonString = "[{\"field\":\"name\",\"type\":\"student\",\"value\":\"hyh\"},{\"field\":\"stu_no\",\"type\":\"student\",\"value\":\"420XXXX\"},{\"field\":\"name\",\"type\":\"teacher\",\"value\":\"XXX\"},{\"field\":\"tea_no\",\"type\":\"teacher\",\"value\":\"123XXX\"}]";
        JSONArray jsonArray = JSON.parseArray(jsonString);

        // 遍历，输出
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("type")+"_"+jsonObject.getString("field") + ": " + jsonObject.getString("value"));
        }

        // 遍历，统计
        Set<String> typeSet = new HashSet<>(); // 存储出现过的 type
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String field = jsonObject.getString("field");
            String type = jsonObject.getString("type");
            String value = jsonObject.getString("value");
            typeSet.add(type);
            Map<String, String> typeMap = resultMap.get(type);
            if (typeMap == null) {
                typeMap = new HashMap<>();
                typeMap.put("first_field", field);  // 将第一个 field 记录到 typeMap 中
                typeMap.put("first_value", value);  // 将第一个 value 记录到 typeMap 中
                resultMap.put(type, typeMap);
            } else {
                typeMap.put(field, value);
            }
        }

        // 遍历每个 type 查找对应的 field 和 value
        for (String typeToFind : typeSet) {
            Map<String, String> typeMap = resultMap.get(typeToFind);
            if (typeMap != null) {
                System.out.println("type: " + typeToFind);
                for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                    String fieldToFind = entry.getKey(); // 要查找的 filed
                    String value = entry.getValue();
                    System.out.println(fieldToFind + ": " + value);
                }
            }
        }

        // 信息存储
        for (String typeToSave : typeSet) {
            // 获取数据模式
            // 查询type集合，找到所有字段，组合一个值为空的文档
            Document newDocument = new Document();
            // ********************************************************************************************************** //
            // 需要修改，查数据模式生成，也就是需要按照typeToSave查询数据模式那个集合，然后把他的字段全部取出加入到newDocument中
            // 可以遍历，newDocument.append(属性名, "")，值都为空
            // ********************************************************************************************************** //
            // 这里先手动建立一个document
            if(typeToSave.equals("student"))
            {
                newDocument.append("name", "")
                        .append("stu_no", "")
                        .append("teacher", "");
            }else {
                newDocument.append("name", "")
                        .append("tea_no", "")
                        .append("student", "");
            }
            // 获取集合
            MongoCollection<Document> tempCollection = testDb.getCollection(typeToSave);

            Map<String, String> typeMap = resultMap.get(typeToSave);
            if (typeMap != null) {
                // 获取查询值
                String firstField = typeMap.get("first_field");
                String firstValue = typeMap.get("first_value");
                System.out.println(firstField+":"+firstValue);
                newDocument.put(firstField,firstValue);
                // 查询
                // 查询条件
                Document query = new Document(firstField, new Document("$eq", firstValue));
                Document document = tempCollection.find(query).first();
                if (document != null) {
                    // 有查询结果
                    System.out.println("找到一条记录！");
                    // 修改文档，把原来的和新的都修改到数据模式中
                    // 获取 "_id" 字段的值
                    ObjectId id = document.getObjectId("_id");
                    // 创建用于更新文档的查询语句
                    Document queryById = new Document("_id", id);
                    // 修改文档
                    for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                        String fieldToFind = entry.getKey(); // 要查找的 filed
                        String value = entry.getValue();
                        document.put(fieldToFind, value);
                    }
                    // 使用 replaceOne() 或 updateOne() 方法将更新后的文档写回到集合中
                    tempCollection.replaceOne(queryById, document);
                }else {
                    //如果没有，直接在数据模式上修改，然后插入
                    for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                        String fieldToFind = entry.getKey(); // 要查找的 filed
                        String value = entry.getValue();
                        if (!fieldToFind.equals("first_field") && !fieldToFind.equals("first_value"))
                        {
                            newDocument.put(fieldToFind, value);
                        }
                    }
                    tempCollection.insertOne(newDocument);
                    System.out.println("插入成功!");
                }
            }
        }

        // 建立关系(写的好复杂啊，我比较笨，ahhhh)
        List<MongoCollection<Document>> collections = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        List<ObjectId> ids = new ArrayList<>();
        List<String> typesToBuild = new ArrayList<>();
        List<Document> queryByIds = new ArrayList<>();

        for (String typeToBuild : typeSet)
        {
            typesToBuild.add(typeToBuild);
            // 获取集合
            MongoCollection<Document> tempCollection = testDb.getCollection(typeToBuild);
            collections.add(tempCollection);
            Map<String, String> typeMap = resultMap.get(typeToBuild);
            if (typeMap != null) {
                // 获取查询值
                String firstField = typeMap.get("first_field");
                String firstValue = typeMap.get("first_value");
                // 查询条件
                Document query = new Document(firstField, new Document("$eq", firstValue));
                // 查询
                Document document = tempCollection.find(query).first();
                documents.add(document);
                ObjectId id = document.getObjectId("_id");
                ids.add(id);
                Document queryById = new Document("_id", id);
                queryByIds.add(queryById);
            }
        }

        documents.get(0).put(typesToBuild.get(1),ids.get(1));
        documents.get(1).put(typesToBuild.get(0),ids.get(0));
        collections.get(0).replaceOne(queryByIds.get(0),documents.get(0));
        collections.get(1).replaceOne(queryByIds.get(1),documents.get(1));

        // 待开发：假设一种情景，项目中还包括同队学生和老师的话
        // 提供一种实现思路：可以遍历数据模式的那个集合，获取所有数据模式的名字，然后如果某json的filed等于其中的某个集合的名字，那么就说明有关系
        // 举个栗子：type为工程的json的filed等于student（假设有个数据模式的名字为student_name）,那么就.....
        // 算了我觉得这个json格式实现不了
        // 等着我再和ckh交流一下吧，下周再做这个功能

        // 关闭连接
        client.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
