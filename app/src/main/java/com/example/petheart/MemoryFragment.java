package com.example.petheart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.petheart.Models.Memory;
import com.example.petheart.Models.MemoryList;

import java.util.Date;
import java.util.UUID;


public class MemoryFragment extends Fragment {

    private static final String ARG_MEMORY_ID = "memory_id";
    private static final String DIALOG_DATE = "dialog_date";
    private static final int REQUEST_DATE = 0;

    private Memory mMemory;
    private EditText mTitleField;
    private Button mDateButton;
    private Switch mFavoriteSwitch;
    private EditText mDescriptionField;

    public static MemoryFragment newInstance(UUID memoryID)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEMORY_ID, memoryID);

        MemoryFragment fragment = new MemoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID memoryId = (UUID) getArguments().getSerializable(ARG_MEMORY_ID);
        mMemory = MemoryList.get(getActivity()).getMemory(memoryId);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Turn into ScrollView
        View v = inflater.inflate(R.layout.fragment_memory, container, false);

        mTitleField = v.findViewById(R.id.memory_title);
        mTitleField.setText(mMemory.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMemory.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        mDateButton = v.findViewById(R.id.memory_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMemory.getDate());
                dialog.setTargetFragment(MemoryFragment.this, REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });

        mFavoriteSwitch = v.findViewById(R.id.memory_favorite);
        mFavoriteSwitch.setChecked(mMemory.isFavorite());
        mFavoriteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mMemory.setFavorite(b);
            }
        });

        mDescriptionField = v.findViewById(R.id.memory_description);
        mDescriptionField.setText(mMemory.getTitle());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMemory.setDescription(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_memory, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_memory:
                MemoryList.get(getActivity()).deleteMemory(mMemory);
                getActivity().finish();
                return true;

            case R.id.send_memory:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getMemoryDetails());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.memory_details_subject));
                startActivity(i);
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMemory.setDate(date);
            updateDate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        MemoryList.get(getActivity()).updateMemory(mMemory);
    }

    private void updateDate() {
        mDateButton.setText(mMemory.getDate().toString());
    }

    private String getMemoryDetails() {

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mMemory.getDate()).toString();

        String details = getString(R.string.memory_details, mMemory.getTitle(), dateString, mMemory.getDescription());

        return details;
    }
}