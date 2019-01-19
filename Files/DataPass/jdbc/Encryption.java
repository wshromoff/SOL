package com.jostens.dam.shared.jdbc;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Perform encrypt/decrypt functionality using the jasypt .jar file found at: 
 * 		http://jasypt.org/
 * 
 * This class provides a method to encrypt and decrypt hiding details
 * like what the seed is for that purpose.
 */
public class Encryption
{

	public String encrypt(String message)
	{
		String passwordSeed = this.getClass().getSimpleName();
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passwordSeed);

		String encryptedString = encryptor.encrypt(message);
		return encryptedString;
	}
	
	public String decrypt(String encryptedMessage)
	{
		String passwordSeed = this.getClass().getSimpleName();
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passwordSeed);

		String decryptedString = encryptor.decrypt(encryptedMessage);
		return decryptedString;
	}

}
