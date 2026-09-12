package co.edu.uts.inmobiliaria.listener;

import co.edu.uts.inmobiliaria.config.Database;
import co.edu.uts.inmobiliaria.dao.DatabaseSyncService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public final class DatabaseSyncListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (!Database.isRemoteConfigured() || !Database.isLocalSyncEnabled()) return;
        scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "inmobiliaria-db-sync");
            thread.setDaemon(true);
            return thread;
        });
        int intervalSeconds = Database.getSyncIntervalSeconds();
        scheduler.scheduleWithFixedDelay(() -> synchronize(event), 15, intervalSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (scheduler != null) scheduler.shutdownNow();
    }

    private void synchronize(ServletContextEvent event) {
        try {
            DatabaseSyncService.getInstance().synchronize();
        } catch (Exception exception) {
            event.getServletContext().log("No fue posible sincronizar la réplica local", exception);
        }
    }
}
