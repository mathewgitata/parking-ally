package com.gitata.parkingally.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.gitata.parkingally.R;
import com.gitata.parkingally.adapters.ParkingLotPagerAdapter;
import com.gitata.parkingally.models.EmelParkingLotResponse;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingLotDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private ParkingLotPagerAdapter adapterViewPager;
    List<EmelParkingLotResponse> mParkingLots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot_detail);
        ButterKnife.bind(this);

        mParkingLots = Parcels.unwrap(getIntent().getParcelableExtra("parkingLots"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new ParkingLotPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mParkingLots);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}
