package fr.dauphine.beans;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import fr.dauphine.interfaces.Bibliotheque;
import fr.dauphine.interfaces.Livre;
import fr.dauphine.interfaces.Personne;


public class BibliothequeImpl extends UnicastRemoteObject implements Bibliotheque {

	private static final long serialVersionUID = 1L;
	private final HashMap<Long, Livre> bibliotheque = new HashMap<Long, Livre>();
	private final HashMap<Long, Personne> annuaire = new HashMap<Long, Personne>();
	private long compteurLivre;
	private long compteurPersonne;

	/**
	 * Le Naming.rebind est mis dans le constructeur de la classe.
	 * Ainsi des que l'objet BibliothequeImpl est cree, il sera accesssible depuis RMI
	 * @throws RemoteException
	 */
	public BibliothequeImpl() throws RemoteException {
		super();
		System.out.println("BibliothequeImpl()");
		try {
			Naming.rebind("rmi://localhost:1099/Bibliotheque", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}
	
	public BibliothequeImpl(int arg0, RMIClientSocketFactory arg1,
			RMIServerSocketFactory arg2) throws RemoteException {
		super(arg0, arg1, arg2);
		System.out.println("BibliothequeImpl(int arg0, RMIClientSocketFactory arg1,"
			+"RMIServerSocketFactory arg2)");
		try {
			Naming.rebind("rmi://localhost:1099/Bibliotheque", this);
			System.out.println();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public BibliothequeImpl(int arg0) throws RemoteException {
		super(arg0);
		System.out.println("BibliothequeImpl(int arg0)");
		try {
			Naming.rebind("rmi://localhost:1099/Bibliotheque", this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	

	public long getCompteurLivre() {
		return compteurLivre;
	}

	public long getCompteurPersonne() {
		return compteurPersonne;
	}
	/**
	 * Supprime le livre de la base
	 */
	@Override
	public boolean delLivre(Livre livre) throws RemoteException {
		if(bibliotheque.containsKey(livre.getNumero())){
			System.out.println(livre.remoteToString() +" vient d'etre supprime de la base");
			return bibliotheque.remove(livre.getNumero()) != null;
		}
		return false;
	}

	@Override
	public Livre[] findByAuteur(String auteur) throws RemoteException {
		ArrayList<Livre> result = new ArrayList<Livre>();
		for (long key : bibliotheque.keySet()){
			if (bibliotheque.get(key).getAuteur().equals(auteur))
				result.add(bibliotheque.get(key));
		}
		return result.size()>0?result.toArray(new Livre[result.size()]):null;
	}

	@Override
	public Livre[] findByTitre(String titre) throws RemoteException {
		ArrayList<Livre> result = new ArrayList<Livre>();
		for (long key : bibliotheque.keySet()){
			if (bibliotheque.get(key).getTitre().equals(titre))
				result.add(bibliotheque.get(key));
		}
		return result.size()>0?result.toArray(new Livre[result.size()]):null;
	}
	/**
	 * Ajoute un livre a la base
	 */
	@Override
	public boolean addLivre(String isbn, String auteur, String titre, double prixEuros, Date dateAjout) throws RemoteException {
		LivreImpl livre = new LivreImpl();
		livre.setAuteur(auteur);
		livre.setIsbn(isbn);
		livre.setTitre(titre);
		livre.setPrixEuros(prixEuros);
		if (dateAjout==null)
			livre.setDateAjout(Calendar.getInstance().getTime());
		else	
			livre.setDateAjout(dateAjout);
		livre.setNumero(compteurLivre);
		compteurLivre++;
		bibliotheque.put(livre.getNumero(), livre);
		System.out.println(livre.remoteToString() +" vient d'etre ajoute a la base");
		return true;
	}
	
	/**
	 * Ajoute une personne a la base
	 */
	@Override
	public boolean addPersonne(Personne personne) throws RemoteException {
		if(findByEmail(personne.getEmail())==null){
			personne.setId(compteurPersonne);
			compteurPersonne++;
			System.out.println(personne.remoteToString() +" vient d'etre ajoute a la base");
			return annuaire.put(personne.getId(), personne) != null;
		}
		System.out.println("Une personne avec le mail: " + personne.getEmail() 
				+ " existe deja dans la base");
		return false;
	}
	/**
	 * Supprime une personne de la base
	 */
	@Override
	public boolean delPersonne(Personne personne) throws RemoteException {
		if(annuaire.containsKey(personne.getId())){
			System.out.println(personne.remoteToString() +" vient d'etre supprime de la base");
		}
		return bibliotheque.remove(personne.getId()) != null;

	}
	/**
	 * @return Trouve une personne par son email
	 */
	@Override
	public Personne findByEmail(String email) throws RemoteException {
		for (long key : annuaire.keySet()){
			if (annuaire.get(key).getEmail().equals(email))
				return annuaire.get(key);
		}
		return null;
	}

	/**
	 * @return achete les livres passes en parametre
	 */

	public boolean acheter(LivreService[] livres) throws RemoteException {
		if (livres==null)
			throw new NullPointerException();
		for (int i=0; i<Arrays.asList(livres).size(); i++){
			Livre l = bibliotheque.get(livres[i].getNumero());			
			if (delLivre(l))
				System.out.println(l.remoteToString() +" vient d'etre achete.");
			else
				System.out.println(l.remoteToString() +" n'a pu etre achete.");
		}
		return true;
	}
	/**
	 * Retourne la liste des livres disponibles à la vente.
	 * @return
	 * @throws RemoteException
	 */
	public LivreService[] getLivresCanSell() throws RemoteException {
		ArrayList<LivreService> livres = new ArrayList<LivreService>();
		for (Entry<Long, Livre> e : bibliotheque.entrySet()){
			if (e.getValue().canSell())
				livres.add(new LivreService(e.getValue()));
		}
		return livres.toArray(new LivreService[livres.size()]);
	}

	@Override
	public Livre[] getLivres() throws RemoteException {
		return bibliotheque.values().toArray(new Livre[bibliotheque.size()]);
	}
	
}
