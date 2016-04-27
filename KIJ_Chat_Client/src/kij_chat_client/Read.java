/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_client;

/*import java.net.Socket;*/
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Scanner;
import static kij_chat_client.EncryptionUtil.PRIVATE_KEY_FILE;
import static kij_chat_client.EncryptionUtil.decrypt;
import org.apache.commons.codec.binary.Base64;
import static kij_chat_client.input.*;

/**
 *
 * @author santen-suru
 */
public class Read implements Runnable {
        
    private Scanner in;//MAKE SOCKET INSTANCE VARIABLE
    String input;
    boolean keepGoing = true;
    ArrayList<String> log;
    ArrayList<String> user;

    public Read(Scanner in, ArrayList<String> log, ArrayList<String> user)
    {
        this.user = user;
            this.in = in;
            this.log = log;
    }

    @Override
    public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
    {
        try
        {
            while (keepGoing)//WHILE THE PROGRAM IS RUNNING
            {						
                if(this.in.hasNext()) { //IF THE SERVER SENT US SOMETHING
                    input = this.in.nextLine();
                    if(input.equalsIgnoreCase("SUCCES login")){            
                        System.out.println(input+"TESTING");//PRINT IT OUT
                    }
                    else if (input.split(" ")[0].toLowerCase().equals("success")) {
                        if (input.split(" ")[1].toLowerCase().equals("logout")) {
                            keepGoing = false;
                        } else if (input.split(" ")[1].toLowerCase().equals("login")) {
                            log.clear();
                            log.add("true");
                        }
                    }
                    else
                    {
                        String[] dapet = input.split("_");
                        input = dapet[1];
                        String path_to_priv = "C:/keys/"+user+"_private.key";
                         byte[] decodedBytes = Base64.decodeBase64(input);
//                        System.out.println(input);
//                        System.out.println("panjaang"+decodedBytes.length);
                        ObjectInputStream inputStream = null;
                        File f = new File(path_to_priv);
                        if(f.exists() && !f.isDirectory()) {
                            inputStream = new ObjectInputStream(new FileInputStream(path_to_priv));
                        }
//                        else{
//                            get_File(dapet[0]+"_private.key");
//                            inputStream = new ObjectInputStream(new FileInputStream(path_to_priv));
//                        }
//                        byte[] test = input.getBytes("ISO-8859-1");
                        final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
                        final String plainText = decrypt(decodedBytes, privateKey);
                        String show = dapet[0]+": "+plainText;
                        System.out.println(show);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
        } 
    }
}
