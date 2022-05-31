import java.awt.Rectangle;

public class Building
{
	String name = "null";
	String makes = "null";
	Rectangle button = new Rectangle();
	Rectangle button2 = new Rectangle();
	double productionSpeed = 0.1;
	double productionMultiplier = 1.0;
	double priceRaiseMultiplier = 1.90;
	double woodPrice = 0.0;
	double stonePrice = 0.0;
	double ironOrePrice = 0.0;
	double ironIngotPrice = 0.0;
	double owned = 0;
	
	public Building() 
	{
		
	}
	
	public Building(Rectangle rect)
	{
		name = "Building";
		button = rect;
		button2 = new Rectangle(button.x+button.width+1,button.y,20,button.height);
		owned = 0;
	}
}
