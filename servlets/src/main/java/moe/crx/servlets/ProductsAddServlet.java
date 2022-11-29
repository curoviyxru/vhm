package moe.crx.servlets;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ProductsAddServlet extends AbstractServlet {

    public final ProductDao productDao;
    public final OrganizationDao organizationDao;

    @Inject
    public ProductsAddServlet(@NotNull ProductDao productDao,
                              @NotNull OrganizationDao organizationDao) {
        this.productDao = productDao;
        this.organizationDao = organizationDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");

        Organization org = new Organization(0, req.getParameter("manufacturer"));

        if (org.getName().trim().isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong manufacturer name.");
            return;
        }

        int orgId = organizationDao.create(org);

        if (orgId == -1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Internal error: organization creation.");
            return;
        }

        Product product;

        try {
            product = new Product(0,
                    req.getParameter("name"),
                    orgId,
                    Integer.parseInt(req.getParameter("amount")));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong amount number.");
            return;
        }

        if (product.getAmount() < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong amount number.");
            return;
        }

        if (product.getName().trim().isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong product name.");
            return;
        }

        if (productDao.create(product) == -1) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error: product creation.");
            return;
        }

        resp.sendRedirect("/products/list");
    }

    @Override
    public String getServletPath() {
        return "/products/add";
    }
}
