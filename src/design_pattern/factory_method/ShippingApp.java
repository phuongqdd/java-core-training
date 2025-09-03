package design_pattern.factory_method;

public class ShippingApp {
    public static void main(String[] args) {
        ShippingService service1 = new StandardShippingService();
        service1.processOrder("PKG001", 3.5);

        ShippingService service2 = new ExpressShippingService();
        service2.processOrder("PKG002", 2);

        ShippingService service3 = new InternationalShippingService();
        service3.processOrder("PKG003", 5);
    }
}
