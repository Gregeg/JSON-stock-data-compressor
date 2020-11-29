
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonInterpret {
	public static ArrayList<Product> getProducts(File inputFile, Store s){
		ArrayList<Product> products = new ArrayList<Product>();
		
		try {
			InputStream input = new FileInputStream(inputFile);
			JSONObject obj = new JSONObject(new JSONTokener(input));
			input.close();
			
			s.lastUpdated = obj.getLong("lastUpdated");
			
			JSONObject prods = (JSONObject) obj.get("products");
			Iterator<String> prodsIt = prods.keys();
			
			while(prodsIt.hasNext()){
				ArrayList<Transaction> sellList = new ArrayList<Transaction>();
				ArrayList<Transaction> buyList = new ArrayList<Transaction>();
				
				String prodName = prodsIt.next();
				JSONObject prod = (JSONObject) prods.get(prodName);
//				System.out.println(prodName);
				
				JSONArray sell = (JSONArray) prod.get("sell_summary");
				for(int i = 0; i < sell.length(); i++) {
					JSONObject tran = (JSONObject) sell.get(i);
					sellList.add(new Transaction(tran.getInt("amount"), tran.getFloat("pricePerUnit"), (short)tran.getInt("orders")));
				}
				
				JSONArray buy = (JSONArray) prod.get("buy_summary");
				for(int i = 0; i < buy.length(); i++) {
					JSONObject tran = (JSONObject) buy.get(i);
					buyList.add(new Transaction(tran.getInt("amount"), tran.getFloat("pricePerUnit"), (short)tran.getInt("orders")));
				}
				
				JSONObject quick = prod.getJSONObject("quick_status");
				QuickSummary qs = new QuickSummary(quick.getFloat("sellPrice"), quick.getInt("sellVolume"), quick.getInt("sellMovingWeek"), quick.getInt("sellOrders"),
							quick.getFloat("buyPrice"), quick.getInt("buyVolume"), quick.getInt("buyMovingWeek"), quick.getInt("buyOrders"));
				
				products.add(new Product(prodName, sellList, buyList, qs));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return products;
	}
}
