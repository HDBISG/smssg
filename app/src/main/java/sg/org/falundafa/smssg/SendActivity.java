package sg.org.falundafa.smssg;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity {

    List<String> splitReceivers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        this.setActionBarLayout();
        this.splitReceivers();
        this.setListView();
    }

    private void splitReceivers() {
        String oneLineReceivers = "";
        String smsSplitChar = UtilitySharedPreference.getInstance(this).getSmsSplitChar();

        for( int i=0; i<ConstantCache.receivers.size(); i++ ) {
            String item = ConstantCache.receivers.get( i );
            oneLineReceivers += item + smsSplitChar ;

            if( (i!=0 &&(i+1)%5 == 0) || i == ConstantCache.receivers.size() -1 ) {
                splitReceivers.add( oneLineReceivers );
                oneLineReceivers = "";
            }
        }
        //合并最后一条;
        if( splitReceivers != null && splitReceivers.size() >1 ) {
            oneLineReceivers = splitReceivers.get( splitReceivers.size() -1 ); //last one
            if( oneLineReceivers.length() < 15*3 +1) {
                //少于三条；
                oneLineReceivers = splitReceivers.get( splitReceivers.size() -2 ) + oneLineReceivers;
                splitReceivers.remove( splitReceivers.size() -1 );
                splitReceivers.remove( splitReceivers.size() -1 );
                splitReceivers.add( oneLineReceivers );
            }
        }
    }

    private void setListView( ) {
        ListView listView = (ListView)this.findViewById( R.id.sendListView );
        SendAdapter stationAdapter = new SendAdapter( this, splitReceivers );
        listView.setAdapter( stationAdapter );
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
                    SendActivity.this.finish();
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
}
