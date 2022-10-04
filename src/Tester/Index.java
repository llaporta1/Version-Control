package Tester;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Index {

	//index
	private File index;
	
	//constructor
	public Index() throws IOException {
		//initializes index file
		index = new File("index");
		index.createNewFile();	
		}
	
	//creates and adds new blob to index and objects folder
	public void add(String fileName, Blob blob) throws Exception {
		//adds blob to index
		FileWriter fw = new FileWriter(index, true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("blob" + " : " + blob.getSHA1() + " " + fileName);
		bw.newLine();
		bw.close();
	}
	
	//removes blob from index
	public void remove(String fileName, Blob blob) throws IOException {
		//reads index for blob and removes
		File temp = File.createTempFile("file", ".txt", index.getParentFile());
		String charset = "UTF-8";
		String delete = fileName + " : " + blob.getSHA1();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(index), charset));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temp), charset));
		for (String line; (line = reader.readLine()) != null;) {
			line = line.replace(delete, "");
			writer.print(line);
		}
		reader.close();
		writer.close();
		index.delete();
		temp.renameTo(index);
	}
}
