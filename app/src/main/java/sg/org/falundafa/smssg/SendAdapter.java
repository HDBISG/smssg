package sg.org.falundafa.smssg;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SendAdapter extends ArrayAdapter {

    private final static String TAG = "SendAdapter";
    List<String> receiverList = new ArrayList<String>();
    Context context = null;
    public SendAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    public SendAdapter(Context context, List<String> receiverList ) {
        super(context, 0);
        this.receiverList = receiverList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return receiverList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_send_item, null);
            }
            final TextView smsReceiversView = (TextView)convertView.findViewById( R.id.smsReceiversView );
            final Button sendBtn =(Button)convertView.findViewById( R.id.sendBtn );
            final Button sendAgainBtn =(Button)convertView.findViewById( R.id.sendAgainBtn );

            //短信接收者
            String receiver = receiverList.get(position);
            smsReceiversView.setText( receiver );
             //发送button;
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendBtn.setEnabled( false );
                    sendAgainBtn.setEnabled(true);

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra("address", smsReceiversView.getText() );
                    sendIntent.putExtra("sms_body", ConstantCache.smsBody);
                    //sendIntent.setType("vnd.android-dir/mms-sms");
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
            //重发
            sendAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendBtn.setEnabled( true );
                    sendAgainBtn.setEnabled( false );
                }
            });
        }catch ( Exception e ) {
            e.printStackTrace();
        }
        return convertView;
    }

}