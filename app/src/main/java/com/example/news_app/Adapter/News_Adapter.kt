package com.example.news_app.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.news_app.Model.Article
import com.example.news_app.R
import com.squareup.picasso.Picasso

class News_Adapter : RecyclerView.Adapter<News_Adapter.ArticleViewHolder>() {
     lateinit var list: List<Article>
    class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
      val imageview:ImageView=itemView.findViewById(R.id.image)
      val timetext:TextView=itemView.findViewById(R.id.time)
      val sourcetext:TextView=itemView.findViewById(R.id.source)
      val titletext:TextView=itemView.findViewById(R.id.title)
      val desctext:TextView=itemView.findViewById(R.id.description)
    }
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.source == newItem.source
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_news,parent,false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
       // val article=list[position]
        holder.itemView.apply {
          Picasso.get().load(article.urlToImage).into(holder.imageview)
         holder.timetext.text=article.publishedAt
          holder.sourcetext.text=article.source.name
          holder.titletext.text=article.title
          holder.desctext.text=article.description

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}