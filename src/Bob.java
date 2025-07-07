public class Bob {
    private final String identity = "Bob";
    private final String sharedSecret;

    public Bob(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getIdentity() {
        return identity;
    }

    public String generateNonce() {
        return CryptoUtil.generateNonce();
    }

    public String generateResponse(String nonceA, String nonceB, String aliceIdentity) throws Exception {
        String data = nonceA + nonceB + aliceIdentity + sharedSecret;
        return CryptoUtil.hashSHA256(data);
    }

    public boolean verifyResponse(String nonceA, String nonceB, String claimedAlice, String hashReceived) throws Exception {
        String data = nonceA + nonceB + identity + sharedSecret;
        String expectedHash = CryptoUtil.hashSHA256(data);
        return expectedHash.equals(hashReceived);
    }
}
