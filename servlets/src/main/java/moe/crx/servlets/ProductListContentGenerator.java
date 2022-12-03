package moe.crx.servlets;

import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.jooq.tables.records.ProductsRecord;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public final class ProductListContentGenerator {

    private static final String CONTENT_BEGIN = """
                    <html>
                        <body>
                            <h1>Product list</h1>
                            <table>
                                <tr><th>#</th><th>Manufacturer</th><th>Name</th><th>Amount</th></tr>""";
    private static final String CONTENT_ROW = "<tr><td>%d</td><td>%s#%d</td><td>%s</td><td>%d</td></tr>";
    private static final String CONTENT_END = """
                            </table>
                            <form action="/products/add" method="post">
                                <p>
                                    Manufacturer: <input type="text" name="manufacturer" value=""><br>
                                    Name: <input type="text" name="name" value=""><br>
                                    Amount: <input type="text" name="amount" value="">
                                </p>
                                <p>
                                    <input type="submit" value="Add product">
                                </p>
                            </form>
                        </body>
                    </html>""";

    public void writeContent(PrintWriter writer,
                               List<OrganizationsRecord> organizations,
                               List<ProductsRecord> products) {
        writer.append(CONTENT_BEGIN);
        products.forEach(product -> {
            var org =
                    organizations.stream().filter(o -> Objects.equals(o.getId(), product.getOrgId())).findAny().get();
            writer.printf(CONTENT_ROW,
                    product.getId(),
                    org.getName(),
                    org.getId(),
                    product.getName(),
                    product.getAmount());
        });
        writer.append(CONTENT_END);
    }

}
