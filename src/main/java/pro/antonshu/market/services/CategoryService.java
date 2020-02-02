package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean existByTitle(String title) {
        return categoryRepository.existsByTitle(title);
    }

    public Category getCategoryByTitle(String title) {
        System.out.println("Title to find in CategoryService: " + title);
        return categoryRepository.findOneByTitle(title);
    }
}
