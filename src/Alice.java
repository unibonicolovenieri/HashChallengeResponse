public class Alice {
    private final String identity = "Alice";
    private final String sharedSecret;

    public Alice(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getIdentity() {
        return identity;
    }

    public String generateNonce() {
        return CryptoUtil.generateNonce();
    }

    public String generateResponse(String nonceA, String nonceB, String bobIdentity) throws Exception {
        String data = nonceA + nonceB + bobIdentity + sharedSecret;
        String hash = CryptoUtil.hashSHA256(data);
        return nonceA + "||" + nonceB + "||" + identity + "||" + hash;
    }

    public boolean verifyResponse(String nonceA, String nonceB, String hashReceived, String bobIdentity) throws Exception {
        String data = nonceA + nonceB + identity + sharedSecret;
        String expectedHash = CryptoUtil.hashSHA256(data);
        return expectedHash.equals(hashReceived);
    }
}
