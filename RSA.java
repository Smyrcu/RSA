import jdk.jshell.spi.ExecutionControl;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSA
{
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int        bitlength = 512;
    private Random     r;

    public RSA()
    {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }



    public RSA(BigInteger e, BigInteger d, BigInteger N)
    {
        this.e = e;
        this.d = d;
        this.N = N;
    }



    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException
    {
        RSA rsa = new RSA();
        byte[] encrypted, decrypted;
        String testString;
        System.out.println("Welcome to RSA encrypting/decrypting program!");
        System.out.println("Made by: Błażej Szadkowski & Daniel Nogaj");
        showMenu();
        DataInputStream in = new DataInputStream(System.in);

        String chosenOption = in.readLine();
        switch (chosenOption){
            case "1": // E text
                System.out.println("Enter the plain text:");
                testString = in.readLine();
                System.out.println("Enter public key e,N");
                String publicKey1 = in.readLine();
                String[] pKey1 = publicKey1.split(",");
                encrypted = rsa.encrypt(testString.getBytes() ,new BigInteger(pKey1[0]), new BigInteger(pKey1[1]));
                System.out.println("Encrypting String: " + testString);
                System.out.println("Encrypted String: " + new String(encrypted));

                break;
            case "2": // D text
                System.out.println("Enter encrypted text:");
                testString = in.readLine();
                System.out.println("Enter private key d,N :");
                String privateKey2 = in.readLine();
                String[] pKey2 = privateKey2.split(",");
                decrypted = rsa.decrypt(testString.getBytes(), new BigInteger(pKey2[0]), new BigInteger(pKey2[1]));
                System.out.println("Decrypting String: " + testString);
                System.out.println("Decrypted String: " + new String(decrypted));

                break;
            case "3": // E file
                System.out.println("Enter path to text file (*.txt):");
                String pathEncrypt = in.readLine();
                System.out.println("Enter public key e,N");
                String publicKey3 = in.readLine();
                String[] pKey3 = publicKey3.split(",");

                String newPathEncrypt = pathEncrypt.replaceAll(".txt","encrypted.txt");
                StringBuilder dataEncrypt = new StringBuilder(new String());
                try{
                    File file = new File(pathEncrypt);
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()){
                        dataEncrypt.append(myReader.nextLine());
                    }
                    myReader.close();
                    System.out.println("Text from file:");
                    System.out.println(dataEncrypt);
                    encrypted = rsa.encrypt(dataEncrypt.toString().getBytes() ,new BigInteger(pKey3[0]),
                            new BigInteger(pKey3[1]));
                    File newFile = new File(newPathEncrypt);
                    if (newFile.createNewFile()){
                        FileWriter myWriter = new FileWriter(newPathEncrypt);
                        for (int i = 0; i < encrypted.length; i++){
                            myWriter.write(encrypted[i]);
                        }
                        myWriter.close();
                        System.out.println("File encrypted:");
                        System.out.println(newPathEncrypt);
                    }
                    else {
                        System.out.println("File already exists.");
                    }
                }catch (FileNotFoundException e){
                    System.out.println("An error occurred:");
                    e.printStackTrace();
                }catch (IOException e){
                    System.out.println("An error occurred:");
                    e.printStackTrace();
                }

                break;
            case "4": // D file
                System.out.println("Enter path to text file (*.txt):");
                String pathDecrypt = in.readLine();
                System.out.println("Enter private key d,N :");
                String privateKey4 = in.readLine();
                String[] pKey4 = privateKey4.split(",");

                String newPathDecrypt = pathDecrypt.replaceAll(".txt","decrypted.txt");
                StringBuilder dataDecrypt = new StringBuilder(new String());
                try{
                    File file = new File(pathDecrypt);
                    Scanner myReader = new Scanner(file);
                    while (myReader.hasNextLine()){
                        dataDecrypt.append(myReader.nextLine());
                    }
                    myReader.close();
                    System.out.println("Text from file:");
                    System.out.println(dataDecrypt);
                    decrypted = rsa.decrypt(dataDecrypt.toString().getBytes() ,new BigInteger(pKey4[0]),
                            new BigInteger(pKey4[1]));
                    File newFile = new File(newPathDecrypt);
                    if (newFile.createNewFile()){
                        FileWriter myWriter = new FileWriter(newPathDecrypt);
                        myWriter.write(decrypted.toString());
                        myWriter.close();
                        System.out.println("File decrypted:");
                        System.out.println(newPathDecrypt);
                    }
                    else {
                        System.out.println("File already exists.");
                    }
                }catch (FileNotFoundException e){
                    System.out.println("An error occurred:");
                    e.printStackTrace();
                }catch (IOException e){
                    System.out.println("An error occurred:");
                    e.printStackTrace();
                }

                break;
            case "5":
                RSA rsa5 = new RSA();
                System.out.println("New public key: " + "(" + rsa5.e + "," + rsa5.N + ")");
                System.out.println("New private key: " + "(" + rsa5.d + "," + rsa5.N + ")");

                System.out.println("Save to file? y/n");
                String input = in.readLine();
                switch (input){
                    case "y":
                        System.out.println("File will be saved as \"RSAKeys\".");
                        System.out.println("Type in path: ");
                        String rsaKeysPath = in.readLine();
                        try{
                            File file = new File(rsaKeysPath + "\\RSAKeys.txt");
                            if (file.createNewFile()){
                                FileWriter keyWriter = new FileWriter(rsaKeysPath + "\\RSAKeys.txt");
                                keyWriter.write("Public key: " + "(" + rsa5.e + "," + rsa5.N + ")");
                                keyWriter.write("Private key: " + "(" + rsa5.d + "," + rsa5.N + ")");
                                keyWriter.close();
                                System.out.println("File saved.");
                            }else{
                                System.out.println("File already exists.");
                            }
                        }catch (IOException e){
                            System.out.println("An error occurred:");
                            e.printStackTrace();
                        }
                        break;
                    case "n":
                        break;
                }
            case "0": // Exit
                System.out.println("Thank you for trying our program!");
                System.exit(0);
                break;
            default:
                showMenu();
                break;
        }

    }

    private static String bytesToString(byte[] encrypted)
    {
        String test = "";
        for (byte b : encrypted)
        {
            test += Byte.toString(b);
        }
        return test;
    }

    // Encrypt message
    public byte[] encrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }
    public byte[] encrypt(byte[] message, BigInteger e2, BigInteger N2)
    {
        return (new BigInteger(message)).modPow(e2, N2).toByteArray();
    }

    // Decrypt message
    public byte[] decrypt(byte[] message)
    {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
    public byte[] decrypt(byte[] message, BigInteger d2, BigInteger N2)
    {
        return (new BigInteger(message)).modPow(d2, N2).toByteArray();
    }

    private static void showMenu() throws IOException {
        System.out.println("");
        System.out.println("<---------MENU--------->");
        System.out.println("{1} - Encrypt plain text");
        System.out.println("{2} - Decrypt plain text");
        System.out.println("{3} - Encrypt text file");
        System.out.println("{4} - Decrypt text file");
        System.out.println("{5} - Create new pair of keys");
        System.out.println("{0} - Exit");

    }

}