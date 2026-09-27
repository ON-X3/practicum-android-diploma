package ru.practicum.android.diploma.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.SkillItemBinding

class SkillViewHolder(private val binding: SkillItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(skill: String) {
        binding.skill.text = skill
    }

    companion object {
        fun from(parent: ViewGroup): SkillViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SkillItemBinding.inflate(inflater, parent, false)
            return SkillViewHolder(binding)
        }
    }
}
