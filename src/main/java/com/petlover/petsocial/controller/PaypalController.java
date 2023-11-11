package com.petlover.petsocial.controller;

import com.petlover.petsocial.payload.request.OrderDTO;
import com.petlover.petsocial.service.PaypalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@CrossOrigin()
@RequestMapping("/paypal")
public class PaypalController {

    @Autowired
    PaypalService service;

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestBody OrderDTO order) {
        try {
            Payment payment = service.createPayment(order.getPrice(),order.getDescription());

            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .header("Location", link.getHref())
                            .build();
                }
            }

            return ResponseEntity.ok(payment);

        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPay() {
        return ResponseEntity.ok("cancel");
    }

    @GetMapping("/success")
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId,
                                        @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            return ResponseEntity.ok(payment);
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}