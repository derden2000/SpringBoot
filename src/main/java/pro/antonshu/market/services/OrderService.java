package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).get();
    }

    public List<Order> findAllOrdersByUserId(Long id) {
        return orderRepository.findAllByUserId(id);
    }

    public Order setOrderPaymentStatusTrue(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setPaymentStatus(true);
        orderRepository.save(order);
        return order;
    }
}
