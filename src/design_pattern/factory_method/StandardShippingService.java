package design_pattern.factory_method;

public class StandardShippingService extends ShippingService{
    @Override
    protected Shipping createShipping() {
        return new StandardShipping();
    }
}
