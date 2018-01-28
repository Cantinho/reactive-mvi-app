package br.com.cantinho.reactivemvi.businesslogic.model;

import io.reactivex.annotations.NonNull;

/**
 * Created by samirtf on 25/01/18.
 */

public class MainMenuItem implements FeedItem {

  private final String name;
  private final boolean selected;

  public MainMenuItem(final String name, final boolean selected) {
    this.name = name;
    this.selected = selected;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public boolean isSelected() {
    return selected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MainMenuItem that = (MainMenuItem) o;

    if (selected != that.selected) {
      return false;
    }
    return name != null ? name.equals(that.name) : that.name == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (selected ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MainMenuItem{" +
        "name='" + name + '\'' +
        ", selected=" + selected +
        '}';
  }
}
