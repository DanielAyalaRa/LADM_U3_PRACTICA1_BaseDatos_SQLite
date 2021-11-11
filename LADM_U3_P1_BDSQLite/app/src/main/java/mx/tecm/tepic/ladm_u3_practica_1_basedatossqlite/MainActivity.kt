package mx.tecm.tepic.ladm_u3_practica_1_basedatossqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var listaID = ArrayList<String>()
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnInsertarAct.setOnClickListener {
            if (descripcion.text.length == 0 || captura.text.length == 0 || entrega.text.length == 0) {
                Toast.makeText(this,"RELLENE TODOS LOS CAMPOS (es obligatorio)", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            var act = Actividad(
                descripcion.text.toString(),
                captura.text.toString(),
                entrega.text.toString()
            )

            act.asignarPuntero(this)

            var res = act.insertar()

            if(res == true) {
                mensaje("SE INSERTÓ LA ACTIVIDAD CON EXITO")
                descripcion.setText("")
                captura.setText("")
                entrega.setText("")
                cargaInformacion()
            } else {
                mensaje("¡ERROR! NO SE PUDO AGREGAR LA ACTIVIDAD")
            }
        }

        buscar.setOnClickListener {
            var ventana = Intent(this,MainActivity2::class.java)
            startActivityForResult(ventana,0)
        }

        cargaInformacion()
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION!!").setMessage(s)
            .setPositiveButton("OK"){
                    d,i-> d.dismiss()
            }
            .show()
    }

    private fun cargaInformacion(){
        try {
            var c = Actividad("","","")
            c.asignarPuntero(this)
            var datos = c.recuperarDatos()

            var tamaño = datos.size-1
            var v = Array<String>(datos.size,{""})

            listaID = ArrayList<String>()
            (0..tamaño).forEach {
                var actividad = datos[it]
                var item = "DESCRIPCION: "+actividad.desc+"\n"+"FECHA DE CAPTURA: "+actividad.fcap+"\n"+"FECHA DE ENTREGA: "+actividad.fent
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

    private fun mostrarAlertEliminarActualizar(posicion: Int) {
        var idLista = listaID.get(posicion)

        AlertDialog.Builder(this)
            .setTitle("ATENCION!!")
            .setMessage("¿QUE DESEA HACER?")
            .setPositiveButton("INSERTAR EVIDENCIA") {d,i-> llamarVentanaEvidencia(idLista)}
            .setNeutralButton("ACTUALIZAR") {d,i-> llamarVentanaActualizar(idLista)}
            .setNegativeButton("CANCELAR") {d,i-> }
            .show()
    }

    private fun llamarVentanaActualizar(idLista: String) {
        var ventana = Intent(this,MainActivity3::class.java)
        var c = Actividad("","","")
        c.asignarPuntero(this)

        var descrip = c.consultaID(idLista).desc
        var fechacap = c.consultaID(idLista).fcap
        var fechaent = c.consultaID(idLista).fent

        ventana.putExtra("id",idLista)
        ventana.putExtra("descrip",descrip)
        ventana.putExtra("fechacap",fechacap)
        ventana.putExtra("fechaent",fechaent)

        startActivityForResult(ventana,0)
    }

    private fun llamarVentanaEvidencia(idLista: String) {
        var ventana = Intent(this,InsertarEvidencias::class.java)

        ventana.putExtra("id",idLista)

        startActivityForResult(ventana,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        cargaInformacion()
    }
}