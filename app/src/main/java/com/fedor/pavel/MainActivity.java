package com.fedor.pavel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.fedor.pavel.adapters.ColorListRvAdapter;
import com.fedor.pavel.models.ColorModel;
import com.fedor.pavel.tespfsoft.R;
import com.fedor.pavel.util.XmlParseUtil;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.rv_color_list)
    RecyclerView mRvColorList;

    private ColorListRvAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadData();
    }

    private void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initColorList();
    }

    private void initColorList() {
        mAdapter = new ColorListRvAdapter(this);
        mRvColorList.setLayoutManager(new LinearLayoutManager(this));
        mRvColorList.setAdapter(mAdapter);
    }

    private void loadData() {
        showProgress();
        try {
            XmlParseUtil.parseColors(getAssets().open("colors.xml"), new XmlParseUtil.ParseDataListener() {
                @Override
                public void onDataParse(List<ColorModel> colorModels) {
                    mAdapter.addAll(colorModels);
                    dismissProgress();
                }

                @Override
                public void onDataParseError(String errorMessage) {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            dismissProgress();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void createProgress() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
    }

    private void showProgress() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
