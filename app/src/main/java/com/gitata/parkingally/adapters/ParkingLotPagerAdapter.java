package com.gitata.parkingally.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gitata.parkingally.models.EmelParkingLotResponse;
import com.gitata.parkingally.ui.ParkingLotDetailFragment;

import java.util.List;

public class ParkingLotPagerAdapter extends FragmentPagerAdapter {
    private List<EmelParkingLotResponse> mParkingLots;

    public ParkingLotPagerAdapter(FragmentManager fm, int behavior, List<EmelParkingLotResponse> parkingLots) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        return ParkingLotDetailFragment.newInstance(mParkingLots.get(position));
    }

    @Override
    public int getCount() {
        return mParkingLots.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mParkingLots.get(position).getName();
    }
}
