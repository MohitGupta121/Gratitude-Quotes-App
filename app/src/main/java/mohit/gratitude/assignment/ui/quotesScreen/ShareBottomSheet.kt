package mohit.gratitude.assignment.ui.quotesScreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import mohit.gradtitude.assignment.R
import mohit.gradtitude.assignment.databinding.BottomSheetShareBinding
import mohit.gratitude.assignment.data.model.Quote
import javax.inject.Inject

@AndroidEntryPoint
class ShareBottomSheet @Inject constructor() : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_DATA = "quote"

        fun newInstance(quote: Quote): ShareBottomSheet {
            val fragment = ShareBottomSheet()
            val args = Bundle()
            args.putParcelable(ARG_DATA, quote)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_share, container, false)

        val binding = BottomSheetShareBinding.bind(view)

        val quote = arguments?.getParcelable<Quote>(ARG_DATA)

        binding.apply {
            txtDialogTitle.text = quote?.themeTitle
            Glide.with(ivQuoteImage)
                .load(quote?.dzImageUrl)
                .into(ivQuoteImage)
            txtQuote.text = quote?.text

            btnCopyQuote.setOnClickListener {
                copyQuoteToClipboard(quote?.text.toString())
            }

            btnClose.setOnClickListener { dismiss() }

            ivShareWhatsapp.setOnClickListener {
                quote?.dzImageUrl?.let { image ->
                    shareOnSocial(
                        image,
                        quote.sharePrefix + quote.articleUrl,
                        "com.whatsapp"
                    )
                }
            }
        }

        return view
    }

    private fun shareOnSocial(imageUrl: String, captionText: String, socialPackageName: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUrl))
        shareIntent.putExtra(Intent.EXTRA_TEXT, captionText)
        shareIntent.setPackage(socialPackageName)

        startActivity(shareIntent)
    }

    private fun copyQuoteToClipboard(text: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("quote", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Quote copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
