package moe.crx.servlets;

import com.google.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ProductsListServlet extends AbstractServlet {

    public final ProductDao productDao;
    public final OrganizationDao organizationDao;

    @Inject
    public ProductsListServlet(@NotNull ProductDao productDao,
                               @NotNull OrganizationDao organizationDao) {
        this.productDao = productDao;
        this.organizationDao = organizationDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
        try (var writer = resp.getWriter()) {
            writer.println("<html><body><h1>Product list</h1>");

            var orgs = organizationDao.all();

            writer.println("<table>");
            writer.println("<tr><th>#</th><th>Manufacturer</th><th>Name</th><th>Amount</th></tr>");
            productDao.all().forEach(product -> {
                var org = orgs.stream().filter(o -> o.getId() == product.getOrgId()).findAny().get();
                writer.printf("<tr><td>%d</td><td>%s#%d</td><td>%s</td><td>%d</td></tr>%n",
                        product.getId(),
                        org.getName(),
                        org.getId(),
                        product.getName(),
                        product.getAmount());
            });
            writer.println("</table>");

            writer.println("""
                    <form action="/products/add" method="post">
                      <p>
                      Name: <input type="text" name="name" value=""><br>
                      Manufacturer: <input type="text" name="manufacturer" value=""><br>
                      Amount: <input type="text" name="amount" value="">
                      </p>
                      <p><input type="submit" value="Add product"></p>
                     </form></body></html>""");
        }
    }

    @Override
    public String getServletPath() {
        return "/products/list";
    }
}
