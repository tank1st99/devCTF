package servlets;

import servlets.data.User;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SBT-Meshcheryakov-AA on 16.01.2018.
 */
public class OisServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        // Java native deserialization
        try {
            byte[] byteSession = Base64.getDecoder().decode(request.getParameter("session"));
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteSession));
            User web_user = (User) ois.readObject();
            ois.close();
            pageVariables.put("user", web_user);
        } catch (ClassNotFoundException e) {};

        response.getWriter().println(PageGenerator.instance().getPage("look_at_yourself.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
