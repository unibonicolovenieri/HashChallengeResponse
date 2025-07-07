public class Main {
    public static void main(String[] args) throws Exception {
        String presharedSecret = "a7S@9F2&zL#cP!xW0R=qTg5)UDV@jX3zA^M8eBnK!zF*upwLd#9RkL1vQpGsG!rT";

        Alice alice = new Alice(presharedSecret);
        Bob bob = new Bob(presharedSecret);
        Channel channel = new Channel();

        // Step 1: Bob → Alice
        String nonceB = bob.generateNonce();
        channel.send("CHALLENGE||" + nonceB);
        System.out.println("Bob → Alice: CHALLENGE " + nonceB);

        // Step 2: Alice → Bob
        String received = channel.receive();
        String nonceB_fromBob = received.split("\\|\\|")[1];
        String nonceA = alice.generateNonce();
        String responseAlice = alice.generateResponse(nonceA, nonceB_fromBob, bob.getIdentity());
        channel.send("RESPONSE_A||" + responseAlice);
        System.out.println("Alice → Bob: RESPONSE_A " + responseAlice);

        // Step 3: Bob verifica Alice
        String[] responseParts = channel.receive().split("\\|\\|", 2)[1].split("\\|\\|");
        String nonceA_received = responseParts[0];
        String nonceB_received = responseParts[1];
        String claimedAlice = responseParts[2];
        String hashFromAlice = responseParts[3];

        boolean aliceOK = bob.verifyResponse(nonceA_received, nonceB_received, claimedAlice, hashFromAlice);
        System.out.println("Bob verifica Alice: " + (aliceOK ? "OK" : "FALLITO"));

        // Step 4: Bob → Alice
        String responseBob = bob.generateResponse(nonceA_received, nonceB_received, claimedAlice);
        channel.send("RESPONSE_B||" + responseBob);
        System.out.println("Bob → Alice: RESPONSE_B " + responseBob);

        // Step 5: Alice verifica Bob
        String hashFromBob = channel.receive().split("\\|\\|")[1];
        boolean bobOK = alice.verifyResponse(nonceA_received, nonceB_received, hashFromBob, bob.getIdentity());
        System.out.println("Alice verifica Bob: " + (bobOK ? "OK" : "FALLITO"));
    }
}
