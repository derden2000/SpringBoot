package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.entities.OrderItem;
import pro.antonshu.market.repositories.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void saveOrderItemList(List<OrderItem> orderItemList, Order order) {
        for (OrderItem item : orderItemList) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }
    }
}
