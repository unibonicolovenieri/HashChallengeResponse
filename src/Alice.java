public class Alice {
    private final String identity = "Alice";
    private final String sharedSecret;
    private String nonce;
    private String receivedNonce;

    public Alice(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getIdentity() {
        return identity;
    }

    public String generateNonce() {
        this.nonce = CryptoUtil.generateNonce();
        return this.nonce;
    }

    public void setReceivedNonce(String nonceB) {
        this.receivedNonce = nonceB;
    }

    public String generateResponse(String bobIdentity) throws Exception {
        String data = nonce + receivedNonce + bobIdentity + sharedSecret;
        return CryptoUtil.hashSHA256(data);
    }

    public boolean verifyResponse(String bobIdentity, String hashReceived) throws Exception {
        String data = nonce + receivedNonce + identity + sharedSecret;
        String expectedHash = CryptoUtil.hashSHA256(data);
        return expectedHash.equals(hashReceived);
    }
}
