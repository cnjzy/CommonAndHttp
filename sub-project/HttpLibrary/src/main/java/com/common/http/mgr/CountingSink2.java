package com.common.http.mgr;

import java.io.IOException;

import okio.Buffer;
import okio.ForwardingSink;
import okio.Sink;

/**
 * @author jzy
 * created at 2018/7/11
 */
public class CountingSink2 extends ForwardingSink {
    long successfulCount;

    CountingSink2(Sink delegate) {
        super(delegate);
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        super.write(source, byteCount);
        successfulCount += byteCount;
    }
}
