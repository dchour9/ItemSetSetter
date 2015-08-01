import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import constant.Region;

import dto.Summoner.Summoner;

import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

import com.google.gson.*;



public class ItemSetGUI extends JPanel implements ActionListener  {
	
//	public static JButton boots, bfSword, done;
	public static final String API_KEY = "449b1ef2-d95b-4cb0-afe5-2b45b159f26d";
	public static ArrayList<String> items = new ArrayList<String>();
	public static PrintWriter writer;
	public static JPanel added = new JPanel();

	public ItemSetGUI() {
		
		setLayout(new GridLayout());
		
		JPanel container = new JPanel(new GridLayout(20,2));
		container.setSize(new Dimension(800,800));
//		container.setSize(800, getComponentCount());
		
		JButton b = new JButton ();
		

		try {
			ItemReader.parse();
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		}
		for(Integer item: ItemReader.items) {
			Image image = null;
			
			try {
				image = ImageIO.read(new File("resources\\" + item + ".jpg"));
			} catch (IOException e) {
				
			}
			ImageIcon img = new ImageIcon(image);
			
			b = new JButton(img);//"" + item.substring(5)
			b.setActionCommand("" + item);
			b.addActionListener(this);
			b.setContentAreaFilled(false);
			b.setBorder(null);
//			b.setFocusPainted(false);
			container.add(b);
			
//			System.out.println(b.getActionCommand());
//			System.out.println(item);
		}
		
		JButton done = new JButton("Finish");
		done.addActionListener(this);
		done.setActionCommand("finish");
		container.add(done);
		
		JScrollPane scroll = new JScrollPane(container);
		scroll.setSize(new Dimension(800,600));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setWheelScrollingEnabled(true);
		add(scroll, BorderLayout.NORTH);
		add(added, BorderLayout.SOUTH);
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Selector");
//		BufferedImage img = null;
//		try {
//			img = ImageIO.read(new File("icon.png"));
//		} catch (IOException e) {}
//		frame.setIconImage(img);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);

		//Add content to the window.
		frame.add(new ItemSetGUI());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[]args) throws RiotApiException, FileNotFoundException, UnsupportedEncodingException {
		RiotApi api = new RiotApi(API_KEY);

//        Map<String, Summoner> summoners = api.getSummonersByName(Region.NA, "belgabad");
//        Summoner summoner = summoners.get("belgabad");
//        long id = summoner.getId();
//        System.out.println(id);
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
		String str = 
		"{\n"
		+"\"title\": \""+name+"\",\n"
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
		               +"\"type\": \""+name+"\",\n"
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
		               +"\"type\": \""+name+"\",\n"
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
		// TODO Auto-generated method stub
//		if(e.getActionCommand().equals("BF")) {
//			items.add(newItem(1038));
//		}
//		if(e.getActionCommand().equals("boots1")) {
//			items.add(newItem(1001));
//		}
		if(e.getActionCommand().equals("finish")) {
			writer.println(newFilledBlock("Starter",items));
	        writer.println(footer());
			writer.close();
			File source = new File( "rec.json");
	        File dest = new File("C:\\Riot Games\\League of Legends\\Config\\Champions\\MonkeyKing\\Recommended\\recSet.json");
	        try {
				Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException eve) {
				// TODO Auto-generated catch block
			}
//			dispose();
		} else {
			items.add(newItem(Integer.parseInt(e.getActionCommand())));
			added.add(new JButton("" + e.getActionCommand()));
		}
	}
}
