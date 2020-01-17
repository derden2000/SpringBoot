package pro.antonshu.market.services.payments;

import java.text.DecimalFormat;

public class OrderDetail {

    private Long orderId;
    private String subtotal;
    private String shipping;
    private String tax;
    private String total;

    public OrderDetail(Long orderId, String amount) {
        this.orderId = orderId;
        this.subtotal = new DecimalFormat("0.00").format(Double.parseDouble(amount) / 1.2).replace(',', '.');
        this.shipping = new DecimalFormat("0.00").format(500 / 1.2).replace(',', '.');
        this.total = new DecimalFormat("0.00").format(Double.parseDouble(amount) + 500).replace(',', '.');
        double tax = Double.parseDouble(total) - Double.parseDouble(subtotal) - Double.parseDouble(shipping);
        this.tax = new DecimalFormat("0.00").format(tax).replace(',', '.');
    }

    public String getOrderId() {
        return String.valueOf(orderId);
    }

    public String getSubtotal() {
        return subtotal;
    }

    public String getShipping() {
        return shipping;
    }

    public String getTax() {
        return tax;
    }

    public String getTotal() {
        return total;
    }
}
