import java.util.ArrayList;
import org.json.JSONObject;

public class Product {
	ArrayList<Transaction> buy, sell;
	QuickSummary qs;
	String name;
	
	public Product(String name, ArrayList<Transaction> sell, ArrayList<Transaction> buy, QuickSummary qs) {
		this.qs = qs;
		this.buy = buy;
		this.sell = sell;
		this.name = name;
	}
	
	public static JSONObject createJSONObject(ArrayList<Product> prods, Store s) {
		JSONObject prodsObj = new JSONObject();
		for(int i = 0; i < prods.size(); i++) {
			Product p = prods.get(i);
			prodsObj.put(p.name, p.toJSONObject());
		}
		
		return new JSONObject().put("success", true).put("lastUpdated", s.lastUpdated).put("products", prodsObj);
	}
	public JSONObject toJSONObject() {
		return new JSONObject().put("product_id", name)
				.put("sell_summary", Transaction.createJSONArray(sell))
				.put("buy_summary", Transaction.createJSONArray(buy))
				.put("quick_status", qs.toJSONObject(name));
	}
}
