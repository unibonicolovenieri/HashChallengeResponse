import java.io.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class CryptoUtil {

    private static final String NONCE_FILE = "nonce_used.txt";

    // Genera un nonce sicuro, controlla che non sia già usato nel file
    public static String generateNonce() {
        ensureNonceFileExists();
        Set<String> usedNonces = loadUsedNonces();

        String nonce;
        do {
            byte[] nonceBytes = new byte[16];
            new SecureRandom().nextBytes(nonceBytes);
            nonce = Base64.getEncoder().encodeToString(nonceBytes);
        } while (usedNonces.contains(nonce));

        // Scrivilo nel file
        try (FileWriter fw = new FileWriter(NONCE_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(nonce);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del nonce nel file: " + e.getMessage());
        }

        return nonce;
    }

    // Calcola hash SHA-256 in base64
    public static String hashSHA256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    // Crea il file se non esiste
    private static void ensureNonceFileExists() {
        File file = new File(NONCE_FILE);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file dei nonce: " + e.getMessage());
        }
    }

    // Carica tutti i nonce usati dal file in un Set
    private static Set<String> loadUsedNonces() {
        Set<String> nonces = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(NONCE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                nonces.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file dei nonce: " + e.getMessage());
        }
        return nonces;
    }

    // resetta i nonce dal file
    public static void resetNonceFile() {
        try (PrintWriter writer = new PrintWriter(NONCE_FILE)) {
            writer.print(""); // cancella tutto
        } catch (FileNotFoundException e) {
            System.err.println("Errore nel reset del file dei nonce: " + e.getMessage());
        }
    }
}
