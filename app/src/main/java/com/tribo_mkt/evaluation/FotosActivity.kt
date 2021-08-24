package com.tribo_mkt.evaluation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tribo_mkt.evaluation.respostas.FotoResposta
import com.squareup.picasso.Picasso

class FotosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotos)

        val albumId = intent.extras!!.getString("albumId")!!
        val usuarioNome = intent.extras!!.getString("usuarioNome")!!

        supportActionBar!!.title = "Fotos de " + usuarioNome
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val stringRequest = StringRequest(
            Request.Method.GET, "https://jsonplaceholder.typicode.com/photos?albumId=" + albumId,
            Response.Listener<String> { response ->
                val todasAsFotos = Gson().newBuilder().create().fromJson(response, Array<FotoResposta>::class.java).toList()

                val lista = findViewById<RecyclerView>(R.id.lista)!!
                val adapter = Adapter(this, todasAsFotos)
                lista.layoutManager = LinearLayoutManager(this)
                lista.adapter = adapter
                findViewById<View>(R.id.loading)!!.visibility = View.GONE
            },
            Response.ErrorListener {
                findViewById<View>(R.id.loading)!!.visibility = View.GONE
                Toast.makeText(this, "Algo errado aconteceu. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            })

        Volley.newRequestQueue(this).add(stringRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class Adapter(
        val activity: Activity,
        var items: List<FotoResposta>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.photo_view, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder as ViewHolder
            view.titulo.text = items[position].titulo
            view.fundo.setOnClickListener {
                val intent = Intent(activity, FotoDetalheActivity::class.java)
                intent.putExtra("fotoUrl", items[position].url)
                intent.putExtra("fotoNome", items[position].titulo)
                activity.startActivity(intent)
            }

            Picasso.get().load(items[position].thumbnailUrl).into(view.thumb)
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val fundo = itemView.findViewById<View>(R.id.fundo)!!
            val thumb = itemView.findViewById<ImageView>(R.id.thumb)!!
            val titulo = itemView.findViewById<TextView>(R.id.titulo)!!
        }
    }
}