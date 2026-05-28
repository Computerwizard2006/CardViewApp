package com.example.mycardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import java.util.List;

import com.mycardview.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final List<CardModel> list;

    public CardAdapter(List<CardModel> list) { this.list = list; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardModel model = list.get(position);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDesc());
        holder.image.setImageResource(model.getImage());

        holder.favoriteBtn.setImageResource(model.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);

        holder.itemView.setOnClickListener(v -> showBottomSheetDetails(v, model));
        holder.button.setOnClickListener(v -> showBottomSheetDetails(v, model));

        holder.favoriteBtn.setOnClickListener(v -> {
            model.setFavorite(!model.isFavorite());
            holder.favoriteBtn.setImageResource(model.isFavorite() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
            String status = model.isFavorite() ? "Added to Favorites" : "Removed from Favorites";
            Toast.makeText(v.getContext(), status, Toast.LENGTH_SHORT).show();
        });
    }

    private void showBottomSheetDetails(View v, CardModel model) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
        View bottomSheetView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_details, null);
        
        ImageView img = bottomSheetView.findViewById(R.id.detailImage);
        TextView title = bottomSheetView.findViewById(R.id.detailTitle);
        TextView desc = bottomSheetView.findViewById(R.id.detailDesc);
        TextView category = bottomSheetView.findViewById(R.id.detailCategory);
        MaterialButton btnEnroll = bottomSheetView.findViewById(R.id.btnEnroll);

        img.setImageResource(model.getImage());
        title.setText(model.getTitle());
        desc.setText(model.getDesc());
        category.setText(model.getCategory());

        btnEnroll.setOnClickListener(view -> {
            Toast.makeText(v.getContext(), "Enrolled in " + model.getTitle(), Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView image, favoriteBtn;
        MaterialButton button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleText);
            desc = itemView.findViewById(R.id.descText);
            image = itemView.findViewById(R.id.cardImage);
            button = itemView.findViewById(R.id.btnExplore);
            favoriteBtn = itemView.findViewById(R.id.favoriteBtn);
        }
    }
}
