
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class IndexTester {
		public static void main(String[] args) throws NoSuchAlgorithmException, IOException
		{
			Index i = new Index();
			i.init();
			i.add("indexTestFile.txt.rtf");
			i.add("blobTestFile.txt");
			i.remove("indexTestFile.txt.rtf");
		}
	}
