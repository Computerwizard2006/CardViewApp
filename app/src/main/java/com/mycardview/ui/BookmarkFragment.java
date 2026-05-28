package com.mycardview.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mycardview.R;
import com.mycardview.adapter.BookmarkAdapter;
import com.mycardview.viewmodel.CourseViewModel;

public class BookmarkFragment extends Fragment {

    private CourseViewModel viewModel;
    private BookmarkAdapter adapter;
    private TextView emptyText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.bookmark_recycler_view);
        emptyText = view.findViewById(R.id.empty_bookmark_text);

        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);

        adapter = new BookmarkAdapter(requireContext(), viewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getBookmarks().observe(getViewLifecycleOwner(), bookmarks -> {
            adapter.setBookmarks(bookmarks);
            emptyText.setVisibility(bookmarks.isEmpty() ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(bookmarks.isEmpty() ? View.GONE : View.VISIBLE);
        });

        return view;
    }
}
