package id.ac.ui.cs.advprog.buildingstore.service;

import id.ac.ui.cs.advprog.buildingstore.model.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    Payment create(Payment payment);

    void delete(String paymentName);

    List<Payment> findAll();
}
