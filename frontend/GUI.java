package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.border.EmptyBorder;

import Backend.decrypt;
import Backend.encrypt;
import keys.Key;

public class GUI {
    private JFrame frame;
    private JFileChooser fileChooser;
    private JProgressBar progressBar;
    private JButton encryptButton;
    private JButton decryptButton;
    private static SecretKey secretKey;

    public GUI() {
        frame = new JFrame("File Encryption Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel keyLabel = new JLabel("AES Key:");
        JTextField keyField = new JTextField(20);
        JButton generateKeyButton = new JButton("Generate Key");

        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Key.generateKey();
                SecretKey secretKey = Key.loadKeyFromPEM();
                keyField.setText(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
            }
        });

        JButton enterKeyButton = new JButton("Enter Key");
        enterKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String enteredKey = JOptionPane.showInputDialog(frame, "Enter the AES key:");
                System.out.println(enteredKey);
                if (enteredKey != null && !enteredKey.isEmpty()) {
                    byte[] keyBytes = Base64.getDecoder().decode(enteredKey);
                    secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
                    Key.saveKeyToPEM(secretKey);
                    keyField.setText(enteredKey);
                }
            }
        });

        topPanel.add(keyLabel);
        topPanel.add(keyField);
        topPanel.add(generateKeyButton);
        topPanel.add(enterKeyButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);

        encryptButton = new JButton("Encrypt");
        decryptButton = new JButton("Decrypt");
        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(fileChooser);
        panel.add(encryptButton);
        panel.add(decryptButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(progressBar, BorderLayout.SOUTH);

        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableActionButtons(true);
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    progressBar.setVisible(true);
                    encryptFileOrFolder(selectedFile, secretKey);
                }
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    progressBar.setVisible(true);
                    decryptFileOrFolder(selectedFile, secretKey);
                }
            }
        });

        frame.setVisible(true);
    }

    private void enableActionButtons(boolean enable) {
        encryptButton.setEnabled(enable);
        decryptButton.setEnabled(enable);
    }

    private void encryptFileOrFolder(File file, SecretKey key) {
        if (file.isFile()) {

            encryptFile(file, key);
            System.out.println(file.getAbsolutePath());
        } else if (file.isDirectory()) {

        }
    }

    private void encryptFile(File file, SecretKey key) {
        try {

            encrypt.encryptFile(file);

            JOptionPane.showMessageDialog(frame, "File encrypted successfully!", "Encryption",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Encryption failed: " + e.getMessage(), "Encryption Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decryptFileOrFolder(File file, SecretKey key) {
        if (file.isFile()) {

            decryptFile(file, key);
            System.out.println(file.getAbsolutePath());
        } else if (file.isDirectory()) {

        }
    }

    private void decryptFile(File file, SecretKey key) {
        try {

            decrypt.decryptFile(file);

            JOptionPane.showMessageDialog(frame, "File decrypted successfully", "Decryption",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Decryption failed: " + e.getMessage(), "Decryption Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        secretKey = Key.loadKeyFromPEM();
        SwingUtilities.invokeLater(() -> new GUI());
    }
}