package kr.hmit.base.base_view_pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // 페이지 최대 갯수
    private int m_nMaxPage = 1;

    // 현재 프래그먼트
    private Fragment mCurrentFragment = new Fragment();

    private List<Fragment> m_fragment;

    /**
     * 2016. 2. 4.[오후 2:48:22]_손정현</br>
     * ineegapp.main.ui.MainPagerAdapter.java </br>
     * 생성자</br>
     */
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragment) {
        super(fm);
        this.m_fragment = fragment;
        this.m_nMaxPage = fragment.size();
    }

    public void updateFragment(List<Fragment> fragment) {
        this.m_fragment = fragment;
        this.m_nMaxPage = fragment.size();
    }

    @Override
    public Fragment getItem(int position) {

        try {
            if (position < 0 || m_nMaxPage <= position)
                return null;

            mCurrentFragment = m_fragment.get(position);

        } catch (Exception e) {

        }

        return mCurrentFragment;

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return m_nMaxPage;
    }
}
