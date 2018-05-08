package com.example.jsondemo;

import android.content.Context;
import android.util.Xml;

import com.afaya.toolslib.LogUtil;
import com.afaya.toolslib.OTHERTOOLS;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created  :   GaryLiu
 * Email    :   gary@Test.com
 * Date     :   2018/4/8
 * Desc     :
 */

public class XmlUtils {
    private static final String TAG = "XmlUtils";
    private void parsePullXml(Context context){
        LogUtil.E(TAG, OTHERTOOLS.getCurrentMethod());
        StringBuilder sb = new StringBuilder();
        XmlPullParser xmlPullParser = Xml.newPullParser();

        try {
            InputStream in = context.getAssets().open("subject.xml");
            xmlPullParser.setInput(in,"UTF-8");

            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                LogUtil.E(TAG,"nodeName = " + nodeName);
//                sb.append("name = " + nodeName + "\n");
                int eventType = xmlPullParser.getEventType();
//                sb.append("id is : " + xmlPullPars)
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        if ("language".equals(nodeName)) {
                            sb.append("id is : " + xmlPullParser.getAttributeValue(0) + "\n");
                        }else if ("id".equals(nodeName)) {
                            sb.append("id is : " + xmlPullParser.nextText() + "\n");
                        }else if("name".equals(nodeName)){
                            sb.append("name is : " + xmlPullParser.nextText() + "\n");
                        }else if("usage".equals(nodeName)){
                            sb.append("usage is : " + xmlPullParser.nextText() + "\n");
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                xmlPullParser.next();

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

//        mTextView.setText(sb.toString());

//        Resources res = getResources().getXml(R.)
//        XmlResourceParser xmlResourceParser = Xml.newPullParser();

    }

}
