package design_pattern.factory_method;

public interface Shipping {
    double calculateCost(double weight);
    void deliver(String packageId);
}
