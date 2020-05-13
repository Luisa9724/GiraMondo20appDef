package com.example.giramondo20app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.giramondo20app.Model.AccommodationModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentFilteredResearchResults extends Fragment implements OnTaskCompletedResearchResults {

    List<AccommodationModel> resultsAcm;

    ProgressBar progressBar;

    ListView listView;
    private AdapterAccommodations mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_filtered_resurch_results, container, false);
        listView = rootView.findViewById(R.id.accommodationList);
        progressBar = rootView.findViewById(R.id.progressBar);
        return rootView;
    }


    @Override
    public void sendErrorResponse() {
        new AlertDialog.Builder(getContext())
                .setTitle("Errore")
                .setMessage("Nessun risultato")

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getFragmentManager().popBackStack();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onTaskComplete(List<AccommodationModel> results) {

        resultsAcm = results;

        mAdapter = new AdapterAccommodations(getContext(),results);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentAccommodationOverview fragOverview = new FragmentAccommodationOverview();

                AccommodationModel entry = (AccommodationModel) parent.getItemAtPosition(position);
                AsyncAccommodationPhotos task = new AsyncAccommodationPhotos(fragOverview,entry);
                task.execute(entry.getName());

                //to set flag in accommodationOverview to indicate when the tabs of viewpager are visualized
                //at first the tabs are not visualized yet so the flag is false
                SharedPreferences pref = getActivity().getSharedPreferences("acm", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("firstCall",false);
                editor.commit();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragOverview,"frag_overview");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.option_name:
                Collections.sort(resultsAcm, new Comparator<AccommodationModel>() {
                    @Override
                    public int compare(AccommodationModel o1, AccommodationModel o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.option_price:
                Collections.sort(resultsAcm, new Comparator<AccommodationModel>() {
                    @Override
                    public int compare(AccommodationModel o1, AccommodationModel o2) {
                        return Integer.compare(o1.getPrice(), o2.getPrice());
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.option_rating:
                Collections.sort(resultsAcm, new Comparator<AccommodationModel>() {
                    @Override
                    public int compare(AccommodationModel o1, AccommodationModel o2) {
                        return -1*(Float.compare(o1.getRating(), o2.getRating()));
                    }
                });
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
