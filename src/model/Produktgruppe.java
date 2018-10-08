package model;

public class Produktgruppe {

    private String navn;

    public Produktgruppe(String navn) {
        this.navn = navn;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }
}
