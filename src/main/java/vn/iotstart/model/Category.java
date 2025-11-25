package vn.iotstart.model;

import java.io.Serializable;
import java.util.List; // Nếu có list Video
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int cateId;

    @Column(name = "cate_name", columnDefinition = "NVARCHAR(200)")
    private String cateName;

    @Column(name = "icons")
    private String icons;

    // --- KẾT NỐI USER ---
    // Quan hệ N-1: Nhiều Category thuộc về 1 User (người tạo)
    @ManyToOne
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại trong bảng categories
    private User user;

    // Constructors
    public Category() { super(); }

    public Category(String cateName, String icons, User user) {
        this.cateName = cateName;
        this.icons = icons;
        this.user = user;
    }

    // Getters and Setters
    public int getCateId() { return cateId; }
    public void setCateId(int cateId) { this.cateId = cateId; }

    public String getCateName() { return cateName; }
    public void setCateName(String cateName) { this.cateName = cateName; }

    public String getIcons() { return icons; }
    public void setIcons(String icons) { this.icons = icons; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}