package com.yxx.framework.dto;

import java.io.Serializable;

/**
 * <p>
 * desc
 * </p>
 *
 * @author wangpan
 * @date 2019/11/5
 */
public class Movies implements Serializable {

    private static final long serialVersionUID = -1418858120727734348L;

    private String title;
    private String desc;
    private String stars;
    private String quote;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
