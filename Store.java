import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Store {
	private int curArrayPos = 0;
	private int groupsOf32 = 0,
			prevMB = 0;
	private byte[] bytes = new byte[16777216];
	public FileOutputStream foss;
	public JSONObject items;
	public String[] itemsArr;
	public long lastUpdated;
	
	public static void main(String[] args) {
		System.out.println(args[0]);
		System.exit(0);
		Store s = new Store(new File("output"));
		ArrayList<Product> p = JsonInterpret.getProducts(new File("data.json"), s);
		s.store(p);
	}
	
	public Store(File outputFile) {
		try {
			File file = outputFile;
			if(!file.createNewFile()) {
				file.delete();
				file.createNewFile();
			}
			foss = new FileOutputStream(file);
			
			InputStream input = new FileInputStream("items.json");
			items = new JSONObject(new JSONTokener(input));
			input.close();
			
			itemsArr = new String[items.length()];
			Iterator<String> iter = items.keys();
			while(iter.hasNext()) {
				String item = iter.next();
				itemsArr[items.getInt(item)] = item;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void store(ArrayList<Product> products) {
		store(lastUpdated);
		
		for(int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			store((byte)p.sell.size());
			for(int t = 0; t < p.sell.size(); t++) {
				Transaction tran = p.sell.get(t);
				store(tran.amount);
				store(tran.pricePerUnit);
				store(tran.orders);
			}
			
			store((byte)p.buy.size());
			for(int t = 0; t < p.buy.size(); t++) {
				Transaction tran = p.buy.get(t);
				store(tran.amount);
				store(tran.pricePerUnit);
				store(tran.orders);
			}
			
			QuickSummary qs = p.qs;
			store(qs.sellPrice);
			store(qs.sellVolume);
			store(qs.sellMovingWeek);
			store(qs.sellOrders);
			store(qs.buyPrice);
			store(qs.buyVolume);
			store(qs.buyMovingWeek);
			store(qs.buyOrders);
		}
		store((byte)0xff);
		finish();
	}
	
	// stores the byte
	private void store(byte val) {
//		curByte <<= 2;
//		curByte |= val;      manipulate bytes from connect four game
//		curBitPos += 2;
		bytes[curArrayPos] = val;
		if(curArrayPos == 16777215) {
			curArrayPos = -1;
			
			try {
				foss.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bytes = new byte[16777216];
			groupsOf32++;
			int mb = (int)getMB();
			if(mb != prevMB) {
				System.out.println("File used " + mb + " MB Used");
				prevMB = mb;
			}
		}
		curArrayPos++;
	}
	private void store(byte[] b) {
		for(int i = 0; i < b.length; i++)
			store(b[i]);
	}
	private void store(short val) {
		store(ByteBuffer.allocate(2).putShort(val).array());
	}
	private void store(int val) {
		store(ByteBuffer.allocate(4).putInt(val).array());
	}
	private void store(float val) {
		store(ByteBuffer.allocate(4).putFloat(val).array());
	}
	private void store(long val) {
		store(ByteBuffer.allocate(8).putLong(val).array());
	}
	
	public double getMB() {
		return (groupsOf32)*16.777216;
	}
	public void finish() {
		System.out.println(curArrayPos);
		byte[] b = new byte[curArrayPos];
		for(int i = 0; i < curArrayPos; i++)
			b[i] = bytes[i];
		try {
			foss.write(b);
			foss.flush();
			foss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	//adds new item to item.json
	public void addItem(String itemName) {
		items.put(itemName, items.length());
		
		try {
			File f = new File("items.json");
			f.delete();
			f.createNewFile();
			FileWriter w = new FileWriter(f);
			w.write(items.toString());
			w.flush();
			w.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		String[] temp = new String[itemsArr.length + 1];
		for(int i = 0; i < itemsArr.length; i++)
			temp[i] = itemsArr[i];
		temp[temp.length-1] = itemName;
		itemsArr = temp;
	}
	
	public Product[] sortProducts(ArrayList<Product> products) {
		Product[] out = new Product[products.size()];
		Iterator<Product> iter = products.iterator();
		while(iter.hasNext()) {
			Product p = iter.next();
			try {
				out[items.getInt(p.name)] = p;
			} catch(JSONException e) {
				addItem(p.name);
				out[items.getInt(p.name)] = p;
			}
		}
		return out;
	}
}
