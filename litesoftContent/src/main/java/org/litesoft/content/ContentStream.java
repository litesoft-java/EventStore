package org.litesoft.content;

import java.io.InputStream;

@SuppressWarnings("unused")
public interface ContentStream extends HasLastModified {
    long NO_CONTENT = -1;

    /**
     * @return -1 indicates that there was no Content (which means that the <code>InputStream</code> will not have been opened and the accessor will return null).
     */
    long getContentLength();

    /**
     * return the underlying <code>InputStream</code>.
     * <p>
     * Note: IF the underlying <code>InputStream</code> is not null then it is already open and MUST be closed!
     *
     * @return nullable
     */
    InputStream getContentIS();
}
