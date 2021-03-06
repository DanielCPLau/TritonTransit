package com.example.daniel.mts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListofStops.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListofStops#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListofStops extends ListFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String lineID;

    private OnFragmentInteractionListener mListener;

    public ListofStops() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListofStops.
     */
    // TODO: Rename and change types and number of parameters
    public static ListofStops newInstance(String param1, String param2) {
        ListofStops fragment = new ListofStops();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // get permission to access network
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.lines_fragment,container, false);
        DisplayListOfStops activity = (DisplayListOfStops)getActivity();

        // get line id from activity
        String id = activity.getId();
        lineID = id;
        Line lin = new Line(id);
        Log.d("In regular Fragment", "" + lin.directionId);

        // use line id to find and display list of stops
        String[] stopIds = lin.listOfStopsId;
        ArrayAdapter<String> adapter = new ListofStops.stopAdapter(getActivity(),R.layout.stoplist_rowlayout, R.id.stoptxt,stopIds);
        setListAdapter(adapter);
        setRetainInstance(true);

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onListItemClick(ListView view1, View view, int position, long id)
    {
        String stopId = (String)getListAdapter().getItem(position);

        // start new activity to show stop info based on stop pressed
        Intent i = new Intent(getActivity(), StopActivity.class);

        // pass in info about the stop
        Bundle dataBundle = new Bundle();
        dataBundle.putString("stopId", stopId);
        dataBundle.putString("lineId", lineID);
        i.putExtras(dataBundle);
        startActivity(i);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     *  Sources of help : http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
     */
    public class stopAdapter extends ArrayAdapter {
        public stopAdapter(Context context, int resource, int textViewResourceId, Object[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);

            // get stop info from stop id
            String stopid = (String)getItem(position);
            Stop stopInfo = new Stop(stopid, lineID);

            // update respective text in row
            TextView stopText = (TextView) view.findViewById(R.id.stoptxt);
            stopText.setText(stopInfo.name);

            // alternating grey and white row backgrounds
            if (position % 2 == 1) {
                view.setBackgroundColor(Color.WHITE);
            } else {
                view.setBackgroundColor(Color.parseColor("#eff5ff"));
            }

            return view;
        }
    }
}
