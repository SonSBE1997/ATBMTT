/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SBE
 */
public class TextFileManagement {

    /**
     * Read text from file
     *
     * @param fileName
     * @return a string include content of file
     */
    public String readFile(String fileName) {
        String tmp = "";
        try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                tmp += line + "\r\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(TextFileManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tmp;
    }

    /**
     * Write text to a file
     *
     * @param str content need write
     * @param fileName
     * @return write to file successful?
     */
    public boolean writeToFile(String str, String fileName) {
        try {
            try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(str);
            }
            return true;
        } catch (IOException e) {
            Logger.getLogger(TextFileManagement.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
}
