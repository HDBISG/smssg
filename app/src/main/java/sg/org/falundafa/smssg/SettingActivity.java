package sg.org.falundafa.smssg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    SettingSmsPrefixDialog settingSmsPrefixDialog;
    UtilitySharedPreference utilitySharedPreference = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        utilitySharedPreference = UtilitySharedPreference.getInstance( this );

        this.setActionBarLayout();
    }


    public void setActionBarLayout( ){
        //ActionBar actionBar = getActionBar();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if( null != actionBar ){
            actionBar.setDisplayShowHomeEnabled( false );
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflator = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate( R.layout.title, null);
            android.support.v7.app.ActionBar.LayoutParams layout = new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);
            ((Toolbar) v.getParent()).setContentInsetsAbsolute(0, 0);

            ImageButton backBtn = (ImageButton)findViewById(R.id.backBtn);
            backBtn.setVisibility( View.VISIBLE );
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SettingActivity.this.finish();
                }
            });
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void openSmsPrefixDialog( View view ) {
        this.setSmsPrefixDialog();
        settingSmsPrefixDialog.show();
    }

    private void setSmsPrefixDialog() {
        settingSmsPrefixDialog = new SettingSmsPrefixDialog( this, utilitySharedPreference.getDefaultSmsPrefix() );

        settingSmsPrefixDialog.setYesOnclickListener("确定", new SettingSmsPrefixDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                String smsPrefix = settingSmsPrefixDialog.getSmsPrefix();
                if( smsPrefix == null || smsPrefix.trim().length() == 0 ) {
                    Toast.makeText(SettingActivity.this, "错误,请选择国家或输入短信前缀", Toast.LENGTH_LONG).show();
                } else {
                    settingSmsPrefixDialog.dismiss();
                    SettingActivity.this.utilitySharedPreference.setDefaultSmsPrefix( smsPrefix );
                }
            }
        });
        settingSmsPrefixDialog.setNoOnclickListener("取消", new SettingSmsPrefixDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                //Toast.makeText(MainActivity.this,"点击了--取消--按钮",Toast.LENGTH_LONG).show();
                settingSmsPrefixDialog.dismiss();
            }
        });

    }
}
