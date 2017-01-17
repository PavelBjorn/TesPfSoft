package com.fedor.pavel.util;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.fedor.pavel.models.ColorModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class XmlParseUtil {

    public static void parseColors(InputStream inputStream, final ParseDataListener onDataListener) {
        new AsyncTask<InputStream, String, List<ColorModel>>() {

            @Override
            protected List<ColorModel> doInBackground(InputStream... inputStreams) {
                try {
                    List<ColorModel> colors = new ArrayList<ColorModel>();
                    XmlPullParser pullParser = prepareXmlPullParser(inputStreams[0]);

                    while (pullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        if (pullParser.getEventType() == XmlPullParser.START_TAG) {

                            if (pullParser.getName().equals("color")) {
                                ColorModel colorModel = new ColorModel();
                                try {
                                    for (int attrPos = 0; attrPos < pullParser.getAttributeCount(); attrPos++) {
                                        if (pullParser.getAttributeName(attrPos).equals("name")) {
                                            colorModel.setColorTitle(pullParser.getAttributeValue(attrPos));
                                        } else {
                                            colorModel.setColorRes(Color.parseColor(pullParser.getAttributeValue(attrPos)));
                                        }
                                    }
                                    colors.add(colorModel);
                                } catch (IllegalArgumentException e) {
                                    publishProgress(colorModel.getColorTitle() + " " + e.getLocalizedMessage());
                                }
                            }
                        }
                        pullParser.next();
                    }
                    return colors;
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                onDataListener.onDataParseError(values[0]);
            }

            @Override
            protected void onPostExecute(List<ColorModel> colorModels) {
                super.onPostExecute(colorModels);
                if (colorModels != null) {
                    onDataListener.onDataParse(colorModels);
                }
            }
        }.execute(inputStream);
    }

    private static XmlPullParser prepareXmlPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(inputStream, "UTF-8");
        return xpp;
    }

    public interface ParseDataListener {
        void onDataParse(List<ColorModel> colorModels);

        void onDataParseError(String errorMessage);
    }

}
