package com.gitata.parkingally.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.EmelParkingLotResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotAdapter.ParkingLotViewHolder> {
    private List<EmelParkingLotResponse> mParkingLots;
    private Context mContext;


    public ParkingLotAdapter(Context context, List<EmelParkingLotResponse> parkingLots) {
        mContext = context;
        mParkingLots = parkingLots;
    }

    @Override
    public ParkingLotAdapter.ParkingLotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_park, parent, false);
        ParkingLotViewHolder viewHolder = new ParkingLotViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParkingLotAdapter.ParkingLotViewHolder holder, int position) {
        holder.bindParkingLot(mParkingLots.get(position));
    }

    @Override
    public int getItemCount() {
        return mParkingLots.size();
    }

    public class ParkingLotViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_parking_lot_name)
        TextView parkingLotName;
        @BindView(R.id.tv_capacity)
        TextView parkingLotCapacity;

        private Context mContext;

        public ParkingLotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindParkingLot(EmelParkingLotResponse parkingLot) {
            parkingLotName.setText(parkingLot.getName());
            parkingLotCapacity.setText(parkingLot.getParkId());
        }

    }
}
