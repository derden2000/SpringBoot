package pro.antonshu.market.controllers;

import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.antonshu.market.services.OrderService;
import pro.antonshu.market.services.payments.OrderDetail;
import pro.antonshu.market.services.payments.PayPalService;

@Controller
public class PaymentController {

    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/paypal/authorize_payment")
    private String executePayment(@RequestParam(name = "orderId") Long orderId,
                                  @RequestParam(name = "amount") String amount) throws PayPalRESTException {
        OrderDetail orderDetail = new OrderDetail(orderId, amount);

        PayPalService payPalService = new PayPalService();
        String approvalLink = payPalService.authorizePayment(orderDetail);

        return "redirect:" + approvalLink;
    }

    @GetMapping("/paypalReturn")
    private String getPaymentResult(@RequestParam(name = "paymentId") String paymentId,
                                    @RequestParam(name = "PayerID") String payerId, Model model) throws PayPalRESTException {

        PayPalService payPalService = new PayPalService();
        Payment payment = payPalService.getPaymentDetails(paymentId);

        PayerInfo payerInfo = payment.getPayer().getPayerInfo();
        Transaction transaction = payment.getTransactions().get(0);
        ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
        System.out.println("OrderID: " + payment.getTransactions().get(0).getDescription());

        model.addAttribute("paymentId", paymentId);
        model.addAttribute("PayerID", payerId);
        model.addAttribute(transaction);
        model.addAttribute(payerInfo);
        model.addAttribute(shippingAddress);
        model.addAttribute("OrderID", payment.getTransactions().get(0).getDescription());
        return "paypal_review";
    }

    @PostMapping("/execute_payment")
    private String executePayment(@RequestParam(name = "paymentId") String paymentId,
                                  @RequestParam(name = "PayerID") String payerId,
                                  @RequestParam(name = "OrderID") String orderId, Model model) throws PayPalRESTException {

        PayPalService paymentServices = new PayPalService();
        Payment payment = paymentServices.executePayment(paymentId, payerId);

        orderService.setOrderPaymentStatusTrue(Long.valueOf(orderId));

        PayerInfo payerInfo = payment.getPayer().getPayerInfo();
        Transaction transaction = payment.getTransactions().get(0);
        model.addAttribute(payerInfo);
        model.addAttribute(transaction);
        return "paypal_receipt";
    }
}
