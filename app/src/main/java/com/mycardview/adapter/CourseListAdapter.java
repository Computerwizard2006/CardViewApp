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

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private List<Course> courses = new ArrayList<>();
    private Context context;
    private CourseViewModel viewModel;
    private int lastPosition = -1;

    public CourseListAdapter(Context context, CourseViewModel viewModel) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courses.get(position);

        holder.titleText.setText(course.getTitle());
        holder.categoryText.setText(course.getCategory());
        holder.moduleText.setText(course.getModuleCount() + " modules");
        holder.progressBar.setProgress(course.getProgress());
        holder.progressText.setText(course.getProgress() + "% complete");

        // Load image
        int imageResId = context.getResources().getIdentifier(course.getImageUrl(), "drawable", context.getPackageName());
        Glide.with(context)
                .load(imageResId != 0 ? imageResId : R.drawable.img_1)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.courseImage);

        // Bookmark icon state
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

    @Override
    public int getItemCount() { return courses.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView titleText, categoryText, moduleText, progressText;
        ProgressBar progressBar;
        ImageButton bookmarkBtn;
        ImageView courseImage;

        ViewHolder(View view) {
            super(view);
            card = (MaterialCardView) view;
            titleText = view.findViewById(R.id.course_title);
            categoryText = view.findViewById(R.id.course_category);
            moduleText = view.findViewById(R.id.course_modules);
            progressBar = view.findViewById(R.id.course_progress);
            progressText = view.findViewById(R.id.course_progress_text);
            bookmarkBtn = view.findViewById(R.id.bookmark_btn);
            courseImage = view.findViewById(R.id.course_image);
        }
    }
}
