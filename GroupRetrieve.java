import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class GroupRetrieve {
	public static void main(String[] args) {
		File input = new File("files to decompress");
		File[] files = input.listFiles();
		
		try {
			for(int i = 0; i < files.length; i++) {
				File out = new File("decompressed output/" + files[i].getName() + ".json");
				out.delete();
				try {
					out.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
				
				int pos = 0;
				
				Store s = new Store(new File("useless file"));
				s.foss.close();
				FileInputStream fis = new FileInputStream(files[i]);
				ArrayList<Product> prods = new ArrayList<Product>();
				
				outer:
					while(pos <= Integer.MAX_VALUE) {
						byte[] bytes = new byte[1048576];
						fis.read(bytes, pos, 1048576);
						pos+=1048576;
						ByteBuffer wrapped = ByteBuffer.wrap(bytes);
						
						s.lastUpdated = wrapped.getLong(0);
						int ind = 8; 
						int prodInd = 0;
						while(ind <= 1048576) {
							byte tAmt = bytes[ind];
							if(tAmt == (byte)0xff)
								break outer;
							ind++;
							
							ArrayList<Transaction> sell = new ArrayList<Transaction>(), buy = new ArrayList<Transaction>();
							ind = RetrieveData.setTransactions(sell, wrapped, tAmt, ind);
							ind = RetrieveData.setTransactions(buy, wrapped, bytes[ind], ind+1);
							
							QuickSummary qs = new QuickSummary(wrapped.getFloat(ind), wrapped.getInt(ind+4), wrapped.getInt(ind+8), wrapped.getInt(ind+12),
										wrapped.getFloat(ind+16), wrapped.getInt(ind+20), wrapped.getInt(ind+24), wrapped.getInt(ind+28));
							ind += 32;
							try {
								prods.add(new Product(s.itemsArr[prodInd], sell, buy, qs));
							} catch(ArrayIndexOutOfBoundsException e) {
								System.err.print("ERROR\nitems in dataFile exceeds item count in item.json\nitems.json likely needs to be updated with new items");
								System.exit(0);
							}
							prodInd++;
						}
					}
				fis.close();
				
				
				File f = out;
				f.delete();
				f.createNewFile();
				FileWriter w = new FileWriter(f);
				w.write(Product.createJSONObject(prods, s) + "");
				w.flush();
				w.close();
				System.out.println("finished writing to file: " + out.getName() );
				
				
				
			}
			new File("useless file").delete();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
