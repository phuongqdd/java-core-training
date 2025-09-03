package design_pattern.factory_method;

public class StandardShipping implements Shipping{
    @Override
    public double calculateCost(double weight) {
        return weight * 5;
    }

    @Override
    public void deliver(String packageId) {
        System.out.println("Standard delivering " + packageId);
    }
}
