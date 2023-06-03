package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class TypeSaveService {

    public void createSchemas(String str) throws Exception{

        System.out.println(str);

        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase database = dataBaseService.SelectDateBase();

        System.out.println("Connect to database successfully");

        MongoCollection<Document> typeCollection = database.getCollection("registeredTypes");
        typeCollection.deleteMany(new Document());

        JSONArray typeArray = JSON.parseArray(str);
//        System.out.println(typeArray);
        for(int i=0; i<typeArray.size(); i++)
        {
            JSONObject typeObj = typeArray.getJSONObject(i);
            System.out.println(typeObj);
            String typeName = typeObj.getString("schemaName");

            JSONArray fields = typeObj.getJSONArray("fields");
            ArrayList<String> fieldList = new ArrayList<String>();
            for(int t = 0; t<fields.size(); t++){
                String field = fields.getString(t);
                fieldList.add(field);
            }

            Document doc = new Document("schemaName", typeName).
                    append("fields", fieldList);

            // 先查询有没有，有则覆盖，没有则插入
            Document fSql = new Document("schemaName", new Document("$eq", typeName));
            Document first = typeCollection.find(fSql).first();
            if (first != null)
            {
                ObjectId id = first.getObjectId("_id");
                // 创建用于更新文档的查询语句
                Document queryById = new Document("_id", id);
                // 使用 replaceOne() 或 updateOne() 方法将更新后的文档写回到集合中
                typeCollection.replaceOne(queryById, doc);
            }else {
                typeCollection.insertOne(doc);
            }
            if (!database.listCollectionNames().into(new ArrayList<String>()).contains(typeName)) {
                database.createCollection(typeName);
            }
        }
        client.close();
    }
}