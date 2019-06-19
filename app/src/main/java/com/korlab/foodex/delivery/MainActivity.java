package com.korlab.foodex.delivery;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.korlab.foodex.delivery.Adapters.TaskAdapter;
import com.korlab.foodex.delivery.Data.Address;
import com.korlab.foodex.delivery.Data.Name;
import com.korlab.foodex.delivery.Data.Task;
import com.korlab.foodex.delivery.Technical.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;
    private ListView listViewTask;
    private ImageView exit;
    private List<Task> tasks;

    public static MainActivity getInstance() {
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Helper.setStatusBarColor(getWindow(), 0xf3f5f8);
        Helper.setStatusBarIconWhite(getWindow());

        instance = this;
        tasks = new ArrayList<>();

        findView();
        exit.setOnClickListener(v-> this.finishAffinity());
        initTaskList();
    }

    private void findView() {
        listViewTask = findViewById(R.id.list_task);
        exit = findViewById(R.id.exit);
    }

    private void initTaskList() {
        generateTaskList("Господаренко", "Юлия","Владимировна",
                "St. Universitets`ka", "30-A", "78",
                "St. Levoberezhna", "10", "22",
                7, 56,
                8, 3,
                "+380 95 948 35 23",
                Task.Type.PACKAGE,
                4);
        generateTaskList("Симиренко", "Дмитрий","Александрович",
                "St. Solovyova", "11", "35",
                "St. Prilago", "93", "22/2",
                8, 3,
                8, 34,
                "+380 97 245 43 59",
                Task.Type.BAG,
                1);
        generateTaskList("Рыбак", "Анна","Сергеевна",
                "St. Prilago", "93", "22/2",
                "St. Uritskogo", "2", "17",
                8, 34,
                9, 23,
                "+380 98 257 57 95",
                Task.Type.PACKAGE,
                3);
        generateTaskList("Симиренко", "Дмитрий","Александрович",
                "St. Solovyova", "11", "35",
                "St. Prilago", "93", "22/2",
                8, 3,
                8, 34,
                "+380 97 245 43 59",
                Task.Type.BAG,
                1);
        generateTaskList("Господаренко", "Юлия","Владимировна",
                "St. Universitets`ka", "30-A", "78",
                "St. Levoberezhna", "10", "22",
                7, 56,
                8, 3,
                "+380 95 948 35 23",
                Task.Type.PACKAGE,
                4);
        generateTaskList("Рыбак", "Анна","Сергеевна",
                "St. Prilago", "93", "22/2",
                "St. Uritskogo", "2", "17",
                8, 34,
                9, 23,
                "+380 98 257 57 95",
                Task.Type.PACKAGE,
                3);
        TaskAdapter programAdapter = new TaskAdapter(tasks, getInstance().getBaseContext());
        listViewTask.setAdapter(programAdapter);
        listViewTask.setDivider(null);
        listViewTask.setDividerHeight(0);
        listViewTask.setOnTouchListener((v, event) -> {
            return false;
        });
    }

    private void generateTaskList(String first, String last, String middle,
                                  String sS, String sH, String sF,
                                  String endS, String endH, String endF,
                                  int sHours, int sMinutes,
                                  int endHours, int endMinutes,
                                  String phone,
                                  Task.Type type,
                                  int countBags ) {
        Name name = new Name(first, last, middle);
        Address startAddress = new Address(sS, sH, sF);
        Address endAddress = new Address(endS, endH, endF);
        Date startTime = new Date(2019,6,10);
        startTime.setHours(sHours);
        startTime.setMinutes(sMinutes);
        Date endTime = new Date(2019,6,10);
        endTime.setHours(endHours);
        endTime.setMinutes(endMinutes);
        Task task = new Task(name, startAddress, endAddress, startTime, endTime, phone,type,  countBags);
//        task.setDone(false);
        tasks.add(task);
    }
}
