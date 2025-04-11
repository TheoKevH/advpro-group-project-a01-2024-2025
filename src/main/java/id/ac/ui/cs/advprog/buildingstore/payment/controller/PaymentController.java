package id.ac.ui.cs.advprog.buildingstore.payment.controller;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @GetMapping("/create")
    public String createPaymentPage(Model model) {
        Payment payment = new Payment();
        model.addAttribute("payment", payment);
        return "createPayment";
    }
    
    @PostMapping("/create")
    public String createPaymentPost(@ModelAttribute Payment payment, Model model) {
        service.create(payment);
        return "redirect:list";
    }

    @PostMapping("/delete")
    public String deletePaymentPost(String paymentName, Model model) {
        service.delete(paymentName);
        return "redirect:list";
    }

    @GetMapping("/edit")
    public String editPaymentPage(Model model) {
        Payment payment = new Payment();
        model.addAttribute("payment", payment);
        return "editPayment";
    }

    @PostMapping("/edit")
    public String editPaymentPost(@ModelAttribute Payment payment, Model model) {
        service.create(payment);
        return "redirect:list";
    }

    @PostMapping("/edit-delete")
    public String editDeletePaymentPost(String paymentName, @ModelAttribute Payment payment, Model model) {
        service.delete(paymentName);
        return "redirect:edit";
    }

    @GetMapping("/list")
    public String paymentListPage(Model model) {
        List<Payment> allPayments = service.findAll();
        model.addAttribute("payments", allPayments);
        return "paymentList";
    }
}