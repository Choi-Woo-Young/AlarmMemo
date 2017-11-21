package it.feio.android.alarmmemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import it.feio.android.alarmmemo.dummy.DummyContent;
import it.feio.android.alarmmemo.utils.Constants;



public class AlarmsActivity extends BaseActivity implements AlarmsListFragment.OnListFragmentInteractionListener{

    public final String FRAGMENT_LIST_TAG = "fragment_list";

    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        initUI();
        init();
    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onNavigateUp());

    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();

        if (mFragmentManager.findFragmentByTag(FRAGMENT_LIST_TAG) == null) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.alarrm_fragment_container, new AlarmsListFragment(), FRAGMENT_LIST_TAG).commit();
        }

        // Handling of Intent actions
        //handleIntents();
    }

    @Override
    public void onStart() {
        ((OmniNotes) getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
        super.onStart();
    }


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.d(Constants.TAG, "onListFragmentInteraction");
    }
}
