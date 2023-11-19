package com.petlover.petsocial.controller;

import com.petlover.petsocial.exception.UserException;
import com.petlover.petsocial.payload.request.OrderDTO;
import com.petlover.petsocial.service.PaypalService;
import com.petlover.petsocial.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@CrossOrigin()
@RequestMapping("/paypal")
public class PaypalController {
    @Autowired
    UserService userService;

    @Autowired
    PaypalService service;

    @Value("${baseurl}")
    private String baseurl;

    @PostMapping("/pay")
    public ResponseEntity<?> payment(@RequestHeader("Authorization") String jwt, @RequestBody OrderDTO order, HttpServletResponse response) {
        try {
            System.out.println("request jwt: " + jwt);
            Payment payment = service.createPayment(order.getPrice(), order.getDescription());
            // Store the JWT token associated with the payment ID
            String paymentId = payment.getId();
            userService.storeJwtToken(paymentId, jwt);
            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .map(Links::getHref)
                    .orElse(null);

            if (approvalUrl != null) {
                return ResponseEntity.ok(approvalUrl);
            } else {
                return ResponseEntity.ok(payment);
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public RedirectView cancelPay() {
        return new RedirectView("http://localhost:8080/payment?success=false");
    }

    @GetMapping("/success")
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId,
                                   @RequestParam("PayerID") String payerId,
                                   HttpServletRequest request) {
        try {
            // Retrieve the JWT token associated with the payment ID
            String jwt = userService.retrieveJwtToken(paymentId);
            System.out.println("success jwt: " + jwt);
            Payment payment = service.executePayment(paymentId, payerId);
            // Update the user's balance
            BigDecimal amount = new BigDecimal(payment.getTransactions().get(0).getAmount().getTotal());
            userService.updateBalance(jwt, amount);
            return ResponseEntity.ok("Payment executed successfully.");
        } catch (PayPalRESTException | UserException e) {
            return ResponseEntity.ok("Payment executed failed for some reason.");
        }
    }
}