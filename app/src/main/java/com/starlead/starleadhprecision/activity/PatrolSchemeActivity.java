package com.starlead.starleadhprecision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.starlead.starleadhprecision.Service.BluetoothConnect;
import com.starlead.starleadhprecision.GPSDatainfo;
import com.starlead.starleadhprecision.Loader.PatrolSchemeLoader;
import com.starlead.starleadhprecision.R;
import com.starlead.starleadhprecision.Service.BluetoothService;
import com.starlead.starleadhprecision.entity.PatrolScheme;
import com.starlead.starleadhprecision.entity.PatrolSchemeGroup;
import com.starlead.starleadhprecision.entity.mapper.PatrolSchemeGroupMapper;
import com.starlead.starleadhprecision.entity.mapper.PatrolSchemeMapper;
import com.starlead.starleadhprecision.widget.LoadingDialog;
import com.yarolegovich.mp.MaterialRightIconPreference;

import java.util.ArrayList;
import java.util.List;

public class PatrolSchemeActivity extends BaseActivity implements GPSDatainfo {
    private RecyclerView mPatrolSchemeRecyclerView;
    private PatrolSchemeAdapter mPatrolSchemeAdapter;
    private PatrolSchemeLoader mLoader;
    public static final int MESSAGE_PATROLSCHEMEGRUOP_DATA = 1;
    public static final int MESSAGE_PATROLSCHEME_DATA = 2;
    public List<PatrolScheme> mPatrolSchemeList;
    private Handler mHandler;
    private BluetoothConnect mBluetoothConnect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothConnect = new BluetoothConnect(PatrolSchemeActivity.this,this);
        mPatrolSchemeList = new ArrayList<PatrolScheme>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PATROLSCHEMEGRUOP_DATA:
                        List<PatrolSchemeGroup> patrolSchemeGroupList = (List<PatrolSchemeGroup>) msg.obj;
                        if (patrolSchemeGroupList != null) {
                            for (int i = 0; i < patrolSchemeGroupList.size(); i++) {
                                PatrolSchemeGroup group = patrolSchemeGroupList.get(i);
                                //获取patrolscheme
                                String url = "PatrolScheme?method=getPatrolSchemeBygroupId&groupid=" + group.getPatrolSchemeGroupId();
                                mLoader.queuePatrolScheme(new PatrolSchemeMapper(), url, MESSAGE_PATROLSCHEME_DATA);
                            }
                        }
                        break;
                    case MESSAGE_PATROLSCHEME_DATA:
                        List<PatrolScheme> patrolSchemelist = (List<PatrolScheme>) msg.obj;
                        mPatrolSchemeList.addAll(patrolSchemelist);
                        //更新列表
                        mPatrolSchemeAdapter.notifyItemInserted(mPatrolSchemeList.size() - 1);
                        hideLoading();
                        break;

                }
            }
        };
        setContentView(R.layout.activity_patrol_scheme);

        mPatrolSchemeRecyclerView = (RecyclerView) findViewById(R.id.patrolscheme_recyclerview);
        mPatrolSchemeAdapter = new PatrolSchemeAdapter(mPatrolSchemeList);
        mPatrolSchemeRecyclerView.setAdapter(mPatrolSchemeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PatrolSchemeActivity.this);
        mPatrolSchemeRecyclerView.setLayoutManager(linearLayoutManager);

        showLoading();

        String url = "SchemeGroup?method=getAllSchemeGroup";
        mLoader = new PatrolSchemeLoader(mHandler);
        mLoader.start();
        mLoader.getLooper();
        mLoader.queuePatrolScheme(new PatrolSchemeGroupMapper(), url, MESSAGE_PATROLSCHEMEGRUOP_DATA);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_device_item:      //弹出添加蓝牙设备对话框:
                mBluetoothConnect.showDeviceDialog();
                return true;
            case R.id.stop_item:
                if (mBluetoothConnect.getState() != BluetoothService.STATE_CONNECTED) {
                    Toast.makeText(this, "未连接蓝牙设备", Toast.LENGTH_SHORT).show();
                    return true;
                }
                mBluetoothConnect.stop();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothConnect.Detach();
    }

    @Override
    public void sendGPSData(LatLng latLng) {

    }


    class PatrolSchemeAdapter extends RecyclerView.Adapter<PatrolSchemeAdapter.ViewHolder> {

        private List<PatrolScheme> mPatrolSchemeList;

        class ViewHolder extends RecyclerView.ViewHolder {
            MaterialRightIconPreference mRightIconPreference;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mRightIconPreference = (MaterialRightIconPreference)itemView.findViewById(R.id.credit_PatrolScheme);
            }
        }

        public PatrolSchemeAdapter(List<PatrolScheme> patrolSchemeList) {
            mPatrolSchemeList = patrolSchemeList;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            View view = LayoutInflater.from(PatrolSchemeActivity.this).inflate(R.layout.patrolscheme_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            final PatrolScheme patrolScheme = mPatrolSchemeList.get(i);
            viewHolder.mRightIconPreference.setSummary(patrolScheme.getPatrolSchemeDescription());
            viewHolder.mRightIconPreference.setTitle(patrolScheme.getPatrolSchemeName());
            viewHolder.mRightIconPreference.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PatrolSchemeActivity.this,MarkerListMapActivity.class);
                    intent.putExtra("PatrolSchemeId", patrolScheme.getPatrolSchemeId());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mPatrolSchemeList.size();
        }


    }



}
