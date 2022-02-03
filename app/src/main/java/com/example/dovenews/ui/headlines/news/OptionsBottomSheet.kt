package com.example.dovenews.ui.headlines.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.dovenews.R
import com.example.dovenews.data.NewsRepository
import com.example.dovenews.databinding.FragmentOptionsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber
import java.lang.ClassCastException

/**
 * A simple [Fragment] subclass.
 */
class OptionsBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var title: String? = null
    private var url: String? = null
    private var obsid: Int = 0
    private var isSaved = false
    private var listener: OptionsBottomSheetListener? = null
    private var binding: FragmentOptionsBottomSheetBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = arguments?.getString(PARAM_TITLE)
            url = arguments?.getString(PARAM_URL)
            obsid = requireArguments().getInt(PARAM_ID)
            isSaved = arguments?.getBoolean(PARAM_SAVED) == true
       }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_options_bottom_sheet,
            container,
            false
        )
        if (isSaved) {
            binding!!.btnSave.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_saved_item,
                0,
                0,
                0
            )
        }
        binding!!.btnShare.setOnClickListener(this)
        binding!!.btnOpenInBrowser.setOnClickListener(this)
        binding!!.btnSave.setOnClickListener(this)
        return binding!!.root
    }

    override fun onClick(v: View) {
        val intent: Intent
        when (v.id) {
            R.id.btn_open_in_browser -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                fragment?.dismiss()
                startActivity(intent)
            }
            R.id.btn_share -> {
                val shareText = """
                    $title
                    $url
                    """.trimIndent()
                intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                intent.type = "text/plain"
                this.dismiss()
                startActivity(intent)
            }
            R.id.btn_save -> {
                if (isSaved) {
                   NewsRepository.getInstance(context)?.removeSaved(obsid)
                   listener!!.onSaveToggle(getString(R.string.message_item_removed))
                } else {
                    NewsRepository.getInstance(context)?.save(obsid)
                    listener!!.onSaveToggle(getString(R.string.message_item_saved))
                }
                Timber.d("Saved for id  : %s", obsid)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as OptionsBottomSheetListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OptionsBottomSheetListener")
        }
    }

    interface OptionsBottomSheetListener {
        fun onSaveToggle(text: String?)
    }

    companion object {
        private const val PARAM_TITLE = "param-title"
        private const val PARAM_URL = "param-url"
        private const val PARAM_ID = "param-id"
        private const val PARAM_SAVED = "param-saved"
        private var fragment: OptionsBottomSheet? = null
        fun getInstance(
            title: String?,
            url: String?,
            id: Int,
            isSaved: Boolean
        ): OptionsBottomSheet? {
            fragment = OptionsBottomSheet()
            val args = Bundle()
            args.putString(PARAM_TITLE, title)
            args.putString(PARAM_URL, url)
            args.putInt(PARAM_ID, id)
            args.putBoolean(PARAM_SAVED, isSaved)
            fragment!!.arguments = args
            return fragment
        }
    }
}