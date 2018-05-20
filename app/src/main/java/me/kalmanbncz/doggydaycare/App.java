package me.kalmanbncz.doggydaycare;

import android.app.Application;
import android.os.StrictMode;
import com.squareup.leakcanary.LeakCanary;
import me.kalmanbncz.doggydaycare.presentation.splash.SplashFlowScope;
import me.kalmanbncz.doggydaycare.presentation.splash.SplashModule;
import retrofit2.Retrofit;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by kalman.bencze on 18/05/2018.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

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
        Scope scope = Toothpick.openScopes(AppScope.class);
        scope.bindScopeAnnotation(AppScope.class);
        scope.installModules(new AppModule(this));
        scope.installModules(new BackendApiModule(scope.getInstance(Retrofit.class)));
        scope.installModules(new DatabaseModule(this));
        Toothpick.inject(this, scope);
        Scope splash = Toothpick.openScopes(AppScope.class, SplashFlowScope.class);
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
