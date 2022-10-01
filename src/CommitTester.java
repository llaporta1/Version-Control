import java.io.IOException;

public class CommitTester {
	public static void main (String[] args) throws Exception
	{
		Git g = new Git();
		g.initiate();
		
		g.add("testFile1.txt");
		Commit c1 = new Commit("first commit", "Lauren",null);
		System.out.println(c1.getLocation());
		
		
		g.add("testFile2.txt");
		g.add("testFile3.txt");
		Commit c2 = new Commit("second commit", "Lauren",c1);
		System.out.println(c2.getLocation());
	
		g.add("testFile4.txt");
		Commit c3 = new Commit("third commit", "Lauren", c2);
		System.out.println(c3.getLocation());
		
//		Blob b1 = new Blob("testFile1.txt");
//		Blob b2 = new Blob("testFile2.txt");
//		Blob b3 = new Blob("testFile3.txt");
//		Blob b4 = new Blob("testFile4.txt");
		
		
	}
}
