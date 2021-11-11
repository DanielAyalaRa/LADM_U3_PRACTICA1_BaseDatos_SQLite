package mx.tecm.tepic.ladm_u3_practica_1_basedatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

class MainActivity2 : AppCompatActivity() {
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btnBuscar.setOnClickListener {
            cargaInformacion()
        }

        lista.setOnItemClickListener{ adapterView, view, i, l ->
            mostrarAlertEliminarActualizar(i)
        }

        btnVolverEv.setOnClickListener {
            finish()
        }
    }

    private fun mostrarAlertEliminarActualizar(posicion: Int) {
        var idLista = listaID.get(posicion)

        AlertDialog.Builder(this)
            .setTitle("ATENCION!!")
            .setMessage("¿QUE DESEA HACER?")
            .setPositiveButton("ELIMINAR") {d,i-> eliminar(idLista)}
            .setNeutralButton("VER DETALLES") {d,i-> llamarVentanaDetalles(idLista)}
            .setNegativeButton("CANCELAR") {d,i->}
            .show()
    }

    private fun eliminar(id:String) {
        var c = Actividad("","","")
        c.asignarPuntero(this)

        if(c.eliminar(id)) {
            mensaje("¡SE HA ELIMINADO LA ACTIVIDAD CON EXITO!")

            var d = Evidencias("",ByteArray(0))
            d.asignarPuntero(this)
            d.eliminarPorAct(id)

            fcapA2.setText("")
            var v = Array<String>(0,{""})
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

        } else {
            mensaje("¡ERROR! NO SE PUDO ELIMINAR LA ACTIVIDAD")
        }
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION!!").setMessage(s)
            .setPositiveButton("OK"){
                    d,i-> d.dismiss()
            }
            .show()
    }

    private fun llamarVentanaDetalles(idLista: String) {
        var ventana = Intent(this,Detalles::class.java)
        var c = Actividad("","","")
        c.asignarPuntero(this)

        ventana.putExtra("id",idLista)

        startActivity(ventana)
    }

    private fun cargaInformacion(){
        try {
            var c = Actividad("","","")
            c.asignarPuntero(this)
            var datos = c.recuperaPorFecha(fcapA2.text.toString())

            var tamaño = datos.size-1
            var v = Array<String>(datos.size,{""})

            listaID = ArrayList<String>()
            (0..tamaño).forEach {
                var actividad = datos[it]
                var item = actividad.desc+"\n"+actividad.fcap+"\n"+actividad.fent
                v[it] = item
                listaID.add(actividad.id.toString())
            }

            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

            lista.setOnItemClickListener{ adapterView, view, i, l ->
                mostrarAlertEliminarActualizar(i)
            }
        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }
}