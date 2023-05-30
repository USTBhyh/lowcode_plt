package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/formData.do")
public class formDataController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 获取参数(需修改)
        String url = request.getParameter("url");
        // 输出结果（即文件名，如 1）
        System.out.println(url);
        // 创建连接
//        MongoClient client = new MongoClient("127.0.0.1");
        MongoClient client = new MongoClient("mongodb", 27017);
        // 打开数据库
        MongoDatabase testDb = client.getDatabase("test");
        // 获取集合
        MongoCollection<Document> tempCollection = testDb.getCollection("formUrl");
        // 查询
        Document query = new Document("url", new Document("$eq", url));
        Document document = tempCollection.find(query).first();
        String mSeg = document.getString("formData");
        System.out.println(mSeg);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(mSeg);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
