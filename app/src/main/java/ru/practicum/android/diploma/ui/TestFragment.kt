package ru.practicum.android.diploma.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.description.text = HtmlFormatter(requireContext()).format(
            "<h2>Описание вакансии</h2><p>Ищем backend-разработчика в продуктовую команду, " +
                "которая отвечает за API, интеграции и устойчивость сервиса.</p><p>Ищем разработчика, способного " +
                "разбираться в существующем коде и предлагать улучшения архитектуры.</p><section><h3>Обязанности</h3>" +
                "<ul><li>Разрабатывать и поддерживать REST API для веб- и мобильных клиентов.</li><li>Оптимизировать " +
                "SQL-запросы и работу с данными.</li><li>Проектировать новые сервисы и рефакторить сложные участки " +
                "системы.</li><li>Участвовать в разборе инцидентов и техническом планировании.</li></ul></section>" +
                "<section><h3>Требования</h3><ul><li>Опыт коммерческой backend-разработки от 2 лет.</li><li>Уверенное" +
                " знание SQL, принципов работы HTTP и очередей сообщений.</li><li>Понимание тестирования, логирования" +
                " и мониторинга сервисов.</li><li>Опыт проектирования API и работы с Docker.</li></ul></section>" +
                "<section><h3>Условия</h3><ul><li>Продуктовые задачи с понятным влиянием на результат.</li>" +
                "<li>Удаленная работа или гибридный формат.</li><li>Технические обсуждения без избыточного" +
                " менеджмента.</li></ul></section>"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
