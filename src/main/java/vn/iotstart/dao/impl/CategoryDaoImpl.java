package vn.iotstart.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.iotstart.connection.DBConnectionMySQL;
import vn.iotstart.dao.CategoryDao;
import vn.iotstart.model.Category;
import vn.iotstart.model.User;

public class CategoryDaoImpl implements CategoryDao {
    
    public Connection conn = null;
    public PreparedStatement ps = null;
    public ResultSet rs = null;

    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<Category>();
        // JOIN bảng users để lấy tên người tạo (hiển thị cho Admin)
        // Lưu ý: Giả sử khóa ngoại là 'user_id' trong bảng category và 'user_id' trong bảng users
        String sql = "SELECT c.*, u.fullname, u.username FROM category c LEFT JOIN users u ON c.user_id = u.user_id";
        
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCateId(rs.getInt("cate_id"));
                category.setCateName(rs.getString("cate_name"));
                category.setIcons(rs.getString("icons"));
                
                // MAP USER VÀO CATEGORY
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullname(rs.getString("fullname")); // Để hiển thị tên người tạo
                user.setUsername(rs.getString("username"));
                
                category.setUser(user); // Quan trọng
                
                categories.add(category);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    // --- HÀM MỚI CHO MANAGER ---
    @Override
    public List<Category> findByCreatorId(int userId) {
        List<Category> categories = new ArrayList<Category>();
        String sql = "SELECT * FROM category WHERE user_id = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCateId(rs.getInt("cate_id"));
                category.setCateName(rs.getString("cate_name"));
                category.setIcons(rs.getString("icons"));
                
                // Gán User hiện tại vào để JSP nhận diện đây là bài của mình
                User user = new User();
                user.setUserId(userId);
                category.setUser(user);
                
                categories.add(category);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category get(int id) {
        String sql = "SELECT * FROM category WHERE cate_id = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Category category = new Category();
                category.setCateId(rs.getInt("cate_id"));
                category.setCateName(rs.getString("cate_name"));
                category.setIcons(rs.getString("icons"));
                
                // Lấy ID người tạo ra
                int creatorId = rs.getInt("user_id");
                User user = new User();
                user.setUserId(creatorId);
                category.setUser(user);
                
                return category;
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Category category) {
        // CẬP NHẬT CÂU INSERT: Thêm user_id
        String sql = "INSERT INTO category(cate_name, icons, user_id) VALUES (?, ?, ?)";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category.getCateName());
            ps.setString(2, category.getIcons());
            
            // Lấy userId từ đối tượng User lồng bên trong Category
            if (category.getUser() != null) {
                ps.setInt(3, category.getUser().getUserId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Category category) {
        // Edit thường không đổi người tạo, giữ nguyên logic cũ
        String sql = "UPDATE category SET cate_name = ?, icons = ? WHERE cate_id = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category.getCateName());
            ps.setString(2, category.getIcons());
            ps.setInt(3, category.getCateId());
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM category WHERE cate_id = ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Category> search(String keyword) {
        List<Category> categories = new ArrayList<Category>();
        String sql = "SELECT * FROM category WHERE cate_name LIKE ?";
        try {
            conn = new DBConnectionMySQL().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCateId(rs.getInt("cate_id"));
                category.setCateName(rs.getString("cate_name"));
                category.setIcons(rs.getString("icons"));
                categories.add(category);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}