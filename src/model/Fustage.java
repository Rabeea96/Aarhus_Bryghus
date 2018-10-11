package model;

public class Fustage {

    private int liter;
    private String navn;

    public Fustage(int liter, String navn) {
        this.liter = liter;
        this.navn = navn;
    }

    public int getLiter() {
        return liter;
    }

    public void setLiter(int liter) {
        this.liter = liter;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

}
