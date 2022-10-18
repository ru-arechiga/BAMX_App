package com.example.bamx_app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
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
    var shareDialog: ShareDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db = DBHelper(activity)
        shareDialog = ShareDialog(this)
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
            val conteo = db.llamarConteo()
            val index = imageButtons.indexOf(i)
            val dbVal = conteo[index].toInt()
            if (index == 2) {
                if(dbVal in 2..4) {
                    i.setImageResource(R.drawable.bronce)
                    i.tag = "bronce"
                } else if (dbVal in 5..9) {
                    i.setImageResource(R.drawable.plata)
                    i.tag = "plata"
                } else if (dbVal >= 10) {
                    i.setImageResource(R.drawable.oro)
                    i.tag = "oro"
                }
            } else if (index == 3) {
                if(dbVal in 200..499) {
                    i.setImageResource(R.drawable.bronce)
                    i.tag = "bronce"
                } else if (dbVal in 500..999) {
                    i.setImageResource(R.drawable.plata)
                    i.tag = "plata"
                } else if (dbVal >= 1000) {
                    i.setImageResource(R.drawable.oro)
                    i.tag = "oro"
                }
            } else {
                if(dbVal in 1..1) {
                    i.setImageResource(R.drawable.bronce)
                    i.tag = "bronce"
                } else if (dbVal in 2..4) {
                    i.setImageResource(R.drawable.plata)
                    i.tag = "plata"
                } else if (dbVal >= 5) {
                    i.setImageResource(R.drawable.oro)
                    i.tag = "oro"
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
        val netDonations = "$${conteo[3]} MXN"
        net.text = netDonations
        val recientes = db.llamarRecientes()
        if(recientes[0] != String()) {
            val donacionesRecientes: TextInputLayout = view.findViewById(R.id.donacionesRecientes)
            donacionesRecientes.visibility = View.VISIBLE
            val fechaRecienteUno: TextView = view.findViewById(R.id.fechaRecienteUno)
            val montoRecienteUno: TextView = view.findViewById(R.id.montoRecienteUno)
            fechaRecienteUno.text = recientes[0]
            val montoUno = "$ ${recientes[1]}"
            montoRecienteUno.text = montoUno
        }
        if(recientes[2] != String()) {
            val recienteDos: LinearLayout = view.findViewById(R.id.recienteDos)
            recienteDos.visibility = View.VISIBLE
            val fechaRecienteDos: TextView = view.findViewById(R.id.fechaRecienteDos)
            val montoRecienteDos: TextView = view.findViewById(R.id.montoRecienteDos)
            fechaRecienteDos.text = recientes[2]
            val montoDos = "$ ${recientes[3]}"
            montoRecienteDos.text = montoDos
        }
        if(recientes[4] != String()) {
            val recienteTres: LinearLayout = view.findViewById(R.id.recienteTres)
            recienteTres.visibility = View.VISIBLE
            val fechaRecienteTres: TextView = view.findViewById(R.id.fechaRecienteTres)
            val montoRecienteTres: TextView = view.findViewById(R.id.montoRecienteTres)
            fechaRecienteTres.text = recientes[4]
            val montoTres = "$ ${recientes[5]}"
            montoRecienteTres.text = montoTres
        }

        val btnWhatsapp : ImageButton = view.findViewById(R.id.whatsappButton)
        btnWhatsapp.setOnClickListener {
            try {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "¡Hola! me gustaría compartirte mis donaciones al Banco " +
                        "de Alimentos de México\n Dinero donado: $${conteo[3]} MXN \n Cantidad de donaciones monetarias: " + conteo[2]
                        +"\n Sesiones de voluntariado: " + conteo[0] + "\n Donaciones en especie: " + conteo[1])
                sendIntent.type = "text/plain"
                sendIntent.setPackage("com.whatsapp")
                startActivity(sendIntent)
            }catch (e: Exception) {
                Toast.makeText(activity, "WhatsApp no esta instalado en su dispositivo", Toast.LENGTH_LONG)
                    .show()
            }

        }
        val btnFacebook : ImageButton = view.findViewById(R.id.facebookButton)

        btnFacebook.setOnClickListener {
            if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                val linkContent: ShareLinkContent = ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://bdalimentos.org/"))
                    .setQuote("¡Hola! me gustaría compartirte mis donaciones al Banco " +
                            "de Alimentos de México\n Dinero donado: $${conteo[3]} MXN \n Cantidad de donaciones monetarias: " + conteo[2]
                            +"\n Sesiones de voluntariado: " + conteo[0] + "\n Donaciones en especie: " + conteo[1])
                    .build()
                shareDialog!!.show(linkContent)

            }
        }

        val btnTwitter : ImageButton = view.findViewById(R.id.twitterButton)
        btnTwitter.setOnClickListener {
            try{
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, "¡Hola! me gustaría compartirte mis donaciones al Banco " +
                        "de Alimentos de México\n Dinero donado: $${conteo[3]} MXN \n Cantidad de donaciones monetarias: " + conteo[2]
                        +"\n Sesiones de voluntariado: " + conteo[0] + "\n Donaciones en especie: " + conteo[1])
                sendIntent.type = "text/plain"
                sendIntent.setPackage("com.twitter.android")
                startActivity(sendIntent)
            } catch (e: Exception) {
                Toast.makeText(activity, "Twitter no esta instalado en su dispositivo", Toast.LENGTH_LONG)
                    .show()
            }

        }

        val btnMail : ImageButton = view.findViewById(R.id.mailButton)

        btnMail.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mis donaciones a BAMX")
                intent.putExtra(Intent.EXTRA_TEXT, "¡Hola! me gustaría compartirte mis donaciones al Banco " +
                        "de Alimentos de México\n Dinero donado: $${conteo[3]} MXN \n Cantidad de donaciones monetarias: " + conteo[2]
                        +"\n Sesiones de voluntariado: " + conteo[0] + "\n Donaciones en especie: " + conteo[1])
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity,
                    "No hay alguna aplicación de correo electrónico instalada",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                when (voluntariadoM.tag) {
                    "bronce" -> {
                        Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                    }
                    "plata" -> {
                        Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                    }
                    "oro" -> {
                        Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.medallaEspecie -> {
                val especieM: ImageButton = requireView().findViewById(R.id.medallaEspecie)
                when (especieM.tag) {
                    "bronce" -> {
                        Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                    }
                    "plata" -> {
                        Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                    }
                    "oro" -> {
                        Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.medallaRedes -> {
                val redesM: ImageButton = requireView().findViewById(R.id.medallaRedes)
                when (redesM.tag) {
                    "bronce" -> {
                        Toast.makeText(activity?.applicationContext, "Bronce: 1", Toast.LENGTH_SHORT).show()
                    }
                    "plata" -> {
                        Toast.makeText(activity?.applicationContext, "Plata: 2-5", Toast.LENGTH_SHORT).show()
                    }
                    "oro" -> {
                        Toast.makeText(activity?.applicationContext, "Oro: 5+", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity?.applicationContext, "Sin medalla: 0", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.medallaNeto -> {
                val netoM: ImageButton = requireView().findViewById(R.id.medallaNeto)
                when (netoM.tag) {
                    "bronce" -> {
                        Toast.makeText(activity?.applicationContext, "Bronce: $200-500", Toast.LENGTH_SHORT).show()
                    }
                    "plata" -> {
                        Toast.makeText(activity?.applicationContext, "Plata: $500-1000", Toast.LENGTH_SHORT).show()
                    }
                    "oro" -> {
                        Toast.makeText(activity?.applicationContext, "Oro: $1000+", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity?.applicationContext, "Sin medalla: $0-200", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.medallaMonetaria -> {
                val monetariaM: ImageButton = requireView().findViewById(R.id.medallaMonetaria)
                when (monetariaM.tag) {
                    "bronce" -> {
                        Toast.makeText(activity?.applicationContext, "Bronce: 2-5", Toast.LENGTH_SHORT).show()
                    }
                    "plata" -> {
                        Toast.makeText(activity?.applicationContext, "Plata: 5-10", Toast.LENGTH_SHORT).show()
                    }
                    "oro" -> {
                        Toast.makeText(activity?.applicationContext, "Oro: 10+", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity?.applicationContext, "Sin medalla: 0-2", Toast.LENGTH_SHORT).show()
                    }
                }
            } else -> {
                Toast.makeText(activity?.applicationContext, "HOLA TEST", Toast.LENGTH_SHORT).show()
            }
        }
    }
}