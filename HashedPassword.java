
/**
* This class creates a simple HashedPassword object that contains 
* fields for a hashed PW and salt
*
* @author  Chris C
* @version 1.0
* @since   2020-03-26 
*/

public class HashedPassword {
	
	//fields
	String password;
	String salt;
	
	//constructor
	public HashedPassword(String password, String salt) {
		this.password = password;
		this.salt = salt;
	}
	
	/**
	* Description: returns the hashed password
	* @param none
	* @return password (String)
	*/
	public String getPassword() {
		return this.password;
	}
	
	/**
	* Description: returns the salt
	* @param none
	* @return salt (String)
	*/
	public String getSalt() {
		return this.salt;
	}
	
	/**
	* Description: sets the hashed password field to a new value
	* @param password(String)
	* @return none
	*/
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	* Description: sets the salt field to a new value
	* @param salt(String)
	* @return none
	*/
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
