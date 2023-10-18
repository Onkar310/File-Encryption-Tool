package keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;
import java.io.*;
import java.util.Base64;

public class Key {
    private static final String PEM_FILE_PATH = "keys\\key.pem";

    public static void saveKeyToPEM(SecretKey key) {
        try {
            byte[] encodedKey = key.getEncoded();
            String base64Key = Base64.getEncoder().encodeToString(encodedKey);

            FileWriter writer = new FileWriter(PEM_FILE_PATH);
            writer.write("-----BEGIN AES KEY-----\n");
            writer.write(base64Key);
            writer.write("\n-----END AES KEY-----\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            saveKeyToPEM(keyGenerator.generateKey());

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static SecretKeySpec loadKeyFromPEM() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(PEM_FILE_PATH));
            StringBuilder keyBuilder = new StringBuilder();
            String line;
            boolean isKey = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("BEGIN AES KEY")) {
                    isKey = true;
                } else if (line.contains("END AES KEY")) {
                    isKey = false;
                } else if (isKey) {
                    keyBuilder.append(line);
                }
            }
            reader.close();

            byte[] decodedKey = Base64.getDecoder().decode(keyBuilder.toString());
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
