package design_pattern.factory_method;

abstract class ShippingService {
    protected abstract Shipping createShipping();

    public void processOrder(String packageId, double weight){
        Shipping shipping = createShipping();
        double cost = shipping.calculateCost(weight);
        System.out.println("Cost for " + packageId + ": $" + cost);
        shipping.deliver(packageId);
    }
}
