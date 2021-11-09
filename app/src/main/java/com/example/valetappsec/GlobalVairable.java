package com.example.valetappsec;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ListView;
import android.widget.TextView;


import com.example.valetappsec.Model.CaptainClientTransfer;
import com.example.valetappsec.Model.ClientOrder;
import com.example.valetappsec.Model.SingUpClientModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class GlobalVairable {


    public static  String  appActivate="0";
    public  static Context context;
    public static ListView gridViewGlobal;
    public static SingUpClientModel singUpUserTableGlobal=new SingUpClientModel();

            public  static  String captainId="";
    public static  List<ClientOrder> clientOrders=new ArrayList<>();

    public  static CaptainClientTransfer captainClientTransfer=new CaptainClientTransfer();

public  static boolean isOk=true;
public static  String arriveTime="";
    public static  String ids="0";
    public static  TextView globalText=null;
    public GlobalVairable(Context context) {

        this.context=context;
//        gridViewGlobal=new GridView(R.id.listItemGrid);
    }


    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

   //static MainValetActivity mainValetActivity=new MainValetActivity();

   // public static ItemListAdapter itemListAdapterGlobal= new ItemListAdapter(mainValetActivity, drivenList); ;


}
