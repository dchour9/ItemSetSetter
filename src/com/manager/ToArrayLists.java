package com.manager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ToArrayLists {
	public static ArrayList<String> items = new ArrayList<String>();
	public static ArrayList<String> championKeys = new ArrayList<String>();
	public static int championId = 62;
	public static void main(String[]args) throws IOException {
		parseItemListToArrayList();
		parseChampNamesToArrayList();
	}

	public static void parseChampNamesToArrayList() throws IOException {
		File db = new File("champions.json");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		JsonParser parser = new JsonParser();
		JsonObject champs = (JsonObject) parser.parse(rd.readLine());
		for(Entry<String, JsonElement> name : ((JsonObject) champs.get("data")).entrySet()) {
			championKeys.add(name.getKey());
		}
		rd.close();
	}

	public static void parseItemListToArrayList() throws NumberFormatException, IOException {
		File db = new File("items.json");
		BufferedReader rd = new BufferedReader(new FileReader(db));
		JsonParser parser = new JsonParser();
		JsonObject champs = (JsonObject) parser.parse(rd.readLine());
		for(Entry<String, JsonElement> name : ((JsonObject) champs.get("data")).entrySet()) {
			items.add(name.getKey());
		}
		rd.close();
	}

}

