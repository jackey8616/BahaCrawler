package org.mocraft;

import java.net.URL;

/**
 * Created by Clode on 2017/4/5.
 */
public class Article {

    public String toString() {
        return this.name;
    }

    public URL getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    private String name;
    private URL url;

    public Article(String name, String url) throws Exception {
        this(name, new URL("https://" + (url.contains("forum.gamer.com.tw/") ? "" : "forum.gamer.com.tw/") + url));
    }

    public Article(String name, URL url) {
        this.name = name;
        this.url = url;
    }
}
