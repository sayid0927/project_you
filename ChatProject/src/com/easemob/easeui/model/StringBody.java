//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.easemob.easeui.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import internal.org.apache.http.entity.mime.content.AbstractContentBody;

public class StringBody extends AbstractContentBody {
    private final byte[] content;
    private final Charset charset;

    public static StringBody create(String text, String mimeType, Charset charset) throws IllegalArgumentException {
        try {
            return new StringBody(text, mimeType, charset);
        } catch (UnsupportedEncodingException var4) {
            throw new IllegalArgumentException("Charset " + charset + " is not supported", var4);
        }
    }

    public static StringBody create(String text, Charset charset) throws IllegalArgumentException {
        return create(text, (String)null, charset);
    }

    public static StringBody create(String text) throws IllegalArgumentException {
        return create(text, (String)null, (Charset)null);
    }

    public StringBody(String text, String mimeType, Charset charset) throws UnsupportedEncodingException {
        super(mimeType);
        if(text == null) {
            throw new IllegalArgumentException("Text may not be null");
        } else {
            if(charset == null) {
                charset = Charset.forName("UTF-8");
            }

            this.content = text.getBytes(charset.name());
            this.charset = charset;
        }
    }

    public StringBody(String text, Charset charset) throws UnsupportedEncodingException {
        this(text, "text/plain", charset);
    }

    public StringBody(String text) throws UnsupportedEncodingException {
        this(text, "text/plain", (Charset)null);
    }

    public Reader getReader() {
        return new InputStreamReader(new ByteArrayInputStream(this.content), this.charset);
    }

    public void writeTo(OutputStream out) throws IOException {
        if(out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        } else {
            ByteArrayInputStream in = new ByteArrayInputStream(this.content);
            byte[] tmp = new byte[4096];

            int l;
            while((l = in.read(tmp)) != -1) {
                out.write(tmp, 0, l);
//                this.callBackInfo.pos += (long)l;
//                if(!this.callBackInfo.doCallBack(false)) {
//                    throw new InterruptedIOException("cancel");
//                }
            }

            out.flush();
        }
    }

    public String getTransferEncoding() {
        return "8bit";
    }

    public String getCharset() {
        return this.charset.name();
    }

    public long getContentLength() {
        return (long)this.content.length;
    }

    public String getFilename() {
        return null;
    }
}
