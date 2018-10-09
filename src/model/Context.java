package model;

public class Context {

    private Strategy_giv_rabat strategy;

    public Context(Strategy_giv_rabat strategy) {
        this.strategy = strategy;
    }

    public double executeStrategy(double rabat, Ordre ordre) {
        return strategy.giv_rabat(rabat, ordre);
    }

}
