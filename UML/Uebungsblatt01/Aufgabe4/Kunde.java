public abstract class Kunde {
    private Konto[] konten;

    public void setKonten(Konto[] konten) {
        this.konten = konten;
    }

    public Kunde(Konto[] konten) {
        this.konten = konten;
    }

    public Konto[] getKonten() {
        return konten;
    }
}
