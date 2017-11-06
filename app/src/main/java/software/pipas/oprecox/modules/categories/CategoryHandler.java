package software.pipas.oprecox.modules.categories;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import software.pipas.oprecox.R;
import software.pipas.oprecox.util.Settings;

import static android.content.Context.MODE_PRIVATE;

public abstract class CategoryHandler
{
    private static ArrayList<ParentCategory> categories;


    public static boolean checkAllSelected()
    {
        for(ParentCategory parentCategory : categories)
            if(!parentCategory.isSelected())
                return false;

        return true;
    }

    public static SubCategory getRandomSubCategory()
    {
        ArrayList<SubCategory> selectedSubCategories = new ArrayList<>();
        for(ParentCategory parentCategory : categories)
        {
            if(parentCategory.isSelected())
            {
                for(SubCategory subCategory : parentCategory.getSubCategories())
                {
                    if(subCategory.isSelected())
                        selectedSubCategories.add(subCategory);
                }
            }
        }
        if(selectedSubCategories.isEmpty())
        {
            selectAll();
            for(ParentCategory parentCategory : categories)
            {
                if(parentCategory.isSelected())
                {
                    for(SubCategory subCategory : parentCategory.getSubCategories())
                    {
                        if(subCategory.isSelected())
                            selectedSubCategories.add(subCategory);
                    }
                }
            }
            deSelectAll();
        }
        Random rand = new Random();
        return selectedSubCategories.get(rand.nextInt(selectedSubCategories.size()));
    }

    public static void selectAll()
    {
        for(ParentCategory parentCategory : categories)
            parentCategory.selectAll();
    }

    public static void deSelectAll()
    {
        for(ParentCategory parentCategory : categories)
            parentCategory.deSelectAll();
    }

    public static Boolean validSelection()
    {
        for(ParentCategory parentCategory : categories)
            if(parentCategory.isSelected())
                return true;

        return false;
    }

    public static void selectFromString(String input)
    {
        Log.d("CATEGORIES", input);
        if(!input.contains(":"))
        {
            selectAll();
            return;
        }
        String[] splited = input.split("\\s+");
        for(int i = 0; i < splited.length; i++)
        {
            String[] subsplit = splited[i].split(":");
            categories.get(Integer.parseInt(subsplit[0])).toggleSubCategory(Integer.parseInt(subsplit[1]));
        }
    }

    public static String saveToString()
    {
        String selectedCategories = "";

        for(int i = 0; i < categories.size(); i++)
        {
            if(categories.get(i).isSelected())
            {
                for(int sub = 0; sub < categories.get(i).getSubCategories().size(); sub++)
                {
                    if(categories.get(i).getSubCategories().get(sub).isSelected())
                        selectedCategories += i + ":" + sub + " ";
                }
            }
        }

        return selectedCategories;
    }

    public static void initiateFromXml(InputStream in_s, Activity activity)
    {
        XmlPullParserFactory pullParserFactory;

        try
        {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            int eventType = parser.getEventType();

            String tag;
            String parentTitle = null;
            String parentImage = null;
            String urlend = null;
            Double urldist = null;
            ArrayList<SubCategory> subCategories = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_DOCUMENT:
                        categories = new ArrayList();
                        break;
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if(tag.equals("parent"))
                        {
                            subCategories = new ArrayList<>();
                        }
                        else if (tag.equals("ptitle"))
                        {
                            parentTitle = parser.nextText();
                        }
                        else if (tag.equals("image"))
                        {
                            parentImage = parser.nextText();
                        }
                        else if(tag.equals("child"))
                        {
                            SubCategory sub = new SubCategory();
                            subCategories.add(sub);
                        }
                        else if(!subCategories.isEmpty())
                        {
                            if(tag.equals("ctitle"))
                            {
                                subCategories.get(subCategories.size() - 1).setTitle(parser.nextText());
                            }
                            else if(tag.equals("urlend"))
                            {
                                urlend = parser.nextText();
                            }
                            else if(tag.equals("dist"))
                            {
                                urldist = Double.parseDouble(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equalsIgnoreCase("url"))
                        {
                            subCategories.get(subCategories.size() - 1).addUrlEnd(urlend, urldist);
                        }
                        else if (tag.equalsIgnoreCase("parent"))
                        {
                            categories.add(new ParentCategory(parentTitle, subCategories, activity.getResources().getIdentifier(parentImage, "drawable", activity.getPackageName()), activity.getResources().getIdentifier(parentImage + "big", "drawable", activity.getPackageName())));
                        }
                }
                eventType = parser.next();
            }
            Log.d("END","END");

        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void checkIfRestart(Activity activity)
    {
        Toasty.Config.getInstance().setInfoColor(ContextCompat.getColor(activity.getBaseContext(), R.color.lightOrange)).apply();
        if(categories == null)
        {
            SharedPreferences sharedPref = activity.getSharedPreferences("gameSettings", MODE_PRIVATE);
            try
            {
                CategoryHandler.initiateFromXml(activity.getApplicationContext().getAssets().open("categories.xml"), activity);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            String c = sharedPref.getString("categories", null);
            if(c != null)
                CategoryHandler.selectFromString(c);

            Settings.setGameSize(sharedPref.getInt("gameSize", 10), activity);
            Settings.setGameTime(sharedPref.getInt("gameTime", 60), activity);
            Settings.setAdCountdown(sharedPref.getInt("adCountdown", 2));
            Settings.setGamesPlayed(sharedPref.getInt("gamesPlayed", 0));
            Settings.setShowRateUs(sharedPref.getBoolean("showRateUs", true));
        }
    }

    public static ArrayList<ParentCategory> getCategories()
    {
        return categories;
    }

    public static void setCategories(ArrayList<ParentCategory> categories)
    {
        CategoryHandler.categories = categories;
    }
}
