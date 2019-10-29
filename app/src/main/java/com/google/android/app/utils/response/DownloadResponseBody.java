package com.google.android.app.utils.response;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;

public class DownloadResponseBody extends ResponseBody {

    private BufferedSource bufferedSource;

    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source());
        }
        return bufferedSource;
    }
}
