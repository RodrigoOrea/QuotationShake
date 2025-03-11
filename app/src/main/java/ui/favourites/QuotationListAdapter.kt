// ui/favourites/QuotationListAdapter.kt
package ui.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dadm.roreizq.QuotationShake.databinding.QuotationItemBinding
import domain.model.Quotation

class QuotationListAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<Quotation, QuotationListAdapter.ViewHolder>(QuotationDiff) {

    inner class ViewHolder(
        private val binding: QuotationItemBinding,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(binding.tvQuotationAuthor.text.toString())
            }
        }

        fun bind(quotation: Quotation) {
            binding.tvQuotationText.text = quotation.text
            binding.tvQuotationAuthor.text = quotation.author.ifEmpty { "Anonymous" }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuotationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val QuotationDiff = object : DiffUtil.ItemCallback<Quotation>() {
            override fun areItemsTheSame(oldItem: Quotation, newItem: Quotation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Quotation, newItem: Quotation): Boolean {
                return oldItem == newItem
            }
        }
    }
}