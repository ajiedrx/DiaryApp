package com.project.diaryapp.presentation.diary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.diaryapp.R
import com.project.diaryapp.databinding.ItemDiaryBinding
import com.project.diaryapp.presentation.diary.model.Diary

class ArchivedDiaryAdapter(
    private val context: Context,
    private val onItemClicked: (Diary) -> Unit,
    private val onActionButtonClicked: (Diary) -> Unit
): RecyclerView.Adapter<ArchivedDiaryAdapter.ViewHolder>() {

    private val datas: MutableList<Diary> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = datas[position]
        holder.bind(context, data, onItemClicked, onActionButtonClicked)
    }

    class ViewHolder(private val binding: ItemDiaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, data: Diary, onItemClicked: (Diary) -> Unit, onActionButtonClicked: (Diary) -> Unit) {
            with(binding){
                btnAction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_unarchive))
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
        return datas.size
    }

    fun setData(diaryList: List<Diary>) {
        val diffCallback = DiaryDiffCallback(this.datas, diaryList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.datas.clear()
        this.datas.addAll(diaryList)
        diffResult.dispatchUpdatesTo(this)
    }

    class DiaryDiffCallback(private val oldList: List<Diary>, private val newList: List<Diary>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id === newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            val (_, value, name) = oldList[oldPosition]
            val (_, value1, name1) = newList[newPosition]

            return name == name1 && value == value1
        }

        @Nullable
        override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
            return super.getChangePayload(oldPosition, newPosition)
        }
    }
}