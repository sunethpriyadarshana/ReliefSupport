package disaster.com.disastermanagement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sumudu on 5/29/2017.
 */

public class ViewAllNeedsActivity extends Activity {
    NetworkStatChecker n = new NetworkStatChecker();
    DBOperations dbOperations = new DBOperations();
    Data data = new Data();

    Context context;
    TextView hiddenLayout;
    ListView viewAllRequirementsListView;
    SwipeRefreshLayout swiper;

    ImageView backButton;

    String lan = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_requirements_layout);
        context = ViewAllNeedsActivity.this;

        SharedPreferences sp = getSharedPreferences("language", 0);
        lan += sp.getString("lan", "1");

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setColorSchemeColors(data.getSWIPER_COLOR_LIST());
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiper.setRefreshing(true);
                new GetAllNeedDetails().execute();
            }
        });

        hiddenLayout = (TextView) findViewById(R.id.hiddenLayout);
        viewAllRequirementsListView = (ListView) findViewById(R.id.viewAllRequirementsListView);
        hiddenLayout = (TextView) findViewById(R.id.hiddenLayout);

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonOption();
            }
        });

        new GetAllNeedDetails().execute();
    }

    @Override
    public void onBackPressed() {
        backButtonOption();
    }

    public void backButtonOption(){
        Intent i = new Intent(context, MainMenuActivity.class);
        startActivity(i);
        finish();
    }

    class GetAllNeedDetails extends AsyncTask<String, Void, String[][]> {
        boolean internetAvailable = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            switch (lan){
                case "1" :
                    progressDialog = ProgressDialog.show(context, "Loading", "Please wait");
                    break;

                case "2" :
                    progressDialog = ProgressDialog.show(context, "දත්ත ලැබෙමින් පවතී", "මඳක් ඉවසන්න");
                    break;

                case "3" :
                    progressDialog = ProgressDialog.show(context, "Loading", "தயவு செய்து காத்திருங்கள்");
                    break;
            }
        }

        @Override
        protected String[][] doInBackground(String... urls) {
            if(n.isConnected(context)){
                this.internetAvailable = true;
                String[][] res = dbOperations.getAllNeeds();
                return res;
            }
            else{
                this.internetAvailable = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if(result != null){
                try{
                    String[] needIDList = result[0];
                    String[] needNameList = result[1];
                    String[] needTelephoneList = result[2];
                    String[] needCityList = result[3];
                    String[] needTextList = result[4];
                    String[] needHeadCountListList = result[5];
                    String[] needCreatedAtList = result[6];
                    String[] needUpdatedAtList = result[7];
                    String[] needIdentifierList = result[8];
                    String[] needAddressList = result[9];

                    CustomAllRequirementsAdapter customAllRequirementsAdapter
                            = new CustomAllRequirementsAdapter(
                            context,
                            ViewAllNeedsActivity.this,

                            needIDList,
                            needNameList,
                            needTelephoneList,
                            needAddressList,
                            needCityList,
                            needTextList,
                            needHeadCountListList,
                            needCreatedAtList,
                            needUpdatedAtList,
                            needIdentifierList);

                    customAllRequirementsAdapter.notifyDataSetChanged();
                    viewAllRequirementsListView.setAdapter(customAllRequirementsAdapter);

                    hiddenLayout.setVisibility(View.GONE);
                    viewAllRequirementsListView.setVisibility(View.VISIBLE);
                }
                catch (Exception e){
                    if(this.internetAvailable){
                        hiddenLayout.setVisibility(View.VISIBLE);
                        viewAllRequirementsListView.setVisibility(View.GONE);
                        switch (lan){
                            case "1" :
                                Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                                break;

                            case "2" :
                                Toast.makeText(context, "දත්ත නොමැත", Toast.LENGTH_SHORT).show();
                                break;

                            case "3" :
                                Toast.makeText(context, "தகவல் எதனையும் பெறமுடியவில்லை", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    else{
                        hiddenLayout.setVisibility(View.VISIBLE);
                        viewAllRequirementsListView.setVisibility(View.GONE);
                        switch (lan){
                            case "1" :
                                Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                break;

                            case "2" :
                                Toast.makeText(context, "අන්තර්ජාල සම්බන්ධතාවය පරික්ෂා කරන්න", Toast.LENGTH_SHORT).show();
                                break;

                            case "3" :
                                Toast.makeText(context, "இணைய இணைப்பை சரிபார்க்கவும்", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            }

            else if(this.internetAvailable){
                hiddenLayout.setVisibility(View.VISIBLE);
                viewAllRequirementsListView.setVisibility(View.GONE);
                switch (lan){
                    case "1" :
                        Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
                        break;

                    case "2" :
                        Toast.makeText(context, "දත්ත නොමැත", Toast.LENGTH_SHORT).show();
                        break;

                    case "3" :
                        Toast.makeText(context, "தகவல் எதனையும் பெறமுடியவில்லை", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            else{
                hiddenLayout.setVisibility(View.VISIBLE);
                viewAllRequirementsListView.setVisibility(View.GONE);
                switch (lan){
                    case "1" :
                        Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        break;

                    case "2" :
                        Toast.makeText(context, "අන්තර්ජාල සම්බන්ධතාවය පරික්ෂා කරන්න", Toast.LENGTH_SHORT).show();
                        break;

                    case "3" :
                        Toast.makeText(context, "இணைய இணைப்பை சரிபார்க்கவும்", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            try{
                progressDialog.dismiss();
                swiper.setRefreshing(false);
            }
            catch(Exception ignored){}
        }
    }
}
