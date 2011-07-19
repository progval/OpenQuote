package com.github.progval.openquote;

// Project specifics
import com.github.progval.openquote.sites.VdmActivity;

// User interface
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

// Android
import android.app.Activity;
import android.os.Bundle;

/**
 * Home screen.
 *
 * @author ProgVal
 */
public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Button buttonVdm = (Button)findViewById(R.id.buttonVdm);
        buttonVdm.setOnClickListener(openVdm);
    }

    public OnClickListener openVdm = new OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(Home.this, VdmActivity.class);
        	startActivity(intent);
        }
    };
}