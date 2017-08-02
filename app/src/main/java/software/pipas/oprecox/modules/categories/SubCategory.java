package software.pipas.oprecox.modules.categories;

import java.util.HashMap;

public class SubCategory extends Category
{
    private HashMap<String, Double> urlEnds = new HashMap<>();;
    private double distSum = 0;
    private String parentCategory;

    public SubCategory() {}

    public SubCategory(String title, String urlEnd)
    {
        super(title);
        addUrlEnd(urlEnd, 1.0d);
    }

    public SubCategory(String title, String[] urlEnds, Double[] distributions)
    {
        super(title);
        if(urlEnds.length == distributions.length)
        {
            for(int i = 0; i < urlEnds.length; i++)
            {
                addUrlEnd(urlEnds[i], distributions[i]);
            }
        }
    }

    public void addUrlEnd(String urlEnd, double distribution)
    {
        this.urlEnds.put(urlEnd, distribution);
        distSum += distribution;
    }

    public String getUrlEnd()
    {
        double rand = Math.random();
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (String i : urlEnds.keySet())
        {
            tempDist += urlEnds.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return null;
    }

    public String getParentCategory()
    {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory)
    {
        this.parentCategory = parentCategory;
    }

}
