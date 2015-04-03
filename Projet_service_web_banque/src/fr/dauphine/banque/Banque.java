package fr.dauphine.banque;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;

public class Banque implements Serializable{
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private final HashMap<Long, Compte> banque = new HashMap<Long, Compte>();
	private long compteurCompte;

	public Banque(){
		super();
	}

	public long getCompteurCompte() {
		return compteurCompte;
	}

	public Compte findByEmail(String email){
		for (long key : banque.keySet()){
			if (banque.get(key).getEmail().equals(email))
				return banque.get(key);
		}
		return null;
	}

	/**
	 * Ajoute un compte a la base
	 */
	public boolean addCompte(String nom, String prenom, String email, String mdp) {
		if(findByEmail(email)==null){
			Compte compte = new Compte(nom, prenom, email, mdp, compteurCompte);
			banque.put(compteurCompte, compte);
			compteurCompte++;
			System.out.println(compte.toString() +" vient d'etre ajoute a la base");
			return true;
		}
		System.out.println("Une personne avec le mail: " + email 
				+ " existe deja dans la base");
		return false;
	}
	
	/**
	 * Supprime le compte de la base
	 */

	public boolean delCompte(String email){
		Compte compte = findByEmail(email);
		if(compte!=null){
			banque.remove(compte.getNoCompte());
			System.out.println(compte.toString() +" vient d'etre supprime de la base");
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Permet d'alimenter le compte
	 * @param email
	 * @param mdp
	 * @param montant
	 * @return true si le email, le mdp et le montant sont bien 
	 * indiques et le montant a ete rajoute au solde
	 */
	public boolean depot(String email, String mdp, double montant){
		Compte compte = findByEmail(email);
		if(compte==null){
			System.out.println("Le compte pour l'email "+ email + " n'existe pas");
			return false;
		} else if(!compte.getMdp().equals(mdp)){
			System.out.println(compte.toString() + " le mot de passe renseigne n'est pas correct");
			return false;
		} else if(montant<=0){
			System.out.println(compte.toString() + " le montant indique n'est pas correct");
			return false;
		} else {
			compte.setSolde(compte.getSolde()+montant);
			System.out.println(compte.toString() + " a ete credite avec succes");
			return true;
		}
	}
	/**
	 * Permet de debiter le compte
	 * @param email
	 * @param mdp
	 * @param montant
	 * @return 0 si le compte avec l'email indiqué n'existe pas; 
	 * 2 si le mot de passe indique n'est pas correcte
	 * 3 si le montant n'a pas ete correctement reseigne
	 * 1 si le compte a ete debite avec succes
	 */
	public int retrait(String email, String mdp, double montant){
		Compte compte = findByEmail(email);
		if(compte==null){
			System.out.println("Le compte pour l'email "+ email + " n'existe pas");
			return 0;
		} else if(!compte.getMdp().equals(mdp)){
			System.out.println(compte.toString() + " le mot de passe renseigne n'est pas correct");
			return 2;
		} else if(montant<=0){
			System.out.println(compte.toString() + " le montant indique n'est pas correct");
			return 3;
		} else {
			compte.setSolde(compte.getSolde()-montant);
			System.out.println(compte.toString() + " a ete debite avec succes");
			return 1;
		}
	}
	/**
	 * Renvoie le solde d'un compte
	 * @param email
	 * @param mdp
	 * @param montant
	 * @return
	 */
	public double consultSolde(String email, String mdp){
		Compte compte = findByEmail(email);
		if(compte==null){
			System.out.println("Le compte pour l'email "+ email + " n'existe pas");
			return 0;
		} else if(!compte.getMdp().equals(mdp)){
			System.out.println(compte.toString() + " le mot de passe renseigne n'est pas correct");
			return 0;
		} else {			
			System.out.println(compte.toString() + " interogation du compte");
			return compte.getSolde();
		}
	}
}