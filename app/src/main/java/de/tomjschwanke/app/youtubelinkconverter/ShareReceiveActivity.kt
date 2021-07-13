package de.tomjschwanke.app.youtubelinkconverter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Matcher
import java.util.regex.Pattern


// Receives the YouTube shorts URL from the share intent, turns it into a normal YouTube URL and copies it back to the clipboard
class ShareReceiveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        moveTaskToBack(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_receive)

        val receivedIntent  : Intent? = intent
        val receivedType    : String? = intent.type

        if(receivedType != null) {
            // Intent received, parse text
            val receivedLink = receivedIntent?.getStringExtra(Intent.EXTRA_TEXT)

            if(receivedLink != null) {
                // Parse the video-id from the URL
                val youtubeIdRegex      : Pattern   = Pattern.compile("https?://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|shorts/|user/(?:[\\w#]+/)+))([^&#?\\n]+)")
                val youtubeIdMatcher    : Matcher   = youtubeIdRegex.matcher(receivedLink)
                var youtubeId           : String?   = ""
                if(youtubeIdMatcher.find()) {
                    youtubeId = youtubeIdMatcher.group(1)
                }

                if(youtubeId != null && youtubeId != "") {
                    // Insert video-id into a normal YouTube link
                    val normalUrl = "https://youtube.com/watch?v=$youtubeId"

                    // And copy that to the clipboard
                    val clipboardManager    = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData            = ClipData.newPlainText("text", normalUrl)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(applicationContext,"YouTube link converted and copied to clipboard", Toast.LENGTH_SHORT).show()

                }else {
                    // something went wrong, no video-id found
                    // TODO: prompt to send email with current URL?
                    Toast.makeText(applicationContext,"Couldn't parse video-id", Toast.LENGTH_SHORT).show()
                }
            }else {
                // something went wrong, nothing received
                // TODO: prompt to send email with received intent?
                Toast.makeText(applicationContext,"Couldn't parse intent",Toast.LENGTH_SHORT).show()
            }
        }else {
            // App started from launcher, redirect to app info screen
            // TODO: redirect to app info screen (settings)
            Toast.makeText(applicationContext, "This app cannot be started from the launcher", Toast.LENGTH_SHORT).show()
        }
        finish();
    }
}