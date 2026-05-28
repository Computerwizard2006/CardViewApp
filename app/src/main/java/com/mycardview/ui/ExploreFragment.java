package com.mycardview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mycardview.R;
import com.mycardview.adapter.CourseListAdapter;
import com.mycardview.viewmodel.CourseViewModel;

public class ExploreFragment extends Fragment {

    private CourseViewModel viewModel;
    private CourseListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        adapter = new CourseListAdapter(requireContext(), viewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getFilteredCourses().observe(getViewLifecycleOwner(), courses -> {
            adapter.setCourses(courses);
        });

        return view;
    }
}