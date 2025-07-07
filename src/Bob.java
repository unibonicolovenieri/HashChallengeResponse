public class Bob {
    private final String identity = "Bob";
    private final String sharedSecret;
    private String nonce;
    private String receivedNonce;

    public Bob(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getIdentity() {
        return identity;
    }

    public String generateNonce() {
        this.nonce = CryptoUtil.generateNonce();
        return this.nonce;
    }

    public void setReceivedNonce(String nonceA) {
        this.receivedNonce = nonceA;
    }

    public String generateResponse(String aliceIdentity) throws Exception {
        String data = receivedNonce + nonce + aliceIdentity + sharedSecret;
        return CryptoUtil.hashSHA256(data);
    }

    public boolean verifyResponse(String claimedAlice, String hashReceived) throws Exception {
        String data = receivedNonce + nonce + identity + sharedSecret;
        String expectedHash = CryptoUtil.hashSHA256(data);
        return expectedHash.equals(hashReceived);
    }
}
