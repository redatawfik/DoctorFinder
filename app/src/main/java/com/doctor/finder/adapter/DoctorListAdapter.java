package com.doctor.finder.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.doctor.finder.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.doctor.finder.model.models.PreDoctor;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder> {

    private final List<PreDoctor> mDoctorList;

    private final Context context;

    private final DoctorAdapterOnClickHandler mClickHandler;

    private NumberFormat formatter;

    public interface DoctorAdapterOnClickHandler {
        void onClick(int position);
    }


    public DoctorListAdapter(List<PreDoctor> doctorList, Context context, DoctorAdapterOnClickHandler clickHandler) {
        this.mDoctorList = doctorList;
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.doctor_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        formatter = new DecimalFormat("#0.00");

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new DoctorViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {

        String firstName = mDoctorList.get(position).getProfile().getFirst_name();
        String lastName = mDoctorList.get(position).getProfile().getLast_name();
        String name = firstName + " " + lastName;
        holder.nameTextView.setText(name);

        if (mDoctorList.get(position).getSpecialties().size() != 0) {
            String specialty = mDoctorList.get(position).getSpecialties().get(0).getActor();
            holder.specialtyTextView.setText(specialty);
        }

        if (mDoctorList.get(position).getPractices().size() != 0) {
            double distance = Double.parseDouble(String.valueOf(mDoctorList.get(position).getPractices().get(0).getDistance()));
            holder.distanceTextView.setText(String.format("%s%s", formatter.format(distance), context.getString(R.string.miles)));


            String address = mDoctorList.get(position).getPractices().get(0).getVisitAddress().getStateLong() + ", " +
                    mDoctorList.get(position).getPractices().get(0).getVisitAddress().getCity() + ", " +
                    mDoctorList.get(position).getPractices().get(0).getVisitAddress().getStreet();

            holder.addressTextView.setText(address);

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

        @BindView(R.id.iv_profile_image_card)
        CircleImageView profileImageView;
        @BindView(R.id.tv_doctor_name_card)
        TextView nameTextView;
        @BindView(R.id.tv_doctor_specialty_card)
        TextView specialtyTextView;
        @BindView(R.id.tv_distance_card)
        TextView distanceTextView;
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

    public void clearData() {
        mDoctorList.clear();
        notifyDataSetChanged();
    }
}
