package model;

import java.time.LocalDate;

public class Fadølsanlæg_udlejning {

    private double pant;
    private LocalDate startDato;
    private LocalDate slutDato;

    public Fadølsanlæg_udlejning(double pant, LocalDate startDato, LocalDate slutDato) {
        this.pant = pant;
        this.startDato = startDato;
        this.slutDato = slutDato;
    }

    public double getPant() {
        return pant;
    }

    public void setPant(double pant) {
        this.pant = pant;
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

}
