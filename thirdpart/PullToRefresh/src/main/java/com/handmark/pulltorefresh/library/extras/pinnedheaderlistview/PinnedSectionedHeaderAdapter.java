package com.handmark.pulltorefresh.library.extras.pinnedheaderlistview;

import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by albert on 15/12/30.
 */

public interface PinnedSectionedHeaderAdapter {
    boolean isSectionHeader(int position);
    int getSectionForPosition(int position);
    View getSectionHeaderView(int section, View convertView, ViewGroup parent);
    int getSectionHeaderViewType(int section);
    int getCount();
}
