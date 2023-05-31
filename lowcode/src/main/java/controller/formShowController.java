package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import service.DataBaseService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/formShow.do")
public class formShowController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pageIndexStr = request.getParameter("PageIndex");
        String pageSizeStr = request.getParameter("PageSize");
        int pageIndex = Integer.parseInt(pageIndexStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        String queryParam = request.getParameter("QueryParam");
        System.out.println("tetaetataaaaaaatttt");

        long count;
        // 分页
        //QueryParam[]               // 其他的查询字段
        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();
        // 选中集合
        MongoCollection<Document> formTypes = testDb.getCollection("formUrl");
        // 按title模糊查询
        FindIterable<Document> results = null;
        int skipNumber = (pageIndex - 1) * pageSize;
        if(queryParam != null && !queryParam.isEmpty()) {
            Pattern pattern = Pattern.compile(queryParam, Pattern.CASE_INSENSITIVE);
            Bson filter = Filters.regex("title", pattern);
            Bson orderBy = Sorts.descending("date"); // 按照date字段逆序排序
//            Bson orderBy = Sorts.ascending("date"); // 按照date字段升序排序
            results = formTypes.find(filter).sort(orderBy).skip(skipNumber).limit(pageSize);
            count = formTypes.count(filter);
//            System.out.println(count);
        } else {
            Bson orderBy = Sorts.descending("date"); // 按照date字段逆序排序
            results = formTypes.find().sort(orderBy).skip(skipNumber).limit(pageSize);
            count = formTypes.count();
        }

        List<String> jsonList = new ArrayList<>();
        for (Document doc : results) {
            Date date = doc.getDate("date"); // 获取日期字段的BsonDate类型实例
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
            String dateString = sdf.format(date); // 将日期对象转换为字符串
            doc.put("date", dateString); // 将新的日期字符串设置回Document对象中
            String jsonStr = doc.toJson();
            jsonList.add(jsonStr);
        }
        String jsonResult = JSON.toJSONString(jsonList); // 将jsonList转化为JSON字符串
        JSONObject resultObject = new JSONObject();
        resultObject.put("formDataTotal", count);
        resultObject.put("formDataGroup", JSON.parse(jsonResult));
        String resultJson = resultObject.toJSONString();
        System.out.println(resultJson);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(resultJson);
        client.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
