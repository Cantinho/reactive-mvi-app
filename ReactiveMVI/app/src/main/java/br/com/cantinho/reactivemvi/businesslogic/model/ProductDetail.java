package br.com.cantinho.reactivemvi.businesslogic.model;

/**
 * Created by samirtf on 25/01/18.
 */

public class ProductDetail implements FeedItem {

  private final Product product;
  private final boolean isInShoppingCart;

  public ProductDetail(final Product product, final boolean isInShoppingCart) {
    this.product = product;
    this.isInShoppingCart = isInShoppingCart;
  }

  public Product getProduct() {
    return product;
  }

  public boolean isInShoppingCart() {
    return isInShoppingCart;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProductDetail that = (ProductDetail) o;

    if (isInShoppingCart != that.isInShoppingCart) {
      return false;
    }
    return product != null ? product.equals(that.product) : that.product == null;
  }

  @Override
  public int hashCode() {
    int result = product != null ? product.hashCode() : 0;
    result = 31 * result + (isInShoppingCart ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ProductDetail{" +
        "product=" + product +
        ", isInShoppingCart=" + isInShoppingCart +
        '}';
  }
}
