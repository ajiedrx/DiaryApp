package com.project.diaryapp.presentation.diary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.diaryapp.databinding.ItemDiaryBinding
import com.project.diaryapp.presentation.diary.model.Diary

class DiaryPagingAdapter(
    private val onItemClicked: (Diary) -> Unit,
    private val onActionButtonClicked: (Diary) -> Unit
): PagingDataAdapter<Diary, DiaryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data, onItemClicked, onActionButtonClicked)
        }
    }

    class ViewHolder(private val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Diary, onItemClicked: (Diary) -> Unit, onActionButtonClicked: (Diary) -> Unit) {
            with(binding){
                tvTitle.text = data.title
                tvContent.text = data.content
                btnAction.setOnClickListener {
                    onActionButtonClicked.invoke(data)
                }
            }
            binding.root.setOnClickListener {
                onItemClicked.invoke(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Diary>() {
            override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}