import io.javalin.Javalin;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create().start(7000);

        // GET 1 - tutti i dipendenti
        app.get("/dipendenti", ctx -> {
            Connection c = ConnessioneDB.getConnection();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM dipendenti");

            List<Dipendente> lista = new ArrayList<>();
            while (rs.next()) {
                Dipendente d = new Dipendente();
                d.id = rs.getInt("id");
                d.nome = rs.getString("nome");
                d.ruolo_id = rs.getInt("ruolo_id");
                lista.add(d);
            }
            ctx.json(lista);
            c.close();
        });

        // GET 2 - tutti i gusti
        app.get("/gusti", ctx -> {
            Connection c = ConnessioneDB.getConnection();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM gusti");

            List<Gusto> lista = new ArrayList<>();
            while (rs.next()) {
                Gusto g = new Gusto();
                g.id = rs.getInt("id");
                g.nome = rs.getString("nome");
                g.prezzo = rs.getDouble("prezzo");
                lista.add(g);
            }
            ctx.json(lista);
            c.close();
        });

        // GET 3 - gusti ordinati per prezzo
        app.get("/gusti-ordinati", ctx -> {
            Connection c = ConnessioneDB.getConnection();
            ResultSet rs = c.createStatement()
                    .executeQuery("SELECT * FROM gusti ORDER BY prezzo");

            List<Gusto> lista = new ArrayList<>();
            while (rs.next()) {
                Gusto g = new Gusto();
                g.id = rs.getInt("id");
                g.nome = rs.getString("nome");
                g.prezzo = rs.getDouble("prezzo");
                lista.add(g);
            }
            ctx.json(lista);
            c.close();
        });

        // GET 4 - filtro vendite per dipendente
        app.get("/vendite", ctx -> {
            String id = ctx.queryParam("dipendente_id");

            Connection c = ConnessioneDB.getConnection();
            String query = "SELECT * FROM vendite";

            if (id != null) {
                query += " WHERE dipendente_id=" + id;
            }

            ResultSet rs = c.createStatement().executeQuery(query);

            List<Vendita> lista = new ArrayList<>();
            while (rs.next()) {
                Vendita v = new Vendita();
                v.id = rs.getInt("id");
                v.gusto_id = rs.getInt("gusto_id");
                v.dipendente_id = rs.getInt("dipendente_id");
                v.quantita = rs.getInt("quantita");
                v.data = rs.getString("data");
                lista.add(v);
            }
            ctx.json(lista);
            c.close();
        });

        // GET 5 - turni
        app.get("/turni", ctx -> {
            Connection c = ConnessioneDB.getConnection();
            ResultSet rs = c.createStatement().executeQuery("SELECT * FROM turni");

            List<Map<String,Object>> lista = new ArrayList<>();
            while (rs.next()) {
                Map<String,Object> t = new HashMap<>();
                t.put("id", rs.getInt("id"));
                t.put("data", rs.getString("data"));
                t.put("dipendente_id", rs.getInt("dipendente_id"));
                lista.add(t);
            }
            ctx.json(lista);
            c.close();
        });

        // POST 1 - inserimento gusto
        app.post("/aggiungi-gusto", ctx -> {
            Gusto g = ctx.bodyAsClass(Gusto.class);

            Connection c = ConnessioneDB.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO gusti(nome, prezzo) VALUES (?,?)");

            ps.setString(1, g.nome);
            ps.setDouble(2, g.prezzo);
            ps.executeUpdate();

            c.close();
            ctx.result("Inserito");
        });

        // POST 2 - eliminazione gusto
        app.post("/elimina-gusto", ctx -> {
            int id = Integer.parseInt(ctx.body());

            Connection c = ConnessioneDB.getConnection();
            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM gusti WHERE id=?");

            ps.setInt(1, id);
            ps.executeUpdate();

            c.close();
            ctx.result("Eliminato");
        });
    }
}