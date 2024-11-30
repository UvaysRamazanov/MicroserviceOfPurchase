package purchase.microserviceofpurchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import purchase.microserviceofpurchase.model.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {

}