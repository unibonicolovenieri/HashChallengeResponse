public class Main {
    public static void main(String[] args) throws Exception {
        String presharedSecret = "a7S@9F2&zL#cP!xW0R=qTg5)UDV@jX3zA^M8eBnK!zF*upwLd#9RkL1vQpGsG!rT";

        Alice alice = new Alice(presharedSecret);
        Bob bob = new Bob(presharedSecret);
        Channel channel = new Channel();

        System.out.println(
                """
                                                                                          \s
                                     C H A L L E N G E   ↔   R E S P O N S E   ↔   H A S H  \s
                                -------------------------------------------------------------
                        """
        );

        // Step 1: Bob → Alice
        String nonceB = bob.generateNonce();
        channel.send("CHALLENGE||" + nonceB);
        System.out.println("Bob → Alice: CHALLENGE " + nonceB);

        System.out.println("--------------------");
        Thread.sleep(1000);

        // Step 2: Alice → Bob
        String received = channel.receive();
        String nonceB_fromBob = received.split("\\|\\|")[1];
        alice.setReceivedNonce(nonceB_fromBob);

        String nonceA = alice.generateNonce();
        String responseAliceHash = alice.generateResponse(bob.getIdentity());
        channel.send("RESPONSE_A||" + nonceA + "||" + responseAliceHash);
        System.out.println("Alice → Bob: RESPONSE_A " + nonceA + " + " + responseAliceHash);

        System.out.println("--------------------");
        Thread.sleep(1000);

        // Step 3: Bob verifica Alice
        String[] responseParts = channel.receive().split("\\|\\|");
        String nonceA_received = responseParts[1];
        String hashFromAlice = responseParts[2];
        bob.setReceivedNonce(nonceA_received);

        boolean aliceOK = bob.verifyResponse("Alice", hashFromAlice);
        System.out.println("Bob verifica Alice: " + (aliceOK ? "OK" : "FALLITO"));

        System.out.println("--------------------");
        Thread.sleep(1000);

        // Step 4: Bob → Alice
        String responseBob = bob.generateResponse("Alice");
        channel.send("RESPONSE_B||" + responseBob);
        System.out.println("Bob → Alice: RESPONSE_B " + responseBob); //HASH INTERO INVIATO

        System.out.println("--------------------");
        Thread.sleep(1000);

        // Step 5: Alice verifica Bob
        String hashFromBob = channel.receive().split("\\|\\|")[1];
        boolean bobOK = alice.verifyResponse(bob.getIdentity(), hashFromBob);
        System.out.println("Alice verifica Bob: " + (bobOK ? "OK" : "FALLITO"));

        System.out.println("--------------------");
    }
}
