package purchase.microserviceofpurchase.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purchase.microserviceofpurchase.model.Bill;
import purchase.microserviceofpurchase.model.CardStatus;
import purchase.microserviceofpurchase.model.Status;
import purchase.microserviceofpurchase.model.StatusChange;
import purchase.microserviceofpurchase.service.BillService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/warehouse")
public class PurchaseController {

    private final BillService billService;
    private final RabbitTemplate template;

    @Autowired
    public PurchaseController(BillService billService, RabbitTemplate template) {
        this.billService = billService;
        this.template = template;
    }

    @GetMapping("/orders")
    public List<Bill> getBills() {
        List<Bill> bills = billService.getBills();
        return bills;
    }

    @PostMapping("/orders")
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill) {
        Bill createdBill = billService.addBill(bill);
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    @PutMapping("/orders/{order_id}/payment")
    public ResponseEntity<Bill> purchase(@PathVariable("order_id") long id, @RequestParam(name = "card") CardStatus cardStatus) {
        Bill bill = billService.purchase(cardStatus, id);
        setStatusChange(bill);

        if (bill.getStatus() == Status.FAILED) {
            template.setExchange("purchaseExchange");
            template.convertAndSend("cancelItem", bill.getOrderId());
            template.convertAndSend("cancelOrder", bill.getOrderId());
        }

        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    @PutMapping("/orders/{order_id}/status/{status}")
    public ResponseEntity<Bill> changeStatus(@PathVariable("order_id") long id, @PathVariable("status") Status status) {
        Bill bill = billService.changeStatus(status, id);

        if (bill.getStatus() == Status.CANCELLED) {
            template.setExchange("purchaseExchange");
            template.convertAndSend("cancelItem", bill.getOrderId());
            template.convertAndSend("cancelOrder", bill.getOrderId());
        }

        setStatusChange(bill);
        return new ResponseEntity<>(bill, HttpStatus.OK);
    }

    private void setStatusChange(Bill bill) {
        template.setExchange("orderExchange");
        template.convertAndSend("status", new StatusChange(bill.getOrderId(), bill.getStatus().toString()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}