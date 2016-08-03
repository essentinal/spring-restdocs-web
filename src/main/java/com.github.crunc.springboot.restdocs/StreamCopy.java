package com.github.crunc.springboot.restdocs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class StreamCopy {

    private StreamCopy() {
        throw new UnsupportedOperationException(StreamCopy.class.getName() + " may not be instantiated");
    }

    static From from(final InputStream source) throws IOException {
        return new From(source);
    }

    static final class From {

        final InputStream source;

        From(final InputStream source) {
            this.source = source;
        }

        To to(final OutputStream target) {
            return new To(source, target);
        }
    }

    static final class To {

        final InputStream source;
        final OutputStream target;

        To(final InputStream source, final OutputStream target) {
            this.source = source;
            this.target = target;
        }

        Copy copy() throws IOException {
            return new Copy(source, target);
        }
    }

    static final class Copy {

        final InputStream source;
        final OutputStream target;
        final long copied;

        Copy(final InputStream source, final OutputStream target) throws IOException {

            this.source = source;
            this.target = target;

            final byte[] buffer = new byte[4096];
            long count = 0;
            int n = 0;
            while (-1 != (n = source.read(buffer))) {
                target.write(buffer, 0, n);
                count += n;
            }

            this.copied = count;
        }

        long dontClose() {
            return copied;
        }

        long close() throws IOException {
            closeInputStream();
            return closeOutputStream();
        }

        long closeInputStream() throws IOException {
            source.close();
            return copied;
        }

        long closeOutputStream() throws IOException {
            target.close();
            return copied;
        }

        long closeQuietly() {
            closeInputStreamQuietly();
            return closeOutputStreamQuietly();
        }

        long closeInputStreamQuietly() {
            try {
                closeInputStream();
            } catch (final IOException ignored) {
            }
            return copied;
        }

        long closeOutputStreamQuietly() {
            try {
                closeOutputStream();
            } catch (final IOException ignored) {
            }
            return copied;
        }
    }
}
