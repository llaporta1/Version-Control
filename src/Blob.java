
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {

	//SHA1 String
	private String SHA1;

	//constructor
	public Blob(String fileName) throws Exception {
		//creates SHA1 string of contents
		SHA1 = encryptThisString(getFileString(fileName));
		
		//creates new file called SHA1
		File file = new File("objects/" + SHA1);	
		file.createNewFile();
		
		//puts file in objects folder
		FileWriter fw = new FileWriter(file);
		
		//copies contents into file
		fw.write(getFileString(fileName));
		fw.close();
	}
	
	//returns SHA1 hash
	public String getSHA1() {
		return SHA1;
	}
	
	//gets contents of file
	private  String getFileString(String fileName) throws IOException{
		Path filePath = Path.of(fileName);
		
		return Files.readString(filePath);
	}
	
	//SHA1 method
	private String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
 
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());
 
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
 
            // Convert message digest into hex value
            String hashtext = no.toString(16);
 
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
 
            // return the HashText
            return hashtext;
        }
 
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
}
