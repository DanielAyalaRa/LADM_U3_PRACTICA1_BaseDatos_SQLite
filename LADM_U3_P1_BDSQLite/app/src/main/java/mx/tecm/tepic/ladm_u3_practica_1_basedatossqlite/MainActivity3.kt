package mx.tecm.tepic.ladm_u3_practica_1_basedatossqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity3 : AppCompatActivity() {
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extras = intent.extras

        descripcionAct.setText(extras!!.getString("descrip"))
        fechaCaptura.setText(extras!!.getString("fechacap"))
        fechaEntrega.setText(extras!!.getString("fechaent"))

        id = extras.getString("id").toString()

        btnActualizarEd.setOnClickListener {
            var c = Actividad(descripcionAct.text.toString(), fechaCaptura.text.toString(), fechaEntrega.text.toString())
            c.id = id.toInt()
            c.asignarPuntero(this)

            if(c.actualizar()) {
                Toast.makeText(this,"SE ACTUALIZÓ LA ACTIVIDAD CON EXITO", Toast.LENGTH_LONG)
                    .show()
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("¡ERROR!")
                    .setMessage("NO SE PUDO ACTUALIZAR LA ACTIVIDAD")
                    .setPositiveButton("OK"){d,i->}
                    .show()
            }
            finish()
        }

        btnVolverEd.setOnClickListener {
            finish()
        }
    }
}