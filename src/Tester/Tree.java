package Tester;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Tree {
	private String output;
	private String sha1;
	private Tree parent;
	private ArrayList<String> blobList = new ArrayList<String>();
	private ArrayList<String> pairs = new ArrayList<String>();
	private String pSha1;
	private String fileToDelete;
	private int lineNumber;
	
	public Tree (ArrayList<String> prs, String parentSha1) throws IOException {
		output="";
		pSha1 = parentSha1;
		pairs = prs;
		if (containsDelete() == true)
		{
			deleteFile(fileToDelete);
		}
		else if (containsDelete() == false && pSha1 != "")
		{
			output += "tree : " + pSha1 + "\n";
		}
		if (output.length()==0)
		{
			int i;
			for(i = 0;i < pairs.size()-1;i++) {
			    if (pairs.get(i)!= null && pairs.get(i).indexOf('*') == -1)
				{
			    		output += pairs.get(i) + "\n";
				}
			}	
			output += pairs.get(i);
		}
		else
		{
			int k;
			
			for(k = 0;k < pairs.size();k++) 
			{
				if (pairs.get(k)!= null && pairs.get(k).indexOf('*') == -1)
				{
			    output += "\n" + pairs.get(k);
				}
			}
		}
//		if (output.length()>)
//		output += "\n" + pairs.get(i);
//		for (String pair: pairs) {
//			
//			output+=pair+"\n";
//		}
		sha1 = generateSha1(output);
		Path path = Paths.get("objects"+File.separator+sha1);
        try {
            Files.writeString(path, output, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public boolean containsDelete()
	{
		for (String s: pairs)
		{
			if (s.indexOf('*') != -1)
			{
				fileToDelete = (s.substring(10));
				return true;
			}
		}
		return false;
	}
	
	public void deleteFile(String fileName) throws IOException
	{
		checkTreeForFile(pSha1,fileName);
		int i;
		if (blobList.size()>0)
		{
		for (i=0; i<blobList.size()-1; i++)
		{
			output += blobList.get(i) + "\n";
		}
		output += blobList.get(i);
		}
	}
//	public void updateTree(String tree) throws IOException
//	{	
//		File inputFile = new File("objects/" + tree);
//		File tempFile = new File("myTempFile.txt");
//
//		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//		String currentLine;
//
//		while((currentLine = reader.readLine()) != null) {
//		    // trim newline when comparing with lineToRemove
//		    String trimmedLine = currentLine.trim();
//		    if(trimmedLine != null && trimmedLine.indexOf("tree")!= -1) continue;
//		    writer.write(currentLine + System.getProperty("line.separator"));
//		}
//		writer.close(); 
//		reader.close(); 
//		tempFile.renameTo(inputFile);
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
//	}	
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
	
	private String generateSha1(String text) {
		String sha1 = "";
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(text.getBytes("utf8"));
	        sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return sha1;
	}
	
	public String getSha1(){
		return sha1;
	}
}
