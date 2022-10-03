import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

public class Commit {

	private Commit parent;
	private Commit next;
	private ArrayList<String> treeList;
	private String summary, author, date;
	private String sha1;
	private String sha1Tree;
	private ArrayList<String> blobList;
	private String correctTree;
	private int lineNumber;
	private String nextTree;

	// constructor
	public Commit(String sum, String auth, Commit p) throws IOException {
		author = auth;
		summary = sum;
		date = getDate();
		blobList = new ArrayList<String>();
		treeList = new ArrayList<String>();
		getFileName();
		String tLineOne = "";
		if (p != null)
		{
			FileInputStream fstream = new FileInputStream("objects/" + p.getFileName());
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine = "";

		//Read File Line By Line
		if ((strLine = br.readLine()) != null)   {
		  // Print the content on the console - do what you want to do
			tLineOne += strLine;
		}
		}
		Tree tree = new Tree (getTreeList(),tLineOne);
		sha1Tree = tree.getSha1();
		if (p != null)
		{
			parent = p;
			writeParent();	
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
	
	public Commit getNext()
	{
		return next;
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
		if (strLine.indexOf("*deleted*") == -1 && strLine.indexOf(lineNumber) == -1)
		{
			treeList.add(strLine);
		}
	}
		return treeList;
	}
	
	public void editFile(String fileName) throws IOException
	{
		addEntry(fileName,"edit");
	}
	
	public void deleteFile(String fileName) throws IOException
	{
		checkTreeForFile(parent.getLocation(),fileName);
		updateTree(getLocation());
		FileWriter fw = new FileWriter("objects/" + getLocation(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		int i;
		if (blobList.size()>0)
		{
		for (i=0; i<blobList.size()-1; i++)
		{
			bw.write(blobList.get(i) + "\n");
		}
		bw.write(blobList.get(i));
		bw.close();
		}
		addEntry(fileName, "delete");
	}
	public void updateTree(String tree) throws IOException
	{	
		File inputFile = new File("objects/" + tree);
		File tempFile = new File("myTempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine != null && trimmedLine.indexOf("tree")!= -1) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		tempFile.renameTo(inputFile);
//		File inputFile = new File("objects/" + tree);
//		File tempFile = new File("myTempFile.txt");
//
//		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//		
//		String currentLine;
//		while((currentLine = br.readLine()) != null){
//			String trimmedLine = currentLine.trim();
//		    if(trimmedLine.equals(removeID)){
//		            currentLine = "";
//		    }
//		    bw.write(currentLine + System.getProperty("line.separator"));
//
//		}
//		bw.close();
//		boolean delete = f.delete();
//		boolean b = temp.renameTo(f);
	}	
//		
//		File inputFile = new File("objects/" + tree);
//		File tempFile = new File("myTempFile.txt");
//
//		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//		int i=0;
//		String lineToRemove = "bbb";
//		String currentLine;
//
//		while((currentLine = reader.readLine()) != null) {
//			i++;
//		    if(i!=line)
//		    {
//		    	writer.write(currentLine);
//		    }
//		}
//		writer.close(); 
//		reader.close(); 
//		tempFile.renameTo(inputFile);
////		inputFile.delete();
		

	private void addEntry(String fileName, String directions) throws IOException
	{
		FileWriter fw = new FileWriter("index", true);
		BufferedWriter bw = new BufferedWriter(fw);
		if (directions.equals("edit"))
		{
			bw.write("*edited* " + fileName);
			bw.newLine();
			bw.close();
		}
		else if (directions.equals("delete"))
		{
			bw.write("*deleted* " + fileName);
			bw.newLine();
			bw.close();
		}
	}
	
	public void checkTreeForFile(String tree, String fileName) throws IOException
	{
		FileInputStream fstream = new FileInputStream("objects/" + tree);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine = "";

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			lineNumber++;
		// Print the content on the console - do what you want to do
		if (strLine.indexOf("blob") != -1 && strLine.indexOf(fileName) == -1)
		{
			blobList.add(strLine);
		}
		else if (strLine.indexOf(fileName) != -1)
		{
			addRemainingBlobs(tree,lineNumber);
			return;
		}
		else if (strLine.indexOf("tree") != -1)
		{
			nextTree = tree;
			checkTreeForFile(strLine.substring(7,47),fileName);
		}
		}
	}
	
	public void addRemainingBlobs(String tree, int line) throws IOException
	{
		FileInputStream fstream = new FileInputStream("objects/" + tree);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine = "";
		int i=0;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			i++;
		// Print the content on the console - do what you want to do
			if (i > line && strLine.indexOf("blob") != -1)
			{
				blobList.add(strLine);
			}
	}
	}
	
	private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}

// read in lines from index file and add to an array list becomes parameter of tree object
// The Index files will need to be reformatted???- add file names
// create parent tree like commit structure
// clear index file
// test & screenshots
	
//*deleted* file1.txt
