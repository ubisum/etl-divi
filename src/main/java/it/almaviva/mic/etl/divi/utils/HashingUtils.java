package it.almaviva.mic.etl.divi.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils 
{	
	public static String getHashingCode(String input) throws NoSuchAlgorithmException
	{
		/* introduzione del codificatore */
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		/* calcolo dell'hash */
		 byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
		 
		 /* conversione in esadecimale (per rendere la stringa formata da caratteri leggibili) */
		 StringBuilder hexString = new StringBuilder();
         for (byte b : hashBytes) 
         {
             String hex = Integer.toHexString(0xff & b); /* ultimi 8 bit */
             if (hex.length() == 1) hexString.append('0'); /* padding (se necessario) */
             hexString.append(hex);
         }
         return hexString.toString();
	}
	
}
