package servlet;

import beans.Image;
import beans.MySQLBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchController")
public class SearchController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keywords");
        String[] list = keyword.split(",");
        List<String> keywords = new ArrayList<>();
        for (int i = 0; i < list.length; i++)
            if (list[i].trim().length() > 0)
                keywords.add(list[i].trim());
        MySQLBean mysql = new MySQLBean();
        List<Image> result = mysql.getImageByKeywords(keywords);
        mysql.close();
        request.setAttribute("result", result);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/show.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
