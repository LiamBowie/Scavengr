package liambowie.scavenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    Button mBack;
    TextView mTeamName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBack = (Button)findViewById(R.id.button);
        mTeamName = (TextView)findViewById(R.id.textView_dashTeamName);

        // Set the team name in the top left corner of the dashboard
        SharedPreferences preferences = getSharedPreferences("team", 0);
        String teamName = preferences.getString("team_name", /*Should never appear*/ "Unassigned");
        mTeamName.setText(teamName);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("team", 0).edit();
                editor.remove("team_name").commit();
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });

    }

}
