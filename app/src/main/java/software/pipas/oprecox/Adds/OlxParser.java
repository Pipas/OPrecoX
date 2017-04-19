package software.pipas.oprecox.Adds;

import android.util.Log;

import software.pipas.oprecox.Categories;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pipas_ on 12/03/2017.
 */

public class OlxParser
{
    String[] forbiddenWords = {"€" ,"EUR", "Euro", "euro", "eur ", "eur.", "eur,"};

    Categories categories = new Categories();

    public OlxParser(ArrayList<String> c)
    {
        categories.setSelected(c);
    }

    public OlxParser() {}

    public String getRandomURL() throws IOException
    {
        String urlStart = "https://www.olx.pt/";
        String urlEnd = "/?search[description]=1&page=";
        String category = categories.generateURL();
        String categoryMax = urlStart + category + urlEnd + "500";

        Response response = Jsoup.connect(categoryMax).followRedirects(true).execute();
        String r = response.url().toString();

        int categoryPages = Integer.parseInt(r.substring(r.lastIndexOf("=") + 1));
        Random rand1 = new Random();
        Random rand3 = new Random();

        int pageNumber = rand1.nextInt(categoryPages) + 1;
        String pageURL = urlStart + category + urlEnd + pageNumber;

        Document document = Jsoup.connect(pageURL).get();
        Elements linkContainer = document.select("table[class^=fixed breakword  ad_]");
        Elements links = linkContainer.select("a[class=marginright5 link linkWithHash detailsLink]");

        int articleNumber = rand3.nextInt(links.size());

        return links.get(articleNumber).attr("href");
    }

    public String getDescription(String pageURL) throws IOException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements descriptionContainer = document.select("div[id=textContent]");
        Elements description = descriptionContainer.select("p");
        String rawDescription = description.html();
        rawDescription = rawDescription.replace("&gt;", ">");
        rawDescription = rawDescription.replace("<br> ", "\n");
        rawDescription = rawDescription.replace("<br>", "\n");
        rawDescription = rawDescription.replace("&amp;", "&");
        rawDescription = rawDescription.replace("&nbsp;", " ");
        for(int i = 0; i < forbiddenWords.length; i++)
        {
            if(rawDescription.contains(forbiddenWords[i]))
                throw new NumberFormatException();
        }
        return rawDescription;
    }

    public String getTitle(String pageURL) throws IOException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements titleE = document.select("h1");
        String title = titleE.html();
        title = title.replace("&amp;", "&");
        for(int i = 0; i < forbiddenWords.length; i++)
        {
            if(title.contains(forbiddenWords[i]))
                throw new NumberFormatException();
        }
        return title;

    }

    public ArrayList<String> getImage(String pageURL) throws IOException
    {
        ArrayList<String> imgs = new ArrayList<String>();
        Document document = Jsoup.connect(pageURL).get();
        Elements img = document.select("img[class^=vtop bigImage]");
        if(img.isEmpty())
            throw new NumberFormatException();
        for(int i = 0; i < img.size(); i++)
            imgs.add(img.get(i).attr("src"));
        return imgs;
    }

    public float getPrice(String pageURL) throws IOException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements priceContainer = document.select("strong[class^=xxxx-large ");
        if(priceContainer.isEmpty())
            throw new NumberFormatException();
        String priceStr = priceContainer.get(0).html();
        String priceNo = priceStr.replace(" €", "");
        priceNo = priceNo.replace(".", "");
        priceNo = priceNo.replace(",", ".");
        return Float.parseFloat(priceNo);
    }
}