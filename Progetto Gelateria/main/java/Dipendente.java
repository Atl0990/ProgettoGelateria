public class Dipendente {

    private int id;
    private String nome;
    private int ruolo_id;

    public Dipendente() {}

    public Dipendente(int id, String nome, int ruolo_id) {
        this.id = id;
        this.nome = nome;
        this.ruolo_id = ruolo_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getRuolo_id() {
        return ruolo_id;
    }

    public void setRuolo_id(int ruolo_id) {
        this.ruolo_id = ruolo_id;
    }
}