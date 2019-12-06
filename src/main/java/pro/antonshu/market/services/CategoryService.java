package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Category;
import pro.antonshu.market.repositories.CategoryRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Map<Long, String> getAllCategories() {
        Map<Long, String> cats = categoryRepository.findAll().stream().collect(Collectors.toMap(Category::getId, Category::getTitle));
        return cats;
    }
}
