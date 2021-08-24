package com.tribo_mkt.evaluation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import com.tribo_mkt.evaluation.respostas.ComentarioResposta

class ComentariosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comentarios)

        val postagemId = intent.extras!!.getString("postagemId")!!
        val usuarioNome = intent.extras!!.getString("usuarioNome")!!

        supportActionBar!!.title = "Comentários de " + usuarioNome
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val stringRequest = StringRequest(
            Request.Method.GET, "https://jsonplaceholder.typicode.com/comments?postId=" + postagemId,
            Response.Listener<String> { response ->
                val todosComentarios = Gson().newBuilder().create().fromJson(response, Array<ComentarioResposta>::class.java).toList()

                val lista = findViewById<RecyclerView>(R.id.lista)!!
                val adapter = Adapter(todosComentarios)
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
        var items: List<ComentarioResposta>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_view, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder as ViewHolder
            view.titulo.text = items[position].nome
            view.comentario.text = items[position].conteudo
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titulo = itemView.findViewById<TextView>(R.id.titulo)!!
            val comentario = itemView.findViewById<TextView>(R.id.comentario)!!
        }
    }
}