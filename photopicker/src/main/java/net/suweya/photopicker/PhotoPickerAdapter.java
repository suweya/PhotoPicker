package net.suweya.photopicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import net.suweya.photopicker.entity.Image;
import net.suweya.photopicker.widget.SquareImageView;

import java.util.ArrayList;

/**
 * PhotoPickerAdapter
 * Created by suweya on 16/4/9.
 */
public class PhotoPickerAdapter extends RecyclerView.Adapter<ViewHolder>
        implements View.OnClickListener {

    interface PhotoCheckListener {
        void onPhotoCheck(int count);

        void onItemViewClick(ArrayList<Image> images, int position);
    }

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_CAMERA = 1;

    private Context mContext;
    private ArrayList<Image> mImages;
    private LayoutInflater mLayoutInflater;
    private boolean mShowCameraGrid;
    //private int mSelectedCount;
    private PhotoCheckListener mPhotoCheckListener;

    //选中记录
    private SparseBooleanArray mCheckedArray = new SparseBooleanArray();
    //Presenter
    private PhotoPickerContract.Presenter mPhotoPickerPresenter;

    public PhotoPickerAdapter(@NonNull Context context,
                              @NonNull  ArrayList<Image> images,
                              boolean showCameraGrid,
                              @NonNull  PhotoCheckListener listener,
                              @NonNull PhotoPickerContract.Presenter presenter) {
        mContext = context;
        mImages = images;
        mLayoutInflater = LayoutInflater.from(context);
        mShowCameraGrid = showCameraGrid;
        mPhotoCheckListener = listener;
        mPhotoPickerPresenter = presenter;

        ImageData.getInstance().setCheckedArray(mCheckedArray);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            VH holder = new VH(mLayoutInflater.inflate(R.layout.item_image, parent, false));
            holder.mCheckMark.setOnClickListener(this);
            holder.itemView.setOnClickListener(this);
            return holder;
        } else {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_camera, parent, false)){};
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof VH) {
            VH vh = (VH) holder;
            position = mShowCameraGrid ? position-1 : position;
            Image image = mImages.get(position);
            Glide.with(mContext)
                    .load(image.path)
                    //.asBitmap()
                    .placeholder(R.drawable.image_loading_placeholder)
                    .error(R.drawable.ic_photo_size_select_actual_black_24dp)
                    .into(vh.mImageView);

            if (image.selected) {
                vh.mCheckMark.setChecked(true);
                vh.mMask.setVisibility(View.VISIBLE);
            } else {
                vh.mCheckMark.setChecked(false);
                vh.mMask.setVisibility(View.GONE);
            }
            vh.mCheckMark.setTag(vh.mMask);
            vh.mMask.setTag(position);
            vh.itemView.setTag(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.check_mark) {

            if (!mPhotoPickerPresenter.isMaxImageSelected(mCheckedArray)) {
                View mask = (View) v.getTag();
                int position = (int) mask.getTag();
                Image image = mImages.get(position);
                boolean value = !image.selected;
                mask.setVisibility(value ? View.VISIBLE : View.GONE);
                ((AppCompatCheckBox) v).setChecked(value);
                image.selected = value;

                //mSelectedCount = value ? mSelectedCount+1 : mSelectedCount-1;

                if (value) {
                    mCheckedArray.put(image.position, true);
                } else {
                    mCheckedArray.delete(image.position);
                }

                mPhotoCheckListener.onPhotoCheck(mCheckedArray.size());
            }

        } else if (v.getId() == R.id.item_view) {
            int position = (int) v.getTag();
            mPhotoCheckListener.onItemViewClick(mImages, position);
        }
    }

    public SparseBooleanArray getCheckedArray() {
        if (mCheckedArray.size() > 0) {
            return mCheckedArray;
        }
        return null;
    }

    public int getSelectedCount() {
        return mCheckedArray.size();
    }

    @Override
    public int getItemCount() {
        return mShowCameraGrid ? mImages.size()+1 : mImages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mShowCameraGrid && position == 0 ? TYPE_CAMERA : TYPE_IMAGE;
    }

    public void refreshData(ArrayList<Image> images, boolean showCameraGrid) {
        mImages = images;
        mShowCameraGrid = showCameraGrid;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {

        AppCompatCheckBox mCheckMark;
        View mMask;
        SquareImageView mImageView;

        public VH(View itemView) {
            super(itemView);
            itemView.setId(R.id.item_view);
            mCheckMark = (AppCompatCheckBox) itemView.findViewById(R.id.check_mark);
            mMask = itemView.findViewById(R.id.mask);
            mImageView = (SquareImageView) itemView.findViewById(R.id.image);
        }
    }
}
