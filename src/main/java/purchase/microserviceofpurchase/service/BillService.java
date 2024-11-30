package purchase.microserviceofpurchase.service;

import purchase.microserviceofpurchase.model.Bill;
import purchase.microserviceofpurchase.model.CardStatus;
import purchase.microserviceofpurchase.model.Status;

import java.util.List;

public interface BillService {
    Bill addBill(Bill bill);

    Bill getBillById(long id);

    List<Bill> getBills();

    Bill purchase(CardStatus status, long id);

    Bill changeStatus(Status status, long id);
}