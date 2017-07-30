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
        for(SubCategory subCategory : subCategories)
            subCategory.setParentCategory(title);
        this.imageId = categoryImage;
    }

    public ArrayList<SubCategory> getSubCategories()
    {
        return subCategories;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void toggleSubCategory(int i)
    {

        if(this.isSelected())
        {
            subCategories.get(i).toggleSelected();
            this.toggleSelected();
            for(SubCategory subCategory : subCategories)
            {
                if (subCategory.isSelected())
                    this.toggleSelected();
            }
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
}
