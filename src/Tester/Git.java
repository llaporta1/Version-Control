package Tester;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Git {

	//index
	private Index index;
	
	//objects
	private File objects;
	
	//blob counter
	private int count;
	
	public Git() {
		
	}
	
	//initiating method
	public void initiate() throws IOException {
		new File("objects").mkdir();
		File f = new File("objects/" + "index");
		//creates new objects folder
		objects = new File("objects");	
//		Files.createDirectories(Paths.get("/path/to/directory"));

		objects.mkdir();
//		
		//sets count
		count = new File("objects").list().length;
			
		//initializes index
		index = new Index();
////		HEAD = new File("HEAD");
		}
	
	//adds blob to index and objects folder
	public void add(String fileName) throws Exception {
		//creates new blob in objects folder
		Blob b = new Blob(fileName);
		index.add(fileName, b);
	
	}
	
	//removes blob from index and objects folder
	public void remove(String fileName) throws Exception {
		//creates blob for removal purposes
		Blob b = new Blob(fileName);
		
		//checks if file existed
		//if blob already existed they would be equal so subtract from count and remove from index
		//if not equal still removes blob
		if (count == objects.list().length) {	
			//removes from index
			index.remove(fileName, b);
			
			count--;
		}
		
		//removes from objects folder
		File f = new File ("objects/"+b.getSHA1());
		f.delete();
	}
	
	public void edit(String fileName) throws Exception
	{
		add(fileName);
		deleteFile(fileName);
//		link to edited file 
//		create two new trees
	}
	
	public void deleteFile(String fileName) throws IOException
	{
		addEntry(fileName, "delete");
	}
	
	private void addEntry(String fileName, String directions) throws IOException
	{
		FileWriter fw = new FileWriter("index", true);
		BufferedWriter bw = new BufferedWriter(fw);
		if (directions.equals("delete"))
		{
			bw.write("*deleted* " + fileName);
			bw.newLine();
			bw.close();
		}
//		else if (directions.equals("edit"))
//		{
//			bw.write("*edited* " + fileName);
//			bw.newLine();
//			bw.close();
//		}
	}
}
	
//	
////	2 conditions for HEAD
////	Init creates index & objects folder & creates file called Head that is blank just like the Index file
////	when you create a commit head shoiuld ONLY have the sha of the most recent commitr
////	instead of reading in parent for commit read in head- get rid of parent commit parametr 
//head is NOT in the objects folder
//}
