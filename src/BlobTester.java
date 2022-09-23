import java.io.IOException;
import java.security.NoSuchAlgorithmException;

	public class BlobTester {
		public static void main (String[] args) throws Exception 
		{
			Blob blob1 = new Blob("blobTestFile.txt");	
			System.out.println(blob1.getSHA1());
		}
	}
