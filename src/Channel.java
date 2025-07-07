import java.util.LinkedList;
import java.util.Queue;

public class Channel {
    private final Queue<String> messages = new LinkedList<>();

    //USO UN ARRAY PER SIMULARE UN CANALE DI COMUNICAZIONE TRA LE DUE ENTITA NON SICURO

    // Inserisco messaggio nel canale
    public void send(String message) {
        messages.offer(message);
    }

    // Prelevo messaggi dal canale
    public String receive() {
        return messages.poll(); // ritorna null se vuoto, FIFO
    }

    // Controlla se ci sono messaggi in attesa
    public boolean hasMessages() {
        return !messages.isEmpty();
    }
}
