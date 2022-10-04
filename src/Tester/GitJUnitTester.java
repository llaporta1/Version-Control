package Tester;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class GitJUnitTester {

	@Test
	void test() throws Exception {
		Git g = new Git();
		g.initiate();
		
		g.add("testFile1.txt");
		g.add("testFile2.txt");
		Commit c1 = new Commit("first commit", "Lauren",null);
		
//		String headToString = readFileAsString("HEAD");
//		System.out.println(headToString);
//		String getTree = getCommitTree("objects/" + headToString);
//		System.out.println(getTree);
//		boolean test1 = checkTree("testFile1.txt");
//		System.out.println(test1);
//		boolean test2 = checkTree("testFile2.txt");
//		System.out.println(test2);
//		assertTrue(test1 == true && test2 == true);
		
//		System.out.println(c1.getLocation());
////		c1.editFile("testFile1.txt");
		g.deleteFile("testFile1.txt");
//		check that delete is only for files added by previous commits
//		 
//		
		g.add("testFile3.txt");
		Commit c2 = new Commit("second commit", "Lauren",c1);
		
//		String headToString2 = readFileAsString("HEAD");
//		String getTree2 = getCommitTree("objects/" + headToString);
//		boolean test3 = checkTree("testFile1.txt");
//		System.out.println(test3);
//		boolean test4 = checkTree("testFile3.txt");
//		System.out.println(test4);
//		assertTrue(test3 == false && test4 == true);
		
//		System.out.println(c2.getLocation());
		g.deleteFile("testFile2.txt");
//		g.edit("testFile3.txt");
//	
		g.add("testFile4.txt");
		Commit c3 = new Commit("third commit", "Lauren", c2);
//		g.edit("testFile4.txt");
		
//		String headToString3 = readFileAsString("HEAD");
//		String getTree3 = getCommitTree("objects/" + headToString);
//		boolean test5 = checkTree("testFile2.txt");
//		boolean test6= checkTree("testFile4.txt");
//		assertTrue(test5 == false && test6 == true);
//		
		g.deleteFile("testFile4.txt");
		g.add("testFile5.txt");
		
		Commit c4 = new Commit("fourth commit", "Lauren", c3);
//		
//		String headToString4 = readFileAsString("HEAD");
//		String getTree4 = getCommitTree("objects/" + headToString);
//		boolean test7 = checkTree("testFile4.txt");
//		boolean test8= checkTree("testFile5.txt");
//		assertTrue(test7 == false && test8 == true);
//	
		g.deleteFile("testFile3.txt");
		
		Commit c5 = new Commit("fifth commit", "Lauren", c4);
		
//		String headToString5 = readFileAsString("HEAD");
//		String getTree5 = getCommitTree("objects/" + headToString);
//		boolean test9 = checkTree("testFile3.txt");
//		assertTrue(test9 == false);
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
	
	private String getCommitTree(String filePath) throws IOException
	{
		FileInputStream fstream = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine = "";
		String tree = "";

		//Read File Line By Line
		if ((strLine = br.readLine()) != null)   {
		  // Print the content on the console - do what you want to do
				tree = strLine;
		}
		return tree;

	}
		
	private boolean checkTree(String fileName) throws IOException
	{
		FileInputStream fstream = new FileInputStream(fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console - do what you want to do
		  if (strLine.indexOf(fileName) != -1)
		  {
			  fstream.close();
			  return false;
		  }
		}

		//Close the input stream
		fstream.close();
		return true;
	}
}
