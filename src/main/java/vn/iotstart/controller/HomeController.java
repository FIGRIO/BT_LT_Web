package vn.iotstart.controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstart.model.User; // Đảm bảo User không phải Users
import vn.iotstart.sercvice.UserService;
import vn.iotstart.sercvice.impl.UserServiceImpl;
import vn.iotstart.controller.Constant; // Đã sửa import đúng package utils
import vn.iotstart.utils.Email;

@WebServlet(urlPatterns = { "/user/home", "/login", "/register", "/forgotpass", "/waiting", "/VerifyCode", "/logout" })
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/login":
                getLogin(req, resp);
                break;
            case "/register":
                getRegister(req, resp);
                break;
            case "/logout":
                getLogout(req, resp);
                break;
            case "/forgotpass":
                req.getRequestDispatcher("/views/auth/forgotpassword.jsp").forward(req, resp);
                break;
            case "/VerifyCode":
                req.getRequestDispatcher("/views/auth/verify.jsp").forward(req, resp);
                break;
            case "/waiting":
                getWaiting(req, resp);
                break;
            case "/user/home":
            default:
                req.getRequestDispatcher("/views/user/home.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/login":
                postLogin(req, resp);
                break;
            case "/register":
                postRegister(req, resp);
                break;
            case "/forgotpass":
                postForgotPassword(req, resp);
                break;
            case "/VerifyCode":
                postVerifyCode(req, resp);
                break;
            default:
                super.doPost(req, resp);
                break;
        }
    }

    // ================= XỬ LÝ LOGIN =================
    protected void getLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    String username = cookie.getValue();
                    User user = userService.login(username, null); 
                    if(user != null) {
                         session = req.getSession(true);
                         session.setAttribute("account", user);
                         resp.sendRedirect(req.getContextPath() + "/waiting");
                         return;
                    }
                }
            }
        }
        req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
    }

    protected void postLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        boolean isRememberMe = "on".equals(remember);

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
            return;
        }

        User user = userService.login(username, password);

        if (user != null) {
            if(user.getStatus() != 1) {
                req.setAttribute("error", "Tài khoản chưa được kích hoạt hoặc đã bị khóa!");
                req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
                return;
            }
            
            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);

            if (isRememberMe) {
                saveRememberMe(resp, username);
            }

            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            req.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
        }
    }

    private void saveRememberMe(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // ================= XỬ LÝ LOGOUT =================
    protected void getLogout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("account");
            session.invalidate();
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                    break;
                }
            }
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }

    // ================= XỬ LÝ REGISTER =================
    protected void getRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
    }

    protected void postRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");

        if (userService.checkExistEmail(email)) {
            req.setAttribute("error", "Email đã tồn tại!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        } 
        if (userService.checkExistUsername(username)) {
            req.setAttribute("error", "Tài khoản đã tồn tại!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        Email sm = new Email();
        String code = sm.getRandom();
        
        // --- SỬA LỖI TYPO ---
        // Đổi 'new Users' thành 'new User'
        // Constructor này khớp với User(username, email, fullname, code) trong Model
        User user = new User(username, email, fullname, code); 

        boolean emailSent = sm.sendEmail(user);
        
        if (emailSent) {
            HttpSession session = req.getSession();
            session.setAttribute("account", user); 
            
            boolean isSuccess = userService.register(email, password, username, fullname, code);
            
            if (isSuccess) {
                resp.sendRedirect(req.getContextPath() + "/VerifyCode");
            } else {
                req.setAttribute("error", "Lỗi thao tác cơ sở dữ liệu!");
                req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            }
        } else {
            req.setAttribute("error", "Không thể gửi email xác thực!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
        }
    }
    
    // ================= XỬ LÝ VERIFY CODE =================
    protected void postVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("account");
            String code = req.getParameter("authcode");

            if (user != null && code != null && code.equals(user.getCode())) {
                user.setStatus(1);
                userService.updatestatus(user);
                
                req.setAttribute("message", "Kích hoạt thành công! Vui lòng đăng nhập.");
                req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Mã kích hoạt không đúng!");
                req.getRequestDispatcher("/views/auth/verify.jsp").forward(req, resp);
            }
        }
    }

    // ================= XỬ LÝ WAITING (PHÂN QUYỀN) =================
    protected void getWaiting(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            User u = (User) session.getAttribute("account");
            req.setAttribute("username", u.getUsername());
            
            // --- SỬA LOGIC LẤY ROLE ID ---
            // Vì User bây giờ chứa object Role, nên phải gọi u.getRole().getRoleId()
            int roleId = u.getRole().getRoleId(); 
            
            if (roleId == 1) {
                resp.sendRedirect(req.getContextPath() + "/admin/home");
            } else if (roleId == 2) {
                resp.sendRedirect(req.getContextPath() + "/manager/home");
            } else {
                resp.sendRedirect(req.getContextPath() + "/user/home");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
    
    // ================= XỬ LÝ QUÊN MẬT KHẨU =================
    protected void postForgotPassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        
        if(username == null || email == null) {
            req.setAttribute("error", "Vui lòng nhập thông tin");
             req.getRequestDispatcher("/views/auth/forgotpassword.jsp").forward(req, resp);
             return;
        }

        User user = userService.findOne(username.trim());

        if (user != null && user.getEmail().equalsIgnoreCase(email.trim())) {
            Email sm = new Email();
            boolean test = sm.sendEmail(user);
            if (test) {
                req.setAttribute("message", "Vui lòng kiểm tra email để lấy lại mật khẩu.");
            } else {
                req.setAttribute("error", "Lỗi gửi email!");
            }
        } else {
            req.setAttribute("error", "Thông tin tài khoản hoặc email không chính xác!");
        }
        req.getRequestDispatcher("/views/auth/forgotpassword.jsp").forward(req, resp);
    }
}