package com.example.dovenews.ui.headlines

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dovenews.BuildConfig
import com.example.dovenews.R
import com.example.dovenews.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var card1: CardView? = null
    private var card2: CardView? = null
    private var card3: CardView? = null

    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        setupSettings()
        // Inflate the layout for this fragment
        return binding!!.root
    }

    private fun setupSettings() {

        card1 = binding!!.aboutUs
        card1!!.setOnClickListener({showInfo() })

        card2 = binding!!.feedback
        card2!!.setOnClickListener({ setupFeedback() })

        card3 = binding!!.rateUs
        card3!!.setOnClickListener({   setupRate()})

    }

    private fun showInfo() {
        val mBuilder: AlertDialog.Builder = AlertDialog.Builder(this.requireActivity())
        mBuilder.setTitle(getString(R.string.about_title, BuildConfig.VERSION_NAME))
        mBuilder.setMessage(getString(R.string.about_message))
        mBuilder.create().show()
    }

    private fun setupRate() {
        val muri: Uri = Uri.parse("market://details?id=\$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, muri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=\$packageName")
                )
            )
        }
    }

    private fun setupFeedback() {
        val emailintent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", "dovenews@gmail.com", null)
        ) //only email apps should handle this
        emailintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.yoursubject))
        startActivity(Intent.createChooser(emailintent, getString(R.string.contact_us)))
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
}