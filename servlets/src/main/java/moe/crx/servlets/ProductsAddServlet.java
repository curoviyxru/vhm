package moe.crx.servlets;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.jooq.tables.records.ProductsRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ProductsAddServlet extends AbstractServlet {

    private final ProductDao productDao;
    private final OrganizationDao organizationDao;

    @Inject
    public ProductsAddServlet(@NotNull ProductDao productDao,
                              @NotNull OrganizationDao organizationDao) {
        this.productDao = productDao;
        this.organizationDao = organizationDao;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Как это сделать? (https://github.com/curoviyxru/vhm/pull/7#discussion_r1038777241)
        resp.setContentType("text/plain");

        OrganizationsRecord organization = new OrganizationsRecord(0, req.getParameter("manufacturer"));

        if (organization.getName().trim().isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong manufacturer name.");
            return;
        }

        if ((organization = organizationDao.create(organization)) == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Internal error: organization creation.");
            return;
        }

        ProductsRecord product;

        try {
            product = new ProductsRecord(0,
                    req.getParameter("name"),
                    organization.getId(),
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

        if (productDao.create(product) == null) {
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
