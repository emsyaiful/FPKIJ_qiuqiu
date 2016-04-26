/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kij_chat_server;

import java.security.DigestException;
import java.util.ArrayList;

/**
 *
 * @author santen-suru
 */
public class User {
    // User-Password list
    private ArrayList<Pair<String,String>> _userlist = new ArrayList<>();
    User() {
        _userlist.add(new Pair("Admin", "0e49e69fe0fceca50a8ed501c006522168964ffd"));
        _userlist.add(new Pair("Andi", "142768920414808a76f384b2d170c989aad5f698"));
        _userlist.add(new Pair("Budi", "ccbc7d8a8c2df26e1672a7e15538fd070ff2c599"));
        _userlist.add(new Pair("Rudi", "d63a753732e85d15f0bb17d46cb73dcfda4da5d8"));
        _userlist.add(new Pair("Luci", "aa774cc14c683e9730b4d6c36dbb38770653f136"));
    }
    
    public ArrayList<Pair<String,String>> getUserList() {
        return _userlist;
    }
}
