package com.example.bamx_app

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.shipping.OnShippingChange
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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
    private lateinit var db : DBHelper
    private var check: Int? = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PayPalCheckout.registerCallbacks(
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "Order successfully captured: $captureOrderResult")
                }
                Toast.makeText(activity?.applicationContext, "Pago exitoso", Toast.LENGTH_SHORT).show()
                scheduleNotification()
                val monto: TextInputEditText = requireView().findViewById(R.id.monto)
                val nombres: TextInputEditText = requireView().findViewById(R.id.nombres)
                val apellidos: TextInputEditText = requireView().findViewById(R.id.apellidos)
                val email: TextInputEditText = requireView().findViewById(R.id.email)
                val telefono: TextInputEditText = requireView().findViewById(R.id.telefono)
                val direccion: TextInputEditText = requireView().findViewById(R.id.direccion)
                val recordar: CheckBox = requireView().findViewById(R.id.recordar)
                val anonimato: CheckBox = requireView().findViewById(R.id.anonimato)
                val fecha: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
                db.donacionDinero(monto.text.toString(), fecha)
                if(!anonimato.isChecked) {
                    val donadorDinero = donadorDinero(LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")), nombres.text.toString(), apellidos.text.toString(), email.text.toString(), telefono.text.toString(), direccion.text.toString())
                    database = FirebaseDatabase.getInstance().getReference("DonadoresDinero")
                    database.child(nombres.text.toString() + " " + apellidos.text.toString()).setValue(donadorDinero).addOnSuccessListener {
                        Toast.makeText(
                            activity?.applicationContext,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener{
                        Toast.makeText(activity?.applicationContext, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
                }
                monto.text?.clear()
                anonimato.isChecked = false
                if (!recordar.isChecked) {
                    nombres.text?.clear()
                    apellidos.text?.clear()
                    email.text?.clear()
                    telefono.text?.clear()
                    direccion.text?.clear()
                }
            },
            onCancel = OnCancel {
                // Optional callback for when a buyer cancels the paysheet
            },
            onError = OnError { errorInfo ->
                Toast.makeText(activity?.applicationContext, "Error en el pago", Toast.LENGTH_SHORT).show()
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
        db = DBHelper(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_donar_dinero, container, false)
        val botton: Button = view.findViewById(R.id.botonEnviar)
        val boton: Button = view.findViewById(R.id.botonDonarOffline)
        val bottton: Button = view.findViewById(R.id.botonDonarAhora)
        val recordar: CheckBox = view.findViewById(R.id.recordar)
        botton.setOnClickListener(this)
        boton.setOnClickListener(this)
        bottton.setOnClickListener(this)
        recordar.setOnClickListener(this)
        var usuario = db.llamarUsuario()
        if(usuario[0] != String()) {
            val nombres: TextInputEditText = view.findViewById(R.id.nombres)
            val apellidos: TextInputEditText = view.findViewById(R.id.apellidos)
            val email: TextInputEditText = view.findViewById(R.id.email)
            val telefono: TextInputEditText = view.findViewById(R.id.telefono)
            val direccion: TextInputEditText = view.findViewById(R.id.direccion)
            nombres.setText(usuario[0])
            apellidos.setText(usuario[1])
            email.setText(usuario[2])
            telefono.setText(usuario[3])
            direccion.setText(usuario[4])
            nombres.setTextColor(resources.getColor(R.color.verde))
            apellidos.setTextColor(resources.getColor(R.color.verde))
            email.setTextColor(resources.getColor(R.color.verde))
            telefono.setTextColor(resources.getColor(R.color.verde))
            direccion.setTextColor(resources.getColor(R.color.verde))
            recordar.isChecked = true
            nombres.isEnabled = false
            apellidos.isEnabled = false
            email.isEnabled = false
            telefono.isEnabled = false
            direccion.isEnabled = false
        }
        return view
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        val monto: TextInputEditText = requireView().findViewById(R.id.monto)
        val nombres: TextInputEditText = requireView().findViewById(R.id.nombres)
        val apellidos: TextInputEditText = requireView().findViewById(R.id.apellidos)
        val email: TextInputEditText = requireView().findViewById(R.id.email)
        val telefono: TextInputEditText = requireView().findViewById(R.id.telefono)
        val direccion: TextInputEditText = requireView().findViewById(R.id.direccion)
        val recordar: CheckBox = requireView().findViewById(R.id.recordar)
        val anonimato: CheckBox = requireView().findViewById(R.id.anonimato)
        val infoDonacionOffline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOffline)
        val infoDonacionOnline: TextInputLayout = requireView().findViewById(R.id.infoDonacionOnline)
        val bDonarOffline: Button = requireView().findViewById(R.id.botonDonarOffline)
        val bDonarAhora: Button = requireView().findViewById(R.id.botonDonarAhora)
        when (v?.id) {
            R.id.botonEnviar -> {
                if((monto.text.toString() != String()) and (anonimato.isChecked or ((nombres.text.toString() != String())
                    and (apellidos.text.toString() != String())
                    and (email.text.toString() != String())
                    and (telefono.text.toString() != String()))))
                {
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
                } else {
                    if (anonimato.isChecked) {
                        Toast.makeText(activity?.applicationContext, "Un monto es necesario", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity?.applicationContext, "Los campos indicados son mandatorios", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.botonDonarOffline -> {
                infoDonacionOnline.setVisibility(View.GONE)
                infoDonacionOffline.setVisibility(View.VISIBLE)
                infoDonacionOnline.isEnabled = false
                bDonarOffline.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.rojo)))
                bDonarAhora.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.blanco)))
                bDonarAhora.setTextColor(resources.getColor(R.color.rojo))
                bDonarOffline.setTextColor(resources.getColor(R.color.blanco))
            }
            R.id.botonDonarAhora -> {
                infoDonacionOnline.setVisibility(View.VISIBLE)
                infoDonacionOffline.setVisibility(View.GONE)
                infoDonacionOnline.isEnabled = true
                bDonarOffline.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.blanco)))
                bDonarAhora.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(R.color.rojo)))
                bDonarAhora.setTextColor(resources.getColor(R.color.blanco))
                bDonarOffline.setTextColor(resources.getColor(R.color.rojo))
            }

            R.id.recordar -> {
                if(recordar.isChecked) {
                    if((nombres.text.toString() != String())
                                and (apellidos.text.toString() != String())
                                and (email.text.toString() != String())
                                and (telefono.text.toString() != String()))
                    {
                        db.guardarUsuario(nombres.text.toString(), apellidos.text.toString(), email.text.toString(), telefono.text.toString(), direccion.text.toString())
                        nombres.isEnabled = false
                        apellidos.isEnabled = false
                        email.isEnabled = false
                        telefono.isEnabled = false
                        direccion.isEnabled = false
                        nombres.setTextColor(resources.getColor(R.color.verde))
                        apellidos.setTextColor(resources.getColor(R.color.verde))
                        email.setTextColor(resources.getColor(R.color.verde))
                        telefono.setTextColor(resources.getColor(R.color.verde))
                        direccion.setTextColor(resources.getColor(R.color.verde))
                        Toast.makeText(activity?.applicationContext, "Información guardada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity?.applicationContext, "Los campos indicados son mandatorios", Toast.LENGTH_SHORT).show()
                        recordar.isChecked = false
                    }
                } else {
                    db.borrarUsuario()
                    nombres.isEnabled = true
                    apellidos.isEnabled = true
                    email.isEnabled = true
                    telefono.isEnabled = true
                    direccion.isEnabled = true
                    nombres.setTextColor(resources.getColor(R.color.grisOscuro))
                    apellidos.setTextColor(resources.getColor(R.color.grisOscuro))
                    email.setTextColor(resources.getColor(R.color.grisOscuro))
                    telefono.setTextColor(resources.getColor(R.color.grisOscuro))
                    direccion.setTextColor(resources.getColor(R.color.grisOscuro))
                    nombres.text?.clear()
                    apellidos.text?.clear()
                    email.text?.clear()
                    telefono.text?.clear()
                    direccion.text?.clear()
                    Toast.makeText(activity?.applicationContext, "Información borrada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun scheduleNotification()
    {
        val intent = Intent(requireActivity().application, Notification::class.java)
        val title = "BAMX"
        val message = "No olvides Donar y ayudar a familias :)"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity().application,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    private fun getTime() : Long
    {
        val c = Calendar.getInstance()
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute+1)
        return calendar.timeInMillis
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
