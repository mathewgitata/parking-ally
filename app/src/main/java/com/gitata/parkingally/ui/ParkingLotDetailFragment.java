package com.gitata.parkingally.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gitata.parkingally.R;
import com.gitata.parkingally.models.EmelParkingLotResponse;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ParkingLotDetailFragment extends Fragment {
    @BindView(R.id.parkingLotNameTextView)
    TextView mParkingLotName;
    @BindView(R.id.updateDateTextView)
    TextView mUpdateTime;
    @BindView(R.id.allspaces)
    TextView mAllSpaces;
    @BindView(R.id.usedspaces)
    TextView mOccupiedSpaces;
    @BindView(R.id.available)
    TextView mAvailableSpaces;

    private EmelParkingLotResponse mParkingLot;

    public ParkingLotDetailFragment() {
    }

    public static ParkingLotDetailFragment newInstance(EmelParkingLotResponse parkingLot) {
        ParkingLotDetailFragment parkingLotDetailFragment = new ParkingLotDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("parkingLot", Parcels.wrap(parkingLot));
        parkingLotDetailFragment.setArguments(args);
        return parkingLotDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParkingLot = Parcels.unwrap(getArguments().getParcelable("parkingLot"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_lot_detail, container, false);
        ButterKnife.bind(this, view);

        String maxCapacity = String.valueOf(mParkingLot.getMaxCapacity());
        String occupiedLots = String.valueOf(mParkingLot.getOccupation());
        int availableLots = Integer.parseInt(maxCapacity) - Integer.parseInt(occupiedLots);

        mParkingLotName.setText(mParkingLot.getName());
        mUpdateTime.setText(mParkingLot.getOccupationDate());
        mAllSpaces.setText(maxCapacity);
        mOccupiedSpaces.setText(occupiedLots);
        mAvailableSpaces.setText(String.valueOf(availableLots));

        return view;
    }
}


