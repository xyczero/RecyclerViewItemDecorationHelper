package com.xyczero.recyclerviewitemdecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.graphics.Rect;
import android.view.View;

/**
 * @author xyczero
 * @time 2015/6/12
 */

/**
 * A {@link android.support.v7.widget.RecyclerView.ItemDecoration} implementations that
 * calculates the outer of the item that should space.
 * Just consider the orientation of VERTICAL.
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    private int mSpanCount;
    private int mRadixX;
    private int mItemCountInLastLine;
    // Record the count of old RV item
    // Judge whether the data of RV is changed,such as loading next page.
    // In that case,we need to recalculate the {@value mItemCountInLastLine} and refresh RV.
    private int mOldItemCount = -1;

    public ItemDecoration(int space) {
        this(space, 1);
    }

    /**
     * @param space
     * @param spanCount spans count of one lane
     */
    public ItemDecoration(int space, int spanCount) {
        this.mSpace = space;
        this.mSpanCount = spanCount;
        this.mRadixX = space / spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, final RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        final int sumCount = state.getItemCount();
        final int position = params.getViewLayoutPosition();
        final int spanSize;
        final int index;

        if (params instanceof GridLayoutManager.LayoutParams) {
            GridLayoutManager.LayoutParams gridParams = (GridLayoutManager.LayoutParams) params;
            spanSize = gridParams.getSpanSize();
            index = gridParams.getSpanIndex();

            if ((position == 0 || mOldItemCount != sumCount) && mSpanCount > 1) {
                int tempPosition = 0;
                int countInLine = 0;
                int spanIndex;

                //calculate item count in last line
                while (tempPosition < sumCount) {
                    spanIndex = ((GridLayoutManager) parent.getLayoutManager()).getSpanSizeLookup().getSpanIndex(tempPosition++, mSpanCount);
                    if (spanIndex != 0) {
                        countInLine++;
                    } else {
                        countInLine = 1;
                    }
                }
                mItemCountInLastLine = countInLine;

                if (mOldItemCount != sumCount) {
                    mOldItemCount = sumCount;
                    //first load don't need to refresh
                    if (position != 0) {
                        parent.post(new Runnable() {
                            @Override
                            public void run() {
                                parent.invalidateItemDecorations();
                            }
                        });
                    }
                }
            }
        } else {
            spanSize = 1;
            index = 0;
        }

        // invalid value
        if (spanSize < 1 || index < 0 || spanSize > mSpanCount) {
            return;
        }

        outRect.left = mSpace - mRadixX * index;
        outRect.right = mRadixX + mRadixX * (index + spanSize - 1);

        // set bottom to all in last lane
        if (mSpanCount == 1 && position == sumCount - 1) {
            outRect.bottom = mSpace;
        } else if (position >= sumCount - mItemCountInLastLine && position < sumCount) {
            outRect.bottom = mSpace;
        }

        //we not unify to set bottom instead of setting top because setting bottom is better.
        //when remove a item from the recyclerview,we usually use Adapter.notifyItemRemoved(index).
        //If the item is top or bottom ,it will lead to show incorrectly.
        //If we unify to set top,we will have two ways to fix it.
        //One is Adapter.notifyItemRemoved(index-1),the another is adding a footer to the adapter because a footer
        // usually not been gone.
        outRect.top = mSpace;
    }
}
