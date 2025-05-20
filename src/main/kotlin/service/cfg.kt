package com.example.service

import com.example.model.Vacancy
import java.io.File
import java.util.UUID


data class Specialist(
    val id: Int,
    val icon: String,
    val name: String,
    val serviceType: String,
    val serviceSubType: String,
    val rating: Float,
    val address: String,
    val experienceYears: Int,
    val needsEmployment: Boolean,
    val freeConsultation: Boolean,
    val workDays: String,
    val description: String,
    val email: String,
    val phone: String,
    val priceList: List<Pair<String, String>>,
    val tags: List<String> = emptyList()
)


    suspend fun seedSpecialists(service: VacancyService) {
        val specialists = listOf(
            Specialist(
                id = 1,
                icon =  "C:\\MIREA\\time\\laz.jpg",
                name = "Иван Иванов",
                serviceType = "Юрист",
                serviceSubType = "Трудовое право",
                rating = 4.8f,
                address = "Москва, ул. Нагорская",
                experienceYears = 5,
                needsEmployment = true,
                freeConsultation = true,
                workDays = "Пн–Пт",
                description = "Профессиональный юрист с опытом в трудовом праве...",
                email = "ivan@example.com",
                phone = "+7 999 123-45-67",
                priceList = listOf(
                    "Консультация по трудовым вопросам" to "4000 ₽",
                    "Поддержка при увольнении" to "7000 ₽"
                ),
                tags = listOf("Трудовые споры", "Увольнение")
            ),
            Specialist(
                id = 2,
                icon = "C:\\MIREA\\time\\ar.jpg",
                name = "Мария Смирнова",
                serviceType = "Адвокат",
                serviceSubType = "Семейное право",
                rating = 4.6f,
                address = "Санкт-Петербург, ул. Ленина",
                experienceYears = 7,
                needsEmployment = false,
                freeConsultation = true,
                workDays = "Пн–Сб",
                description = "Адвокат с опытом успешных бракоразводных процессов и дел об опеке.",
                email = "maria@example.com",
                phone = "+7 999 234-56-78",
                priceList = listOf(
                    "Развод и раздел имущества" to "8000 ₽",
                    "Опека над детьми" to "9000 ₽"
                ),
                tags = listOf("Развод", "Опека", "Алименты")
            ),
            Specialist(
                id = 3,
                icon = "C:\\MIREA\\time\\kr.jpg",
                name = "Алексей Кузнецов",
                serviceType = "Правовой консультант",
                serviceSubType = "Гражданское право",
                rating = 4.9f,
                address = "Казань, пр. Победы",
                experienceYears = 10,
                needsEmployment = true,
                freeConsultation = false,
                workDays = "Пн–Пт",
                description = "Консультации и юридическая помощь по вопросам гражданского права.",
                email = "alex@example.com",
                phone = "+7 999 345-67-89",
                priceList = listOf(
                    "Договорные споры" to "6000 ₽",
                    "Возмещение ущерба" to "7000 ₽"
                ),
                tags = listOf("Договоры", "Имущественные споры")
            ),
            Specialist(
                id = 4,
                icon = "C:\\MIREA\\time\\da.jpg",
                name = "Ольга Новикова",
                serviceType = "Адвокат по уголовным делам",
                serviceSubType = "Уголовное право",
                rating = 4.7f,
                address = "Новосибирск, Красный проспект",
                experienceYears = 8,
                needsEmployment = false,
                freeConsultation = true,
                workDays = "Пн–Сб",
                description = "Защита интересов клиентов на всех этапах уголовного процесса.",
                email = "olga@example.com",
                phone = "+7 999 456-78-90",
                priceList = listOf(
                    "Консультация по уголовным делам" to "5000 ₽",
                    "Защита в суде" to "15000 ₽"
                ),
                tags = listOf("Защита", "Уголовные дела")
            ),
            Specialist(
                id = 5,
                icon = "C:\\MIREA\\time\\art.jpg",
                name = "Дмитрий Соколов",
                serviceType = "Правозащитник",
                serviceSubType = "Конституционное право",
                rating = 4.5f,
                address = "Екатеринбург, ул. Малышева",
                experienceYears = 6,
                needsEmployment = true,
                freeConsultation = false,
                workDays = "Пн–Пт",
                description = "Эксперт в защите конституционных прав граждан.",
                email = "dmitry@example.com",
                phone = "+7 999 567-89-01",
                priceList = listOf(
                    "Защита прав граждан" to "7000 ₽",
                    "Обжалование решений" to "9000 ₽"
                ),
                tags = listOf("Права человека", "Свободы")
            ),
            Specialist(
                id = 6,
                icon = "C:\\MIREA\\time\\dar.jpg",
                name = "Анна Федорова",
                serviceType = "Юрист",
                serviceSubType = "Административное право",
                rating = 4.4f,
                address = "Ростов-на-Дону, ул. Пушкинская",
                experienceYears = 4,
                needsEmployment = true,
                freeConsultation = true,
                workDays = "Пн–Пт",
                description = "Опыт ведения административных дел и защиты интересов граждан.",
                email = "anna@example.com",
                phone = "+7 999 678-90-12",
                priceList = listOf(
                    "Оспаривание штрафов" to "3000 ₽",
                    "Защита в административных делах" to "5000 ₽"
                ),
                tags = listOf("Штрафы", "Административные споры")
            ),
            Specialist(
                id = 7,
                icon = "C:\\MIREA\\time\\kol.jpg",
                name = "Игорь Павлов",
                serviceType = "Экологический юрист",
                serviceSubType = "Экологическое право",
                rating = 4.3f,
                address = "Краснодар, ул. Красная",
                experienceYears = 5,
                needsEmployment = false,
                freeConsultation = true,
                workDays = "Пн–Сб",
                description = "Правовая поддержка по вопросам охраны окружающей среды.",
                email = "igor@example.com",
                phone = "+7 999 789-01-23",
                priceList = listOf(
                    "Консультация по экологии" to "4000 ₽",
                    "Представление в суде" to "10000 ₽"
                ),
                tags = listOf("Экология", "Окружающая среда")
            ),
            Specialist(
                id = 8,
                icon =  "C:\\MIREA\\time\\kab.jpg",
                name = "Екатерина Васильева",
                serviceType = "Адвокат",
                serviceSubType = "Семейное право",
                rating = 4.6f,
                address = "Воронеж, ул. Кирова",
                experienceYears = 9,
                needsEmployment = true,
                freeConsultation = false,
                workDays = "Пн–Пт",
                description = "Решение вопросов семейного права любой сложности.",
                email = "ekaterina@example.com",
                phone = "+7 999 890-12-34",
                priceList = listOf(
                    "Консультация по семейному праву" to "5000 ₽",
                    "Развод без суда" to "7000 ₽"
                ),
                tags = listOf("Развод", "Раздел имущества")
            ),
            Specialist(
                id = 9,
                icon = "C:\\MIREA\\time\\dim.jpg",
                name = "Сергей Морозов",
                serviceType = "Юрист",
                serviceSubType = "Гражданское право",
                rating = 4.7f,
                address = "Пермь, ул. Ленина",
                experienceYears = 11,
                needsEmployment = false,
                freeConsultation = true,
                workDays = "Пн–Сб",
                description = "Решение гражданско-правовых споров любой сложности.",
                email = "sergey@example.com",
                phone = "+7 999 901-23-45",
                priceList = listOf(
                    "Иски о взыскании долгов" to "6000 ₽",
                    "Наследственные споры" to "8000 ₽"
                ),
                tags = listOf("Наследство", "Долги")
            ),
            Specialist(
                id = 10,
                icon = "C:\\MIREA\\time\\in.jpg",
                name = "Наталья Киселева",
                serviceType = "Адвокат по уголовным делам",
                serviceSubType = "Уголовное право",
                rating = 4.8f,
                address = "Самара, Московское шоссе",
                experienceYears = 12,
                needsEmployment = true,
                freeConsultation = true,
                workDays = "Пн–Пт",
                description = "Защита интересов клиентов по уголовным делам.",
                email = "natalia@example.com",
                phone = "+7 999 012-34-56",
                priceList = listOf(
                    "Защита обвиняемого" to "15000 ₽",
                    "Адвокатская консультация" to "5000 ₽"
                ),
                tags = listOf("Защита", "Судебные процессы")
            )
        )

        val uploadsDir = File("uploads/icons").apply { mkdirs() }

        specialists.forEach { specialist ->
            // Копируем файл изображения в uploads/icons
            val sourceFile = File(specialist.icon)
            val fileExtension = sourceFile.extension
            val newFileName = "icon_${UUID.randomUUID()}.$fileExtension"
            val targetFile = File(uploadsDir, newFileName)
            sourceFile.copyTo(targetFile, overwrite = true)

            val vacancy = Vacancy(
                id = 0, // ВАЖНО! id = 0, потому что база сама его создаёт
                icon = "/icons/$newFileName", // Относительный путь для клиента
                name = specialist.name,
                serviceType = specialist.serviceType,
                serviceSubType = specialist.serviceSubType,
                rating = specialist.rating,
                address = specialist.address,
                experienceYears = specialist.experienceYears,
                needsEmployment = specialist.needsEmployment,
                freeConsultation = specialist.freeConsultation,
                workDays = specialist.workDays,
                description = specialist.description,
                email = specialist.email,
                phone = specialist.phone,
                priceList = specialist.priceList,
                tags = specialist.tags
            )

            service.createVacancy(vacancy)
        }
    }
