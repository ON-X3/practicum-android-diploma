package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity
import ru.practicum.android.diploma.data.dto.AddressDTO
import ru.practicum.android.diploma.data.dto.AreaDTO
import ru.practicum.android.diploma.data.dto.ContactsDTO
import ru.practicum.android.diploma.data.dto.EmployerDTO
import ru.practicum.android.diploma.data.dto.IdNameDTO
import ru.practicum.android.diploma.data.dto.SalaryDTO
import ru.practicum.android.diploma.data.dto.VacancyDetailDTO
import ru.practicum.android.diploma.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancySalary


class VacancyDetailConverter {
    fun toEntity(vacancy: VacancyDetailDTO): VacancyDetailEntity {
        return VacancyDetailEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.description,
            url = vacancy.url,
            salary = vacancy.salary,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            contacts = vacancy.contacts,
            area = vacancy.area,
            employer = vacancy.employer,
            industry = vacancy.industry,
            skills = vacancy.skills
        )
    }

    fun toEntity(vacancy: VacancyDetail): VacancyDetailEntity =
        VacancyDetailEntity(
            id = vacancy.id,
            name = vacancy.name,
            description = vacancy.descriptionHtml,
            url = vacancy.sharingUrl,
            salaryFrom = vacancy.salary?.from,
            salaryTo = vacancy.salary?.to,
            salaryCurrency = vacancy.salary?.currency,
            address = vacancy.address,
            experience = vacancy.experience,
            schedule = vacancy.schedule,
            employment = vacancy.employment,
            contacts = null,
            employerName = vacancy.employer.name,
            employerLogo = vacancy.employer.logo,
            area = vacancy.areaName,
            industry = null,
            skills = vacancy.skills.joinToString(separator = "|"),
        )

    fun toDomain(entity: VacancyDetailEntity?): VacancyDetail? =
        entity?.let {
            VacancyDetail(
                id = it.id,
                name = it.name,
                descriptionHtml = it.description,
                salary = if (it.salaryFrom != null || it.salaryTo != null || it.salaryCurrency != null) {
                    VacancySalary(
                        from = it.salaryFrom,
                        to = it.salaryTo,
                        currency = it.salaryCurrency,
                    )
                } else {
                    null
                },
                address = it.address.orEmpty(),
                experience = it.experience.orEmpty(),
                schedule = it.schedule.orEmpty(),
                employment = it.employment.orEmpty(),
                employer = Employer(
                    name = it.employerName.orEmpty(),
                    logo = it.employerLogo.orEmpty(),
                ),
                areaName = it.area.orEmpty(),
                skills = it.skills
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    .orEmpty(),
                sharingUrl = it.url.orEmpty(),
                isFavorite = true,
            )
        }

    fun toDomain(response: VacancyDetailResponse): VacancyDetail =
        toDomain(response.items)

    fun toDomain(dto: VacancyDetailDTO): VacancyDetail {
        return VacancyDetail(
            id = dto.id,
            name = dto.name,
            descriptionHtml = dto.description.orEmpty(),
            salary = dto.salary.toDomain(),
            address = dto.address.raw(),
            experience = dto.experience.name(),
            schedule = dto.schedule.name(),
            employment = dto.employment.name(),
            employer = dto.employer.toDomain(),
            areaName = dto.area.name(),
            industryName = dto.industry.name(),
            contactName = dto.contacts.contactName(),
            contactEmail = dto.contacts.contactEmail(),
            contactPhones = dto.contacts.phones(),
            skills = dto.skills.orEmpty(),
            url = dto.url.orEmpty(),
            isFavorite = false
        )
    }

    fun toEntity(dto: VacancyDetailDTO): VacancyDetailEntity {
        return VacancyDetailEntity(
            id = dto.id,
            name = dto.name,
            descriptionHtml = dto.description.orEmpty(),
            salaryFrom = dto.salary?.from,
            salaryTo = dto.salary?.to,
            salaryCurrency = dto.salary?.currency,
            address = dto.address.raw(),
            experience = dto.experience.name(),
            schedule = dto.schedule.name(),
            employment = dto.employment.name(),
            employerName = dto.employer.name(),
            employerLogo = dto.employer.logo(),
            areaName = dto.area.name(),
            industryName = dto.industry.name(),
            contactName = dto.contacts.contactName(),
            contactEmail = dto.contacts.contactEmail(),
            contactPhones = dto.contacts.phones(),
            skills = dto.skills.orEmpty(),
            url = dto.url.orEmpty(),
            isFavorite = false
        )
    }

    fun toDomain(entity: VacancyDetailEntity): VacancyDetail {
        return VacancyDetail(
            id = entity.id,
            name = entity.name,
            descriptionHtml = entity.descriptionHtml,
            salary = entity.toDomainSalary(),
            address = entity.address,
            experience = entity.experience,
            schedule = entity.schedule,
            employment = entity.employment,
            employer = Employer(entity.employerName, entity.employerLogo),
            areaName = entity.areaName,
            industryName = entity.industryName,
            contactName = entity.contactName,
            contactEmail = entity.contactEmail,
            contactPhones = entity.contactPhones,
            skills = entity.skills,
            url = entity.url,
            isFavorite = entity.isFavorite
        )
    }

    private fun SalaryDTO?.toDomain(): VacancySalary? =
        this?.let { VacancySalary(it.from, it.to, it.currency) }

    private fun EmployerDTO?.toDomain(): Employer = Employer(
        name = this?.name.orEmpty(),
        logo = this?.logo.orEmpty()
    )

    private fun AddressDTO?.raw(): String = this?.raw.orEmpty()

    private fun IdNameDTO?.name(): String = this?.name.orEmpty()

    private fun AreaDTO?.name(): String = this?.name.orEmpty()

    private fun EmployerDTO?.name(): String = this?.name.orEmpty()

    private fun EmployerDTO?.logo(): String = this?.logo.orEmpty()

    private fun ContactsDTO?.contactName(): String = this?.name.orEmpty()

    private fun ContactsDTO?.contactEmail(): String = this?.email.orEmpty()

    private fun ContactsDTO?.phones(): List<String> =
        this?.phones.orEmpty().mapNotNull { it.formatted }

    private fun VacancyDetailEntity.toDomainSalary(): VacancySalary? {
        if (salaryFrom == null && salaryTo == null && salaryCurrency == null) return null
        return VacancySalary(salaryFrom, salaryTo, salaryCurrency)
    }
}
