package com.example.sunlei.chatui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();
    private String content_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initMsgs();
        adapter = new MsgAdapter(MainActivity.this,R.layout.msg_item,msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_str = inputText.getText().toString();


                if(!"".equals(content_str)){
                    Msg msg4 = new Msg(content_str,Msg.TYPE_SENT);
                    msgList.add(msg4);
                    adapter.notifyDataSetChanged();
                    getJSONVolly();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                }
            }
        });


    }

    private void initMsgs(){
        Msg msg1= new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
        Msg msg2=new Msg("Hello.Who is that",Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom.NICE talking to you",Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }
    public  void getJSONVolly() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String JSONUrl = "http://www.tuling123.com/openapi/api?key=43076b1d1cc4eda6d6e2b7ee903e18b4&info="+content_str;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSONUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
//                            System.out.println(jsonObject.getString("code"));
//                            System.out.println(jsonObject.getString("text"));
                            Msg msg = new Msg(jsonObject.getString("text"),Msg.TYPE_RECEIVED);
                            msgList.add(msg);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("有问题");
            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
