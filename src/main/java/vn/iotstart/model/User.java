package vn.iotstart.model;

import java.io.Serializable;
<<<<<<< HEAD

@SuppressWarnings("serial")
public class User implements Serializable {

    // Tất cả các trường đã khớp với ảnh database
    private int userId;
    private String username;
    private String email;
    private String fullname;
    private String password;
    private String images; // Đã đổi từ 'avatar'
    private String phone;
    private int status;    // Đã thêm
    private String code;     // Đã thêm
    private int roleid;
    private int sellerid;  // Đã thêm

    // Constructors
    public User() {
        super();
    }

    // Constructor đầy đủ
    public User(int userId, String username, String email, String fullname, String password, String images, String phone,
            int status, String code, int roleid, int sellerid) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.images = images;
        this.phone = phone;
        this.status = status;
        this.code = code;
        this.roleid = roleid;
        this.sellerid = sellerid;
    }
    
    

    public User(String username, String fullname, String code) {
		super();
		this.username = username;
		this.fullname = fullname;
		this.code = code;
	}

	public User(String username, String email, String fullname, String password, int status, String code, int roleid) {
		super();
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.password = password;
		this.status = status;
		this.code = code;
		this.roleid = roleid;
	}
=======
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
>>>>>>> origin/master

	public User(String username, String email, String fullname, String code) {
		super();
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.code = code;
	}
<<<<<<< HEAD

	// Getters and Setters cho tất cả các trường
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public int getSellerid() {
        return sellerid;
    }

    public void setSellerid(int sellerid) {
        this.sellerid = sellerid;
    }

	
=======
>>>>>>> origin/master
}