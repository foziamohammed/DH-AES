import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class DHClient {

    public static int keyLength = 128;
    static BufferedReader userentry = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Create an instance of DHClientGUI directly
        DHClientGUI gui = new DHClientGUI();
    }
 


    public static void connectToServer(String host, String message) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecureRandom sr = new SecureRandom();
        BigInteger q = new BigInteger(keyLength, 10, sr);
        BigInteger a = new BigInteger(keyLength - 1, sr);
        BigInteger xa = new BigInteger(keyLength - 1, sr);
        BigInteger ya = a.modPow(xa, q);

        Socket link = new Socket(host, 11111);
        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintStream out = new PrintStream(link.getOutputStream());

        out.println(q);
        out.println(a);
        out.println(ya);

        BigInteger yb = new BigInteger(in.readLine());
        BigInteger key = yb.modPow(xa, q);

        System.out.println("The secret key is:\n" + key);

        // Encryption
        Cipher cipher = Cipher.getInstance("AES");
        byte[] sharedKeyBytes = key.toByteArray();
        // Use only the first 16 bytes for the AES key
        byte[] aesKeyBytes = new byte[16];
        System.arraycopy(sharedKeyBytes, 0, aesKeyBytes, 0, 16);
        SecretKeySpec sharedSecretKey = new SecretKeySpec(aesKeyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sharedSecretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        ObjectOutputStream outputStream = new ObjectOutputStream(link.getOutputStream());
        outputStream.writeObject(encryptedMessage);
        System.out.println("Encrypted message sent to the server.");

        outputStream.close();
        in.close();
        out.close();
        link.close();
    }
}

