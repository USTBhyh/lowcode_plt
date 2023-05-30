package controller;

import service.TypeSaveService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/saveType.do")
public class TypeSaveController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        StringBuilder str_b = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            str_b.append(line);
        }
        System.out.println(str_b);

        TypeSaveService typeSaveService = new TypeSaveService();
        try {
            typeSaveService.createSchemas(str_b.toString());
        }catch (Exception e){
            e.printStackTrace();
            //异常信息
            request.setAttribute("errorInfo",e.getMessage());
        }

        response.getWriter().print("Type saved!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
