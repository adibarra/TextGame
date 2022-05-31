import java.awt.Rectangle;

public class Smeltery extends Building
{
	
	public Smeltery(Rectangle rect)
	{
		name = "Smeltery";
		makes = "ironIngot";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 0;
		woodPrice = 50.0;
		stonePrice = 150.0;
	}
	
	public void updateNextCost()//Calculate new building's cost
	{
		woodPrice += owned * priceRaiseMultiplier;
		stonePrice += owned * priceRaiseMultiplier;
	}
	
	public String getPrice()
	{
		return "Wood: " + (String)GameLogic.format.format(woodPrice) + " Stone: " + (String)GameLogic.format.format(stonePrice);
	}
}