package com.example.bamx_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bamx_app.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link paypalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class paypalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    Button btnDonaAhora;
    EditText edtAmount;
    String amount="";

    @Override
    public void onDestroy(){
        getActivity().stopService(new Intent(paypalFragment.this.getActivity(), PayPalService.class));
        super.onDestroy();
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public paypalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment paypalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static paypalFragment newInstance(String param1, String param2) {
        paypalFragment fragment = new paypalFragment();
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

            Intent intent = new Intent(paypalFragment.this.getActivity(), PayPalService.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            getActivity().startService(intent);


            btnDonaAhora = (Button)getView().findViewById(R.id.btnDonaAhora);
            edtAmount = (EditText)getView().findViewById(R.id.edtAmount);
            btnDonaAhora.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    processPayment();
                }
            });
        }
    }

    private void processPayment() {
        amount = edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "MXN",
                "Dona a BAMX", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(paypalFragment.this.getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paypal, container, false);
    }
}