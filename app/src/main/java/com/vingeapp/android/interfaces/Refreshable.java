package com.vingeapp.android.interfaces;

/**
 * Created by deepankur on 2/7/17.
 */

public interface Refreshable {
    /**
     * @return true if the view refreshed itself false if it failed to do so
     */
    boolean doRefresh();
}
