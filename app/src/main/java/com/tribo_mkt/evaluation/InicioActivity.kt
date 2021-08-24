package com.tribo_mkt.evaluation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.tribo_mkt.evaluation.respostas.UsuarioResposta
import java.util.*

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        supportActionBar!!.title = "Usuários"

        val stringRequest = StringRequest(Request.Method.GET, "https://jsonplaceholder.typicode.com/users",
                Response.Listener<String> { response ->
                    val usuarios = Gson().newBuilder().create().fromJson(response, Array<UsuarioResposta>::class.java).toMutableList()

                    usuarios.sortWith(Comparator { s1, s2 -> s1.nome.compareTo(s2.nome) })

                    val lista = findViewById<RecyclerView>(R.id.lista)!!
                    val adapter = Adapter(this, usuarios)
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

    class Adapter(
        val activity: Activity,
        var items: List<UsuarioResposta>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.usuario_view, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder as ViewHolder
            view.nome.text = items[position].nome
            view.usuarioNome.text = items[position].usuarioNome
            view.telefone.text = items[position].telefone
            view.email.text = items[position].email
            view.letra.text = items[position].nome.substring(0, 2).toUpperCase()
            if ((position - 1) % 2 == 0) {
                view.fundo.setBackgroundColor(ContextCompat.getColor(activity, R.color.fundo))
            }
            view.albunsBotao.setOnClickListener {
                val intent = Intent(activity, AlbunsActivity::class.java)
                intent.putExtra("usuarioId", items[position].id)
                intent.putExtra("usuarioNome", items[position].usuarioNome)
                activity.startActivity(intent)
            }
            view.postagensBotao.setOnClickListener {
                val intent = Intent(activity, PostagensActivity::class.java)
                intent.putExtra("usuarioId", items[position].id)
                intent.putExtra("usuarioNome", items[position].usuarioNome)
                activity.startActivity(intent)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nome = itemView.findViewById<TextView>(R.id.nome)!!
            val usuarioNome = itemView.findViewById<TextView>(R.id.usuarioNome)!!
            val telefone = itemView.findViewById<TextView>(R.id.telefone)!!
            val email = itemView.findViewById<TextView>(R.id.email)!!
            val fundo = itemView.findViewById<View>(R.id.fundo)!!
            val letra = itemView.findViewById<TextView>(R.id.letra)!!
            val albunsBotao = itemView.findViewById<TextView>(R.id.albunsBotao)!!
            val postagensBotao = itemView.findViewById<TextView>(R.id.postagensBotao)!!
        }
    }
}