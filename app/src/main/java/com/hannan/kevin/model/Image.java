package com.hannan.kevin.model;

public class Image {

    private String contentType;
    private String href;
    private String rel;

    /**
     *
     * @return
     * The contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @param contentType
     * The content-type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     *
     * @return
     * The href
     */
    public String getHref() {
        return href;
    }

    /**
     *
     * @param href
     * The href
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     *
     * @return
     * The rel
     */
    public String getRel() {
        return rel;
    }

    /**
     *
     * @param rel
     * The rel
     */
    public void setRel(String rel) {
        this.rel = rel;
    }
}