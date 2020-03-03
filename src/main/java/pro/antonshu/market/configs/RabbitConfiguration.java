package pro.antonshu.market.configs;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.antonshu.market.services.rabbitmq.Receiver;

@Configuration
public class RabbitConfiguration {

	public static String topicExchangerName = "topicExchanger";

	private String hostName, rawQueueName, processedQueueName, routingKey;
    private String userName, password;

    public static void setTopicExchangerName(String topicExchangerName) {
        RabbitConfiguration.topicExchangerName = topicExchangerName;
    }

    @Value("${rabbitmq.hostName}")
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Value("${rabbitmq.rawQueueName}")
    public void setRawQueueName(String rawQueueName) {
        this.rawQueueName = rawQueueName;
    }

    @Value("${rabbitmq.processedQueueName}")
    public void setProcessedQueueName(String processedQueueName) {
        this.processedQueueName = processedQueueName;
    }

    @Value("${rabbitmq.routingKey}")
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Value("${rabbitmq.userName}")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Value("${rabbitmq.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(hostName);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }


	@Bean
    Queue queueTopic1() {
		return new Queue(rawQueueName, true, false, true);
	}

	@Bean
    Queue queueTopic2() {
		return new Queue(processedQueueName, true, false, true);
	}

	@Bean
    TopicExchange topicExchange() {
		return new TopicExchange(topicExchangerName);
	}

	@Bean
    Binding bindingTopic1(@Qualifier("queueTopic1") Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
	}

	@Bean
    SimpleMessageListenerContainer containerForTopic(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(processedQueueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
}
