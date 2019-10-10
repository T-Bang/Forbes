package controllers;

import beans.Article;
import constancts.Constant;
import org.apache.logging.log4j.LogManager;
import rdb.RdbSingleton;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;

import static constancts.Constant.*;

public class SearchController extends HttpServlet {
    private static Logger LOGGER = LogManager.getLogger(SearchController.class);
    private static RdbSingleton rdbSingleton;
    private static Map<String, Article> cache;

    public void init(ServletConfig config) {
        cache = new HashMap<>();
        rdbSingleton = RdbSingleton.getInstance();
        rdbSingleton.setupDatabaseConnection(config);
        rdbSingleton.initializeDbData(filePath.getValue());
    }

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(indexJSP.getValue());
    }


    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
        controller(req, resp);
    }

    private void controller(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqTitle = req.getParameter(title.getValue());
        String reqViewTitle = req.getParameter(viewTitle.getValue());
        String jspPath = "";

        req.setAttribute(title.getValue(), reqTitle);
        req.setAttribute(viewTitle.getValue(), reqViewTitle);

        if (reqViewTitle != null) {
            jspPath = detailsJSP.getValue();
            req.setAttribute(article.getValue(), cache.get(reqViewTitle));
        } else {
            jspPath = listJSP.getValue();
            setupArticleList(req, reqTitle);
        }

        if (cache.values().size() > 10) {
            int page = (req.getParameter(Constant.page.getValue()) != null)
                    ? Integer.parseInt(req.getParameter(Constant.page.getValue()))
                    : 0,
                start = Math.min(page * 10, cache.values().size()-1),
                end = Math.min((page + 1) * 10, cache.values().size());

            final List<Article> values = new ArrayList<>(cache.values()).subList(start, end);
            req.setAttribute(Constant.page.getValue(), page+1);
            req.setAttribute(articleList.getValue(), values);
        }
        LOGGER.info("Resulting JSP: " + jspPath);
        req.getRequestDispatcher(jspPath).forward(req, resp);
    }

    private void setupArticleList(HttpServletRequest req, String title) {
        String sql = "select * from articles";
        Connection conn;
        ResultSet rs = null;
        PreparedStatement prepStatem;
        List<Article> articles = new ArrayList<>();

        try {
            sql += " where title like ?";
            conn = rdbSingleton.getConnection();
            prepStatem = conn.prepareStatement(sql);
            prepStatem.setString(1, "%"+title+"%");

            rs = prepStatem.executeQuery();

            while (rs.next()) {
                LOGGER.info("Setting up list for: " + rs.getString(Constant.title.getValue()));
                Article article = new Article(rs.getString(Constant.title.getValue()),
                        rs.getString(content.getValue()), rs.getString(author.getValue()));
                articles.add(article);
            }
            cache.putAll(articles.stream().collect(Collectors.toMap(Article::getTitle, Function.identity())));
        } catch (SQLException sqlExc) {
            LOGGER.error("Cannot read the database", sqlExc);
            throw new IllegalStateException("Cannot read the information", sqlExc);
        } finally {
            rdbSingleton.cleanUp(rs);
        }

        LOGGER.info("Resulting Person list of size: " + articles.size());
        req.setAttribute(articleList.getValue(), articles);
    }
}
