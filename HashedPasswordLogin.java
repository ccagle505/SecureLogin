/**
* This class can be used to create basic login functionality.
* The class allows you to create a user profile consisting of a username and password
* This password is hashed for security.
* When a new user profile is created, a file is created to store the user information.
* The name of the file is the username. It will be saved to the same directory as the .java 
* file by default.
* The password is hashed and stored in the file along with the randomly generated salt 
* that was used to perform the hash. Each generated salt is unique.
* <p>
* When the user logs in, their user information in their user file is compared
* with the information they provide to determine successful or unsuccessful login.
*
* Note: password info is currently stored in the local project folder.
* Functionality will need to be added to send user file to specific locale.
* 
* @author  Chris C
* @version 1.0
* @since   2020-03-26 
*/


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class HashedPasswordLogin {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HashedPasswordLogin window = new HashedPasswordLogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HashedPasswordLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		//Login button logic
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String username = JOptionPane.showInputDialog(null,"Please enter your username");
					FileReader myFile = new FileReader(username + ".txt");
					//
					HashedPassword hashedObj = readInput(username);
					String password = JOptionPane.showInputDialog(null,"Please enter your password");
					String retrievedPassword = hashedObj.getPassword();
					String salt = hashedObj.getSalt();
					if(retrievedPassword.equals(getSecurePassword(password, salt))) {
						JOptionPane.showMessageDialog(null, "Successful Login.");
					}
					else{
						JOptionPane.showMessageDialog(null, "Failed Login.");
					}
				}
				
				catch(FileNotFoundException ioe){
					JOptionPane.showMessageDialog(null, "Invalid username. Please try again.");
				}

				frame.revalidate();
				frame.repaint();
			}
		});
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 5, 0);
		gbc_btnLogin.gridx = 5;
		gbc_btnLogin.gridy = 3;
		frame.getContentPane().add(btnLogin, gbc_btnLogin);
		
		//create user button logic
		JButton btnCreateNewUser = new JButton("Create New User");
		btnCreateNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String username = JOptionPane.showInputDialog(null,"Please enter your username");
					String password = JOptionPane.showInputDialog(null,"Please enter your password");
					boolean found = checkUsername(username);
					if(found == true) {
						JOptionPane.showMessageDialog(null, "Username is in use. Please try again.");
						return;
					}
					else {

					}
					fileWrite(username, password);
				}
				catch(Exception ioe){
					JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
				}
				frame.revalidate();
				frame.repaint();
			}
		});
		GridBagConstraints gbc_btnCreateNewUser = new GridBagConstraints();
		gbc_btnCreateNewUser.gridx = 5;
		gbc_btnCreateNewUser.gridy = 5;
		frame.getContentPane().add(btnCreateNewUser, gbc_btnCreateNewUser);
	}
	
	/**
	* Description: uses a MessageDigest object to create a hashed password.
	* The parameters include the original string entered as a password and 
	* the randomly generated salt.
	* The hashed password is returned.
	* @param String originalPW, String salt
	* @return String genPassword
	*/
	public static String getSecurePassword(String originalPW, String salt){
	    String genPassword = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(salt.getBytes(StandardCharsets.UTF_8));
	        byte[] bytes = md.digest(originalPW.getBytes(StandardCharsets.UTF_8));
	        StringBuilder builder = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++){
	            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        genPassword = builder.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return genPassword;
	}
	
	/**
	* Description: writes the randomly generated salt and the hashed Password
	* to the specified user profile. 
	* @param String username, String password
	* @return none
	*/
	
	public static void fileWrite(String username, String password) {
		try{	
			String salt = getRandSalt();
			password = getSecurePassword(password, salt);
		    String filename= username + (".txt");
		    FileWriter fw = new FileWriter(filename,false); 
		    BufferedWriter out = new BufferedWriter(fw);
		    out.write(password);
		    out.newLine();
		    out.write(salt);
		    out.close();
		}
		catch(IOException ioe){
		    System.err.println("IOException: " + ioe.getMessage());
		}
	}
	
	/**
	* Description: reads from specified user file and returns the hashed password
	* and the salt from the specified file. Returns a simple HashedPassword object
	* which contains the hashed password and salt for that particular user.
	* @param String nameFromFile
	* @return HashedPassword passwordObj
	*/
	public static HashedPassword readInput(String nameFromFile) {
	
	String password = null;
	String salt = null;
	boolean fileFound = false;
	//while loop continues until a valid file name is input
	while(fileFound == false) {
		try {
			
			File myFile = new File(nameFromFile.concat(".txt"));
			Scanner fileReader = new Scanner(myFile);
			
	
		    while(fileReader.hasNext()){

			    //variables store data read from input
			    password = fileReader.next();
			    salt = fileReader.next();
		    }
		  //if no File Not Found Exception is thrown, set boolean to true
	      fileFound = true;
	    }
	    catch (FileNotFoundException e) {
	     
	    }
	}
	HashedPassword passwordObj = new HashedPassword(password, salt);
	return passwordObj;
	} 
	
	/**
	* Description: randomly generates a salt with a 32-byte length
	* @param none
	* @return String salt
	*/
	public static String getRandSalt() {
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[32];
		rand.nextBytes(salt);
		return salt.toString();
	}
	
	/**
	* Description: Checks if the username already exists. The filename contains the username.
	* If the file is found, the filefound variable is set to trye. If it is not found, it is set to false.
	* @param String nameFromFile
	* @return Boolean fileFound
	*/
	public static boolean checkUsername(String nameFromFile) {
		boolean fileFound = false;
			try {
				
				File myFile = new File(nameFromFile.concat(".txt"));
				Scanner fileReader = new Scanner(myFile);
				
		      fileFound = true;
		    }
		    catch (FileNotFoundException e) {
		    	return false;
		    }
		return fileFound;
	}
}

