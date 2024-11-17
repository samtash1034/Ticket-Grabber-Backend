package com.project.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class RSAUtil {

	@Value("${key.privateKey}")
	String privateKey;

	@Value("${key.publicKey}")
	String publicKey;

	private static final String ALGORITHM = "RSA";
	private static final int KEY_SIZE = 1024;

	public PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
		return factory.generatePrivate(privateKeySpec);
	}

	public PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
		return factory.generatePublic(publicKeySpec);
	}

//	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
//		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//		keyGen.initialize(KEY_SIZE);
//		KeyPair pair = keyGen.generateKeyPair();
//		log.info(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()));
//		log.info("=============================================");
//		log.info(Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded()));
//	}

}
