package design_pattern.factory_method;

public class InternationalShipping implements Shipping{
    @Override
    public double calculateCost(double weight) {
        return 0;
    }

    @Override
    public void deliver(String packageId) {

    }
}
