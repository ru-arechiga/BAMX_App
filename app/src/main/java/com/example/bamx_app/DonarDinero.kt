package com.example.bamx_app

import android.app.Application
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import com.paypal.checkout.shipping.OnShippingChange
import com.paypal.pyplcheckout.home.view.interfaces.PayPalCheckoutButtonClickedListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DonarDinero.newInstance] factory method to
 * create an instance of this fragment.
 */
class DonarDinero : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var database : DatabaseReference
    private var check: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PayPalCheckout.registerCallbacks(
            onApprove = OnApprove { approval ->
            approval.orderActions.capture { captureOrderResult ->
                Log.i("CaptureOrder", "Order successfully captured: $captureOrderResult")
            }
        },
            onCancel = OnCancel {
                // Optional callback for when a buyer cancels the paysheet
            },
            onError = OnError { errorInfo ->
                // Optional error callback
            },
            onShippingChange = OnShippingChange { shippingChangeData, shippingChangeActions ->
                // Optional onShippingChange callback. See update shipping section for more details.
            }
        )
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_donar_dinero, container, false)
        val botton: Button = view.findViewById(R.id.botonEnviar)
        val boton: Button = view.findViewById(R.id.botonDonarOffline)
        val bottton: Button = view.findViewById(R.id.botonDonarAhora)
        botton.setOnClickListener(this)
        boton.setOnClickListener(this)
        bottton.setOnClickListener(this)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.botonEnviar -> {
                val monto: TextInputEditText = requireView().findViewById(R.id.monto)
                val nombres: TextInputEditText = requireView().findViewById(R.id.nombres)
                val apellidos: TextInputEditText = requireView().findViewById(R.id.apellidos)
                val email: TextInputEditText = requireView().findViewById(R.id.email)
                val telefono: TextInputEditText = requireView().findViewById(R.id.telefono)
                val direccion: TextInputEditText = requireView().findViewById(R.id.direccion)
                val frecuencia: Spinner = requireView().findViewById(R.id.frecuencia)
                val anonimato: CheckBox = requireView().findViewById(R.id.anonimato)

                PayPalCheckout.startCheckout(
                    CreateOrder { createOrderActions ->
                        val order = Order(
                            intent = OrderIntent.CAPTURE,
                            appContext = AppContext(
                                userAction = UserAction.PAY_NOW
                            ),
                            purchaseUnitList = listOf(
                                PurchaseUnit(
                                    amount = Amount(
                                        currencyCode = CurrencyCode.MXN,
                                        value = monto.text.toString()
                                    )
                                )
                            )
                        )
                        createOrderActions.create(order)
                    }
                )

                if(anonimato.isChecked() == false) {
                    database = FirebaseDatabase.getInstance().getReference("DonadoresDinero")
                    val donadorDinero = donadorDinero(LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")), nombres.text.toString(), apellidos.text.toString(), email.text.toString(), telefono.text.toString(), direccion.text.toString())
                    if (donadorDinero.nombres!!.isNotEmpty() && donadorDinero.apellidos!!.isNotEmpty() && donadorDinero.email!!.isNotEmpty() && donadorDinero.telefono!!.isNotEmpty()){
                        database.child(nombres.text.toString() + " " + apellidos.text.toString()).setValue(donadorDinero).addOnSuccessListener {
                            monto.text!!.clear()
                            nombres.text!!.clear()
                            apellidos.text!!.clear()
                            email.text!!.clear()
                            telefono.text!!.clear()
                            direccion.text!!.clear()
                            frecuencia.setSelection(0)
                            anonimato.setChecked(false)
                            Toast.makeText(
                                activity?.applicationContext,
                                "Registro exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener{
                            Toast.makeText(activity?.applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity?.applicationContext, "Todos los campos son mandatorios", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (monto.text.toString().isEmpty()) {
                        Toast.makeText(activity?.applicationContext, "Un monto es mandatorio", Toast.LENGTH_SHORT).show()
                    }
                    monto.text!!.clear()
                    nombres.text!!.clear()
                    apellidos.text!!.clear()
                    email.text!!.clear()
                    telefono.text!!.clear()
                    direccion.text!!.clear()
                    frecuencia.setSelection(0)
                    anonimato.setChecked(false)
                }
            }
            R.id.botonDonarOffline -> {
                val infoDonacionOffline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOffline)
                val infoDonacionOnline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOnline)
                val bDonarOffline: Button = requireView().findViewById(R.id.botonDonarOffline)
                val bDonarAhora: Button = requireView().findViewById(R.id.botonDonarAhora)
                infoDonacionOnline.setVisibility(View.GONE)
                infoDonacionOffline.setVisibility(View.VISIBLE)
                bDonarOffline.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.rojo)))
                bDonarAhora.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.blanco)))
                bDonarAhora.setTextColor(resources.getColor(R.color.rojo))
                bDonarOffline.setTextColor(resources.getColor(R.color.blanco))
                check = 1
            }
            R.id.botonDonarAhora -> {
                val infoDonacionOffline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOffline)
                val infoDonacionOnline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOnline)
                val bDonarOffline: Button = requireView().findViewById(R.id.botonDonarOffline)
                val bDonarAhora: Button = requireView().findViewById(R.id.botonDonarAhora)
                val monto: TextInputEditText = requireView().findViewById(R.id.monto)
                val nombres: TextInputEditText = requireView().findViewById(R.id.nombres)
                val apellidos: TextInputEditText = requireView().findViewById(R.id.apellidos)
                val email: TextInputEditText = requireView().findViewById(R.id.email)
                val telefono: TextInputEditText = requireView().findViewById(R.id.telefono)
                val direccion: TextInputEditText = requireView().findViewById(R.id.direccion)
                val frecuencia: Spinner = requireView().findViewById(R.id.frecuencia)
                val anonimato: CheckBox = requireView().findViewById(R.id.anonimato)
                infoDonacionOnline.setVisibility(View.VISIBLE)
                infoDonacionOffline.setVisibility(View.GONE)
                bDonarOffline.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.blanco)))
                bDonarAhora.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.rojo)))
                bDonarAhora.setTextColor(resources.getColor(R.color.blanco))
                bDonarOffline.setTextColor(resources.getColor(R.color.rojo))
                if(check == 1) {
                    monto.text!!.clear()
                    nombres.text!!.clear()
                    apellidos.text!!.clear()
                    email.text!!.clear()
                    telefono.text!!.clear()
                    direccion.text!!.clear()
                    frecuencia.setSelection(0)
                    anonimato.setChecked(false)
                    check = 0
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DonarDinero.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DonarDinero().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
