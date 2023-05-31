package service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TypeSearchService {
    public String getType() throws Exception {

        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();

        MongoCollection<Document> typeCollection = testDb.getCollection("registeredTypes");

        StringBuilder jsonStr = new StringBuilder("[");

        MongoCursor<Document> cursor = typeCollection.find().iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            jsonStr.append(document.toJson());
            if(cursor.hasNext())
                jsonStr.append(",");
        }
        jsonStr.append("]");

        System.out.println(jsonStr.toString());

        return jsonStr.toString();
    }
}
