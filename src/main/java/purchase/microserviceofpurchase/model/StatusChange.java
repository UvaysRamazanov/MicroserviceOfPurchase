package purchase.microserviceofpurchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusChange {
    private long orderId;
    private String status;
}