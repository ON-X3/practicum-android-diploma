package ru.practicum.android.diploma.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SkillAdapter(): RecyclerView.Adapter<SkillViewHolder>() {

    private val skills: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): SkillViewHolder = SkillViewHolder.from(p0)

    override fun onBindViewHolder(p0: SkillViewHolder, p1: Int) {
        p0.bind(skills[p1])
    }

    override fun getItemCount(): Int {
        return skills.size
    }

    fun addSkills(skills: List<String>) {
        this.skills.clear()
        this.skills.addAll(skills)
        notifyDataSetChanged()
    }

}
