package software.pipas.oprecox.modules.parsing;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import software.pipas.oprecox.modules.exceptions.OLXSyntaxChangeException;

public abstract class OlxParser
{
    private static String[] forbiddenWords = {"€" ,"EUR", "Euro", "euro", "eur ", "eur.", "eur,", "centimo"};

    public static String getRandomURL(String urlMid) throws IOException, OLXSyntaxChangeException
    {
        String urlStart = "https://www.olx.pt/";
        String urlEnd = "/?search[description]=1&page=";
        String categoryMax = urlStart + urlMid + urlEnd + "500";

        Response response = Jsoup.connect(categoryMax).followRedirects(true).execute();
        String r = response.url().toString();

        int categoryPages = Integer.parseInt(r.substring(r.lastIndexOf("=") + 1));
        Random rand = new Random();

        int pageNumber = rand.nextInt(categoryPages) + 1;
        String pageURL = urlStart + urlMid + urlEnd + pageNumber;

        Document document = Jsoup.connect(pageURL).get();
        Elements linkContainer = document.select("table[class=fixed offers breakword ]");
        if(linkContainer.isEmpty())
        {
            linkContainer = document.select("ul[class=gallerywide clr normal ]");
            if(linkContainer.isEmpty())
                throw new OLXSyntaxChangeException();
        }

        Elements links = linkContainer.select("a[class*=link linkWithHash detailsLink]");
        if(links.isEmpty())
            throw new OLXSyntaxChangeException();

        int articleNumber = rand.nextInt(links.size());

        return links.get(articleNumber).attr("href");
    }

    public static String getDescription(String pageURL) throws IOException, OLXSyntaxChangeException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements descriptionContainer = document.select("div[id=textContent]");
        if(descriptionContainer.isEmpty())
            throw new OLXSyntaxChangeException();

        Elements description = descriptionContainer.select("p");
        if(description.isEmpty())
            throw new OLXSyntaxChangeException();

        String rawDescription = description.html();
        rawDescription = rawDescription.replace("&gt;", ">");
        rawDescription = rawDescription.replace("<br> ", "\n");
        rawDescription = rawDescription.replace("<br>", "\n");
        rawDescription = rawDescription.replace("&amp;", "&");
        rawDescription = rawDescription.replace("&nbsp;", " ");
        for(int i = 0; i < forbiddenWords.length; i++)
        {
            if(rawDescription.contains(forbiddenWords[i]))
                throw new IOException();
        }
        return rawDescription;
    }

    public static String getTitle(String pageURL) throws IOException, OLXSyntaxChangeException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements titleE = document.select("h1");
        if(titleE.isEmpty())
            throw new OLXSyntaxChangeException();
        String title = titleE.html();
        title = title.replace("&amp;", "&");
        for(int i = 0; i < forbiddenWords.length; i++)
        {
            if(title.contains(forbiddenWords[i]))
                throw new IOException();
        }
        return title;

    }

    public static ArrayList<String> getImageUrls(String pageURL) throws IOException
    {
        ArrayList<String> imgs = new ArrayList<String>();
        Document document = Jsoup.connect(pageURL).get();
        Elements img = document.select("img[class^=vtop bigImage]");
        if(img.isEmpty())
            throw new IOException();
        for(int i = 0; i < img.size(); i++)
            imgs.add(img.get(i).attr("src"));
        return imgs;
    }

    public static float getPrice(String pageURL) throws IOException, OLXSyntaxChangeException
    {
        Document document = Jsoup.connect(pageURL).get();
        Elements priceContainer = document.select("strong[class^=xxxx-large ");
        if(priceContainer.isEmpty())
            throw new IOException();
        String priceStr = priceContainer.get(0).html();
        String priceNo = priceStr.replace(" €", "");
        priceNo = priceNo.replace(".", "");
        priceNo = priceNo.replace(",", ".");
        return Float.parseFloat(priceNo);
    }

    public static boolean isValid(String pageURL)
    {
        try
        {
            Document document = Jsoup.connect(pageURL).get();
            Elements priceContainer = document.select("strong[class^=xxxx-large ");
            if(priceContainer.isEmpty())
                return false;
            Elements img = document.select("img[class^=vtop bigImage]");
            if(img.isEmpty())
                return false;
            Elements descriptionContainer = document.select("div[id=textContent]");
            Elements description = descriptionContainer.select("p");
            String check = description.html();
            Elements titleE = document.select("h1");
            check += titleE.html();
            for(int i = 0; i < forbiddenWords.length; i++)
                if(check.contains(forbiddenWords[i]))
                    return false;
        }
        catch(IOException e)
        {
            return false;
        }
        return true;
    }
}