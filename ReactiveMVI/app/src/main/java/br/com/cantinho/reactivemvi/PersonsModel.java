package br.com.cantinho.reactivemvi;

import java.util.List;

/**
 * Created by samirtf on 17/01/18.
 */
class PersonsModel {

  // In a real application fields would be private
  // and we would have getters to access them

  /**
   * A bppçeam as loading view state.
   */
  final boolean loading;

  /**
   * A list of persons.
   */
  final List<Person> persons;

  /**
   * An error.
   */
  final Throwable error;

  /**
   * Builds a custom Person model.
   *
   * @param loading a boolean representing loading view.
   * @param persons a list of persons.
   * @param error an error.
   */
  public PersonsModel(final boolean loading, final List<Person> persons, final Throwable error) {
    this.loading = loading;
    this.persons = persons;
    this.error = error;
  }

}
