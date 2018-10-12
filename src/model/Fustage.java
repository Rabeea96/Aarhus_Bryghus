package model;

public class Fustage {

    private int liter;
    private String navn;
    private int pris;

    public Fustage(int liter, String navn, int pris) {
        this.liter = liter;
        this.navn = navn;
        this.pris = pris;
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

    public int getPris() {
        return pris;
    }

    public void setPris(int pris) {
        this.pris = pris;
    }

}
