# Protocollo di Autenticazione tra Alice e Bob

## Panoramica

Questo progetto implementa un semplice protocollo di autenticazione mutua tra due entità, **Alice** e **Bob**, utilizzando nonce, hash SHA-256 e un segreto condiviso.

Il flusso del protocollo avviene su un canale insicuro simulato in memoria (`Channel`), ma l'autenticazione si basa sull'integrità dei messaggi e la precondivisione del segreto.

---

## Componenti Principali

### `Main.java`

Contiene la logica di esecuzione dell'intero protocollo:

1. Bob genera un `nonceB` e lo invia ad Alice.
2. Alice riceve `nonceB`, genera un `nonceA` e invia una risposta hashata a Bob.
3. Bob verifica il messaggio ricevuto da Alice.
4. Bob risponde ad Alice con un hash.
5. Alice verifica l'identità di Bob.

---

### `CryptoUtil.java`

Classe di utilità crittografica:

- `generateNonce()`: Genera un nonce sicuro e ne garantisce l'unicità persistente.
- `hashSHA256(String input)`: Hasha l'input con SHA-256 e restituisce una stringa codificata in base64.
- `resetNonceFile()`: Pulisce il file dei nonce.

> **Nota:** I nonce usati vengono salvati nel file `nonce_used.txt`.

---

### `Channel.java`

Simula un canale di comunicazione insicuro usando una coda FIFO:

- `send(String message)`: Invia un messaggio sul canale.
- `receive()`: Riceve un messaggio.
- `hasMessages()`: Controlla se ci sono messaggi in attesa.

---

### `Alice.java`

Rappresenta l'entità Alice:

- `generateNonce()`: Genera un nonce tramite `CryptoUtil`.
- `generateResponse(...)`: Crea la risposta composta da `nonceA`, `nonceB`, `identity`, e l'hash finale.
- `verifyResponse(...)`: Verifica l'hash ricevuto da Bob.

---

### `Bob.java`

Rappresenta l'entità Bob:

- `generateNonce()`: Genera un nonce.
- `generateResponse(...)`: Genera un hash di autenticazione per Alice.
- `verifyResponse(...)`: Verifica l'hash inviato da Alice.

---

## Sequenza del Protocollo

```text
Bob        →      Alice: CHALLENGE || nonceB
Alice      →      Bob: RESPONSE_A || nonceA || hashA(noncea + nonceb + Bobid + secret)
Bob verifica Alice:
           SHA-256(nonceA + nonceB + "Bobid" + secret) == hashA

Bob        →      Alice: RESPONSE_B || hashB(nonceA + nonceB + Aliceid + secret)
Alice verifica Bob:
           SHA-256(nonceA + nonceB + "Aliceid" + secret) == hashB
```

---

## Sicurezza

Il protocollo si basa su:

- Un **segreto condiviso** predefinito e noto solo ad Alice e Bob.
- **Nonce unici** per prevenire replay attack.
- **Hash SHA-256** per garantire integrità e autenticità.

---

## Esecuzione

Per eseguire il programma:

1. Assicurati che il file `nonce_used.txt` sia scrivibile nella directory di esecuzione.
2. Compila tutti i file `.java`.
3. Esegui `Main`.

```bash
  javac *.java
  java Main
```

---

## Reset dei nonce
Puoi ripulire il file dei nonce con il metodo:

```bash
  CryptoUtil.resetNonceFile();
```

---
