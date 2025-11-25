package vn.iotstart.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import vn.iotstart.model.Category;
import vn.iotstart.model.User;
import vn.iotstart.sercvice.CategoryService;
import vn.iotstart.sercvice.impl.CategoryServiceImpl;

// Cập nhật URL Patterns để xử lý cho cả Admin và Manager
@WebServlet(urlPatterns = { 
    "/admin/category/list", "/admin/category/add", "/admin/category/edit", "/admin/category/delete",
    "/manager/category/list", "/manager/category/add", "/manager/category/edit", "/manager/category/delete"
})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    CategoryService cateService = new CategoryServiceImpl();
    
    public static final String UPLOAD_DIR = "D:\\upload"; 

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI(); // Dùng URI thay vì URL để dễ so sánh
        req.setCharacterEncoding("UTF-8");
        
        // Lấy user để check quyền xóa và redirect
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("account");

        if (url.contains("list")) {
            // Phần này có thể bỏ nếu đã dùng HomeController/ManagerHomeController
            List<Category> list = cateService.getAll();
            req.setAttribute("cateList", list);
            req.getRequestDispatcher("/views/admin/category/list-category.jsp").forward(req, resp);
            
        } else if (url.contains("add")) {
            // Điều hướng form add dùng chung
            req.getRequestDispatcher("/views/admin/category/add-category.jsp").forward(req, resp);
            
        } else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = cateService.get(id);
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category/edit-category.jsp").forward(req, resp);
            
        } else if (url.contains("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            
            // BỔ SUNG: Check quyền trước khi xóa
            Category oldCate = cateService.get(id);
            if(canEditOrDelete(currentUser, oldCate)) {
                cateService.delete(id);
            }
            
            // Redirect về đúng trang dashboard
            redirectHome(req, resp, currentUser);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("account");

        if (url.contains("add")) {
            String categoryName = req.getParameter("name");
            String icon = "";
            
            // Xử lý Upload file
            try {
                Part part = req.getPart("icon");
                if (part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = filename.substring(index + 1);
                    String fname = System.currentTimeMillis() + "." + ext;
                    
                    // Tạo thư mục nếu chưa có
                    File uploadDir = new File(UPLOAD_DIR);
                    if (!uploadDir.exists()) uploadDir.mkdir();

                    part.write(UPLOAD_DIR + File.separator + fname);
                    icon = fname;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Category category = new Category();
            category.setCateName(categoryName);
            category.setIcons(icon);
            
            // BỔ SUNG QUAN TRỌNG: Gán người tạo
            if(currentUser != null) {
                category.setUser(currentUser);
            }
            
            cateService.insert(category);
            redirectHome(req, resp, currentUser);

        } else if (url.contains("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            String categoryName = req.getParameter("name");
            String icon = req.getParameter("old_icon");

            // Xử lý Upload file mới
            try {
                Part part = req.getPart("icon");
                if (part != null && part.getSize() > 0) {
                    String filename = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                    int index = filename.lastIndexOf(".");
                    String ext = filename.substring(index + 1);
                    String fname = System.currentTimeMillis() + "." + ext;
                    
                    part.write(UPLOAD_DIR + File.separator + fname);
                    icon = fname;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Category category = new Category();
            category.setCateId(id);
            category.setCateName(categoryName);
            category.setIcons(icon);
            
            // BỔ SUNG: Check quyền trước khi sửa
            // Lấy category cũ để check người sở hữu (vì form edit có thể không gửi user_id lên)
            Category oldCate = cateService.get(id);
            if(canEditOrDelete(currentUser, oldCate)) {
                cateService.edit(category);
            }
            
            redirectHome(req, resp, currentUser);
        }
    }
    
    // Hàm phụ trợ: Điều hướng về trang chủ tương ứng role
    private void redirectHome(HttpServletRequest req, HttpServletResponse resp, User user) throws IOException {
        if (user != null && user.getRole().getRoleId() == 1) { // 1 là Admin
            resp.sendRedirect(req.getContextPath() + "/admin/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/manager/home");
        }
    }
    
    // Hàm phụ trợ: Kiểm tra quyền sở hữu
    private boolean canEditOrDelete(User user, Category cate) {
        if (user == null || cate == null) return false;
        // Admin (1) được quyền hết
        if (user.getRole().getRoleId() == 1) return true;
        // Manager (2) chỉ được sửa của mình
        if (user.getRole().getRoleId() == 2) {
            // So sánh ID user đang login và ID user tạo category
            // Lưu ý: Cần đảm bảo CategoryDao.get() đã map User vào Category
            return cate.getUser() != null && cate.getUser().getUserId() == user.getUserId();
        }
        return false;
    }
}