package design_pattern.factory_method;

public class ExpressShippingService extends ShippingService{
    @Override
    protected Shipping createShipping() {
        return new ExpressShipping();
    }
}
