package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Discount;
import pro.antonshu.market.repositories.DiscountRepository;

import java.util.List;

@Service
public class DiscountService {

    private DiscountRepository discountRepository;

    @Autowired
    public void setDiscountRepository(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> findAll() {
        return discountRepository.findAll();
    }
}
