package software.pipas.oprecox.modules.categories;

import java.util.ArrayList;

public class ParentCategory extends Category
{
    private ArrayList<SubCategory> subCategories;
    private int imageId;

    public ParentCategory(String title, ArrayList<SubCategory> subCategories, int categoryImage)
    {
        super(title);
        this.subCategories = subCategories;
        this.imageId = categoryImage;
    }

    public ArrayList<SubCategory> getSubCategories()
    {
        return subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories)
    {
        this.subCategories = subCategories;
    }

    public int getImageId()
    {
        return imageId;
    }
}
