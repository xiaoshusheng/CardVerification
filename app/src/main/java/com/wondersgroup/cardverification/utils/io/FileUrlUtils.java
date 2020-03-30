package com.wondersgroup.cardverification.utils.io;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 作者：create by YangZ on 2017/12/14 10:15
 * 邮箱：YangZL8023@163.com
 */

public class FileUrlUtils {
    public static Uri getFileUri(Context context, Uri uri){
        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            Log.d("00000000000000", "path1 is " + path);
            if (path != null) {
                path = Uri.decode(path);
                Log.d("00000000000000", "path2 is " + path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + path + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur
                        .moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    //do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    Log.d("00000000000000", "uri_temp is " + uri_temp);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
}
