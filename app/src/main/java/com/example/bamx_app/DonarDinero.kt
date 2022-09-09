package com.example.bamx_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_donar_dinero, container, false)
        val botton: Button = view.findViewById(R.id.botonEnviar)
        botton.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.botonEnviar -> {
                val monto: TextInputEditText = requireView().findViewById(R.id.monto)
                val nombres: TextInputEditText = requireView().findViewById(R.id.nombres)
                val apellidos: TextInputEditText = requireView().findViewById(R.id.apellidos)
                val email: TextInputEditText = requireView().findViewById(R.id.email)
                val frecuencia: Spinner = requireView().findViewById(R.id.frecuencia)
                val anonimato: CheckBox = requireView().findViewById(R.id.anonimato)

                if(anonimato.isChecked() == false) {
                    database = FirebaseDatabase.getInstance().getReference("DonadoresDinero")
                    val donadorDinero = donadorDinero(monto.text.toString(), nombres.text.toString(), apellidos.text.toString(), email.text.toString(), frecuencia.getSelectedItem().toString())
                    if (donadorDinero.monto!!.isNotEmpty() && donadorDinero.nombres!!.isNotEmpty() && donadorDinero.apellidos!!.isNotEmpty() && donadorDinero.email!!.isNotEmpty()){
                        database.child(nombres.text.toString() + " " + apellidos.text.toString()).setValue(donadorDinero).addOnSuccessListener {
                            monto.text!!.clear()
                            nombres.text!!.clear()
                            apellidos.text!!.clear()
                            email.text!!.clear()
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
                    anonimato.setChecked(false)
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