package kr.clude.yearnning.smartdorm;

import java.util.Random;

/**
 * Created by yearnning on 15. 2. 7..
 */
public class MainListItem {

    public static MainListItem newInstance(int type) {
        MainListItem mainListItem = new MainListItem();
        mainListItem.type = type;
        switch (type) {
            case 1:
                mainListItem.title = "건조기";
                mainListItem.total_cnt = 2;
                mainListItem.code = 1;
                break;
            case 2:
                mainListItem.title = "세탁기";
                mainListItem.total_cnt = 2;
                mainListItem.code = 1;
                break;
            case 3:
                mainListItem.title = "샤워실";
                mainListItem.total_cnt = 8;
                mainListItem.code = 1;
                break;
            case 4:
                mainListItem.title = "화장실 비데";
                mainListItem.total_cnt = 4;
                mainListItem.code = 1;
                break;
            case 5:
                mainListItem.code = 2;

                int sort = (int) Math.floor(Math.random() * 3);
                if (sort == 0) {
                    mainListItem.message = "룸메이트가 방에 있습니다.\n커피 한 잔 사가시는 건 어떤가요?";
                    mainListItem.icon_resource_id = R.drawable.card_coffee;
                } else if (sort == 1) {
                    mainListItem.message = "룸메이트가 방에 있습니다.\n맥주 한 캔 사가시는 건 어떤가요?";
                    mainListItem.icon_resource_id = R.drawable.card_beer;
                } else {
                    mainListItem.message = "룸메이트가 방에 있습니다.\n같이 식사할까 물어보시는 건 어떤가요?";
                    mainListItem.icon_resource_id = R.drawable.card_meal;
                }

                break;
            case 6:
                mainListItem.message = "전등을 켜 둔 상태로 나오셨습니다.\n전등을 꺼주시는 건 어떨까요?";
                mainListItem.icon_resource_id = R.drawable.card_light;
                mainListItem.code = 2;
                break;
            case 7:
                mainListItem.message = "에어컨을 켜 둔 상태로 나오셨습니다.\n에어컨을 꺼주시는 건 어떨까요?";
                mainListItem.icon_resource_id = R.drawable.card_airconditioner;
                mainListItem.code = 2;
                break;
            default:
                mainListItem.title = "Unknown";
                break;
        }

        Random random = new Random();

        if (Math.random() < 0.25f) {
            mainListItem.empty_cnt = 0;
        } else {
            mainListItem.empty_cnt = (int) Math.round(Math.random() * mainListItem.total_cnt);
        }


        mainListItem.occupied_cnt = mainListItem.total_cnt - mainListItem.empty_cnt;

        if (Math.random() < 0.5f) {
            mainListItem.when = (int) Math.round(Math.random() * 6f * 60f * 60f * random.nextGaussian());
            if (mainListItem.when < 0) {
                mainListItem.when *= -1;
            }
        } else {
            mainListItem.when = (int) Math.round(Math.random() * 10f * random.nextGaussian());
            if (mainListItem.when < 0) {
                mainListItem.when *= -1;
            }
        }
        mainListItem.recommend = (int) Math.round(Math.random() * 60f * 24f);

        /**
         *
         */
        mainListItem.updateStrs();
        return mainListItem;
    }

    public int type = -1;
    public int code = -1;

    public String message = null;
    public int icon_resource_id = -1;

    public String title = null;
    public String when_str = null;
    public String recommend_str = null;

    public int total_cnt = -1;
    public int empty_cnt = -1;
    public int occupied_cnt = -1;
    public int when = -1;
    public int recommend = -1;


    public boolean isAlarmed = false;

    private void updateStrs() {
        if (empty_cnt == 0) {
            int when_hour = (int) (when / 3600);
            int when_minute = (int) ((when / 60) % 60);
            int when_second = (int) (when % 60) + 1;

            String when_str = "";
            if (when_hour != 0) {
                when_str += String.format("%d", when_hour) + "시간 ";
            }
            if (when_minute != 0) {
                when_str += String.format("%d", when_minute) + "분 ";
            }
            if (when_second != 0 && (when_hour == 0 && when_minute == 0)) {
                when_str += String.format("%d", when_second) + "초 ";
            }

            when_str += "후에 빌 것으로 예상됩니다";
            this.when_str = when_str;
        } else {
            when_str = "현재 비어있습니다";
        }

        recommend_str = String.format("%02d", (int) (recommend / 60)) + ":" + String.format("%02d", (int) (recommend % 60)) + "쯤에 이용하시면 보통 좋습니다";
    }
}
