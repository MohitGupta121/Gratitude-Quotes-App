package mohit.gratitude.assignment.ui.quotesScreen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_quotes.*
import mohit.gradtitude.assignment.R
import mohit.gradtitude.assignment.databinding.FragmentQuotesBinding
import mohit.gratitude.assignment.adapter.QuotesAdapter
import mohit.gratitude.assignment.util.DateUtil
import mohit.gratitude.assignment.util.Resource

private const val TAG = "QuotesFragment"

@AndroidEntryPoint
class QuotesFragment : Fragment(R.layout.fragment_quotes) {

    private val viewModel: QuotesViewModel by viewModels()
    var isLoading = false
    private val dateManager = DateUtil()
    private var currentQuotesDate = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentQuotesBinding.bind(view)
        val quoteAdapter = QuotesAdapter(parentFragmentManager)

        binding.apply {
            rvQuotes.apply {
                adapter = quoteAdapter
                setHasFixedSize(true)
            }

            quoteAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    rvQuotes.scrollToPosition(positionStart)
                }
            })

            btnPreviousDate.setOnClickListener {

                val updatedDate = dateManager.updateDateToPreviousDay()

                if (updatedDate != null) {
                    viewModel.getQuotes(updatedDate)
                    currentQuotesDate = updatedDate

                    txtDisplayDate.text = dateManager.formatDisplayDate(currentQuotesDate)

                    if (txtDisplayDate.text.toString() == "Today")
                        btnForwardDate.visibility = GONE
                    else
                        btnForwardDate.visibility = VISIBLE

                } else {
                    println("Invalid date or not within the last 7 days")
                }
            }

            btnForwardDate.setOnClickListener {

                val nextDate = dateManager.getNextDate(currentQuotesDate)

                viewModel.getQuotes(nextDate)
                currentQuotesDate = nextDate
                txtDisplayDate.text = dateManager.formatDisplayDate(currentQuotesDate)

                if (txtDisplayDate.text.toString() == "Today")
                    btnForwardDate.visibility = GONE
                else
                    btnForwardDate.visibility = VISIBLE
            }

        }

        viewModel.quotesData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { quotesData ->
                        quoteAdapter.submitList(quotesData)
                    }
                }
                is Resource.Error -> {
                    paginationProgressBar.visibility = View.INVISIBLE
                    isLoading = true
                    it.message?.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error: $message")
                    }
                }
                is Resource.Loading -> {
                    paginationProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }
}