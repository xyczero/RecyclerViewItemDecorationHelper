package com.xyczero.materialdesiginmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xyczero.recyclerviewitemdecoration.ItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {
    private static final int GRID_SPAN_COUOT = 3;
    @InjectView(R.id.recycleview)
    RecyclerView mRecyclerView;
    private RAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setupRecyclerView();
    }

    public void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, GRID_SPAN_COUOT);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookUp());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new RAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new ItemDecoration(24, GRID_SPAN_COUOT));
    }

    class SpanSizeLookUp extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            if (mAdapter.getItemViewType(position) == 1)
                return 1;
            else if (mAdapter.getItemViewType(position) == 2)
                return 2;
            else
                return 3;
        }
    }

    static class RAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ViewHolder.create(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 1;
            if (position == 3)
                return 1;
            if (position == 4)
                return 1;
            if (position % 2 == 0)
                return 2;
            else
                return 1;
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        static ViewHolder create(ViewGroup viewGroup, int viewtype) {
            switch (viewtype) {
                case 1:
                    return ViewItem1.create(viewGroup);
                case 2:
                case 3:
                    return ViewItem2.create(viewGroup);
            }
            return null;
        }

        abstract void bind();
    }

    static class ViewItem1 extends ViewHolder {
        @InjectView(R.id.item_text)
        TextView textView;

        public ViewItem1(View view) {
            super(view);
            ButterKnife.inject(this, itemView);
        }

        static ViewHolder create(ViewGroup viewGroup) {
            return new ViewItem1(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false));
        }

        void bind() {
            textView.setText("1111111111");
        }
    }

    static class ViewItem2 extends ViewHolder {
        @InjectView(R.id.item_text)
        TextView textView;

        public ViewItem2(View view) {
            super(view);
            ButterKnife.inject(this, itemView);
        }

        static ViewHolder create(ViewGroup viewGroup) {
            return new ViewItem2(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false));
        }

        void bind() {
            textView.setText("22222222");
        }
    }
}