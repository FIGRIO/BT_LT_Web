package vn.iotstart.sercvice;

import java.util.List;
import vn.iotstart.model.Category;

public interface CategoryService {
    List<Category> getAll();
    Category get(int id);
    void insert(Category category);
    void edit(Category category);
    void delete(int id);
    List<Category> search(String keyword);
    
    // --- BỔ SUNG MỚI ---
    // Tìm danh mục theo ID người tạo (cho chức năng Manager)
    List<Category> findByCreatorId(int userId);
}