import java.io.IOException;

public class CommitTester {
	public static void main (String[] args) throws Exception
	{
		Git g = new Git();
		g.initiate();
		
		g.add("testFile1.txt");
		g.add("testFile2.txt");
		Commit c1 = new Commit("first commit", "Lauren",null);
//		System.out.println(c1.getLocation());
////		c1.editFile("testFile1.txt");
		g.deleteFile("testFile1.txt");
//		check that delete is only for files added by previous commits
//		 
//		
		g.add("testFile3.txt");
		Commit c2 = new Commit("second commit", "Lauren",c1);
//		System.out.println(c2.getLocation());
		g.deleteFile("testFile2.txt");
//	
		g.add("testFile4.txt");
		Commit c3 = new Commit("third commit", "Lauren", c2);
////		System.out.println(c3.getLocation());
//		
//		g.deleteFile("testFile3.txt");
//		Commit 
		
		
	}
}
