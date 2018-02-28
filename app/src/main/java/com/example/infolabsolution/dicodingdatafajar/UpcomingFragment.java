package com.example.infolabsolution.dicodingdatafajar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.infolabsolution.dicodingdatafajar.MainActivity;
import com.example.infolabsolution.dicodingdatafajar.R;
import com.example.infolabsolution.dicodingdatafajar.MovieAdapter;
import com.example.infolabsolution.dicodingdatafajar.MovieItems;
import com.example.infolabsolution.dicodingdatafajar.UpcomingLoader;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    private static final String TAG = "TAG_UpComing";
    private MovieAdapter adapter;
    @BindView(R.id.tv_not_found)
    TextView tvNotFound;
    @BindView(R.id.rcv_movie)
    RecyclerView rcvlistMovie;
    @BindView(R.id.progress)
    ProgressBar loading;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(2,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        ButterKnife.bind(this, view);

        adapter = new MovieAdapter(getActivity());

        rcvlistMovie.setVisibility(View.INVISIBLE);

        if (savedInstanceState != null) {
            setToolbarTitle();
            getActivity().getSupportLoaderManager().initLoader(2,null,this);
        }
        return view;
    }

    private void setToolbarTitle() {
        String[] activityTitles = getResources().getStringArray(R.array.nav_titleactivity);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(activityTitles[2]);
    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader Upcoming: 1");
        loading.setVisibility(View.VISIBLE);
        return new UpcomingLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        Log.d(TAG, "onLoadFinished Upcoming: 1");
        if (data.size()!=0) {
            loading.setVisibility(View.GONE);
            rcvlistMovie.setVisibility(View.VISIBLE);
            adapter.setListMovie(data);
            rcvlistMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcvlistMovie.setAdapter(adapter);
        }else {
            loading.setVisibility(View.GONE);
            rcvlistMovie.setVisibility(View.INVISIBLE);
            tvNotFound.setVisibility(View.VISIBLE);
            tvNotFound.setText(getResources().getString(R.string.error_load_message));
            tvNotFound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportLoaderManager().restartLoader(2,null,UpcomingFragment.this);
                    tvNotFound.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setListMovie(null);

    }
}
