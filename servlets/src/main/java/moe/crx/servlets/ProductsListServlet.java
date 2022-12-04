package moe.crx.servlets;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class ProductsListServlet extends AbstractServlet {

    private final ProductDao productDao;
    private final OrganizationDao organizationDao;
    private final ProductListContentGenerator contentGenerator;

    @Inject
    public ProductsListServlet(@NotNull ProductDao productDao,
                               @NotNull OrganizationDao organizationDao,
                               @NotNull ProductListContentGenerator contentGenerator) {
        this.productDao = productDao;
        this.organizationDao = organizationDao;
        this.contentGenerator = contentGenerator;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var organizations = organizationDao.all();
        var products = productDao.all();

        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        try (var writer = resp.getWriter()) {
            contentGenerator.writeContent(writer, organizations, products);
        }
    }

    @Override
    public String getServletPath() {
        return "/products/list";
    }
}
