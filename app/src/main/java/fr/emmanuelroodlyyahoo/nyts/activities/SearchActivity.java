package fr.emmanuelroodlyyahoo.nyts.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import cz.msebera.android.httpclient.Header;
import fr.emmanuelroodlyyahoo.nyts.R;
import fr.emmanuelroodlyyahoo.nyts.adapters.ArticleArrayAdapter;
import fr.emmanuelroodlyyahoo.nyts.model.Article;
import fr.emmanuelroodlyyahoo.nyts.model.MyDialog;

import static android.os.Build.VERSION_CODES.M;

public class SearchActivity extends AppCompatActivity {

    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter myAdapter;
    MyDialog d; //appel d'un fragment personnalise
    FragmentManager fm;
    android.widget.SearchView searchView;
    boolean chargement = false;
    static int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_ly);
        getSupportActionBar().setLogo(R.mipmap.ic_ly);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupsViews();
        fm = getSupportFragmentManager();
        d = new MyDialog();
    }

    public void setupsViews(){
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        myAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(myAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent next = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = articles.get(i);
                next.putExtra("article", article);
                Toast.makeText(SearchActivity.this, "Page loading", Toast.LENGTH_SHORT).show();
                startActivity(next);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();
        searchView = (android.widget.SearchView) menuItem.getActionView();
        searchView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                d.show(fm, "Settings");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(SearchActivity.this, "My texte " + s, Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "Recherche de : " + query, Toast.LENGTH_SHORT).show();
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
                String key = "a341c7b04a4846318b10b134e35b5c6c";
                RequestParams params = new RequestParams();
                params.put("api-key", key);
                params.put("page", page);
                if(isNetworkAvailable()){

                    if(!s.isEmpty() && s != null) {
                        params.put("q", s);//ajout des parametres de recherche si non nul
                        myAdapter.clear();
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("SearchParams", Context.MODE_PRIVATE);
                        String sort = prefs.getString("sort", null);
                        if (sort == "newest") {
                            params.put("sort", "newest");
                        } else if (sort == "oldest") {
                            params.put("sort", "oldest");
                        }

                        int year = prefs.getInt("year", -1);
                        int month = prefs.getInt("month", -1);
                        int day = prefs.getInt("day", -1);

                        if (!(year == -1 || month == -1 || day == -1)) {
                            month++;
                            String one = month < 10 ? "0" : "";
                            String two = day < 10 ? "0" : "";
                            String begin = year + one + month + two + day;
                            params.put("begin_date", begin);
                        }

                        String filter = "";
                        if (prefs.getBoolean("none", false)) {
                            filter = "\"None\"";
                        }

                        if (prefs.getBoolean("foreign", false)) {
                            filter = filter + " \"Foreign\"";
                        }

                        if (prefs.getBoolean("sport", false)) {
                            filter = filter + " \"Sports\"";
                        }

                        if (filter != null && !filter.isEmpty()) {
                            params.put("fq", "news_desk:(" + filter + ")");
                        }

                        client.get(url, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                JSONArray articleJSONResults = null;
                                //Log.d("DEBUG", response.toString());

                                try {
                                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                                    chargement = true;
                                    myAdapter.clear();
                                    myAdapter.addAll(Article.fromJSONArray(articleJSONResults));
                                    myAdapter.notifyDataSetChanged();
                                    Log.d("DEBUG", articles.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(SearchActivity.this, "Champ se saisie invalide", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SearchActivity.this, "No active connection | Use WiFi or Mobile data", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.clearFocus();
        //return  true;
        return true;
    }




    public void showDialog(MenuItem item) {
        d.show(fm, "Settings");
    }

    //methode qui va retournee les valeurs du fragment de type myDialog
    public void getValuesFromFragment(int jours, int mois, int annee , String r_spinner, boolean none, boolean foreign, boolean national){
        //Toast.makeText(this, "parametres: " + jours +"/"+ mois +"/"+ annee +"/" + " | " + r_spinner + " | " + String.valueOf(none) + " | " + String.valueOf(foreign) + " | " + String.valueOf(national) + " | ", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor transfert = getApplicationContext().getSharedPreferences("SearchParams", Context.MODE_PRIVATE).edit();
        transfert.putInt("year", annee);
        transfert.putInt("month", mois);
        transfert.putInt("day", jours);
        transfert.putString("sort", r_spinner);
        transfert.putBoolean("none", none);
        transfert.putBoolean("foreign", foreign);
        transfert.putBoolean("national", national);
        transfert.commit();
        page = 0;
        myAdapter.clear();
        searchView.callOnClick();
        myAdapter.notifyDataSetChanged();
    }

    public void onFilterClick(MenuItem item) {
        d.show(fm, "Settings");
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
