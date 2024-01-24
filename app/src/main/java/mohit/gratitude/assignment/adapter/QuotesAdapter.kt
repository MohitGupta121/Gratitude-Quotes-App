package mohit.gratitude.assignment.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mohit.gradtitude.assignment.databinding.ItemQuoteBinding
import mohit.gratitude.assignment.data.model.Quote
import mohit.gratitude.assignment.ui.quotesScreen.ShareBottomSheet

class QuotesAdapter(
    private val fragmentManager: FragmentManager,
) :
    ListAdapter<Quote, QuotesAdapter.QuoteViewHolder>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding =
            ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {

                ivShareBtn.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val quote = getItem(position)
                        val bottomSheetFragment = ShareBottomSheet.newInstance(quote)
                        bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
                    }
                }
            }
        }

        fun bind(quote: Quote) {
            binding.apply {
                Glide.with(itemView)
                    .load(quote.dzImageUrl)
                    .into(ivQuoteImage)
                tvThemeTitle.text = quote.themeTitle

                if (quote.articleUrl != "") {
                    ivReadArticleBtn.visibility = VISIBLE

                    ivReadArticleBtn.setOnClickListener {
                        root.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(quote.articleUrl)))
                    }

                }

            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }

    }
}