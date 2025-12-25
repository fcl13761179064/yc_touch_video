package com.lucky.sound.main.pager;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class GridPagerSnapHelper extends SnapHelper {
    private static final int MAX_SCROLL_ON_FLING_DURATION = 100;
    @Nullable
    private OrientationHelper mVerticalHelper;
    @Nullable
    private OrientationHelper mHorizontalHelper;
    private RecyclerView recyclerView;

    private int pageCount;
    private int curPage;


    public GridPagerSnapHelper(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }


    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = this.distanceToStart(layoutManager, targetView, this.getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = this.distanceToStart(layoutManager, targetView, this.getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }

        return out;
    }

    /**
     * 该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SanpView，对应的position称为SnapPosition。如果返回null，就表示没有需要对齐的View，也就不会做滚动对齐调整。
     */
    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return this.findStartSnapView(layoutManager, this.getVerticalHelper(layoutManager));
        } else {
            return layoutManager.canScrollHorizontally() ? this.findStartSnapView(layoutManager, this.getHorizontalHelper(layoutManager)) : null;
        }
    }

    /**
     *该方法会根据触发Fling操作的速率（参数velocityX和参数velocityY）来找到RecyclerView需要滚动到哪个位置，
     * 该位置对应的ItemView就是那个需要进行对齐的列表项。我们把这个位置称为targetSnapPosition，
     * 对应的View称为targetSnapView。如果找不到targetSnapPosition，就返回RecyclerView.NO_POSITION。
     * 版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
     * 原文链接：https://blog.csdn.net/2401_84121871/article/details/137531662
     */
    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int itemCount = layoutManager.getItemCount();
        if (itemCount == 0) {
            return -1;
        } else {
            View mStartMostChildView = null;
            if (layoutManager.canScrollVertically()) {
                mStartMostChildView = this.findStartView(layoutManager, this.getVerticalHelper(layoutManager));
            } else if (layoutManager.canScrollHorizontally()) {
                mStartMostChildView = this.findStartView(layoutManager, this.getHorizontalHelper(layoutManager));
            }

            if (mStartMostChildView == null) {
                return -1;
            } else {
                int centerPosition = layoutManager.getPosition(mStartMostChildView);
                if (centerPosition == -1) {
                    return -1;
                } else {
                    //　计算当前页面索引
                    int pagerIndex = centerPosition / pageCount;
                    // 是否滑向下一页
                    boolean forwardDirection;
                    if (layoutManager.canScrollHorizontally()) {
                        forwardDirection = velocityX > 0;
                    } else {
                        forwardDirection = velocityY > 0;
                    }

                    //　条目是否是翻转模式
                    boolean reverseLayout = false;
                    if (layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) {
                        RecyclerView.SmoothScroller.ScrollVectorProvider vectorProvider = (RecyclerView.SmoothScroller.ScrollVectorProvider) layoutManager;
                        PointF vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1);
                        if (vectorForEnd != null) {
                            reverseLayout = vectorForEnd.x < 0.0F || vectorForEnd.y < 0.0F;
                        }
                    }
                    int targetPosition = -1;
                    if (reverseLayout) {
                        targetPosition = (forwardDirection ? (pagerIndex - 1) * pageCount : (pagerIndex) * pageCount);
                    } else {
                        targetPosition = (forwardDirection ? (pagerIndex + 1) * pageCount : (pagerIndex) * pageCount);
                    }
                    return targetPosition;
                }
            }
        }
    }

    //创建一个平滑滚动其
    @Override
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        return !(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.recyclerView.getContext()) {
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = GridPagerSnapHelper.this.calculateDistanceToFinalSnap(GridPagerSnapHelper.this.recyclerView.getLayoutManager(), targetView);
                int dx = snapDistances[0];
                int dy = snapDistances[1];
                int time = this.calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, this.mDecelerateInterpolator);
                }

            }

            //每个像素滚动到底部需要时间   //滚动速率
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 60.0F / (float) displayMetrics.densityDpi;
            }

            protected int calculateTimeForScrolling(int dx) {
                return Math.min(100, super.calculateTimeForScrolling(dx));
            }
        };
    }

    private int distanceToStart(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView, OrientationHelper helper) {
        int childStart = helper.getDecoratedStart(targetView);
        int containerStart;
        if (layoutManager.getClipToPadding()) {
            containerStart = helper.getStartAfterPadding();
        } else {
            containerStart = 0;
        }

        return childStart - containerStart;
    }

    @Nullable
    private View findStartSnapView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        } else {
            View closestChild = null;
            View lingChild = null;
            int start;
            if (layoutManager.getClipToPadding()) {
                start = helper.getStartAfterPadding();
            } else {
                start = 0;
            }

            int absClosest = 2147483647;

            for (int i = 0; i < childCount; ++i) {
                View child = layoutManager.getChildAt(i);

                if (layoutManager.getPosition(child) % pageCount == 0) {
                    lingChild = child;
                }

                int childStart = helper.getDecoratedStart(child);
                int absDistance = Math.abs(childStart - start);
                if (absDistance < absClosest) {
                    absClosest = absDistance;
                    closestChild = child;
                }
            }

            int position = layoutManager.getPosition(closestChild);
            if (position % pageCount != 0) {
                if (position / pageCount + 1 < curPage) {
                    recyclerView.smoothScrollToPosition(position / pageCount * pageCount);
                    return null;
                } else
                    return lingChild;
            }

            return closestChild;
        }
    }

    @Nullable
    private View findStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper
            helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        } else {
            View closestChild = null;
            int startest = 2147483647;

            for (int i = 0; i < childCount; ++i) {
                View child = layoutManager.getChildAt(i);
                int childStart = helper.getDecoratedStart(child);
                if (childStart < startest) {
                    startest = childStart;
                    closestChild = child;
                }
            }
            return closestChild;
        }
    }

    @NonNull
    private OrientationHelper getVerticalHelper(@NonNull RecyclerView.LayoutManager
                                                        layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.getLayoutManager() != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }

        return this.mVerticalHelper;
    }

    @NonNull
    private OrientationHelper getHorizontalHelper(@NonNull RecyclerView.LayoutManager
                                                          layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.getLayoutManager() != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        return this.mHorizontalHelper;
    }
}
