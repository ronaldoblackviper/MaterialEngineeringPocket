package com.example.android.tugasbesarteknikmaterial.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.tugasbesarteknikmaterial.Activities.curriculumActivity;
import com.example.android.tugasbesarteknikmaterial.Activities.infodosenActivity;
import com.example.android.tugasbesarteknikmaterial.Activities.lulusanActivity;
import com.example.android.tugasbesarteknikmaterial.Activities.visimisiActivity;
import com.example.android.tugasbesarteknikmaterial.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mtFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mtFragment extends Fragment {
    private CardView cardtop, cardright,cardleft1, cardleft2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public mtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static mtFragment newInstance(String param1, String param2) {
        mtFragment fragment = new mtFragment();
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
        View v = inflater.inflate(R.layout.fragment_mt, container, false);
        cardtop = v.findViewById(R.id.cardatas);
        cardleft1 = v.findViewById(R.id.cardkiri1);
        cardleft2 = v.findViewById(R.id.cardkiri2);
        cardright = v.findViewById(R.id.cardkanan);

        Animation animeBottomToTop = AnimationUtils.loadAnimation(getActivity(),R.anim.anime_bottom_to_top);
        Animation animeTopToBottom = AnimationUtils.loadAnimation(getActivity(),R.anim.anime_top_to_bottom);
        Animation animeRightToLeft = AnimationUtils.loadAnimation(getActivity(),R.anim.anime_right_to_left);
        Animation animeLeftToRight = AnimationUtils.loadAnimation(getActivity(),R.anim.anime_left_to_right);

        cardleft2.setAnimation(animeBottomToTop);
        cardtop.setAnimation(animeTopToBottom);
        cardleft1.setAnimation(animeLeftToRight);
        cardright.setAnimation(animeRightToLeft);

        cardtop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), visimisiActivity.class);
                startActivity(i);
            }
        });

        cardleft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), infodosenActivity.class);
                startActivity(i);
            }
        });

        cardleft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), lulusanActivity.class);
                startActivity(i);
            }
        });

        cardright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), curriculumActivity.class);
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
