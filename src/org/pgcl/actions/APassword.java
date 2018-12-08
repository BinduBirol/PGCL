package org.pgcl.actions;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class APassword {

	
	public static void main(String[] args) {


		StrongPasswordEncryptor spasswordEncryptor = new StrongPasswordEncryptor();
		BasicPasswordEncryptor bpasswordEncryptor = new BasicPasswordEncryptor();
		
		
		String sPassword=(spasswordEncryptor.encryptPassword("Biis609"));
		
		String bPassword=(bpasswordEncryptor.encryptPassword("Biis609"));
		
		System.out.println("S-> "+sPassword);
		System.out.println("B-> "+bPassword);
		
	}

}
