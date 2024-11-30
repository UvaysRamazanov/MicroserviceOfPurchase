package purchase.microserviceofpurchase.listener.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MessageOrder implements Serializable {
    private long id;
    private long price;
}

