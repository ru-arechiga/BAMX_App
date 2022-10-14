package com.example.bamx_app

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MisDonaciones.newInstance] factory method to
 * create an instance of this fragment.
 */
class MisDonaciones : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db = DBHelper(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_mis_donaciones, container, false)
        val voluntariadoM: ImageButton = view.findViewById(R.id.medallaVoluntariado)
        val especieM: ImageButton = view.findViewById(R.id.medallaEspecie)
        val monetariaM: ImageButton = view.findViewById(R.id.medallaMonetaria)
        val netoM: ImageButton = view.findViewById(R.id.medallaNeto)
        val redesM: ImageButton = view.findViewById(R.id.medallaRedes)
        val imageButtons = arrayOf(voluntariadoM, especieM, monetariaM, netoM, redesM)
        for(i in imageButtons) {
            i.setOnClickListener(this)
            var conteo = db.llamarConteo()
            var index = imageButtons.indexOf(i)
            var dbVal = conteo[index].toInt()
            if (index == 2) {
                if(dbVal >= 2 && dbVal < 5) {
                    i.setImageResource(R.drawable.bronze)
                    i.setTag("bronze")
                } else if (dbVal >= 5 && dbVal < 10) {
                    i.setImageResource(R.drawable.plata)
                    i.setTag("plata")
                } else if (dbVal >= 10) {
                    i.setImageResource(R.drawable.oro)
                    i.setTag("oro")
                }
            } else if (index == 3) {
                if(dbVal >= 200 && dbVal < 500) {
                    i.setImageResource(R.drawable.bronze)
                    i.setTag("bronze")
                } else if (dbVal >= 500 && dbVal < 1000) {
                    i.setImageResource(R.drawable.plata)
                    i.setTag("plata")
                } else if (dbVal >= 1000) {
                    i.setImageResource(R.drawable.oro)
                    i.setTag("oro")
                }
            } else {
                if(dbVal >= 1 && dbVal < 2) {
                    i.setImageResource(R.drawable.bronze)
                    i.setTag("bronze")
                } else if (dbVal >= 2 && dbVal < 5) {
                    i.setImageResource(R.drawable.plata)
                    i.setTag("plata")
                } else if (dbVal >= 5) {
                    i.setImageResource(R.drawable.oro)
                    i.setTag("oro")
                }
            }
        }
        val volunteer: TextView = view.findViewById(R.id.volunteer)
        val spice: TextView = view.findViewById(R.id.spice)
        val money: TextView = view.findViewById(R.id.money)
        val net: TextView = view.findViewById(R.id.net)
        val conteo = db.llamarConteo()
        volunteer.text = conteo[0]
        spice.text = conteo[1]
        money.text = conteo[2]
        net.text = "$ ${conteo[3]} MXN"
        val recientes = db.llamarRecientes()
        if(recientes[0] != String()) {
            val donacionesRecientes: TextInputLayout = view.findViewById(R.id.donacionesRecientes)
            donacionesRecientes.visibility = View.VISIBLE
            val fechaRecienteUno: TextView = view.findViewById(R.id.fechaRecienteUno)
            val montoRecienteUno: TextView = view.findViewById(R.id.montoRecienteUno)
            fechaRecienteUno.text = recientes[0]
            montoRecienteUno.text = "$ ${recientes[1]}"
        }
        if(recientes[2] != String()) {
            val recienteDos: LinearLayout = view.findViewById(R.id.recienteDos)
            recienteDos.visibility = View.VISIBLE
            val fechaRecienteDos: TextView = view.findViewById(R.id.fechaRecienteDos)
            val montoRecienteDos: TextView = view.findViewById(R.id.montoRecienteDos)
            fechaRecienteDos.text = recientes[2]
            montoRecienteDos.text = "$ ${recientes[3]}"
        }
        if(recientes[4] != String()) {
            val recienteTres: LinearLayout = view.findViewById(R.id.recienteTres)
            recienteTres.visibility = View.VISIBLE
            val fechaRecienteTres: TextView = view.findViewById(R.id.fechaRecienteTres)
            val montoRecienteTres: TextView = view.findViewById(R.id.montoRecienteTres)
            fechaRecienteTres.text = recientes[4]
            montoRecienteTres.text = "$ ${recientes[5]}"
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MisDonaciones.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MisDonaciones().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.medallaVoluntariado -> {
                val voluntariadoM: ImageButton = requireView().findViewById(R.id.medallaVoluntariado)
                val tag = voluntariadoM.tag
                if(tag == "bronze") {
                    Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                } else if (tag == "plata") {
                    Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                } else if (tag == "oro") {
                    Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.medallaEspecie -> {
                val especieM: ImageButton = requireView().findViewById(R.id.medallaEspecie)
                val tag = especieM.tag
                if(tag == "bronze") {
                    Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                } else if (tag == "plata") {
                    Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                } else if (tag == "oro") {
                    Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.medallaRedes -> {
                val redesM: ImageButton = requireView().findViewById(R.id.medallaRedes)
                val tag = redesM.tag
                if(tag == "bronze") {
                    Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                } else if (tag == "plata") {
                    Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                } else if (tag == "oro") {
                    Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.medallaNeto -> {
                val netoM: ImageButton = requireView().findViewById(R.id.medallaNeto)
                val tag = netoM.tag
                if(tag == "bronze") {
                    Toast.makeText(activity?.applicationContext, "Bronce: 200-500", Toast.LENGTH_SHORT).show()
                } else if (tag == "plata") {
                    Toast.makeText(activity?.applicationContext, "Plata: 500-1000", Toast.LENGTH_SHORT).show()
                } else if (tag == "oro") {
                    Toast.makeText(activity?.applicationContext, "Oro: 1000+", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity?.applicationContext, "Sin medalla: 0-200", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.medallaMonetaria -> {
                val monetariaM: ImageButton = requireView().findViewById(R.id.medallaMonetaria)
                val tag = monetariaM.tag
                if(tag == "bronze") {
                    Toast.makeText(activity?.applicationContext, "Bronce: 2-5", Toast.LENGTH_SHORT).show()
                } else if (tag == "plata") {
                    Toast.makeText(activity?.applicationContext, "Plata: 5-10", Toast.LENGTH_SHORT).show()
                } else if (tag == "oro") {
                    Toast.makeText(activity?.applicationContext, "Oro: 10+", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity?.applicationContext, "Sin medalla: 0-2", Toast.LENGTH_SHORT).show()
                }
            } else -> {
                Toast.makeText(activity?.applicationContext, "HOLA TEST", Toast.LENGTH_SHORT).show()
            }
        }
    }
}