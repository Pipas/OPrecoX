package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.categories.ParentCategory;

public class CategoryListAdapter extends ArrayAdapter<ParentCategory>
{
    Context mContext;
    ContentResolver mContentResolver;

    public CategoryListAdapter(ArrayList<ParentCategory> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.parent_category_item, data);
        this.mContext = context;
        this.mContentResolver = contentResolver;
    }

    public void remove(int position)
    {
        super.remove(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.parent_category_item, null);
        }

        ParentCategory category = getItem(position);

        if (category != null)
        {
            TextView parentCategoryTitle = (TextView) v.findViewById(R.id.parentCategoryTitle);
            ImageView parentCategoryImage = (ImageView) v.findViewById(R.id.parentCategoryImage);

            if (parentCategoryTitle != null)
            {
                parentCategoryTitle.setText(category.getTitle());
            }

            if (parentCategoryImage != null)
            {
                parentCategoryImage.setImageResource(category.getImageId());
            }
        }

        return v;
    }
}
