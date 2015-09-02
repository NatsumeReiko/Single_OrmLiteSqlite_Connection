package jp.co.sprix.singleormlitesqliteconnection;


import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class DatabaseManager {

//    private static AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;

    public <T> RuntimeExceptionDao<T, Integer> getDao(Class<T> tClass) {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return mDatabaseHelper.getRuntimeExceptionDao(tClass);
    }

    private static DatabaseHelper mDatabaseHelper;

    private DatabaseManager() {
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = OpenHelperManager.getHelper(context.getApplicationContext(), DatabaseHelper.class);

        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

//        if(mOpenCounter.incrementAndGet() == 1 ) {
//            mDatabaseHelper.getWritableDatabase();
//        }
//        Log.d(Constant.LOG_TAG, "Database open counter: " + mOpenCounter.get());

        return instance;
    }

//    private synchronized void closeDatabase() {
//        if (mOpenCounter.decrementAndGet() == 0) {
//            // Closing database
//            mDatabaseHelper.close();
//
//        }
//        Log.d(Constant.LOG_TAG, "Database close counter: " + mOpenCounter.get());
//    }

    public <T> void executeAddDataQueryTask(final List<T> dataList, final Class<T> tClass) {

        if (dataList == null) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                RuntimeExceptionDao<T, Integer> dao = mDatabaseHelper.getRuntimeExceptionDao(tClass);
                for (T data : dataList) {
                    dao.create(data);
                }
//                closeDatabase();
                Log.d(Constant.LOG_TAG, "End *Add* action");

            }
        }).start();
    }

    public void executeGetDataQueryTask(final QueryExecutor executor) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                executor.run();
//                closeDatabase();
                Log.d(Constant.LOG_TAG, "End *GET* action");

            }
        }).start();
    }


}
