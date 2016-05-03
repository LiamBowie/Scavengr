package liambowie.scavenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import liambowie.scavenger.objects.Team;

public class SignUp extends AppCompatActivity {

    // Instance Variable for Sign Up Activity
    final Firebase mRootRef = new Firebase("https://scavengr.firebaseio.com");
    final Firebase mTeamRef = mRootRef.child("teams");
    TextView mNoTeamsMsg;
    EditText mTeamName;
    Button mSubmit;
    ListView mTeamList;
    CheckBox mJoinTeam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiating all UI Components and setting variables
        mTeamName = (EditText)findViewById(R.id.editText_teamName);
        mSubmit = (Button)findViewById(R.id.button_submitTeamName);
        mTeamList = (ListView)findViewById(R.id.listView_teamList);
        mJoinTeam = (CheckBox)findViewById(R.id.checkBox_joinTeam);
        mNoTeamsMsg = (TextView)findViewById(R.id.textView_noTeams);

        // Hide the List of Teams and No Team Msg by default (Shown by Chkbox click)
        mTeamList.setVisibility(View.GONE);
        mNoTeamsMsg.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Access Shared Preferences to determine whether the user is part of a team already
        SharedPreferences preferences = getSharedPreferences("team", 0);
        String user = preferences.getString("team_name", "unassigned");

        // This is not clean since this activity is always launched first.
        // Maybe try a splash screen? May also be messy but it'll be nicer than this
        if(user != "unassigned"){ // If the user is already with a team..
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        // Firebase List Adapter to populate the list of teams in real time
        FirebaseListAdapter<Team> adapter = new FirebaseListAdapter<Team>(this, Team.class, android.R.layout.simple_list_item_1, mTeamRef) {
            @Override
            protected void populateView(View view, Team team, int i) {
                ((TextView)view.findViewById(android.R.id.text1)).setText(team.getTeamName());
            }
        };
        mTeamList.setAdapter(adapter);

        // Toggle whether to show New Team options or Join Team options
        mJoinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mJoinTeam.isChecked()) {
                    mTeamName.setVisibility(View.GONE);
                    mSubmit.setVisibility(View.GONE);
                    mTeamList.setVisibility(View.VISIBLE);
                    //mNoTeamsMsg.setVisibility(View.VISIBLE);
                } else {
                    mTeamName.setVisibility(View.VISIBLE);
                    mSubmit.setVisibility(View.VISIBLE);
                    mTeamList.setVisibility(View.GONE);
                    mNoTeamsMsg.setVisibility(View.GONE);
                }
            }
        });

        // Creating a new team by clicking submit.
        // Team name gets saved to preferences and is added to Firebase
        // User is the naviageted to the Dashboard
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mTeamName.getText().toString().isEmpty()) {
                    String teamName = mTeamName.getText().toString();
                    Team team = new Team(teamName);
                    Firebase teamPost = mTeamRef.push();
                    teamPost.setValue(team);
                    String key = teamPost.getKey();
                    mTeamRef.child(key).child("key").setValue(key);

                    SharedPreferences.Editor preferenceEditor = getSharedPreferences("team", 0).edit();
                    preferenceEditor.putString("team_name", teamName).apply();
                    preferenceEditor.putString("team_key", key).apply();

                    finish();
                    startActivity(new Intent(getApplication(), Dashboard.class));
                }
                else{
                    new AlertDialog.Builder(SignUp.this)
                            .setTitle(R.string.alert_invalidteamname_title)
                            .setMessage((R.string.alert_invalidteamname_msg))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

        // Clicking on an existing team will execute an alert asking the user if they want
        // to join that team.
        mTeamList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Team team = (Team)mTeamList.getItemAtPosition(position);
                final String name = team.getTeamName();
                final String key = team.getKey();
                AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this);
                alert.setTitle(R.string.alert_confirmteam_title);
                alert.setMessage(R.string.alert_confirmteam_msg);
                alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor preferenceEditor = getSharedPreferences("team", 0).edit();
                        preferenceEditor.putString("team_name", name);
                        preferenceEditor.putString("team_key", key);
                        preferenceEditor.commit();

                        finish();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    }
                });
                alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
