package com.mycardview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.card.MaterialCardView;
import com.mycardview.R;
import com.mycardview.model.Course;
import com.mycardview.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseGridAdapter extends RecyclerView.Adapter<CourseGridAdapter.ViewHolder> {

    private List<Course> courses = new ArrayList<>();
    private Context context;
    private CourseViewModel viewModel;
    private int lastPosition = -1;

    public CourseGridAdapter(Context context, CourseViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.titleText.setText(course.getTitle());
        holder.categoryText.setText(course.getCategory());
        holder.durationText.setText(course.getDuration());
        holder.difficultyText.setText(course.getDifficulty());
        holder.progressBar.setProgress(course.getProgress());
        holder.progressText.setText(course.getProgress() + "%");

        // Load image
        int imageResId = context.getResources().getIdentifier(course.getImageUrl(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(imageResId != 0 ? imageResId : R.drawable.img_2)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.courseImage);

        // Category color accent
        int color = getCategoryColor(course.getCategory());
        holder.categoryText.setTextColor(color);
        holder.card.setStrokeColor(color);

        // Bookmark state
        viewModel.checkBookmark(course.getId(), isBookmarked -> {
            if (holder.bookmarkBtn.getContext() != null) {
                holder.bookmarkBtn.post(() ->
                    holder.bookmarkBtn.setImageResource(
                        isBookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline
                    )
                );
            }
        });

        holder.bookmarkBtn.setOnClickListener(v -> {
            viewModel.toggleBookmark(course);
            notifyItemChanged(position);
        });

        holder.card.setOnClickListener(v -> {
            CourseDetailBottomSheet sheet = CourseDetailBottomSheet.newInstance(course, viewModel);
            sheet.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "detail");
        });

        // Fall-down animation
        if (position > lastPosition) {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.fall_down);
            holder.itemView.startAnimation(anim);
            lastPosition = position;
        }
    }

    private int getCategoryColor(String category) {
        switch (category) {
            case "Security": return context.getColor(R.color.color_security);
            case "AI": return context.getColor(R.color.color_ai);
            case "Dev": return context.getColor(R.color.color_dev);
            default: return context.getColor(R.color.color_default);
        }
    }

    @Override
    public int getItemCount() { return courses.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView titleText, categoryText, durationText, difficultyText, progressText;
        ProgressBar progressBar;
        ImageButton bookmarkBtn;
        ImageView courseImage;

        ViewHolder(View view) {
            super(view);
            card = (MaterialCardView) view;
            titleText = view.findViewById(R.id.course_title);
            categoryText = view.findViewById(R.id.course_category);
            durationText = view.findViewById(R.id.course_duration);
            difficultyText = view.findViewById(R.id.course_difficulty);
            progressBar = view.findViewById(R.id.course_progress);
            progressText = view.findViewById(R.id.course_progress_text);
            bookmarkBtn = view.findViewById(R.id.bookmark_btn);
            courseImage = view.findViewById(R.id.course_image);
        }
    }
}
