package software.pipas.oprecox.modules.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import software.pipas.oprecox.R;
import software.pipas.oprecox.modules.categories.SubCategory;

public class SubCategoryListAdapter extends ArrayAdapter<SubCategory>
{
    Context mContext;
    ContentResolver mContentResolver;

    public SubCategoryListAdapter(ArrayList<SubCategory> data, Context context, ContentResolver contentResolver)
    {
        super(context, R.layout.sub_category_item, data);
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
            v = vi.inflate(R.layout.sub_category_item, null);
        }

        SubCategory category = getItem(position);

        if (category != null)
        {
            TextView subCategoryTitle = (TextView) v.findViewById(R.id.subCategoryTitle);

            if (subCategoryTitle != null)
            {
                subCategoryTitle.setText(category.getTitle());
            }

            if(category.isSelected())
            {
                CheckBox subCategoryCheckbox = (CheckBox) v.findViewById(R.id.subCategoryCheckbox);
                subCategoryCheckbox.setChecked(true);
            }
        }

        return v;
    }


}
