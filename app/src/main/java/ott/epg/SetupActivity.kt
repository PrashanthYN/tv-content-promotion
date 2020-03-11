package ott.epg

import android.content.ContentUris
import android.media.tv.TvContract
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import kotlinx.android.synthetic.main.activity_setup.*

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        syncButton.setOnClickListener {
            ContentTask().execute("")
        }
    }

    private fun createChannelPrograms(uri:Uri) {

        val channelId=ContentUris.parseId(uri)

        val programBuilder1 = PreviewProgram.Builder()

        for (i in 1..10){
        programBuilder1.setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
            .setTitle("CustomProgram Program "+i)
            .setThumbnailUri(Uri.parse("https://wallpapersbq.com/images/300/300-wallpaper-9.jpg"))
            .setPosterArtUri(Uri.parse("https://wallpapersbq.com/images/300/300-wallpaper-9.jpg"))
            .setDescription("Program description "+i)
            .setLogoUri(Uri.parse("https://images-eu.ssl-images-amazon.com/images/I/51CNksbNcfL.png"))

            contentResolver.insert(TvContractCompat.PreviewPrograms.CONTENT_URI,
                programBuilder1.build().toContentValues())
        }

        TvContractCompat.requestChannelBrowsable(applicationContext, channelId)
    }

    private fun createChannels(number:Int): Uri {
        val builder = Channel.Builder()
// Every channel you create must have the type TYPE_PREVIEW
        builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
            .setDisplayName("CustomChannel "+number)
            .setInputId(1.toString())
           return contentResolver.insert(TvContract.Channels.CONTENT_URI, builder.build().toContentValues())!!
        }

   inner class ContentTask:AsyncTask<String,String,String>(){
       override fun doInBackground(vararg params: String?): String {
           for(i in 1..4) {
               createChannelPrograms(createChannels(i))
           }
           return "completed"
       }

       override fun onPostExecute(result: String?) {
           Toast.makeText(applicationContext,"Channels and programs created",Toast.LENGTH_SHORT).show()
       }
   }

}
