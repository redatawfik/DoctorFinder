package com.doctor.finder.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.doctor.finder.R;
import com.doctor.finder.database.DoctorEntry;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SavedDoctorListAdapter extends RecyclerView.Adapter<SavedDoctorListAdapter.DoctorViewHolder> {

    private List<DoctorEntry> mDoctorList;

    private final Context context;

    private final SavedDoctorAdapterOnClickHandler mClickHandler;

    public interface SavedDoctorAdapterOnClickHandler {
        void onClick(int position);
    }


    public SavedDoctorListAdapter(Context context, SavedDoctorAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    public void setDoctors(List<DoctorEntry> doctorEntries) {
        mDoctorList = doctorEntries;
        notifyDataSetChanged();
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

        String firstName = mDoctorList.get(position).getFirstName();
        String lastName = mDoctorList.get(position).getLastName();
        String name = firstName + " " + lastName;
        holder.nameTextView.setText(name);

        if (mDoctorList.get(position).getSpecialtyName() != null ||
                !mDoctorList.get(position).getSpecialtyName().equals("")) {
            String specialty = mDoctorList.get(position).getSpecialtyName();
            holder.specialtyTextView.setText(specialty);
        }

        String address = mDoctorList.get(position).getState() + ", " +
                mDoctorList.get(position).getCity() + ", " +
                mDoctorList.get(position).getStreet();
        if (!address.equals(", , ")) {
            holder.addressTextView.setText(address);
        }

        String profileImageUri = mDoctorList.get(position).getProfileImage();
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

        @BindView(R.id.iv_profile_image_card)
        CircleImageView profileImageView;
        @BindView(R.id.tv_doctor_name_card)
        TextView nameTextView;
        @BindView(R.id.tv_doctor_specialty_card)
        TextView specialtyTextView;
        @BindView(R.id.tv_doctor_address_card)
        TextView addressTextView;

        DoctorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }


}
