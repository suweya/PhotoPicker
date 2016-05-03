package net.suweya.photopicker;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import net.suweya.photopicker.base.BaseFragment;
import net.suweya.photopicker.entity.Folder;
import net.suweya.photopicker.entity.Image;
import net.suweya.photopicker.preview.ImagePreviewActivity;
import net.suweya.photopicker.util.ArrayUtil;
import net.suweya.photopicker.util.ScreenUtils;
import net.suweya.photopicker.widget.GridDividerDecoration;

import java.util.ArrayList;
import java.util.Locale;

/**
 * PhotoPickerFragment
 * Created by suweya on 16/4/9.
 */
public class PhotoPickerFragment extends BaseFragment<PhotoPickerContract.Presenter>
        implements PhotoPickerContract.View {

    public static final int SPAN_COUNT = 3;
    private static final String KEY_SHOW_CAMERA_GRID = "SHOW_CAMERA_GRID";

    public static PhotoPickerFragment newInstance(boolean showCameraGrid) {

        Bundle args = new Bundle();
        args.putBoolean(KEY_SHOW_CAMERA_GRID, showCameraGrid);

        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private boolean mShowCameraGrid;
    private ListPopupWindow mFolderPopupWindow;
    private View mPopupAnchorView;
    private Button mPreviewButton;
    private View mBackgroundView;
    private Button mCategoryButton;
    private PhotoPickerActivity mPhotoPickerActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPhotoPickerActivity = (PhotoPickerActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // improve glide memory category
        Glide.get(getContext()).setMemoryCategory(MemoryCategory.HIGH);
        mShowCameraGrid = getArguments().getBoolean(KEY_SHOW_CAMERA_GRID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_photo_picker, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        mRecyclerView.addItemDecoration(new GridDividerDecoration(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mPopupAnchorView = view.findViewById(R.id.footer);
        mPreviewButton = (Button) view.findViewById(R.id.preview);

        mBackgroundView = view.findViewById(R.id.background);

        mCategoryButton = (Button) view.findViewById(R.id.category_btn);
        mCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFolderPickerPop();
            }
        });

        mPreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray array = ((PhotoPickerAdapter)mRecyclerView.getAdapter()).getCheckedArray();
                if (array != null) {
                    ImagePreviewActivity.start(getContext(), ArrayUtil.asKeyList(array));
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //局部刷新
        if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            GridLayoutManager manager = (GridLayoutManager) mRecyclerView.getLayoutManager();
            int positionStart = manager.findFirstVisibleItemPosition();
            int itemCount = manager.findLastVisibleItemPosition() - positionStart;
            PhotoPickerAdapter adapter = (PhotoPickerAdapter) mRecyclerView.getAdapter();
            adapter.notifyItemRangeChanged(positionStart, itemCount);
            updatePreviewButton(adapter.getSelectedCount());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.fetchGalleryData(getContext());
    }

    @Override
    public void showToast(int message) {
        Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGallery(ArrayList<Image> images) {
        ImageData.getInstance().setAllImages(images);
        PhotoPickerAdapter adapter = new PhotoPickerAdapter(getContext(), images, mShowCameraGrid,
                new PhotoPickerAdapter.PhotoCheckListener() {
                    @Override
                    public void onPhotoCheck(int count) {
                        updatePreviewButton(count);
                    }

                    @Override
                    public void onItemViewClick(ArrayList<Image> images, int position) {
                        ImageData.getInstance().setImages(images);
                        ImagePreviewActivity.start(getContext(), position);
                    }
                }, mPresenter);
        mRecyclerView.setAdapter(adapter);
    }

    private void updatePreviewButton(int count) {
        boolean enable = !(count == 0);
        if (enable) {
            mPreviewButton.setText(String.format(Locale.CHINA, "预览(%d)", count));
        } else {
            mPreviewButton.setText("预览");
        }
        mPreviewButton.setEnabled(enable);

        // update send button num
        mPhotoPickerActivity.modifySendButtonNum(count);
    }

    @Override
    public void showFilterGallery(ArrayList<Image> images, boolean showCameraGrid) {
        PhotoPickerAdapter adapter = (PhotoPickerAdapter) mRecyclerView.getAdapter();
        if (adapter != null) {
            adapter.refreshData(images, showCameraGrid);
            mRecyclerView.scrollToPosition(0);
            mFolderPopupWindow.dismiss();
        }
    }

    @Override
    public void showFolderPickerPop() {
        if (mFolderPopupWindow == null) {
            createPopupFolderListAndShow(mPresenter.fetchFolderData());
        } else if (mFolderPopupWindow.isShowing()){
            mFolderPopupWindow.dismiss();
        } else {
            mBackgroundView.setVisibility(View.VISIBLE);
            mFolderPopupWindow.show();
            ListView listView = mFolderPopupWindow.getListView();
            int position = ((FolderAdapter)listView.getAdapter()).getSelectedPosition();
            position = position == 0 ? position : (position - 1);
            listView.setSelection(position);
        }
    }

    @Override
    public void changeGalleryName(String categoryName) {
        if (mCategoryButton != null) {
            mCategoryButton.setText(categoryName);
        }
    }

    @Override
    protected PhotoPickerContract.Presenter createPresenter() {
        return new PhotoPickerPresenter(this, Injection.providePhotoPickerModel());
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderListAndShow(ArrayList<Folder> folders) {
        if (folders == null) return;

        Point point = ScreenUtils.getScreenSize(getActivity());
        int width = point.x;
        int height = (int) (point.y * 0.7);
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        //mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(new FolderAdapter(getContext(), folders));
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBackgroundView.setVisibility(View.GONE);
            }
        });
        mFolderPopupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.filterFolderImage(position);
                ((FolderAdapter) parent.getAdapter()).setSelectedPosition(position);
            }
        });

        mBackgroundView.setVisibility(View.VISIBLE);
        mFolderPopupWindow.show();
        mFolderPopupWindow.getListView().setSelector(android.R.color.transparent);
    }

    public ArrayList<String> getSelectedImagePath() {
        return  mPresenter.getSelectedImagePath();
    }
}
