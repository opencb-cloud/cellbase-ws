package org.bioinfo.infrared.ws.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class RestPathsTest {

	@Test
	public void testGetTest() {
		StringBuilder sb1 = new StringBuilder();
		for(int i=0; i< 1012; i++) {
			sb1.append("a");
		}
		System.out.println("punto -1 ");
		String urlStr = "http://localhost:8080/infrared-ws/rest/hsa/gettest/"+sb1.toString();
		URL url;
		try {
			url = new URL(urlStr);
			System.out.println("aqui si");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(20000);
			System.out.println(conn.getContentLength());
			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//		return sb.toString();
	}

	public void testGetTestPost() {
		StringBuilder sb1 = new StringBuilder();
		for(int i=0; i< 7960; i++) {
			sb1.append("a");
		}
		System.out.println("punto - 1 ");
		String urlStr = "http://localhost:8080/infrared-ws/rest/hsa/gettest2/"+sb1.toString();
		URL url;
		HttpURLConnection conn;
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
//			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");

			// Create the form content
//			OutputStream out = conn.getOutputStream();
//			Writer writer = new OutputStreamWriter(out, "UTF-8");
//			for (int i = 0; i < paramName.length; i++) {
//				writer.write(paramName[i]);
//				writer.write("=");
//				writer.write(URLEncoder.encode(paramVal[i], "UTF-8"));
//				writer.write("&");
//			}
//			writer.close();
//			out.close();

			if (conn.getResponseCode() != 200) {
				throw new IOException(conn.getResponseMessage());
			}

			// Buffer the result into a string
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


}
