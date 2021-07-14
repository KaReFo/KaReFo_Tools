package com.karefo.popularity_pending;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.*;
import java.util.*;


@Service("GetPopularityScore")
@EnableAutoConfiguration
@ComponentScan(basePackages={"com"})
public class GetPopularityScore {

    public static final String GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1?key=AIzaSyAiLlNz2aVRE6xY84kgTfa6D0cmZYMaAGw&cx=7f6dff7edd4a8cafbcd&q=%E0%AE%95%E0%AF%81%E0%AE%9A%E0%AF%8D%E0%AE%9A%E0%AE%BF&alt=json&fields=queries(request(totalResults))";

    public void test1(String searchTerm, int num) throws IOException {

        //String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
        String searchURL = GOOGLE_SEARCH_URL;
        //without proper User-Agent, we will get 403 error
        Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        //below will print HTML data, save it to a file and open in browser to compare
        //System.out.println(doc.html());

        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
        Elements results = doc.select("h3.r > a");

        for (Element result : results) {
            String linkHref = result.attr("href");
            String linkText = result.text();
            //System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
        }
    }

    public void processAll() {
        // TODO code application logic here
        final String  Result;
        Result = temp("ஆலமரம்");
        //System.out.println("Results:"+ Result);

    }

    private String temp(String word){
        String url = "https://www.google.com/search?q="+word;
        Document document = null;
        try {
            document = Jsoup //
                    .connect(url) //
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)") //
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
//System.out.println(document.outerHtml());
        Element divResultStats = document.select("div#resultStats").first();
        if (divResultStats==null) {
            throw new RuntimeException("Unable to find results stats.");
        }

        return divResultStats.text();
    }

    private static String getResultsCount(final String query) throws IOException {
        //final URL url = new URL("https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8"));
        //final URL url = new URL("https://www.google.com/search?q="+URLEncoder.encode(query, "UTF-8")+"&aqs=chrome.1.69i57j0l9.10574j0j7&sourceid=chrome&ie=UTF-8");
        final URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAiLlNz2aVRE6xY84kgTfa6D0cmZYMaAGw&cx=7f6dff7edd4a8cafbcd&q=%E0%AE%95%E0%AF%81%E0%AE%9A%E0%AF%8D%E0%AE%9A%E0%AE%BF&alt=json&fields=queries(request(totalResults))");
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(60000);
        connection.setReadTimeout(60000);
        connection.addRequestProperty("User-Agent", "Mozilla/5.0");
        final Scanner reader = new Scanner(connection.getInputStream(), "UTF-8");
        while(reader.hasNextLine()){
            final String line = reader.nextLine();
            //System.out.println(line);
            if(!line.contains("<div id=\"result-stats\">About "))
                continue;
            try{
                return line.substring(line.indexOf("<div id=\"resultStats\">"), line.indexOf("results<nobr>")).replace(",","").replace(" ", "");
                //return Integer.parseInt(line.split("<div id=\"resultStats\">")[1].split("<")[0].replaceAll("[^\\d]", ""));
            }finally{
                reader.close();
            }
        }
        reader.close();
        return "0";
    }

    public String getOligoaFromURL(int i){
        StringBuilder result = new StringBuilder();
        URL oracle = null;
        try{
            oracle = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyDuRorFeCZ7OehAehyJUw9mnMMCcVuJZ5A&cx=bada34ad1482dd42a&q=%E0%AE%86%E0%AE%B2%E0%AE%AE%E0%AE%B0%E0%AE%AE%E0%AF%8D");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result.append(inputLine + " ");
            in.close();
            JSONObject olingoaJson = (JSONObject) JSONValue.parse(result.toString());
            //result = new StringBuilder();
            //result.append(olingoaJson.get("output").toString());
            //System.out.println(olingoaJson.toString());
            JSONObject x = (JSONObject) olingoaJson.get("queries");
            JSONArray array = (JSONArray) x.get("request");
            JSONObject y = (JSONObject) array.get(0);
            //System.out.println(i+":::"+y.get("totalResults").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }


    public static void main(String args[]){
        ConfigurableApplicationContext configurableApplicationContext= SpringApplication.run(GetPopularityScore.class, args);
        GetPopularityScore startCholIndex = configurableApplicationContext.getBean(GetPopularityScore.class);
        /*for(int z = 1; z<10000; z++) {
            startCholIndex.getOligoaFromURL(z);
        }*/

        Document doc;
        try{
            //System.out.println("hai");
            doc =        Jsoup.connect("https://www.google.com/search?q=apple&source=hp&ei=gTvkYPvtHtXD3LUP0qG36A0&iflsig=AINFCbYAAAAAYORJkRaezFYyA9w0qx2jI5w5zGd23kQO&oq=apple&gs_lcp=Cgdnd3Mtd2l6EAMyBwgAELEDEEMyCggAELEDEIMBEEMyBwgAELEDEEMyBwgAELEDEEMyCwguELEDEMcBEKMCMgcIABDJAxBDMgUIABCSAzIFCAAQkgMyBQgAELEDMgcIABCxAxBDUABYAGD2PWgAcAB4AIABXIgBXJIBATGYAQCqAQdnd3Mtd2l6wAEB&sclient=gws-wiz&ved=0ahUKEwi7svPnqM7xAhXVIbcAHdLQDd0Q4dUDCAc&uact=5").userAgent("Mozilla").ignoreHttpErrors(true).timeout(0).get();
            //System.out.println("hai"+doc.toString());
            Elements links = doc.select("li[class=g]");
            for (Element link : links) {
                Elements titles = link.select("h3[class=r5]");
                String title = titles.text();
                //System.out.println(title);

                Elements bodies = link.select("span[class=st]");
                String body = bodies.text();
                //System.out.println(body);

                //System.out.println("Title: "+title);
                //System.out.println("Body: "+body+"\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}

//https://www.googleapis.com/customsearch/v1?key=AIzaSyDuRorFeCZ7OehAehyJUw9mnMMCcVuJZ5A&cx=bada34ad1482dd42a&q=%E0%AE%86%E0%AE%B2%E0%AE%AE%E0%AE%B0%E0%AE%AE%E0%AF%8D