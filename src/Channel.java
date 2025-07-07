import java.util.LinkedList;
import java.util.Queue;

public class Channel {
    private final Queue<String> messages = new LinkedList<>();

    public void send(String message) {
        messages.offer(message);
    }

    public String receive() {
        return messages.poll();
    }

    public boolean hasMessages() {
        return !messages.isEmpty();
    }
}
