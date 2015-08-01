import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ToArrayLists {
	public static ArrayList<Integer> items = new ArrayList<Integer>();
	public static ArrayList<String> itemNames = new ArrayList<String>();
	public static String[] champNames;
	public static ArrayList<String> championKeys = new ArrayList<String>();
	public static ArrayList<String> champIds = new ArrayList<String>();
	public static int championId = 62;
	public static void main(String[]args) throws IOException {
		parseItemListToArrayList();
		parseChampIdsToArrayList();
		parseChampNamesToArrayList();
	}

	public static void parseChampIdsToArrayList() throws IOException {
		File db = new File("championIds.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = "";
		while((line = rd.readLine()) != null) {
			champIds.add(line);
		}
		rd.close();
	}
	public static void parseChampNamesToArrayList() throws IOException {
		File db = new File("championNames.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = "";
		while((line = rd.readLine()) != null) {
			championKeys.add(line);
		}
		rd.close();
	}

	@SuppressWarnings("resource")
	public static void parseItemListToArrayList() throws NumberFormatException, IOException {
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
}
