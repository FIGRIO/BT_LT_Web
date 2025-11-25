package vn.iotstart.model;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(200)")
    private String fullname;

    @Column(name = "password")
    private String password;

    @Column(name = "images")
    private String images;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private int status;

    @Column(name = "code")
    private String code;
    
    // Thay int sellerid bằng Integer để chấp nhận null (nếu cần)
    @Column(name = "seller_id")
    private Integer sellerid; 

    // --- KẾT NỐI ROLE ---
    // Quan hệ N-1: Nhiều User có thể cùng 1 Role
    @ManyToOne
    @JoinColumn(name = "role_id") // Tên cột khóa ngoại trong bảng users
    private Role role;

    // --- KẾT NỐI CATEGORY ---
    // Quan hệ 1-N: Một User (Manager) có thể tạo nhiều Category
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Category> categories;

    // Constructors
    public User() { super(); }

    // Constructor rút gọn (Ví dụ cho Đăng ký)
    public User(String username, String email, String fullname, String code, Role role) {
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.code = code;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public Integer getSellerid() { return sellerid; }
    public void setSellerid(Integer sellerid) { this.sellerid = sellerid; }

    // Getter/Setter cho Role
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Getter/Setter cho Categories
    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

	public User(String username, String email, String fullname, String code) {
		super();
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.code = code;
	}
}