package unified.android.kalido.me.unifiedsearch.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

public class BlankRecyclerView extends RecyclerView {

    private class DataSetObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            checkEmptyState();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            // do nothing
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkEmptyState();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            // do nothing
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkEmptyState();
        }
    }

    public interface EmptyViewObserver {

        void onHideEmptyView();

        void onShowEmptyView();
    }

    public interface FilteredAdapter<T> {

        ArrayList<T> getFilteredData();

        boolean isFiltered();
    }

    private Adapter mAdapter;

    private DataSetObserver mDataSetObserver;

    private EmptyViewObserver mEmptyViewObserver;

    private View[] mEmptyViews;

    public BlankRecyclerView(Context context) {
        super(context);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlankRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void checkEmptyState() {
        updateEmptyStatus(mAdapter == null || getData() == null || getData().isEmpty());
    }

    public boolean isInFilterMode() {
        return mAdapter != null && ((FilteredAdapter) mAdapter).isFiltered();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (isInEditMode()) {
            super.setAdapter(adapter);
            return;
        }
        if (adapter instanceof FilteredAdapter) {
            if (mEmptyViews == null) {
                throw new IllegalArgumentException("BlankRecyclerView expects Empty View to be set at this point");
            }

            setupObserver(adapter);
            super.setAdapter(adapter);
            checkEmptyState();
        } else {
            throw new IllegalArgumentException("BlankRecyclerView depends on the adapter implementing FilteredAdapter");
        }
    }

    public void setEmptyViewObserver(EmptyViewObserver emptyViewObserver) {
        this.mEmptyViewObserver = emptyViewObserver;
    }

    public void setEmptyViews(View... emptyView) {
        this.mEmptyViews = emptyView;
    }

    public void setOnScrollChangeListener(@NotNull final Function0<Unit> scroll, @NotNull final Function0<Unit> bottom, final Function0<Boolean> busy) {
        clearOnScrollListeners();
        addOnScrollListener(new OnScrollListener() {
            Boolean scrollingDown = false;

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scroll.invoke();
                if (recyclerView != null && recyclerView.canScrollVertically(1) == false && scrollingDown && !busy.invoke()) {
                    bottom.invoke();
                    scrollingDown = false;
                }
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollingDown = dy > 0;
            }
        });
    }

    private ArrayList getData() {
        return ((FilteredAdapter) mAdapter).getFilteredData();
    }

    private void setupObserver(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterAdapterDataObserver(mDataSetObserver);
        }

        if (mDataSetObserver == null) {
            mDataSetObserver = new DataSetObserver();
        }
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataSetObserver);
    }

    private void updateEmptyStatus(boolean empty) {
        if (isInFilterMode()) {
            empty = false;
        }

        if (empty) {
            if (mEmptyViews != null) {
                for (View view : mEmptyViews) {
                    view.setVisibility(View.VISIBLE);
                }

                setVisibility(View.GONE);
                if (mEmptyViewObserver != null) {
                    mEmptyViewObserver.onShowEmptyView();
                }
            } else {
                // If the caller just removed our empty view, make sure the list view is visible
                setVisibility(View.VISIBLE);
                if (mEmptyViewObserver != null) {
                    mEmptyViewObserver.onHideEmptyView();
                }
            }
        } else {
            if (mEmptyViews != null) {
                for (View view : mEmptyViews) {
                    view.setVisibility(View.GONE);
                }
            }
            setVisibility(View.VISIBLE);
            if (mEmptyViewObserver != null) {
                mEmptyViewObserver.onHideEmptyView();
            }
        }
    }
}
