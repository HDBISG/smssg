package sg.org.falundafa.smssg;

import android.app.Application;

/**
 * Created by ok on 3/6/17.
 */
public class MyAPP extends Application {
    // 共享变量
    private MainActivity.MainActiviityHandler handler = null;

    // set方法
    public void setHandler(MainActivity.MainActiviityHandler handler) {
        this.handler = handler;
    }

    // get方法
    public MainActivity.MainActiviityHandler getHandler() {
        return handler;
    }
}