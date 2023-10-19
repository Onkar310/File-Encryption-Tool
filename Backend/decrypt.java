package Backend;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.*;

import javax.crypto.SecretKey;

import keys.Key;

public class decrypt {

    public static void decryptFile(File file) {
        try {
            SecretKey secretKey = Key.loadKeyFromPEM();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            String originalFileName = file.getName();
            String fileNameWithoutExtension = originalFileName.replace(".enc", "");

            String decryptedFileName = "decrypted_" + fileNameWithoutExtension;

            File decryptedFile = new File(file.getParentFile(), decryptedFileName);

            FileInputStream encryptedInputStream = new FileInputStream(file);

            FileOutputStream decryptedOutputStream = new FileOutputStream(decryptedFile);

            CipherInputStream cipherInputStream = new CipherInputStream(encryptedInputStream, cipher);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                decryptedOutputStream.write(buffer, 0, bytesRead);
            }

            cipherInputStream.close();
            decryptedOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void decryptDirectory(File directory)
            throws Exception {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                decryptDirectory(file); // Recursively decrypt subdirectories
            } else {
                if (file.getName().endsWith(".enc")) {
                    try {
                        SecretKey secretKey = Key.loadKeyFromPEM();

                        Cipher cipher = Cipher.getInstance("AES");
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);

                        String originalFileName = file.getName();
                        String fileNameWithoutExtension = originalFileName.replace(".enc", "");

                        String decryptedFileName = "decrypted_" + fileNameWithoutExtension;

                        File decryptedFile = new File(file.getParentFile(), decryptedFileName);

                        FileInputStream encryptedInputStream = new FileInputStream(file);

                        FileOutputStream decryptedOutputStream = new FileOutputStream(decryptedFile);

                        CipherInputStream cipherInputStream = new CipherInputStream(encryptedInputStream, cipher);

                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                            decryptedOutputStream.write(buffer, 0, bytesRead);
                        }

                        cipherInputStream.close();
                        decryptedOutputStream.close();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
        }
    }
}
