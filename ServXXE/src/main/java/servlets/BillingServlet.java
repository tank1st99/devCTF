package servlets;

import servlets.data.Product;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Created by SBT-Meshcheryakov-AA on 22.09.2017.
 */
public class BillingServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println(PageGenerator.instance().getPage("bill.html", new HashMap<>()));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<>();

        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("Bad request!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
         try{
            List<FileItem> fields = upload.parseRequest(request);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                response.getWriter().println("Bad request!");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            while (it.hasNext()) {
                FileItem fileItem = it.next();
                if (!fileItem.isFormField() && fileItem.getFieldName().equalsIgnoreCase("bill_xml")) {
                    pageVariables.put("bill_name", fileItem.getName());
                    // process XML
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fileItem.getInputStream());
                    NodeList nList = doc.getElementsByTagName("product");
                    double total_price = 0.0;
                    List<Product> products = new ArrayList<>();
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            String temp_name =  eElement.getElementsByTagName("name").item(0).getTextContent();
                            Integer temp_quantity = Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent());
                            Double temp_price = Double.parseDouble(eElement.getElementsByTagName("price").item(0).getTextContent());
                            products.add(new Product(temp_name, temp_quantity, temp_price));
                            total_price += temp_price;
                        }
                    }
                    pageVariables.put("products", products);
                    pageVariables.put("total_price", total_price);
                }
            }
            response.getWriter().println(PageGenerator.instance().getPage("bill_print.html", pageVariables));
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
