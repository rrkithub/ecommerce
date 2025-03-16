package com.rkit.e.commerce.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class PaypalService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;



    private APIContext getAPIContext() {
        return new APIContext(clientId, clientSecret, mode);
    }

    public Payment createAndExecutePayment(Double total, String currency, String description, String userName) {
        APIContext apiContext = new APIContext(clientId, clientSecret, mode);

        // 🔹 Set Payment Method
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setPayerId(userName);
        payer.setPayerInfo(payerInfo);

        // 🔹 Set Transaction Amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        // 🔹 Set Transaction Details
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        // 🔹 Add Redirect URLs (Important ✅)
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:3000/payment/cancel");
        redirectUrls.setReturnUrl("http://localhost:3000/payment/success");

        // 🔹 Set Payment Object
        Payment payment = new Payment();
        payment.setIntent("sale"); // Sale means immediate capture
        payment.setPayer(payer);

        payment.setTransactions(Arrays.asList(transaction));
        payment.setRedirectUrls(redirectUrls);  // ✅ Required

        try {
            // 🔹 Create Payment (Calls PayPal API)
            Payment createdPayment = payment.create(apiContext);

            // 🔹 Execute Payment (Complete the transaction)
            for (Links link : createdPayment.getLinks()) {
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    // User should be redirected to this URL for approval
                    System.out.println("Redirect to PayPal: " + link.getHref());
                }
            }
            return createdPayment;
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return null;
    }


}
