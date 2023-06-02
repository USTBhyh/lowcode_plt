package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.bson.Document;
import service.DataBaseService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

// 前端发布表单信息处理
@WebServlet("/formUrl.do")
public class formUrlController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //  获取参数
        String formData = request.getParameter("formData");
        String url = request.getParameter("url");
        int titleIndex = formData.indexOf("title");
        int colonIndex = formData.indexOf(":", titleIndex);
        int commaIndex = formData.indexOf(",", colonIndex);
        String title = formData.substring(colonIndex+2, commaIndex-1);

        // 处理
        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();
        // 获取集合
        MongoCollection<Document> urlCollection = testDb.getCollection("formUrl");
        // 构造文档
        Document newDocument = new Document();
        newDocument.append("url", "")
                .append("formData", "")
                .append("title","")
                .append("date","")
                .append("answerNum",0)
                .append("answer",new ArrayList<>());
        newDocument.put("url", url);
        newDocument.put("formData",formData);
        newDocument.put("title",title);
        // 获取时间
        // 获取当前时间
        Date date = new Date();
        newDocument.put("date", date);
        // 插入
        urlCollection.insertOne(newDocument);
        client.close();
        response.getWriter().print("Form released!");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
