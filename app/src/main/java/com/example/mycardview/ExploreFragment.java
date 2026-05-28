package com.example.mycardview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.mycardview.R;
import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<CardModel> fullList, filteredList;
    private TextView emptyStateText;
    private String currentSearchText = "";
    private String currentCategory = "All";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        TextInputEditText searchEditText = view.findViewById(R.id.searchEditHome);
        ChipGroup categoryGroup = view.findViewById(R.id.categoryGroup);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        fullList = new ArrayList<>();
        loadExploreData();
        filteredList = new ArrayList<>(fullList);

        adapter = new CardAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Search logic
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { 
                currentSearchText = s.toString();
                applyFilters();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Chip logic
        categoryGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                currentCategory = "All";
            } else {
                int id = checkedIds.get(0);
                if (id == R.id.chipAll) currentCategory = "All";
                else if (id == R.id.chipSecurity) currentCategory = "Security";
                else if (id == R.id.chipAI) currentCategory = "AI";
            }
            applyFilters();
        });
    }

    private void applyFilters() {
        filteredList.clear();
        for (CardModel item : fullList) {
            boolean matchesSearch = item.getTitle().toLowerCase().contains(currentSearchText.toLowerCase());
            boolean matchesCategory = currentCategory.equals("All") || item.getCategory().equalsIgnoreCase(currentCategory);
            
            if (matchesSearch && matchesCategory) {
                filteredList.add(item);
            }
        }
        
        if (filteredList.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        
        adapter.notifyDataSetChanged();
    }

    private void loadExploreData() {
        fullList.add(new CardModel("Cyber Security", "Ethical hacking and defense.", R.drawable.img, "Security"));
        fullList.add(new CardModel("AI & ML", "Neural networks & Data Science.", R.drawable.img_1, "AI"));
        fullList.add(new CardModel("Cloud Computing", "AWS, Azure, and DevOps.", R.drawable.img_2, "Dev"));
        fullList.add(new CardModel("Android Dev", "Modern app development.", R.drawable.img, "Dev"));
    }
}
