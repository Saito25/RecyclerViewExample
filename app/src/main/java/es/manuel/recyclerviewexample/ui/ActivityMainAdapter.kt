package es.manuel.recyclerviewexample.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.manuel.recyclerviewexample.databinding.ActivityMainRecyclerviewItemBinding
import es.manuel.recyclerviewexample.model.local.entity.Student

val StudentDiffCallback = object: DiffUtil.ItemCallback<Student>() {
    override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
        oldItem == newItem

}

class ActivityMainAdapter : ListAdapter<Student, ActivityMainAdapter.ViewHolder>(StudentDiffCallback) {

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ActivityMainRecyclerviewItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class ViewHolder(private val binding: ActivityMainRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(adapterPosition)
            }
        }

        fun bind(student: Student) {
            binding.run {
                txtRecyclerViewItemNameLabel.text = student.name
                txtRecyclerViewItemAgeLabel.text = student.age.toString()
            }
        }
    }

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}