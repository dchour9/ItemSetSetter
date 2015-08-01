import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import main.java.riotapi.RiotApiException;

@SuppressWarnings("serial")
public class TabbedView extends JPanel implements ActionListener  {

	//	public static JButton boots, bfSword, done;
	public static final String API_KEY = "key";
	public static String championId = "MonkeyKing";
	public static ArrayList<String> items = new ArrayList<String>();
	public static ArrayList<String> champions = new ArrayList<String>();
	public static PrintWriter writer;
	public static JFrame frame;
	public static JTabbedPane tab;
	public static JPanel selectedChampions, selectedItems;

	public TabbedView() {

		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		setLayout(new GridLayout(1,1));

		tab = new JTabbedPane();
		JPanel itemTab = new JPanel(new BorderLayout());
		JPanel items = new JPanel();
		JScrollPane itemScroll = new JScrollPane(items);
		JPanel championTab = new JPanel(new BorderLayout());
		JPanel champions = new JPanel();
		selectedChampions = new JPanel();
		selectedItems = new JPanel();
		selectedChampions.setPreferredSize(new Dimension(500,100));
		selectedItems.setPreferredSize(new Dimension(500,100));
		items.setPreferredSize(new Dimension(500,800));
		itemScroll.setPreferredSize(new Dimension(500,800));
		champions.setPreferredSize(new Dimension(500,400));
		//		container.setSize(800, getComponentCount());

		//Default JButton for adding mass buttons
		JButton b = new JButton ();

		try {
			ToArrayLists.main(new String[0]);
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
		/**
		 * Adds champion buttons to the champion JPanel
		 */
		for(String champ: ToArrayLists.championKeys) {
			Image image = null;

			try {
				image = ImageIO.read(new File("champIds\\" + champ + ".jpg"));
			} catch (IOException e) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

			b = new JButton(img);//"" + item.substring(5)
			b.setActionCommand("Champ" + champ);
			b.addActionListener(this);
			b.setContentAreaFilled(false);
			b.setBorder(null);
			//			b.setFocusPainted(false);
			champions.add(b);
		}
		/**
		 * Adds item buttons to the items JPanel
		 */
		for(Integer item: ToArrayLists.items) {
			Image image = null;

			try {
				image = ImageIO.read(new File("itemIds\\" + item + ".jpg"));
			} catch (IOException e) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

			b = new JButton(img);//"" + item.substring(5)
			b.setActionCommand("" + item);
			b.addActionListener(this);
			b.setContentAreaFilled(false);
			b.setBorder(null);
			//			b.setFocusPainted(false);
			items.add(b);

			//			System.out.println(b.getActionCommand());
			//			System.out.println(item);
		}
		JButton next = new JButton("Next");
		next.setActionCommand("Next");
		champions.add(next);
		JButton done = new JButton("Finish");
		done.addActionListener(this);
		done.setActionCommand("finish");
		items.add(done);
		selectedItems.add(done);
		JScrollPane scroll = new JScrollPane(items);
		scroll.setSize(new Dimension(800,800));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setWheelScrollingEnabled(true);

		championTab.add(champions,BorderLayout.NORTH);
		championTab.add(selectedChampions, BorderLayout.SOUTH);
		itemTab.add(itemScroll, BorderLayout.NORTH);
		itemTab.add(selectedItems, BorderLayout.SOUTH);
		tab.addTab("Champions", championTab);
		tab.addTab("Items", itemTab);
//		tab.addTab("Already Done", selectedItems);
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		add(tab);
		//The following line enables to use scrolling tabs.

	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		frame = new JFrame("Selector");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);

		//Add content to the window.
		frame.add(new TabbedView());
		BufferedImage img = null;
		Random rng = new Random();
		try {
			img = ImageIO.read(new File("champIds\\" + ToArrayLists.championKeys.get(rng.nextInt(ToArrayLists.championKeys.size()-1)) + ".jpg"));
			frame.setIconImage(img);
		} catch (IOException e) {
			System.out.println("No file found");
		}

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[]args) throws RiotApiException, FileNotFoundException, UnsupportedEncodingException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
		writer = new PrintWriter("rec.json", "UTF-8");
		writer.println(newHeader("Default"));
	}
	public static String newHeader(String name) {
		String str = "{\n"
				+"\"title\": \"" + name + "\",\n"
				+"\"type\": \"custom\"\n,"
				+"\"map\": \"any\",\n"
				+"\"mode\": \"any\",\n"
				+"\"priority\": false,\n"
				+"\"sortrank\": 0,";

		return str;
	}

	public static String newEmptyBlock(String name) {
		String str = "\"blocks\": [\n"
				+ "{\n"
				+"\"type\": \"" + name + "\",\n"
				+ "\"recMath\": false,\n"
				+"\"minSummonerLevel\": -1,\n"
				+"\"maxSummonerLevel\": -1,\n"
				+"\"showIfSummonerSpell\": \"\",\n"
				+"\"hideIfSummonerSpell\": \"\",\n"
				+"\"items\": [\n"
				+"\n]"
				+"},\n";
		return str;
	}
	public static String newFilledBlock(String name, ArrayList<String> items) {
		String str = "\"blocks\": [\n"
				+ "{\n"
				+"\"type\": \"" + name + "\",\n"
				+ "\"recMath\": false,\n"
				+"\"minSummonerLevel\": -1,\n"
				+"\"maxSummonerLevel\": -1,\n"
				+"\"showIfSummonerSpell\": \"\",\n"
				+"\"hideIfSummonerSpell\": \"\",\n"
				+"\"items\": [\n";
		for (String item: items) {
			str+=item + ","; 
		}
		str = str.substring(0,str.length()-1);

		str+="\n]"
				+"}\n";
		return str;
	}

	public static String newItem(int id) {
		String str = "";
		str+= "{\n"
				+"\"id\": \"" + id + "\",\n"
				+"\"count\": 1\n"
				+"}";
		return str;
	}

	public static String footer() {
		String str = "";
		str += "]\n}";
		return str;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("finish")) {
			writer.println(newFilledBlock("Starter",items));
			writer.println(footer());
			writer.close();
			File source = new File( "rec.json");
			for(String champ: champions) {
				File dest = new File("C:\\Riot Games\\League of Legends\\Config\\Champions\\" + champ + "\\Recommended\\ES_"+ champ +".json");
				try {
					dest.mkdirs();
					Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException eve) {
					// TODO Auto-generated catch block
				}
				frame.dispose();
			}
		}
		else if (e.getActionCommand().contains("Champ")) {
			Image image = null;

			try {
				image = ImageIO.read(new File("champIds\\" + e.getActionCommand().substring(5) + ".jpg"));
			} catch (IOException e1) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
			championId = e.getActionCommand().substring(5);
			JButton b = new JButton(img);
			selectedChampions.add(b);
			selectedChampions.revalidate();
			champions.add(e.getActionCommand().substring(5));
		}
		else if (e.getActionCommand().contains("Sel")) {
			items.remove(newItem(Integer.parseInt(e.getActionCommand())));
			
		}
		else if(e.getActionCommand().equals("Next")) {

		}
		else {
			Image image = null;

			try {
				image = ImageIO.read(new File("itemIds\\" + e.getActionCommand() + ".jpg"));
			} catch (IOException e1) {

			}
			ImageIcon img = new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
			items.add(newItem(Integer.parseInt(e.getActionCommand())));
			JButton b = new JButton(img);
			b.setActionCommand("Sel" + e.getActionCommand());
			b.setContentAreaFilled(false);
			b.setBorder(null);
			selectedItems.add(b);
			selectedItems.revalidate();
		}
	}
}
