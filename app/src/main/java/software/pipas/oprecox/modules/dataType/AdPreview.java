package software.pipas.oprecox.modules.dataType;

public class AdPreview
{
    private String title;
    private String description;
    private byte[] thumbnail;
    private long id;

    public AdPreview() {}

    public AdPreview(String title, String description, byte[] thumbnail)
    {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public byte[] getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
