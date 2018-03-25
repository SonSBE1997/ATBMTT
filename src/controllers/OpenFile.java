/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.TextFileManagement;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author SBE
 */
public class OpenFile {

    public static String readFile(JFrame frm) {
        JFileChooser chooseFile = new JFileChooser("D:");
        chooseFile.setDialogTitle("Please choose a file to send");
        chooseFile.setDialogType(JFileChooser.OPEN_DIALOG);
        chooseFile.setFileFilter(new FileNameExtensionFilter("Text document", "txt"));
        chooseFile.showOpenDialog(frm);
        TextFileManagement management = new TextFileManagement();
        return management.readFile(chooseFile.getSelectedFile().getPath());
    }
}
