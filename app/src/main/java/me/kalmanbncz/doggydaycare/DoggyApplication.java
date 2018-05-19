package me.kalmanbncz.doggydaycare;

import android.app.Application;
import android.os.StrictMode;
import com.squareup.leakcanary.LeakCanary;
import me.kalmanbncz.doggydaycare.di.AppModule;
import me.kalmanbncz.doggydaycare.di.BackendApiModule;
import me.kalmanbncz.doggydaycare.di.SplashModule;
import me.kalmanbncz.doggydaycare.di.scopes.ApplicationScope;
import me.kalmanbncz.doggydaycare.di.scopes.flow.SplashFlowScope;
import retrofit2.Retrofit;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class DoggyApplication extends Application {

    private static final String TAG = DoggyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            setStrictMode();
        }

        //        Toothpick.setConfiguration(Configuration.forDevelopment().disableReflection());
        //        MemberInjectorRegistryLocator.setRootRegistry(new me.kalmanbncz.doggydaycare.MemberInjectorRegistry());
        //        FactoryRegistryLocator.setRootRegistry(new me.kalmanbncz.doggydaycare.FactoryRegistry());
        Scope scope = Toothpick.openScopes(ApplicationScope.class);
        scope.bindScopeAnnotation(ApplicationScope.class);
        scope.installModules(new AppModule(this));
        scope.installModules(new BackendApiModule(scope.getInstance(Retrofit.class)));
        Toothpick.inject(this, scope);
        Scope splash = Toothpick.openScopes(ApplicationScope.class, SplashFlowScope.class);
        splash.bindScopeAnnotation(SplashFlowScope.class);
        splash.installModules(new SplashModule());
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                       .detectAll()
                                       .penaltyLog()
                                       .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                   .detectLeakedSqlLiteObjects()
                                   .penaltyLog()
                                   .penaltyDeath()
                                   .build());
    }
}
