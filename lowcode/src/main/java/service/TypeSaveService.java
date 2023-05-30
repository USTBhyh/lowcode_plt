package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class TypeSaveService {

    public void createSchemas(String str) throws Exception{

        System.out.println(str);

        // 创建 MongoDB 连接
//        MongoClient client = new MongoClient("localhost", 27017);
        MongoClient client = new MongoClient("mongodb", 27017);
        // 获取数据库对象
        MongoDatabase database = client.getDatabase("test");

        System.out.println("Connect to database successfully");

        MongoCollection<Document> typeCollection = database.getCollection("registeredTypes");

        JSONArray typeArray = JSON.parseArray(str);

        for(int i=0; i<typeArray.size(); i++)
        {
            JSONObject typeObj = typeArray.getJSONObject(i);

            String typeName = typeObj.getString("mode");

            JSONArray fields = typeObj.getJSONArray("fields");
            ArrayList<String> fieldList = new ArrayList<String>();
            for(int t = 0; t<fields.size(); t++){
                String field = fields.getString(t);
                fieldList.add(field);
            }

            Document doc = new Document("typeName", typeName).
                    append("fields", fieldList);

            typeCollection.insertOne(doc);

//            MongoCollection<Document> collection = database.getCollection(typeName);
//            collection.insertOne(new Document("type", typeName));
            database.createCollection(typeName);
        }

        client.close();
    }
}