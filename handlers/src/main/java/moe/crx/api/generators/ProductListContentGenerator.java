package moe.crx.api.generators;

import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import org.jetbrains.annotations.NotNull;

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
                            <form action="/products/delete" method="post">
                                <p>
                                    Name: <input type="text" name="name" value="">
                                </p>
                                <p>
                                    <input type="submit" value="Delete product">
                                </p>
                            </form>
                        </body>
                    </html>""";

    public @NotNull String writeContent(@NotNull List<Product> products,
                               @NotNull List<Organization> organizations) {
        StringBuilder builder = new StringBuilder();
        builder.append(CONTENT_BEGIN);
        products.forEach(product -> {
            var org = organizations
                    .stream()
                    .filter(o -> Objects.equals(o.getId(), product.getOrgId()))
                    .findAny()
                    .orElse(new Organization());
            builder.append(String.format(
                    CONTENT_ROW,
                    product.getId(),
                    org.getName(),
                    org.getId(),
                    product.getName(),
                    product.getAmount()
            ));
        });
        builder.append(CONTENT_END);
        return builder.toString();
    }

}
