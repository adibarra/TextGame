import java.awt.Rectangle;

public class Mine extends Building
{
	
	public Mine(Rectangle rect)
	{
		name = "Mine";
		makes = "ore";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 0;
		stonePrice = 50.0;
	}
	
	public void updateNextCost()//Calculate new building's cost
	{
		stonePrice += owned * priceRaiseMultiplier;
	}
	
	public String getPrice()
	{
		return "Stone: " + (String)GameLogic.format.format(stonePrice);
	}
}