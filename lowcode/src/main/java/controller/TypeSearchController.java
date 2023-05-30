package controller;

import service.DataStorageService;
import service.TypeSearchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/getType.do")
public class TypeSearchController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TypeSearchService typeSearchService = new TypeSearchService();
        String jsonStr = "";
        try {
            jsonStr = typeSearchService.getType();
        }catch (Exception e){
            e.printStackTrace();
            //异常信息
            request.setAttribute("errorinfo",e.getMessage());
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(jsonStr);
    }
}
