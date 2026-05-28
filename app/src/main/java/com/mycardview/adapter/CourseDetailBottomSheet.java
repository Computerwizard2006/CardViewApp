package com.mycardview.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mycardview.R;
import com.mycardview.model.Course;
import com.mycardview.viewmodel.CourseViewModel;

public class CourseDetailBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_CATEGORY = "category";
    private static final String ARG_DURATION = "duration";
    private static final String ARG_MODULES = "modules";
    private static final String ARG_DIFFICULTY = "difficulty";
    private static final String ARG_PROGRESS = "progress";

    private CourseViewModel viewModel;

    public static CourseDetailBottomSheet newInstance(Course course, CourseViewModel viewModel) {
        CourseDetailBottomSheet sheet = new CourseDetailBottomSheet();
        sheet.viewModel = viewModel;
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, course.getId());
        args.putString(ARG_TITLE, course.getTitle());
        args.putString(ARG_DESC, course.getDescription());
        args.putString(ARG_CATEGORY, course.getCategory());
        args.putString(ARG_DURATION, course.getDuration());
        args.putInt(ARG_MODULES, course.getModuleCount());
        args.putString(ARG_DIFFICULTY, course.getDifficulty());
        args.putInt(ARG_PROGRESS, course.getProgress());
        sheet.setArguments(args);
        return sheet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_course_detail, container, false);

        Bundle args = getArguments();
        int courseId = args.getInt(ARG_COURSE_ID);
        String title = args.getString(ARG_TITLE);
        int progress = args.getInt(ARG_PROGRESS);
        int totalModules = args.getInt(ARG_MODULES);

        ((TextView) view.findViewById(R.id.detail_title)).setText(title);
        ((TextView) view.findViewById(R.id.detail_description)).setText(args.getString(ARG_DESC));
        ((TextView) view.findViewById(R.id.detail_category)).setText(args.getString(ARG_CATEGORY));
        ((TextView) view.findViewById(R.id.detail_duration)).setText(args.getString(ARG_DURATION));
        ((TextView) view.findViewById(R.id.detail_modules)).setText(totalModules + " modules");
        ((TextView) view.findViewById(R.id.detail_difficulty)).setText(args.getString(ARG_DIFFICULTY));

        ProgressBar progressBar = view.findViewById(R.id.detail_progress_bar);
        TextView progressText = view.findViewById(R.id.detail_progress_text);
        progressBar.setProgress(progress);
        progressText.setText(progress + "% completed");

        Button enrollBtn = view.findViewById(R.id.enroll_btn);
        enrollBtn.setOnClickListener(v -> {
            // Simulate progress update — 10% per enroll click
            int newProgress = Math.min(progress + 10, 100);
            int completedModules = (int) (totalModules * (newProgress / 100.0));
            viewModel.updateProgress(courseId, newProgress, completedModules);
            Toast.makeText(requireContext(),
                    newProgress == 100 ? "Course completed! 🎉" : "Progress updated: " + newProgress + "%",
                    Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }
}
