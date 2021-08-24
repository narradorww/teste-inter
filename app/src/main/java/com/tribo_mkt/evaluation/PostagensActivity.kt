package com.tribo_mkt.evaluation

import android.app.Activity
import android.content.Intent
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
import com.tribo_mkt.evaluation.respostas.PostagemResposta

class PostagensActivity : AppCompatActivity() {

    var postagens: Resposta? = null
    var comentarios: Resposta? = null
    lateinit var usuarioNome: String

    data class Resposta(val lista: List<Any>?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_postagens)

        val usuarioId = intent.extras!!.getString("usuarioId")!!
        usuarioNome = intent.extras!!.getString("usuarioNome")!!

        supportActionBar!!.title = "Postagens de " + usuarioNome
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val queue = Volley.newRequestQueue(this)

        val postsStringRequest = StringRequest(
            Request.Method.GET, "https://jsonplaceholder.typicode.com/posts?userId=" + usuarioId,
            Response.Listener<String> { response ->
                finalizar(
                    Resposta(
                        Gson().newBuilder().create().fromJson(response, Array<PostagemResposta>::class.java).toList()
                    ),
                    null
                )
            },
            Response.ErrorListener {
                finalizar(
                    null,
                    null
                )
            })

        val comentariosStringRequest = StringRequest(
            Request.Method.GET, "https://jsonplaceholder.typicode.com/comments?userId=" + usuarioId,
            Response.Listener<String> { response ->
                finalizar(
                    null,
                    Resposta(
                        Gson().newBuilder().create().fromJson(response, Array<ComentarioResposta>::class.java).toList()
                    )
                )
            },
            Response.ErrorListener {
                finalizar(
                    null,
                    null
                )
            })

        queue.add(postsStringRequest)
        queue.add(comentariosStringRequest)
    }

    fun finalizar(postagens: Resposta?, comentarios: Resposta?) {

        if (postagens != null) {
            this.postagens = postagens
        }

        if (comentarios != null) {
            this.comentarios = comentarios
        }

        if (this.postagens != null && this.comentarios != null) {

            val postagensLista = this.postagens!!.lista
            val comentariosLista = this.comentarios!!.lista

            if (postagensLista != null) {
                (postagensLista as List<PostagemResposta>).forEach {
                    if (comentariosLista != null) {
                        it.comentarios = (comentariosLista as List<ComentarioResposta>).filter { comment -> comment.postagemId == it.id }.size
                    }
                }

                val lista = findViewById<RecyclerView>(R.id.lista)!!
                val adapter = Adapter(this, postagensLista, usuarioNome)
                lista.layoutManager = LinearLayoutManager(this)
                lista.adapter = adapter
                findViewById<View>(R.id.loading)!!.visibility = View.GONE
            } else {
                findViewById<View>(R.id.loading)!!.visibility = View.GONE
                Toast.makeText(this, "Algo errado aconteceu. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            }
        }
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
        var items: List<PostagemResposta>,
        var usuarioNome: String
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_view, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder as ViewHolder
            view.titulo.text = items[position].titulo
            view.fundo.setOnClickListener {
                val intent = Intent(activity, ComentariosActivity::class.java)
                intent.putExtra("postagemId", items[position].id)
                intent.putExtra("usuarioNome", usuarioNome)
                activity.startActivity(intent)
            }
            view.comentarios.text = "Número de comentários: " + items[position].comentarios.toString()
            if (items[position].comentarios == null) {
                view.comentarios.visibility = View.GONE
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val fundo = itemView.findViewById<View>(R.id.fundo)!!
            val titulo = itemView.findViewById<TextView>(R.id.titulo)!!
            val comentarios = itemView.findViewById<TextView>(R.id.comentarios)!!
        }
    }
}