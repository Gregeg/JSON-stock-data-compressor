import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GroupStore {
	public static void main(String[] args) {
		File input = new File("files to compress");
		File[] files = input.listFiles();
		for(int i = 0; i < files.length; i++) {
			File out = new File("compress output/" + files[i].getName().substring(0, files[i].getName().length()-5));
			out.delete();
			try {
				out.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Store s = new Store(out);
			ArrayList<Product> p = JsonInterpret.getProducts(files[i], s);
			s.store(p);
		}
	}
}
