package kr.clude.yearnning.smartdorm;

import java.util.ArrayList;

/**
 * Created by yearnning on 15. 2. 8..
 */
public class MainListApi {

    public ArrayList<MainListItem> mainListItems = new ArrayList<>();

    public MainListApi() {

        int i = (int) Math.floor(Math.random() * 5);
        if (i == 0) {
            mainListItems.add(MainListItem.newInstance(7));
        } else if (i == 1) {
            mainListItems.add(MainListItem.newInstance(6));
        } else {
            mainListItems.add(MainListItem.newInstance(5));
        }

        mainListItems.add(MainListItem.newInstance(2));
        mainListItems.add(MainListItem.newInstance(1));
        mainListItems.add(MainListItem.newInstance(3));
        mainListItems.add(MainListItem.newInstance(4));
    }

    public ArrayList<MainListItem> getResult() {
        return mainListItems;
    }
}
