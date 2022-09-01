package com.example.bamx_app

import android.widget.EditText
import android.content.Intent
import com.paypal.android.sdk.payments.PayPalService
import android.os.Bundle
import com.example.bamx_app.paypalFragment
import com.example.bamx_app.R
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PaymentActivity
import android.app.Activity
import com.paypal.android.sdk.payments.PaymentConfirmation
import com.example.bamx_app.PaymentDetails
import org.json.JSONException
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.bamx_app.Config.Config
import com.paypal.android.sdk.payments.PayPalConfiguration
import java.math.BigDecimal

/**
 * A simple [Fragment] subclass.
 * Use the [paypalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class paypalFragment : Fragment() {
    var btnDonaAhora: Button? = null
    var edtAmount: EditText? = null
    var amount = ""
    override fun onDestroy() {
        requireActivity().stopService(Intent(this@paypalFragment.activity, PayPalService::class.java))
        super.onDestroy()
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
            val intent = Intent(this@paypalFragment.activity, PayPalService::class.java)
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
            requireActivity().startService(intent)
            btnDonaAhora = requireView().findViewById<View>(R.id.btnDonaAhora) as Button
            edtAmount = requireView().findViewById<View>(R.id.edtAmount) as EditText
            btnDonaAhora!!.setOnClickListener { processPayment() }
        }
    }

    private fun processPayment() {
        amount = edtAmount!!.text.toString()
        val payPalPayment = PayPalPayment(
            BigDecimal(amount), "MXN",
            "Dona a BAMX", PayPalPayment.PAYMENT_INTENT_SALE
        )
        val intent = Intent(this@paypalFragment.activity, PaymentActivity::class.java)
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val confirmation =
                    data!!.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirmation != null) {
                    try {
                        val paymentDetails = confirmation.toJSONObject().toString(4)
                        startActivity(
                            Intent(this@paypalFragment.activity, PaymentDetails::class.java)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        )
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) Toast.makeText(
                this@paypalFragment.activity,
                "Cancelar",
                Toast.LENGTH_SHORT
            ).show()
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) Toast.makeText(
            this@paypalFragment.activity,
            "Invalido",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paypal, container, false)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        const val PAYPAL_REQUEST_CODE = 7171
        private val config = PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID)
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment paypalFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): paypalFragment {
            val fragment = paypalFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}