/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.DigestException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author santen-suru
 */
public class User {
    // User-Password list
    private ArrayList<Pair<String,String>> _userlist = new ArrayList<>();
    User() {
        FileReader fr = null;
        try {
            String listUser = "../listUser.txt";
            String line = null;
            fr = new FileReader(listUser);
            BufferedReader br = new BufferedReader(fr);
            try {
                while ((line = br.readLine()) != null) {
                    String[] part = line.split(" ");
                    String user = part[0];
                    String passwd = part[1];
                    _userlist.add(new Pair(user, passwd));
                }
            } catch (IOException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public ArrayList<Pair<String,String>> getUserList() {
        return _userlist;
    }
}
