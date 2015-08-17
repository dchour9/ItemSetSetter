package com.update;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.resources.ToArrayLists;

public class CreateImages {

	public static void main(String[]args) throws NumberFormatException, IOException {
		ToArrayLists.main(args);
		parseItemIdsToImage(args[0]);
		parseChampIdsToImage(args[0]);
	}
	//http://ddragon.leagueoflegends.com/cdn/5.14.1/img/champion/Aatrox.png
	public static void parseChampIdsToImage(String version) throws NumberFormatException, IOException {
		for(String name: ToArrayLists.championKeys) {
			URL url = null;
			try {
				url = new URL("http://ddragon.leagueoflegends.com/cdn/" + version + "/img/champion/" +  name + ".png");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
			FileOutputStream fos = new FileOutputStream(".\\champIds\\" + name + ".jpg");
			fos.write(response);
			fos.close();
		}
	}

	public static void parseItemIdsToImage(String version) throws NumberFormatException, IOException {

		for(String id: ToArrayLists.items) {

			URL url = null;
			try {
				url = new URL("http://ddragon.leagueoflegends.com/cdn/" + version + "/img/item/" +  id + ".png");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
			FileOutputStream fos = new FileOutputStream(".\\itemIds\\" + id + ".jpg");
			fos.write(response);
			fos.close();
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
