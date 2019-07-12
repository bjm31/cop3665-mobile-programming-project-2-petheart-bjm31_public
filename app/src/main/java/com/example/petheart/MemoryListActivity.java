package com.example.petheart;


import androidx.fragment.app.Fragment;

public class MemoryListActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new MemoryListFragment();
    }
}