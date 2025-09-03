package design_pattern.factory_method;

public class ExpressShipping implements Shipping{
    @Override
    public double calculateCost(double weight) {
        return 10 + weight * 8;
    }

    @Override
    public void deliver(String packageId) {
        System.out.println("Express delivering " + packageId);
    }
}
