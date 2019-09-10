package com.example.mutiselectelistactivity

import android.animation.AnimatorInflater
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SelectableListAdapter(var list: ArrayList<MessageModel>) :
    RecyclerView.Adapter<SelectableListAdapter.SelectableListVH>() {

    init {
        setHasStableIds(true)
    }

    lateinit var tracker: SelectionTracker<Long>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectableListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_coversation_layout, parent, false)
        return SelectableListVH(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SelectableListVH, position: Int) {
        holder.bind(list[position], position, tracker.isSelected(position.toLong()), tracker)
        holder.profilePic.setOnClickListener {
            if (!tracker.isSelected(position.toLong())) {
                tracker.select(position.toLong())
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class SelectableListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView by lazy { itemView.findViewById<ImageView>(R.id.imageView) }
        val title: TextView by lazy { itemView.findViewById<TextView>(R.id.textView) }
        val content: TextView by lazy { itemView.findViewById<TextView>(R.id.textView2) }
        lateinit var itemDetail: MessageItemDetail

        fun bind(message: MessageModel, pos: Int, isSelected: Boolean, tracker: SelectionTracker<Long>) {
            itemDetail = MessageItemDetail(adapterPosition.toLong(), pos)

            title.text = message.title
            content.text = message.content

            itemView.isActivated = isSelected
            if (isSelected) {
                itemView.setBackgroundColor(Color.LTGRAY)
                Glide.with(itemView)
                    .load(R.drawable.ic_check_circle_black_24dp)
                    .into(profilePic)

                val anim = AnimatorInflater.loadAnimator(itemView.context, R.animator.flip_anim)
                anim.setTarget(profilePic)
                anim.start()
            } else {
                itemView.setBackgroundColor(Color.WHITE)
                Glide.with(itemView)
                    .load(message.imageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profilePic)
                if (isSelected) {
                    val anim = AnimatorInflater.loadAnimator(itemView.context, R.animator.flip_anim_reverse)
                    anim.setTarget(profilePic)
                    anim.start()
                }
            }
        }

        fun getMessageDetail(): MessageItemDetail {
            return itemDetail
        }
    }
}