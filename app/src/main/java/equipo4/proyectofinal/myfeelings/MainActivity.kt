package equipo4.proyectofinal.myfeelings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import utilities.CustomBarDrawable
import utilities.CustomCircleDrawable
import utilities.Emociones
import utilities.JSONFile



class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verySad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    private lateinit var graph: View
    private lateinit var graphHappy: View
    private lateinit var graphVeryHappy: View
    private lateinit var graphNeutral: View
    private lateinit var graphVerySad: View
    private lateinit var graphSad: View
    private lateinit var icon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var guardarButton: Button = findViewById(R.id.guardarButton)
        var veryHappyButton: ImageButton = findViewById(R.id.veryHappyButton)
        var happyButton: ImageButton = findViewById(R.id.happyButton)
        var neutralButton: ImageButton = findViewById(R.id.neutralButton)
        var sadButton: ImageButton = findViewById(R.id.sadButton)
        var verySadButton: ImageButton = findViewById(R.id.verySadButton)

        graph = findViewById(R.id.graph)
        graphHappy = findViewById(R.id.graphHappy)
        graphVeryHappy = findViewById(R.id.graphVeryHappy)
        graphNeutral = findViewById(R.id.graphNeutral)
        graphVerySad = findViewById(R.id.graphVerySad)
        graphSad = findViewById(R.id.graphSad)
        icon = findViewById(R.id.icon)

        jsonFile = JSONFile()

        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0f, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0f, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0f, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0f, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0f, R.color.deepBlue, verySad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }



        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

    }


    fun fetchingData(){

        try {
            val json: String = jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                val jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista) {
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verySad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }

    }

    fun iconoMayoria() {

        if (happy > veryHappy && happy > neutral && happy > sad && happy > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if (veryHappy > happy && veryHappy > neutral && veryHappy > sad && veryHappy > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }
        if (neutral > veryHappy && neutral > happy && neutral > sad && neutral > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if (sad > happy && sad > neutral && sad > veryHappy && sad > verySad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if (verySad > happy && verySad > neutral && verySad > sad && verySad > veryHappy) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }


    fun actualizarGrafica(){

        val total = veryHappy+happy+neutral+verySad+sad

        val pVH: Float = (veryHappy * 100 / total).toFloat()
        val pH: Float = (happy * 100 / total).toFloat()
        val pN: Float = (neutral * 100 / total).toFloat()
        val pS: Float = (sad * 100 / total).toFloat()
        val pVS: Float = (verySad * 100 / total).toFloat()

        Log.d("porcentajes", "very happy " + pVH)
        Log.d("porcentajes", "happy " + pH)
        Log.d("porcentajes", "neutral " + pN)
        Log.d("porcentajes", "sad " + pS)
        Log.d("porcentajes", "very sad " + pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        val fondo = CustomCircleDrawable(this, lista)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        graph.background = fondo

    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{

        val lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()) {
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                val emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }
        return lista

    }

    fun guardar(){

        val jsonArray = JSONArray()
        var o: Int = 0
        for (i in lista) {
            Log.d("objetos", i.toString())
            val j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}