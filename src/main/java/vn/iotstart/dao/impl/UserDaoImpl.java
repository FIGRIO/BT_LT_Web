package vn.iotstart.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import vn.iotstart.connection.DBConnectionMySQL;
import vn.iotstart.dao.UserDao;
import vn.iotstart.model.Role;
import vn.iotstart.model.User;

public class UserDaoImpl implements UserDao {
    public Connection conn = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;

    @Override
    public User get(String username) {
        // Tên bảng là 'users' theo Model mới
        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                // Map các trường cơ bản
                user.setUserId(rs.getInt("user_id")); // Lưu ý tên cột trong DB
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFullname(rs.getString("fullname"));
                user.setPassword(rs.getString("password"));
                user.setImages(rs.getString("images"));
                user.setPhone(rs.getString("phone"));
                user.setStatus(rs.getInt("status"));
                user.setCode(rs.getString("code"));
                
                // Handle Seller ID (có thể null)
                int sellerId = rs.getInt("seller_id");
                if(!rs.wasNull()){
                    user.setSellerid(sellerId);
                }

                // MỚI: Map Role Object
                // Giả sử DB vẫn dùng cột role_id (hoặc roleid)
                int roleIdFromDB = rs.getInt("role_id"); 
                
                Role role = new Role();
                role.setRoleId(roleIdFromDB);
                user.setRole(role); // Set Object Role
                
                // (Optional) Vẫn set roleid kiểu int nếu logic cũ cần
                // user.setRoleid(roleIdFromDB); 

                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
        return null;
    }

    @Override
    public void insertregister(User user) {
        // Insert vào bảng 'users'
        String sql = "INSERT INTO users (email,username,fullname,password,status,role_id,code) Values (?,?,?,?,?,?,?)";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getStatus());
            
            // Lấy roleId từ object Role trong User
            if (user.getRole() != null) {
                 ps.setInt(6, user.getRole().getRoleId());
            } else {
                 // Fallback mặc định là User (Role ID = 3 chẳng hạn)
                 ps.setInt(6, 3); 
            }
            
            ps.setString(7, user.getCode());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        boolean duplicate = false;
        String query = "select * from users where email = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                duplicate = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
        return duplicate;
    }

    @Override
    public boolean checkExistUsername(String username) {
        boolean duplicate = false;
        String query = "select * from users where username = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                duplicate = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
        return duplicate;
    }
    
    @Override
    public User findOne(String username) {
        return get(username);
    }

    @Override
    public void updatestatus(User user) {
        String sql = "UPDATE users SET status=?, code=? WHERE email = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getStatus());
            ps.setString(2, user.getCode());
            ps.setString(3, user.getEmail());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
    }
}