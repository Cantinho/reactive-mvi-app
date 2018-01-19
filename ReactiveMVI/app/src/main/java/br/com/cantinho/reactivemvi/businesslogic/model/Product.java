package br.com.cantinho.reactivemvi.businesslogic.model;

/**
 * Created by samirtf on 17/01/18.
 */

public class Product implements FeedItem {

  /**
   * Id.
   */
  private int id;

  /**
   * Image.
   */
  private String image;

  /**
   * Name.
   */
  private String name;

  /**
   * Category.
   */
  private String category;

  /**
   * Description.
   */
  private String description;

  /**
   * Price.
   */
  private double price;

  Product() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Product product = (Product) other;

    if (id != product.id) {
      return false;
    }
    if (Double.compare(product.price, price) != 0) {
      return false;
    }
    if (image != null ? !image.equals(product.image) : product.image != null) {
      return false;
    }
    if (name != null ? !name.equals(product.name) : product.name != null) {
      return false;
    }
    if (category != null ? !category.equals(product.category) : product.category != null) {
      return false;
    }
    return description != null ? description.equals(product.description)
        : product.description == null;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = id;
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    temp = Double.doubleToLongBits(price);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Product{" +
        "id=" + id +
        ", image='" + image + '\'' +
        ", name='" + name + '\'' +
        ", category='" + category + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        '}';
  }
}
