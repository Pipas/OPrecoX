package software.pipas.oprecox.modules.categories;

public class Category
{
    private String title;
    private Boolean selected;

    public Category(String title)
    {
        this.title = title;
        this.selected = false;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Boolean getSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }

    public void toggleSelected()
    {
        this.selected = !this.selected;
    }
}
