package com.update;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class CreateLists {
	public static String[] champNames;
	public static void createChampionListFromJsonURL() throws MalformedURLException, IOException {
		Scanner fromUrl = new Scanner(new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/champion?champData=tags&api_key=449b1ef2-d95b-4cb0-afe5-2b45b159f26d").openStream(), "UTF-8");
		String out = fromUrl.useDelimiter("\\A").next();
		champNames = out.split("},");
		File champIds = null;

		champIds = new File(".\\champions.json");
		PrintWriter writer = new PrintWriter(champIds);
		writer.println(out);
		writer.close();
		fromUrl.close();
	}

	public static void createItemListFromJsonURL() throws MalformedURLException, IOException {
		Scanner fromUrl = new Scanner(new URL("https://global.api.pvp.net/api/lol/static-data/na/v1.2/item?itemListData=all&api_key=" + Update.API_KEY).openStream(), "UTF-8");
		String out = fromUrl.useDelimiter("\\A").next();
		File itemIds = null;
		itemIds = new File(".\\items.json");
		PrintWriter writer = new PrintWriter(itemIds);
		writer.println(out);
		writer.close();
		fromUrl.close();
	}
	
	public static void main(String[]args) throws MalformedURLException, IOException {
		createItemListFromJsonURL();
		createChampionListFromJsonURL();
	}
}
