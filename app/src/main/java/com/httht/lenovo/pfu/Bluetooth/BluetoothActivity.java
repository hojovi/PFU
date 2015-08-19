package com.httht.lenovo.pfu.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.httht.lenovo.pfu.R;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {
    private TextView textView4;
    private Button button5;
    private ListView listView2;
    private MyAdapter adapter;
    private List<Pair<String,String>> devices;
    private BluetoothAdapter bluetoothAdapter;
    private MyBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        textView4=(TextView)findViewById(R.id.textView4);
        button5=(Button)findViewById(R.id.button5);
        listView2=(ListView)findViewById(R.id.listView2);
        devices=new LinkedList<>();
        adapter=new MyAdapter(devices);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        receiver=new MyBroadcastReceiver();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            if(!bluetoothAdapter.isEnabled()){
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }else{
                bluetoothOpened();
            }
        }
    }

    private void bluetoothOpened(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            bluetoothOpened();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str1=device.getName();
                String str2=device.getAddress();
                if(device.getBondState()==BluetoothDevice.BOND_BONDED){
                    str1+="(已配对）";
                }
                devices.add(new Pair<String, String>(str1,str2));
                adapter.notifyDataSetChanged();
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(BluetoothActivity.this,"搜索完成",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyAdapter extends BaseAdapter{
//        private String[] str1;
//        private String[] str2;
        private List<Pair<String,String>> list;
        public MyAdapter(List<Pair<String,String>> list){
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder=null;
            if(convertView==null){
                holder=new Holder();
                convertView=getLayoutInflater().inflate(R.layout.list_item_two_textview,null);
                holder.textView1=(TextView)convertView.findViewById(R.id.textView5);
                holder.textView2=(TextView)convertView.findViewById(R.id.textView6);
                convertView.setTag(holder);
            }else{
                holder= (Holder) convertView.getTag();
            }
            holder.textView1.setText(list.get(position).first);
            holder.textView2.setText(list.get(position).second);
            return  convertView;
        }

        class Holder{
            TextView textView1;
            TextView textView2;
        }
    }

    class BluetoothThread extends Thread{
        private String serverName;
        public BluetoothThread(String serverName){
            this.serverName=serverName;
        }
        @Override
        public void run() {
            try {
                BluetoothServerSocket serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(serverName, UUID.randomUUID());
                serverSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
