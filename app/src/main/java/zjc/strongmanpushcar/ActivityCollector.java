package zjc.strongmanpushcar;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    public  static List<Activity> activities = new ArrayList<Activity>();

    //添加一个活动
    public  static void addActivity(Activity activity){
        activities.add(activity);
    }
    //删除一个活动
    public static  void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //结束所有活动
    public static  void finishAll(){
        for (Activity a:activities){
            if (!a.isFinishing()){
                a.finish();
            }
        }
    }
}
