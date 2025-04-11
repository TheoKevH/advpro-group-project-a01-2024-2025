package id.ac.ui.cs.advprog.buildingstore.repository;

import id.ac.ui.cs.advprog.buildingstore.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class PaymentRepository {
    private final List<Payment> paymentData = new ArrayList<>();

    public Payment create(Payment payment) {
        paymentData.add(payment);
        return payment;
    }

    public void delete(String paymentName) {
        for (int i = 0; i < paymentData.size(); i++) {
            if (paymentData.get(i).getPaymentName().equals(paymentName)) {
                paymentData.remove(i);
                break;
            }
        }
    }

    public Iterator<Payment> findAll() {
        return paymentData.iterator();
    }
}
