/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;
import static kij_chat_client.EncryptionUtil.PUBLIC_KEY_FILE;
import static kij_chat_client.EncryptionUtil.encrypt;
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

    public Write(Scanner chat, PrintWriter out, ArrayList<String> log)
    {
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
                String user = part[1];
                String salt = Character.toString(user.charAt(0));
                for (int i = 0; i < user.length(); i++) {
                    if ((i % 2 != 0)) {
                        salt = salt+Character.toString(user.charAt(i));
                    }             
                }
                if (command.equalsIgnoreCase("login")) {
                    String passwd = part[2];
                    passwd = SHA1(salt+passwd);
                    input = command+" "+user+" "+passwd;
                    System.out.println(input);
                    out.println(input);//SEND IT TO THE SERVER
                    out.flush();//FLUSH THE STREAM
                    
                }else if (command.equalsIgnoreCase("pm")) {
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
                    inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
                    final PublicKey publicKey = (PublicKey) inputStream.readObject();
                    final byte[] cipherText = encrypt(data, publicKey);
                    
                    String Coba = new String(cipherText,"ISO-8859-1");
                    input = command+" "+user+" "+Coba;
//                    System.out.println(input);
                    out.println(input);//SEND IT TO THE SERVER
                    out.flush();//FLUSH THE STREAM
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
