package com.gitata.parkingally.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.ui.ParkingLotDetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingLotListAdapter extends RecyclerView.Adapter<ParkingLotListAdapter.ParkingLotViewHolder> {
    private List<EmelParkingLotResponse> mParkingLots;
    private Context mContext;


    public ParkingLotListAdapter(Context context, List<EmelParkingLotResponse> parkingLots) {
        mContext = context;
        mParkingLots = parkingLots;
    }

    @Override
    public ParkingLotListAdapter.ParkingLotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car_park, parent, false);
        ParkingLotViewHolder viewHolder = new ParkingLotViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ParkingLotListAdapter.ParkingLotViewHolder holder, int position) {
        holder.bindParkingLot(mParkingLots.get(position));
    }

    @Override
    public int getItemCount() {
        return mParkingLots.size();
    }

    public class ParkingLotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_parking_lot_name)
        TextView parkingLotName;
        @BindView(R.id.tv_carpark_id)
        TextView parkingLotCapacity;

        public ParkingLotViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mContext = itemView.getContext();
        }

        public void bindParkingLot(EmelParkingLotResponse parkingLot) {
            parkingLotName.setText(parkingLot.getName());
            parkingLotCapacity.setText(parkingLot.getParkId());
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, ParkingLotDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("parkingLots", Parcels.wrap(mParkingLots));
            mContext.startActivity(intent);
        }
    }
}
