package srcs.banque;

import srcs.persistance.Sauvegardable;

import java.io.*;

public class Compte implements Sauvegardable {
	
	private final String id;
	private double solde;
	

	public Compte(String id) {
		this.id=id;	
		this.solde=0.0;
	}

	public Compte(InputStream in) throws IOException {
		DataInputStream dis = new DataInputStream(in);
		this.id = dis.readUTF();
		this.solde = dis.readDouble();
	}
		
	public String getId() {
		return id;
	}

	public double getSolde() {
		return solde;
	}

	public void crediter(double montant) {
		solde += montant;
	}

	public void debiter(double montant) {
		solde -= montant;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o==this) return true;
		if(o==null) return false;
		if(!(o instanceof Compte)) return false;
		Compte other= (Compte) o;
		return other.id.equals(id);
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public void save(OutputStream out) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		dos.writeUTF(id);
		dos.writeDouble(solde);
	}
	
}
