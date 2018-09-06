package com.example.utente.Client;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Classe principale che modella le varie interfacce.
 * @author Lorenzo
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Vedi di più su {@link SectionsPagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * Attributo che indica l'indirizzo ip del server a cui ci si vuole connettere.
     */
    static String ip="localhost";
    /**
     * Attributo che indica la porta del server.
     */
    static int port=8080;
    /**
     * Manager del layout che consente di switchare a destra e sinistra tra le pagine del'interfaccia.
     */
    private ViewPager mViewPager;

    /**
     * Override del metodo principale che inizializza le componenti grafiche.
     * @param savedInstanceState Attributo che contiene lo stato dell'applicazione.
     */
    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        }


        /**
         * Aggiunge il menu delle opzioni all'activity.
         * @param menu Il menu da mostrare.
         * @return True se vuoi mostrare il menu (come in questo caso), false altrimenti.
         */
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

    /**
     * Questo metodo è chiamato quando uno degli elementi del menu viene selezionato.
     * @param item L'item che è stato selezionato.
     * @return False per permettere di procedere il normale conseguimento, true per eseguire il processo qui.
     */
    @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                return true;
            }
            if (id==R.id.action_config){
                openDialog();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    /**
     * Mostra il popup delle impostazioni.
     */
    public void openDialog(){
            Config config=new Config();
            config.show(getSupportFragmentManager(),"Config");
        }

        /**
         * Un "segnaposto" per il fragment contenente una semplice View.
         */
        public static class PlaceholderFragment extends Fragment {
            /**
             * Argomento del fragment che rappresenta il numero di sezioni per questo fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

            /**
             * Costruttore di default.
             */
            public PlaceholderFragment() {
            }

            /**
             * Restituisce una nuova istanza di questo fragment per il numero di section specificato.
             *
             * @param sectionNumber Il numero di section.
             */
            public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
            }

            /**
             * Chiamato per avere la user interface istanziata per questo fragment.
             * @param inflater Oggetto di tipo LayoutInflater che può essere usato per riempire le viste nel fragment.
             * @param container Se non-null è il contenitore padre in cui si trova il fragment.
             * @param savedInstanceState Se non-null questo fragment viene ricostruito da uno stato salvato precedente, cioè questo.
             * @return La View per l'user interface del fragment.
             */
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
        }

        /**
         * Un {@link FragmentPagerAdapter} che restituisce un fragment corrispondente a una delle sezioni/tabelle/pagine.
         */
        public class SectionsPagerAdapter extends FragmentPagerAdapter {
            /**
             * Costruttore di classe.
             * @param fm Il FragmentManager con il quale si vuole inizializzare il fragment.
             */
            public SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            /**
             * Restituisce l'elemento nella posizione corrispondente.
             * @param position Posizione in cui si trova l'elemento.
             * @return Elemento corrispondente alla posizione data.
             */
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = new Mining();
                        break;
                    case 1:
                        fragment = new LoadFile();
                        break;
                }
                return fragment;
            }

            /**
             * Restituisce il numero di pagine.
             * @return Restituisce il numero totale di pagine.
             */
            @Override
            public int getCount() {
                // Show 2 total pages.
                return 2;
            }
        }
    }
