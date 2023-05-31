package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

// 跳转统一界面生成
@WebServlet("/public.do")
public class publicFormController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取用户正在访问的页面
        String page = request.getRequestURI();
        System.out.println("User is visiting " + page);
        // 重定向
        response.sendRedirect("http://localhost:8081/lowcode_war_exploded/#/user/submission");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
