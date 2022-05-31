import java.awt.Rectangle;

public class Woodshop extends Building
{

	public Woodshop(Rectangle rect)
	{
		name = "Woodshop";
		makes = "wood";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 1;
		woodPrice = 10.0;
	}
	
	public void updateNextCost()//Calculate new building's cost
	{
		woodPrice += owned * priceRaiseMultiplier;
	}
	
	public String getPrice()
	{
		return "Wood: " + (String)GameLogic.format.format(woodPrice);
	}
}