package ru.practicum.android.diploma.data.dto

data class VacancyDetailDTO(
    val id: String,
    val name: String,
    val description: String?,
    val salary: SalaryDTO?,
    val address: AddressDTO?,
    val experience: IdNameDTO?,
    val schedule: IdNameDTO?,
    val contacts: ContactsDTO?,
    val employment: IdNameDTO?,
    val employer: EmployerDTO?,
    val area: AreaDTO?,
    val skills: List<String>?,
    val url: String?,
    val industry: IdNameDTO
)

data class AddressDTO(
    val raw: String?
)

data class IdNameDTO(
    val id: String?,
    val name: String?
)

data class EmployerDTO(
    val id: String?,
    val name: String?,
    val logo: String?
)

data class ContactsDTO(
    val id: String?,
    val name: String?,
    val email: String?,
    val phones: List<PhoneDTO>?
)

data class PhoneDTO(
    val comment: String?,
    val formatted: String?
)
