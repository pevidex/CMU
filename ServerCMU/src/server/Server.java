package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import command.Command;
import command.LoginCommand;
import command.RegisterCommand;
import response.Response;

import security.*;


public class Server {
	
	private static final int PORT = 9090;

	public static void main(String[] args) throws Exception {
		CommandHandlerImpl chi = new CommandHandlerImpl();
		ServerSocket socket = new ServerSocket(PORT);
		Socket client = null;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Server now closed.");
				try { socket.close(); }
				catch (Exception e) { }
			}
		});
		
		System.out.println("Server is accepting connections at " + PORT);
		
	//	testDebug();
	 //   createKeypair();
	//	testDebug2();
	//	createSignKey();
		
		while (true) {
			try {
			client = socket.accept();
			ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
			Command cmd =  (Command) ois.readObject();
			Response rsp = cmd.handle(chi);
			
			ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
			oos.writeObject(rsp);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (client != null) {
					try { client.close(); }
					catch (Exception e) {}
				}
			}
		}
	}	
	
	

	private static void createSignKey() {
		try {
			SignKeypair signKeypair = new SignKeypair();
			byte[] pbk = signKeypair.getSignPbkBytes();
			byte[] prk = signKeypair.getSignPrkBytes();
			signKeypair.writeToFile("KeyPair/signPbk", pbk);
			signKeypair.writeToFile("KeyPair/signPrk", prk);
 		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



	private static void createKeypair() {
		
			GenerateKeys gk;
			try {
				gk = new GenerateKeys(1024);
				gk.createKeys();
				gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
				gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
			} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
				System.err.println(e.getMessage());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}

		
		
	}

	public static void testDebug() throws IOException, NoSuchProviderException, InvalidKeyException, SignatureException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
		
		SignKeypair signKeypair = new SignKeypair();
		//PublicKey pbk = signKeypair.getSignPbk(signKeypair.getSignPbkBytes());
		
		RegisterCommand lc = new RegisterCommand("code","name", signKeypair.getSignPbkBytes());
		SignedObject lc_signed= new SignedObject(lc, signKeypair.getSign_prk());
		RegisterCommand deser_lc = (RegisterCommand) lc_signed.getObject();
		System.out.println(deser_lc.getUsername());
		if(lc_signed.verify(deser_lc.getPubkey()))
			System.out.println("succuess");

	}
	
	private static void testDebug2() throws NoSuchPaddingException, NoSuchAlgorithmException {
		GenerateKeys gk;
		PrivateKey serverPrk = null;
		PublicKey serverPbk = null;
		try {
			gk = new GenerateKeys(1024);
			serverPrk =  gk.getPrivate("KeyPair/privateKey");
			serverPbk = gk.getPublic("KeyPair/publicKey");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecretKey sk = CryptoManager.generateSK();
		byte[] encrypted = CryptoManager.encryptSK(sk.getEncoded(), serverPbk.getEncoded());
		try {
			CryptoManager.revertSK(encrypted, serverPrk);
		} catch (InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("final");
		
		
	}
	
}
