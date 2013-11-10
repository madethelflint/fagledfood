package com.darkly.fagledfood;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class Ingredient {

	public String name;
	public String definition; 
	public ArrayList<Ingredient> ingredients;
	
	private List<Ingredient> definedWords;
	
	public Ingredient(){
		ingredients = new ArrayList<Ingredient>();
		definedWords = new ArrayList<Ingredient>();
	}
	
	public List<Ingredient> processIngredients(){
		for(int i=0; i<ingredients.size(); i++){
			Ingredient ing = ingredients.get(i);
			ing.definition = fetchDefinitionForWord(ing.name);
			definedWords.add(ing);
		}
		return definedWords;
	}
	
	public String fetchDefinitionForWord(String theName){
		theName = theName.toLowerCase();
		theName.replace("ingredients", "");
		theName.replace(":", "");
		
		String encodedName = theName;
		try {
			encodedName = URLEncoder.encode(theName, "utf-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String url = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" + encodedName + "?key=98309b95-e3a2-4f15-b27a-21c8ba14944e";
		 DefaultHttpClient httpclient = new DefaultHttpClient();
         HttpGet httpget = new HttpGet(url);
         HttpResponse response = null;
         
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
         try {
             BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((line = in.readLine()) != null) {                    
			     sb.append(line + NL);
			 }
	         in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
         String result = sb.toString();
         Log.v("My Response :: ", result);
         
//         XmlPullParser p = Xml.newPullParser();
//         try {
//        	 InputStream in1 = new ByteArrayInputStream(result.getBytes("UTF-8"));
//			p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//			p.setInput(in1, null);
//			p.nextTag();
//			return readFeed(p);
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException exce){
//			
//		} catch (IOException ioexc){
//			
//		}
         String crap = "";
         try{
        	crap = result.substring(result.indexOf("<dt>"), result.indexOf("</dt>"));
             	 
         }catch(Exception norme){
        	 
         }
         
         return crap;
         //return "stuff";
	}
	
//	private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
//	    List entries = new ArrayList();
//
//	    parser.require(XmlPullParser.START_TAG, ns, "feed");
//	    while (parser.next() != XmlPullParser.END_TAG) {
//	        if (parser.getEventType() != XmlPullParser.START_TAG) {
//	            continue;
//	        }
//	        String name = parser.getName();
//	        // Starts by looking for the entry tag
//	        if (name.equals("entry")) {
//	            entries.add(readEntry(parser));
//	        } else {
//	            skip(parser);
//	        }
//	    }  
//	    return entries;
//	}
}
