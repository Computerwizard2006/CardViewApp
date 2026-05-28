package com.example.mycardview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.mycardview.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<CardModel> fullList, filteredList;
    private CardAdapter adapter;
    private TextInputEditText searchEdit;
    private ChipGroup categoryGroup;
    private String selectedCategory = "All";

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        recyclerView = view.findViewById(R.id.recyclerView);
        searchEdit = view.findViewById(R.id.searchEditHome);
        categoryGroup = view.findViewById(R.id.categoryGroup);

        // 2. Data Setup
        fullList = new ArrayList<>();
        loadData();
        filteredList = new ArrayList<>(fullList);

        // 3. Adapter & Grid Layout (2 columns)
        adapter = new CardAdapter(filteredList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        // 4. Search Filter Logic
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString(), selectedCategory);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 5. Category Chip Filter Logic
        categoryGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                selectedCategory = "All";
            } else {
                Chip chip = view.findViewById(checkedIds.get(0));
                selectedCategory = chip.getText().toString();
            }
            applyFilters(searchEdit.getText().toString(), selectedCategory);
        });
    }

    private void applyFilters(String query, String category) {
        filteredList.clear();
        for (CardModel item : fullList) {
            boolean matchesSearch = item.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDesc().toLowerCase().contains(query.toLowerCase());

            boolean matchesCategory = category.equalsIgnoreCase("All") ||
                    item.getCategory().equalsIgnoreCase(category);

            if (matchesSearch && matchesCategory) {
                filteredList.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        // Sample course data with categories
        fullList.add(new CardModel("Cyber Security", "Ethical hacking and defense.", R.drawable.img, "Security"));
        fullList.add(new CardModel("AI & ML", "Neural networks & Data Science.", R.drawable.img_1, "AI"));
        fullList.add(new CardModel("Cloud Computing", "AWS, Azure, and DevOps.", R.drawable.img_2, "Cloud"));
        fullList.add(new CardModel("Android Dev", "Modern app development.", R.drawable.img, "Dev"));
        fullList.add(new CardModel("Python Pro", "Advanced Python programming.", R.drawable.img_1, "AI"));
        fullList.add(new CardModel("Web Design", "UI/UX and Frontend mastery.", R.drawable.img_2, "Dev"));
        fullList.add(new CardModel("Network Sec", "Protecting infrastructure.", R.drawable.img, "Security"));
        fullList.add(new CardModel("Data Analytics", "Uncover hidden insights.", R.drawable.img_1, "AI"));
    }
}
