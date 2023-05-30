package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;


@WebServlet("/formUrl.do")
public class formUrlController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 获取参数(需修改)
        String formData = request.getParameter("formData");
//        String url = request.getParameter("url");
        int titleIndex = formData.indexOf("title");
        int colonIndex = formData.indexOf(":", titleIndex);
        int commaIndex = formData.indexOf(",", colonIndex);
        String title = formData.substring(colonIndex+2, commaIndex-1);
        // 定义固定的前缀URL
        String prefixUrl = "http://localhost:8081/demo_war_exploded/form/";

        // 定义随机生成的文件名
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        String randomFileName = sb.toString();
        String randomUrl = prefixUrl + randomFileName + ".html";
        // String title = request.getParameter("title");
        System.out.println("randomUrl:"+randomUrl);
        System.out.println("title:"+title);

        // 处理
        // 创建连接
//        MongoClient client = new MongoClient("127.0.0.1");
        MongoClient client = new MongoClient("mongodb", 27017);
        // 打开数据库
        MongoDatabase testDb = client.getDatabase("test");
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
        newDocument.put("url", randomFileName+".html");
        newDocument.put("formData",formData);
        newDocument.put("title",title);
        // 获取时间
        // 获取当前时间
        Date date = new Date();
        // 格式化日期为 "M月d日 H:mm" 格式
//        SimpleDateFormat dateFormat = new SimpleDateFormat("M月d日 H:mm");
//        String formattedDate = dateFormat.format(date);
        newDocument.put("date", date);
//        newDocument.put("m_date", date);
        // 插入
        urlCollection.insertOne(newDocument);
        response.getWriter().print("Form released!"+randomUrl);
//        response.sendRedirect("http://localhost:8081/demo_war_exploded/index.html#/admin/formFinish");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
