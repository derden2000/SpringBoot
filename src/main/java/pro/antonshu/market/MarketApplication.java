package pro.antonshu.market;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pro.antonshu.market.services.rabbitmq.Receiver;

@SpringBootApplication
public class MarketApplication {
	public static final String TOPIC_EXCHANGER_NAME = "topicExchanger";

//	@Bean
//	Queue queueTopic1() {
//		return new Queue("Orders", false, false, true);
//	}
//
//	@Bean
//	TopicExchange topicExchange() {
//		return new TopicExchange(TOPIC_EXCHANGER_NAME);
//	}
//
//	@Bean
//	Binding bindingTopic1(@Qualifier("queueTopic1") Queue queue, TopicExchange topicExchange) {
//		return BindingBuilder.bind(queue).to(topicExchange).with("processed");
//	}
//
//	@Bean
//	SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		container.setQueueNames("Orders");
//		container.setMessageListener(listenerAdapter);
//		return container;
//	}
//
//	@Bean
//	MessageListenerAdapter listenerAdapter(Receiver receiver) {
//		return new MessageListenerAdapter(receiver, "receiveMessage");
//	}

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

}
