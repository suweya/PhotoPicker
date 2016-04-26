package net.suweya.photopicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.suweya.photopicker.entity.Folder;

import java.util.ArrayList;
import java.util.Locale;

/**
 * FolderAdapter
 * Created by suweya on 16/4/10.
 */
public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private ArrayList<Folder> mFolders;
    private int mSelectedPosition = 0;

    public FolderAdapter(Context context, ArrayList<Folder> folders){
        mContext = context;
        mFolders = folders;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public int getCount() {
        return mFolders.size();
    }

    @Override
    public Folder getItem(int i) {
        return mFolders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Folder data = mFolders.get(i);

        holder.name.setText(data.name);
        if (data.images != null) {
            holder.size.setText(String.format(Locale.CHINA, "%d%s", data.images.size(),
                    mContext.getResources().getString(R.string.photo_unit)));
        }
        // 显示图片
        Glide.with(mContext)
                .load(data.cover.path)
                .placeholder(R.drawable.image_loading_placeholder)
                .into(holder.cover);
        if (i == mSelectedPosition) {
            holder.indicator.setBackgroundResource(R.drawable.ic_check_black_24dp);
        } else {
            holder.indicator.setBackgroundResource(0);
        }

        return view;
    }

    static class ViewHolder{
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;

        ViewHolder(View view){
            cover = (ImageView)view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            size = (TextView) view.findViewById(R.id.size);
            view.setTag(this);
        }
    }
}
