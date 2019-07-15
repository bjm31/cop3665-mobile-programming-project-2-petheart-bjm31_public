package com.example.petheart;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petheart.Models.Memory;
import com.example.petheart.Models.MemoryList;

import java.util.List;

public class MemoryListFragment extends Fragment {

    private class MemoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Memory mMemory;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mMemoryFavorite;
        private TextView mMemoryDescription;

        public MemoryHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_memory, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.memory_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.memory_date);
            mMemoryFavorite = (ImageView) itemView.findViewById(R.id.memory_favorite);
            mMemoryDescription = (TextView) itemView.findViewById(R.id.memory_description);
        }

        public void bind(Memory memory) {

            mMemory = memory;
            mTitleTextView.setText(mMemory.getTitle());
            mDateTextView.setText(mMemory.getDate().toString());
            mMemoryFavorite.setVisibility(mMemory.isFavorite() ? View.VISIBLE : View.INVISIBLE);
            mMemoryDescription.setText(mMemory.getDescription());
        }

        @Override
        public void onClick(View view) {
            Intent intent = MemoryPagerActivity.newIntent(getActivity(), mMemory.getId());
            startActivity(intent);
        }
    }

    private class MemoryAdapter extends RecyclerView.Adapter<MemoryHolder> {

        private List<Memory> mMemories;

        public MemoryAdapter(List<Memory> memories) {
            mMemories = memories;
        }

        @NonNull
        @Override
        public MemoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            Log.d("adapter", "Creating");
            return new MemoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MemoryHolder memoryHolder, int position) {
            Memory memory = mMemories.get(position);
            Log.d("adapter", "Binding");
            memoryHolder.bind(memory);
        }

        public void setMemories(List<Memory> memories){
            mMemories = memories;
        }

        @Override
        public int getItemCount() {
            return mMemories.size();
        }
    }

    private RecyclerView mMemoryRecyclerView;
    private MemoryAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_memory_list, container, false);
        mMemoryRecyclerView = (RecyclerView) v.findViewById(R.id.memory_recycler_view);
        mMemoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_memory_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.new_memory:
                Memory memory = new Memory();
                MemoryList.get(getActivity()).addMemory(memory);
                Intent intent = MemoryPagerActivity.newIntent(getActivity(), memory.getId());
                startActivity(intent);
                return true;
            case R.id.favorites_memories:
                return true;
            case R.id.default_memories:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {

        MemoryList memoryList = MemoryList.get(getActivity());
        List<Memory> memories = memoryList.getMemories();

        if(mAdapter == null) {
            mAdapter = new MemoryAdapter(memories);
            mMemoryRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setMemories(memories);
            mAdapter.notifyDataSetChanged();
        }
    }
}