import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class Commit {

	private Commit parent;
	private Commit next;
	private ArrayList<String> treeList = new ArrayList<String>();
	private String summary, author, date;
	private String sha1;
	private String sha1Tree;

	// constructor
	public Commit(String sum, String auth, Commit p) throws IOException {
		author = auth;
		summary = sum;
		date = getDate();
		getFileName();
		String tLineOne = "";
		Tree tree = new Tree (getTreeList(),tLineOne);
		sha1Tree = tree.getSha1();
		if (p != null)
		{
			parent = p;
			writeParent();
			
			FileInputStream fstream = new FileInputStream("objects/" + p.getFileName());
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = "";

			//Read File Line By Line
			if ((strLine = br.readLine()) != null)   {
			  // Print the content on the console - do what you want to do
				tLineOne += strLine;
		}
		}
		writeFile();
		        FileWriter fw = new FileWriter("index", false); 
		        PrintWriter pw = new PrintWriter(fw, false);
		        pw.flush();
		        pw.close();
		        fw.close();
		}

	// method of reading in from parent
	// also changes second line to this location
	// writes back out to parent file
	private void writeParent() throws IOException {
		parent.setChild(this);
		parent.writeFile();
	}

	// set next
	public void setChild(Commit child) {
		next = child;
	}

	// getsFileName
	public String getFileName() {
		//NOT CHILD!!
		String fileName = getContentsSHA1();
		return fileName;
	}
	
	public String getContentsSHA1()
	{
		String contents = "";
		contents += summary + "\n";
		contents += date + "\n"; //private instance
		contents += author + "\n";
		if (parent != null)
		{
			contents += parent;
		}
//		System.out.println("po"+contents);
		String s = encryptThisString(contents);
//		System.out.println(s);
		return s;
	}
//	
//	// SHA1 method
	private String encryptThisString(String input) {
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
	

	// writing method
	public void writeFile() throws IOException {
		ArrayList<String> arr = getContents();
		File file = new File(getContentsSHA1());

		FileWriter fw = new FileWriter("objects/" + file);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String s : arr) {
			if (s != null) {
				bw.write(s);
			}
			bw.newLine();
		}
		bw.close();
		fw.close();
	}

	// gets contents of file
	private ArrayList<String> getContents() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(sha1Tree); //how to change to pointer to Tree
		if (parent != null) {
			arr.add(parent.getLocation());
		} else {
			arr.add(null);
		}
		if (next != null) {
			arr.add(next.getLocation());
		} else {
			arr.add(null);
		}
		arr.add(author);
		arr.add(date);
		arr.add(summary);
		return arr;
	}

	// gets location of commit
	public String getLocation() {
		return sha1Tree;
	}

	// date method
	private String getDate() {
		Date date = new Date();
		return date.toString();
	}

	public ArrayList<String> getTreeList() throws IOException
	{
	FileInputStream fstream = new FileInputStream("index");
	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

	ArrayList<String> treeList = new ArrayList<String>();
	String strLine = "";
	String str = "";

	//Read File Line By Line
	while ((strLine = br.readLine()) != null)   {
	  // Print the content on the console - do what you want to do
		treeList.add(strLine);
	}
		return treeList;
	}

}

// read in lines from index file and add to an array list becomes parameter of tree object
// The Index files will need to be reformatted???- add file names
// create parent tree like commit structure
// clear index file
// test & screenshots
	
//*deleted* file1.txt
