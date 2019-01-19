package com.jostens.dam.shared.jdbc;

import static org.junit.Assert.assertEquals;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;

import com.jostens.dam.shared.testing.SharedBaseTest;

/**
 * Test encryption/decryption provided by the jasypt .jar file
 */
public class EncryptionTest extends SharedBaseTest
{

	@Test
	public void testJasypt()
	{
		// Test the underlying jasypt encryption class
		
		// Use a seed based on the simple name of this class
		String passwordSeed = this.getClass().getSimpleName();

		String passwordToEncrypt = "MyPassword186";
		
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passwordSeed);
		
		String encryptedString = encryptor.encrypt(passwordToEncrypt);
//		System.out.println("Encrypted String:" + encryptedString);
		
		// Decrypt
		encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passwordSeed);
		
		String decrypted = encryptor.decrypt(encryptedString);
		
		assertEquals(decrypted, passwordToEncrypt);
	}
	
	@Test
	public void testEncryption()
	{
		// Test the encryption wrapper class found in common package
		
		Encryption encryptor = new Encryption();
		
		String toEncrypt = "Wowwy123.";
		
		String encryptedString = encryptor.encrypt(toEncrypt);
		
		String decryptedString = encryptor.decrypt(encryptedString);
		
		assertEquals(toEncrypt, decryptedString);
	}
}
