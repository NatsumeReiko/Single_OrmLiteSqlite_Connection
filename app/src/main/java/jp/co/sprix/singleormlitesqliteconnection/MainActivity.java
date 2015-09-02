package jp.co.sprix.singleormlitesqliteconnection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    List<TestData> result00 = new ArrayList<>();
    List<TestData> result01 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager.initializeInstance(MainActivity.this);

        EventBus.getDefault().register(this);

        findViewById(R.id.get_db_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BackupDatabase.copyDirectory(MainActivity.this, new File(""), new File(""));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbIntentService.startActionGetData(MainActivity.this.getApplicationContext(), "abc", "cde");
                DbIntentService.startActionAddData(MainActivity.this.getApplicationContext(), "abc", "cde");
            }
        });
        findViewById(R.id.open_new_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
            }
        });

        findViewById(R.id.add_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        findViewById(R.id.get_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseManager manager = DatabaseManager.getInstance();
                manager.executeGetDataQueryTask(new QueryExecutor() {
                    @Override
                    public void run() {
                        boolean success = true;

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

                                for (TestData item : result00) {
                                    Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age);
                                }


                            } else {
                                dbReadError();
                            }
                        }
                    }
                });

                final DatabaseManager manager01 = DatabaseManager.getInstance();
                manager01.executeGetDataQueryTask(new QueryExecutor() {
                    @Override
                    public void run() {
                        boolean success = true;

                        try {
                            RuntimeExceptionDao<TestData, Integer> targetDao = manager01.getDao(TestData.class);

                            QueryBuilder<TestData, Integer> queryBuilder =
                                    targetDao.queryBuilder();

                            Where<TestData, Integer> where = queryBuilder.where();
                            SelectArg selectArg = new SelectArg();
                            where.le("age", selectArg);

//                            where.and();

//                            SelectArg selectArg01 = new SelectArg();
//                            where.eq("gender", selectArg01);

                            PreparedQuery<TestData> preparedQuery = queryBuilder.prepare();

                            selectArg.setValue("50");
//                            selectArg01.setValue("W");

                            result01 = targetDao.query(preparedQuery);

                        } catch (Exception e) {
                            success = false;
                        } finally {

                            if (success) {
                                Log.d(Constant.LOG_TAG, "*********************************");
//                                doSomething();
                                for (TestData item : result01) {
                                    Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age + " gender: " + item.gender);
                                }
                                Log.d(Constant.LOG_TAG, "*********************************");

                            } else {
                                dbReadError();
                            }
                        }
                    }
                });
            }
        });

    }

    private void dbReadError() {

    }

    private void doSomething() {
        for (TestData item : result00) {
            Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age);
        }

        for (TestData item : result01) {
            Log.d(Constant.LOG_TAG, "ID: " + item.userId + " Age: " + item.age);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void onEvent(
            final EventBusData event) {
        Log.d(Constant.LOG_TAG, "IEventBusData");

    }

}
