package Backend;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.*;
import javax.crypto.spec.SecretKeySpec;

import keys.Key;

public class encrypt {

    public static void encryptFile(File file) {
        try {

            Cipher cipher = Cipher.getInstance("AES");
            SecretKey key = Key.loadKeyFromPEM();
            cipher.init(Cipher.ENCRYPT_MODE, key);

            FileInputStream fileInputStream = new FileInputStream(file);

            FileOutputStream encryptedOutputStream = new FileOutputStream(file.getAbsolutePath() + ".enc");

            CipherOutputStream cipherOutputStream = new CipherOutputStream(encryptedOutputStream, cipher);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                cipherOutputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            cipherOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptDirectory(File directory)
            throws Exception {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                encryptDirectory(file); // Recursively encrypt subdirectories
            } else {
                try {

                    Cipher cipher = Cipher.getInstance("AES");
                    SecretKey key = Key.loadKeyFromPEM();
                    cipher.init(Cipher.ENCRYPT_MODE, key);

                    FileInputStream fileInputStream = new FileInputStream(file);

                    FileOutputStream encryptedOutputStream = new FileOutputStream(file.getAbsolutePath() + ".enc");

                    CipherOutputStream cipherOutputStream = new CipherOutputStream(encryptedOutputStream, cipher);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        cipherOutputStream.write(buffer, 0, bytesRead);
                    }
                    cipherOutputStream.close();

                    fileInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "done encrypting";
    }
}
