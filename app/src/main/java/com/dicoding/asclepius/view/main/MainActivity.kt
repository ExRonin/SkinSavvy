package com.dicoding.skinSavvy.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.skinSavvy.R
import com.dicoding.skinSavvy.databinding.ActivityMainBinding
import com.dicoding.skinSavvy.helper.ImageClassifierHelper
import com.dicoding.skinSavvy.server.ApiClient
import com.dicoding.skinSavvy.server.Article
import com.dicoding.skinSavvy.view.news.NewsAdapter
import com.dicoding.skinSavvy.view.user.PredictionHistoryActivity
import com.dicoding.skinSavvy.view.user.ResultActivity
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private var articles: MutableList<Article> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageClassifierHelper = ImageClassifierHelper(this)
        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            val select = getString(R.string.selectimage)
            currentImageUri?.let {
                analyzeImage()
                clearImage()

            } ?: showToast(select)
        }
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    currentImageUri = uri
                    startUCrop(uri)
                }
            }
        }
        recyclerView = findViewById(R.id.recyclerViewContent)
        adapter = NewsAdapter(articles)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchDataFromApi()
        requestStoragePermission()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    private fun startGallery() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(pickImage)
    }

    private fun startUCrop(sourceUri: Uri) {
        val timestamp = System.currentTimeMillis()
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image_$timestamp.jpg"))
        val options = UCrop.Options().apply {
            setCompressionQuality(70)
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark))
            setToolbarWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
            setHideBottomControls(false)
        }

        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    private fun showImage() {
        currentImageUri?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        }
    }

    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val result = imageClassifierHelper.classifyStaticImage(uri)
            val failed = getString(R.string.failedimage)
            result?.let { (label, confidence) ->
                val resultText = "Prediction: $label\nConfidence: ${"%.2f".format(confidence)}"
                showToast(resultText)
                moveToResult(label, confidence)
            } ?: showToast(failed)
        }
    }

    private fun moveToResult(label: String, confidence: Float) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("LABEL", label)
            putExtra("CONFIDENCE", confidence)
            putExtra("IMAGE_URI", currentImageUri)
        }
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let { uri ->
                currentImageUri = uri
                showImage()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageClassifierHelper.closeClassifier()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permission = getString(R.string.permissonacces)
        val denied = getString(R.string.permissondenied)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(permission)
            } else {
                showToast(denied)
            }
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
    private fun fetchDataFromApi() {
        val apiKey = com.dicoding.skinSavvy.BuildConfig.KEY
        val service = ApiClient.create()
        lifecycleScope.launch {
            try {
                val response = service.getTopHeadlines("cancer", "health", "en", apiKey)
                if (response.status == "ok") {
                    val articles = response.articles
                    val mutableArticles: MutableList<Article> = articles.toMutableList()
                    adapter.updateData(mutableArticles)
                } else {
                    val fail = getString(R.string.failedtofetch)
                    showToast(fail)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showToast("Error: ${e.message}")
            }
        }
    }
    private fun clearImage() {
        super.onResume()
        binding.previewImageView.setImageDrawable(null)
    }


    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 101
    }
}
