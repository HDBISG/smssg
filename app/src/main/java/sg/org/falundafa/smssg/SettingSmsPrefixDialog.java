package sg.org.falundafa.smssg;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by ok on 24/9/16.
 */
public class SettingSmsPrefixDialog extends Dialog {

    private Button yes;//确定按钮
    private Button no;//取消按钮
    private TextView smsPrefixTv;//消息提示文本

    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    RadioGroup radiogroup;
    RadioButton radio1,radio2,radio3;
    //int countryId;
    String smsPrefix;

    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
            noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

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

    public SettingSmsPrefixDialog(Context context, String smsPrefix) {
        //super(context, R.style.MyDialog);
        super(context);
        this.setTitle("选择国家(短信前缀)");
        this.smsPrefix = smsPrefix;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_sms_prefix);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();

        initRadioData();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            //countryId = checkedId;

            if(checkedId==radio3.getId())
            {
                smsPrefixTv.setEnabled( true );
            }else
            {
                smsPrefixTv.setEnabled( false );
            }
            }
        });

    //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

        //如果设置按钮的文字
        yes.setText( "确定" );
        no.setText( "取消" );
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        smsPrefixTv = (TextView) findViewById(R.id.sms_prefix);

        radiogroup=(RadioGroup)findViewById(R.id.lactate_radio);
        radio1 = (RadioButton)findViewById(R.id.locate_sg);
        radio2 = (RadioButton)findViewById(R.id.locate_usa);
        radio3 = (RadioButton)findViewById(R.id.locate_other);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick();
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

    private void initRadioData(  ) {

        radio1.setChecked( false );
        radio2.setChecked( false );
        radio3.setChecked( false );

        smsPrefixTv.setEnabled( false );

        if( "+86".equalsIgnoreCase( smsPrefix )) {
            radio1.setChecked( true );
        } else if( "01186".equalsIgnoreCase( smsPrefix )) {
            radio2.setChecked( true );
        } else  {
            radio3.setChecked( true );
            smsPrefixTv.setEnabled( true );
            smsPrefixTv.setText( smsPrefix );
        }
    }

    public String getSmsPrefix() {
        if( radio1.isChecked() ) {
            return "+86";
        } else if( radio2.isChecked() ) {
            return  "01186";
        } else  {
            return smsPrefixTv.getText().toString();
        }
    }

}
