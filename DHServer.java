import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class DHServer {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {

        System.out.println("Start by listening on port no 11111");

        ServerSocket ss = new ServerSocket(11111);
        Socket link = ss.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintStream out = new PrintStream(link.getOutputStream());

        BigInteger q = new BigInteger(in.readLine());
        BigInteger a = new BigInteger(in.readLine());
        BigInteger ya = new BigInteger(in.readLine());

        SecureRandom sr = new SecureRandom();
        BigInteger xb = new BigInteger(q.bitLength() - 1, sr);
        BigInteger yb = a.modPow(xb, q);

        out.println(yb);

        BigInteger key = ya.modPow(xb, q);

        System.out.println("The secret key with " + link.getInetAddress().toString() + " is\n" + key);

        // Receive and decrypt the message
        Cipher cipher = Cipher.getInstance("AES");
        byte[] sharedKeyBytes = key.toByteArray();
        // Use only the first 16 bytes for the AES key
        byte[] aesKeyBytes = new byte[16];
        System.arraycopy(sharedKeyBytes, 0, aesKeyBytes, 0, 16);
        SecretKeySpec sharedSecretKey = new SecretKeySpec(aesKeyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, sharedSecretKey);

        ObjectInputStream inputStream = new ObjectInputStream(link.getInputStream());
        byte[] encryptedMessage = (byte[]) inputStream.readObject();

        // Print the encrypted message
        System.out.println("Encrypted message received: " + new String(encryptedMessage));

        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        String receivedMessage = new String(decryptedMessage);
        System.out.println("Decrypted message: " + receivedMessage);

        inputStream.close();
        out.close();
        in.close();
        link.close();
        ss.close();
    }
}

