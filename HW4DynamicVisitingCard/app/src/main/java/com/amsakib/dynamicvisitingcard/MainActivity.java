package com.amsakib.dynamicvisitingcard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void visitOrbitaxWebiste(View view) {
        String url = "https://www.orbitax.com";
        visitWebsite(url);
    }

    private void visitWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void visitUserWebsite(View view) {
        String url = "https://www.amsakib.com";
        visitWebsite(url);
    }

    public void onClickMobileNumber(View view) {
        String number =getResources().getString(R.string.mobile_number);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" +number));
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        String email_address = getResources().getString(R.string.email_id);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email_address);
        startActivity(intent);
    }

    public void onClickAddress(View view) {
        Uri gmmIntentUri = Uri.parse("geo:23.778847835035577,90.39875826594019?q=Orbitax+Bangladesh+Limited");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}