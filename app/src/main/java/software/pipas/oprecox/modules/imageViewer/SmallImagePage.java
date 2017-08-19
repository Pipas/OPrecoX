package software.pipas.oprecox.modules.imageViewer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import software.pipas.oprecox.R;
import software.pipas.oprecox.activities.singlePlayer.RevampedGameActivity;


public class SmallImagePage extends Fragment
{
    private int page;
    private Bitmap bitmap;
    private View view;
    private RevampedGameActivity gameActivity;

    public static SmallImagePage newInstance(int page, Bitmap bitmap, RevampedGameActivity gameActivity)
    {
        SmallImagePage imagePage = new SmallImagePage();
        imagePage.setPage(page);
        imagePage.setBitmap(bitmap);
        imagePage.setGameActivity(gameActivity);
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
        view = inflater.inflate(R.layout.small_image_page_layout, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.myImageView);
        imageView.setImageBitmap(bitmap);

        view.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                gameActivity.startImageViewerActivity(page);
            }
        });

        return view;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public void setBitmap (Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public void setGameActivity (RevampedGameActivity gameActivity)
    {
        this.gameActivity = gameActivity;
    }
}
