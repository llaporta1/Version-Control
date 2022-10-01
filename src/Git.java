import java.io.File;
import java.io.IOException;

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
		//creates new objects folder
		objects = new File("objects");		
		objects.mkdir();
		
		//sets count
		count = new File("objects").list().length;
			
		//initializes index
		index = new Index();
		}
	
	//adds blob to index and objects folder
	public void add(String fileName) throws Exception {
		//creates new blob in objects folder
		Blob b = new Blob(fileName);
		
		//checks if file is repeated
		int listLen = objects.list().length;
		if (count != listLen) {
			count++;
			//adds to index
			index.add(fileName, b);
		}
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
}
