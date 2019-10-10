package beans;

import java.io.Serializable;

public class Article implements Serializable {

    private static final long serialVersionUID = 19620501;
    private String title;
    private String content;
    private String author;

    public Article(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Article {" +
                "title :'" + title + '\'' +
                ", content :'" + content + '\'' +
                ", author :'" + author + '\'' +
                '}';
    }
}
