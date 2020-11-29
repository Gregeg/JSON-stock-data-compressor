import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Transaction {
	public int amount;
	float pricePerUnit;
	short orders;
	
	public Transaction(int amount, float pricePerUnit, short orders) {
		this.amount = amount;
		this.pricePerUnit = pricePerUnit;
		this.orders = orders;
	}
	
	public JSONObject toJSONObject() {
		return new JSONObject().put("amount", amount)
				.put("pricePerUnit", pricePerUnit)
				.put("orders", orders);
	}
	
	public static JSONArray createJSONArray(ArrayList<Transaction> trans) {
		JSONArray a = new JSONArray();
		for(int i = 0; i < trans.size(); i++)
			a.put(trans.get(i).toJSONObject());
		return a;
	}
}
