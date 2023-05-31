package service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

// 作用：用于统一数据库连接
public class DataBaseService {
    public MongoClient loginDateBase()
    {
       // 本地
       return new MongoClient("127.0.0.1");
       // docker部署
//       return new MongoClient("mongodb", 27017);
    }

    public MongoDatabase SelectDateBase()
    {
        MongoClient client = loginDateBase();
        // 打开数据库
        return client.getDatabase("test");
    }
}
