package pro.antonshu.market.services.payments;

public class OrderDetail {

    private String orderId;
    private float subtotal;
    private float shipping;
    private float tax;
    private float total;

    public OrderDetail(String orderId, String subtotal, String shipping, String tax, String total) {
        this.orderId = orderId;
        this.subtotal = Float.parseFloat(subtotal);
        this.shipping = Float.parseFloat(shipping);
        this.tax = Float.parseFloat(tax);
        this.total = Float.parseFloat(total);
    }

    public String getOrderId() {
        return String.valueOf(orderId);
    }

    public String getSubtotal() {
        return String.format("%.2f", subtotal).replace(',', '.');
    }

    public String getShipping() {
        return String.format("%.2f", shipping).replace(',', '.');
    }

    public String getTax() {
        return String.format("%.2f", tax).replace(',', '.');
    }

    public String getTotal() {
        return String.format("%.2f", tax + shipping + subtotal).replace(',', '.');
    }
}
