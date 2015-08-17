package com.manager;

import com.resources.ToArrayLists;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.resources.ModifiedFlowLayout;
import com.resources.WrapLayout;

import main.java.riotapi.RiotApiException;

@SuppressWarnings("serial")
public class ItemManager extends JPanel implements ActionListener  {

	public static final String API_KEY = com.update.Update.API_KEY;
	static PrintWriter writer;
	public static JFrame frame;
	static JPanel itemsPanel = new JPanel(), selectedItems = new JPanel(), selectedChampions = new JPanel();
	static JsonArray blocks = new JsonArray(), itemJson = new JsonArray();
	static JsonObject blockObj = new JsonObject(), file = new JsonObject();
	static JsonParser parser = new JsonParser();
	static JsonWriter write;
	JsonObject itemObj = new JsonObject();	
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	ArrayList<String> championsList = new ArrayList<String>(), items = new ArrayList<String>();
//	public static ArrayList<String> championKeys = new ArrayList<String>();
	JFileChooser fc;
	JComboBox<String> chBox, itBox, mapBox; 
	JButton openButton;
	String championId = "";
	JTextField searchBar = new JTextField(10), championSearch = new JTextField(10);
	static JPanel champions;


	public ItemManager() {
		
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		String[] chCategories = {"Categories...","Fighter","Mage", "Assassin", "Support","Tank","Marksman"};
		String[] itCategories = {"AAA","CooldownReduction","SpellDamage","Jungle","AttackSpeed","OnHit","Consumable","Lane","Mana","HealthRegen","Armor","Slow","NonbootsMovement","Active","Bilgewater","Damage","LifeSteal","ManaRegen","GoldPer","ArmorPenetration","Stealth","Vision","MagicPenetration","CriticalStrike",};
		String[] mapCategories = {"Any","Summoner's Rift","Twisted Tree Line","ARAM"};
		Arrays.sort(itCategories);
		itCategories[0] = "Any";
		//
		/*
		 * JPanels
		 */
		JPanel container = new JPanel(new BorderLayout());
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		JPanel selectors = new JPanel(new FlowLayout());
		JPanel search = new JPanel(new FlowLayout());
		JPanel rightSouth = new JPanel(new FlowLayout());
		champions = new JPanel(new FlowLayout());
		itemsPanel.setLayout(new ModifiedFlowLayout());
		selectedItems.setLayout(new WrapLayout());

		JScrollPane itemScroll = new JScrollPane(itemsPanel);
		itemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		itemScroll.setWheelScrollingEnabled(true);
		itemScroll.getVerticalScrollBar().setUnitIncrement(16);

		chBox = new JComboBox<String>(chCategories);
		itBox = new JComboBox<String>(itCategories);
		mapBox = new JComboBox<String>(mapCategories);
		itBox.addActionListener(this);
		mapBox.addActionListener(this);
		chBox.addActionListener(this);

		JLabel lChChategories = new JLabel("Champions ");
		JLabel lItChategories = new JLabel("Items ");
		JLabel lMap = new JLabel("Map");

		//		itemsPanel.setPreferredSize(new Dimension(480,330));
		itemScroll.setPreferredSize(new Dimension(500,400));
		champions.setPreferredSize(new Dimension(500,400));
		selectedItems.setPreferredSize(new Dimension(500,100));
		selectedChampions.setPreferredSize(new Dimension(500, 100));
		search.setPreferredSize(new Dimension(500,25));

		/*
		 * Adding many buttons to the GUI
		 */
		JButton b = new JButton();

		try {
			com.resources.ToArrayLists.main(new String[0]);
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
		/*
		 * Adds champion buttons to the champion JPanel
		 */
		Image image = null;
		for(String champ: ToArrayLists.championKeys) {


			try {
				image = ImageIO.read(new File("champIds\\" + champ + ".jpg"));
			} catch (IOException e) {
				System.out.println(champ);
			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

			b = new JButton(img);//"" + item.substring(5)
			b.setActionCommand("Champ" + champ);
			b.addActionListener(this);
			b.setContentAreaFilled(false);
			b.setBorder(null);
			b.setToolTipText("" + champ);
			champions.add(b);
		}
		try {
			createPanelItem("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		/*
		 * Save JButton
		 */
		JButton done = new JButton("Save");
		done.addActionListener(this);
		done.setActionCommand("Finish");


		/*
		 * Borders
		 */
		itemScroll.setBorder(new TitledBorder("Select Items"));
		TitledBorder b0 = new TitledBorder("Selected Item(s)");
		b0.setTitlePosition(TitledBorder.BOTTOM);
		TitledBorder b1 = new TitledBorder("Selected Champion(s)");
		b1.setTitlePosition(TitledBorder.BOTTOM);
		champions.setBorder(new TitledBorder("Select Champion(s)"));
		//		selectedChampions.setBorder(new TitledBorder(""));
		//		selectedItems.setBorder(new TitledBorder(""));
		/*
		 * Adding things to panels
		 */
		selectors.add(lChChategories);
		selectors.add(chBox);
		selectors.add(lItChategories);
		selectors.add(itBox);
		selectors.add(lMap);
		selectors.add(mapBox);

		search.add(new JLabel("Champion Search"));
		search.add(championSearch);
		championSearch.addActionListener(this);

		search.add(new JLabel("Item Search"));
		search.add(searchBar);
		searchBar.addActionListener(this);

		rightSouth.add(done);

		left.add(selectors);
		left.add(champions);
		left.add(selectedChampions);

		right.add(search);
		right.add(itemScroll);
		right.add(selectedItems);
		right.add(rightSouth);
		container.add(left, BorderLayout.WEST);
		container.add(right, BorderLayout.EAST);
		add(container);
		try {
			createPanel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new JsonObject <code>file</code>.
	 * @param name The title of the Item Set to appear in game
	 */
	public static void newHeader(String name) {
		file.add("title",  parser.parse(name));
		file.add("type", parser.parse("custom"));
		file.add("map", parser.parse("any"));
		file.add("mode", parser.parse("any"));
		file.add("priority", parser.parse("false"));
		file.add("sortrank", parser.parse("0"));
		file.add("blocks", blocks);
		blockObj.add("type", parser.parse("basic"));
		blockObj.add("recMath", parser.parse("false"));
		blockObj.add("minSummonerLevel", parser.parse("-1"));
		blockObj.add("maxSummonerLevel", parser.parse("-1"));
		blockObj.add("showIfSummonerSpell", parser.parse(""));
		blockObj.add("hideIfSummonerSpell", parser.parse(""));
		blockObj.add("items", itemJson);
		blocks.add(blockObj);
	}

	/**
	 * Creates a new item <code>JsonObject</code> from an id.
	 * @param id The id to make a new item from
	 * @return The completed JsonObject.
	 */
	public static JsonObject newItem(String id) {
		JsonObject jsObject = new JsonObject();
		jsObject.add("id", parser.parse("\"" + id + "\""));
		jsObject.add("count", parser.parse("1"));
		return jsObject;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getSource()); //debugging
		if(e.getSource() == championSearch) {
			for(Component button: champions.getComponents()) {
				if(!((JButton) button).getActionCommand().contains(championSearch.getText())) {
					champions.remove(button);
					champions.repaint();
					champions.revalidate();
				}
			}
		}
		else if(e.getSource() == searchBar) {
			for(Component button: itemsPanel.getComponents()) {
				if(!((JButton) button).getActionCommand().contains(searchBar.getText())) {
					itemsPanel.remove(button);
					itemsPanel.repaint();
					itemsPanel.revalidate();
				}
			}
		}
		/*
		 * Write current set to file
		 */
		else if(e.getActionCommand().equals("Finish")) {
			for(String id: items) {
				itemJson.add(newItem(id));
			}
			gson.toJson(file, write);
			writer.close();
			File source = new File( "rec.json");
			if(championsList.size() > 0) {
				for(String champ: championsList) {
					File dest = null;
					if(System.getProperty("os.name").contains("Windows")) {
						dest = new File("C:\\Riot Games\\League of Legends\\Config\\Champions\\" + champ + "\\Recommended\\ES_"+ champ +".json");
					}
					try {
						dest.mkdirs();
						Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException eve) {
					}
				}
				frame.dispose();
			}
		}

		else if(e.getSource() == chBox){
			try {
				createPanelChampions(chBox.getSelectedItem().toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			champions.revalidate();
		}

		else if(e.getSource()==itBox || e.getSource()==mapBox) {
			if(itBox.getSelectedItem().equals("Any")) {
				if(mapBox.getSelectedItem().equals("Summoner's Rift")) {
					try {
						createPanelMap(11, "");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();
				}
				else if(mapBox.getSelectedItem().equals("Twisted Tree Line")) {
					try {
						createPanelMap(10, "");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();
				}
				else if(mapBox.getSelectedItem().equals("ARAM")) {
					try {
						createPanelMap(12,"");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();
				}
			}//End ITEM == ANY

			if(mapBox.getSelectedItem().equals("Any") && itBox.getSelectedItem().equals("Any")) {
				try {
					createPanelItem("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				itemsPanel.revalidate();
			}

			if(mapBox.getSelectedItem().equals("Any") && !itBox.getSelectedItem().equals("Any")) {
				try {
					createPanelItem(itBox.getSelectedItem().toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				itemsPanel.revalidate();
			}
			else {
				if(mapBox.getSelectedItem().toString().equals("Summoner's Rift")) {
					try {
						createPanelMap(11,itBox.getSelectedItem().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();

				}
				if(mapBox.getSelectedItem().toString().equals("Twisted Tree Line")) {
					try {
						createPanelMap(10,itBox.getSelectedItem().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();

				}
				if(mapBox.getSelectedItem().toString().equals("ARAM")) {
					try {
						createPanelMap(12,itBox.getSelectedItem().toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					itemsPanel.revalidate();

				}
			}

		}
		/*
		 * Chose a champion. Add it to the champion array
		 */
		else if (e.getActionCommand().contains("Champ")) {
			Image image = null;

			try {
				image = ImageIO.read(new File("champIds\\" + e.getActionCommand().substring(5) + ".jpg"));
			} catch (IOException e1) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
			championId = e.getActionCommand().substring(5);
			championsList.add(e.getActionCommand().substring(5));
			JButton b = new JButton(img);
			b.setToolTipText(e.getActionCommand().substring(5));
			b.setActionCommand("Sel" + e.getActionCommand());
			b.setContentAreaFilled(false);
			b.setBorder(null);
			selectedChampions.add(b);
			selectedChampions.revalidate();
			champions.removeAll();
			champions.revalidate();
			champions.repaint();
			for(String champ: ToArrayLists.championKeys) {

				try {
					image = ImageIO.read(new File("champIds\\" + champ + ".jpg"));
				} catch (IOException e1) {

				}
				ImageIcon img1 = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

				b = new JButton(img1);//"" + item.substring(5)
				b.setActionCommand("Champ" + champ);
				b.addActionListener(this);
				b.setContentAreaFilled(false);
				b.setBorder(null);
				b.setToolTipText("" + champ);
				champions.add(b);
			}

		}
		else if (e.getActionCommand().contains("Sel")) {
			//			items.remove(newItem(Integer.parseInt(e.getActionCommand())));
			items.remove(e.getActionCommand().substring(0, 4));
			//			item
		}
		/*
		 * Chose an item. Add to be written
		 */
		else if(e.getActionCommand().equals("Exit")) {
			frame.dispose();
		}
		else {
			Image image = null;

			try {
				image = ImageIO.read(new File("itemIds\\" + e.getActionCommand().substring(0, 4) + ".jpg"));
			} catch (IOException e1) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
			items.add("" + e.getActionCommand().substring(0, 4));
			JButton b = new JButton(img);
			//			b.setToolTipText(e.getActionCommand().substring(6,e.getActionCommand().length()-1));
			b.setActionCommand("Sel" + e.getActionCommand());
			b.setContentAreaFilled(false);
			b.setBorder(null);
			selectedItems.add(b);
			selectedItems.revalidate();
			itemsPanel.removeAll();

			itemsPanel.repaint();
			try {
				createPanelItem("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			itemsPanel.revalidate();
		}
	}

	private void createPanelItem(String tag) throws IOException {
		itemsPanel.removeAll();
		itemsPanel.repaint();
		File f = new File(".\\items.json");
		BufferedReader rd = new BufferedReader(new FileReader(f));
		JsonParser parser = new JsonParser();
		String line = rd.readLine();
		JsonObject itemObj = (JsonObject) parser.parse(line);
		int count = 0;
		rd.close();
		for(Entry<String, JsonElement> ele: itemObj.entrySet()) {
			count++;
			if(count == 4) {
				for(Entry<String, JsonElement> item: ((JsonObject)ele.getValue()).entrySet()) {
					if((item.getValue().getAsJsonObject().has("tags") && item.getValue().getAsJsonObject().get("tags").getAsJsonArray().contains(parser.parse(tag))) || tag.equals("")) {
						String id = item.getValue().getAsJsonObject().get("id").toString();
						Image image = null;
						try {
							image = ImageIO.read(new File("itemIds\\" + id + ".jpg"));
						} catch (IOException e) {
							System.out.println("Fail: " + id);
						}
						ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
						JButton b = new JButton(img);
						b.setBorder(null);
						b.setActionCommand(id);
						b.setContentAreaFilled(false);
						b.addActionListener(this);
						ItemManager.itemsPanel.add(b);
					}
				} 
				break;
			}
		}
	}

	private void createPanelChampions(String tag) throws IOException {
		
		champions.removeAll();
		champions.repaint();
		if(tag.equals("Categories...")) {
			Image image = null;
			for(String champ: ToArrayLists.championKeys) {
				try {
					image = ImageIO.read(new File("champIds\\" + champ + ".jpg"));
				} catch (IOException e) {

				}
				ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
				JButton b;
				b = new JButton(img);//"" + item.substring(5)
				b.setActionCommand("Champ" + champ);
				b.addActionListener(this);
				b.setContentAreaFilled(false);
				b.setBorder(null);
				b.setToolTipText("" + champ);
				champions.add(b);
			}
		} else {
			
			File f = new File(".\\champions.json");
			BufferedReader rd = new BufferedReader(new FileReader(f));
			JsonParser parser = new JsonParser();
			String line = rd.readLine();
			JsonObject itemObj = (JsonObject) parser.parse(line);
			JsonObject data = itemObj.get("data").getAsJsonObject();
			rd.close();
			for(Entry<String, JsonElement> ele: data.entrySet()) {
				if(ele.getValue().getAsJsonObject().get("tags").getAsJsonArray().contains(parser.parse(tag))) {
					Image image = null;
					try {
						image = ImageIO.read(new File("champIds\\" + ele.getKey() + ".jpg"));
					} catch (IOException e) {
						System.out.println("Failed " + ele.getKey());
					}
					ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
					JButton b = new JButton(img);
					b.setBorder(null);
					b.setActionCommand("Champ" + ele.getKey());
					b.setContentAreaFilled(false);
					b.addActionListener(this);
					champions.add(b);			
				}
			}
		}
	}


	private void createPanel() throws IOException {
		itemsPanel.removeAll();
		itemsPanel.repaint();

		File f = new File(".\\items.json");
		BufferedReader rd = new BufferedReader(new FileReader(f));
		JsonParser parser = new JsonParser();
		String line = rd.readLine();
		JsonObject itemObj = (JsonObject) parser.parse(line);
		int count = 0;
		for(Entry<String, JsonElement> ele: itemObj.entrySet()) {
			count++;
			if(count == 4) {
				for(Entry<String, JsonElement> item: ((JsonObject)ele.getValue()).entrySet()) {
					String id = item.getValue().getAsJsonObject().get("id").toString();
					Image image = null;
					try {
						image = ImageIO.read(new File("itemIds\\" + id + ".jpg"));
					} catch (IOException e) {
						System.out.println("Failed " + id);
					}
					ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
					JButton b = new JButton(img);
					b.setBorder(null);
					b.setActionCommand(id);
					b.setContentAreaFilled(false);
					b.addActionListener(this);
					ItemManager.itemsPanel.add(b);
					ItemManager.itemsPanel.revalidate();
				}
				break;
			}
		}
		rd.close();
	}
	/**
	 * Add items to the <code>itemsPanel</code> JPanel.
	 * @param tag The tag of items to add to the JPanel.
	 * @param map The map to check.
	 * @throws IOException
	 */
	private void createPanelMap(int map, String tag) throws IOException {
		itemsPanel.removeAll();
		itemsPanel.repaint();
		File f = new File(".\\items.json");
		BufferedReader rd = new BufferedReader(new FileReader(f));
		JsonParser parser = new JsonParser();
		String line = rd.readLine();
		JsonObject itemObj = (JsonObject) parser.parse(line);
		int count = 0;
		File f1 = new File(".\\maps.json");
		BufferedReader rd1 = new BufferedReader(new FileReader(f1));
		line = rd1.readLine();
		JsonObject mapObj = (JsonObject) parser.parse(line);
		rd1.close();
		rd.close();
		JsonArray mapSpecific = mapObj.get("data").getAsJsonObject().get("" + map).getAsJsonObject().get("unpurchasableItemList").getAsJsonArray();
		for(Entry<String, JsonElement> ele: itemObj.entrySet()) {
			count++;
			if(count == 4) {
				for(Entry<String, JsonElement> item: ((JsonObject)ele.getValue()).entrySet()) {
					if((item.getValue().getAsJsonObject().has("tags") && item.getValue().getAsJsonObject().get("tags").getAsJsonArray().contains(parser.parse(tag))
							&& !mapSpecific.contains(item.getValue().getAsJsonObject().get("id"))) || (tag.equals("Any"))&& !mapSpecific.contains(item.getValue().getAsJsonObject().get("id"))) {
						String id = item.getValue().getAsJsonObject().get("id").toString();
						Image image = null;
						try {
							image = ImageIO.read(new File("itemIds\\" + id + ".jpg"));
						} catch (IOException e) {
							System.out.println("Failed " + id);
						}
						ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
						JButton b = new JButton(img);
						b.setBorder(null);
						b.setActionCommand(id);
						b.setContentAreaFilled(false);
						b.addActionListener(this);
						ItemManager.itemsPanel.add(b);
					}
				}
				break;
			}
		}
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("Open Item Set");
		menu.add(menuItem);
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("Exit");
		menu.add(menuItem);
		return menuBar;
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		frame = new JFrame("Selector");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);

		//Add content to the window.
		ItemManager menu = new ItemManager();
		frame.add(menu);
		BufferedImage img = null;
		Random rng = new Random();
		try {
			int idx = ToArrayLists.championKeys.size() - 1;
			img = ImageIO.read(new File("champIds\\" + ToArrayLists.championKeys.get(rng.nextInt(idx)) + ".jpg"));
			frame.setIconImage(img);
		} catch (IOException e) {
			System.out.println("No file found");
		}
		frame.setJMenuBar(menu.createMenuBar());
		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
	}

	public static void main(String[]args) throws RiotApiException, IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
		writer = new PrintWriter("rec.json", "UTF-8");
		write = new JsonWriter(writer);
		newHeader("K");
	}
}
