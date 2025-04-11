package id.ac.ui.cs.advprog.buildingstore.payment.service;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment create(Payment payment) {
        paymentRepository.create(payment);
        return payment;
    }

    @Override
    public void delete(String paymentName) {
        paymentRepository.delete(paymentName);
    }

    @Override
    public List<Payment> findAll() {
        return List.of();
    }
}
