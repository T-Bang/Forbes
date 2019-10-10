package controllers;

import beans.Article;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rdb.RdbSingleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static constancts.Constant.*;
import static org.mockito.Mockito.*;

public class SearchControllerTest {

    @Mock private RdbSingleton rdbSingleton;
    @InjectMocks private SearchController searchController;

    private Map<String, Article> cache;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        cache = Map.of(
                "How to swim" , new Article("How to swim", "Just swim", "Lol"),
                "Coding", new Article("Coding", "Write code", "Blah"),
                "Testing", new Article("Testing", "Write unit tests", "Tdd")
                );
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        new SearchController().doGet(request, response);
        verify(response).sendRedirect(indexJSP.getValue());
    }

    @Test (expected = NullPointerException.class)
    public void testDoPostArticles() throws ServletException, IOException, SQLException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(title.getValue())).thenReturn("How");
        new SearchController().doPost(request, response);

        verify(request, atLeast(1)).getParameter(title.getValue());
    }

    @Test (expected = NullPointerException.class)
    public void testDoPostContent() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(viewTitle.getValue())).thenReturn("How to Swim");
        new SearchController().doPost(request, response);
        verify(request, atLeast(1)).getParameter(viewTitle.getValue());
    }
}