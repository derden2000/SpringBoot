package pro.antonshu.market.services.rabbitmq;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.antonshu.market.dto.OrderDto;
import pro.antonshu.market.entities.Order;
import pro.antonshu.market.services.OrderService;

@Component
public class Receiver {

    private RabbitTemplate rabbitTemplate;

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void receiveMessage(String message) {
        Gson gson = new Gson();
        OrderDto orderDto = gson.fromJson(message, OrderDto.class);
        Order orderToChange = orderService.findOrderById(orderDto.getId());
        if (orderToChange != null) {
            orderToChange.setCompleteStatus(orderDto.isCompleteStatus());
            orderService.saveOrder(orderToChange);
            System.out.println("Changes was saved");
        }
    }
}
