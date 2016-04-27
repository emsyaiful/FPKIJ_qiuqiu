/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;
import static kij_chat_client.EncryptionUtil.PRIVATE_KEY_FILE;
import static kij_chat_client.EncryptionUtil.PUBLIC_KEY_FILE;
import static kij_chat_client.EncryptionUtil.decrypt;
import static kij_chat_client.EncryptionUtil.encrypt;
import static kij_chat_client.input.get_File;
import org.apache.commons.codec.binary.Base64;
//import static jdk.nashorn.tools.ShellFunctions.input;

/**
 *
 * @author santen-suru
 */
public class Write implements Runnable {
    
    private Scanner chat;
    private PrintWriter out;
    boolean keepGoing = true;
    ArrayList<String> log;
    ArrayList<String> user;

    public Write(Scanner chat, PrintWriter out, ArrayList<String> log, ArrayList<String> user)
    {
        this.user = user;
        this.chat = chat;
        this.out = out;
        this.log = log;
    }

    @Override
    public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
    {
        try
        {
            while (keepGoing)//WHILE THE PROGRAM IS RUNNING
            {						
                String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                String[] part = input.split(" ");
                int length = part.length;
                String command = part[0];
                user.add(part[1]);
                String salt = Character.toString(user.get(0).charAt(0));
                for (int i = 0; i < user.get(0).length(); i++) {
                    if ((i % 2 != 0)) {
                        salt = salt+Character.toString(user.get(0).charAt(i));
                    }             
                }
                if (command.equalsIgnoreCase("login")) {
                    String passwd = part[2];
                    passwd = SHA1(salt+passwd);
                    input = command+" "+user.get(0)+" "+passwd;
                    System.out.println(input);
                    out.println(input);//SEND IT TO THE SERVER
                    out.flush();//FLUSH THE STREAM
                    
                }
                else if (command.equalsIgnoreCase("bm")) {
                    String bm = part[1]; 
                    if(length > 2)
                    {
                        for (int i = 2; i < length; i++) 
                        {
                            bm = bm+" "+part[i];
                        }
                    }
                    else bm = part[1];
                    input = command+" "+bm;
                    out.println(input);//SEND IT TO THE SERVER
                    out.flush();//FLUSH THE STREAM
                }
                else if (command.equalsIgnoreCase("pm")) {
                    String temp = user.get(0);
                    user.clear();
                    user.add(part[1]);
                    String data = part[2];
                    if(length > 3)
                    {
                        for (int i = 3; i < length; i++) 
                        {
                            data = data+" "+part[i];
                        }
                    }
                    else data = part[2];
//                    System.out.println(data);
                    // Ekripsinya disini
                    ObjectInputStream inputStream = null;

      // Encrypt the string using the public key
                    String path_to_pub = "C:/keys/"+user.get(0)+"_public.key";
                    File f = new File(path_to_pub);
                    if(f.exists() && !f.isDirectory()) {
                            inputStream = new ObjectInputStream(new FileInputStream(path_to_pub));
                        }else{
                            get_File(user.get(0)+"_public.key");
                            inputStream = new ObjectInputStream(new FileInputStream(path_to_pub));
                        }
                    inputStream = new ObjectInputStream(new FileInputStream(path_to_pub));
                    final PublicKey publicKey = (PublicKey) inputStream.readObject();
                    final byte[] cipherText = encrypt(data, publicKey);
                    
                    byte[] encodedBytes = Base64.encodeBase64(cipherText);
//                    System.out.println("encodedBytes " + new String(encodedBytes));
                    String Coba = new String(encodedBytes);
                    
//                    byte[] test = Coba.getBytes("ISO-8859-1");
//                    System.out.println("Sebelum dikirm KE server"+test);
//                    System.out.println(test.length);
//                    
//                    inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
//                    final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
//                    final String plainText = decrypt(test, privateKey);
                    
//                    System.out.println(plainText);
                    input = command+" "+user.get(0)+" "+Coba;
//                    System.out.println("ini yang dikirim"+input);
                    
                    out.println(input);//SEND IT TO THE SERVER
                    out.flush();//FLUSH THE STREAM
                    user.clear();
                    user.add(temp);
                }else if (command.equalsIgnoreCase("bm")) { //BROADCAST
                   String temp = user.get(0);
                    String usert;
                    user.clear();
                    //user.add(part[1]);
                    String data = part[1];
                    if(length > 2)
                    {
                        for (int i = 2; i < length; i++) 
                        {
                            data = data+" "+part[i];
                        }
                    }
                    else data = part[1];
//                    System.out.println(data);
                    // Ekripsinya disini
                    ObjectInputStream inputStream = null;
/*
                   BAKAL DILOOPING UNTUK SEMUA DATA USER YANG ADA DI FOLDER
                    */
                    
                    File folder = new File("C:/keys");
                    File[] listOfFiles = folder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            //System.out.println("User " + listOfFiles[i].getName());
                            usert = listOfFiles[i].getName();
                            String path_to_pub = "C:/keys/"+usert;
                            usert = usert.replace("_public.key","");
                            
                            inputStream = new ObjectInputStream(new FileInputStream(path_to_pub));
                            final PublicKey publicKey = (PublicKey) inputStream.readObject();
                            final byte[] cipherText = encrypt(data, publicKey);
                    
                            byte[] encodedBytes = Base64.encodeBase64(cipherText);
//                           System.out.println("encodedBytes " + new String(encodedBytes));
                            String Coba = new String(encodedBytes);
                            input = "pm "+usert+" "+Coba; //jadi di PM satu2
                    
                            out.println(input);//SEND IT TO THE SERVER
                            out.flush();//FLUSH THE STREAM
                        }
                     }
                    
                }
                if (input.contains("logout")) {
                    if (log.contains("true"))
                        keepGoing = false;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
        } 
    }

    private String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
