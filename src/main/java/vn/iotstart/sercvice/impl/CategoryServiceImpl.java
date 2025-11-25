package vn.iotstart.sercvice.impl;

import java.io.File;
import java.util.List;

import vn.iotstart.dao.CategoryDao;
import vn.iotstart.dao.impl.CategoryDaoImpl;
import vn.iotstart.model.Category;
import vn.iotstart.sercvice.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    
<<<<<<< HEAD
    // Gọi đến tầng DAO để xử lý dữ liệu
=======
>>>>>>> origin/master
    CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public Category get(int id) {
        return categoryDao.get(id);
    }

    @Override
    public void insert(Category category) {
<<<<<<< HEAD
=======
        // categoryDao.insert đã được update để nhận cả user_id trong object category
>>>>>>> origin/master
        categoryDao.insert(category);
    }

    @Override
    public void edit(Category newCategory) {
        Category oldCategory = categoryDao.get(newCategory.getCateId());
<<<<<<< HEAD
=======
        
        // Cập nhật tên
>>>>>>> origin/master
        oldCategory.setCateName(newCategory.getCateName());
        
        // Logic xử lý ảnh: Nếu người dùng tải ảnh mới thì xóa ảnh cũ đi
        if (newCategory.getIcons() != null) {
            String fileName = oldCategory.getIcons();
<<<<<<< HEAD
            final String DIR = "C:\\upload"; //DuongDan
=======
            // Lưu ý: Đường dẫn này nên đưa vào file cấu hình (properties) thay vì hardcode
            final String DIR = "C:\\upload"; 
>>>>>>> origin/master
            
            File file = new File(DIR + File.separator + fileName);
            if (file.exists()) {
                file.delete(); // Xóa file cũ
            }
            oldCategory.setIcons(newCategory.getIcons());
        }
        
<<<<<<< HEAD
=======
        // Gọi DAO để update xuống DB
>>>>>>> origin/master
        categoryDao.edit(oldCategory);
    }

    @Override
    public void delete(int id) {
        categoryDao.delete(id);
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDao.search(keyword);
    }
<<<<<<< HEAD
=======

    // --- TRIỂN KHAI HÀM MỚI ---
    @Override
    public List<Category> findByCreatorId(int userId) {
        return categoryDao.findByCreatorId(userId);
    }
>>>>>>> origin/master
}