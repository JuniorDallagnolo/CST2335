package com.algonquinlive.dall0078.androidlabs

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class WeatherForecast : Activity() {
    lateinit var image: ImageView
    lateinit var currentTemp: TextView
    lateinit var maxTemp: TextView
    lateinit var minTemp: TextView
    lateinit var windSpeed: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)

        image = findViewById(R.id.weatherImage) as ImageView
        currentTemp = findViewById(R.id.currentTemp) as TextView
        maxTemp = findViewById(R.id.maxTemp) as TextView
        minTemp = findViewById(R.id.minTemp) as TextView
        windSpeed = findViewById(R.id.windSpeed) as TextView
        progressBar = findViewById(R.id.progress) as ProgressBar

        progressBar.visibility = View.VISIBLE //setting visibility of progress bar
        val myQuery = ForecastQuery()
        myQuery.execute() //runs the thread

    }

    //AsyncTask is a background thread
    @SuppressLint("StaticFieldLeak")
    private inner class ForecastQuery : AsyncTask<String, Int, String>() {

        var wind: String? = null
        var current: String? = null
        var min: String? = null
        var max: String? = null
        var iconName: String? = null
        var progress = 0
        lateinit var bitmp: Bitmap

        override fun doInBackground(vararg params: String?): String {


            //this is where we want to go
            val url = URL("http://api.openweathermap.org/data/2.5/weather?q=ottawa," +
                    "ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric")

            val connection = url.openConnection() as HttpURLConnection //goes to the server
            val response = connection.getInputStream() //connects to server and gets response back

            val factory = XmlPullParserFactory.newInstance()
            factory.setNamespaceAware(false)
            val xpp = factory.newPullParser()
            xpp.setInput(response, "UTF-8") //read XML from server

            while (xpp.eventType != XmlPullParser.END_DOCUMENT) {
                when (xpp.eventType) {
                    XmlPullParser.START_TAG -> {

                        if (xpp.name == "speed") {
                            wind = xpp.getAttributeValue(null, "value")
                            progress += 20
                        } else if (xpp.name == "temperature") {
                            current = xpp.getAttributeValue(null, "value")
                            min = xpp.getAttributeValue(null, "min")
                            max = xpp.getAttributeValue(null, "max")
                            progress += 60
                        }
                        if (xpp.name == "weather") {

                            iconName = xpp.getAttributeValue(null, "icon")

                            if (fileExistance("$iconName.png")) {
                                var fis: FileInputStream? = null
                                try {
                                    fis = openFileInput("$iconName.png")
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                                bitmp = BitmapFactory.decodeStream(fis)

                            } else {

                                val weatherUrl = "http://openweathermap.org/img/w/$iconName.png"
                                bitmp = getImage(weatherUrl)!!
                                val outputStream = openFileOutput("$iconName.png", Context.MODE_PRIVATE)
                                bitmp.compress(Bitmap.CompressFormat.PNG, 80, outputStream)
                                outputStream.flush()
                                outputStream.close()

                            }

                            progress += 20
                        }
                        publishProgress() //causes android to call onProgressUpdate when GUI is ready
                    }

                    XmlPullParser.TEXT -> {
                    }
                }

                xpp.next() // go to the next xml element
            }

            return "Done"
        }

        @SuppressLint("SetTextI18n")
        override fun onProgressUpdate(vararg values: Int?) { //update your GUI
            //text fields setText()
            currentTemp.text = "Current Temperature: $current"
            minTemp.text = "Minimum Temperature: $min"
            maxTemp.text = "Maximum Temperature: $max"
            windSpeed.text = "Wind Speed: $wind"
            progressBar.setProgress(progress)

        }

        override fun onPostExecute(result: String?) { //run when thread is done and going away
            image.setImageBitmap(bitmp)
            progressBar.visibility = View.INVISIBLE //hide progress bar
        }

        fun fileExistance(fname: String): Boolean {
            val file = getBaseContext().getFileStreamPath(fname)
            return file.exists()
        }

        fun getImage(url: URL): Bitmap? {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val responseCode = connection.responseCode
                return if (responseCode == 200) {
                    BitmapFactory.decodeStream(connection.inputStream)
                } else
                    null
            } catch (e: Exception) {
                return null
            } finally {
                connection?.disconnect()
            }
        }

        fun getImage(urlString: String): Bitmap? {
            try {
                val url = URL(urlString)
                return getImage(url)
            } catch (e: MalformedURLException) {
                return null
            }

        }

    }
}
