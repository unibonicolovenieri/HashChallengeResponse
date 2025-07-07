# Protocollo di Autenticazione tra Alice e Bob

## Panoramica

Questo progetto implementa un semplice protocollo di autenticazione mutua tra due entità, **Alice** e **Bob**, utilizzando nonce, hash SHA-256 e un segreto condiviso.

Il flusso del protocollo avviene su un canale insicuro simulato in memoria (`Channel`), ma l'autenticazione si basa sull'integrità dei messaggi e la condivisione del segreto.

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
Bob        →      Alice: RESPONSE_B || hashB(nonceA + nonceB + Aliceid + secret)
Bob verifica Alice:
           SHA-256(nonceA + nonceB + "Bobid" + secret) == hashA
Alice verifica Bob:
           SHA-256(nonceA + nonceB + "Aliceid" + secret) == hashB
```

---

## Sicurezza

Il protocollo si basa su:

- Un **segreto pre-condiviso** predefinito e che dovrebbe essere noto solo ad Alice e Bob.
- **Nonce (teoricamente) unici** per prevenire replay attack.
- **Hash SHA-256** per garantire integrità e autenticità.

### Criticità
Queste sono alcune delle criticità evidenziate, tuttavia lo scopo del progetto è riuscire a dimostrare tramite Sfida e Risposta con hash l'identificazione di due soggetti, Alice e Bob. Oltre al progetto mi sono deidicato all'analisi delle criticità sia logiche sia "*di struttura*" del programma per capirne le vulnerabilità. Chiaramente ce ne sono molte:
- **Canale di comunicazione non sicuro** : l Channel è solo una Queue Java: non ha cifratura, autenticazione né protezione contro man-in-the-middle. Una possibile soluzione sarebbe l'implementazione di TLS o una forma di cifratura/autenticazione dei messaggi 
- **Nonce memorizzati localmente in file semplice** : Ho memorizzato in un file i nonce utilizzati per poi al momento della creazione verificare che non vengano riutilizzare per riuscire a garantire che il nonce sia un numero casuale non riutilizzato ma tutta via mi espongo ad un ulteriore criticità ovvero un file contenete tutti i nonce utilizzati.
- **Autenticazione basata solo su hash con secret condiviso** : L'autenticazione con l'hash avviene grazie anche ad un segreto precondiviso, se qualcuno entra in possesso di quel segreto (banalmente leggendo il codice) riesce a bucare l'intero protocollo. La soluzione possibile sarebbe l'utilizzo di HMAC-SHA256, non semplice SHA256.
- **Mancanza di verifica dell'identità del mittente** : L'identità è una banale stringa che si può ottenere con il metodo getIdentity e dunque chiunque può entrare in possesso di quell'identità e falsificarsi come tale. Resterebbe il segreto si, ma come detto prima è banalmente visibile dal codice. Una soluzione sarebbe utilizzare certificati o firmare con una chiave privata il messaggio. E permettere al ricevente di identificarne l'arrivo tramite chiave pubblica del mittente


---

## Esecuzione

Per eseguire il programma:

1. Assicuratevi che il file `nonce_used.txt` sia scrivibile nella directory di esecuzione.
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
