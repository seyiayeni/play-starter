import com.google.inject.AbstractModule;
import controllers.Singletons;
import formatters.FormattersProvider;
import play.data.format.Formatters;
import services.CacheService;
import services.DBService;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(CacheService.class).asEagerSingleton();
        bind(DBService.class).asEagerSingleton();
        bind(Singletons.class).asEagerSingleton();
        bind(Formatters.class).toProvider(FormattersProvider.class);
    }
}
