package com.example.olayg.spectraforcechallenge.view.mainactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.firstmodule.JNILib;
import com.example.olayg.spectraforcechallenge.MyApp;
import com.example.olayg.spectraforcechallenge.R;
import com.example.olayg.spectraforcechallenge.util.DoneOnEditorActionListener;
import com.example.olayg.spectraforcechallenge.view.base.BaseActivity;
import com.example.olayg.spectraforcechallenge.view.triangleactivity.TriangleActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainActivityContract.View {

    private static final String MY_MESSAGE = "message";

    @BindView(R.id.display_text)
    TextView displayText;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @Inject
    MainActivityPresenter presenter;
    @Inject
    SharedPreferences preferences;

    JNILib JNILib;
    String myMessage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activateToolbar(false);
        init();
    }

    private void init() {
        JNILib = new JNILib();
        etMessage.setOnEditorActionListener(new DoneOnEditorActionListener());
        DaggerMainActivityComponent.builder()

                .netComponent(((MyApp) getApplicationContext()).getNetComponent())
                .mainActivityModule(new MainActivityModule(this))
                .build().inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("Saved to preferences is " + preferences.getString(MY_MESSAGE, "Nothing here"));
        displayText.setText(preferences.getString(MY_MESSAGE, "Nothing here"));
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

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Oops something is wrong!")
                .setMessage(error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
/*                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })*/
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @OnClick({R.id.btnSendToJni, R.id.ibRewind, R.id.ibPlay, R.id.ibFoward, R.id.ibDrawShape})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSendToJni:
                myMessage = etMessage.getText().toString();
                preferences.edit().putString(MY_MESSAGE, myMessage).apply();
                Timber.d("Saved to preferences is " + preferences.getString(MY_MESSAGE, "Nothing here"));
                sendMessage();  break;
            case R.id.ibRewind:
                FancyToast.makeText(this, JNILib.toastRewind(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                break;
            case R.id.ibPlay:
                FancyToast.makeText(this, JNILib.toastPlay(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                break;
            case R.id.ibFoward:
                FancyToast.makeText(this, JNILib.toastFastForward(), FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                break;
            case R.id.ibDrawShape:
                startActivity(new Intent(this, TriangleActivity.class));
                break;
        }
    }

    public native void sendMessageToJni(String message);

    private void sendMessage() {
        if (!myMessage.isEmpty()) {
            sendMessageToJni(myMessage);
        } else {
            showError("No Message was provided try sending a message");
        }
        etMessage.getText().clear();
    }

    private void jniCallback(String message) {
        displayText.setText(message);
    }
}
