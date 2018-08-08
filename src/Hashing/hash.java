/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author kevin
 */
public class hash {
    
    /**
     * Método que realiza un hashing a una cadena
     * @param txt String, cadena a tratar
     * @param hashType String, tipo de Hashing a realizar
     * @return String, cadena formateada del código hexadecimal del hashing
     */
    private static String doHash(String txt, String hashType) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    
    /**
     * Método que realiza un hashing de tipo SHA-1
     * @param txt String, cadena a ser tratada
     * @return String, cadena resultante de la conversión
     */
    public static String sha1(String txt) {
        return hash.doHash(txt, "SHA1");
    }
    
}
