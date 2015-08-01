import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreateLists {
	public static String[] champNames;
	public static void createChampionListFromJsonURL() throws MalformedURLException, IOException {
		Scanner fromUrl = new Scanner(new URL("https://na.api.pvp.net/api/lol/na/v1.2/champion?api_key=" + TabbedView.API_KEY).openStream(), "UTF-8");
		String out = fromUrl.useDelimiter("\\A").next();
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
		fromUrl.close();
	}
}
