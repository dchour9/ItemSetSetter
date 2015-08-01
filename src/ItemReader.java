import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ItemReader {
	public static ArrayList<Integer> items = new ArrayList<Integer>();
	public static ArrayList<String> itemNames = new ArrayList<String>();
	public static String[] champNames;
	public static ArrayList<String> championKeys = new ArrayList<String>();
	public static ArrayList<String> champIds = new ArrayList<String>();
	public static int championId = 62;
	public static void main(String[]args) throws IOException {
		parse();
		parseChampIds();
		parseChampNames();
		//		parseImageToFile();
	}

	public static void createFileFromJsonURL(String url) throws MalformedURLException, IOException {

		String champObj = "";
		try {
			champObj = readUrl(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String out = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
		champNames = out.split("},");
		File champIds = null;

		champIds = new File(".\\championIds.txt");
		System.out.println(out);
		PrintWriter writer = new PrintWriter(champIds);
		JsonParser parser = new JsonParser();
		for (int i = 1; i < champNames.length-1; i++) {
			Object obj = parser.parse(champNames[i] + "}");
			JsonObject champ = (JsonObject) obj;
			writer.println(champ.get("id"));
		}
		writer.close();
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read); 

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}

	}

	public static void parseChampIds() throws IOException {
		File db = new File("championIds.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = "";
		while((line = rd.readLine()) != null) {
			champIds.add(line);
		}
	}
	public static void parseChampNames() throws IOException {
		File db = new File("championNames.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = "";
		while((line = rd.readLine()) != null) {
			championKeys.add(line);
		}
	}

	@SuppressWarnings("resource")
	public static void parse() throws NumberFormatException, IOException {
		File db = new File("itemsComplete.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = rd.readLine();
		System.out.println();
		while((line = rd.readLine()) != null) {
			if(!line.equals("") && Character.isDigit(line.charAt(0))) {
				System.out.println(line);
				items.add(Integer.parseInt(line));
			}
		}

	}



	@SuppressWarnings("resource")
	public static void parseItems(String item) throws NumberFormatException, IOException {
		File db = new File("itemDb.json");
		PrintWriter fileToWrite = new PrintWriter("movement.txt", "UTF-8");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line= "";

		while((line = rd.readLine()) != null) {
			System.out.println(line);
			if(line.contains(item)) {
				fileToWrite.println(line);

			}
		}
		fileToWrite.close();
	}

	@SuppressWarnings("resource")
	public static void parseImageToFile() throws NumberFormatException, IOException {
		File db = new File("itemsComplete.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line= rd.readLine();
		int lineCount = 1;
		while((line = rd.readLine()) != null) {
			if(!line.equals("") && !Character.isLetter(line.charAt(0)) && Character.isDigit(line.charAt(0))) {
				System.out.println(line);
				//				line = "";
				//				char tmp = (char) rd.read();
				//				line += tmp;
				//				tmp = (char) rd.read();
				//				line += tmp;
				//				tmp = (char) rd.read();
				//				line += tmp;
				//				tmp = (char) rd.read();
				//				line += tmp;
				//				System.out.println(line);
				Image image = null;
				URL url = null;
				try {
					url = new URL("http://ddragon.leagueoflegends.com/cdn/5.14.1/img/item/" +  line + ".png");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				Image image = null;
				//				URL url = null;
				//				try {
				//					url = new URL("http://ddragon.leagueoflegends.com/cdn/5.2.1/img/item/" + line + ".png ");
				//					image = ImageIO.read(url);
				//				} catch (IOException e) {
				//				}

				InputStream in = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1!=(n=in.read(buf)))
				{
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
				FileOutputStream fos = new FileOutputStream(".\\itemIds\\" + line + ".jpg");
				fos.write(response);
				fos.close();
			}
			lineCount++;
		}

	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = null;
		try{
			is = url.openStream();
		} catch (Exception e) {
			System.out.println(imageUrl);
			return;
		}
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
