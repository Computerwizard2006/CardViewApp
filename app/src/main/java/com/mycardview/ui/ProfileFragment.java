package com.mycardview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mycardview.MainActivity;
import com.mycardview.R;
import com.mycardview.viewmodel.CourseViewModel;

public class ProfileFragment extends Fragment {

    private CourseViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        TextView totalCoursesText = view.findViewById(R.id.total_courses_text);
        TextView bookmarkCountText = view.findViewById(R.id.bookmark_count_text);
        ProgressBar overallProgressBar = view.findViewById(R.id.overall_progress_bar);
        TextView overallProgressText = view.findViewById(R.id.overall_progress_text);
        SwitchCompat darkModeSwitch = view.findViewById(R.id.dark_mode_switch);

        // Set stats
        totalCoursesText.setText(String.valueOf(viewModel.getFilteredCourses().getValue() != null
                ? viewModel.getFilteredCourses().getValue().size() : 9));

        viewModel.getBookmarks().observe(getViewLifecycleOwner(), bookmarks -> {
            bookmarkCountText.setText(String.valueOf(bookmarks.size()));
        });

        viewModel.getOverallProgress().observe(getViewLifecycleOwner(), progress -> {
            int p = progress != null ? Math.round(progress) : 0;
            overallProgressBar.setProgress(p);
            overallProgressText.setText(p + "%");
        });

        // Dark mode toggle
        boolean isDark = requireContext()
                .getSharedPreferences("settings", 0)
                .getBoolean("dark_mode", true);
        darkModeSwitch.setChecked(isDark);
        darkModeSwitch.setOnCheckedChangeListener((btn, checked) -> {
            ((MainActivity) requireActivity()).toggleDarkMode(checked);
        });

        return view;
    }
}
