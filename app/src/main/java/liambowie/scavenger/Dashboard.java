package liambowie.scavenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class Dashboard extends AppCompatActivity {

    // Instance Variables for the Dashboard Class
    final Firebase mRootRef = new Firebase("https://scavengr.firebaseio.com");
    final Firebase mTaskRef = mRootRef.child("tasks");
    Button mBack;
    TextView mTeamName;
    ListView mTasks;
    ProgressBar mTeamScoreBar;
    String mTeamKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiating all UI Components and setting variables
        mBack = (Button)findViewById(R.id.button);
        mTeamName = (TextView)findViewById(R.id.textView_dashTeamName);
        mTasks = (ListView)findViewById(R.id.listView_tasks);
        mTeamScoreBar = (ProgressBar)findViewById(R.id.progressBar_taskProgress);

        Bundle extras = getIntent().getExtras();
        if (extras!=null)
        {
            mTeamKey = extras.getString("liambowie.scavenger.teamkey");
        }

        // Set the team name in the top left corner of the dashboard
        SharedPreferences preferences = getSharedPreferences("team", 0);
        String teamName = preferences.getString("team_name", /*Should never appear*/ "Unassigned");
        mTeamName.setText("Team:" + teamName);

//        Created dummy tasks to be shown in the ListView mTasks
//        Task task = new Task("Dippin' Skinny", "Go skinny dipping in the North Sea", 25);
//        mTaskRef.push().setValue(task);
//        Task task1 = new Task("Going Somewhere?", "Get a photo outside Dundee Airport", 40);
//        mTaskRef.push().setValue(task1);
//        Task task2 = new Task("Abdn got Talent", "Earn £5 busking on the streets of Aberdeen", 20);
//        mTaskRef.push().setValue(task2);
//        Task task3 = new Task("Mikey's Challenge", "Eat a hotdog underwater", 20);
//        mTaskRef.push().setValue(task3);
//        Task task4 = new Task("Beer Tower pt.1", "Fill a beer tower that has a base of 5 cups by 5 cups", 35);
//        mTaskRef.push().setValue(task4);
//        Task task5 = new Task("Beer Tower pt.2", "Drink the beer tower!", 35);
//        mTaskRef.push().setValue(task5);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("team", 0).edit();
                editor.remove("team_name").commit();
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Firebase mTeamRef = mRootRef.child("teams/" + mTeamKey);

        FirebaseListAdapter<Task> adapter = new FirebaseListAdapter<Task>(this, Task.class, android.R.layout.simple_list_item_1, mTaskRef) {
            @Override
            protected void populateView(View view, Task task, int i) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(task.getName());
            }
        };
        mTasks.setAdapter(adapter);

        // Setting the Progress Bar
        int maxScore = 0;
        for(int i = 0; i < mTasks.getCount(); i++){
            Task current = (Task)mTasks.getItemAtPosition(i);
            maxScore += current.getScore();
        }
        mTeamScoreBar.setMax(maxScore);

        mTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task)mTasks.getItemAtPosition(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);
                alert.setTitle(task.getName());
                alert.setMessage(task.getDesc() + "\n\n worth: " + task.getScore() + "points");
                alert.setPositiveButton(R.string.complete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setNegativeButton(R.string.incomplete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });
    }
}
