package com.mycardview.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mycardview.R;
import com.mycardview.adapter.CourseGridAdapter;
import com.mycardview.viewmodel.CourseViewModel;

public class HomeFragment extends Fragment {

    private CourseViewModel viewModel;
    private CourseGridAdapter adapter;
    private ShimmerFrameLayout shimmerLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private EditText searchInput;
    private ChipGroup chipGroup;
    private String activeCategory = "All";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        recyclerView = view.findViewById(R.id.home_recycler_view);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        searchInput = view.findViewById(R.id.search_input);
        chipGroup = view.findViewById(R.id.chip_group);

        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        setupRecyclerView();
        setupSearch();
        setupChips();
        setupSwipeRefresh();
        showShimmer();

        viewModel.getFilteredCourses().observe(getViewLifecycleOwner(), courses -> {
            hideShimmer();
            adapter.setCourses(courses);
        });

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CourseGridAdapter(requireContext(), viewModel);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.filterCourses(activeCategory, s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupChips() {
        String[] categories = {"All", "Security", "AI", "Dev"};
        for (String cat : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(cat);
            chip.setCheckable(true);
            chip.setChecked(cat.equals("All"));
            chip.setOnClickListener(v -> {
                activeCategory = cat;
                viewModel.filterCourses(cat, searchInput.getText().toString());
            });
            chipGroup.addView(chip);
        }
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            showShimmer();
            new Handler().postDelayed(() -> {
                viewModel.filterCourses(activeCategory, searchInput.getText().toString());
                swipeRefresh.setRefreshing(false);
            }, 1000);
        });
    }

    private void showShimmer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        shimmerLayout.startShimmer();
        recyclerView.setVisibility(View.GONE);
    }

    private void hideShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
