package srcs.securite.test;

import static srcs.securite.test.ChannelTestUtil.ALGODIGEST;
import static srcs.securite.test.ChannelTestUtil.ALGOKEY_A;
import static srcs.securite.test.ChannelTestUtil.ALGOSIGN;
import static srcs.securite.test.ChannelTestUtil.IDCLIENT;
import static srcs.securite.test.ChannelTestUtil.IDSERVER;
import static srcs.securite.test.ChannelTestUtil.SIZEKEY_A;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;

import srcs.securite.Authentication;
import srcs.securite.Certif;
import srcs.securite.CertificationAuthority;
import srcs.securite.Channel;
import srcs.securite.PasswordStore;
import srcs.securite.Util;

public final class AuthenticationFactory {

	private final CertificationAuthority ca ;
	private final KeyPair kpclient;
	private final KeyPair kpserveur ;
	
	private final Certif certif_client  ;
	private final Certif certif_serveur;
	
	private final String loginclient;
	private final String passwdclient;
	private final PasswordStore ps;
	
	
//	kpclient = Util.generateNewKeyPair(ALGOKEY_A,SIZEKEY_A);
//	kpserveur = Util.generateNewKeyPair(ALGOKEY_A,SIZEKEY_A);
//			
//	CertificationAuthority ca = new CertificationAuthority(ALGOKEY_A,SIZEKEY_A, ALGOSIGN);
//	Certif certif_client  = ca.declarePublicKey(IDCLIENT, kpclient.getPublic());
//	Certif certif_serveur = ca.declarePublicKey(IDSERVER, kpserveur.getPublic());
//	String passwdclient="!PassCli!";
//	PasswordStore ps = new PasswordStore(ALGODIGEST);
//	ps.storePassword(IDCLIENT, passwdclient);
	
	public AuthenticationFactory() throws GeneralSecurityException {
		
		kpclient = Util.generateNewKeyPair(ALGOKEY_A,SIZEKEY_A);
		kpserveur = Util.generateNewKeyPair(ALGOKEY_A,SIZEKEY_A);
		ca = new CertificationAuthority(ALGOKEY_A,SIZEKEY_A, ALGOSIGN);
		
		certif_client  = ca.declarePublicKey(IDCLIENT, kpclient.getPublic());
		certif_serveur = ca.declarePublicKey(IDSERVER, kpserveur.getPublic());
		loginclient="Mickey";
		passwdclient="!PassWorD!";
		ps = new PasswordStore(ALGODIGEST);
		ps.storePassword(loginclient, passwdclient);
	}
		
	public Authentication newServeurAuthentication(Channel c) throws IOException, GeneralSecurityException {
		return new Authentication(c, certif_serveur, kpserveur, ps, ca.getPublicKey());
	}
	
	public Authentication newClientAuthentication(Channel c) throws IOException, GeneralSecurityException {
		return new Authentication(c, certif_client, kpclient,loginclient, passwdclient, ca.getPublicKey());
	}

	public KeyPair getKpclient() {
		return kpclient;
	}

	public KeyPair getKpserveur() {
		return kpserveur;
	}

	public Certif getCertif_client() {
		return certif_client;
	}

	public Certif getCertif_serveur() {
		return certif_serveur;
	}
	
	public String getLoginclient() {
		return loginclient;
	}

	public String getPasswdclient() {
		return passwdclient;
	}
	
	public CertificationAuthority getCA() {
		return ca;
	}
	
	

	
}