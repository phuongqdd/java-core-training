package design_pattern.factory_method;

public class InternationalShippingService extends ShippingService{
    @Override
    protected Shipping createShipping() {
        return new InternationalShipping();
    }
}
