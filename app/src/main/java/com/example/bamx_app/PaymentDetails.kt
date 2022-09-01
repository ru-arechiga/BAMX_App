package com.example.bamx_app

import android.widget.TextView
import android.os.Bundle
import org.json.JSONObject
import org.json.JSONException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentDetails : Fragment() {
    var txtId: TextView? = null
    var txtAmount: TextView? = null
    var txtStatus: TextView? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
            txtId = requireView().findViewById<View>(R.id.txtId) as TextView
            txtAmount = requireView().findViewById<View>(R.id.txtAmount) as TextView
            txtStatus = requireView().findViewById<View>(R.id.txtStatus) as TextView
            val intent = requireActivity().intent
            try {
                val jsonObject = JSONObject(intent.getStringExtra("PaymentDetails"))
                showDetails(
                    jsonObject.getJSONObject("response"),
                    intent.getStringExtra("PaymentAmount")
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun showDetails(response: JSONObject, paymentAmount: String?) {
        try {
            txtId!!.text = response.getString("id")
            txtStatus!!.text = response.getString("state")
            txtAmount!!.text = response.getString(String.format("$%s, paymentAmount"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_details, container, false)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PaymentDetails.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): PaymentDetails {
            val fragment = PaymentDetails()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}