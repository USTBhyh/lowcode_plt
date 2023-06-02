package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import service.DataBaseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet("/formData.do")
public class formDataController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 获取参数
        String url = request.getParameter("url");
        // 输出结果（即文件名，如 1）
        String subUrl = "";
        if(url.contains("subUrl=")){
            subUrl = url.substring(url.indexOf("subUrl=") + 7); // +7 是跳过 "subUrl=" 这个字符串的长度
        }
        System.out.println(subUrl); // 输出 j9Be27tzSL.html
        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();
        // 获取集合
        MongoCollection<Document> tempCollection = testDb.getCollection("formUrl");
        // 查询
//        Document query = new Document("url", new Document("$eq", url));
//        Pattern pattern = Pattern.compile(url, Pattern.CASE_INSENSITIVE);
        Bson filter = Filters.regex("url", ".*subUrl=" + subUrl + ".*");
        Document document = tempCollection.find(filter).first();
        String mSeg = "";
        if (document != null)
        {
            mSeg = document.getString("formData");
            System.out.println(mSeg);
        }
        client.close();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(mSeg);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
