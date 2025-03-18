package com.dicoding.skinSavvy.view.user
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.dicoding.skinSavvy.R
import com.dicoding.skinSavvy.databinding.ActivityResultBinding
import com.dicoding.skinSavvy.local.database.PredictionHistoryDatabase
import com.dicoding.skinSavvy.local.user.PredictionHistory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val label = intent.getStringExtra("LABEL")
        val confidence = intent.getFloatExtra("CONFIDENCE", 0f)
        @Suppress("DEPRECATION")
        imageUri = intent.getParcelableExtra("IMAGE_URI") ?: return
        val resultText: TextView = findViewById(R.id.result_text)
        val resultCardView: CardView = findViewById(R.id.resultCardView)
        val positiveResultLayout: View = findViewById(R.id.positiveResultLayout)
        val negativeResultLayout: View = findViewById(R.id.negativeResultLayout)
        val resultIcon: ImageView = findViewById(R.id.resultIcon)
        binding.resultImage.setImageURI(imageUri)
        val confidencePercentage = confidence * 100
        binding.resultText.text = getString(R.string.result_text, label, confidencePercentage)


        if (label == "Cancer") {
            binding.btnConsultation.visibility = View.VISIBLE
            resultText.setTextColor(resources.getColor(R.color.red, null))
            resultCardView.setCardBackgroundColor(resources.getColor(R.color.soft_red, null))
            resultIcon.setImageResource(R.drawable.baseline_warning_24);
            positiveResultLayout.visibility = View.VISIBLE
            negativeResultLayout.visibility = View.GONE
        } else {
            binding.btnConsultation.visibility = View.GONE
            resultText.setTextColor(resources.getColor(R.color.green, null))
            resultCardView.setCardBackgroundColor(resources.getColor(R.color.soft_green, null))
            resultIcon.setImageResource(R.drawable.baseline_check_circle_24);
            positiveResultLayout.visibility = View.GONE
            negativeResultLayout.visibility = View.VISIBLE
        }

        binding.btnConsultation.setOnClickListener {
//            if (FirebaseAuth.getInstance().currentUser != null) {
//                val intent = Intent(this, ConsultationActivity::class.java)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
        }

        binding.SaveData.setOnClickListener {
            savePrediction(label, confidence)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun savePrediction(label: String?, confidence: Float) {
        val imagePath = imageUri.toString()
        val history = PredictionHistory(
            imagePath = imagePath,
            label = label.orEmpty(),
            confidence = confidence
        )
        val dao = PredictionHistoryDatabase.getDatabase(this).predictionHistoryDao()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                dao.insert(history)
                showToast("Prediction saved!")
            } catch (e: Exception) {
                showToast("Failed to save prediction!")
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun showToast(message: String, exception: Exception? = null) {
        val errorMessage = if (exception != null) {
            "$message: ${exception.message}"
        } else {
            message
        }

        runOnUiThread {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                startActivity(Intent(this, PredictionHistoryActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}
