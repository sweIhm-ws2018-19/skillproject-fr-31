public class Adresse {
    private String strasse;
    private int hausnummer;
    private String plz;
    private String ort;
    private Kunde[] kunden;

    public Adresse(String strasse, int hausnummer, String plz, String ort, Kunde[] kunden) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.kunden = kunden;
    }

    public String getStrasse() {
        return strasse;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public Kunde[] getKunden() {
        return kunden;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public void setHausnummer(int hausnummer) {
        this.hausnummer = hausnummer;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setKunden(Kunde[] kunden) {
        this.kunden = kunden;
    }
}
