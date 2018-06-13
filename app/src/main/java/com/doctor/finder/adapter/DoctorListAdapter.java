package com.doctor.finder.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.doctor.finder.R;
import com.doctor.finder.model.Doctor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder> {

    private final List<Doctor> mDoctorList;

    private final Context context;

    private final DoctorAdapterOnClickHandler mClickHandler;

    public interface DoctorAdapterOnClickHandler {
        void onClick(int position);
    }


    public DoctorListAdapter(List<Doctor> doctorList, Context context, DoctorAdapterOnClickHandler clickHandler) {
        this.mDoctorList = doctorList;
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public DoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.doctor_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new DoctorViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final DoctorViewHolder holder, int position) {

        String firstName = mDoctorList.get(position).getProfile().getFirst_name();
        String lastName = mDoctorList.get(position).getProfile().getLast_name();
        String name = firstName + " " + lastName;
        holder.nameTextView.setText(name);

        if (mDoctorList.get(position).getSpecialties().size() != 0) {
            String specialty = mDoctorList.get(position).getSpecialties().get(0).getActor();
            holder.specialtyTextView.setText(specialty);
        }

        if (mDoctorList.get(position).getPractices().size() != 0) {
            String distance = String.valueOf(mDoctorList.get(position).getPractices().get(0).getDistance());
            holder.distanceTextView.setText(distance);
        }

        String profileImageUri = mDoctorList.get(position).getProfile().getImage_url();
        Picasso.with(context)
                .load(profileImageUri)
                .noFade().
                into(holder.profileImageView);


    }

    @Override
    public int getItemCount() {
        if (mDoctorList != null) {
            return mDoctorList.size();
        } else {
            return 0;
        }

    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CircleImageView profileImageView;
        final TextView nameTextView;
        final TextView specialtyTextView;
        final TextView distanceTextView;


        DoctorViewHolder(View itemView) {
            super(itemView);

            profileImageView = itemView.findViewById(R.id.iv_profile_image_card);
            nameTextView = itemView.findViewById(R.id.tv_doctor_name_card);
            specialtyTextView = itemView.findViewById(R.id.tv_doctor_specialty_card);
            distanceTextView = itemView.findViewById(R.id.tv_distance_card);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setMoviesArray() {
        mDoctorList.clear();
        notifyDataSetChanged();
    }
}
