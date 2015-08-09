package com.resources;
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
		parseItemListNameIdToArrayList();
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
			System.out.println(line);
			championKeys.add(line);
		}
		rd.close();
	}

	public static void parseItemListToArrayList() throws NumberFormatException, IOException {
		File db = new File("itemIds.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = rd.readLine();
		while((line = rd.readLine()) != null) {
			items.add(Integer.parseInt(line));
		}
		rd.close();
	}
	
	public static void parseItemListNameIdToArrayList() throws NumberFormatException, IOException {
		File db = new File("itemNameIds.txt");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		String line = rd.readLine();
		while((line = rd.readLine()) != null) {
			itemNames.add(line);
		}
		rd.close();
	}
	
}

