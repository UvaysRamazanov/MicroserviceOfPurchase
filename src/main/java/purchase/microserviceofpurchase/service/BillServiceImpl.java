package purchase.microserviceofpurchase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import purchase.microserviceofpurchase.model.Bill;
import purchase.microserviceofpurchase.model.CardStatus;
import purchase.microserviceofpurchase.model.Status;
import purchase.microserviceofpurchase.repository.BillRepository;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public Bill addBill(Bill order) {
        return billRepository.save(order);
    }

    @Override
    public Bill getBillById(long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No bill with id : " + id));
    }

    @Override
    public List<Bill> getBills() {
        return billRepository.findAll();
    }

    @Override
    public Bill purchase(CardStatus cardStatus, long id) {
        Bill bill = getBillById(id);

        if (bill.getStatus() == Status.COLLECTED) {
            bill.setStatus(cardStatus == CardStatus.AUTHORIZED ? Status.PAID : Status.FAILED);
            bill.setCardAuthorizationInfo(cardStatus);
        }

        billRepository.save(bill);
        return bill;
    }

    @Override
    public Bill changeStatus(Status status, long id) {
        Bill bill = getBillById(id);

        switch (status) {
            case CANCELLED:
                if (bill.getStatus() == Status.PAID || bill.getStatus() == Status.COLLECTED) {
                    bill.setStatus(status);
                }
                break;
            case SHIPPING:
                if (bill.getStatus() == Status.PAID) {
                    bill.setStatus(status);
                }
                break;
            case COMPLETE:
                if (bill.getStatus() == Status.SHIPPING) {
                    bill.setStatus(status);
                }
                break;
        }

        billRepository.save(bill);

        return bill;
    }
}
