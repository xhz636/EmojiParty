package servlet;

import beans.Image;
import beans.Keyword;
import beans.MySQLBean;
import digest.SHA256;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "UploadController")
public class UploadController extends HttpServlet {

    private final String UPLOAD_DIRECTORY = "emoji";
    private final int MAX_FILE_SIZE = 1 * 1024 * 1024;
    private final int MAX_REQUEST_SIZE = 10 * 1024 * 1024;
    private final int MEMORY_THRESHOLD = 10 * 1024 * 1024;
    private final String[] FILE_FORMAT = {"gif", "jpg", "png", "bmp"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.sendError(500);
            return;
        }
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        upload.setHeaderEncoding("UTF-8");
        String uploadPath = getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            boolean exit = false;
            if (formItems != null && formItems.size() > 0) {
                Image image = new Image();
                String keywords = "";
                MySQLBean mysql;
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        ImageInputStream inputStream = ImageIO.createImageInputStream(item.getInputStream());
                        Iterator<ImageReader> readerIterator = ImageIO.getImageReaders(inputStream);
                        if (!readerIterator.hasNext()) {
                            request.setAttribute("message", "file");
                            exit = true;
                        }
                        else {
                            ImageReader reader = readerIterator.next();
                            String format = reader.getFormatName().toLowerCase();
                            if (format.equals("jpeg"))
                                format = "jpg";
                            boolean allow = false;
                            for (int i = 0; i < FILE_FORMAT.length; i++) {
                                if (format.equals(FILE_FORMAT[i])) {
                                    allow = true;
                                    break;
                                }
                            }
                            if (!allow) {
                                request.setAttribute("message", "format");
                                exit = true;
                            }
                            else {
                                File tempfile = File.createTempFile("emoji", ".tmp");
                                tempfile.deleteOnExit();
                                FileOutputStream outputStream = new FileOutputStream(tempfile);
                                IOUtils.copy(item.getInputStream(), outputStream);
                                String ID = SHA256.getSHA256(tempfile);
                                mysql = new MySQLBean();
                                if (mysql.isIDExist(ID)) {
                                    mysql.close();
                                    request.setAttribute("message", "exist");
                                    exit = true;
                                }
                                else {
                                    mysql.close();
                                    String filepath = uploadPath + File.separator + ID + "." + format;
                                    File file = new File(filepath);
                                    item.write(file);
                                    int size = (int) tempfile.length();
                                    BufferedImage read = ImageIO.read(tempfile);
                                    int height = read.getHeight();
                                    int width = read.getWidth();
                                    Date uploadtime = new Date();
                                    image = new Image(ID, format, size, height, width, uploadtime);
                                }
                            }
                        }
                    }
                    else {
                        String field = item.getFieldName();
                        if (field.equals("keywords")) {
                            keywords = new String(item.getString().getBytes("ISO-8859-1"),"UTF-8") ;
                        }
                    }
                    if (exit)
                        break;
                }
                if (!exit) {
                    mysql = new MySQLBean();
                    String[] list = keywords.split(",");
                    List<String> keys = new ArrayList<>();
                    for (int i = 0; i < list.length; i++) {
                        if (list[i].trim().length() > 0 && list[i].trim().length() <= 32) {
                            keys.add(list[i].trim());
                        }
                    }
                    if (keys.size() > 0) {
                        mysql.addImage(image);
                        for (int i = 0; i < keys.size(); i++) {
                            Keyword keyword = new Keyword(keys.get(i), image.getID(), 0);
                            mysql.addKeyWord(keyword);
                        }
                        request.setAttribute("message", "succeed");
                    }
                    else {
                        request.setAttribute("message", "keyword");
                    }
                    mysql.close();
                }
            }
        } catch (Exception e) {
            response.sendError(500, "Exception");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
