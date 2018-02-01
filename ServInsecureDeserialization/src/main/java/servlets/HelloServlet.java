package servlets;

import servlets.data.User;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by SBT-Meshcheryakov-AA on 15.01.2018.
 */
public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        Random rand = new Random();
        int id = rand.nextInt();
        String name = request.getParameter("name") != null ? request.getParameter("name") : "test";
        User webUser = new User(id, name);
        // Java native serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(webUser);
        oos.close();
        String webUserOISB64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        pageVariables.put("session", webUserOISB64);
        response.getWriter().println(PageGenerator.instance().getPage("hello.html", pageVariables));
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
