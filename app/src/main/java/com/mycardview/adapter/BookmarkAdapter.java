package com.mycardview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.mycardview.R;
import com.mycardview.database.BookmarkEntity;
import com.mycardview.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<BookmarkEntity> bookmarks = new ArrayList<>();
    private Context context;
    private CourseViewModel viewModel;

    public BookmarkAdapter(Context context, CourseViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setBookmarks(List<BookmarkEntity> bookmarks) {
        this.bookmarks = bookmarks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookmarkEntity bookmark = bookmarks.get(position);
        holder.titleText.setText(bookmark.title);
        holder.categoryText.setText(bookmark.category);
        holder.durationText.setText(bookmark.duration);
        holder.difficultyText.setText(bookmark.difficulty);

        holder.removeBtn.setOnClickListener(v -> {
            viewModel.toggleBookmark(new com.mycardview.model.Course(
                bookmark.courseId, bookmark.title, "", bookmark.category,
                bookmark.imageUrl, 0, bookmark.duration, bookmark.difficulty
            ));
        });
    }

    @Override
    public int getItemCount() { return bookmarks.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, categoryText, durationText, difficultyText;
        ImageButton removeBtn;

        ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.bookmark_title);
            categoryText = view.findViewById(R.id.bookmark_category);
            durationText = view.findViewById(R.id.bookmark_duration);
            difficultyText = view.findViewById(R.id.bookmark_difficulty);
            removeBtn = view.findViewById(R.id.remove_bookmark_btn);
        }
    }
}
