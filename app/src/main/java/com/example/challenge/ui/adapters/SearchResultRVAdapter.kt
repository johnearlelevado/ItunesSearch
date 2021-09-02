package com.example.challenge.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challenge.R
import com.example.challenge.room.entities.ItunesResults
import com.example.challenge.ui.ResultClickListener

class SearchResultRVAdapter(private val resultClickListener: ResultClickListener?) :
    RecyclerView.Adapter<SearchResultRVAdapter.ViewHolder>() {
    private var resultModelList: List<ItunesResults>? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imgCover: ImageView
        var tvTrackerName: TextView
        var tvGenre: TextView
        var tvPrice: TextView
        fun bindData(resultModel: ItunesResults) {
            tvTrackerName.text = resultModel.trackName
            tvGenre.text = resultModel.primaryGenreName
            tvPrice.text = resultModel.currency + " " + resultModel.trackPrice
            Glide.with(imgCover.context)
                .load(resultModel.artworkUrl100)
                .placeholder(R.drawable.img_placeholder)
                .into(imgCover)
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION && resultClickListener != null) {
                resultClickListener.onResultItemClick(resultModelList!![pos])
            }
        }

        init {
            imgCover = itemView.findViewById(R.id.img_item)
            tvTrackerName = itemView.findViewById(R.id.tv_trackname)
            tvGenre = itemView.findViewById(R.id.tv_genre)
            tvPrice = itemView.findViewById(R.id.tv_price)
            itemView.setOnClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_search_result_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(resultModelList!![position])
    }

    override fun getItemCount(): Int {
        return if (resultModelList != null) resultModelList!!.size else 0
    }

    fun updateResults(resultModelList: List<ItunesResults>?) {
        this.resultModelList = resultModelList
        notifyDataSetChanged()
    }
}