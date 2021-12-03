package fun.onysakura.algorithm.utils.core.web.httpclient;

public interface HttpClientConstants {

    enum ContentType {
        APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
        APPLICATION_JSON("application/json"),
        APPLICATION_XML("application/xml"),
        MULTIPART_FORM_DATA("multipart/form-data"),
        APPLICATION_JSON_TEXT("application/json"),
        WEBSERVICE_XML_TEXT("application/xml"),
        ;

        ContentType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getValueWithCharset() {
            return this.getValueWithCharset(null);
        }

        public String getValueWithCharset(String charset) {
            if (charset == null) {
                charset = "UTF-8";
            }
            return this.getValue() + ";charset=" + charset;
        }

        private final String value;
    }

}
