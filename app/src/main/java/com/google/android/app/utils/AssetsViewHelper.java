package com.google.android.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AssetsViewHelper {
    private static Context mcontext;
    private static AssetsViewHelper assetsViewHelper;
    /**
     * assets 目录前缀
     */
    private static String assetsFile = "assets/";

    private AssetsViewHelper() {
    }

    public static AssetsViewHelper width(Context context) {
        mcontext = context.getApplicationContext();
        if (assetsViewHelper == null) {
            synchronized (AssetsViewHelper.class) {
                if (assetsViewHelper == null) {
                    assetsViewHelper = new AssetsViewHelper();
                }
            }
        }
        return assetsViewHelper;
    }

    /**
     * 获取layout方法
     *
     * @return
     */
    public View getAssetsLayout() {
        AssetManager am = mcontext.getResources().getAssets();
        try {
            XmlResourceParser parser = am.openXmlResourceParser(assetsFile + "language.xml");
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
            View inflate = inflater.inflate(parser, null);
            return inflate;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTempValue() {
        try {
            //传入文件名：language.xml；用来获取流
            InputStream is = mcontext.getAssets().open("language.xml");
            //首先创造：DocumentBuilderFactory对象
            DocumentBuilderFactory dBuilderFactory = DocumentBuilderFactory.newInstance();
            //获取：DocumentBuilder对象
            DocumentBuilder dBuilder = dBuilderFactory.newDocumentBuilder();
            //将数据源转换成：document对象
            Document document = dBuilder.parse(is);
            //获取根元素
            Element element = document.getDocumentElement();
            if (element != null) {
                String userId = element.getAttribute("userId");
                if (!TextUtils.isEmpty(userId)) {
                    return userId;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return "";
    }



    /**
     * 获取渠道号
     *
     * @param context
     * @return
     */
    public static String getChannel(Context context) {
        String channel="";
        if (context == null)
            return "";
        if (!TextUtils.isEmpty(channel)) {
            return channel;
        }
        try {
            ZipFile zipFile = new ZipFile(context.getApplicationInfo().sourceDir);
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                //META-INF/channel_这个标识需要跟你打包时候注入渠道号时的一致
                if (entryName.contains("META-INF/channel_")) {
                    channel = entryName.replace("META-INF/channel_", "");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(channel)){
            channel = "";
        }
        return channel;
    }
}
