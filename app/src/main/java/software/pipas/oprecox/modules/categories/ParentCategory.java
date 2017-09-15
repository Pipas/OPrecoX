package software.pipas.oprecox.modules.categories;

import java.util.ArrayList;

public class ParentCategory extends Category
{
    private ArrayList<SubCategory> subCategories;
    private int imageId;
    private int bigImageId;

    public ParentCategory(String title, ArrayList<SubCategory> subCategories, int regularImageId, int bigImageId)
    {
        super(title);
        this.subCategories = subCategories;
        for(SubCategory subCategory : subCategories)
            subCategory.setParentCategory(title);
        this.imageId = regularImageId;
        this.bigImageId = bigImageId;
    }

    public ArrayList<SubCategory> getSubCategories()
    {
        return subCategories;
    }

    public int getRegularImageId()
    {
        return imageId;
    }

    public int getBigImageId()
    {
        return bigImageId;
    }

    public void toggleSubCategory(int i)
    {

        if(this.isSelected())
        {
            subCategories.get(i).toggleSelected();
            this.toggleSelected();
            Boolean selected = false;
            for(SubCategory subCategory : subCategories)
            {
                if (subCategory.isSelected())
                    selected = true;
            }
            this.setSelected(selected);
        }
        else
        {
            subCategories.get(i).toggleSelected();
            this.toggleSelected();
        }

    }

    public void selectAll()
    {
        this.setSelected(true);
        for(SubCategory subCategory : subCategories)
        {
            subCategory.setSelected(true);
        }
    }

    public void deSelectAll()
    {
        this.setSelected(false);
        for(SubCategory subCategory : subCategories)
        {
            subCategory.setSelected(false);
        }
    }

    public void toggleAll()
    {
        if(this.isSelected())
        {
            for(SubCategory subCategory : subCategories)
            {
                subCategory.setSelected(false);
            }
            this.setSelected(false);
        }
        else
        {
            for(SubCategory subCategory : subCategories)
            {
                subCategory.setSelected(true);
            }
            this.setSelected(true);
        }

    }
}
