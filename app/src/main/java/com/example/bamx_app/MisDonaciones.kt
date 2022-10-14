package com.example.bamx_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
class MisDonaciones : Fragment() {
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
}