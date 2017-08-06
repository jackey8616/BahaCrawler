package org.mocraft;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Clode on 2017/4/5.
 */
public class BahaCrawler {

    static GuiMain guiMain;

    static String crawl(URL url) {
        String result = "";
        BufferedReader in = null;
        try {
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            while((line = in.readLine()) != null)
                result += line + "\n";
        } catch (Exception e) {
            System.out.println("Error occurred while sending get request!" + e);
            e.printStackTrace();
        } finally {
            try {
                if(in != null)
                    in.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    static ArrayList<Article> splitBahaArticle(String html) throws Exception {
        guiMain.log.append("[Splitting articles...]\n");
        ArrayList<Article> result = new ArrayList<>();
        String[] lines = html.split("\n");
        for(String line : lines) {
            String url = "", name="";
            if(line.contains("FM-blist3") && !line.contains("FM-blist3A")) {
                line = line.substring(line.indexOf("href=") + 6);
                url = line.substring(2, line.indexOf("\""));
                name = line.substring(line.indexOf(">") + 1, line.indexOf("</a>"));
            } else if(line.contains("b-list__main__title") && !line.contains("is-del")) {
                line = line.replace(" is-highlight", "");
                line = line.substring(line.indexOf("href=") + 6);
                url = line.substring(0, line.indexOf("\""));
                line = line.substring(line.indexOf("b-list__main__title"));
                name = line.substring(21, line.indexOf("</a>"));
            } else {
                continue;
            }
            Article article = new Article(name, url);
            result.add(article);
        }
        guiMain.log.append("[Split completed!]\n");
        return result;
    }

    static ArrayList<Article> banListCrawl(ArrayList<Article> list, String[] banString) throws Exception {
        guiMain.log.append("[Detecting banned words...]\n");
        int i = 1;
        ArrayList<Article> result = new ArrayList<>();
        for(Article article : list) {
            String html = crawl(article.getUrl());
            for(String ban : banString) {
                if(html.contains(ban)) {
                    guiMain.log.append("Find Num." + i + " banned article name:" + article.getName().substring(0, article.getName().length() * 2 / 3) + "\n");
                    ((DefaultListModel)guiMain.list.getModel()).addElement(article);
                    result.add(article);
                    i++;
                    break;
                }
            }
        }
        guiMain.log.append("[Detection completed. Find " + (i - 1) + " results.]\n");
        return result;
    }

    void start(String url, String[] banString) {
        try {
            int i = 1;
            String result = "";

            ArrayList<Article> articleList = new ArrayList<>();
            ArrayList<Article> banList = new ArrayList<>();

            while (!(result = crawl(new URL(url + "&page=" + i))).contains("next no")) {
                guiMain.log.append("Processing Page: " + i + "\n");
                System.out.println("Page" + i);

                articleList = splitBahaArticle(result);
                banList = banListCrawl(articleList, banString);

                for (Article article : banList) {
                    System.out.println(article.getName() + " / " + article.getUrl().toString());
                }
                System.out.println("========================================================================");
                i++;
            }
            guiMain.log.append("[All crawl done!]\n ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        guiMain = new GuiMain();

    }
}

