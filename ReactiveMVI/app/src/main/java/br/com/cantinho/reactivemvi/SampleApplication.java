package br.com.cantinho.reactivemvi;

import android.app.Application;
import android.content.Context;
import br.com.cantinho.reactivemvi.dependencyinjection.DependencyInjection;
import timber.log.Timber;

/**
 * A custom Application class mainly used to provide dependency injection.
 */
public class SampleApplication extends Application {

  protected DependencyInjection dependencyInjection = new DependencyInjection();

  {
    Timber.plant(new Timber.DebugTree());
  }

  public static DependencyInjection getDependencyInjection(final Context context) {
    return ((SampleApplication) context.getApplicationContext()).dependencyInjection;
  }


}
