package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Product;
import pro.antonshu.market.repositories.ProductRepository;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(Specification<Product> specification, PageRequest pageRequest) {
        return productRepository.findAll(specification, pageRequest);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).get();
    }
}
