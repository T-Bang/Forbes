package constancts;

public enum  Constant {
     indexJSP("index.jsp"),
     detailsJSP ("/WEB-INF/hiddenjsp/details.jsp"),
     listJSP ("/WEB-INF/hiddenjsp/list.jsp"),
     filePath ("/Users/thabang/webapps/Forbes/src/main/java/data/Articles.csv"),
     title ("title"),
     viewTitle("title2"),
     article ("article"),
     articleList ("Articles"),
     content ("content"),
     author ("author"),
     rdbDriverClass ("RdbDriverClass"),
     rdbURL ("RdbURL"),
     rdbUserId ("RdbUserId"),
     rdbPassword ("RdbPassword"),
     page("page");

    private String value;

    Constant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
