import java.awt.Rectangle;

public class Blacksmith extends Building
{
	
	public Blacksmith(Rectangle rect)
	{
		name = "Blacksmith";
		makes = "ironTools";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 0;
		stonePrice = 300.0;
		ironIngotPrice = 200.0;
		productionMultiplier = 0.75;
	}
	
	public void updateNextCost()//Calculate new building's cost
	{
		stonePrice += owned * priceRaiseMultiplier;
		ironIngotPrice += owned * priceRaiseMultiplier;
	}
	
	public String getPrice()
	{
		return "Stone: " + (String)GameLogic.format.format(stonePrice) + " Iron Ingot: " + (String)GameLogic.format.format(ironIngotPrice);
	}
}