package net.suweya.photopicker.preview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.suweya.photopicker.ImageData;
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
    public static final String KEY_PREVIEW_TYPE = "PREVIEW_TYPE";
    public static final int TYPE_ITEM_PREVIEW = 0;
    public static final int TYPE_CHECKED_PREVIEW = 1;

    public static ImagePreviewFragment newInstance(int item) {

        Bundle args = new Bundle();
        args.putInt(CURRENT_ITEM, item);
        args.putInt(KEY_PREVIEW_TYPE, TYPE_ITEM_PREVIEW);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImagePreviewFragment newInstance(ArrayList<Integer> list) {

        Bundle args = new Bundle();
        args.putIntegerArrayList(ImagePreviewActivity.KEY_CHECKED_POS_ARRAY, list);
        args.putInt(KEY_PREVIEW_TYPE, TYPE_CHECKED_PREVIEW);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImagePreviewActivity mImagePreviewActivity;
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private AppCompatImageView mCheckMark;
    private int mCurrentSelectedPosition;
    private SparseBooleanArray mCheckedArray;
    private RelativeLayout mBottomBar;
    //private GestureDetectorCompat mGestureDetectorCompat;

    public ImagePreviewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImagePreviewActivity = (ImagePreviewActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckedArray = ImageData.getInstance().getCheckedArray();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_image_preview, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mCheckMark = (AppCompatImageView) view.findViewById(R.id.check_mark);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_check_mark);
        mBottomBar = (RelativeLayout) view.findViewById(R.id.bottom_bar);

        mPresenter.filterCheckedImageList(getArguments());

        /*mGestureDetectorCompat = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Toast.makeText(getContext(), "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
                return super.onSingleTapUp(e);
            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetectorCompat.onTouchEvent(event);
                return false;
            }
        });*/

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
                mImagePreviewActivity.setTitle((position + 1) + "/" + imageCount);
                mCheckMark.setSelected(images.get(position).selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(mCurrentSelectedPosition);
        if (mCurrentSelectedPosition == 0) {
            mImagePreviewActivity.setTitle((mCurrentSelectedPosition + 1) + "/" + imageCount);
            mCheckMark.setSelected(images.get(mCurrentSelectedPosition).selected);
        }
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Image image = images.get(mCurrentSelectedPosition);
                boolean isChecked = !image.selected;
                modifyCheckStatus(isChecked, image);
            }
        });
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void modifyCheckStatus(boolean value, Image image) {
        if (mCheckedArray != null) {
            if (value) {
                boolean allow = !mPresenter.isMaxImageSelected(mCheckedArray);
                if (allow) {
                    mCheckedArray.put(image.position, true);
                    image.selected = true;
                    mCheckMark.setSelected(true);
                }
            } else {
                mCheckedArray.delete(image.position);
                image.selected = false;
                mCheckMark.setSelected(false);
            }
        }
    }

    public void toggleHideBar() {

        /*
        BEGIN_INCLUDE (get_current_ui_flags)
        The UI options currently enabled are represented by a bitfield.
        getSystemUiVisibility() gives us that bitfield.
        */
        int uiOptions = getActivity().getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        mImagePreviewActivity.toggleToolbar(isImmersiveModeEnabled);
        if (isImmersiveModeEnabled) {
            mBottomBar.animate().translationYBy(-mBottomBar.getHeight())
                    .setDuration(ImagePreviewActivity.DEFAULT_ANIMATION_DURATION).start();
        } else {
            mBottomBar.animate().translationYBy(mBottomBar.getHeight())
                    .setDuration(ImagePreviewActivity.DEFAULT_ANIMATION_DURATION).start();
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getActivity().getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }
}
