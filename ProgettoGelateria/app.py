from flask import Flask, request, jsonify, render_template
import mysql.connector

class GelateriaApp:
    def __init__(self):
        self.app = Flask(__name__)
        self.setup_routes()

    def run(self):
        self.app.run(debug=True)

    def setup_routes(self):
        # rotte per le pagine html
        @self.app.route('/')
        def index(): return render_template('index.html')
        @self.app.route('/inserisci')
        def inserisci(): return render_template('inserisci.html')
        @self.app.route('/elimina')
        def elimina(): return render_template('elimina.html')
        @self.app.route('/lavora_con_noi')
        def lavora_con_noi(): return render_template('lavora_con_noi.html')

        # api get per prendere i dati dal database
        @self.app.route('/api/gusti', methods=['GET'])
        def get_gusti(): 
            return jsonify(GustoDAO.get_all())

        @self.app.route('/api/dipendenti', methods=['GET'])
        def get_dipendenti(): 
            return jsonify(DipendenteDAO.get_all())
            
        @self.app.route('/api/ordini', methods=['GET'])
        def get_ordini(): 
            return jsonify(OrdineDAO.get_all())

        @self.app.route('/api/gusti/ordinati', methods=['GET'])
        def get_gusti_ordinati():
            #ordina i gusti alfabeticamente
            return jsonify(GustoDAO.get_all_ordered())

        @self.app.route('/api/dipendenti/filtro', methods=['GET'])
        def get_dipendenti_filtro():
            ruolo = request.args.get('ruolo', 'cassa')
            return jsonify(DipendenteDAO.get_by_role(ruolo))

        # api post per mandare i dati
        @self.app.route('/api/aggiungi_gusto', methods=['POST'])
        def add_gusto():
            nome = request.form['nome']
            quantita = request.form['quantita_kg']
            GustoDAO.insert(nome, quantita)
            return render_template('successo.html', messaggio="Gusto inserito")

        @self.app.route('/api/elimina_gusto', methods=['POST'])
        def delete_gusto():
            id_gusto = request.form['id']
            GustoDAO.delete(id_gusto)
            return render_template('successo.html', messaggio="Gusto eliminato")

        @self.app.route('/api/invia_cv', methods=['POST'])
        def invia_cv():
            nome = request.form['nome']
            email = request.form['email']
            posizione = request.form['posizione']
            note = request.form['note']
            CandidatoDAO.insert(nome, email, posizione, note)
            return render_template('successo.html', messaggio="Curriculum inviato. Fosba deciderà cosa farne di te.")

#classe per connettersi a xamp
class Database:
    @staticmethod
    def get_connection():
        #la psw vuota di defalt 
        return mysql.connector.connect(
            host="localhost", user="root", password="", database="gelateria"
        )

class GustoDAO:
    @staticmethod
    def get_all():
        conn = Database.get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM Gusto")
        return cursor.fetchall()

    @staticmethod
    def get_all_ordered():
        conn = Database.get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM Gusto ORDER BY nome ASC")
        return cursor.fetchall()
        
    @staticmethod
    def insert(nome, quantita):
        conn = Database.get_connection()
        cursor = conn.cursor()
        cursor.execute("INSERT INTO Gusto (nome, quantita_kg) VALUES (%s, %s)", (nome, quantita))
        conn.commit()

    @staticmethod
    def delete(id_gusto):
        conn = Database.get_connection()
        cursor = conn.cursor()
        cursor.execute("DELETE FROM Gusto WHERE id = %s", (id_gusto,))
        conn.commit()

class DipendenteDAO:
    @staticmethod
    def get_all():
        conn = Database.get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM Dipendente")
        return cursor.fetchall()

    @staticmethod
    def get_by_role(ruolo):
        conn = Database.get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM Dipendente WHERE ruolo = %s", (ruolo,))
        return cursor.fetchall()

class OrdineDAO:
    @staticmethod
    def get_all():
        conn = Database.get_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM Ordine")
        return cursor.fetchall()

class CandidatoDAO:
    @staticmethod
    def insert(nome, email, posizione, note):
        conn = Database.get_connection()
        cursor = conn.cursor()
        cursor.execute("INSERT INTO Candidatura (nome, email, posizione, note) VALUES (%s, %s, %s, %s)", (nome, email, posizione, note))
        conn.commit()

if __name__ == '__main__':
    app_principale = GelateriaApp()
    app_principale.run()