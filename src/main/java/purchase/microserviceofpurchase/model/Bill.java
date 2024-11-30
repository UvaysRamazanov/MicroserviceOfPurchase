package purchase.microserviceofpurchase.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bill")
@Getter
@Setter
@NoArgsConstructor
public class Bill {

    @Id
    @Column(name = "order_id")
    private long orderId;

    @Column
    @Enumerated(EnumType.STRING)
    private CardStatus cardAuthorizationInfo = CardStatus.UNAUTHORIZED;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status = Status.COLLECTED;

    @Column
    private long price;

    // Конструктор, принимающий только orderId и price
    @JsonCreator
    public Bill(long orderId, long price) {
        this.orderId = orderId;
        this.price = price;
    }
}