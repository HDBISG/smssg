package sg.org.falundafa.smssg;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ok on 24/9/16.
 */
public class SmsBodyCountDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView smsPrefixTv;//消息提示文本

    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    String smsPrefix;
    int smsBodyLength;

    private TextView smsBodyCountTextView;//消息提示文本

    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
            yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public SmsBodyCountDialog(Context context, int smsBodyLength) {
        //super(context, R.style.MyDialog);
        super(context);
        this.setTitle("短信长度");
        this.smsBodyLength = smsBodyLength;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sms_body_count);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();

        initRadioData();
        //初始化界面数据
        initViewData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {


    //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initViewData() {

        //如果设置按钮的文字
        yes.setText( "确定" );
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        smsBodyCountTextView = (TextView) findViewById(R.id.smsBodyCountTextView);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    private void initRadioData(  ) {
        String s = "短信内容相当于三条短信内容。";
        Spannable spanText = new SpannableString( s );
        spanText.setSpan(new ForegroundColorSpan( Color.RED )
                , 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        smsBodyCountTextView.setText( spanText );

    }


}
