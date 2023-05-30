package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/public.do")
public class publicFormController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户正在访问的页面
        String page = request.getRequestURI();
        System.out.println("User is visiting " + page);
        // 使用字符串 /form/ 进行分割，取第二个元素作为文件名
        String url = page.split("/form/")[1];
//        System.out.println(url);
//        // 输出结果（即文件名，如 1）
//        System.out.println(url);
//        // 创建连接
//        MongoClient client = new MongoClient("127.0.0.1");
//        // 打开数据库
//        MongoDatabase testDb = client.getDatabase("test");
//        // 获取集合
//        MongoCollection<Document> tempCollection = testDb.getCollection("formUrl");
//        // 查询
//        Document query = new Document("url", new Document("$eq", url));
//        Document document = tempCollection.find(query).first();
//        String mSeg = document.getString("formData");
//        System.out.println(mSeg);
        // 重定向
        response.sendRedirect("http://localhost:8081/demo_war_exploded/#/user/submission?key1="+url);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
