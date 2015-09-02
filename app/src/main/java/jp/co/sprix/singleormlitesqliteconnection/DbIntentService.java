package jp.co.sprix.singleormlitesqliteconnection;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DbIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "jp.co.sprix.singleormlitesqliteconnection.action.FOO";
    private static final String ACTION_BAZ = "jp.co.sprix.singleormlitesqliteconnection.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "jp.co.sprix.singleormlitesqliteconnection.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "jp.co.sprix.singleormlitesqliteconnection.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetData(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionAddData(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public DbIntentService() {
        super("DbIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        Log.d(Constant.LOG_TAG, "Start *GET* action" );

        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
        final DatabaseManager manager = DatabaseManager.getInstance();
        manager.executeGetDataQueryTask(new QueryExecutor() {
            @Override
            public void run() {
                boolean success = true;
                List<TestData> result00 = new ArrayList<>();

                try {
                    RuntimeExceptionDao<TestData, Integer> targetDao = manager.getDao(TestData.class);

                    QueryBuilder<TestData, Integer> queryBuilder =
                            targetDao.queryBuilder();

                    queryBuilder.where().ge("age", 50);
                    queryBuilder.orderBy("age", true);
                    result00 = targetDao.query(queryBuilder.prepare());


                } catch (Exception e) {
                    success = false;
                } finally {

                    if (success) {
//                                doSomething();

//                        for (TestData item : result00) {
//                            Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age);
//                        }


                    } else {
                    }
                }
            }
        });


        final DatabaseManager manager01 = DatabaseManager.getInstance();
        manager01.executeGetDataQueryTask(new QueryExecutor() {
            @Override
            public void run() {
                List<TestData> result01 = new ArrayList<>();

                boolean success = true;

                try {
                    RuntimeExceptionDao<TestData, Integer> targetDao = manager01.getDao(TestData.class);

                    QueryBuilder<TestData, Integer> queryBuilder =
                            targetDao.queryBuilder();

                    Where<TestData, Integer> where = queryBuilder.where();
                    SelectArg selectArg = new SelectArg();
                    where.le("age", selectArg);

                    where.and();

                    SelectArg selectArg01 = new SelectArg();
                    where.eq("gender", selectArg01);

                    PreparedQuery<TestData> preparedQuery = queryBuilder.prepare();

                    selectArg.setValue("50");
                    selectArg01.setValue("W");

                    result01 = targetDao.query(preparedQuery);

                } catch (Exception e) {
                    success = false;
                } finally {

                    if (success) {
//                        Log.d(Constant.LOG_TAG, "*********************************");
////                                doSomething();
//                        for (TestData item : result01) {
//                            Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age + " gender: " + item.gender);
//                        }
//                        Log.d(Constant.LOG_TAG, "*********************************");

                    } else {
                    }
                }
            }
        });


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {

        Log.d(Constant.LOG_TAG, "Start *Add* action" );

        List<TestData> targetList1 = new ArrayList<>();
        for (int index = 0; index < 100; index++) {
            TestData data = new TestData(index, index + "n", index % 2 == 0 ? "W" : "M", index);
            targetList1.add(data);
        }

        List<TestData> targetList2 = new ArrayList<>();
        for (int index = 100; index < 199; index++) {
            TestData data = new TestData(index, index + "n", index % 2 == 0 ? "W" : "M", index);
            targetList2.add(data);
        }

        DatabaseManager manager = DatabaseManager.getInstance();
        manager.executeAddDataQueryTask(targetList1, TestData.class);

        DatabaseManager manager01 = DatabaseManager.getInstance();
        manager01.executeAddDataQueryTask(targetList2, TestData.class);

        EventBus.getDefault().post(new EventBusData());

    }
}
