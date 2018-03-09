package kr.clude.yearnning.smartdorm;

/**
 * Created by yearnning on 15. 1. 22..
 */
public class NavigationDrawerItem {

    public int mIconResource;
    public String mTitle;

    public static NavigationDrawerItem newInstance(int mIconResource, String mTitle) {
        NavigationDrawerItem navigationDrawerItem = new NavigationDrawerItem();
        navigationDrawerItem.mIconResource = mIconResource;
        navigationDrawerItem.mTitle = mTitle;
        return navigationDrawerItem;
    }
}
