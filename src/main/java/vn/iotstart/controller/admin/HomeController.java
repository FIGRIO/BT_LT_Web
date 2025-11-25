package vn.iotstart.controller.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstart.model.Category;
import vn.iotstart.sercvice.CategoryService;
import vn.iotstart.sercvice.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = {"/admin/home"})
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    // BỔ SUNG: Gọi Service
    CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // YÊU CẦU: Admin hiển thị tất cả Category
        List<Category> list = cateService.getAll();
        req.setAttribute("listCate", list);
        
        req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}