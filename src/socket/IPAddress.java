/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author SBE
 */
public class IPAddress {

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(String ip) {
        return PATTERN.matcher(ip).matches();
    }

//    public static void main(String[] args) {
//        String ip = "192.168.10.11";
//        System.out.println(validate(ip));
//    }
    public static String getIpAddressLocalHost() {
        try {
            return Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
