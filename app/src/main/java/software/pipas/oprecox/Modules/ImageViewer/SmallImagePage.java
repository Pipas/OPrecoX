package software.pipas.oprecox.modules.imageViewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import software.pipas.oprecox.R;

public class SmallImagePage extends Fragment
{
    private int page;
    private Bitmap bm;

    public static SmallImagePage newInstance(int page, Bitmap bit)
    {
        SmallImagePage imagePage = new SmallImagePage();
        imagePage.setPage(page);
        imagePage.setBm(bit);
        return imagePage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.small_image_page_layout, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.myImageView);
        imageView.setImageBitmap(bm);

        return view;
    }

    public void setPage(int p)
    {
        page = p;
    }

    public void setBm (Bitmap b)
    {
        bm = b;
    }
}
