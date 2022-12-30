Простое приложение-галерея для просмотра изображений с диска.

Создал в процессе самостоятельного изучения Android и Kotlin.

https://user-images.githubusercontent.com/119162938/208895487-8827eab7-8260-4640-9c16-d50e29f7059e.mp4

Функционал:
- Добавление и удаление папок устройства
- Отображение изображений списком
- Отображение изображений на весь экран в карусели
- Пользователь может менять размерность списков

Что реализовано в приложении:
- Clean-архитектура с отдельными модулями (domain, data, presentation, app)
- Внедрение зависимостей с помощью Hilt
- Хранение данных с помощью Room database, Proto Datastore
- Пользовательский интерфейс с одной активностью, фрагментами, ViewModel'ами
- Асинхронная работа домена и источников данных
- Навигация и передача данных между фрагментами с помощью графов (Navigation)
- Использование элементов RecyclerView, MotionLayout
- Работа с изображениями через Glide и Coil

Какие есть проблемы реализации:
- Не занимался оптимизацией, кроме самых очевидных моментов
- Не использовал RxJava, только Flow + LiveData (хотя в принципе работает)
- Не нашел четких рекоммендаций по организации валидации данных в домене и бд, сделал по-своему
- Небольшие визуальные проблемы в работе карусели

Планы на будущее:
- Написать юнит-тесты
- Добавить работу с сетью
- Добавить взаимодействие с ОС ("открыть с помощью", "поделиться" и т.п.)
- Сделать альтернативную реализацию интерфейса в Jetpack Compose
