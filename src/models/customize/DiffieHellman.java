/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.customize;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author SBE
 */
public class DiffieHellman {
//Attribute

    //Private key
    private BigInteger privateKey;
    //Các tham số để tính khóa chung
    BigInteger p; //modules
    BigInteger g; //prime of p
//Getter and setter

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

// Method
    /**
     * Tính khóa công khai. mỗi người tự tính r gửi khóa công khai cho người còn
     * lại
     *
     * @return publicKey= g ^ primaryKey mod p;
     */
    public BigInteger getPublicKey() {
        return g.modPow(privateKey, p);
    }

    /**
     * Hàm tính khóa chung
     *
     * @param publicKey
     * @return commonKey = pubB ^ priA mod p; người A tính, tương tự với người B
     * 2 người tính sao cho commonKey giống nhau //khác nhau là tính sai
     */
    public BigInteger getPrivateCommonKey(BigInteger publicKey) {
        return publicKey.modPow(privateKey, p);
    }

    /**
     *
     * @throws NoSuchAlgorithmException keyPairGenerator không thể sinh ra khóa
     * của thuật toán Diffie Hellman, throws dùng để bắt lỗi
     */
    private void generateCommonKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DiffieHellman"); //bộ sinh khóa có sẵn
        keyPairGenerator.initialize(512);  //khởi tạo kích thước khóa = 1024 bits hoặc 512 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); //sinh khóa 

        DHParameterSpec params = ((DHPublicKey) keyPair.getPublic()).getParams();//lấy các tham số để tính khóa chung
        p = params.getP(); //modules
        g = params.getG(); //prime of p

//        System.out.println(p);
//        System.out.println(g);
    }

    public static void main(String[] args) {
        // Phải có try catch để bắt lỗi do throws (ở trên) đẩy lên
        try {
            DiffieHellman dh = new DiffieHellman(); //Tạo đối tượng
            dh.generateCommonKey(); //Sinh khóa chung

            //A
            dh.setPrivateKey(BigInteger.valueOf(25)); //Giả sử priA = 25 <=> dh.privateKey = 25 //trong vở ký hiệu là x
            BigInteger pubA = dh.getPublicKey(); // A tính pubA= g ^ xA mod p, A gửi pubA cho B // yA 

            //B
            dh.setPrivateKey(BigInteger.valueOf(45)); //priB = 45, dh.privateKey = 45
            BigInteger pubB = dh.getPublicKey(); //B tính pubB  r gửi cho A
            //B tính: commonKey = pubA ^ priB mod dh.p;  //dh.privateKey = priB
            System.out.println("Private Common Key: B calculate:" + dh.getPrivateCommonKey(pubA));

            dh.setPrivateKey(BigInteger.valueOf(25)); //đặt lại để trở lại vai trò người A, dh.privateKey = 25
            //A tính: commonKey = pubB ^ priB mod dh.p; //pubB ^ priA mod dh.p 
            System.out.println("Private Common Key: A calculate:" + dh.getPrivateCommonKey(pubB));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DiffieHellman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
