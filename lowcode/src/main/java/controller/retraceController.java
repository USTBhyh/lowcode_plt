package controller;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import service.DataBaseService;


@WebServlet("/retrace.do")
public class retraceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        // 获取参数(需修改)
        String userId = request.getParameter("userId");
        // 处理
        // 创建连接
        DataBaseService dataBaseService = new DataBaseService();
        MongoClient client = dataBaseService.loginDateBase();
        MongoDatabase testDb = dataBaseService.SelectDateBase();

        JSONArray jsonArray = JSON.parseArray(userId);

        // 遍历，输出
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println(jsonObject.getString("type")+"_"+jsonObject.getString("field") + ": " + jsonObject.getString("value"));
        }

        // 获取要填充的内容是哪个集合里的，记录在mainType中
        String mainType = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            String field = jsonObject.getString("field");
            String value = jsonObject.getString("value");
            if(!value.equals(""))
            {
                mainType = type;
                System.out.println(mainType);
            }
        }

        // 遍历，预处理
        String condition = "";
        String mValue = "";
        // 记录要查询的字段
        List<String> fieldList = new ArrayList<>();
//        List<Document> keyValueList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String type = jsonObject.getString("type");
            String field = jsonObject.getString("field");
            String value = jsonObject.getString("value");
            if(!value.equals(""))
            {
                if (type.equals(mainType))
                {
                    condition = field;
                    mValue = value;
                    System.out.println("存在值");
                }
            }else {
                if (type.equals(mainType))
                {
                    fieldList.add(field);
                    System.out.println(fieldList);
                    System.out.println("值为空");
                }
            }
        }

        // 查询
        MongoCollection<Document> tempCollection = testDb.getCollection(mainType);
        // 查询条件
        Document query = new Document(condition, new Document("$eq", mValue));
        Document document = tempCollection.find(query).first();
        if (document != null) {
            // 有查询结果
            for (String seg : fieldList) {
                if (document.containsKey(seg)) {
                    String mSeg = document.getString(seg);
                    // 更新json
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String field = jsonObject.getString("field");
                        if(field.equals(seg)) {
                            // 找到目标元素，将value值替换
                            jsonObject.put("value", mSeg);
                            break;
                        }
                    }
                    System.out.println(jsonArray);
                    System.out.println(mSeg);
                } else {
                    // 没有字段或者值为空
                    System.out.println("");
                }
            }
            System.out.println("查询到结果！");
        } else {
            // 没有查询结果
            System.out.println("未查询到！");
        }

        // 组装查询条件
        // new Document("$and", Arrays.asList(
        // new Document("field", new Document("$eq", field)),
        // new Document("value", new Document("$eq", value))
        // ));

        // 关闭连接
        client.close();

        // 告诉浏览器我给你响应的结果 是什么类型，以及编码格式
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonArray);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
