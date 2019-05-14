import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdaptor extends FragmentStatePagerAdapter {

    int tabCount;
    Context context;

    public PagerAdaptor(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TabVersionName();
            case 1:
                return new TabVersionCode();
            case 2:
                return new TabVersionRelease();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
