package purchase.microserviceofpurchase.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("localhost");
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean
    public Queue addCollectedOrderQueue() {
        return new Queue("addOrder");
    }

    @Bean
    public Queue cancelToItemQueue() {
        return new Queue("cancelToItem");
    }

    @Bean
    public Queue cancelToOrderQueue() {
        return new Queue("cancelToOrder");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("purchaseExchange");
    }

    @Bean
    public Binding bindingAddOrder(){
        return BindingBuilder.bind(addCollectedOrderQueue()).to(directExchange()).with("add");
    }

    @Bean
    public Binding bindingCancelToItem(){
        return BindingBuilder.bind(cancelToItemQueue()).to(directExchange()).with("cancelItem");
    }

    @Bean
    public Binding bindingCancelToOrder(){
        return BindingBuilder.bind(cancelToOrderQueue()).to(directExchange()).with("cancelOrder");
    }
}
