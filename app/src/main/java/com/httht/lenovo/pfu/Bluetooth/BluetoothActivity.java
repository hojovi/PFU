package com.httht.lenovo.pfu.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.httht.lenovo.pfu.R;

import java.util.LinkedList;
import java.util.List;

@Deprecated
public class BluetoothActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BLUETOOTH=1;
    private static final int REQUEST_DISCOVERY_BLUETOOTH=2;
    private static final int SOCKET_WRITE = 3;
    private static final int SOCKET_READ = 4;

    private TextView textView4;
    private Button button5;
    private ListView listView2;
    private MyAdapter adapter;
    private List<BluetoothDevice> devices;
    private BluetoothAdapter bluetoothAdapter;
    private MyBroadcastReceiver receiver;

    private List<BluetoothSocket> curSockets;
    private BluetoothServerSocket serverSocket;

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
        curSockets=new LinkedList<>();
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter!=null){
            if(!bluetoothAdapter.isEnabled()){
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
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
        if(requestCode==REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                bluetoothOpened();
            }else{
                Toast.makeText(this,"打开蓝牙失败",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_DISCOVERY_BLUETOOTH){
            if(resultCode==RESULT_OK){

            }else{
                Toast.makeText(this,"应用没有发现设备的权限",Toast.LENGTH_SHORT).show();
            }
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
                devices.add(device);
                adapter.notifyDataSetChanged();
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Toast.makeText(BluetoothActivity.this,"搜索完成",Toast.LENGTH_SHORT).show();
            }else if(BluetoothDevice.ACTION_NAME_CHANGED.equals(action)){

            }else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){

            }
        }
    }

    class MyAdapter extends BaseAdapter{
//        private String[] str1;
//        private String[] str2;
        private List<BluetoothDevice> list;
        public MyAdapter(List<BluetoothDevice> list){
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
            holder.textView1.setText(list.get(position).getName());
            holder.textView2.setText(list.get(position).getAddress());
            return  convertView;
        }

        class Holder{
            TextView textView1;
            TextView textView2;
        }
    }

//    class BluetoothThread extends Thread{
//        private String serverName;
//
//        public BluetoothThread(String serverName){
//            this.serverName=serverName;
//        }
//        @Override
//        public void run() {
//            try {
//                BluetoothSocket bs;
//                while(true){
//                    serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(serverName, UUID.randomUUID());
//                    bs=serverSocket.accept();
//                    if(bs==null){
//                        break;
//                    }
//                    curSockets.add(bs);
//                };
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    class BluetoothSRThread extends Thread{
//        private BluetoothSocket socket;
//        private String path;
//        private int action;
//        private byte[] bytes;
//
//        public BluetoothSRThread(BluetoothSocket socket,String path,int action){
//            this.socket=socket;
//            this.path=path;
//            this.action=action;
//            bytes=new byte[1024*5];
//        }
//
//        @Override
//        public void run() {
//            try {
//                switch (action) {
//                    case SOCKET_WRITE:
//                        OutputStream os = socket.getOutputStream();
//                        FileInputStream fis=new FileInputStream(path);
//                        int len=-1;
//                        while((len=fis.read(bytes))!=-1){
//                            os.write(bytes,0,len);
//                        }
//                        fis.close();
//                        os.close();
//                        break;
//                    case SOCKET_READ:
//                        InputStream is=socket.getInputStream();
//                        FileOutputStream fos=new FileOutputStream(path);
//                        int len1=-1;
//                        while((len1=is.read(bytes))!=-1){
//                            fos.write(bytes,0,len1);
//                        }
//                        is.close();
//                        fos.close();
//                        break;
//                }
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
