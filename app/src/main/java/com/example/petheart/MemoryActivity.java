package com.example.petheart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class MemoryActivity extends SingleFragmentActivity {

    private static final String EXTRA_MEMORY_ID = "memory_id";

    @Override
    public Fragment createFragment() {
        UUID memoryId = (UUID) getIntent().getSerializableExtra(EXTRA_MEMORY_ID);

        return MemoryFragment.newInstance(memoryId);
    }

    public static Intent newIntent(Context packageContext, UUID memoryId)
    {
        Intent intent = new Intent(packageContext, MemoryActivity.class);
        intent.putExtra(EXTRA_MEMORY_ID, memoryId);
        return intent;
    }
}