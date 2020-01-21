package com.gitata.parkingally.models;

import com.gitata.parkingally.R;

public enum NavBarItem {
    MY_ACCOUNT(R.id.tv_account_settings),
    PARKING_HISTORY(R.id.tv_parking_history),
    PAYMENT(R.id.tv_payment),
    HELP(R.id.tv_help),
    LEGAL(R.id.tv_legal);

    private int itemId;

    NavBarItem(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public static NavBarItem fromViewId(int viewId) {
        for (NavBarItem navBarItem : NavBarItem.values()) {
            if (navBarItem.itemId == viewId) {
                return navBarItem;
            }
        }
        throw new IllegalStateException("Cannot find view type");
    }
}
