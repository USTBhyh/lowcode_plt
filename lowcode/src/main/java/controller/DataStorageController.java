package controller;

import com.alibaba.fastjson.JSONObject;
import service.DataStorageService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/saveData.do")
public class DataStorageController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        StringBuilder strB = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            strB.append(line);
        }
        System.out.println(strB.toString());
        JSONObject json = JSONObject.parseObject(strB.toString());
        String submission = json.getString("submission");
        System.out.println(submission);

        DataStorageService dataStorageService = new DataStorageService();
        try {
            dataStorageService.storeData(submission.toString());
        }catch (Exception e){
            e.printStackTrace();
            //异常信息
            request.setAttribute("errorinfo",e.getMessage());
        }

        response.getWriter().print("Data saved!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
