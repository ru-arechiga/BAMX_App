package com.example.bamx_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.jar.JarException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentDetails extends Fragment {

    TextView txtId, txtAmount, txtStatus;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PaymentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentDetails newInstance(String param1, String param2) {
        PaymentDetails fragment = new PaymentDetails();
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

            txtId = (TextView)getView().findViewById(R.id.txtId);
            txtAmount = (TextView)getView().findViewById(R.id.txtAmount);
            txtStatus = (TextView)getView().findViewById(R.id.txtStatus);

            Intent intent = getActivity().getIntent();

            try{
                JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
                showDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText(response.getString(String.format("$%s, paymentAmount")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }
}