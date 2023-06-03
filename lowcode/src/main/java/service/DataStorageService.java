package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

public class DataStorageService {
    public void storeData(String jsonString, String subUrl) throws Exception{

        System.out.println(jsonString);

        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();

        // 处理
        JSONArray jsonArray = JSON.parseArray(jsonString);

        //*********修改*********//
        //存储form相关信息
        Bson filter = Filters.regex("url", ".*subUrl=" + subUrl + ".*");
        MongoCollection<Document> formUrl = testDb.getCollection("formUrl");
        Document formDocument = formUrl.find(filter).first();
        Integer answerNum = formDocument.getInteger("answerNum");
        answerNum = answerNum + 1;
        formDocument.put("answerNum",answerNum);
        // 更新文档
        formUrl.updateOne(filter, new Document("$set", formDocument));
        //*********修改*********//

        //first field 类似主键，唯一确定一个 type 的文档
        //根据 first field  create or update 文档 ——> 得到文档id
        //得到文档 id 与文档 type后，就可建立文档间关系，实质上为图节点建立关系——双重循环

        // 遍历，统计
        Set<String> typeSet = new HashSet<>(); // 存储出现过的 schema
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        ArrayList<Map<String, Map<String, String>>> resultList = new ArrayList<>();
        //   schema       field    value
        Map<String, String> firstFieldMap = new HashMap<>();//用于查找文档（唯一确定一个文档）
        //  schema  first_field
        Map<ObjectId, String> docIdMap = new HashMap<>();//本次数据存储涉及的所有文档集合，用于建立关系。用 map 是为了存储 schema。java8 不支持 pair
        ArrayList<Map<ObjectId, String>> docIdMapList = new ArrayList<>();
        //  ObjectId   schema

        int i = 0;
        int j = 0;
        for (i = 0; i < jsonArray.size(); i=j) {
            int flag = 0;
            for(j = i; j  < jsonArray.size(); j++)
            {
                System.out.println(j);
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                String field = jsonObject.getString("field");
                String type = jsonObject.getString("schema");
                String value = jsonObject.getString("value");
                typeSet.add(type);
                Map<String, String> newTypeMap = null;
                Map<String, String> typeMap = resultMap.get(type);
                if (typeMap == null) {
                    newTypeMap = new HashMap<>();
                    firstFieldMap.put(type, field);
                }else {
                    if(typeMap.containsKey(field)){
                        resultList.add(new HashMap<>(resultMap));
                        flag = 1;
                        resultMap.remove(type);
                        break;
                    }
                    newTypeMap = new HashMap<>(typeMap);
                }
                newTypeMap.put(field, value);
                resultMap.put(type, newTypeMap);
            }
            if (flag==0)
            {
                resultList.add(new HashMap<>(resultMap));
            }
            System.out.println("***********************");
            System.out.println(resultList.toString());
            System.out.println("***********************");
        }

        // 对结果集进行切片操作
        List<Map<String, Map<String, String>>> subList = resultList.subList(0, resultList.size());
        // 信息存储
        for (Map<String, Map<String, String>> stringMapMap : subList) {
            for (String typeToSave : typeSet) {
                // 获取数据模式
                // 查询type集合，找到所有字段，组合一个值为空的文档
                Document newDocument = new Document();
                // ********************************************************************************************************** //
                // 需要修改，查数据模式生成，也就是需要按照typeToSave查询数据模式那个集合，然后把他的字段全部取出加入到newDocument中
                // 可以遍历，newDocument.append(属性名, "")，值都为空
                // ********************************************************************************************************** //
                MongoCollection<Document> registeredTypes = testDb.getCollection("registeredTypes");
                Document typeQuery = new Document("schemaName", typeToSave);
                Document typeDoc = registeredTypes.find(typeQuery).first();
                ArrayList<String> fieldsList = (ArrayList<String>) typeDoc.get("fields");
                for(String fieldName: fieldsList){
                    newDocument.append(fieldName, "");
                }
                // 获取集合
                MongoCollection<Document> tempCollection = testDb.getCollection(typeToSave);

                Map<String, String> typeMap = stringMapMap.get(typeToSave);
                if (typeMap != null) {

                    // 获取查询值
                    String firstField = firstFieldMap.get(typeToSave);
                    String firstValue = typeMap.get(firstField);

                    System.out.println(firstField+": "+firstValue);
                    newDocument.put(firstField, firstValue);
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

                        docIdMap.put(id, typeToSave);

                        // 创建用于更新文档的查询语句
                        Document queryById = new Document("_id", id);
                        // 修改文档
                        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                            String fieldToFind = entry.getKey(); // 要查找的 field
                            String value = entry.getValue();
                            document.put(fieldToFind, value);
                        }
                        // 使用 replaceOne() 或 updateOne() 方法将更新后的文档写回到集合中
                        tempCollection.replaceOne(queryById, document);

                    }else {
                        //如果没有，直接在数据模式上修改，然后插入
                        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
                            String fieldToFind = entry.getKey(); // 要查找的 field
                            String value = entry.getValue();
                            if (!fieldToFind.equals("first_field") && !fieldToFind.equals("first_value"))
                            {
                                newDocument.put(fieldToFind, value);
                            }
                        }
                        tempCollection.insertOne(newDocument);

                        docIdMap.put(newDocument.getObjectId("_id"), typeToSave);

                        System.out.println("插入成功!");
                    }
                }
            }
            docIdMapList.add(new HashMap<>(docIdMap));
            docIdMap.clear();
        }

        //建立 document 之间关系
        //图节点建立关系：双重循环遍历
        //对于该建立关系方法，相同 type 的 documents 之间也可建立关系
        //一个 document 与任意一种 type 的 document 的连接用列表，表中为相连接的 document ObjectId
        int index = 0;
        for (Map<String, Map<String, String>> stringMapMap : subList) {
            for(Map.Entry<ObjectId,String> entry : docIdMapList.get(index).entrySet()) {
                ObjectId id = entry.getKey();
                String type = entry.getValue();
                for (Map.Entry<ObjectId,String> entry_ : docIdMapList.get(index).entrySet()) {
                    if (!entry_.equals(entry)) {//不和自己建立关系
                        String typeToBulid = entry_.getValue();
                        MongoCollection<Document> tempCollection = testDb.getCollection(typeToBulid);
                        Map<String, String> typeMap = stringMapMap.get(typeToBulid);
                        if (typeMap != null) {
                            // 获取查询值
                            String firstField = firstFieldMap.get(typeToBulid);
                            String firstValue = typeMap.get(firstField);
                            // 查询条件
                            Document query = new Document(firstField, new Document("$eq", firstValue));
                            // 查询
                            Document document = tempCollection.find(query).first();

                            ArrayList<ObjectId> connectionList = new ArrayList<ObjectId>();

                            //若该 document 已与 type 类型 document 建立连接，则更新连接列表
                            if(document.containsKey(type)){
                                // 获取相关 document id 列表
                                connectionList = (ArrayList<ObjectId>) document.get(type);
                                // 添加新 document id
                                connectionList.add(id);
                                //用于更新的查询 document
                                Document doc = new Document();
                                doc.put("$set", new Document(type,connectionList));
                                // 更新相关 document id 列表
                                tempCollection.updateOne(query, doc);
                            }
                            else
                            {
                                //该 document 第一次与 type 类型 document 建立连接，创建连接列表
                                connectionList.add(id);
                                document.append(type, connectionList);
                                tempCollection.replaceOne(query, document);
                            }
                        }
                    }
                }
            }
            index = index + 1;
        }

        // 关闭连接
        client.close();
    }
}
