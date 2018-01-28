package br.com.cantinho.reactivemvi.businesslogic.model;

import io.reactivex.annotations.NonNull;

/**
 * Created by samirtf on 25/01/18.
 */

public class SectionHeader implements FeedItem {

  private final String name;

  public SectionHeader(@NonNull final String name) {
    this.name = name;
  }

  @NonNull
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SectionHeader that = (SectionHeader) o;

    return name != null ? name.equals(that.name) : that.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "SectionHeader{" +
        "name='" + name + '\'' +
        '}';
  }
}
