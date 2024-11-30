package purchase.microserviceofpurchase.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import purchase.microserviceofpurchase.listener.data.MessageOrder;
import purchase.microserviceofpurchase.model.Bill;
import purchase.microserviceofpurchase.repository.BillRepository;

@Component
public class RabbitMqListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final BillRepository repository;

    @Autowired
    public RabbitMqListener(BillRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "addOrder")
    public void addOrder(MessageOrder order) {
        repository.save(new Bill(order.getId(), order.getPrice()));
    }
}

