package vn.iotstart.sercvice.impl;

import vn.iotstart.dao.UserDao;
import vn.iotstart.dao.impl.UserDaoImpl;
import vn.iotstart.model.Role;
import vn.iotstart.model.User;
import vn.iotstart.sercvice.UserService;

public class UserServiceImpl implements UserService {
    
    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        // Kiểm tra null và password (nên mã hóa password thực tế)
        if (user != null && password.trim().equals(user.getPassword().trim())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }
    
    @Override
    public User findOne(String username) {
        return userDao.findOne(username);
    }

    @Override
    public boolean register(String email, String password, String username, String fullname, String code) {
        if (userDao.checkExistEmail(email)) {
            return false;
        }
        if (userDao.checkExistUsername(username)) {
            return false;
        }

        // --- CẬP NHẬT LOGIC TẠO USER MỚI ---
        
        // 1. Tạo User mới
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setFullname(fullname);
        newUser.setPassword(password); // Nên mã hóa ở đây
        newUser.setStatus(0); // Chưa kích hoạt
        newUser.setCode(code);
        
        // 2. Thiết lập Role mặc định cho User mới đăng ký
        // Giả sử Role ID 3 là "User/Customer"
        Role defaultRole = new Role();
        defaultRole.setRoleId(3); 
        newUser.setRole(defaultRole); // Quan trọng: Gán object Role để DAO không bị lỗi NullPointerException
        
        // newUser.setRoleid(3); // Giữ field cũ nếu cần tương thích ngược
        
        // 3. Gọi DAO
        userDao.insertregister(newUser);
        return true;
    }
    
    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public void updatestatus(User user) {
        userDao.updatestatus(user);
    }
}