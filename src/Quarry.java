import java.awt.Rectangle;

public class Quarry extends Building
{
	
	public Quarry(Rectangle rect)
	{
		name = "Quarry";
		makes = "stone";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 0;
		woodPrice = 50.0;
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