package com.tribo_mkt.evaluation

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class FotoDetalheActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotos_detalhe)

        val fotoUrl = intent.extras!!.getString("fotoUrl")!!
        val fotoNome = intent.extras!!.getString("fotoNome")!!

        supportActionBar!!.title = "Foto Detalhe"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        findViewById<TextView>(R.id.imagemNome).text = fotoNome

        Picasso.get().load(fotoUrl).into(findViewById<ImageView>(R.id.imagem))
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
}