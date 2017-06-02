package sg.org.falundafa.smssg;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {


    UtilitySharedPreference utilitySharedPreference = null;

    TextView logTextView = null;
    EditText smsReceiversView = null;
    EditText smsBodyEditView = null;
    Button sendSmsBtn = null;

    SmsBodyCountDialog smsBodyCountDialog;

    TextView msgSmsPrefixTv = null;
    String smsPrefix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.setActionBarLayout();

        this.checkStart();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        this.processInitData();
    }
    private void processInitData() {


        utilitySharedPreference = UtilitySharedPreference.getInstance( this );

        logTextView = (TextView)this.findViewById( R.id.textViewLog );
        smsReceiversView = (EditText) this.findViewById(R.id.smsReceivers);
        /*smsReceiversView.setInputType(InputType.TYPE_NULL);
        smsReceiversView.setTextIsSelectable(true);
        smsReceiversView.setCursorVisible( true );*/
        //smsReceiversView.set
        msgSmsPrefixTv = (TextView)this.findViewById( R.id.msgSmsPrefixTv );
        smsPrefix = utilitySharedPreference.getDefaultSmsPrefix();
        this.setSmsPrefixTv( smsPrefix );

        smsBodyEditView = (EditText) this.findViewById(R.id.smsBody);
        smsBodyEditView.setText(utilitySharedPreference.getDefaultSmsbody());
        //smsBodyEditView.setInputType(InputType.TYPE_NULL);


        sendSmsBtn = (Button)this.findViewById(R.id.sendSms);
        sendSmsBtn.setEnabled(false);

        smsReceiversView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                sendSmsBtn.setEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        // keyboard
        Button pasteToReceiversBtn = (Button)this.findViewById(R.id.pasteToReceivers);
        pasteToReceiversBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager)MainActivity.this
                        .getSystemService(CLIPBOARD_SERVICE);
                if (cmb.hasPrimaryClip()){
                    String receivers = cmb.getPrimaryClip().getItemAt(0).getText().toString().trim();
                    smsReceiversView.setText( receivers );
                }
            }
        });
        Button pasteToSmsBodyBtn = (Button)this.findViewById(R.id.pasteToSmsBody);
        pasteToSmsBodyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager)MainActivity.this
                        .getSystemService(CLIPBOARD_SERVICE);
                if (cmb.hasPrimaryClip()){
                    String smsBody = cmb.getPrimaryClip().getItemAt(0).getText().toString().trim();
                    smsBodyEditView.setText( smsBody );
                }

            }
        });
    }
    /**
     * 格式化接收者
     */
    public void formatReceivers( View view ) {
        EditText receiversEditView = (EditText)this.findViewById( R.id.smsReceivers );
        String receivers = receiversEditView.getText().toString().trim();
        ConstantCache.receivers = this.formatReceivers( receivers );
        receiversEditView.setText(formateToDisplay());
        String errorMsg = checkReceivers();
        if( errorMsg == null || errorMsg.trim().length() == 0 ) {
            sendSmsBtn.setEnabled(true);
            this.displayLogs("格式化成功，共有"+ConstantCache.receivers.size() + "条短信");
        } else {
            sendSmsBtn.setEnabled( false );
            this.displayLogs("格式化失败，"+ errorMsg);
        }
    }

    public void popupSmsBodyDialog(View view ) {
        this.SmsBodyCountDialog();
        smsBodyCountDialog.show();
    }

    public void navigateToSendActivity(  ) {

        ConstantCache.smsBody = smsBodyEditView.getText().toString();
        Intent intent = new Intent( this, SendActivity.class);
        this.startActivity(intent) ;
        utilitySharedPreference.setDefaultSmsbody(ConstantCache.smsBody);

        this.displayLogs( "开始发送短信");
    }

    private void displayLogs( String log ) {
        if( logTextView != null && log != null ) {
            logTextView.setText(log.trim());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText( this, "请按左键退出", Toast.LENGTH_SHORT ).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setActionBarLayout( ){
        //ActionBar actionBar = getActionBar();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if( null != actionBar ){
            actionBar.setDisplayShowHomeEnabled( false );
            actionBar.setDisplayShowCustomEnabled(true );

            LayoutInflater inflator = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate( R.layout.title, null);
            android.support.v7.app.ActionBar.LayoutParams layout
                    = new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.setMargins(0,0,0,0);
            actionBar.setCustomView(v, layout);
            ((Toolbar) v.getParent()).setContentInsetsAbsolute(0, 0);
        }
    }

    public static String[] StrToArray(String str)
    {
        String[] str_array = new String[str.length()];
        for (int cnt = 0; cnt < str.length(); cnt++)
        {
            str_array[cnt] = str.substring(cnt,1);
        }
        return str_array;
    }
    public List<String> formatReceivers( String receiversBody) {

        //Log.d("receiversBody", receiversBody);
        //1:按回车键,逗号分类；
        List<String> receiversByCtrol = new LinkedList<String>();
        String[] receiversArray = receiversBody.split("\n|,|;");
        //Log.d("receiversArray", receiversArray.length + "");
        for (String str : receiversArray) {
            if (str != null && str.trim().length() > 0) {
                receiversByCtrol.add(str);
            }
        }
        // 2 按正则表达式过滤
        List<String> receiversByPattern = new LinkedList<>();
        Pattern pattern = Pattern.compile("[+0-9]{6,}");
        for (String str : receiversArray) {
            if (str != null && str.trim().length() > 0) {
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    receiversByPattern.add(matcher.group());
                }
            }
        }
        //3 填+86
        for (int i = 0; i < receiversByPattern.size(); i++) {
            String str = receiversByPattern.get(i);

            if (str.length() >= 11) {
                str = str.substring(str.length()-11,str.length() );
                str = smsPrefix + str;
                receiversByPattern.remove(i);
                receiversByPattern.add(i, str);
            }
        }
        /*for (int i = 0; i < receiversByPattern.size(); i++) {
            String str = receiversByPattern.get(i);
            if (str.length() == 14 && str.indexOf("+86") == 0) {
                //合格的，不用处理；
                continue;
            }
            if (str.length() == 13 && str.indexOf("86") == 0) {
                str = "+" + str;
                receiversByPattern.remove(i);
                receiversByPattern.add(i, str);
                continue;
            }
            if (str.length() == 11) {
                str = "+86" + str;
                receiversByPattern.remove(i);
                receiversByPattern.add(i, str);
                continue;
            }
        }*/
        return receiversByPattern;
    }

    private String checkReceivers() {
        String errorMsg = "";
        if( ConstantCache.receivers.size() == 0 ) {
            errorMsg = "没有接收者？";
            return errorMsg;
        }
        for( int i=0;i <ConstantCache.receivers.size();i++  ) {
            String str = ConstantCache.receivers.get( i );
            if( str.length() != (11 + smsPrefix.length() ) ) {
                errorMsg += "第"+i+"条长度不正确,";
            }
            if( str.indexOf( smsPrefix ) !=0 ) {
                errorMsg += "第"+i+"条不是" + smsPrefix + "开始的,";
            }
        }
        return errorMsg;
    }

    private String formateToDisplay() {
        //生成返回结果；
        String rst = "";
        for( int i=0;i < ConstantCache.receivers.size();i ++  ) {
            String str = ConstantCache.receivers.get( i );
            rst = rst + str + "\n";
            if( i!=0 && (i+1)%5 ==0 ) {
                rst = rst + "\n"; //五个一换行；
            }
        }
        return rst;
    }

    private void checkStart() {
        /*new AlertDialog.Builder(this).setMessage("今天学法、炼功、发正念如何？")
                .setPositiveButton("已经学法、炼功、发正念", dialogClickListener)
                .setNegativeButton("稍后就学法、炼功、发正念", dialogClickListener)
                .set("还没有", dialogClickListener).show();*/
        final CharSequence myList[] = { "已经学法、炼功、发正念了 ^-^", "稍后就学法、炼功、发正念!", "还没有进行." };
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("今天修炼如何?");
        ad.setSingleChoiceItems(myList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1) {
                switch ( arg1 ) {
                    case 0: {
                        new AlertDialog.Builder(MainActivity.this).setMessage("太棒了！每天都学法、炼功、发正念！")
                                .setPositiveButton("OK", null).show();
                        break;
                    }
                    case 1: {
                        new AlertDialog.Builder(MainActivity.this).setMessage("稍后再学法、炼功、发正念也可以。别忘了哦！")
                                .setPositiveButton("OK", null).show();
                        break;
                    }
                    case 2: {
                        new AlertDialog.Builder(MainActivity.this).setMessage("没有学法、炼功、发正念，就很难利剑齐放了！\n 还是先去学法、炼功、发正念吧！")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        MainActivity.this.finish();
                                    }
                                }).show();
                        break;
                    }
                }
                dialogInterface.dismiss();
            }
        });
        ad.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        ad.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            new AlertDialog.Builder( MainActivity.this ).setMessage(getResources().getString(R.string.help_msg))
                                    .setPositiveButton("OK",null).show();
                            break;
                    }
                }
            };
            new AlertDialog.Builder(this).setMessage("软件有问题？")
                    .setPositiveButton("有点问题。", dialogClickListener)
                    .setNegativeButton("没有任何问题！完美无缺的！", dialogClickListener).show();
            return true;
        }
        if (id == R.id.action_settings) {

            //this.setSettingDialog();
            //settingSmsPrefixDialog.show();

            Intent intent = new Intent( this, SettingActivity.class);
            this.startActivity(intent) ;

            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            MainActivity.this.finish();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
            builder.setMessage("确实要退出吗?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
            return true;*/
            MainActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setSmsPrefixTv( String msgSmsPrefixTv) {
        this.msgSmsPrefixTv.setText( "短信前缀: " + msgSmsPrefixTv );
    }

    private void SmsBodyCountDialog() {
        smsBodyCountDialog = new SmsBodyCountDialog( this, 1 );

        smsBodyCountDialog.setYesOnclickListener("确定", new SmsBodyCountDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                smsBodyCountDialog.dismiss();
                navigateToSendActivity( );
            }
        });
    }

}
