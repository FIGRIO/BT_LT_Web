package vn.iotstart.controller.manager;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstart.model.Category;
import vn.iotstart.model.User;
import vn.iotstart.sercvice.CategoryService;
import vn.iotstart.sercvice.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = {"/manager/home"})
public class ManagerHomeController extends HttpServlet{

    private static final long serialVersionUID = 1L;
    
    // BỔ SUNG: Gọi Service
    CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // BỔ SUNG: Lấy thông tin User đăng nhập
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("account");

        if (user != null) {
            // YÊU CẦU: Manager chỉ hiển thị list của userid tương ứng
            // Gọi hàm findByCreatorId đã viết trong Service
            List<Category> list = cateService.findByCreatorId(user.getUserId());
            req.setAttribute("listCate", list);
            req.getRequestDispatcher("/views/manager/home.jsp").forward(req, resp);
        } else {
            // Chưa đăng nhập thì đá về trang login
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}