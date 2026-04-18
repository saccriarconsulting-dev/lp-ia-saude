package com.axys.redeflexmobile.ui.adquirencia.ranking;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.axys.redeflexmobile.R;
import com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus;
import com.axys.redeflexmobile.shared.models.adquirencia.PagerAdapterItem;
import com.axys.redeflexmobile.shared.models.adquirencia.Ranking;
import com.axys.redeflexmobile.ui.adquirencia.ranking.list.RankingListFragment;

import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.getColor;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus.MORE_THAN_100;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus.MORE_THAN_50;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus.MORE_THAN_90;
import static com.axys.redeflexmobile.shared.enums.adquirencia.EnumRankingStatus.UNDER_THAN_50;
import static com.axys.redeflexmobile.shared.util.NumberUtils.EMPTY_INT;
import static com.axys.redeflexmobile.shared.util.NumberUtils.SINGLE_INT;

/**
 * @author Rogério Massa on 29/10/18.
 */

public class RankingPagerAdapter extends FragmentPagerAdapter implements TabLayout.BaseOnTabSelectedListener {

    private static final int POSITION_MORE_THAN_100 = 0;
    private static final int POSITION_MORE_THAN_90 = 1;
    private static final int POSITION_MORE_THAN_50 = 2;
    private static final int POSITION_UNDER_THAN_50 = 3;

    private Context context;
    private IMainTabSelectedListener tabSelectedListener;
    private List<PagerAdapterItem> items;
    private TabLayout tabLayout;
    private List<Ranking> rankingList;

    RankingPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    void initializeFragments() {
        this.items = Arrays.asList(
                new PagerAdapterItem(context.getString(R.string.ranking_tab_more_than_100),
                        RankingListFragment.newInstance(filterList(MORE_THAN_100))),
                new PagerAdapterItem(context.getString(R.string.ranking_tab_more_than_90),
                        RankingListFragment.newInstance(filterList(MORE_THAN_90))),
                new PagerAdapterItem(context.getString(R.string.ranking_tab_more_than_50),
                        RankingListFragment.newInstance(filterList(MORE_THAN_50))),
                new PagerAdapterItem(context.getString(R.string.ranking_tab_under_than_50),
                        RankingListFragment.newInstance(filterList(UNDER_THAN_50))));
    }

    void setTabSelectedListener(IMainTabSelectedListener tabAdapterListener) {
        this.tabSelectedListener = tabAdapterListener;
    }

    void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        return this.items.get(position).fragment;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.items.get(position).title;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        this.setTabIndicatorColor(tab.getPosition());
        if (tabSelectedListener != null) {
            tabSelectedListener.onTabSelected(tab);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //unused
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //unused
    }

    void setRankingList(List<Ranking> rankingList) {
        this.rankingList = rankingList;
    }

    private List<Ranking> filterList(EnumRankingStatus status) {
        return Stream.ofNullable(rankingList)
                .sortBy(ranking -> -ranking.getStatus())
                .filter(ranking -> status.equals(EnumRankingStatus.getEnumByValue(ranking.getStatus())))
                .toList();
    }

    private void setTabIndicatorColor(int position) {
        switch (position) {
            case 0:
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,
                        R.color.ranking_tab_more_than_100));
                break;
            case 1:
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,
                        R.color.ranking_tab_more_than_90));
                break;
            case 2:
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,
                        R.color.ranking_tab_more_than_50));
                break;
            case 3:
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context,
                        R.color.ranking_tab_under_than_50));
                break;
            default:
                break;
        }
    }

    void initializeColorScheme() {
        for (int tabIndex = EMPTY_INT; tabIndex < tabLayout.getTabCount(); tabIndex++) {
            LinearLayout viewGroup = (LinearLayout) tabLayout.getChildAt(EMPTY_INT);
            LinearLayout tabChildLayout = (LinearLayout) viewGroup.getChildAt(tabIndex);
            TextView tabText = (TextView) tabChildLayout.getChildAt(SINGLE_INT);
            if (tabText == null) return;
            int iconResId = 0;
            switch (tabIndex) {
                case POSITION_MORE_THAN_100:
                    iconResId = R.drawable.ic_dollar_green_wrapped;
                    tabText.setTextColor(getColor(context, R.color.ranking_tab_more_than_100));
                    break;
                case POSITION_MORE_THAN_90:
                    iconResId = R.drawable.ic_dollar_yellow_wrapped;
                    tabText.setTextColor(getColor(context, R.color.ranking_tab_more_than_90));
                    break;
                case POSITION_MORE_THAN_50:
                    iconResId = R.drawable.ic_dollar_red_wrapped;
                    tabText.setTextColor(getColor(context, R.color.ranking_tab_more_than_50));
                    break;
                case POSITION_UNDER_THAN_50:
                    iconResId = R.drawable.ic_dollar_black_wrapped;
                    tabText.setTextColor(getColor(context, R.color.ranking_tab_under_than_50));
                    break;
                default:
                    break;
            }

            TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
            if (tab != null) {
                tab.setIcon(iconResId);
            }
        }
    }

    public interface IMainTabSelectedListener {
        void onTabSelected(TabLayout.Tab tab);
    }
}
