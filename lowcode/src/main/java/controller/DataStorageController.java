package controller;

import com.alibaba.fastjson.JSONObject;
import service.DataStorageService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        System.out.println(strB);
        JSONObject json = JSONObject.parseObject(strB.toString());
        String submission = json.getString("submission");
        System.out.println(submission);
        String url = json.getString("url");
        System.out.println(url);
        JSONObject mUrl = JSONObject.parseObject(url);
        String value = mUrl.getString("_value");
        String pattern = "[\\?&]subUrl=([^&]*)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(value);
        String subUrl = "";
        if (matcher.find()) {
            subUrl = matcher.group(1);
            System.out.println(subUrl);
        }

        DataStorageService dataStorageService = new DataStorageService();
        try {
            dataStorageService.storeData(submission,subUrl);
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
