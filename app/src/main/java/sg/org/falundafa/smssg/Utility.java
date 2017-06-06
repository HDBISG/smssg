package sg.org.falundafa.smssg;

/**
 * Created by ok on 1/6/17.
 */
public class Utility {

    /**
     * 根据短信内容，判断长短信，需要拆分为几条普通短信；
     * 1：计算短信的真实字节长度，
     * 2：一条短信；140 bytes;
     * 3: 超过140，按134分割；长短信是134字节，6个字节是报文头。
     * @param smsBody 短信内容；
     * @return
     */
    public static int getSmsCount( String smsBody ) {
        int smsBodyLen = smsBody.getBytes().length;
        if( smsBodyLen <= 140 ) {
            return 1;
        } else {
            int count = smsBodyLen/134;
            if( smsBodyLen%134 == 0 ) {
                return count;
            } else {
                return count + 1;
            }
        }
    }

    /**
     * Can't less than 1;
     * @param progress
     * @return
     */
    public static int checkProgress( int progress ) {
        if( progress < 1 ) {
            return 1;
        } else if( progress > 100 ) {
            return 100;
        }
        return progress;
    }

}
