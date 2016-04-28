package net.suweya.photopicker.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import net.suweya.photopicker.R;
import net.suweya.photopicker.base.BaseFragment;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * ImagePreviewFragment
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewFragment extends BaseFragment<ImagePreviewContract.Presenter> implements ImagePreviewContract.View {

    public static final String CURRENT_ITEM = "CURRENT_ITEM";

    public static ImagePreviewFragment newInstance(int item) {

        Bundle args = new Bundle();
        args.putInt(CURRENT_ITEM, item);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImagePreviewFragment newInstance(ArrayList<Integer> list) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(ImagePreviewActivity.KEY_CHECKED_POS_ARRAY, list);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImagePreviewActivity mImagePreviewActivity;
    private ViewPager mViewPager;
    private AppCompatCheckBox mAppCompatCheckBox;
    private int mCurrentSelectedPosition;

    public ImagePreviewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImagePreviewActivity = (ImagePreviewActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_image_preview, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mAppCompatCheckBox = (AppCompatCheckBox) view.findViewById(R.id.check_mark);

        mPresenter.filterCheckedImageList(getArguments());

        return view;
    }

    @Override
    protected ImagePreviewContract.Presenter createPresenter() {
        return new ImagePreviewPresenter(this);
    }

    @Override
    public void setUpViewPager(final ArrayList<Image> images, int pos) {
        final int imageCount = images.size();
        mCurrentSelectedPosition = pos;
        mViewPager.setAdapter(new ImagePreviewAdapter(getChildFragmentManager(), images));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentSelectedPosition = position;
                mImagePreviewActivity.setTitle((position+1) + "/" + imageCount);
                mAppCompatCheckBox.setChecked(images.get(position).selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mCurrentSelectedPosition);
        if (mCurrentSelectedPosition == 0) {
            mImagePreviewActivity.setTitle((mCurrentSelectedPosition+1) + "/" + imageCount);
            mAppCompatCheckBox.setChecked(images.get(mCurrentSelectedPosition).selected);
        }
        mAppCompatCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                images.get(mCurrentSelectedPosition).selected = isChecked;
            }
        });
    }
}
