package vn.iotstart.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Chỉ lọc các đường dẫn bắt đầu bằng /user/ (Trang cá nhân, trang home user...)
// KHÔNG lọc /login, /register để tránh bị lặp vô tận
@WebFilter(urlPatterns = { "/user/*", "/admin/*", "/manager/*" }) 
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        
        // Kiểm tra Session: Nếu null hoặc chưa có account -> ĐÁ VỀ LOGIN
        if (session == null || session.getAttribute("account") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            System.out.print("không thể truy cập");
            return; // Dừng lại, không cho vào Controller
        }

        // Nếu đã đăng nhập -> Cho đi tiếp
        chain.doFilter(request, response);
    }
}