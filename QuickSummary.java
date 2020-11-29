import org.json.JSONObject;

public class QuickSummary {
	public float sellPrice, buyPrice;
	public int sellVolume, sellMovingWeek, sellOrders,
			buyVolume, buyMovingWeek, buyOrders;
	
	public QuickSummary(float sellPrice, int sellVolume, int sellMovingWeek, int sellOrders,
			float buyPrice, int buyVolume, int buyMovingWeek, int buyOrders) {
		this.sellPrice = sellPrice;
		this.sellVolume = sellVolume;
		this.sellMovingWeek = sellMovingWeek;
		this.sellOrders = sellOrders;
		this.buyPrice = buyPrice;
		this.buyVolume = buyVolume;
		this.buyMovingWeek = buyMovingWeek;
		this.buyOrders = buyOrders;
	}
	
	public JSONObject toJSONObject(String name) {
		return new JSONObject().put("sellPrice", sellPrice).put("sellVolume", sellVolume).put("sellMovingWeek", sellMovingWeek).put("sellOrders", sellOrders)
					.put("buyPrice", buyPrice).put("buyVolume", buyVolume).put("buyMovingWeek", buyMovingWeek).put("buyOrders", buyOrders)
					.put("productId", name);
	}
}
