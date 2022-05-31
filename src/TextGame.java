import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class TextGame
{
	public static void main(String args[])
	{
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("mac os x")) 
		{
			//Makes Command+Q activate the windowClosing windowEvent
			System.setProperty("apple.eawt.quitStrategy","CLOSE_ALL_WINDOWS");
		}
		
		GameLogic tester = new GameLogic();
		tester.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				tester.saveGame();
				System.exit(0);
			}
		});
		tester.setSize(1200,650);
		tester.setVisible(true);
	}
}

@SuppressWarnings("serial")
class GameLogic extends Frame implements MouseListener,MouseMotionListener,KeyListener
{	
	static Graphics2D g2;
	static Image virtualMem = null;
	static boolean started = false;
	static DecimalFormat format = new DecimalFormat("0.00");
	static int clickx, clicky, hoverx, hovery, counter = 0;
	static ArrayList<Building> buildings = new ArrayList<Building>();
	static ArrayList<Material> materials = new ArrayList<Material>();
	static final double GAME_VERSION = 0.1;
	
	//TODO MANUAL DATA ADDING
	Building tempBuilding = new Building();
	Woodshop woodshop =     new Woodshop(  new Rectangle(0,031,100,100));
	Quarry quarry =         new Quarry(    new Rectangle(0,125,100,100));
	Mine mine =             new Mine(      new Rectangle(0,225,100,100));
	Smeltery smeltery =     new Smeltery(  new Rectangle(0,325,100,100));
	Blacksmith blacksmith = new Blacksmith(new Rectangle(0,425,100,100));
	Building tempB =        new Building(  new Rectangle(0,525,100,100));
	
	Material tempMaterial = new Material();
	Material wood =         new Material("Wood");
	Material stone =        new Material("Stone");
	Material ore =          new Material("Ore");
	Material ironIngot =    new Material("IronIngot");
	Material ironTools =    new Material("IronTools");
					
   
	public GameLogic()
	{
		super("TextGame");
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		//TODO MANUAL DATA ADDING
		buildings.add(woodshop);
		buildings.add(quarry);
		buildings.add(mine);
		buildings.add(smeltery);
		buildings.add(blacksmith);
		buildings.add(tempB);
		
		materials.add(wood);
		materials.add(stone);
		materials.add(ore);
		materials.add(ironIngot);
		materials.add(ironTools);
				
		try {
			firstLoad();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g)
	{  
		virtualMem = createImage(1200,650);//create image with screen size
		g2 = (Graphics2D) virtualMem.getGraphics();
		Graphics2D g2D = (Graphics2D) g2;
		g2D.translate(1,23);
		
        if(!started)
        {
        	titleScreen(g2D);
        	delay(200);
        }
        else
        {
        	mainScreen(g2D);
        	updateInventory();
        	displayInventory(g2D);
        	delay(100);
        }
        
        g.drawImage(virtualMem,0,0,this);
        repaint();
	}

	public void titleScreen(Graphics2D g2)
	{     
		g2.setColor(Color.white);      
		g2.fillRect(-20,-20,1240,1240);
		g2.setColor(Color.black);
		g2.setFont(new Font("Arial",Font.BOLD,72));
		g2.drawString("Welcome to TextGame",125,315);
		g2.drawString("Click to play",260,375);
   }
   
	public void mainScreen(Graphics2D g2)
	{
		g2.setFont(new Font("Arial",Font.BOLD,12));
		g2.setColor(Color.white);      
		g2.fillRect(-20,-20,1240,1240);
		g2.setColor(new Color(100,100,100));      
		g2.fillRect(101,26,1096,599);
      
		g2.setColor(Color.black);
		for(int k = 0; k < buildings.size(); k++)
		{
			tempBuilding = buildings.get(k);
			g2.fillRect(tempBuilding.button.x,tempBuilding.button.y,tempBuilding.button.width,tempBuilding.button.height);
			g2.fillRect(tempBuilding.button2.x,tempBuilding.button2.y,tempBuilding.button2.width,tempBuilding.button2.height);
		}
      
		g2.setColor(Color.white);
		for(int k = 0; k < buildings.size(); k++)
		{
			tempBuilding = buildings.get(k);
			g2.drawRect(tempBuilding.button.x,tempBuilding.button.y,tempBuilding.button.width,tempBuilding.button.height);
			g2.drawRect(tempBuilding.button2.x,tempBuilding.button2.y,tempBuilding.button2.width,tempBuilding.button2.height);
		}
		
		g2.setColor(Color.black);
		g2.fillRect(1,1,1196,24);
		g2.setColor(Color.white);
		g2.drawString("F11 to save |", 1004, 18);
		g2.drawString("F12 to save and exit", 1080, 18);
		
		//TODO MANUAL DATA ADDING
		g2.setColor(Color.white);
		g2.drawString("Woodshop",21,75);
		g2.drawString("Quarry",31,175);
		g2.drawString("Mine",36,275);
		g2.drawString("Smeltery",22,375);
		g2.drawString("Blacksmith",20,475);
		g2.drawString("TempB",34,575);
	}
	
	public void updateInventory()
	{
		//TODO MANUAL DATA ADDING
		if(counter % 5 == 0)
		{	
			if(wood.quantity <= wood.limit)//Wood logic
			{
				wood.quantity += (woodshop.productionSpeed * woodshop.owned * woodshop.productionMultiplier);
			}
			
			if(stone.quantity <= stone.limit)//Stone logic
			{
				stone.quantity += (quarry.productionSpeed * quarry.owned * quarry.productionMultiplier);
			}
			
			if(ore.quantity <= ore.limit)//Ore logic
			{
				ore.quantity += (mine.productionSpeed * mine.owned * mine.productionMultiplier);
			}
			
			if(ironIngot.quantity <= ironIngot.limit)//IronIngot logic
			{
				for(int k = 0; k < smeltery.owned; k++)
				{
					if(ore.quantity >= 1)
					{
						ore.quantity -= 1;
						ironIngot.quantity += (smeltery.productionSpeed * smeltery.productionMultiplier);
					}
				}
			}
			
			if(ironTools.quantity <= ironTools.limit)//IronTool logic
			{
				for(int k = 0; k < blacksmith.owned; k++)
				{
					if(ironIngot.quantity >= 1)
					{
						ironIngot.quantity -= 1;
						ironTools.quantity += (blacksmith.productionSpeed * blacksmith.productionMultiplier);
					}
				}
			}
			
			//Make sure no materials pass limit
			for(int k = 0; k < materials.size(); k++)
			{
				tempMaterial = materials.get(k);
				if(tempMaterial.quantity > tempMaterial.limit)
				{
					tempMaterial.quantity = tempMaterial.limit;
				}
			}	
		}
	}
	
	public void displayInventory(Graphics2D g2)
	{
		//TODO MANUAL DATA ADDING
		AffineTransform original = g2.getTransform();
		g2.setColor(new Color(245,245,245));
		g2.setFont(new Font("Arial",Font.BOLD,18));
		
		g2.drawString("BUILDINGS", 430, 75);
		g2.drawString("Woodshop: "  +Double.toString(woodshop.owned)  ,430,100);
		g2.drawString("Quarry: "    +Double.toString(quarry.owned)    ,430,125);
		g2.drawString("Mine: "      +Double.toString(mine.owned)      ,430,150);
		g2.drawString("Smeltery: "  +Double.toString(smeltery.owned)  ,430,175);
		g2.drawString("Blacksmith: "+Double.toString(blacksmith.owned),430,200);
		g2.drawString("TempB: "+Double.toString(tempB.owned),430,225);
		
		g2.drawString("Next Cost", 600, 75);
		g2.drawString(woodshop.getPrice()  ,600,100);
		g2.drawString(quarry.getPrice()    ,600,125);
		g2.drawString(mine.getPrice()      ,600,150);
		g2.drawString(smeltery.getPrice()  ,600,175);
		g2.drawString(blacksmith.getPrice(),600,200);
		//g2.drawString(tempB.getPrice(),600,225);
		
		g2.drawString("MATERIALS", 900, 75);
		g2.drawString("Wood: "       +((String)format.format(wood.quantity))     +"/"+(String)format.format(wood.limit)     ,900,100);
		g2.drawString("Stone: "      +((String)format.format(stone.quantity))    +"/"+(String)format.format(stone.limit)    ,900,125);
		g2.drawString("Iron Ore: "   +((String)format.format(ore.quantity))      +"/"+(String)format.format(ore.limit)      ,900,150);
		g2.drawString("Iron Ingots: "+((String)format.format(ironIngot.quantity))+"/"+(String)format.format(ironIngot.limit),900,175);
		g2.drawString("Iron Tools: " +((String)format.format(ironTools.quantity))+"/"+(String)format.format(ironTools.limit),900,200);
		
		g2.setFont(new Font("Arial",Font.BOLD,12));
		g2.rotate(-Math.PI/2);
		g2.drawString("Demolish",-105,115);
		g2.drawString("Demolish",-205,115);
		g2.drawString("Demolish",-305,115);
		g2.drawString("Demolish",-405,115);
		g2.drawString("Demolish",-505,115);
		g2.drawString("Demolish",-605,115);
		g2.setTransform(original);
	}
   
	public void mouseMoved(MouseEvent e)
	{  
		hoverx = e.getX()-1;
		hovery = e.getY()-23;
	}
   
	public void mouseDragged(MouseEvent e)
	{	
		started = true;
		clickx = e.getX()-1;
		clicky = e.getY()-23;
		
		g2.setFont(new Font("Arial",Font.BOLD,24));
		g2.setColor(Color.black);
      
		//TODO MANUAL DATA ADDING
		if(woodshop.button.contains(clickx,clicky))
		{  
			if(wood.quantity >= woodshop.woodPrice)
			{
				wood.quantity -= woodshop.woodPrice;
				woodshop.updateNextCost();
				woodshop.owned++;
			}
		}
		else if(woodshop.button2.contains(clickx,clicky))
		{
			if(woodshop.owned > 1)
			{
				woodshop.owned--;
			}
		}
		else if(quarry.button.contains(clickx,clicky))
		{
			if(wood.quantity >= quarry.woodPrice)
			{
				wood.quantity -= quarry.woodPrice;
				quarry.updateNextCost();
				quarry.owned++;
			}
		}
		else if(quarry.button2.contains(clickx,clicky))
		{
			if(quarry.owned > 0)
			{
				quarry.owned--;
			}
		}
		else if(mine.button.contains(clickx,clicky))
		{
			if(stone.quantity >= mine.stonePrice)
			{
				stone.quantity -= mine.stonePrice;
				mine.updateNextCost();
				mine.owned++;
			}
		}
		else if(mine.button2.contains(clickx,clicky))
		{
			if(mine.owned > 0)
			{
				mine.owned--;
			}
		}
		else if(smeltery.button.contains(clickx,clicky))
		{
			if(wood.quantity >= smeltery.woodPrice && stone.quantity >= smeltery.stonePrice)
			{
				wood.quantity -= smeltery.woodPrice;
				stone.quantity -= smeltery.stonePrice;
				smeltery.updateNextCost();
				smeltery.owned++;
			}
		}
		else if(smeltery.button2.contains(clickx,clicky))
		{
			if(smeltery.owned > 0)
			{
				smeltery.owned--;
			}
		}
		else if(blacksmith.button.contains(clickx,clicky))
		{
			if(stone.quantity >= blacksmith.stonePrice && ironIngot.quantity >= blacksmith.ironIngotPrice)
			{
				stone.quantity -= blacksmith.stonePrice;
				ironIngot.quantity -= blacksmith.ironIngotPrice;
				blacksmith.updateNextCost();
				blacksmith.owned++;
			}
		}
		else if(blacksmith.button2.contains(clickx,clicky))
		{
			if(blacksmith.owned > 0)
			{
				blacksmith.owned--;
			}
		}
	}
	
	public void parseCommand(String command)
	{
		String parameter = null;
		if(command.equalsIgnoreCase("ClearAll") || command.equalsIgnoreCase("Clear"))
		{
			parameter = JOptionPane.showInputDialog("Enter a new building to clear.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				for(int k = 0; k < materials.size(); k++)
				{
					tempMaterial = materials.get(k);
					tempMaterial.quantity = 0;
				}
			}
			for(int k = 0; k < materials.size(); k++)
			{
				tempMaterial = materials.get(k);
				if(parameter.equalsIgnoreCase(tempMaterial.name))
				{
					tempMaterial.quantity = 0;
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("SetMaterials") || command.equalsIgnoreCase("Materials"))
		{
			parameter = JOptionPane.showInputDialog("Enter a new type of materials to own.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new amount of materials to own.", "0");
				for(int k = 0; k < materials.size(); k++)
				{
					tempMaterial = materials.get(k);
					tempMaterial.quantity = Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < materials.size(); k++)
			{
				tempMaterial = materials.get(k);
				if(parameter.equalsIgnoreCase(tempBuilding.name))
				{
					tempMaterial.quantity = Double.parseDouble(JOptionPane.showInputDialog("Enter a new amount of materials to own.", "0"));
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("SetOwned") || command.equalsIgnoreCase("Owned"))
		{
			parameter = JOptionPane.showInputDialog("Enter a new type of building to own.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new amount of buildings to own.", "0");
				for(int k = 0; k < buildings.size(); k++)
				{
					tempBuilding = buildings.get(k);
					tempBuilding.owned = Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < buildings.size(); k++)
			{
				tempBuilding = buildings.get(k);
				if(parameter.equalsIgnoreCase(tempBuilding.name))
				{
					tempBuilding.owned = Double.parseDouble(JOptionPane.showInputDialog("Enter a new amount of buildings to own.", "0"));
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("AddOwned") || command.equalsIgnoreCase("Add"))
		{
			parameter = JOptionPane.showInputDialog("Enter a new type of buildings to add.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new amount of buildings to add.", "0");
				for(int k = 0; k < buildings.size(); k++)
				{
					tempBuilding = buildings.get(k);
					tempBuilding.owned += Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < buildings.size(); k++)
			{
				tempBuilding = buildings.get(k);
				if(parameter.equalsIgnoreCase(tempBuilding.name))
				{
					tempBuilding.owned += Double.parseDouble(JOptionPane.showInputDialog("Enter a new amount of buildings to add.", "0"));
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("SetLimit") || command.equalsIgnoreCase("Limit"))
		{
			parameter = JOptionPane.showInputDialog("Enter a building to modify.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new limit.", "1000");
				for(int k = 0; k < materials.size(); k++)
				{
					tempMaterial = materials.get(k);
					tempMaterial.limit = Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < materials.size(); k++)
			{
				tempMaterial = materials.get(k);
				if(parameter.equalsIgnoreCase(tempMaterial.name))
				{
					tempMaterial.limit = Double.parseDouble(JOptionPane.showInputDialog("Enter a new limit.", "1000"));
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("SetProdBonus") || command.equalsIgnoreCase("ProdBonus"))
		{
			parameter = JOptionPane.showInputDialog("Enter a building to modify.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new production multiplier.", "1.0");
				for(int k = 0; k < buildings.size(); k++)
				{
					tempBuilding = buildings.get(k);
					tempBuilding.productionMultiplier = Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < buildings.size(); k++)
			{
				tempBuilding = buildings.get(k);
				if(parameter.equalsIgnoreCase(tempBuilding.name))
				{
					tempBuilding.productionMultiplier = Double.parseDouble(JOptionPane.showInputDialog("Enter a new production multiplier.", "1.0"));
					break;
				}
			}
		}
		else if(command.equalsIgnoreCase("SetProdSpeed") || command.equalsIgnoreCase("ProdSpeed"))
		{
			parameter = JOptionPane.showInputDialog("Enter a building to modify.", "All");
			if(parameter.equalsIgnoreCase("All"))
			{
				parameter = JOptionPane.showInputDialog("Enter a new production speed.", "0.1");
				for(int k = 0; k < buildings.size(); k++)
				{
					tempBuilding = buildings.get(k);
					tempBuilding.productionSpeed = Double.parseDouble(parameter);
				}
			}
			for(int k = 0; k < buildings.size(); k++)
			{
				tempBuilding = buildings.get(k);
				if(parameter.equalsIgnoreCase(tempBuilding.name))
				{
					tempBuilding.productionSpeed = Double.parseDouble(JOptionPane.showInputDialog("Enter a new production speed.", "0.1"));
					break;
				}
			}
		}
		else
		{
			parseCommand(JOptionPane.showInputDialog("--LIST OF COMMANDS--\nClearAll or Clear\nSetMaterials or Materials\n"
					+ "SetOwned or Owned\nAddOwned or Add\nSetLimit or Limit\nSetProdBonus or ProdBonus\n"
					+ "SetProdSpeed or ProdSpeed"));
		}
	}
	
	public void firstLoad() throws URISyntaxException, IOException
	{
		String saveGame[];
		String gameData = "";
		String line = "";
		Boolean fileDidNotExist = false;
		
		File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
		File saveFile = new File(jarFile.getParentFile()+"/Save.dat");
		
		if(saveFile.exists())
		{
			BufferedReader br = new BufferedReader(new FileReader(saveFile));
			
			while((line = br.readLine()) != null)
			{
				if(!line.contains("#"))
				{
					gameData += line;
				}
			}
			br.close();
		}
		saveGame = gameData.split(",");
		
		//If the file is empty or otherwise incomplete then dont load in the save
		if(saveGame[0] != null && saveGame[saveGame.length-1] != null && !(fileDidNotExist = saveFile.createNewFile()))
		{	
			woodshop.productionSpeed      = Double.parseDouble(saveGame[0]);
			woodshop.productionMultiplier = Double.parseDouble(saveGame[1]);
			woodshop.priceRaiseMultiplier = Double.parseDouble(saveGame[2]);
			woodshop.woodPrice            = Double.parseDouble(saveGame[3]);
			woodshop.stonePrice           = Double.parseDouble(saveGame[4]);
			woodshop.ironOrePrice         = Double.parseDouble(saveGame[5]);
			woodshop.ironIngotPrice       = Double.parseDouble(saveGame[6]);
			woodshop.owned                = Double.parseDouble(saveGame[7]);
			wood.quantity                 = Double.parseDouble(saveGame[8]);
			wood.limit                    = Double.parseDouble(saveGame[9]);
				
			quarry.productionSpeed      = Double.parseDouble(saveGame[10]);
			quarry.productionMultiplier = Double.parseDouble(saveGame[11]);
			quarry.priceRaiseMultiplier = Double.parseDouble(saveGame[12]);
			quarry.woodPrice            = Double.parseDouble(saveGame[13]);
			quarry.stonePrice           = Double.parseDouble(saveGame[14]);
			quarry.ironOrePrice         = Double.parseDouble(saveGame[15]);
			quarry.ironIngotPrice       = Double.parseDouble(saveGame[16]);
			quarry.owned                = Double.parseDouble(saveGame[17]);
			stone.quantity              = Double.parseDouble(saveGame[18]);
			stone.limit                 = Double.parseDouble(saveGame[19]);
			
			mine.productionSpeed      = Double.parseDouble(saveGame[20]);
			mine.productionMultiplier = Double.parseDouble(saveGame[21]);
			mine.priceRaiseMultiplier = Double.parseDouble(saveGame[22]);
			mine.woodPrice            = Double.parseDouble(saveGame[23]);
			mine.stonePrice           = Double.parseDouble(saveGame[24]);
			mine.ironOrePrice         = Double.parseDouble(saveGame[25]);
			mine.ironIngotPrice       = Double.parseDouble(saveGame[26]);
			mine.owned                = Double.parseDouble(saveGame[27]);
			ore.quantity              = Double.parseDouble(saveGame[28]);
			ore.limit                 = Double.parseDouble(saveGame[29]);
			
			smeltery.productionSpeed      = Double.parseDouble(saveGame[30]);
			smeltery.productionMultiplier = Double.parseDouble(saveGame[31]);
			smeltery.priceRaiseMultiplier = Double.parseDouble(saveGame[32]);
			smeltery.woodPrice            = Double.parseDouble(saveGame[33]);
			smeltery.stonePrice           = Double.parseDouble(saveGame[34]);
			smeltery.ironOrePrice         = Double.parseDouble(saveGame[35]);
			smeltery.ironIngotPrice       = Double.parseDouble(saveGame[36]);
			smeltery.owned                = Double.parseDouble(saveGame[37]);
			ironIngot.quantity            = Double.parseDouble(saveGame[38]);
			ironIngot.limit               = Double.parseDouble(saveGame[39]);
			
			blacksmith.productionSpeed      = Double.parseDouble(saveGame[40]);
			blacksmith.productionMultiplier = Double.parseDouble(saveGame[41]);
			blacksmith.priceRaiseMultiplier = Double.parseDouble(saveGame[42]);
			blacksmith.woodPrice            = Double.parseDouble(saveGame[43]);
			blacksmith.stonePrice           = Double.parseDouble(saveGame[44]);
			blacksmith.ironOrePrice         = Double.parseDouble(saveGame[45]);
			blacksmith.ironIngotPrice       = Double.parseDouble(saveGame[46]);
			blacksmith.owned                = Double.parseDouble(saveGame[47]);
			ironTools.quantity              = Double.parseDouble(saveGame[48]);
			ironTools.limit                 = Double.parseDouble(saveGame[49]);
		}
		if(fileDidNotExist)
		{
			makeNewSave();
		}
	}
	
	public void saveGame()
	{
		String saveGame[] = new String[50];
		
		saveGame[0] = Double.toString(woodshop.productionSpeed);
		saveGame[1] = Double.toString(woodshop.productionMultiplier);
		saveGame[2] = Double.toString(woodshop.priceRaiseMultiplier);
		saveGame[3] = Double.toString(woodshop.woodPrice);
		saveGame[4] = Double.toString(woodshop.stonePrice);
		saveGame[5] = Double.toString(woodshop.ironOrePrice);
		saveGame[6] = Double.toString(woodshop.ironIngotPrice);
		saveGame[7] = Double.toString(woodshop.owned);
		saveGame[8] = Double.toString(wood.quantity);
		saveGame[9] = Double.toString(wood.limit);
		
		saveGame[10] = Double.toString(quarry.productionSpeed);
		saveGame[11] = Double.toString(quarry.productionMultiplier);
		saveGame[12] = Double.toString(quarry.priceRaiseMultiplier);
		saveGame[13] = Double.toString(quarry.woodPrice);
		saveGame[14] = Double.toString(quarry.stonePrice);
		saveGame[15] = Double.toString(quarry.ironOrePrice);
		saveGame[16] = Double.toString(quarry.ironIngotPrice);
		saveGame[17] = Double.toString(quarry.owned);
		saveGame[18] = Double.toString(stone.quantity);
		saveGame[19] = Double.toString(stone.limit);

		saveGame[20] = Double.toString(mine.productionSpeed);
		saveGame[21] = Double.toString(mine.productionMultiplier);
		saveGame[22] = Double.toString(mine.priceRaiseMultiplier);
		saveGame[23] = Double.toString(mine.woodPrice);
		saveGame[24] = Double.toString(mine.stonePrice);
		saveGame[25] = Double.toString(mine.ironOrePrice);
		saveGame[26] = Double.toString(mine.ironIngotPrice);
		saveGame[27] = Double.toString(mine.owned);
		saveGame[28] = Double.toString(ore.quantity);
		saveGame[29] = Double.toString(ore.limit);
		
		saveGame[30] = Double.toString(smeltery.productionSpeed);
		saveGame[31] = Double.toString(smeltery.productionMultiplier);
		saveGame[32] = Double.toString(smeltery.priceRaiseMultiplier);
		saveGame[33] = Double.toString(smeltery.woodPrice);
		saveGame[34] = Double.toString(smeltery.stonePrice);
		saveGame[35] = Double.toString(smeltery.ironOrePrice);
		saveGame[36] = Double.toString(smeltery.ironIngotPrice);
		saveGame[37] = Double.toString(smeltery.owned);
		saveGame[38] = Double.toString(ironIngot.quantity);
		saveGame[39] = Double.toString(ironIngot.limit);
		
		saveGame[40] = Double.toString(blacksmith.productionSpeed);
		saveGame[41] = Double.toString(blacksmith.productionMultiplier);
		saveGame[42] = Double.toString(blacksmith.priceRaiseMultiplier);
		saveGame[43] = Double.toString(blacksmith.woodPrice);
		saveGame[44] = Double.toString(blacksmith.stonePrice);
		saveGame[45] = Double.toString(blacksmith.ironOrePrice);
		saveGame[46] = Double.toString(blacksmith.ironIngotPrice);
		saveGame[47] = Double.toString(blacksmith.owned);
		saveGame[48] = Double.toString(ironTools.quantity);
		saveGame[49] = Double.toString(ironTools.limit);
		
		try {
			File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			File saveFile = new File(jarFile.getParentFile()+"/Save.dat");
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			
			for(int k = 0; k < saveGame.length; k++)
			{
				if(k == saveGame.length-1)
				{
					bw.write(saveGame[k]);
				}
				else
				{
					bw.write(saveGame[k]+",");
				}
				if(k % 10 == 9)
				{
					bw.write("\n");
				}
			}
			bw.write("\n#For Version "+GAME_VERSION+" of Game");
			bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
	}
	
	public void makeNewSave()
	{
		try {
			File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			File saveFile = new File(jarFile.getParentFile()+"/Save.dat");
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			
			bw.write("0.1,1.0,1.9,10.0,0.0,0.0,0.0,1.0,0,1000.0,\n");
			bw.write("0.1,1.0,1.9,50.0,0.0,0.0,0.0,0.0,0.0,1000.0,\n");
			bw.write("0.1,1.0,1.9,0.0,50.0,0.0,0.0,0.0,0.0,1000.0,\n");
			bw.write("0.1,1.0,1.9,50.0,150.0,0.0,0.0,0.0,0.0,1000.0,\n");
			bw.write("0.1,0.75,1.9,0.0,300.0,0.0,200.0,0.0,0.0,1000.0\n");
			bw.write("\n#For Version "+GAME_VERSION+" of Game");
			bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
	}
	
	public void delay(long delay)
	{
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
   
	public void update(Graphics g2)
	{
		paint(g2);	
	}
   
	public void mousePressed(MouseEvent e)
	{
		mouseDragged(e);  
	}
	
	public void keyPressed(KeyEvent e) 
	{	
		switch (e.getKeyCode()) 
		{ 
			case KeyEvent.VK_F1:
			{		
				parseCommand(JOptionPane.showInputDialog("Enter console command or type help."));
				break;
			}
			case KeyEvent.VK_F11:
			{		
				saveGame();
				break;
			}
			case KeyEvent.VK_F12:
			{		
				saveGame();
				System.exit(0);
				break;
			}
			
		}
		started = true;
	}
   
	public void mouseReleased(MouseEvent e){}
   
	public void mouseClicked(MouseEvent e){}
   
	public void mouseEntered(MouseEvent e){}
   
	public void mouseExited(MouseEvent e){}

	public void keyTyped(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

}   