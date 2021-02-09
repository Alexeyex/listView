package com.example.listviewchermyanin;

import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static @StyleRes
    final int themeRes = R.style.AppTheme;
    private SharedPreferences mySharedPref;
    private final static String TEXT_PRGRPH = "note_text";
    private List<Map<String, String>> simpleAdapter;
    private String mText;
    private ListView mListView;
    String[] arrayContent;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(themeRes);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listView);
        mText = getString(R.string.large_text);
        saveText();
        init();
        loadText();
    }

    private void init() {
        arrayContent = mySharedPref.getString(TEXT_PRGRPH, mText).split("\n\n");
        simpleAdapter = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for (int i = 0; i < arrayContent.length; i++) {
            map = new HashMap<String, String>();
            map.put("text", arrayContent[i]);
            map.put("length", Integer.toString(arrayContent[i].length()));
            simpleAdapter.add(map);
        }

        String[] from = {"text", "length"};
        int[] to = {R.id.text, R.id.lengthText};

        SimpleAdapter adapter = new SimpleAdapter(this, simpleAdapter, R.layout.inside_listview, from, to);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                simpleAdapter.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadText() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                init();
            }
        });
    }

    private void saveText() {
        mySharedPref = getSharedPreferences("MyText", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = mySharedPref.edit();
        myEditor.putString(TEXT_PRGRPH, mText);
        myEditor.apply();
    }
}