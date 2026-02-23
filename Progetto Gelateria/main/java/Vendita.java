public class Vendita {

    private int id;
    private int gusto_id;
    private int dipendente_id;
    private int quantita;
    private String data;

    public Vendita() {}

    public Vendita(int id, int gusto_id, int dipendente_id, int quantita, String data) {
        this.id = id;
        this.gusto_id = gusto_id;
        this.dipendente_id = dipendente_id;
        this.quantita = quantita;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGusto_id() {
        return gusto_id;
    }

    public void setGusto_id(int gusto_id) {
        this.gusto_id = gusto_id;
    }

    public int getDipendente_id() {
        return dipendente_id;
    }

    public void setDipendente_id(int dipendente_id) {
        this.dipendente_id = dipendente_id;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}